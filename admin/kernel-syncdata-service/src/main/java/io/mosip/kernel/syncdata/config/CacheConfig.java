package io.mosip.kernel.syncdata.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import io.mosip.kernel.syncdata.constant.SyncDataConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /** Logger instance for logging events and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    /** Cache TTL in minutes (configurable). */
    @Value("${mosip.syncdata.cache.expire-after-write-minutes:30}")
    private long cacheTtlMinutes;

    @Value("${mosip.syncdata.cache.refresh-after-write-minutes:25}")
    private long refreshAfterMinutes;

    @Value("${mosip.syncdata.cache.maximum-size:100000}")
    private long maxSize;

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(Arrays.asList(SyncDataConstant.CACHE_NAME_SYNC_DATA,
                SyncDataConstant.CACHE_NAME_INITIAL_SYNC, SyncDataConstant.CACHE_NAME_DELTA_SYNC)); // Add initial-sync and delta-sync cache
        cacheManager.setCaffeine(caffeineCacheBuilder());
        // Enable async cache loading for better performance
        cacheManager.setAsyncCacheMode(true);
        LOGGER.info("cacheManager Started");
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTtlMinutes, TimeUnit.MINUTES) // UNCOMMENTED: TTL from config
                //.refreshAfterWrite(refreshAfterMinutes, TimeUnit.MINUTES) // Background refresh
                //.maximumSize(maxSize) // UNCOMMENTED: Limit cache size
                .recordStats() // Enable cache statistics for monitoring
                .removalListener((key, value, cause) -> {
                    LOGGER.debug("Cache entry removed - Key: {}, Cause: {}", key, cause);
                });
    }
}