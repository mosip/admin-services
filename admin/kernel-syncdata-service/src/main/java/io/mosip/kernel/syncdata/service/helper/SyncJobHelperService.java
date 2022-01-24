package io.mosip.kernel.syncdata.service.helper;

import io.mosip.kernel.core.exception.IOException;
import io.mosip.kernel.core.util.FileUtils;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SyncJobHelperService {

    private static final Logger logger = LoggerFactory.getLogger(SyncJobHelperService.class);

    @Value("${mosip.syncdata.clientsettings.data.dir:./_SNAPSHOTS}")
    private String clientSettingsDir;

    @Value("${mosip.syncdata.regclient.module.id:10002}")
    private String regClientModuleId;

    @Value("${syncdata.cache.evict.delta-sync.cron:0 0/15 * * * *}")
    private String deltaCacheEvictCron;

    @Value("${syncdata.cache.snapshot.cron:0 0 23 * * *}")
    private String snapshotCron;

    @Autowired
    private SyncMasterDataServiceHelper serviceHelper;

    @Autowired
    private MapperUtils mapper;

    @Autowired
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate selfTokenRestTemplate;

    @Autowired
    private CacheManager cacheManager;


    //By default, to trigger every hour
    @Scheduled(cron = "${syncdata.cache.evict.delta-sync.cron}", zone = "UTC")
	public void evictDeltaCaches() {
		logger.info("Eviction of all keys from delta-sync cache started");
		if (null != cacheManager.getCache("delta-sync"))
			cacheManager.getCache("delta-sync").clear();
		logger.info("Eviction of all keys from delta-sync cache completed");
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

    @Scheduled(cron = "${syncdata.cache.snapshot.cron}", zone = "UTC")
    public void clearCacheAndRecreateSnapshot() {
        logger.info("Eviction of all keys from initial-sync cache started");
        if(null!=cacheManager.getCache("initial-sync"))
        	cacheManager.getCache("initial-sync").clear();
        logger.info("Eviction of all keys from initial-sync cache Completed");

        createEntitySnapshot();
    }



    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 0)
    public void createEntitySnapshot() {

        LocalDateTime currentTimestamp = getFullSyncCurrentTimestamp();
        logger.info("Create snapshot scheduled job started : {}", currentTimestamp);

        Map<Class, CompletableFuture> futuresMap = new HashMap<>();
        futuresMap.put(AppAuthenticationMethod.class, serviceHelper.getAppAuthenticationMethodDetails(null, currentTimestamp));
        futuresMap.put(AppRolePriority.class, serviceHelper.getAppRolePriorityDetails(null, currentTimestamp));
        futuresMap.put(Template.class, serviceHelper.getTemplates(regClientModuleId, null, currentTimestamp));
        futuresMap.put(DocumentType.class, serviceHelper.getDocumentTypes(null, currentTimestamp));
        futuresMap.put(ApplicantValidDocument.class, serviceHelper.getApplicantValidDocument(null, currentTimestamp));
        futuresMap.put(Location.class, serviceHelper.getLocationHierarchy(null, currentTimestamp));
        futuresMap.put(LocationHierarchy.class, serviceHelper.getLocationHierarchyList(null, selfTokenRestTemplate));
        futuresMap.put(DynamicFieldDto.class, serviceHelper.getAllDynamicFields(null, selfTokenRestTemplate));
        futuresMap.put(ReasonCategory.class, serviceHelper.getReasonCategory(null, currentTimestamp));
        futuresMap.put(ReasonList.class, serviceHelper.getReasonList(null, currentTimestamp));
        futuresMap.put(ScreenAuthorization.class, serviceHelper.getScreenAuthorizationDetails(null, currentTimestamp));
        futuresMap.put(ScreenDetail.class, serviceHelper.getScreenDetails(null, currentTimestamp));
        futuresMap.put(BlocklistedWords.class, serviceHelper.getBlackListedWords(null, currentTimestamp));
        futuresMap.put(ProcessList.class, serviceHelper.getProcessList(null, currentTimestamp));
        futuresMap.put(SyncJobDef.class,  serviceHelper.getSyncJobDefDetails(null, currentTimestamp));
        futuresMap.put(PermittedLocalConfig.class, serviceHelper.getPermittedConfig(null, currentTimestamp));

        CompletableFuture array [] = new CompletableFuture[futuresMap.size()];
        CompletableFuture<Void> future = CompletableFuture.allOf(futuresMap.values().toArray(array));

        try {
            future.join();
        } catch (CompletionException e) {
            logger.error("Failed to fetch data", e);
            if (e.getCause() instanceof SyncDataServiceException) {
                throw (SyncDataServiceException) e.getCause();
            } else {
                throw (RuntimeException) e.getCause();
            }
        }

        retrieveAndCreateSnapshot(futuresMap);
        logger.info("Create snapshot scheduled job completed");
    }


    private void retrieveAndCreateSnapshot(Map<Class, CompletableFuture> futures) {
        for (Map.Entry<Class, CompletableFuture> entry : futures.entrySet()) {
            try {
                Object result = entry.getValue().get();
                if (result == null)
                    continue;

                String entityType = entry.getKey() == DynamicFieldDto.class ? "dynamic" : "structured";
                List entities = (List) result;
                entities = (List) entities.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());

                if(entityType.equals("structured"))
                    handleFields(entities, entry.getKey().getSimpleName());
                else
                    handleDynamicFields(entities); //Fills dynamic field data

            } catch (Exception e) {
            	
                logger.error("Failed to create snapshot {} {}", entry.getKey().getSimpleName(), e);
               
            }
        }
    }

    private void handleDynamicFields(List entities) {
        Map<String, List<DynamicFieldDto>> data = new HashMap<String, List<DynamicFieldDto>>();
        entities.forEach(dto -> {
            if(!data.containsKey(((DynamicFieldDto)dto).getName())) {
                List<DynamicFieldDto> langBasedData = new ArrayList<DynamicFieldDto>();
                langBasedData.add(((DynamicFieldDto)dto));
                data.put(((DynamicFieldDto)dto).getName(), langBasedData);
            }
            else
                data.get(((DynamicFieldDto)dto).getName()).add(((DynamicFieldDto)dto));
        });

        List<SyncDataBaseDto> result = new ArrayList<>();
        for(String key : data.keySet()) {
            try {
                result.add(new SyncDataBaseDto(key, "dynamic", mapper.getObjectAsJsonString(data.get(key))));
            } catch (Exception e) {
                logger.error("Failed json serialization : {} {}", key, e);
            }
        }

        handleFields(result, DynamicFieldDto.class.getSimpleName());
    }

    private void handleFields(List entities, String entityName) {
        if(entities != null && !entities.isEmpty()) {
            try {
                createSnapshot(entityName, mapper.getObjectAsJsonString(entities));
                logger.info("{} SNAPSHOT created", entityName);
            } catch (Exception e) {
               logger.error("Failed to create snapshot for {} {}", entityName, e);
            }
        }
    }

    private void createSnapshot(String entityName, String content) throws IOException {
        Path path = Path.of(clientSettingsDir);
        if(!path.toFile().exists()) {
            boolean status = path.toFile().mkdirs();
            logger.info("creating _SNAPSHOTS folder as it doesn't exists. path: {}, mkdirs() status: {}", path, status);
        }

        FileUtils.write(path.resolve(entityName.toUpperCase()).toFile(), content, StandardCharsets.UTF_8, false);
    }

}
