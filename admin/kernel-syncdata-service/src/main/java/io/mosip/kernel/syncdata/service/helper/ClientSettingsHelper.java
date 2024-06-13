package io.mosip.kernel.syncdata.service.helper;

import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.helper.beans.RegistrationCenterMachine;
import io.mosip.kernel.syncdata.service.helper.beans.RegistrationCenterUser;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class ClientSettingsHelper {

	private final static Logger LOGGER = LoggerFactory.getLogger(ClientSettingsHelper.class);

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@Autowired
	private ModuleDetailRepository moduleDetailRepository;

	@Value("${mosip.syncdata.regclient.module.id:10002}")
	private String regClientModuleId;

	@Value("#{'${mosip.registration.sync.scripts:applicanttype.mvel}'.split(',')}")
	private Set<String> scriptNames;

	@Autowired
	private Environment environment;

	@Autowired
	private MapperUtils mapper;

	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	private boolean hasURLDetails(Class clazz, boolean isV2API, boolean deltaSync) {
		if (!isV2API)
			return false;

		String entityName = clazz.getSimpleName().toUpperCase();
		if (!environment.containsProperty(String.format("mosip.sync.entity.url.%s", entityName)))
			return false;

		Boolean onlyOnFullSync = environment
				.getProperty(String.format("mosip.sync.entity.only-on-fullsync.%s", entityName), Boolean.class, true);

		return onlyOnFullSync ? !deltaSync : true;
	}

	public Map<Class, CompletableFuture> getInitiateDataFetch(String machineId, String regCenterId,
			LocalDateTime lastUpdated, LocalDateTime currentTimestamp, boolean isV2API, boolean deltaSync, String fullSyncEntities) {
		List<String> entities = (fullSyncEntities != null && !fullSyncEntities.isBlank()) ? Arrays.asList(fullSyncEntities.split("\\s*,\\s*")) : new ArrayList<>();
		
		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
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

		// to handle backward compatibility
		if (!isV2API) {
			// template_file_format & template_type
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

			// valid_document
			futuresMap.put(ValidDocument.class,
					hasURLDetails(ValidDocument.class, isV2API, deltaSync) ? getURLDetails(ValidDocument.class)
							: serviceHelper.getValidDocuments(lastUpdated, currentTimestamp));
		}

		// invokes master-data-service
		futuresMap.put(LocationHierarchy.class,
				hasURLDetails(LocationHierarchy.class, isV2API, deltaSync) ? getURLDetails(LocationHierarchy.class)
						: serviceHelper.getLocationHierarchyList(getLastUpdatedTimeFromEntity(LocationHierarchy.class, lastUpdated, entities)));
		futuresMap.put(DynamicFieldDto.class,
				hasURLDetails(DynamicFieldDto.class, isV2API, deltaSync) ? getURLDetails(DynamicFieldDto.class)
						: serviceHelper.getAllDynamicFields(getLastUpdatedTimeFromEntity(DynamicFieldDto.class, lastUpdated, entities)));

		return futuresMap;
	}
	
	private LocalDateTime getLastUpdatedTimeFromEntity(Class clazz, LocalDateTime lastUpdated, List<String> fullSyncEntities) {
		return fullSyncEntities.contains(clazz.getSimpleName()) ? null : lastUpdated;
	}

	public List<SyncDataBaseDto> retrieveData(Map<Class, CompletableFuture> futures, RegistrationCenterMachineDto regCenterMachineDto, boolean isV2API)
			throws RuntimeException {
		final List<SyncDataBaseDto> list = new ArrayList<>();
		futures.entrySet().parallelStream().forEach(entry -> {
			try {
				Object result = entry.getValue().get();
				if (result != null) {
					String entityType = (result instanceof Map)
							? (entry.getKey() == DynamicFieldDto.class ? "dynamic-url" : "structured-url")
							: (entry.getKey() == DynamicFieldDto.class ? "dynamic" : "structured");

					switch (entityType) {
					case "structured-url":
					case "dynamic-url":
						list.add(getEncryptedSyncDataBaseDto(entry.getKey(), regCenterMachineDto, entityType, result));
						break;
					case "dynamic":
						handleDynamicData((List) result, list, regCenterMachineDto, isV2API);
						break;
					case "structured":
						if (isV2API)
							serviceHelper.getSyncDataBaseDtoV2(entry.getKey().getSimpleName(), entityType,
									(List) result, regCenterMachineDto, list);
						else
							serviceHelper.getSyncDataBaseDto(entry.getKey().getSimpleName(), entityType, (List) result,
									regCenterMachineDto, list);
						break;
					}
				}
			} catch (InterruptedException ie) {
				LOGGER.error("InterruptedException: ", ie);
				Thread.currentThread().interrupt();
			} catch (Throwable e) {
				LOGGER.error("Failed to construct client settings response", e);
				throw new RuntimeException(e);
			}
		});
		return list;
	}

	private void handleDynamicData(List entities, List<SyncDataBaseDto> list, RegistrationCenterMachineDto registrationCenterMachineDto, boolean isV2) {
		Map<String, List<DynamicFieldDto>> data = new HashMap<String, List<DynamicFieldDto>>();
		entities.forEach(dto -> {
			if (!data.containsKey(((DynamicFieldDto) dto).getName())) {
				List<DynamicFieldDto> langBasedData = new ArrayList<DynamicFieldDto>();
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

	public List<SyncDataBaseDto> getConfiguredScriptUrlDetail(RegistrationCenterMachineDto regCenterMachineDto) {
		List<SyncDataBaseDto> list = new ArrayList<>();
		scriptNames.forEach(fileName -> {
			Map<String, Object> urlDetail = buildUrlDetailMap(fileName);
			try {
				TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
				tpmCryptoRequestDto
						.setValue(CryptoUtil.encodeToURLSafeBase64(mapper.getObjectAsJsonString(urlDetail).getBytes()));
				tpmCryptoRequestDto.setPublicKey(regCenterMachineDto.getPublicKey());
				tpmCryptoRequestDto.setClientType(regCenterMachineDto.getClientType());
				TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
				list.add(new SyncDataBaseDto(fileName, "script", tpmCryptoResponseDto.getValue()));
			} catch (Exception e) {
				LOGGER.error("Failed to create script url detail {} data to json", fileName, e);
			}
		});
		return list;
	}

	private CompletableFuture<Map<String, Object>> getURLDetails(Class clazz) {
		Map<String, Object> urlDetails = buildUrlDetailMap(clazz.getSimpleName());
		return CompletableFuture.completedFuture(urlDetails);
	}

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
