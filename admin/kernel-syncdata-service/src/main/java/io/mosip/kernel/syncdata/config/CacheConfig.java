package io.mosip.kernel.syncdata.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import io.mosip.kernel.syncdata.constant.SyncDataConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    /** Cache TTL in minutes (configurable). */
    @Value("${mosip.syncdata.cache.expire-after-write-minutes:30}")
    private long cacheTtlMinutes;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(SyncDataConstant.CACHE_NAME_SYNC_DATA);
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTtlMinutes, TimeUnit.MINUTES) // TTL from config
                .maximumSize(1000); // Optional: Limit cache size to prevent memory issues
    }
}