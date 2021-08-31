package io.mosip.kernel.syncdata.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.syncdata.constant.AdminServiceErrorCode;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.exception.*;
import io.mosip.kernel.syncdata.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;

/**
 * Sync handler masterData service helper
 * 
 * @author Abhishek Kumar
 * @author Srinivasan
 * @since 1.0.0
 */
@Component
public class SyncMasterDataServiceHelper {

	private Logger logger = LoggerFactory.getLogger(SyncMasterDataServiceHelper.class);

	@Autowired
	private MapperUtils mapper;
	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private MachineRepository machineRepository;
	@Autowired
	private MachineTypeRepository machineTypeRepository;
	@Autowired
	private RegistrationCenterRepository registrationCenterRepository;
	@Autowired
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;
	@Autowired
	private TemplateRepository templateRepository;
	@Autowired
	private TemplateFileFormatRepository templateFileFormatRepository;
	@Autowired
	private ReasonCategoryRepository reasonCategoryRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private BlacklistedWordsRepository blacklistedWordsRepository;
	@Autowired
	private BiometricTypeRepository biometricTypeRepository;
	@Autowired
	private BiometricAttributeRepository biometricAttributeRepository;
	@Autowired
	private TitleRepository titleRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DocumentCategoryRepository documentCategoryRepository;
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	@Autowired
	private IdTypeRepository idTypeRepository;
	@Autowired
	private DeviceSpecificationRepository deviceSpecificationRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private TemplateTypeRepository templateTypeRepository;
	@Autowired
	private MachineSpecificationRepository machineSpecificationRepository;
	@Autowired
	private DeviceTypeRepository deviceTypeRepository;
	@Autowired
	private ValidDocumentRepository validDocumentRepository;
	@Autowired
	private ReasonListRepository reasonListRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private ApplicantValidDocumentRespository applicantValidDocumentRepository;
	@Autowired
	private AppAuthenticationMethodRepository appAuthenticationMethodRepository;
	@Autowired
	private AppDetailRepository appDetailRepository;
	@Autowired
	private AppRolePriorityRepository appRolePriorityRepository;
	@Autowired
	private ScreenAuthorizationRepository screenAuthorizationRepository;
	@Autowired
	private ProcessListRepository processListRepository;
	@Autowired
	private ScreenDetailRepository screenDetailRepository;
	@Autowired
	private SyncJobDefRepository syncJobDefRepository;
	@Autowired
	private DeviceProviderRepository deviceProviderRepository;
	@Autowired
	private DeviceServiceRepository deviceServiceRepository;
	@Autowired
	private RegisteredDeviceRepository registeredDeviceRepository;
	@Autowired
	private FoundationalTrustProviderRepository foundationalTrustProviderRepository;
	@Autowired
	private DeviceTypeDPMRepository deviceTypeDPMRepository;
	@Autowired
	private DeviceSubTypeDPMRepository deviceSubTypeDPMRepository;
	@Autowired
	private UserDetailsHistoryRepository userDetailsHistoryRepository;
	@Autowired
	private MachineHistoryRepository machineHistoryRepository;
	@Autowired
	private DeviceHistoryRepository deviceHistoryRepository;
	@Autowired
	private PermittedLocalConfigRepository permittedLocalConfigRepository;
	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${mosip.kernel.masterdata.locationhierarchylevels.uri}")
	private String locationHirerarchyUrl;

	@Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
	private String dynamicfieldUrl;



