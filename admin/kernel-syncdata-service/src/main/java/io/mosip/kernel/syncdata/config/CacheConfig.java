package io.mosip.kernel.syncdata.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.mosip.kernel.syncdata.service.impl.SyncConfigDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import io.mosip.kernel.syncdata.constant.SyncDataConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableCaching
public class CacheConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    /** Cache TTL in minutes (configurable). */
    @Value("${mosip.syncdata.cache.expire-after-write-minutes:30}")
    private long cacheTtlMinutes;

    @Value("${mosip.syncdata.cache.maximum-size:1000000}")
    private long maxSize;

    @Bean
    public CacheManager cacheManager() {
        LOGGER.info("cacheManager entry: ");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(Arrays.asList(SyncDataConstant.CACHE_NAME_SYNC_DATA, SyncDataConstant.CACHE_NAME_INITIAL_SYNC)); // Add initial-sync cache
        cacheManager.setCaffeine(caffeineCacheBuilder());
        LOGGER.info("cacheManager exit: {}",cacheManager);
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTtlMinutes, TimeUnit.MINUTES) // TTL from config
                .maximumSize(maxSize); // Optional: Limit cache size to prevent memory issues
    }
}