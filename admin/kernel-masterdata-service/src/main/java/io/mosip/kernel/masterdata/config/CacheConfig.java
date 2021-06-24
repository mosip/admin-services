package io.mosip.kernel.masterdata.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.mosip.kernel.masterdata.service.CacheManagementService;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
	
	private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

	@Autowired
	private CacheManagementService cacheManagementService;
	
	/**
	 * Scheduler Remove's the entire cache based on the cron time
	 */
	@Scheduled(cron = "${scheduling.job.cron}")
	public void clearCacheSchedule() {
		log.info("Cache evict scheduler started");
		cacheManagementService.clearCache();
	}
	
}