	/**
	 * Method to fetch machine details by regCenter id
	 * 
	 * @param regCenterId      registration center id
	 * @param lastUpdated      lastUpdated time-stamp
	 * @param currentTimeStamp current time stamp
	 * 
	 * @return list of {@link MachineDto} list of machine dto
	 */
	@Async
	public CompletableFuture<List<MachineDto>> getMachines(String regCenterId, LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp, String machineId) {
		List<Machine> machineDetailList = null;
		List<MachineDto> machineDetailDtoList = new ArrayList<>();
		try {

			if(!isChangesFound("Machine", lastUpdated)) {
				return CompletableFuture.completedFuture(machineDetailDtoList);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			machineDetailList = machineRepository.findMachineLatestCreatedUpdatedDeleted(regCenterId, lastUpdated,
					currentTimeStamp, machineId);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (!machineDetailList.isEmpty()) {

			// machineDetailDtoList = MapperUtils.mapAll(machineDetailList,
			// MachineDto.class);
			machineDetailList.forEach(machine -> {
				MachineDto responseDto = new MachineDto();
				responseDto.setPublicKey(machine.getPublicKey());
				responseDto.setId(machine.getId());
				responseDto.setIpAddress(machine.getIpAddress());
				responseDto.setIsActive(machine.getIsActive());
				responseDto.setIsDeleted(machine.getIsDeleted());
				responseDto.setKeyIndex(machine.getKeyIndex());
				responseDto.setLangCode(machine.getLangCode());
				responseDto.setMacAddress(machine.getMacAddress());
				responseDto.setMachineSpecId(machine.getMachineSpecId());
				responseDto.setName(machine.getName());
				responseDto.setSerialNum(machine.getSerialNum());
				responseDto.setValidityDateTime(machine.getValidityDateTime());
				machineDetailDtoList.add(responseDto);
			});

		}

		return CompletableFuture.completedFuture(machineDetailDtoList);
	}

	/**
	 * Method to fetch location hierarchy details
	 * 
	 * @param lastUpdated      lastUpdated time-stamp
	 * 
	 * @return list of {@link LocationHierarchyDto} list of
	 *         locationHierarchyList dto
	 */
	@Async
	public CompletableFuture<List<LocationHierarchyDto>> getLocationHierarchyList(LocalDateTime lastUpdated) {
		List<LocationHierarchyDto> locationHierarchyLevelDtos = new ArrayList<LocationHierarchyDto>();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(locationHirerarchyUrl);
		if(lastUpdated != null) {	builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated)); }
		ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toUri(), String.class);

