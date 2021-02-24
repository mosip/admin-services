package io.mosip.hotlist.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.constant.HotlistStatus;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.entity.Hotlist;
import io.mosip.hotlist.entity.HotlistHistory;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.repository.HotlistHistoryRepository;
import io.mosip.hotlist.repository.HotlistRepository;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.hotlist.service.HotlistService;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

/**
 * The Class HotlistServiceImpl.
 *
 * @author Manoj SP
 */
@Service
@Transactional
public class HotlistServiceImpl implements HotlistService {
	
	/** The Constant PUBLISH_EVENT. */
	private static final String PUBLISH_EVENT = "publishEvent";

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistServiceImpl.class);

	/** The topic. */
	@Value("${mosip.hotlist.topic-to-publish}")
	private String topic;

	/** The web sub hub url. */
	@Value("${websub.publish.url}")
	private String webSubHubUrl;

	/** The app id. */
	@Value("${spring.application.name:HOTLIST}")
	private String appId;

	/** The hotlist repo. */
	@Autowired
	private HotlistRepository hotlistRepo;

	/** The hotlist H repo. */
	@Autowired
	private HotlistHistoryRepository hotlistHRepo;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/** The publisher. */
	@Autowired
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	/**
	 * Block.
	 *
	 * @param blockRequest the block request
	 * @return the hotlist request response DTO
	 * @throws HotlistAppException the hotlist app exception
	 */
	@Override
	public HotlistRequestResponseDTO block(HotlistRequestResponseDTO blockRequest) throws HotlistAppException {
		try {
			String idHash = HotlistSecurityManager.hash(blockRequest.getId().getBytes());
			if (hotlistRepo.existsByIdHashAndIdType(idHash, blockRequest.getIdType())) {
				Optional<Hotlist> hotlistedOptionalData = hotlistRepo.findByIdHashAndIdType(idHash,
						blockRequest.getIdType());
				if (hotlistedOptionalData.isPresent()
						&& hotlistedOptionalData.get().getExpiryTimestamp().isBefore(DateUtils.getUTCCurrentDateTime())) {
					hotlistRepo.delete(hotlistedOptionalData.get());
					this.publishEvent(idHash, blockRequest.getIdType(), HotlistStatus.UNBLOCKED, null);
					return this.block(blockRequest);
				}
				mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "block", "RECORD ALREADY EXISTS");
				throw new HotlistAppException(HotlistErrorConstants.RECORD_EXISTS);
			} else {
				String status = getStatus(blockRequest.getStatus(), blockRequest.getExpiryTimestamp());
				Hotlist hotlist = new Hotlist();
				hotlist.setIdHash(idHash);
				hotlist.setIdValue(blockRequest.getId());
				hotlist.setIdType(blockRequest.getIdType());
				hotlist.setStatus(status);
				hotlist.setStartTimestamp(DateUtils.getUTCCurrentDateTime());
				hotlist.setExpiryTimestamp(
						Objects.nonNull(blockRequest.getExpiryTimestamp()) ? blockRequest.getExpiryTimestamp()
								: LocalDateTime.MAX.withYear(9999));
				hotlistHRepo.save(mapper.convertValue(hotlist, HotlistHistory.class));
				hotlistRepo.save(hotlist);
				this.publishEvent(idHash, blockRequest.getIdType(), status, hotlist.getExpiryTimestamp());
				return buildResponse(hotlist.getIdValue(), null, hotlist.getStatus(), null);
			}
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "block", e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	/**
	 * Retrieve hotlist.
	 *
	 * @param id the id
	 * @param idType the id type
	 * @return the hotlist request response DTO
	 * @throws HotlistAppException the hotlist app exception
	 */
	@Override
	public HotlistRequestResponseDTO retrieveHotlist(String id, String idType) throws HotlistAppException {
		try {
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo
					.findByIdHashAndIdType(HotlistSecurityManager.hash(id.getBytes()), idType);
			if (hotlistedOptionalData.isPresent()) {
				Hotlist hotlistedData = hotlistedOptionalData.get();
				return buildResponse(hotlistedData.getIdValue(), hotlistedData.getIdType(),
						hotlistedData.getExpiryTimestamp().isAfter(DateUtils.getUTCCurrentDateTime())
								? HotlistStatus.BLOCKED
								: HotlistStatus.UNBLOCKED,
						hotlistedData.getExpiryTimestamp());
			} else {
				return buildResponse(id, idType, HotlistStatus.UNBLOCKED, null);
			}
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "retrieveHotlist", e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	/**
	 * Update hotlist.
	 *
	 * @param updateRequest the update request
	 * @return the hotlist request response DTO
	 * @throws HotlistAppException the hotlist app exception
	 */
	@Override
	public HotlistRequestResponseDTO updateHotlist(HotlistRequestResponseDTO updateRequest) throws HotlistAppException {
		try {
			String idHash = HotlistSecurityManager.hash(updateRequest.getId().getBytes());
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo.findByIdHashAndIdType(idHash,
					updateRequest.getIdType());
			if (hotlistedOptionalData.isPresent()) {
				String status = getStatus(updateRequest.getStatus(), updateRequest.getExpiryTimestamp());
				Hotlist hotlist = hotlistedOptionalData.get();
				hotlist.setIdHash(idHash);
				hotlist.setIdValue(updateRequest.getId());
				hotlist.setIdType(updateRequest.getIdType());
				hotlist.setStatus(status);
				hotlist.setStartTimestamp(DateUtils.getUTCCurrentDateTime());
				hotlist.setExpiryTimestamp(
						Objects.nonNull(updateRequest.getExpiryTimestamp()) ? updateRequest.getExpiryTimestamp()
								: LocalDateTime.MAX.withYear(9999));
				hotlistHRepo.save(mapper.convertValue(hotlist, HotlistHistory.class));
				if (updateRequest.getStatus().contentEquals(HotlistStatus.UNBLOCKED)) {
					hotlistRepo.delete(hotlist);
					this.publishEvent(idHash, updateRequest.getIdType(), HotlistStatus.UNBLOCKED, null);
				} else {
					hotlistRepo.save(hotlist);
					this.publishEvent(idHash, updateRequest.getIdType(), status, hotlist.getExpiryTimestamp());
				}
				return buildResponse(hotlist.getIdValue(), null, hotlist.getStatus(), null);
			} else {
				mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "updateHotlist", "NO RECORDS FOUND TO UPDATE");
				throw new HotlistAppException(HotlistErrorConstants.NO_RECORD_FOUND);
			}
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "updateHotlist", e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	/**
	 * Gets the status.
	 *
	 * @param status the status
	 * @param expiryTimestamp the expiry timestamp
	 * @return the status
	 */
	private String getStatus(String status, LocalDateTime expiryTimestamp) {
		return Objects.isNull(status) ? HotlistStatus.BLOCKED
				: Objects.isNull(expiryTimestamp) ? HotlistStatus.BLOCKED
						: expiryTimestamp.isAfter(DateUtils.getUTCCurrentDateTime()) ? HotlistStatus.BLOCKED
								: HotlistStatus.UNBLOCKED;
	}

	/**
	 * Builds the response.
	 *
	 * @param id the id
	 * @param idType the id type
	 * @param status the status
	 * @param expiryTimestamp the expiry timestamp
	 * @return the hotlist request response DTO
	 */
	private HotlistRequestResponseDTO buildResponse(String id, String idType, String status,
			LocalDateTime expiryTimestamp) {
		HotlistRequestResponseDTO response = new HotlistRequestResponseDTO();
		response.setId(id);
		response.setIdType(idType);
		response.setStatus(status);
		response.setExpiryTimestamp(expiryTimestamp);
		return response;
	}

	/**
	 * Publish event.
	 *
	 * @param id the id
	 * @param idType the id type
	 * @param status the status
	 * @param expiryTimestamp the expiry timestamp
	 */
	private void publishEvent(String id, String idType, String status, LocalDateTime expiryTimestamp) {
		EventModel payload = new EventModel();
		payload.setPublisher(appId);
		payload.setTopic(topic);
		payload.setPublishedOn(DateUtils.getCurrentDateTimeString());
		Event event = new Event();
		Map<String, Object> data = new HashMap<>();
		data.put("id", id);
		data.put("idType", idType);
		data.put("status", status);
		data.put("expiryTimestamp", DateUtils.formatToISOString(expiryTimestamp));
		event.setData(data);
		payload.setEvent(event);
		mosipLogger.debug(HotlistSecurityManager.getUser(), "HotlistServiceImpl", PUBLISH_EVENT, "PUBLISHING EVENT - " + payload.toString());
		publisher.publishUpdate(topic, payload, MediaType.APPLICATION_JSON_VALUE, null, webSubHubUrl);
	}

}
