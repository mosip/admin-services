package io.mosip.kernel.syncdata.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SyncJobHelperService {

    private static final Logger logger = LoggerFactory.getLogger(SyncJobHelperService.class);

    @Value("${mosip.syncdata.regclient.module.id:10002}")
    private String regClientModuleId;

    @Value("${syncdata.cache.evict.delta-sync.cron:0 0/15 * * * *}")
    private String deltaCacheEvictCron;

    @Value("${syncdata.cache.snapshot.cron:0 0 23 * * *}")
    private String snapshotCron;

    @Autowired
    private CacheManager cacheManager;

    //By default, to trigger every 15th minute
    @Scheduled(cron = "${syncdata.cache.evict.delta-sync.cron}", zone = "UTC")
    public void evictDeltaCaches() {
        logger.info("Eviction of all keys from delta-sync cache started");
        cacheManager.getCache("delta-sync").clear();
        logger.info("Eviction of all keys from delta-sync cache completed");
    }


    @Scheduled(cron = "${syncdata.cache.snapshot.cron}", zone = "UTC")
    public void clearCacheAndRecreateSnapshot() {
        logger.info("Eviction of all keys from initial-sync cache started");
        cacheManager.getCache("initial-sync").clear();
        logger.info("Eviction of all keys from initial-sync cache Completed");
    }

    public LocalDateTime getFullSyncCurrentTimestamp() {
        return LocalDate.now(ZoneOffset.UTC).atTime(0,0,0,0);
    }

    public LocalDateTime getDeltaSyncCurrentTimestamp() {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(deltaCacheEvictCron, TimeZone.getTimeZone(ZoneOffset.UTC));
        Date nextTrigger1 = cronSequenceGenerator.next(new Date());
        Date nextTrigger2 = cronSequenceGenerator.next(nextTrigger1);

        long minutes = ChronoUnit.MINUTES.between(nextTrigger1.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(),
                nextTrigger2.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());

        LocalDateTime previousTrigger = nextTrigger1.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime().minus(minutes,
                ChronoUnit.MINUTES);

        logger.debug("Identified previous trigger : {}", previousTrigger);
        return previousTrigger;
    }


}
