package io.mosip.kernel.masterdata.config;

import java.time.LocalDateTime;

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

	@Autowired
	private CacheManagementService cacheManagementService;
	
	/**
	 * Scheduler Remove's the entire cache based on the cron time
	 */
	@Scheduled(cron = "${scheduling.job.cron}")
	public void clearCacheSchedule() {
		System.out.println("Time now: " + LocalDateTime.now());
		System.out.println("Calling scheduller");
		cacheManagementService.clearCache();
	}
	
}
