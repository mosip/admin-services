package io.mosip.hotlist.config.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.repository.HotlistRepository;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;

/**
 * The Class HotlistScheduledCleanupJob.
 *
 * @author Manoj SP
 */
@Component
public class HotlistScheduledCleanupJob {

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistScheduledCleanupJob.class);

	/** The hotlist repo. */
	@Autowired
	private HotlistRepository hotlistRepo;

	/**
	 * Cleanup unblocked ids.
	 */
	@Scheduled(fixedDelayString = "#{60 * 60 * 1000 * ${mosip.hotlist.cleanup-schedule.fixed-delay-in-hours}}")
	public void cleanupUnblockedIds() {
		try {
			mosipLogger.info(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupUnblockedIds",
					"INITIATED CLEANUP OF UNBLOCKED IDs");
			hotlistRepo.findByStatusAndExpiryDTimesAndIsDeleted(HotlistStatus.UNBLOCKED, null, false).forEach(hotlistRepo::delete);
		} catch (Exception e) {
			mosipLogger.warn(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupUnblockedIds",
					"HOTLIST STATUS CLEANUP FAILED WITH EXCEPTION - " + ExceptionUtils.getStackTrace(e));
		}

	}

	/**
	 * Cleanup expired ids.
	 */
	@Scheduled(fixedDelayString = "#{60 * 60 * 1000 * ${mosip.hotlist.cleanup-schedule.fixed-delay-in-hours}}")
	public void cleanupExpiredIds() {
		try {
			mosipLogger.info(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupExpiredIds",
					"INITIATED CLEANUP OF EXPIRED IDs");
			hotlistRepo.findByExpiryTimestampLessThanAndStatusAndIsDeleted(DateUtils.getUTCCurrentDateTime(),
					HotlistStatus.UNBLOCKED, false).forEach(hotlistRepo::delete);
		} catch (Exception e) {
			mosipLogger.warn(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupUnblockedIds",
					"HOTLIST STATUS CLEANUP FAILED WITH EXCEPTION - " + ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Cleanup deleted ids.
	 */
	@Scheduled(fixedDelayString = "#{60 * 60 * 1000 * ${mosip.hotlist.cleanup-schedule.fixed-delay-in-hours}}")
	public void cleanupDeletedIds() {
		try {
			mosipLogger.info(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupDeletedIds",
					"INITIATED CLEANUP OF EXPIRED IDs");
			hotlistRepo.findByIsDeleted(true).forEach(hotlistRepo::delete);
		} catch (Exception e) {
			mosipLogger.warn(HotlistSecurityManager.getUser(), "HotlistScheduledCleanupJob", "cleanupDeletedIds",
					"HOTLIST STATUS CLEANUP FAILED WITH EXCEPTION - " + ExceptionUtils.getStackTrace(e));
		}
	}
}