		if (response.getStatusCode().equals(HttpStatus.OK)) {
			String responseBody = response.getBody();
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
			if (!validationErrorsList.isEmpty()) {
				throw new SyncServiceException(validationErrorsList);
			}

			try {
				ResponseWrapper<?> responseObject = objectMapper.readValue(response.getBody(), ResponseWrapper.class);
				LocationHierarchyLevelResponseDto locationHierarchyResponseDto = objectMapper.readValue(
						objectMapper.writeValueAsString(responseObject.getResponse()),
						LocationHierarchyLevelResponseDto.class);
				locationHierarchyLevelDtos = locationHierarchyResponseDto.getLocationHierarchyLevels();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new SyncDataServiceException(
						MasterDataErrorCode.LOCATION_HIERARCHY_DESERIALIZATION_FAILED.getErrorCode(),
						MasterDataErrorCode.LOCATION_HIERARCHY_DESERIALIZATION_FAILED.getErrorMessage());
			}
		}
		return CompletableFuture.completedFuture(locationHierarchyLevelDtos);
	}

	@Async
	public CompletableFuture<List<LocationHierarchyDto>> getLocationHierarchyList(LocalDateTime lastUpdated, RestTemplate restClient) {
		List<LocationHierarchyDto> locationHierarchyLevelDtos = new ArrayList<LocationHierarchyDto>();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(locationHirerarchyUrl);
		if(lastUpdated != null) {	builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated)); }
		ResponseEntity<String> response = restClient.getForEntity(builder.build().toUri(), String.class);

		if (response.getStatusCode().equals(HttpStatus.OK)) {
			String responseBody = response.getBody();
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
			if (!validationErrorsList.isEmpty()) {
				throw new SyncServiceException(validationErrorsList);
			}

			try {
				ResponseWrapper<?> responseObject = objectMapper.readValue(response.getBody(), ResponseWrapper.class);
				LocationHierarchyLevelResponseDto locationHierarchyResponseDto = objectMapper.readValue(
						objectMapper.writeValueAsString(responseObject.getResponse()),
						LocationHierarchyLevelResponseDto.class);
				locationHierarchyLevelDtos = locationHierarchyResponseDto.getLocationHierarchyLevels();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new SyncDataServiceException(
						MasterDataErrorCode.LOCATION_HIERARCHY_DESERIALIZATION_FAILED.getErrorCode(),
						MasterDataErrorCode.LOCATION_HIERARCHY_DESERIALIZATION_FAILED.getErrorMessage());
			}
		}
		return CompletableFuture.completedFuture(locationHierarchyLevelDtos);
	}



	/**
	 * Method to fetch registration center detail.
	 *
	 * @param machineId        machine id
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp the current time stamp
	 * @return list of {@link RegistrationCenterDto}
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterDto>> getRegistrationCenter(String machineId,
			LocalDateTime lastUpdated, LocalDateTime currentTimeStamp) {
		List<RegistrationCenterDto> registrationCenterList = null;
		List<RegistrationCenter> list = null;
		try {
			if(!isChangesFound("RegistrationCenter", lastUpdated)) {
				return CompletableFuture.completedFuture(registrationCenterList);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			list = registrationCenterRepository.findLatestRegistrationCenterByMachineId(machineId, lastUpdated,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.APPLICATION_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (list != null && !list.isEmpty()) {
			registrationCenterList = MapperUtils.mapAll(list, RegistrationCenterDto.class);
		}

		return CompletableFuture.completedFuture(registrationCenterList);
	}



	/**
	 * Method to fetch templates
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link TemplateDto}
	 */
	@Async
	public CompletableFuture<List<TemplateDto>> getTemplates(String moduleId, LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<TemplateDto> templates = null;
		List<Template> templateList = null;
		try {
			if(!isChangesFound("Template", lastUpdated)) {
				return CompletableFuture.completedFuture(templates);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			templateList = templateRepository.findAllLatestCreatedUpdateDeletedByModule(lastUpdated, currentTimeStamp,
					moduleId);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (templateList != null && !templateList.isEmpty()) {
			templates = MapperUtils.mapAll(templateList, TemplateDto.class);
		}
		return CompletableFuture.completedFuture(templates);
	}

	/**
	 * Method to fetch template format types
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link TemplateFileFormatDto}
	 */
	@Async
	public CompletableFuture<List<TemplateFileFormatDto>> getTemplateFileFormats(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<TemplateFileFormatDto> templateFormats = null;
		List<TemplateFileFormat> templateTypes = null;
		try {
			if(!isChangesFound("TemplateFileFormat", lastUpdated)) {
				return CompletableFuture.completedFuture(templateFormats);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			templateTypes = templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(lastUpdated,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		templateFormats = MapperUtils.mapAll(templateTypes, TemplateFileFormatDto.class);
		return CompletableFuture.completedFuture(templateFormats);
	}

	/**
	 * Method to fetch reason-category
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link PostReasonCategoryDto}
	 */
	@Async
	public CompletableFuture<List<PostReasonCategoryDto>> getReasonCategory(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<PostReasonCategoryDto> reasonCategories = null;
		List<ReasonCategory> reasons = null;
		try {
			if(!isChangesFound("ReasonCategory", lastUpdated)) {
				return CompletableFuture.completedFuture(reasonCategories);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			reasons = reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.REASON_CATEGORY_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (reasons != null && !reasons.isEmpty()) {
			reasonCategories = MapperUtils.mapAll(reasons, PostReasonCategoryDto.class);
		}
		return CompletableFuture.completedFuture(reasonCategories);
	}

	/**
	 * Method to fetch Reason List
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link ReasonListDto}
	 */
	@Async
	public CompletableFuture<List<ReasonListDto>> getReasonList(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<ReasonListDto> reasonList = null;
		List<ReasonList> reasons = null;
		try {
			if(!isChangesFound("ReasonList", lastUpdated)) {
				return CompletableFuture.completedFuture(reasonList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			reasons = reasonListRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.REASON_LIST_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}

		if (reasons != null && !reasons.isEmpty())
			reasonList = MapperUtils.mapAll(reasons, ReasonListDto.class);

		return CompletableFuture.completedFuture(reasonList);
	}

	/**
	 * Method to fetch Holidays
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @param machineId        machine id
	 * @return list of {@link HolidayDto}
	 */
	@Async
	public CompletableFuture<List<HolidayDto>> getHolidays(LocalDateTime lastUpdated, String machineId,
			LocalDateTime currentTimeStamp) {
		List<HolidayDto> holidayList = null;
		List<Holiday> holidays = null;
		try {
			if(!isChangesFound("Holiday", lastUpdated)) {
				return CompletableFuture.completedFuture(holidayList);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			holidays = holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(machineId, lastUpdated,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.HOLIDAY_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}

		if (holidays != null && !holidays.isEmpty())
			holidayList = mapper.mapHolidays(holidays);

		return CompletableFuture.completedFuture(holidayList);
	}

	/**
	 * Method to fetch blacklisted words
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link BlacklistedWordsDto}
	 */
	@Async
	public CompletableFuture<List<BlacklistedWordsDto>> getBlackListedWords(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<BlacklistedWordsDto> blacklistedWords = null;
		List<BlacklistedWords> words = null;

		try {
			if(!isChangesFound("BlacklistedWords", lastUpdated)) {
				return CompletableFuture.completedFuture(blacklistedWords);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			words = blacklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.BLACKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (words != null && !words.isEmpty()) {
			blacklistedWords = MapperUtils.mapAll(words, BlacklistedWordsDto.class);
		}

		return CompletableFuture.completedFuture(blacklistedWords);
	}


	/**
	 * Method to fetch document type
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link DocumentTypeDto}
	 */
	@Async
	public CompletableFuture<List<DocumentTypeDto>> getDocumentTypes(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<DocumentTypeDto> documentTypeList = null;
		List<DocumentType> documentTypes = null;
		try {
			if(!isChangesFound("DocumentType", lastUpdated)) {
				return CompletableFuture.completedFuture(documentTypeList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			documentTypes = documentTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.DOCUMENT_TYPE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}

		if (documentTypes != null && !documentTypes.isEmpty())
			documentTypeList = MapperUtils.mapAll(documentTypes, DocumentTypeDto.class);

		return CompletableFuture.completedFuture(documentTypeList);
	}


	/**
	 * Method to fetch locations
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link LocationDto}
	 */
	@Async
	public CompletableFuture<List<LocationDto>> getLocationHierarchy(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<LocationDto> responseList = null;
		List<Location> locations = null;
		try {
			if(!isChangesFound("Location", lastUpdated)) {
				return CompletableFuture.completedFuture(responseList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}

			locations = locationRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (!locations.isEmpty()) {
			responseList = MapperUtils.mapAll(locations, LocationDto.class);
		}
		return CompletableFuture.completedFuture(responseList);
	}

	/**
	 * Method to fetch template types
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link TemplateTypeDto}
	 */
	@Async
	public CompletableFuture<List<TemplateTypeDto>> getTemplateTypes(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<TemplateTypeDto> templateTypeList = null;
		List<TemplateType> templateTypes = null;
		try {
			if(!isChangesFound("TemplateType", lastUpdated)) {
				return CompletableFuture.completedFuture(templateTypeList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			templateTypes = templateTypeRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.TEMPLATE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}

		if (templateTypes != null && !templateTypes.isEmpty())
			templateTypeList = MapperUtils.mapAll(templateTypes, TemplateTypeDto.class);

		return CompletableFuture.completedFuture(templateTypeList);
	}


	/**
	 * Method to fetch document mapping
	 * 
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link ValidDocumentDto}
	 */
	@Async
	public CompletableFuture<List<ValidDocumentDto>> getValidDocuments(LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp) {
		List<ValidDocumentDto> validDocumentList = null;
		List<ValidDocument> validDocuments = null;
		try {
			if(!isChangesFound("ValidDocument", lastUpdated)) {
				return CompletableFuture.completedFuture(validDocumentList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			validDocuments = validDocumentRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}

		if (validDocuments != null && !validDocuments.isEmpty()) {
			validDocumentList = MapperUtils.mapAll(validDocuments, ValidDocumentDto.class);
		}
		return CompletableFuture.completedFuture(validDocumentList);
	}

	/**
	 * 
	 * @param regCenterId
	 * @param lastUpdated
	 * @param currentTimeStamp
	 * @return list of {@link RegistrationCenterMachineDto}
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterMachineDto>> getRegistrationCenterMachines(String regCenterId,
			LocalDateTime lastUpdated, LocalDateTime currentTimeStamp, String machineId) {
		List<RegistrationCenterMachineDto> registrationCenterMachineDtos = new ArrayList<>();
		List<Machine> machines = null;
		try {
			if(!isChangesFound("Machine", lastUpdated)) {
				return CompletableFuture.completedFuture(registrationCenterMachineDtos);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			machines = machineRepository.findMachineLatestCreatedUpdatedDeleted(regCenterId, lastUpdated, currentTimeStamp,
					machineId);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (machines != null && !machines.isEmpty()) {
			for (Machine machine : machines) {

				RegistrationCenterMachineDto dto = new RegistrationCenterMachineDto();
				dto.setIsActive(machine.getIsActive());
				dto.setIsDeleted(machine.getIsDeleted());
				dto.setLangCode(machine.getLangCode());
				dto.setMachineId(machine.getId());
				dto.setRegCenterId(machine.getRegCenterId());
				registrationCenterMachineDtos.add(dto);

			}

		}
		return CompletableFuture.completedFuture(registrationCenterMachineDtos);
	}


	/**
	 * 
	 * @param regId            - registration center id
	 * @param lastUpdated      - last updated time
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link RegistrationCenterUserDto} - list of
	 *         RegistrationCenterUserDto
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterUserDto>> getRegistrationCenterUsers(String regId,
			LocalDateTime lastUpdated, LocalDateTime currentTimeStamp) {
		List<RegistrationCenterUserDto> registrationCenterUserDtos = new ArrayList<>();
		List<UserDetails> userDetails = null;
		try {
			if(!isChangesFound("UserDetails", lastUpdated)) {
				return CompletableFuture.completedFuture(registrationCenterUserDtos);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			userDetails = userDetailsRepository.findAllLatestCreatedUpdatedDeleted(regId, lastUpdated,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_USER_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		if (userDetails != null && !userDetails.isEmpty()) {
			for (UserDetails userDetail : userDetails) {

				RegistrationCenterUserDto dto = new RegistrationCenterUserDto();
				dto.setIsActive(userDetail.getIsActive());
				dto.setIsDeleted(userDetail.getIsDeleted());
				dto.setLangCode(userDetail.getLangCode());
				dto.setUserId(userDetail.getId());
				dto.setRegCenterId(userDetail.getRegCenterId());
				registrationCenterUserDtos.add(dto);

			}

		}
		return CompletableFuture.completedFuture(registrationCenterUserDtos);
	}

	/**
	 * 
	 * @param lastUpdatedTime  - last updated time stamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link ApplicantValidDocumentDto}
	 */
	@Async
	public CompletableFuture<List<ApplicantValidDocumentDto>> getApplicantValidDocument(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ApplicantValidDocumentDto> applicantValidDocumentDtos = null;
		List<ApplicantValidDocument> applicantValidDocuments = null;
		try {
			if(!isChangesFound("ApplicantValidDocument", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(applicantValidDocumentDtos);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			applicantValidDocuments = applicantValidDocumentRepository.findAllByTimeStamp(lastUpdatedTime,
					currentTimeStamp);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(
					MasterDataErrorCode.APPLICANT_VALID_DOCUMENT_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APPLICANT_VALID_DOCUMENT_FETCH_EXCEPTION.getErrorMessage());
		}
		if (applicantValidDocuments != null && !applicantValidDocuments.isEmpty()) {
			applicantValidDocumentDtos = MapperUtils.mapAll(applicantValidDocuments, ApplicantValidDocumentDto.class);
		}
		return CompletableFuture.completedFuture(applicantValidDocumentDtos);
	}


	@Async
	public CompletableFuture<List<AppAuthenticationMethodDto>> getAppAuthenticationMethodDetails(
			LocalDateTime lastUpdatedTime, LocalDateTime currentTimeStamp) {
		List<AppAuthenticationMethod> appAuthenticationMethods = null;
		List<AppAuthenticationMethodDto> appAuthenticationMethodDtos = null;
		try {
			if(!isChangesFound("AppAuthenticationMethod", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(appAuthenticationMethodDtos);
			}

			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}

			appAuthenticationMethods = appAuthenticationMethodRepository
					.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTimeStamp);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(
					MasterDataErrorCode.APP_AUTHORIZATION_METHOD_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APP_AUTHORIZATION_METHOD_FETCH_EXCEPTION.getErrorMessage());
		}
		if (appAuthenticationMethods != null && !appAuthenticationMethods.isEmpty()) {
			appAuthenticationMethodDtos = MapperUtils.mapAll(appAuthenticationMethods,
					AppAuthenticationMethodDto.class);
		}
		return CompletableFuture.completedFuture(appAuthenticationMethodDtos);

	}


	@Async
	public CompletableFuture<List<AppRolePriorityDto>> getAppRolePriorityDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<AppRolePriority> appRolePriorities = null;
		List<AppRolePriorityDto> appRolePriorityDtos = null;
		try {

			if(!isChangesFound("AppRolePriority", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(appRolePriorityDtos);
			}

			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			appRolePriorities = appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime,
					currentTimeStamp);
		} catch (DataAccessException ex) {
			throw new SyncDataServiceException(MasterDataErrorCode.APP_ROLE_PRIORITY_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.APP_ROLE_PRIORITY_FETCH_EXCEPTION.getErrorMessage());
		}
		if (appRolePriorities != null && !appRolePriorities.isEmpty()) {
			appRolePriorityDtos = MapperUtils.mapAll(appRolePriorities, AppRolePriorityDto.class);
		}
		return CompletableFuture.completedFuture(appRolePriorityDtos);
	}

	@Async
	public CompletableFuture<List<ScreenAuthorizationDto>> getScreenAuthorizationDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ScreenAuthorization> screenAuthorizationList = null;
		List<ScreenAuthorizationDto> screenAuthorizationDtos = null;
		try {
			if(!isChangesFound("ScreenAuthorization", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(screenAuthorizationDtos);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			screenAuthorizationList = screenAuthorizationRepository
					.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime, currentTimeStamp);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCREEN_AUTHORIZATION_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.SCREEN_AUTHORIZATION_FETCH_EXCEPTION.getErrorMessage());
		}
		if (screenAuthorizationList != null && !screenAuthorizationList.isEmpty()) {
			screenAuthorizationDtos = MapperUtils.mapAll(screenAuthorizationList, ScreenAuthorizationDto.class);
		}
		return CompletableFuture.completedFuture(screenAuthorizationDtos);
	}

	@Async
	public CompletableFuture<List<ProcessListDto>> getProcessList(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ProcessList> processList = null;
		List<ProcessListDto> processListDtos = null;
		try {
			if(!isChangesFound("ProcessList", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(processListDtos);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			processList = processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime,
					currentTimeStamp);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorMessage());
		}
		if (processList != null && !processList.isEmpty()) {
			processListDtos = MapperUtils.mapAll(processList, ProcessListDto.class);
		}
		return CompletableFuture.completedFuture(processListDtos);
	}

	@Async
	public CompletableFuture<List<SyncJobDefDto>> getSyncJobDefDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<SyncJobDefDto> syncJobDefDtos = null;
		List<SyncJobDef> syncJobDefs = null;
		try {
			if(!isChangesFound("SyncJobDef", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(syncJobDefDtos);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			syncJobDefs = syncJobDefRepository.findLatestByLastUpdatedTimeAndCurrentTimeStamp(lastUpdatedTime,
					currentTimeStamp);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new AdminServiceException(AdminServiceErrorCode.SYNC_JOB_DEF_FETCH_EXCEPTION.getErrorCode(),
					AdminServiceErrorCode.SYNC_JOB_DEF_FETCH_EXCEPTION.getErrorMessage()+e);
		}
		if (syncJobDefs != null && !syncJobDefs.isEmpty()) {
			syncJobDefDtos = MapperUtils.mapAll(syncJobDefs, SyncJobDefDto.class);
		}
		return CompletableFuture.completedFuture(syncJobDefDtos);
	}

	@Async
	public CompletableFuture<List<ScreenDetailDto>> getScreenDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ScreenDetail> screenDetails = null;
		List<ScreenDetailDto> screenDetailDtos = null;
		try {
			if(!isChangesFound("ScreenDetail", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(screenDetailDtos);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			screenDetails = screenDetailRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdatedTime,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.SCREEN_DETAIL_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.SCREEN_DETAIL_FETCH_EXCEPTION.getErrorMessage());
		}
		if (screenDetails != null && !screenDetails.isEmpty()) {
			screenDetailDtos = MapperUtils.mapAll(screenDetails, ScreenDetailDto.class);
		}
		return CompletableFuture.completedFuture(screenDetailDtos);
	}


	@Async
	public CompletableFuture<List<DynamicFieldDto>> getAllDynamicFields(LocalDateTime lastUpdated) {
		List<DynamicFieldDto> result = new ArrayList<>();
		try {
			PageDto<DynamicFieldDto> pageDto = null;
			int pageNo = 0;
			do {
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(dynamicfieldUrl);
				if(lastUpdated != null) {	builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated)); }
				builder.queryParam("pageNumber", pageNo++);
				//its with default sort on crd_dtimes
				ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

				objectMapper.registerModule(new JavaTimeModule());
				ResponseWrapper<PageDto<DynamicFieldDto>> resp = objectMapper.readValue(responseEntity.getBody(),
						new TypeReference<ResponseWrapper<PageDto<DynamicFieldDto>>>() {});

				if(resp.getErrors() != null && !resp.getErrors().isEmpty())
					throw new SyncInvalidArgumentException(resp.getErrors());

				pageDto = resp.getResponse();
				result.addAll(pageDto.getData());
			} while(pageNo < pageDto.getTotalPages());

			return CompletableFuture.completedFuture(result);

		} catch (Exception e) {
			logger.error("Failed to fetch dynamic fields", e);
			throw new SyncDataServiceException(MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorCode(),
					MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorMessage() + " : " +
							ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}

	@Async
	public CompletableFuture<List<DynamicFieldDto>> getAllDynamicFields(LocalDateTime lastUpdated, RestTemplate restClient) {
		List<DynamicFieldDto> result = new ArrayList<>();
		try {
			PageDto<DynamicFieldDto> pageDto = null;
			int pageNo = 0;
			do {
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(dynamicfieldUrl);
				if(lastUpdated != null) {	builder.queryParam("lastUpdated", DateUtils.formatToISOString(lastUpdated)); }
				builder.queryParam("pageNumber", pageNo++);
				//its with default sort on crd_dtimes
				ResponseEntity<String> responseEntity = restClient.getForEntity(builder.build().toUri(), String.class);

				objectMapper.registerModule(new JavaTimeModule());
				ResponseWrapper<PageDto<DynamicFieldDto>> resp = objectMapper.readValue(responseEntity.getBody(),
						new TypeReference<ResponseWrapper<PageDto<DynamicFieldDto>>>() {});

				if(resp.getErrors() != null && !resp.getErrors().isEmpty())
					throw new SyncInvalidArgumentException(resp.getErrors());

				pageDto = resp.getResponse();
				result.addAll(pageDto.getData());
			} while(pageNo < pageDto.getTotalPages());

			return CompletableFuture.completedFuture(result);

		} catch (Exception e) {
			logger.error("Failed to fetch dynamic fields", e);
			throw new SyncDataServiceException(MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorCode(),
					MasterDataErrorCode.DYNAMIC_FIELD_FETCH_FAILED.getErrorMessage() + " : " +
							ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}

	@Async
	public CompletableFuture<List<PermittedConfigDto>> getPermittedConfig(LocalDateTime lastUpdated,
															 LocalDateTime currentTimeStamp) {
		List<PermittedConfigDto> dtoList = null;
		List<PermittedLocalConfig> list = null;
		try {
			if(!isChangesFound("PermittedLocalConfig", lastUpdated)) {
				return CompletableFuture.completedFuture(dtoList);
			}
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			list = permittedLocalConfigRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.PERMITTED_CONFIG_FETCH_FAILED.getErrorCode(),
					e.getMessage(), e);
		}
		if (list != null && !list.isEmpty()) {
			dtoList = MapperUtils.mapAll(list, PermittedConfigDto.class);
		}
		return CompletableFuture.completedFuture(dtoList);
	}

	public void getSyncDataBaseDto(Class entityClass, String entityType, List entities, String publicKey, List result) {
		getSyncDataBaseDto(entityClass.getSimpleName(), entityType, entities, publicKey, result);
	}

	@SuppressWarnings("unchecked")
	public void getSyncDataBaseDto(String entityName, String entityType, List entities, String publicKey, List result) {
		if (null != entities) {
			List<String> list = Collections.synchronizedList(new ArrayList<String>());
			entities.parallelStream().filter(Objects::nonNull).forEach(obj -> {
				try {
					String json = mapper.getObjectAsJsonString(obj);
					if (json != null) {
						list.add(json);
					}
				} catch (Exception e) {
					logger.error("Failed to map " + entityName + " data to json", e);
				}
			});

			try {
				if (list.size() > 0) {
					TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
					tpmCryptoRequestDto
							.setValue(CryptoUtil.encodeBase64(mapper.getObjectAsJsonString(list).getBytes()));
					tpmCryptoRequestDto.setPublicKey(publicKey);
					TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService
							.csEncrypt(tpmCryptoRequestDto);
					result.add(new SyncDataBaseDto(entityName, entityType, tpmCryptoResponseDto.getValue()));
				}
			} catch (Exception e) {
				logger.error("Failed to encrypt " + entityName + " data to json", e);
			}
		}
	}

	public void getSyncDataBaseDtoV2(String entityName, String entityType, List entities, String publicKey, List result) {
		if (null != entities) {
			try {
				entities = (List) entities.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
				if (entities.size() > 0) {
					TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
					tpmCryptoRequestDto
							.setValue(CryptoUtil.encodeBase64(mapper.getObjectAsJsonString(entities).getBytes()));
					tpmCryptoRequestDto.setPublicKey(publicKey);
					TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService
							.csEncrypt(tpmCryptoRequestDto);
					result.add(new SyncDataBaseDto(entityName, entityType, tpmCryptoResponseDto.getValue()));
				}
			} catch (Exception e) {
				logger.error("Failed to encrypt " + entityName + " data to json", e);
			}
		}
	}

	/**
	 * This method queries registrationCenterMachineRepository to fetch active
	 * registrationCenterMachine with input keyIndex.
	 *
	 * KeyIndex is mandatory param registrationCenterId is optional, if provided
	 * validates, if this matches the mapped registration center
	 *
	 * @param registrationCenterId
	 * @param keyIndex
	 * @return RegistrationCenterMachineDto(machineId , registrationCenterId)
	 * @throws SyncDataServiceException
	 */
	public RegistrationCenterMachineDto getRegistrationCenterMachine(String registrationCenterId, String keyIndex)
			throws SyncDataServiceException {
		try {

			List<Object[]> regCenterMachines = machineRepository
					.getRegistrationCenterMachineWithKeyIndexWithoutStatusCheck(keyIndex);

			if (regCenterMachines.isEmpty()) {
				throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
						MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());
			}

			String mappedRegCenterId = (String) ((Object[]) regCenterMachines.get(0))[0];

			if (mappedRegCenterId == null)
				throw new RequestException(MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
						MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());

			if (registrationCenterId != null && !mappedRegCenterId.equals(registrationCenterId))
				throw new RequestException(MasterDataErrorCode.REG_CENTER_UPDATED.getErrorCode(),
						MasterDataErrorCode.REG_CENTER_UPDATED.getErrorMessage());

			return new RegistrationCenterMachineDto(mappedRegCenterId,
					(String) ((Object[]) regCenterMachines.get(0))[1],
					(String) ((Object[]) regCenterMachines.get(0))[2]);

		} catch (DataAccessException | DataAccessLayerException e) {
			logger.error("Failed to fetch registrationCenterMachine : ", e);
		}

		throw new SyncDataServiceException(MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
				MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorMessage());
	}

	private boolean isChangesFound(String entityName, LocalDateTime lastUpdated) {
		if(lastUpdated == null) //if it's null, then the request is for full sync
			return true;

		List<Object[]> result = Collections.EMPTY_LIST;
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
				result = blacklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime();
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
		}
		if(result.isEmpty()) {
			logger.info("** No data found in the table : {}", entityName);
			return false;
		}
		//check if updatedDateTime is null, then always take createdDateTime
		Object changedDate = (result.get(0).length == 2 && result.get(0)[1] == null) ? result.get(0)[0] : result.get(0)[1];
		return changedDate == null ? false : lastUpdated.isBefore((LocalDateTime)changedDate);
	}
}
