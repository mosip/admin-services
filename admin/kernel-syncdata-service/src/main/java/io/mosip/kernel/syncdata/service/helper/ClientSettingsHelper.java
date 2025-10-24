package io.mosip.kernel.syncdata.service.helper;

import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.RegistrationCenterMachineDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.helper.beans.RegistrationCenterMachine;
import io.mosip.kernel.syncdata.service.helper.beans.RegistrationCenterUser;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import jakarta.annotation.PostConstruct;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Helper class for client settings synchronization in the MOSIP system.
 * Manages the initiation and retrieval of data fetches for various master data entities,
 * handles dynamic data grouping, and performs encryption for secure data transfer.
 * Optimized for performance by caching environment properties, using parallel processing
 * where beneficial, and minimizing redundant operations.
 *
 * @author [Mosip Team]
 * @since 1.0.0
 */
@Component
public class ClientSettingsHelper {

	/**
	 * Logger instance for logging events and errors in this class.
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientSettingsHelper.class);

	/**
	 * Service helper for fetching master data.
	 */
	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	/**
	 * Repository for accessing module details.
	 */
	@Autowired
	private ModuleDetailRepository moduleDetailRepository;

	/**
	 * ID of the registration client module.
	 */
	@Value("${mosip.syncdata.regclient.module.id:10002}")
	private String regClientModuleId;

	/**
	 * Set of script names for synchronization.
	 */
	@Value("#{'${mosip.registration.sync.scripts:applicanttype.mvel}'.split(',')}")
	private Set<String> scriptNames;

	/**
	 * Environment for accessing configuration properties.
	 */
	@Autowired
	private Environment environment;

	/**
	 * Utility for mapping objects to JSON.
	 */
	@Autowired
	private MapperUtils mapper;

	/**
	 * Service for client cryptographic operations.
	 */
	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	/**
	 * Cache for storing URL detail maps for entity classes.
	 */
	private final Map<String, Map<String, Object>> urlDetailsCache = new ConcurrentHashMap<>();

	/**
	 * Cache for storing whether URL details are available for entity classes.
	 */
	private final Map<String, Boolean> hasUrlDetailsCache = new ConcurrentHashMap<>();

	/**
	 * Precomputed map of script URL details, initialized at startup for performance.
	 */
	private Map<String, Map<String, Object>> precomputedScriptUrls;

	/**
	 * Initializes the helper by precomputing script URL details at application startup.
	 * This avoids runtime computation and improves response time for script-related operations.
	 */
	@PostConstruct
	private void init() {
		precomputedScriptUrls = scriptNames.stream()
				.collect(Collectors.toMap(
						name -> name,
						this::buildUrlDetailMap,
						(a, b) -> a,
						ConcurrentHashMap::new
				));
	}

	/**
	 * Checks if URL details are configured for the given class, considering API version and sync type.
	 * Uses caching to avoid repeated environment property lookups.
	 *
	 * @param clazz       the entity class to check
	 * @param isV2API     indicates if V2 API is being used
	 * @param deltaSync   indicates if delta sync is being performed
	 * @return true if URL details are available and applicable, false otherwise
	 */
	private boolean hasURLDetails(Class clazz, boolean isV2API, boolean deltaSync) {
		if (!isV2API)
			return false;

		String entityName = clazz.getSimpleName().toUpperCase();
		String cacheKey = entityName + "_" + deltaSync;

		return hasUrlDetailsCache.computeIfAbsent(cacheKey, key -> {
			if (!environment.containsProperty(String.format("mosip.sync.entity.url.%s", entityName))) {
				return false;
			}

			Boolean onlyOnFullSync = environment.getProperty(
					String.format("mosip.sync.entity.only-on-fullsync.%s", entityName), Boolean.class, true);

			return onlyOnFullSync ? !deltaSync : true;
		});
	}

