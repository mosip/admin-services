package io.mosip.kernel.syncdata.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.syncdata.constant.AdminServiceErrorCode;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.exception.*;
import io.mosip.kernel.syncdata.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Helper class for synchronizing master data in the MOSIP system.
 * Provides methods to fetch and transform master data entities into DTOs, with support for
 * asynchronous operations and encryption. Optimized for performance by minimizing database queries,
 * reducing serialization overhead, and leveraging parallel processing where appropriate.
 *
 * @author Abhishek Kumar
 * @author Srinivasan
 * @since 1.0.0
 */
@Component
public class SyncMasterDataServiceHelper {
	/**
	 * Logger instance for logging events and errors.
	 */
	private final static Logger logger = LoggerFactory.getLogger(SyncMasterDataServiceHelper.class);

	/**
	 * Normalizes the last updated timestamp by setting it to epoch if null.
	 *
	 * @param lastUpdated the last updated timestamp
	 * @return the normalized timestamp
	 */
	private LocalDateTime normalizeLastUpdated(LocalDateTime lastUpdated) {
		return lastUpdated == null ? LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC) : lastUpdated;
	}

	/**
	 * Utility for mapping entities to DTOs.
	 */
	@Autowired
	private MapperUtils mapper;

	/**
	 * Repository for accessing application data.
	 */
	@Autowired
	private ApplicationRepository applicationRepository;

	/**
	 * Repository for accessing machine data.
	 */
	@Autowired
	private MachineRepository machineRepository;

	/**
	 * Repository for accessing machine type data.
	 */
	@Autowired
	private MachineTypeRepository machineTypeRepository;

	/**
	 * Repository for accessing registration center data.
	 */
	@Autowired
	private RegistrationCenterRepository registrationCenterRepository;

	/**
	 * Repository for accessing registration center type data.
	 */
	@Autowired
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;

	/**
	 * Repository for accessing template data.
	 */
	@Autowired
	private TemplateRepository templateRepository;

	/**
	 * Repository for accessing template file format data.
	 */
	@Autowired
	private TemplateFileFormatRepository templateFileFormatRepository;

	/**
	 * Repository for accessing reason category data.
	 */
	@Autowired
	private ReasonCategoryRepository reasonCategoryRepository;

	/**
	 * Repository for accessing holiday data.
	 */
	@Autowired
	private HolidayRepository holidayRepository;

	/**
	 * Repository for accessing blocklisted words data.
	 */
	@Autowired
	private BlocklistedWordsRepository blocklistedWordsRepository;

	/**
	 * Repository for accessing biometric type data.
	 */
	@Autowired
	private BiometricTypeRepository biometricTypeRepository;

	/**
	 * Repository for accessing biometric attribute data.
	 */
	@Autowired
	private BiometricAttributeRepository biometricAttributeRepository;

	/**
	 * Repository for accessing title data.
	 */
	@Autowired
	private TitleRepository titleRepository;

	/**
	 * Repository for accessing language data.
	 */
	@Autowired
	private LanguageRepository languageRepository;

	/**
	 * Repository for accessing device data.
	 */
	@Autowired
	private DeviceRepository deviceRepository;

	/**
	 * Repository for accessing document category data.
	 */
	@Autowired
	private DocumentCategoryRepository documentCategoryRepository;

	/**
	 * Repository for accessing document type data.
	 */
	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	/**
	 * Repository for accessing ID type data.
	 */
	@Autowired
	private IdTypeRepository idTypeRepository;

	/**
	 * Repository for accessing device specification data.
	 */
	@Autowired
	private DeviceSpecificationRepository deviceSpecificationRepository;

	/**
	 * Repository for accessing location data.
	 */
	@Autowired
	private LocationRepository locationRepository;

	/**
	 * Repository for accessing template type data.
	 */
	@Autowired
	private TemplateTypeRepository templateTypeRepository;

	/**
	 * Repository for accessing machine specification data.
	 */
	@Autowired
	private MachineSpecificationRepository machineSpecificationRepository;

	/**
	 * Repository for accessing device type data.
	 */
	@Autowired
	private DeviceTypeRepository deviceTypeRepository;

	/**
	 * Repository for accessing valid document data.
	 */
	@Autowired
	private ValidDocumentRepository validDocumentRepository;

	/**
	 * Repository for accessing reason list data.
	 */
	@Autowired
	private ReasonListRepository reasonListRepository;

	/**
	 * Repository for accessing user details data.
	 */
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	/**
	 * Repository for accessing applicant valid document data.
	 */
	@Autowired
	private ApplicantValidDocumentRespository applicantValidDocumentRepository;

	/**
	 * Repository for accessing app authentication method data.
	 */
	@Autowired
	private AppAuthenticationMethodRepository appAuthenticationMethodRepository;

	/**
	 * Repository for accessing app detail data.
	 */
	@Autowired
	private AppDetailRepository appDetailRepository;

	/**
	 * Repository for accessing app role priority data.
	 */
	@Autowired
	private AppRolePriorityRepository appRolePriorityRepository;

	/**
	 * Repository for accessing screen authorization data.
	 */
	@Autowired
	private ScreenAuthorizationRepository screenAuthorizationRepository;

	/**
	 * Repository for accessing process list data.
	 */
	@Autowired
	private ProcessListRepository processListRepository;

	/**
	 * Repository for accessing screen detail data.
	 */
	@Autowired
	private ScreenDetailRepository screenDetailRepository;

	/**
	 * Repository for accessing sync job definition data.
	 */
	@Autowired
	private SyncJobDefRepository syncJobDefRepository;

	/**
	 * Repository for accessing device provider data.
	 */
	@Autowired
	private DeviceProviderRepository deviceProviderRepository;

	/**
	 * Repository for accessing device service data.
	 */
	@Autowired
	private DeviceServiceRepository deviceServiceRepository;

	/**
	 * Repository for accessing registered device data.
	 */
	@Autowired
	private RegisteredDeviceRepository registeredDeviceRepository;

	/**
	 * Repository for accessing foundational trust provider data.
	 */
	@Autowired
	private FoundationalTrustProviderRepository foundationalTrustProviderRepository;

	/**
	 * Repository for accessing device type DPM data.
	 */
	@Autowired
	private DeviceTypeDPMRepository deviceTypeDPMRepository;

	/**
	 * Repository for accessing device sub-type DPM data.
	 */
	@Autowired
	private DeviceSubTypeDPMRepository deviceSubTypeDPMRepository;

	/**
	 * Repository for accessing user details history data.
	 */
	@Autowired
	private UserDetailsHistoryRepository userDetailsHistoryRepository;

	/**
	 * Repository for accessing machine history data.
	 */
	@Autowired
	private MachineHistoryRepository machineHistoryRepository;

	/**
	 * Repository for accessing device history data.
	 */
	@Autowired
	private DeviceHistoryRepository deviceHistoryRepository;

	/**
	 * Repository for accessing permitted local configuration data.
	 */
	@Autowired
	private PermittedLocalConfigRepository permittedLocalConfigRepository;

	/**
	 * Service for client cryptographic operations.
	 */
	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	/**
	 * RestTemplate for making HTTP requests with self-token authentication.
	 */
	@Autowired
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate restTemplate;

	/**
	 * ObjectMapper for JSON serialization and deserialization.
	 */
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * URL for fetching location hierarchy data.
	 */
	@Value("${mosip.kernel.masterdata.locationhierarchylevels.uri}")
	private String locationHirerarchyUrl;

	/**
	 * URL for fetching dynamic field data.
	 */
	@Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
	private String dynamicfieldUrl;

	/**
	 * Constant for Android machine type code.
	 */
	private static final String ANDROID_MACHINE_TYPE_CODE = "ANDROID";

	/**
	 * Fetches machine details by registration center ID asynchronously.
	 *
	 * @param regCenterId      the registration center ID
	 * @param lastUpdated      the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @param machineId        the machine ID to filter; may be null
	 * @return a {@link CompletableFuture} containing a list of {@link MachineDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<MachineDto>> getMachines(String regCenterId, LocalDateTime lastUpdated,
														   LocalDateTime currentTimeStamp, String machineId) {

		if (!isChangesFound("Machine", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = normalizeLastUpdated(lastUpdated);
			List<Machine> machineDetailList = machineRepository.findMachineLatestCreatedUpdatedDeleted(regCenterId, effectiveLastUpdated, currentTimeStamp, machineId);
			return CompletableFuture.completedFuture(convertMachinesToDto(machineDetailList));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch machines for regCenterId: {}", regCenterId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch machine details: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Machine} entities to {@link MachineDto} objects.
	 *
	 * @param machineDetailList the list of machine entities
	 * @return a list of {@link MachineDto}; returns empty list if input is null or empty
	 */
	private List<MachineDto> convertMachinesToDto(List<Machine> machineDetailList) {
		if (machineDetailList == null || machineDetailList.isEmpty()) {
			return new ArrayList<>();
		}
		return machineDetailList.stream()
				.map(machine -> {
					MachineDto dto = new MachineDto();
					dto.setPublicKey(machine.getPublicKey());
					dto.setId(machine.getId());
					dto.setIpAddress(machine.getIpAddress());
					dto.setIsActive(machine.getIsActive());
					dto.setIsDeleted(machine.getIsDeleted());
					dto.setKeyIndex(machine.getKeyIndex());
					dto.setLangCode(machine.getLangCode());
					dto.setMacAddress(machine.getMacAddress());
					dto.setMachineSpecId(machine.getMachineSpecId());
					dto.setName(machine.getName());
					dto.setSerialNum(machine.getSerialNum());
					dto.setValidityDateTime(machine.getValidityDateTime());
					dto.setRegCenterId(machine.getRegCenterId());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches location hierarchy details asynchronously from an external service.
	 *
	 * @param lastUpdated the last updated timestamp; if null, fetches all records
	 * @return a {@link CompletableFuture} containing a list of {@link LocationHierarchyDto}
	 * @throws SyncDataServiceException if deserialization or service call fails
	 */
	@Async
	public CompletableFuture<List<LocationHierarchyDto>> getLocationHierarchyList(LocalDateTime lastUpdated) {
		return getLocationHierarchyList(lastUpdated, restTemplate);
	}
	/**
	 * Fetches location hierarchy details asynchronously using a specified RestTemplate.
	 *
	 * @param lastUpdated the last updated timestamp; if null, fetches all records
	 * @param restClient  the RestTemplate to use for the HTTP request
	 * @return a {@link CompletableFuture} containing a list of {@link LocationHierarchyDto}
	 * @throws SyncDataServiceException if deserialization or service call fails
	 */
	@Async
	public CompletableFuture<List<LocationHierarchyDto>> getLocationHierarchyList(LocalDateTime lastUpdated, RestTemplate restClient) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(locationHirerarchyUrl);
			if (lastUpdated != null) {
				builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated));
			}
			ResponseEntity<String> response = restClient.getForEntity(builder.build().toUri(), String.class);

			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new SyncDataServiceException(MasterDataErrorCode.LOCATION_HIERARCHY_FETCH_FAILED.getErrorCode(),
						"HTTP status " + response.getStatusCode() + " when fetching location hierarchy");
			}

			String responseBody = response.getBody();
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
			if (!validationErrorsList.isEmpty()) {
				throw new SyncServiceException(validationErrorsList);
			}

			ResponseWrapper<LocationHierarchyLevelResponseDto> responseObject = objectMapper.readValue(
					responseBody, new TypeReference<ResponseWrapper<LocationHierarchyLevelResponseDto>>() {});
			return CompletableFuture.completedFuture(responseObject.getResponse().getLocationHierarchyLevels());
		} catch (Exception e) {
			logger.error("Failed to fetch location hierarchy", e);
			throw new SyncDataServiceException(MasterDataErrorCode.LOCATION_HIERARCHY_DESERIALIZATION_FAILED.getErrorCode(),
					"Failed to deserialize location hierarchy: " + e.getMessage(), e);
		}
	}

	/**
	 * Fetches registration center details asynchronously.
	 *
	 * @param centerId        the registration center ID
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link RegistrationCenterDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterDto>> getRegistrationCenter(String centerId,
																				LocalDateTime lastUpdated, LocalDateTime currentTimeStamp) {

		if (!isChangesFound("RegistrationCenter", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<RegistrationCenter> list = registrationCenterRepository.findRegistrationCentersById(centerId, effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertRegistrationCenterToDto(list));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch registration centers for centerId: {}", centerId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.APPLICATION_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch registration centers: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link RegistrationCenter} entities to {@link RegistrationCenterDto} objects.
	 *
	 * @param list the list of registration center entities
	 * @return a list of {@link RegistrationCenterDto}; returns empty list if input is null or empty
	 */
	private List<RegistrationCenterDto> convertRegistrationCenterToDto(List<RegistrationCenter> list) {
		if (list == null || list.isEmpty()) {
			return new ArrayList<>();
		}
		return list.stream()
				.map(this::copyRegistrationCenterProperties)
				.collect(Collectors.toList());
	}

	/**
	 * Copies properties from a {@link RegistrationCenter} entity to a {@link RegistrationCenterDto}.
	 *
	 * @param regEntity the registration center entity
	 * @return a {@link RegistrationCenterDto} with copied properties
	 */
	private RegistrationCenterDto copyRegistrationCenterProperties(RegistrationCenter regEntity) {
		RegistrationCenterDto regDTO = new RegistrationCenterDto();
		regDTO.setId(regEntity.getId());
		regDTO.setName(regEntity.getName());
		regDTO.setCenterTypeCode(regEntity.getCenterTypeCode());
		regDTO.setAddressLine1(regEntity.getAddressLine1());
		regDTO.setAddressLine2(regEntity.getAddressLine2());
		regDTO.setAddressLine3(regEntity.getAddressLine3());
		regDTO.setLatitude(regEntity.getLatitude());
		regDTO.setLangCode(regEntity.getLongitude());
		regDTO.setLocationCode(regEntity.getLocationCode());
		regDTO.setHolidayLocationCode(regEntity.getHolidayLocationCode());
		regDTO.setContactPhone(regEntity.getContactPhone());
		regDTO.setWorkingHours(regEntity.getWorkingHours());
		regDTO.setNumberOfKiosks(regEntity.getNumberOfKiosks());
		regDTO.setPerKioskProcessTime(regEntity.getPerKioskProcessTime());
		regDTO.setCenterStartTime(regEntity.getCenterStartTime());
		regDTO.setCenterEndTime(regEntity.getCenterEndTime());
		regDTO.setTimeZone(regEntity.getTimeZone());
		regDTO.setContactPerson(regEntity.getContactPerson());
		regDTO.setLunchStartTime(regEntity.getLunchStartTime());
		regDTO.setLunchEndTime(regEntity.getLunchEndTime());
		regDTO.setIsDeleted(regEntity.getIsDeleted());
		regDTO.setIsActive(regEntity.getIsActive());
		regDTO.setLangCode(regEntity.getLangCode());
		return regDTO;
	}

	/**
	 * Fetches template details asynchronously.
	 *
	 * @param moduleId        the module ID to filter templates; may be null
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link TemplateDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<TemplateDto>> getTemplates(String moduleId, LocalDateTime lastUpdated,
															 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("Template", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}
		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<Template> templateList = templateRepository.findAllLatestCreatedUpdateDeletedByModule(effectiveLastUpdated, currentTimeStamp, moduleId);
			return CompletableFuture.completedFuture(convertTemplateEntityToDto(templateList));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch templates for moduleId: {}", moduleId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch templates: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Template} entities to {@link TemplateDto} objects.
	 *
	 * @param templateList the list of template entities
	 * @return a list of {@link TemplateDto}; returns empty list if input is null or empty
	 */
	private List<TemplateDto> convertTemplateEntityToDto(List<Template> templateList) {
		if (templateList == null || templateList.isEmpty()) {
			return new ArrayList<>();
		}
		return templateList.stream()
				.map(entity -> {
					TemplateDto dto = new TemplateDto(entity.getId(), entity.getName(), entity.getDescription(),
							entity.getFileFormatCode(), entity.getModel(), entity.getFileText(), entity.getModuleId(),
							entity.getModuleName(), entity.getTemplateTypeCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches template file format details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link TemplateFileFormatDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<TemplateFileFormatDto>> getTemplateFileFormats(LocalDateTime lastUpdated,
																				 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("TemplateFileFormat", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<TemplateFileFormat> templateTypes = templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertTemplateFileFormatEntityToDto(templateTypes));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch template file formats", e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch template file formats: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link TemplateFileFormat} entities to {@link TemplateFileFormatDto} objects.
	 *
	 * @param templateTypesList the list of template file format entities
	 * @return a list of {@link TemplateFileFormatDto}; returns empty list if input is null or empty
	 */
	private List<TemplateFileFormatDto> convertTemplateFileFormatEntityToDto(List<TemplateFileFormat> templateTypesList) {
		if (templateTypesList == null || templateTypesList.isEmpty()) {
			return new ArrayList<>();
		}
		return templateTypesList.stream()
				.map(entity -> {
					TemplateFileFormatDto dto = new TemplateFileFormatDto(entity.getCode(), entity.getDescription());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches reason category details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link PostReasonCategoryDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<PostReasonCategoryDto>> getReasonCategory(LocalDateTime lastUpdated,
																			LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ReasonCategory", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}
		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ReasonCategory> reasons = reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertPostReasonCategoryEntityToDto(reasons));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch reason categories", e);
			throw new SyncDataServiceException(MasterDataErrorCode.REASON_CATEGORY_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch reason categories: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ReasonCategory} entities to {@link PostReasonCategoryDto} objects.
	 *
	 * @param reasons the list of reason category entities
	 * @return a list of {@link PostReasonCategoryDto}; returns empty list if input is null or empty
	 */
	private List<PostReasonCategoryDto> convertPostReasonCategoryEntityToDto(List<ReasonCategory> reasons) {
		if (reasons == null || reasons.isEmpty()) {
			return new ArrayList<>();
		}
		return reasons.stream()
				.map(entity -> {
					PostReasonCategoryDto dto = new PostReasonCategoryDto(entity.getCode(), entity.getDescription(), entity.getName());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches reason list details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ReasonListDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ReasonListDto>> getReasonList(LocalDateTime lastUpdated,
																LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ReasonList", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ReasonList> reasons = reasonListRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertReasonListEntityToDto(reasons));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch reason list", e);
			throw new SyncDataServiceException(MasterDataErrorCode.REASON_LIST_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch reason list: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ReasonList} entities to {@link ReasonListDto} objects.
	 *
	 * @param reasons the list of reason list entities
	 * @return a list of {@link ReasonListDto}; returns empty list if input is null or empty
	 */
	private List<ReasonListDto> convertReasonListEntityToDto(List<ReasonList> reasons) {
		if (reasons == null || reasons.isEmpty()) {
			return new ArrayList<>();
		}
		return reasons.stream()
				.map(entity -> {
					ReasonListDto dto = new ReasonListDto(entity.getCode(), entity.getName(), entity.getDescription(),
							entity.getRsnCatCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches holiday details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param machineId       the machine ID to filter holidays
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link HolidayDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<HolidayDto>> getHolidays(LocalDateTime lastUpdated, String machineId,
														   LocalDateTime currentTimeStamp) {

		if (!isChangesFound("Holiday", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<Holiday> holidays = holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(machineId, effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertEntityToHoliday(holidays));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch holidays for machineId: {}", machineId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.HOLIDAY_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch holidays: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Holiday} entities to {@link HolidayDto} objects.
	 *
	 * @param holidays the list of holiday entities
	 * @return a list of {@link HolidayDto}; returns empty list if input is null or empty
	 */
	private List<HolidayDto> convertEntityToHoliday(List<Holiday> holidays) {
		if (holidays == null || holidays.isEmpty()) {
			return new ArrayList<>();
		}
		return holidays.stream()
				.map(holiday -> {
					LocalDate date = holiday.getHolidayId().getHolidayDate();
					HolidayID holidayId = holiday.getHolidayId();
					HolidayDto dto = new HolidayDto();
					dto.setHolidayId(String.valueOf(holiday.getId()));
					dto.setHolidayDate(String.valueOf(date));
					dto.setHolidayName(holidayId.getHolidayName());
					dto.setLangCode(holidayId.getLangCode());
					dto.setHolidayYear(String.valueOf(date.getYear()));
					dto.setHolidayMonth(String.valueOf(date.getMonth().getValue()));
					dto.setHolidayDay(String.valueOf(date.getDayOfWeek().getValue()));
					dto.setIsActive(holiday.getIsActive());
					dto.setLocationCode(holidayId.getLocationCode());
					dto.setIsDeleted(holiday.getIsDeleted());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches blacklisted words asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link BlacklistedWordsDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<BlacklistedWordsDto>> getBlackListedWords(LocalDateTime lastUpdated,
																			LocalDateTime currentTimeStamp) {

		if (!isChangesFound("BlacklistedWords", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<BlocklistedWords> words = blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertBlocklistedWordsEntityToDto(words));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch blacklisted words", e);
			throw new SyncDataServiceException(MasterDataErrorCode.BLACKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch blacklisted words: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link BlocklistedWords} entities to {@link BlacklistedWordsDto} objects.
	 *
	 * @param words the list of blocklisted words entities
	 * @return a list of {@link BlacklistedWordsDto}; returns empty list if input is null or empty
	 */
	private List<BlacklistedWordsDto> convertBlocklistedWordsEntityToDto(List<BlocklistedWords> words) {
		if (words == null || words.isEmpty()) {
			return new ArrayList<>();
		}
		return words.stream()
				.map(entity -> {
					BlacklistedWordsDto dto = new BlacklistedWordsDto(entity.getWord(), entity.getDescription());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches document type details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link DocumentTypeDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<DocumentTypeDto>> getDocumentTypes(LocalDateTime lastUpdated,
																	 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("DocumentType", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<DocumentType> documentTypes = documentTypeRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertDocumentTypeEntityToDto(documentTypes));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch document types", e);
			throw new SyncDataServiceException(MasterDataErrorCode.DOCUMENT_TYPE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch document types: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link DocumentType} entities to {@link DocumentTypeDto} objects.
	 *
	 * @param documentTypes the list of document type entities
	 * @return a list of {@link DocumentTypeDto}; returns empty list if input is null or empty
	 */
	private List<DocumentTypeDto> convertDocumentTypeEntityToDto(List<DocumentType> documentTypes) {
		if (documentTypes == null || documentTypes.isEmpty()) {
			return new ArrayList<>();
		}
		return documentTypes.stream()
				.map(entity -> {
					DocumentTypeDto dto = new DocumentTypeDto(entity.getCode(), entity.getName(), entity.getDescription());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());

	}

	/**
	 * Fetches location hierarchy details asynchronously from the database.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link LocationDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<LocationDto>> getLocationHierarchy(LocalDateTime lastUpdated,
																	 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("Location", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<Location> locations = locationRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertLocationsEntityToDto(locations));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch locations", e);
			throw new SyncDataServiceException(MasterDataErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch locations: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Location} entities to {@link LocationDto} objects.
	 *
	 * @param locations the list of location entities
	 * @return a list of {@link LocationDto}; returns empty list if input is null or empty
	 */
	private List<LocationDto> convertLocationsEntityToDto(List<Location> locations) {
		if (locations == null || locations.isEmpty()) {
			return new ArrayList<>();
		}
		return locations.stream()
				.map(entity -> {
					LocationDto dto = new LocationDto(entity.getCode(), entity.getName(), entity.getHierarchyLevel(),
							entity.getHierarchyName(), entity.getParentLocCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches template type details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link TemplateTypeDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<TemplateTypeDto>> getTemplateTypes(LocalDateTime lastUpdated,
																	 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("TemplateType", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<TemplateType> templateTypes = templateTypeRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertTemplateTypeEntityToDto(templateTypes));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch template types", e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch template types: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link TemplateType} entities to {@link TemplateTypeDto} objects.
	 *
	 * @param templateTypesList the list of template type entities
	 * @return a list of {@link TemplateTypeDto}; returns empty list if input is null or empty
	 */
	private List<TemplateTypeDto> convertTemplateTypeEntityToDto(List<TemplateType> templateTypesList) {
		if (templateTypesList == null || templateTypesList.isEmpty()) {
			return new ArrayList<>();
		}
		return templateTypesList.stream()
				.map(entity -> {
					TemplateTypeDto dto = new TemplateTypeDto(entity.getCode(), entity.getDescription());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches valid document mappings asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ValidDocumentDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ValidDocumentDto>> getValidDocuments(LocalDateTime lastUpdated,
																	   LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ValidDocument", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ValidDocument> validDocuments = validDocumentRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertValidDocumentEntityToDto(validDocuments));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch valid documents", e);
			throw new SyncDataServiceException(MasterDataErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch valid documents: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ValidDocument} entities to {@link ValidDocumentDto} objects.
	 *
	 * @param validDocuments the list of valid document entities
	 * @return a list of {@link ValidDocumentDto}; returns empty list if input is null or empty
	 */
	private List<ValidDocumentDto> convertValidDocumentEntityToDto(List<ValidDocument> validDocuments) {
		if (validDocuments == null || validDocuments.isEmpty()) {
			return new ArrayList<>();
		}
		return validDocuments.stream()
				.map(entity -> {
					ValidDocumentDto dto = new ValidDocumentDto(entity.getDocTypeCode(), entity.getDocCategoryCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches registration center machine mappings asynchronously.
	 *
	 * @param regCenterId     the registration center ID
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @param machineId       the machine ID to filter; may be null
	 * @return a {@link CompletableFuture} containing a list of {@link RegistrationCenterMachineDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterMachineDto>> getRegistrationCenterMachines(String regCenterId,
																							   LocalDateTime lastUpdated, LocalDateTime currentTimeStamp, String machineId) {

		if (!isChangesFound("Machine", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<Machine> machines = machineRepository.findMachineLatestCreatedUpdatedDeleted(regCenterId, effectiveLastUpdated, currentTimeStamp, machineId);
			return CompletableFuture.completedFuture(convertToRegistrationCenterMachineDto(machines));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch registration center machines for regCenterId: {}", regCenterId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch registration center machines: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Machine} entities to {@link RegistrationCenterMachineDto} objects.
	 *
	 * @param machines the list of machine entities
	 * @return a list of {@link RegistrationCenterMachineDto}; returns empty list if input is null or empty
	 */
	private List<RegistrationCenterMachineDto> convertToRegistrationCenterMachineDto(List<Machine> machines) {
		if (machines == null || machines.isEmpty()) {
			return new ArrayList<>();
		}
		return machines.stream()
				.map(machine -> {
					RegistrationCenterMachineDto dto = new RegistrationCenterMachineDto();
					dto.setIsActive(machine.getIsActive());
					dto.setIsDeleted(machine.getIsDeleted());
					dto.setLangCode(machine.getLangCode());
					dto.setMachineId(machine.getId());
					dto.setRegCenterId(machine.getRegCenterId());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches registration center user mappings asynchronously.
	 *
	 * @param regId           the registration center ID
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link RegistrationCenterUserDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterUserDto>> getRegistrationCenterUsers(String regId,
																						 LocalDateTime lastUpdated, LocalDateTime currentTimeStamp) {

		if (!isChangesFound("UserDetails", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<UserDetails> userDetails = userDetailsRepository.findAllLatestCreatedUpdatedDeleted(regId, effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertToRegistrationCenterUserDto(userDetails));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch registration center users for regId: {}", regId, e);
			throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_USER_FETCH_EXCEPTION.getErrorCode(),
					"Failed to fetch registration center users: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link UserDetails} entities to {@link RegistrationCenterUserDto} objects.
	 *
	 * @param userDetails the list of user details entities
	 * @return a list of {@link RegistrationCenterUserDto}; returns empty list if input is null or empty
	 */
	private List<RegistrationCenterUserDto> convertToRegistrationCenterUserDto(List<UserDetails> userDetails) {
		if (userDetails == null || userDetails.isEmpty()) {
			return new ArrayList<>();
		}
		return userDetails.stream()
				.map(userDetail -> {
					RegistrationCenterUserDto dto = new RegistrationCenterUserDto();
					dto.setIsActive(userDetail.getIsActive());
					dto.setIsDeleted(userDetail.getIsDeleted());
					dto.setLangCode(userDetail.getLangCode());
					dto.setUserId(userDetail.getId());
					dto.setRegCenterId(userDetail.getRegCenterId());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches applicant valid document mappings asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ApplicantValidDocumentDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ApplicantValidDocumentDto>> getApplicantValidDocument(LocalDateTime lastUpdatedTime,
																						LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ApplicantValidDocument", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ApplicantValidDocument> applicantValidDocuments = applicantValidDocumentRepository.findAllByTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertApplicantValidDocumentEntityToDto(applicantValidDocuments));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch applicant valid documents", e);
			throw new SyncDataServiceException(MasterDataErrorCode.APPLICANT_VALID_DOCUMENT_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APPLICANT_VALID_DOCUMENT_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ApplicantValidDocument} entities to {@link ApplicantValidDocumentDto} objects.
	 *
	 * @param applicantValidDocuments the list of applicant valid document entities
	 * @return a list of {@link ApplicantValidDocumentDto}; returns empty list if input is null or empty
	 */
	private List<ApplicantValidDocumentDto> convertApplicantValidDocumentEntityToDto(List<ApplicantValidDocument> applicantValidDocuments) {
		if (applicantValidDocuments == null || applicantValidDocuments.isEmpty()) {
			return new ArrayList<>();
		}
		return applicantValidDocuments.stream()
				.map(entity -> {
					ApplicantValidDocumentDto dto = new ApplicantValidDocumentDto(
							entity.getApplicantValidDocumentId().getAppTypeCode(),
							entity.getApplicantValidDocumentId().getDocTypeCode(),
							entity.getApplicantValidDocumentId().getDocCatCode(),
							entity.getLangCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches app authentication method details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link AppAuthenticationMethodDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<AppAuthenticationMethodDto>> getAppAuthenticationMethodDetails(
			LocalDateTime lastUpdatedTime, LocalDateTime currentTimeStamp) {

		if (!isChangesFound("AppAuthenticationMethod", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<AppAuthenticationMethod> appAuthenticationMethods = appAuthenticationMethodRepository
					.findByLastUpdatedAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertAppAuthMethodEntityToDto(appAuthenticationMethods));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch app authentication methods", e);
			throw new SyncDataServiceException(MasterDataErrorCode.APP_AUTHORIZATION_METHOD_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APP_AUTHORIZATION_METHOD_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link AppAuthenticationMethod} entities to {@link AppAuthenticationMethodDto} objects.
	 *
	 * @param appAuthenticationMethods the list of app authentication method entities
	 * @return a list of {@link AppAuthenticationMethodDto}; returns empty list if input is null or empty
	 */
	private List<AppAuthenticationMethodDto> convertAppAuthMethodEntityToDto(List<AppAuthenticationMethod> appAuthenticationMethods) {
		if (appAuthenticationMethods == null || appAuthenticationMethods.isEmpty()) {
			return new ArrayList<>();
		}
		return appAuthenticationMethods.stream()
				.map(entity -> {
					AppAuthenticationMethodDto dto = new AppAuthenticationMethodDto(
							entity.getAppId(), entity.getProcessId(), entity.getRoleCode(),
							entity.getAuthMethodCode(), entity.getMethodSequence(), entity.getLangCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches app role priority details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link AppRolePriorityDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<AppRolePriorityDto>> getAppRolePriorityDetails(LocalDateTime lastUpdatedTime,
																				 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("AppRolePriority", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<AppRolePriority> appRolePriorities = appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertAppRolePrioritiesToDto(appRolePriorities));
		} catch (DataAccessException ex) {
			throw new SyncDataServiceException(MasterDataErrorCode.APP_ROLE_PRIORITY_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APP_ROLE_PRIORITY_FETCH_EXCEPTION.getErrorMessage());
		}
	}

	/**
	 * Converts a list of {@link AppRolePriority} entities to {@link AppRolePriorityDto} objects.
	 *
	 * @param appRolePriorities the list of app role priority entities
	 * @return a list of {@link AppRolePriorityDto}; returns empty list if input is null or empty
	 */
	private List<AppRolePriorityDto> convertAppRolePrioritiesToDto(List<AppRolePriority> appRolePriorities) {
		if (appRolePriorities == null || appRolePriorities.isEmpty()) {
			return new ArrayList<>();
		}
		return appRolePriorities.stream()
				.map(entity -> {
					AppRolePriorityDto dto = new AppRolePriorityDto(
							entity.getAppId(), entity.getProcessId(), entity.getRoleCode(),
							entity.getPriority(), entity.getLangCode());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches screen authorization details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ScreenAuthorizationDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ScreenAuthorizationDto>> getScreenAuthorizationDetails(LocalDateTime lastUpdatedTime,
																						 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ScreenAuthorization", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ScreenAuthorization> screenAuthorizationList = screenAuthorizationRepository
					.findByLastUpdatedAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertScreenAuthorizationToDto(screenAuthorizationList));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch screen authorizations", e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCREEN_AUTHORIZATION_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.SCREEN_AUTHORIZATION_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ScreenAuthorization} entities to {@link ScreenAuthorizationDto} objects.
	 *
	 * @param screenAuthorizationList the list of screen authorization entities
	 * @return a list of {@link ScreenAuthorizationDto}; returns empty list if input is null or empty
	 */
	private List<ScreenAuthorizationDto> convertScreenAuthorizationToDto(
			List<ScreenAuthorization> screenAuthorizationList) {
		if (screenAuthorizationList == null || screenAuthorizationList.isEmpty()) {
			return new ArrayList<>();
		}
		return screenAuthorizationList.stream()
				.map(entity -> {
					ScreenAuthorizationDto dto = new ScreenAuthorizationDto(
							entity.getScreenId(), entity.getRoleCode(), entity.getIsPermitted());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches process list details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ProcessListDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ProcessListDto>> getProcessList(LocalDateTime lastUpdatedTime,
																  LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ProcessList", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ProcessList> processList = processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertProcessListEntityToDto(processList));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch process list", e);
			throw new SyncDataServiceException(MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ProcessList} entities to {@link ProcessListDto} objects.
	 *
	 * @param processList the list of process list entities
	 * @return a list of {@link ProcessListDto}; returns empty list if input is null or empty
	 */
	private List<ProcessListDto> convertProcessListEntityToDto(List<ProcessList> processList) {
		if (processList == null || processList.isEmpty()) {
			return new ArrayList<>();
		}
		return processList.stream()
				.map(entity -> {
					ProcessListDto dto = new ProcessListDto(entity.getId(), entity.getName(), entity.getDescr());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches sync job definition details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link SyncJobDefDto}
	 * @throws AdminServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<SyncJobDefDto>> getSyncJobDefDetails(LocalDateTime lastUpdatedTime,
																	   LocalDateTime currentTimeStamp) {

		if (!isChangesFound("SyncJobDef", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<SyncJobDef> syncJobDefs = syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertSyncJobDefEntityToDto(syncJobDefs));
		} catch (DataAccessException | DataAccessLayerException e) {
			logger.error("Failed to fetch sync job definitions", e);
			throw new AdminServiceException(AdminServiceErrorCode.SYNC_JOB_DEF_FETCH_EXCEPTION.getErrorCode(),
					AdminServiceErrorCode.SYNC_JOB_DEF_FETCH_EXCEPTION.getErrorMessage() + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link SyncJobDef} entities to {@link SyncJobDefDto} objects.
	 *
	 * @param syncJobDefs the list of sync job definition entities
	 * @return a list of {@link SyncJobDefDto}; returns empty list if input is null or empty
	 */
	private List<SyncJobDefDto> convertSyncJobDefEntityToDto(List<SyncJobDef> syncJobDefs) {
		if (syncJobDefs == null || syncJobDefs.isEmpty()) {
			return new ArrayList<>();
		}
		return syncJobDefs.stream()
				.map(entity -> new SyncJobDefDto(
						entity.getId(), entity.getName(), entity.getApiName(),
						entity.getParentSyncJobId(), entity.getSyncFreq(), entity.getLockDuration(),
						entity.getIsActive(), entity.getIsDeleted(), entity.getLangCode()))
				.collect(Collectors.toList());
	}

	/**
	 * Fetches screen detail details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link ScreenDetailDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<ScreenDetailDto>> getScreenDetails(LocalDateTime lastUpdatedTime,
																	 LocalDateTime currentTimeStamp) {

		if (!isChangesFound("ScreenDetail", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<ScreenDetail> screenDetails = screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertScreenDetailToDto(screenDetails));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch screen details", e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCREEN_DETAIL_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.SCREEN_DETAIL_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link ScreenDetail} entities to {@link ScreenDetailDto} objects.
	 *
	 * @param screenDetails the list of screen detail entities
	 * @return a list of {@link ScreenDetailDto}; returns empty list if input is null or empty
	 */
	private List<ScreenDetailDto> convertScreenDetailToDto(List<ScreenDetail> screenDetails) {
		if (screenDetails == null || screenDetails.isEmpty()) {
			return new ArrayList<>();
		}
		return screenDetails.stream()
				.map(entity -> {
					ScreenDetailDto dto = new ScreenDetailDto(entity.getId(), entity.getAppId(), entity.getName(), entity.getDescr());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					dto.setLangCode(entity.getLangCode());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches dynamic field details asynchronously from an external service.
	 *
	 * @param lastUpdated the last updated timestamp; if null, fetches all records
	 * @return a {@link CompletableFuture} containing a list of {@link DynamicFieldDto}
	 * @throws SyncDataServiceException if an error occurs during data access or deserialization
	 */
	@Async
	public CompletableFuture<List<DynamicFieldDto>> getAllDynamicFields(LocalDateTime lastUpdated) {
		return getAllDynamicFields(lastUpdated, restTemplate);
	}

	/**
	 * Fetches dynamic field details asynchronously using a specified RestTemplate.
	 *
	 * @param lastUpdated the last updated timestamp; if null, fetches all records
	 * @param restClient  the RestTemplate to use for the HTTP request
	 * @return a {@link CompletableFuture} containing a list of {@link DynamicFieldDto}
	 * @throws SyncDataServiceException if an error occurs during data access or deserialization
	 */
	@Async
	public CompletableFuture<List<DynamicFieldDto>> getAllDynamicFields(LocalDateTime lastUpdated, RestTemplate restClient) {
		List<DynamicFieldDto> result = new ArrayList<>();
		try {
			int pageNo = 0;
			PageDto<DynamicFieldDto> pageDto;
			do {
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(dynamicfieldUrl);
				if (lastUpdated != null) {
					builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated));
				}
				builder.queryParam("pageNumber", pageNo++);
				ResponseEntity<String> responseEntity = restClient.getForEntity(builder.build().toUri(), String.class);

				ResponseWrapper<PageDto<DynamicFieldDto>> resp = objectMapper.readValue(responseEntity.getBody(),
						new TypeReference<ResponseWrapper<PageDto<DynamicFieldDto>>>() {
						});

				if (resp.getErrors() != null && !resp.getErrors().isEmpty()) {
					throw new SyncInvalidArgumentException(resp.getErrors());
				}

				pageDto = resp.getResponse();
				result.addAll(pageDto.getData());
			} while (pageNo < pageDto.getTotalPages());

			return CompletableFuture.completedFuture(result);
		} catch (Exception e) {
			logger.error("Failed to fetch dynamic fields", e);
			throw new SyncDataServiceException(MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorCode(),
					MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorMessage() + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Fetches permitted configuration details asynchronously.
	 *
	 * @param lastUpdated     the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link PermittedConfigDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<PermittedConfigDto>> getPermittedConfig(LocalDateTime lastUpdated,
																		  LocalDateTime currentTimeStamp) {

		if (!isChangesFound("PermittedLocalConfig", lastUpdated)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<PermittedLocalConfig> list = permittedLocalConfigRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertPermittedConfigEntityToDto(list));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch permitted configurations", e);
			throw new SyncDataServiceException(MasterDataErrorCode.PERMITTED_CONFIG_FETCH_FAILED.getErrorCode(),
					"Failed to fetch permitted configurations: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link PermittedLocalConfig} entities to {@link PermittedConfigDto} objects.
	 *
	 * @param permittedLocalConfigList the list of permitted local configuration entities
	 * @return a list of {@link PermittedConfigDto}; returns empty list if input is null or empty
	 */
	private List<PermittedConfigDto> convertPermittedConfigEntityToDto(List<PermittedLocalConfig> permittedLocalConfigList) {
		if (permittedLocalConfigList == null || permittedLocalConfigList.isEmpty()) {
			return new ArrayList<>();
		}
		return permittedLocalConfigList.stream()
				.map(entity -> {
					PermittedConfigDto dto = new PermittedConfigDto(entity.getCode(), entity.getName(), entity.getType());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Fetches language details asynchronously.
	 *
	 * @param lastUpdatedTime the last updated timestamp; if null, fetches all records
	 * @param currentTimeStamp the current timestamp for filtering
	 * @return a {@link CompletableFuture} containing a list of {@link LanguageDto}
	 * @throws SyncDataServiceException if an error occurs during data access
	 */
	@Async
	public CompletableFuture<List<LanguageDto>> getLanguageList(LocalDateTime lastUpdatedTime,
																LocalDateTime currentTimeStamp) {

		if (!isChangesFound("Language", lastUpdatedTime)) {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

		try {
			LocalDateTime effectiveLastUpdated = lastUpdatedTime != null ? lastUpdatedTime : LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			List<Language> list = languageRepository.findAllLatestCreatedUpdateDeleted(effectiveLastUpdated, currentTimeStamp);
			return CompletableFuture.completedFuture(convertLanguageEntityToDto(list));
		} catch (DataAccessException e) {
			logger.error("Failed to fetch languages", e);
			throw new SyncDataServiceException(MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorMessage(), e);
		}
	}

	/**
	 * Converts a list of {@link Language} entities to {@link LanguageDto} objects.
	 *
	 * @param languageList the list of language entities
	 * @return a list of {@link LanguageDto}; returns empty list if input is null or empty
	 */
	private List<LanguageDto> convertLanguageEntityToDto(List<Language> languageList) {
		if (languageList == null || languageList.isEmpty()) {
			return new ArrayList<>();
		}
		return languageList.stream()
				.map(entity -> {
					LanguageDto dto = new LanguageDto(entity.getCode(), entity.getName(), entity.getFamily(), entity.getNativeName());
					dto.setIsDeleted(entity.getIsDeleted());
					dto.setIsActive(entity.getIsActive());
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Converts and encrypts entity data to {@link SyncDataBaseDto} objects.
	 *
	 * @param entityName                the name of the entity
	 * @param entityType                the type of the entity
	 * @param entities                  the list of entities to process
	 * @param registrationCenterMachineDto the registration center machine DTO for encryption
	 * @param result                    the list to store the resulting {@link SyncDataBaseDto}
	 */
	@SuppressWarnings("unchecked")
	public void getSyncDataBaseDto(String entityName, String entityType, List entities, RegistrationCenterMachineDto
			registrationCenterMachineDto, List result) {
		if (entities == null || entities.isEmpty()) {
			return;
		}
		try {
			String json = mapper.getObjectAsJsonString(entities);
			if (json != null) {
				TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
				tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(json.getBytes()));
				tpmCryptoRequestDto.setPublicKey(registrationCenterMachineDto.getPublicKey());
				tpmCryptoRequestDto.setClientType(registrationCenterMachineDto.getClientType());
				TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
				result.add(new SyncDataBaseDto(
						entityName.equalsIgnoreCase(BlocklistedWords.class.getSimpleName()) ? "BlacklistedWords" : entityName,
						entityType, tpmCryptoResponseDto.getValue()));
			}
		} catch (Exception e) {
			logger.error("Failed to encrypt {} data to JSON", entityName, e);
		}
	}

	/**
	 * Converts and encrypts entity data to {@link SyncDataBaseDto} objects (version 2).
	 *
	 * @param entityName                the name of the entity
	 * @param entityType                the type of the entity
	 * @param entities                  the list of entities to process
	 * @param result                    the list to store the resulting {@link SyncDataBaseDto}
	 */
	public void getSyncDataBaseDtoV2(String entityName, String entityType,
									 List<?> entities,
									 RegistrationCenterMachineDto regCenterMachineDto,
									 List<SyncDataBaseDto> result) {
		if (entities == null || entities.isEmpty()) return;

		List<?> filtered = entities.stream().filter(Objects::nonNull).toList();
		if (filtered.isEmpty()) return;

		try {
			String json = mapper.getObjectAsJsonString(filtered);
			if (json != null) {
				TpmCryptoRequestDto req = new TpmCryptoRequestDto();
				req.setValue(CryptoUtil.encodeToURLSafeBase64(json.getBytes(StandardCharsets.UTF_8)));
				req.setPublicKey(regCenterMachineDto.getPublicKey());
				req.setClientType(regCenterMachineDto.getClientType());

				TpmCryptoResponseDto enc = clientCryptoManagerService.csEncrypt(req);
				result.add(new SyncDataBaseDto(entityName, entityType, enc.getValue()));
			}
		} catch (Exception e) {
			logger.error("Failed to encrypt {} data to JSON", entityName, e);
		}
	}
	/**
	 * Fetches registration center machine details by key index.
	 *
	 * @param registrationCenterId the registration center ID; may be null
	 * @param keyIndex            the key index to query
	 * @return a {@link RegistrationCenterMachineDto} with machine and registration center details
	 * @throws SyncDataServiceException if the machine is not found or registration center validation fails
	 */
	public RegistrationCenterMachineDto getRegistrationCenterMachine(String registrationCenterId, String keyIndex)
			throws SyncDataServiceException {
		try {
			//get the machine entry without status check
			Machine machine = machineRepository.findOneByKeyIndexIgnoreCase(keyIndex);
			if (machine == null) {
				throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
						MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());
			}

			String mappedRegCenterId = machine.getRegCenterId();
			if (mappedRegCenterId == null)
				throw new RequestException(MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
						MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());

			if (registrationCenterId != null && !mappedRegCenterId.equals(registrationCenterId))
				throw new RequestException(MasterDataErrorCode.REG_CENTER_UPDATED.getErrorCode(),
						MasterDataErrorCode.REG_CENTER_UPDATED.getErrorMessage());

			return new RegistrationCenterMachineDto(mappedRegCenterId,machine.getId(), machine.getPublicKey(),
					machine.getMachineSpecId(), machine.getMachineSpecification() != null ?
					machine.getMachineSpecification().getMachineTypeCode() : null, getClientType(machine));

		} catch (DataAccessException | DataAccessLayerException e) {
			logger.error("Failed to fetch registrationCenterMachine : ", e);
		}

		throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
				MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorMessage());
	}

	/**
	 * Determines the client type based on the machine's specification.
	 * Returns {@link ClientType#ANDROID} for Android machines, or null otherwise.
	 *
	 * @param machine the {@link Machine} entity to evaluate
	 * @return the {@link ClientType} or null if the machine specification is unavailable or not Android
	 */
	public static ClientType getClientType(Machine machine) {
		if (machine == null || machine.getMachineSpecification() == null) {
			return null;
		}
		return ANDROID_MACHINE_TYPE_CODE.equalsIgnoreCase(machine.getMachineSpecification().getMachineTypeCode())
				? ClientType.ANDROID
				: null;
	}

	/**
	 * Checks if there are changes in the specified entity since the last updated timestamp.
	 * Uses cached timestamps to minimize database queries.
	 *
	 * @param entityName   the name of the entity to check (e.g., "Machine", "RegistrationCenter")
	 * @param lastUpdated  the last updated timestamp; if null, assumes full sync is required
	 * @return true if changes are found or full sync is required, false otherwise
	 * @throws SyncDataServiceException if an error occurs while fetching timestamps
	 */
	private boolean isChangesFound(String entityName, LocalDateTime lastUpdated) {
		if(lastUpdated == null) //if it's null, then the request is for full sync
			return true;

		EntityDtimes result = null;
		switch (entityName) {
			case "AppAuthenticationMethod":
				result = appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "AppRolePriority":
				result = appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "Machine":
				result = machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "RegistrationCenter":
				result = registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "UserDetails":
				result = userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "Template":
				result = templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "DocumentType":
				result = documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ApplicantValidDocument":
				result = applicantValidDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "Location":
				result = locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ReasonCategory":
				result = reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ReasonList":
				result = reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "Holiday":
				result = holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "BlacklistedWords":
				result = blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ScreenAuthorization":
				result = screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ScreenDetail":
				result = screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ProcessList":
				result = processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "SyncJobDef":
				result = syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "PermittedLocalConfig":
				result = permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "TemplateFileFormat":
				result = templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "TemplateType":
				result = templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "ValidDocument":
				result = validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
			case "Language":
				result = languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
				break;
		}
		if(result == null) {
			logger.info("** No data found in the table : {}", entityName);
			return false;
		}

		return ( (result.getDeletedDateTime() != null && lastUpdated.isBefore(result.getDeletedDateTime())) ||
				(result.getUpdatedDateTime() != null && lastUpdated.isBefore(result.getUpdatedDateTime())) ||
				(result.getCreatedDateTime() != null && lastUpdated.isBefore(result.getCreatedDateTime())) );
	}
}