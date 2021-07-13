package io.mosip.hotlist.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.HotlistRequestResponseDTO;
import io.mosip.hotlist.entity.Hotlist;
import io.mosip.hotlist.entity.HotlistHistory;
import io.mosip.hotlist.event.HotlistEventHandler;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.repository.HotlistHistoryRepository;
import io.mosip.hotlist.repository.HotlistRepository;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.hotlist.service.HotlistService;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;

/**
 * The Class HotlistServiceImpl.
 *
 * @author Manoj SP
 */
@Service
@Transactional
public class HotlistServiceImpl implements HotlistService {

	/** The Constant RETRIEVE_HOTLIST. */
	private static final String RETRIEVE_HOTLIST = "retrieveHotlist";

	/** The Constant BLOCK. */
	private static final String BLOCK = "block";

	/** The Constant HOTLIST_SERVICE_IMPL. */
	private static final String HOTLIST_SERVICE_IMPL = "HotlistServiceImpl";

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

	/** The event handler. */
	@Autowired
	private HotlistEventHandler eventHandler;

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
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(idHash,
					blockRequest.getIdType(), false);
			String dbStatus = HotlistStatus.UNBLOCKED;
			String requestedStatus = HotlistStatus.BLOCKED;
			LocalDateTime expiryTimestamp = blockRequest.getExpiryTimestamp();
			if (hotlistedOptionalData.isPresent()) {
				updateStatus(blockRequest, idHash, hotlistedOptionalData, dbStatus, requestedStatus);
			} else {
				String status = Objects.nonNull(expiryTimestamp) ? dbStatus : requestedStatus;
				Hotlist hotlist = new Hotlist();
				buildHotlistEntity(blockRequest, idHash, status, hotlist);
				hotlist.setCreatedBy(HotlistSecurityManager.getUser());
				hotlist.setCreatedDateTime(DateUtils.getUTCCurrentDateTime());
				hotlist.setIsDeleted(false);
				hotlistHRepo.save(mapper.convertValue(hotlist, HotlistHistory.class));
				hotlistRepo.save(hotlist);
				eventHandler.publishEvent(idHash, blockRequest.getIdType(), status, hotlist.getExpiryTimestamp());
			}
			return buildResponse(blockRequest.getId(), null, requestedStatus, isExpired(expiryTimestamp));
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE_IMPL, BLOCK, e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	/**
	 * Retrieve hotlist.
	 *
	 * @param id     the id
	 * @param idType the id type
	 * @return the hotlist request response DTO
	 * @throws HotlistAppException the hotlist app exception
	 */
	@Override
	public HotlistRequestResponseDTO retrieveHotlist(String id, String idType) throws HotlistAppException {
		try {
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo
					.findByIdHashAndIdTypeAndIsDeleted(HotlistSecurityManager.hash(id.getBytes()), idType, false);
			if (hotlistedOptionalData.isPresent()) {
				Hotlist hotlistedData = hotlistedOptionalData.get();
				String status = hotlistedData.getStatus();
				if (Objects.nonNull(isExpired(hotlistedData.getExpiryTimestamp()))) {
					switch (status) {
					case HotlistStatus.BLOCKED:
						status = HotlistStatus.UNBLOCKED;
						break;
					case HotlistStatus.UNBLOCKED:
						status = HotlistStatus.BLOCKED;
						break;
					}
					return buildResponse(id, idType, status, hotlistedData.getExpiryTimestamp());
				}
				return buildResponse(id, idType, status, null);
			} else {
				return buildResponse(id, idType, HotlistStatus.UNBLOCKED, null);
			}
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE_IMPL, RETRIEVE_HOTLIST, e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	/**
	 * Update hotlist.
	 *
	 * @param unblockRequest the update request
	 * @return the hotlist request response DTO
	 * @throws HotlistAppException the hotlist app exception
	 */
	@Override
	public HotlistRequestResponseDTO unblock(HotlistRequestResponseDTO unblockRequest) throws HotlistAppException {
		try {
			String idHash = HotlistSecurityManager.hash(unblockRequest.getId().getBytes());
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(idHash,
					unblockRequest.getIdType(), false);
			String dbStatus = HotlistStatus.BLOCKED;
			String requestedStatus = HotlistStatus.UNBLOCKED;
			if (hotlistedOptionalData.isPresent()) {
				updateStatus(unblockRequest, idHash, hotlistedOptionalData, dbStatus, requestedStatus);
			}
			return buildResponse(unblockRequest.getId(), null, requestedStatus, isExpired(unblockRequest.getExpiryTimestamp()));
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE_IMPL, "unblock", e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
		}
	}

	private void updateStatus(HotlistRequestResponseDTO request, String idHash, Optional<Hotlist> hotlistedOptionalData,
			String dbStatus, String requestedStatus) {
		if (hotlistedOptionalData.get().getStatus().contentEquals(dbStatus)) {
			updateHotlist(request, idHash, Objects.nonNull(request.getExpiryTimestamp()) ? dbStatus : requestedStatus,
					hotlistedOptionalData);
		} else {
			request.setExpiryTimestamp(null);
			updateHotlist(request, idHash, requestedStatus, hotlistedOptionalData);
		}
	}

	/**
	 * Update hotlist.
	 *
	 * @param updateRequest         the update request
	 * @param idHash                the id hash
	 * @param status                the status
	 * @param hotlistedOptionalData the hotlisted optional data
	 * @return the hotlist request response DTO
	 */
	private HotlistRequestResponseDTO updateHotlist(HotlistRequestResponseDTO updateRequest, String idHash, String status,
			Optional<Hotlist> hotlistedOptionalData) {
		Hotlist hotlist = hotlistedOptionalData.get();
		buildHotlistEntity(updateRequest, idHash, status, hotlist);
		hotlist.setUpdatedBy(HotlistSecurityManager.getUser());
		hotlist.setUpdatedDateTime(DateUtils.getUTCCurrentDateTime());
		hotlistHRepo.save(mapper.convertValue(hotlist, HotlistHistory.class));
		hotlistRepo.save(hotlist);
		eventHandler.publishEvent(idHash, updateRequest.getIdType(), status, hotlist.getExpiryTimestamp());
		return buildResponse(hotlist.getIdValue(), null, updateRequest.getStatus(), null);
	}

	/**
	 * Builds the hotlist entity.
	 *
	 * @param request the request
	 * @param idHash  the id hash
	 * @param status  the status
	 * @param hotlist the hotlist
	 */
	private void buildHotlistEntity(HotlistRequestResponseDTO request, String idHash, String status, Hotlist hotlist) {
		hotlist.setIdHash(idHash);
		hotlist.setIdValue(request.getId());
		hotlist.setIdType(request.getIdType());
		hotlist.setStatus(status);
		hotlist.setStartTimestamp(DateUtils.getUTCCurrentDateTime());
		hotlist.setExpiryTimestamp(Objects.nonNull(request.getExpiryTimestamp()) ? request.getExpiryTimestamp() : null);
	}

	private LocalDateTime isExpired(LocalDateTime expiryTimestamp) {
		return Objects.nonNull(expiryTimestamp) && expiryTimestamp.isAfter(DateUtils.getUTCCurrentDateTime()) ? expiryTimestamp
				: null;
	}

	/**
	 * Builds the response.
	 *
	 * @param id              the id
	 * @param idType          the id type
	 * @param status          the status
	 * @param expiryTimestamp the expiry timestamp
	 * @return the hotlist request response DTO
	 */
	private HotlistRequestResponseDTO buildResponse(String id, String idType, String status, LocalDateTime expiryTimestamp) {
		HotlistRequestResponseDTO response = new HotlistRequestResponseDTO();
		response.setId(id);
		response.setIdType(idType);
		response.setStatus(status);
		response.setExpiryTimestamp(expiryTimestamp);
		return response;
	}

}