	/**
	 * Initiates data fetching for various entities asynchronously.
	 * Builds a map of entity classes to CompletableFutures for efficient parallel processing.
	 *
	 * @param machineId              the machine ID for filtering
	 * @param regCenterId            the registration center ID for filtering
	 * @param lastUpdated            the last updated timestamp; if null, fetches all records
	 * @param currentTimestamp       the current timestamp for filtering
	 * @param isV2API                indicates if V2 API is being used
	 * @param deltaSync              indicates if delta sync is being performed
	 * @param fullSyncEntities       comma-separated list of entities for full sync; may be blank
	 * @return a map of entity classes to CompletableFutures containing fetched data
	 */
	public Map<Class<?>, CompletableFuture<?>> getInitiateDataFetch(String machineId, String regCenterId,
															  LocalDateTime lastUpdated, LocalDateTime currentTimestamp, boolean isV2API, boolean deltaSync, String fullSyncEntities) {
		LOGGER.info("Initiating data fetch for machineId: {}, regCenterId: {}, lastUpdated: {}, currentTimestamp: {}, isV2API: {}, deltaSync: {}, fullSyncEntities: {}",
				machineId, regCenterId, lastUpdated, currentTimestamp, isV2API, deltaSync, fullSyncEntities);
		List<String> entities = StringUtils.isNotBlank(fullSyncEntities) ? List.of(StringUtils.split(fullSyncEntities, ",")) : new ArrayList<>();

		Map<Class<?>, CompletableFuture<?>> futuresMap = new ConcurrentHashMap<>();
		futuresMap.put(AppAuthenticationMethod.class,
				hasURLDetails(AppAuthenticationMethod.class, isV2API, deltaSync)
						? getURLDetails(AppAuthenticationMethod.class)
						: serviceHelper.getAppAuthenticationMethodDetails(getLastUpdatedTimeFromEntity(AppAuthenticationMethod.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(AppRolePriority.class,
				hasURLDetails(AppRolePriority.class, isV2API, deltaSync) ? getURLDetails(AppRolePriority.class)
						: serviceHelper.getAppRolePriorityDetails(getLastUpdatedTimeFromEntity(AppRolePriority.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(Machine.class, serviceHelper.getMachines(regCenterId, getLastUpdatedTimeFromEntity(Machine.class, lastUpdated, entities), currentTimestamp, machineId));
		futuresMap.put(RegistrationCenter.class,
				serviceHelper.getRegistrationCenter(regCenterId, getLastUpdatedTimeFromEntity(RegistrationCenter.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(Template.class, hasURLDetails(Template.class, isV2API, deltaSync) ? getURLDetails(Template.class)
				: serviceHelper.getTemplates(regClientModuleId, getLastUpdatedTimeFromEntity(Template.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(DocumentType.class,
				hasURLDetails(DocumentType.class, isV2API, deltaSync) ? getURLDetails(DocumentType.class)
						: serviceHelper.getDocumentTypes(getLastUpdatedTimeFromEntity(DocumentType.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(ApplicantValidDocument.class,
				hasURLDetails(ApplicantValidDocument.class, isV2API, deltaSync)
						? getURLDetails(ApplicantValidDocument.class)
						: serviceHelper.getApplicantValidDocument(getLastUpdatedTimeFromEntity(ApplicantValidDocument.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(Location.class, hasURLDetails(Location.class, isV2API, deltaSync) ? getURLDetails(Location.class)
				: serviceHelper.getLocationHierarchy(getLastUpdatedTimeFromEntity(Location.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(ReasonCategory.class,
				hasURLDetails(ReasonCategory.class, isV2API, deltaSync) ? getURLDetails(ReasonCategory.class)
						: serviceHelper.getReasonCategory(getLastUpdatedTimeFromEntity(ReasonCategory.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(ReasonList.class,
				hasURLDetails(ReasonList.class, isV2API, deltaSync) ? getURLDetails(ReasonList.class)
						: serviceHelper.getReasonList(getLastUpdatedTimeFromEntity(ReasonList.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(Holiday.class, serviceHelper.getHolidays(getLastUpdatedTimeFromEntity(Holiday.class, lastUpdated, entities), machineId, currentTimestamp));
		futuresMap.put(BlocklistedWords.class,
				hasURLDetails(BlocklistedWords.class, isV2API, deltaSync) ? getURLDetails(BlocklistedWords.class)
						: serviceHelper.getBlackListedWords(getLastUpdatedTimeFromEntity(BlocklistedWords.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(ScreenAuthorization.class,
				hasURLDetails(ScreenAuthorization.class, isV2API, deltaSync) ? getURLDetails(ScreenAuthorization.class)
						: serviceHelper.getScreenAuthorizationDetails(getLastUpdatedTimeFromEntity(ScreenAuthorization.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(ScreenDetail.class,
				hasURLDetails(ScreenDetail.class, isV2API, deltaSync) ? getURLDetails(ScreenDetail.class)
						: serviceHelper.getScreenDetails(getLastUpdatedTimeFromEntity(ScreenDetail.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(ProcessList.class,
				hasURLDetails(ProcessList.class, isV2API, deltaSync) ? getURLDetails(ProcessList.class)
						: serviceHelper.getProcessList(getLastUpdatedTimeFromEntity(ProcessList.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(SyncJobDef.class,
				hasURLDetails(SyncJobDef.class, isV2API, deltaSync) ? getURLDetails(SyncJobDef.class)
						: serviceHelper.getSyncJobDefDetails(getLastUpdatedTimeFromEntity(SyncJobDef.class, lastUpdated, entities), currentTimestamp));
		futuresMap.put(PermittedLocalConfig.class,
				hasURLDetails(PermittedLocalConfig.class, isV2API, deltaSync)
						? getURLDetails(PermittedLocalConfig.class)
						: serviceHelper.getPermittedConfig(getLastUpdatedTimeFromEntity(PermittedLocalConfig.class, lastUpdated, entities), currentTimestamp));

		futuresMap.put(Language.class, serviceHelper.getLanguageList(getLastUpdatedTimeFromEntity(Language.class, lastUpdated, entities), currentTimestamp));

		// Handle backward compatibility for non-V2 API
		if (!isV2API) {
			futuresMap.put(TemplateFileFormat.class,
					hasURLDetails(TemplateFileFormat.class, isV2API, deltaSync)
							? getURLDetails(TemplateFileFormat.class)
							: serviceHelper.getTemplateFileFormats(lastUpdated, currentTimestamp));
			futuresMap.put(TemplateType.class,
					hasURLDetails(TemplateType.class, isV2API, deltaSync) ? getURLDetails(TemplateType.class)
							: serviceHelper.getTemplateTypes(lastUpdated, currentTimestamp));

			futuresMap.put(RegistrationCenterMachine.class,
					serviceHelper.getRegistrationCenterMachines(regCenterId, lastUpdated, currentTimestamp, machineId));
			futuresMap.put(RegistrationCenterUser.class,
					serviceHelper.getRegistrationCenterUsers(regCenterId, lastUpdated, currentTimestamp));

			futuresMap.put(ValidDocument.class,
					hasURLDetails(ValidDocument.class, isV2API, deltaSync) ? getURLDetails(ValidDocument.class)
							: serviceHelper.getValidDocuments(lastUpdated, currentTimestamp));

			LOGGER.debug("Non-V2 API entities added: TemplateFileFormat, TemplateType, RegistrationCenterMachine, RegistrationCenterUser, ValidDocument");

		}

		// Invoke master-data-service for specific entities
		futuresMap.put(LocationHierarchy.class,
				hasURLDetails(LocationHierarchy.class, isV2API, deltaSync) ? getURLDetails(LocationHierarchy.class)
						: serviceHelper.getLocationHierarchyList(getLastUpdatedTimeFromEntity(LocationHierarchy.class, lastUpdated, entities)));
		futuresMap.put(DynamicFieldDto.class,
				hasURLDetails(DynamicFieldDto.class, isV2API, deltaSync) ? getURLDetails(DynamicFieldDto.class)
						: serviceHelper.getAllDynamicFields(getLastUpdatedTimeFromEntity(DynamicFieldDto.class, lastUpdated, entities)));

		LOGGER.info("Data fetch tasks initiated for entities: {}", futuresMap.keySet());

		return futuresMap;
	}

	/**
	 * Determines the effective last updated timestamp for an entity, adjusting for full sync if specified.
	 *
	 * @param clazz             the entity class
	 * @param lastUpdated       the original last updated timestamp
	 * @param fullSyncEntities  list of entities requiring full sync
	 * @return the effective last updated timestamp, or null for full sync
	 */
	private LocalDateTime getLastUpdatedTimeFromEntity(Class clazz, LocalDateTime lastUpdated, List<String> fullSyncEntities) {
		return fullSyncEntities.contains(clazz.getSimpleName()) ? null : lastUpdated;
	}

	/**
	 * Retrieves and processes data from the provided futures map, encrypting results as needed.
	 * Uses parallel processing for efficiency, with thread-safe collections to prevent concurrency issues.
	 *
	 * @param futures               the map of futures to process
	 * @param regCenterMachineDto   the DTO for registration center machine details used in encryption
	 * @param isV2API               indicates if V2 API is being used
	 * @return a list of encrypted {@link SyncDataBaseDto} objects
	 * @throws RuntimeException if an error occurs during data retrieval or processing
	 */
	public List<SyncDataBaseDto> retrieveData(
			Map<Class<?>, CompletableFuture<?>> futures,
			RegistrationCenterMachineDto regCenterMachineDto,
			boolean isV2API) {

		return futures.entrySet()
				.parallelStream()
				.flatMap(e -> processEntry(e, regCenterMachineDto, isV2API).stream())
				.toList();   // Java 16+. For Java 8–15 use .collect(Collectors.toList())
	}
	private List<SyncDataBaseDto> processEntry(
			Map.Entry<Class<?>, CompletableFuture<?>> entry,
			RegistrationCenterMachineDto regCenterMachineDto,
			boolean isV2API) {

		try {
			Object result = entry.getValue().join();
			if (result == null) return List.of();

			boolean isDynamic = entry.getKey() == DynamicFieldDto.class;
			boolean isUrl     = result instanceof Map;

			String entityType = isUrl
					? (isDynamic ? "dynamic-url" : "structured-url")
					: (isDynamic ? "dynamic"     : "structured");

			List<SyncDataBaseDto> out = new ArrayList<>();

			switch (entityType) {
				case "structured-url", "dynamic-url" -> {
					SyncDataBaseDto dto =
							getEncryptedSyncDataBaseDto(entry.getKey(), regCenterMachineDto, entityType, result);
					if (dto != null) out.add(dto);
				}
				case "dynamic" ->
						handleDynamicData(castList(result), out, regCenterMachineDto, isV2API);
				case "structured" -> {
					if (isV2API) {
						serviceHelper.getSyncDataBaseDtoV2(entry.getKey().getSimpleName(),
								entityType,
								castList(result),
								regCenterMachineDto,
								out);
					} else {
						serviceHelper.getSyncDataBaseDto(entry.getKey().getSimpleName(),
								entityType,
								castList(result),
								regCenterMachineDto,
								out);
					}
				}
			}
			return out;

		} catch (Throwable e) {
			LOGGER.error("Failed to construct client settings response for entity: {}",
					entry.getKey().getSimpleName(), e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> castList(Object obj) {
		return (List<T>) obj;
	}


	/**
	 * Handles grouping and encryption of dynamic field data.
	 * Uses concurrent structures for thread safety during potential parallel access.
	 *
	 * @param entities                  the list of dynamic field entities
	 * @param list                      the synchronized list to add encrypted DTOs
	 * @param registrationCenterMachineDto the DTO for encryption details
	 * @param isV2                      indicates if V2 API is being used
	 */
	private void handleDynamicData(List entities, List<SyncDataBaseDto> list, RegistrationCenterMachineDto registrationCenterMachineDto, boolean isV2) {
		Map<String, List<DynamicFieldDto>> data = new HashMap<>();
		entities.forEach(dto -> {
			if (!data.containsKey(((DynamicFieldDto) dto).getName())) {
				List<DynamicFieldDto> langBasedData = new ArrayList<>();
				langBasedData.add(((DynamicFieldDto) dto));
				data.put(((DynamicFieldDto) dto).getName(), langBasedData);
			} else
				data.get(((DynamicFieldDto) dto).getName()).add(((DynamicFieldDto) dto));
		});

		for (String key : data.keySet()) {
			if (isV2)
				serviceHelper.getSyncDataBaseDtoV2(key, "dynamic", data.get(key), registrationCenterMachineDto, list);
			else
				serviceHelper.getSyncDataBaseDto(key, "dynamic", data.get(key), registrationCenterMachineDto, list);
		}
	}

	/**
	 * Encrypts URL details into a {@link SyncDataBaseDto}.
	 * Handles serialization and encryption efficiently.
	 *
	 * @param clazz                     the entity class
	 * @param registrationCenterMachineDto the DTO for encryption details
	 * @param entityType                the type of the entity
	 * @param urlDetails                the URL details to encrypt
	 * @return the encrypted {@link SyncDataBaseDto}, or null if encryption fails
	 */
	private SyncDataBaseDto getEncryptedSyncDataBaseDto(Class clazz, RegistrationCenterMachineDto registrationCenterMachineDto, String entityType,
														Object urlDetails) {
		try {
			TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
			tpmCryptoRequestDto
					.setValue(CryptoUtil.encodeToURLSafeBase64(mapper.getObjectAsJsonString(urlDetails).getBytes()));
			tpmCryptoRequestDto.setPublicKey(registrationCenterMachineDto.getPublicKey());
			tpmCryptoRequestDto.setClientType(registrationCenterMachineDto.getClientType());
			TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
			return new SyncDataBaseDto(clazz.getSimpleName(), entityType, tpmCryptoResponseDto.getValue());
		} catch (Exception e) {
			LOGGER.error("Failed to encrypt urlDetails for {} data to json", clazz.getSimpleName(), e);
		}
		return null;
	}

	/**
	 * Retrieves and encrypts configured script URL details.
	 * Processes scripts in parallel for efficiency.
	 *
	 * @param regCenterMachineDto the DTO for encryption details
	 * @return a list of encrypted {@link SyncDataBaseDto} for scripts
	 */
	/**
	 * Retrieves and encrypts configured script URL details.
	 * Processes scripts in parallel for efficiency.
	 *
	 * @param regCenterMachineDto the DTO for encryption details
	 * @return a list of encrypted {@link SyncDataBaseDto} for scripts
	 */
	public List<SyncDataBaseDto> getConfiguredScriptUrlDetail(RegistrationCenterMachineDto regCenterMachineDto) {
		return precomputedScriptUrls.entrySet().parallelStream()
				.map(entry -> {
					try {
						TpmCryptoRequestDto request = new TpmCryptoRequestDto();
						request.setValue(CryptoUtil.encodeToURLSafeBase64(mapper.getObjectAsJsonString(entry.getValue()).getBytes()));
						request.setPublicKey(regCenterMachineDto.getPublicKey());
						request.setClientType(regCenterMachineDto.getClientType());
						TpmCryptoResponseDto response = clientCryptoManagerService.csEncrypt(request);
						return new SyncDataBaseDto(entry.getKey(), "script", response.getValue());
					} catch (Exception e) {
						LOGGER.error("Encryption failed for script: {}", entry.getKey(), e);
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Asynchronously retrieves URL details for the given class.
	 * Uses caching to avoid repeated map building.
	 *
	 * @param clazz the entity class
	 * @return a CompletableFuture containing the URL details map
	 */
	private CompletableFuture<Map<String, Object>> getURLDetails(Class clazz) {
		String entityName = clazz.getSimpleName().toUpperCase();
		Map<String, Object> urlDetails = urlDetailsCache.computeIfAbsent(entityName, key -> buildUrlDetailMap(clazz.getSimpleName()));
		return CompletableFuture.completedFuture(urlDetails);
	}

	/**
	 * Builds a map of URL details from environment properties.
	 * Optimized with direct property access.
	 *
	 * @param name the name for property lookups
	 * @return a map containing URL configuration details
	 */
	private Map<String, Object> buildUrlDetailMap(String name) {
		Map<String, Object> urlDetail = new HashMap<>();
		urlDetail.put("url", environment.getProperty(String.format("mosip.sync.entity.url.%s", name.toUpperCase())));
		urlDetail.put("auth-required",
				environment.getProperty(String.format("mosip.sync.entity.auth-required.%s", name.toUpperCase())));
		urlDetail.put("auth-token",
				environment.getProperty(String.format("mosip.sync.entity.auth-token.%s", name.toUpperCase())));
		urlDetail.put("encrypted",
				environment.getProperty(String.format("mosip.sync.entity.encrypted.%s", name.toUpperCase())));
		urlDetail.put("headers",
				environment.getProperty(String.format("mosip.sync.entity.headers.%s", name.toUpperCase())));
		return urlDetail;
	}
}
