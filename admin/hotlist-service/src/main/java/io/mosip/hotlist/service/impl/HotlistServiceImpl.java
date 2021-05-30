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
			String status = getStatus(HotlistStatus.BLOCKED, blockRequest.getExpiryTimestamp());
			Optional<Hotlist> hotlistedOptionalData = hotlistRepo.findByIdHashAndIdTypeAndIsDeleted(idHash,
					blockRequest.getIdType(), false);
			if (hotlistedOptionalData.isPresent()) {
				this.updateHotlist(blockRequest, idHash, status, hotlistedOptionalData);
			} else {
				Hotlist hotlist = new Hotlist();
				buildHotlistEntity(blockRequest, idHash, status, hotlist);
				hotlist.setCreatedBy(HotlistSecurityManager.getUser());
				hotlist.setCreatedDateTime(DateUtils.getUTCCurrentDateTime());
				hotlist.setIsDeleted(false);
				hotlistHRepo.save(mapper.convertValue(hotlist, HotlistHistory.class));
				hotlistRepo.save(hotlist);
				eventHandler.publishEvent(idHash, blockRequest.getIdType(), status, hotlist.getExpiryTimestamp());
			}
			return buildResponse(blockRequest.getId(), null, HotlistStatus.BLOCKED, null);
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
				return buildResponse(hotlistedData.getIdValue(), hotlistedData.getIdType(),
						Objects.isNull(hotlistedData.getExpiryTimestamp()) ? HotlistStatus.BLOCKED
								: hotlistedData.getExpiryTimestamp().isAfter(DateUtils.getUTCCurrentDateTime())
										? HotlistStatus.BLOCKED
										: HotlistStatus.UNBLOCKED,
						hotlistedData.getExpiryTimestamp());
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
			if (hotlistedOptionalData.isPresent()) {
				unblockRequest.setExpiryTimestamp(DateUtils.getUTCCurrentDateTime());
				updateHotlist(unblockRequest, idHash, HotlistStatus.UNBLOCKED, hotlistedOptionalData);
			}
			return buildResponse(unblockRequest.getId(), null, HotlistStatus.UNBLOCKED, null);
		} catch (DataAccessException | TransactionException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_SERVICE_IMPL, "unblock", e.getMessage());
			throw new HotlistAppException(HotlistErrorConstants.DATABASE_ACCESS_ERROR, e);
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

	/**
	 * Gets the status.
	 *
	 * @param status          the status
	 * @param expiryTimestamp the expiry timestamp
	 * @return the status
	 */
	private String getStatus(String status, LocalDateTime expiryTimestamp) {
		return Objects.isNull(expiryTimestamp) ? status
				: expiryTimestamp.isAfter(DateUtils.getUTCCurrentDateTime()) ? HotlistStatus.BLOCKED : HotlistStatus.UNBLOCKED;
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
