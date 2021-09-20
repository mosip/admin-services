package io.mosip.kernel.syncdata.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.cache.CacheManager;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SyncJobHelperService {

    private static final Logger logger = LoggerFactory.getLogger(SyncJobHelperService.class);

    @Autowired
    private CacheManager cacheManager;

    //By default, set to 24 hours
    @Scheduled(fixedDelayString = "${syncdata.cache.evict.delay.millis:86400000}",
            initialDelayString = "${syncdata.cache.evict.delay-on-startup:86400000}")
    public void evictAllCaches() {
        logger.info("Eviction of all caches started");
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        logger.info("Eviction of all caches completed");
    }

}
