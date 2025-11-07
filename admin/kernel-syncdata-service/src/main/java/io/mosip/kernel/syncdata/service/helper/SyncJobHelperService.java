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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
/**
 * Service helper class for managing cache eviction and snapshot creation for synchronization data.
 * <p>
 * This class provides methods to evict delta and initial sync caches based on cron schedules,
 * calculate timestamps for full and delta sync operations, and create snapshots of various entity
 * types. It uses asynchronous operations to fetch data efficiently and handles dynamic and structured
 * data serialization to files. The class is designed to run with high precedence and logs key events
 * and errors for traceability.
 * </p>
 *
 * @author [Anusha]
 * @since 1.0.0
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SyncJobHelperService {
    /** Logger instance for logging events and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncJobHelperService.class);
    /** Directory path for storing snapshot files. */
    @Value("${mosip.syncdata.clientsettings.data.dir:./_SNAPSHOTS}")
    private String clientSettingsDir;
    /** Module ID for the registration client. */
    @Value("${mosip.syncdata.regclient.module.id:10002}")
    private String regClientModuleId;
    /** Cron expression for delta sync cache eviction. */
    @Value("${syncdata.cache.evict.delta-sync.cron:0 0/15 * * * *}")
    private String deltaCacheEvictCron;
    /** Cron expression for snapshot creation and initial sync cache eviction. */
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
    /**
     * Evicts all keys from the delta-sync cache based on the configured cron schedule.
     * <p>
     * This method clears the cache named "delta-sync" and logs the start and completion
     * of the eviction process. If the cache is not found, no action is taken.
     * </p>
     */
    //By default, to trigger every hour
    @Scheduled(cron = "${syncdata.cache.evict.delta-sync.cron}", zone = "UTC")
    public void evictDeltaCaches() {
        LOGGER.info("Eviction of all keys from delta-sync cache started");
        Cache c = cacheManager.getCache("delta-sync");
        if (null != c)
            c.clear();
        LOGGER.info("Eviction of all keys from delta-sync cache completed");
    }
    /**
     * Retrieves the timestamp for full synchronization, set to midnight of the current day in UTC.
     *
     * @return the {@link LocalDateTime} representing midnight of the current day in UTC
     */
    public LocalDateTime getFullSyncCurrentTimestamp() {
        return LocalDate.now(ZoneOffset.UTC).atTime(0, 0, 0, 0);
    }
    /**
     * Calculates the previous trigger timestamp for delta synchronization based on the cron schedule.
     * <p>
     * This method uses the configured cron expression to determine the most recent trigger time
     * before the current moment. If the cron expression is invalid or no triggers are found,
     * an error is logged, and null is returned.
     * </p>
     *
     * @return the {@link LocalDateTime} of the previous trigger, or null if no valid trigger is found
     */
    public LocalDateTime getDeltaSyncCurrentTimestamp() {
        CronExpression cron = CronExpression.parse(deltaCacheEvictCron);

        final ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);

        final ZonedDateTime immediateNextTrigger = cron.next(nowUtc);
        if (immediateNextTrigger == null) {
            LOGGER.error("Cron expression might be invalid or has no upcoming triggers.");
            return null;
        }
        final ZonedDateTime next2 = cron.next(immediateNextTrigger);
        if (next2 == null) {
            LOGGER.error("No upcoming triggers after {}", immediateNextTrigger);
            return null;
        }

        final Duration gap = Duration.between(immediateNextTrigger, next2);
        final ZonedDateTime previous = immediateNextTrigger.minus(gap);

        // Optional sanity guard: if cron had very uneven gaps, ensure previous < now
        // If it's not, fallback to a tiny forward scan:
        //   ZonedDateTime cursor = previous;
        //   while (cursor != null && !cursor.isAfter(nowUtc)) { last = cursor; cursor = cron.next(cursor); }
        //   return last.toLocalDateTime();

        return previous.toLocalDateTime();
    }
    /**
     * Clears the initial-sync cache and triggers snapshot creation based on the configured cron schedule.
     * <p>
     * This method evicts all keys from the "initial-sync" cache and initiates the snapshot creation process.
     * If the cache is not found, the eviction is skipped, and snapshot creation proceeds.
     * </p>
     */
    @Scheduled(cron = "${syncdata.cache.snapshot.cron}", zone = "UTC")
    public void clearCacheAndRecreateSnapshot() {
        LOGGER.info("Eviction of all keys from initial-sync cache started");
        Cache c = cacheManager.getCache("initial-sync");
        if (null != c)
            c.clear();
        LOGGER.info("Eviction of all keys from initial-sync cache Completed");
        createEntitySnapshot();
    }
    /**
     * Creates snapshots for all configured entity types by fetching data asynchronously.
     * <p>
     * This method initiates asynchronous data retrieval for various entity types, waits for completion,
     * and processes the results to create snapshots. It handles both structured and dynamic data,
     * logging errors for any failures. The method runs with a fixed delay to ensure it executes only once
     * at startup and then as triggered by the cron schedule.
     * </p>
     */
    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 0)
    public void createEntitySnapshot() {
        LocalDateTime currentTimestamp = getFullSyncCurrentTimestamp();
        LOGGER.info("Create snapshot scheduled job started : {}", currentTimestamp);
        Map<Class<?>, CompletableFuture<?>> futuresMap = new HashMap<>();
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
        futuresMap.put(SyncJobDef.class, serviceHelper.getSyncJobDefDetails(null, currentTimestamp));
        futuresMap.put(PermittedLocalConfig.class, serviceHelper.getPermittedConfig(null, currentTimestamp));
        CompletableFuture<?>[] array = new CompletableFuture<?>[futuresMap.size()];
        CompletableFuture<Void> future = CompletableFuture.allOf(futuresMap.values().toArray(array));
        try {
            future.join();
        } catch (CompletionException e) {
            LOGGER.error("Failed to fetch data", e);
            if (e.getCause() instanceof SyncDataServiceException) {
                throw (SyncDataServiceException) e.getCause();
            } else {
                throw (RuntimeException) e.getCause();
            }
        }
        retrieveAndCreateSnapshot(futuresMap);
        LOGGER.info("Create snapshot scheduled job completed");
    }
    /**
     * Processes the results of asynchronous data fetches and creates snapshots for each entity type.
     * <p>
     * This method iterates over the completed futures, filters out null entities, and processes
     * the data into snapshots. Structured data is serialized directly, while dynamic data is grouped
     * by name before serialization. Errors during processing are logged.
     * </p>
     *
     * @param futures map of entity classes to their corresponding {@link CompletableFuture} results
     */
    private void retrieveAndCreateSnapshot(Map<Class<?>, CompletableFuture<?>> futures) {
        for (Map.Entry<Class<?>, CompletableFuture<?>> entry : futures.entrySet()) {
            try {
                Object result = entry.getValue().get();
                if (result == null)
                    continue;
                String entityType = entry.getKey() == DynamicFieldDto.class ? "dynamic" : "structured";
                List<?> entities = (List<?>) result;
                entities = (List<?>) entities.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
                if (entityType.equals("structured"))
                    handleFields(entities, entry.getKey().getSimpleName());
                else
                    handleDynamicFields(entities); //Fills dynamic field data
            } catch (InterruptedException ie) {
                LOGGER.error("InterruptedException: ", ie);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.error("Failed to create snapshot {} {}", entry.getKey().getSimpleName(), e);
            }
        }
    }
    /**
     * Groups dynamic field data by name and creates snapshots for each group.
     * <p>
     * This method organizes {@link DynamicFieldDto} objects by their name, serializes each group
     * into a JSON string, and creates a snapshot for the grouped data. Errors during serialization
     * are logged.
     * </p>
     *
     * @param entities list of {@link DynamicFieldDto} objects to process
     */
    private void handleDynamicFields(List<?> entities) {
        Map<String, List<DynamicFieldDto>> data = new HashMap<>();
        entities.forEach(dto -> {
            if (!data.containsKey(((DynamicFieldDto) dto).getName())) {
                List<DynamicFieldDto> langBasedData = new ArrayList<>();
                langBasedData.add(((DynamicFieldDto) dto));
                data.put(((DynamicFieldDto) dto).getName(), langBasedData);
            } else
                data.get(((DynamicFieldDto) dto).getName()).add(((DynamicFieldDto) dto));
        });
        List<SyncDataBaseDto> result = new ArrayList<>();
        for (String key : data.keySet()) {
            try {
                result.add(new SyncDataBaseDto(key, "dynamic", mapper.getObjectAsJsonString(data.get(key))));
            } catch (Exception e) {
                LOGGER.error("Failed json serialization : {} {}", key, e);
            }
        }
        handleFields(result, DynamicFieldDto.class.getSimpleName());
    }
    /**
     * Serializes a list of entities to JSON and creates a snapshot file.
     * <p>
     * This method converts the provided entities to a JSON string and writes it to a file
     * in the configured directory. The directory is created if it does not exist. Errors
     * during serialization or file writing are logged.
     * </p>
     *
     * @param entities   list of entities to serialize
     * @param entityName name of the entity type for the snapshot file
     */
    private void handleFields(List<?> entities, String entityName) {
        if (entities != null && !entities.isEmpty()) {
            try {
                createSnapshot(entityName, mapper.getObjectAsJsonString(entities));
                LOGGER.info("{} SNAPSHOT created", entityName);
            } catch (Exception e) {
                LOGGER.error("Failed to create snapshot for {} {}", entityName, e);
            }
        }
    }
    /**
     * Writes content to a snapshot file in the configured directory.
     * <p>
     * This method creates the snapshot directory if it does not exist and writes the provided
     * content to a file named after the entity type. The file is written in UTF-8 encoding.
     * </p>
     *
     * @param entityName name of the entity type for the snapshot file
     * @param content    JSON content to write to the file
     * @throws IOException if an error occurs during file writing
     */
    private void createSnapshot(String entityName, String content) throws IOException {
        if (content == null || content.isEmpty()) {
            LOGGER.error("Invalid request: snapshot content is null or empty for entity {}", entityName);
            return;
        }
        Path path = Path.of(clientSettingsDir);
        if (!path.toFile().exists()) {
            boolean status = path.toFile().mkdirs();
            LOGGER.info("creating _SNAPSHOTS folder as it doesn't exists. path: {}, mkdirs() status: {}", path, status);
        }
        FileUtils.write(path.resolve(entityName.toUpperCase()).toFile(), content, StandardCharsets.UTF_8, false);
    }
}