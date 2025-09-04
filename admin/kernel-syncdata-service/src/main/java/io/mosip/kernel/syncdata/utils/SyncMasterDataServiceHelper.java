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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Sync handler masterData service helper
 * 
 * @author Abhishek Kumar
 * @author Srinivasan
 * @since 1.0.0
 */
@Component
public class SyncMasterDataServiceHelper {

	private final static Logger logger = LoggerFactory.getLogger(SyncMasterDataServiceHelper.class);

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
	private BlocklistedWordsRepository blocklistedWordsRepository;
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
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${mosip.kernel.masterdata.locationhierarchylevels.uri}")
	private String locationHirerarchyUrl;

	@Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
	private String dynamicfieldUrl;

	private static final String ANDROID_MACHINE_TYPE_CODE = "ANDROID";



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
				responseDto.setRegCenterId(machine.getRegCenterId());
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
	 * @param centerId        Center Id
	 * @param lastUpdated      lastUpdated timestamp
	 * @param currentTimeStamp the current time stamp
	 * @return list of {@link RegistrationCenterDto}
	 */
	@Async
	public CompletableFuture<List<RegistrationCenterDto>> getRegistrationCenter(String centerId,
			LocalDateTime lastUpdated, LocalDateTime currentTimeStamp) {
		List<RegistrationCenter> list = null;
		try {
			if(!isChangesFound("RegistrationCenter", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			list = registrationCenterRepository.findRegistrationCentersById(centerId, lastUpdated,
					currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.APPLICATION_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		return CompletableFuture.completedFuture(convertRegistrationCenterToDto(list));
	}

	private List<RegistrationCenterDto> convertRegistrationCenterToDto(List<RegistrationCenter> list) {
		   if(list != null && !list.isEmpty()) {
		      List<RegistrationCenterDto> registrationCenterDtos  = new ArrayList<>();
		      list.stream().forEach( entity -> {
		    	  RegistrationCenterDto regiDto = copyRegistrationCenterProperties(entity);		    	  
		    	  registrationCenterDtos.add(regiDto);
		      });
		      return registrationCenterDtos;
		   }
		   return null;
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
		List<Template> templateList = null;
		try {
			if(!isChangesFound("Template", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertTemplateEntityToDto(templateList));
	}	
	/**
	 * this method copy the properties from TemplateEntity to TemplateDTO
	 * @param templateList
	 * @return
	 */
	private List<TemplateDto> convertTemplateEntityToDto(List<Template> templateList) {
		if (templateList != null && !templateList.isEmpty()) {
			List<TemplateDto> templateDtos = new ArrayList<>();
			templateList.stream().forEach(entity -> {
			TemplateDto	entityDTO = new TemplateDto(entity.getId(), entity.getName(), entity.getDescription(),
						entity.getFileFormatCode(), entity.getModel(), entity.getFileText(), entity.getModuleId(),
						entity.getModuleName(), entity.getTemplateTypeCode());			
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			templateDtos.add(entityDTO);
			});
			return templateDtos;
		}
		return null;
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
		List<TemplateFileFormat> templateTypes = null;
		try {
			if(!isChangesFound("TemplateFileFormat", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertTemplateFileFormatEntityToDto(templateTypes));
	}
	
	
	/**
	 * this method copy the properties from TemplateFileFormatEntity to TemplateFileFormatDto
	 * 
	 * @param templateTypesList
	 * @return
	 */
	private List<TemplateFileFormatDto> convertTemplateFileFormatEntityToDto(List<TemplateFileFormat> templateTypesList) {
		if (templateTypesList != null && !templateTypesList.isEmpty()) {
			List<TemplateFileFormatDto> templateFileFormatDtos = new ArrayList<>();
			templateTypesList.stream().forEach(entity -> {
				TemplateFileFormatDto entityDTO = 	new TemplateFileFormatDto(entity.getCode(), entity.getDescription());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				entityDTO.setLangCode(entity.getLangCode());	
			templateFileFormatDtos.add(entityDTO);	
			});
			return templateFileFormatDtos;
		}
		return null;
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
		List<ReasonCategory> reasons = null;
		try {
			if(!isChangesFound("ReasonCategory", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertPostReasonCategoryEntityToDto(reasons));
	}

	/**
	 * this method copy the properties from ReasonCategoryEntity to ReasonCategoryDTO
	 * @param reasons
	 * @return
	 */
	private List<PostReasonCategoryDto> convertPostReasonCategoryEntityToDto(List<ReasonCategory> reasons) {
		   if(reasons != null && !reasons.isEmpty()) {
		      List<PostReasonCategoryDto> postReasonCategoryDtos  = new ArrayList<>();
		      reasons.stream().forEach( entity -> {
		    	  PostReasonCategoryDto entityDTO = new PostReasonCategoryDto(entity.getCode(), entity.getDescription(), entity.getName());
		      entityDTO.setIsDeleted(entity.getIsDeleted());
		      entityDTO.setIsActive(entity.getIsActive());
		      entityDTO.setLangCode(entity.getLangCode());  
		      postReasonCategoryDtos.add(entityDTO);
		      });
		      return postReasonCategoryDtos;
		   }
		   return null;
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
		List<ReasonList> reasons = null;
		try {
			if(!isChangesFound("ReasonList", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertReasonListEntityToDto(reasons));
	}

	/**
	 * this method is copying the properties from ReasonListEntity to ReasonDTO
	 * @param reasons
	 * @return
	 */
	private List<ReasonListDto> convertReasonListEntityToDto(List<ReasonList> reasons) {
		if (reasons != null && !reasons.isEmpty()) {
			List<ReasonListDto> reasonListDtos = new ArrayList<>();
			reasons.stream().forEach(entity -> {
			ReasonListDto entityDTO = new ReasonListDto(entity.getCode(), entity.getName(), entity.getDescription(),
						entity.getRsnCatCode());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());			
			reasonListDtos.add(entityDTO);
			});
			return reasonListDtos;
		}
		return null;
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
		List<Holiday> holidays = null;
		try {
			if(!isChangesFound("Holiday", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertEntityToHoliday(holidays));
	}

	private List<HolidayDto> convertEntityToHoliday(List<Holiday> holidays) {
		if(holidays == null || holidays.isEmpty())
			return null;

		List<HolidayDto> holidayDtos = new ArrayList<>();
		holidays.forEach(holiday -> {
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
			holidayDtos.add(dto);
		});
		return holidayDtos;
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
		List<BlocklistedWords> words = null;

		try {
			if(!isChangesFound("BlacklistedWords", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
			}

			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			words = blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimeStamp);

		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.BLACKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					e.getMessage(), e);
		}
		return CompletableFuture.completedFuture(convertBlocklistedWordsEntityToDto(words));
	}

	/**
	 * this method copy the properties from BlocklistedWordsEnity to BlocklistedWordsDTO
	 * @param words
	 * @return
	 */
	private List<BlacklistedWordsDto> convertBlocklistedWordsEntityToDto(List<BlocklistedWords> words) {
		if (words != null && !words.isEmpty()) {
			List<BlacklistedWordsDto> blocklistedWordsDtos = new ArrayList<>();
			words.stream().forEach(entity -> {
				BlacklistedWordsDto	entityDTO = new BlacklistedWordsDto(entity.getWord(), entity.getDescription());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				entityDTO.setLangCode(entity.getLangCode());
			blocklistedWordsDtos.add(entityDTO);
			});
			return blocklistedWordsDtos;
		}
		return null;
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
		List<DocumentType> documentTypes = null;
		try {
			if(!isChangesFound("DocumentType", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertDocumentTypeEntityToDto(documentTypes));
	}

	/**
	 * copy the documentTypeEntity properties to documentTypeDTO 
	 * @param documentTypes
	 * @return
	 */
	private List<DocumentTypeDto> convertDocumentTypeEntityToDto(List<DocumentType> documentTypes) {
		if (documentTypes != null && !documentTypes.isEmpty()) {
			List<DocumentTypeDto> documentTypeDtos = new ArrayList<>();
			documentTypes.stream().forEach(entity -> {
				DocumentTypeDto entityDTO = new DocumentTypeDto(entity.getCode(), entity.getName(), entity.getDescription());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				entityDTO.setLangCode(entity.getLangCode());
				documentTypeDtos.add(entityDTO);
			});
			return documentTypeDtos;
		}
		return null;
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
		List<Location> locations = null;
		try {
			if(!isChangesFound("Location", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertLocationsEntityToDto(locations));
	}

	/**
	 * copy the properties of LocatinEntity to LocationDTO
	 * 
	 * @param locations
	 * @return
	 */
	private List<LocationDto> convertLocationsEntityToDto(List<Location> locations) {
		if (locations != null && !locations.isEmpty()) {
			List<LocationDto> locationDtos = new ArrayList<>();
			locations.stream().forEach(entity -> {
				LocationDto entityDTO = new LocationDto(entity.getCode(), entity.getName(), entity.getHierarchyLevel(),
						entity.getHierarchyName(), entity.getParentLocCode());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				entityDTO.setLangCode(entity.getLangCode());
				locationDtos.add(entityDTO);
			});
			return locationDtos;
		}
		return null;
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
		List<TemplateType> templateTypes = null;
		try {
			if(!isChangesFound("TemplateType", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertTemplateTypeEntityToDto(templateTypes));
	}

	/**
	 * copy the properties from TemplateTypeEntity to TemplateTypeDTO
	 * 
	 * @param templateTypesList
	 * @return
	 */
	private List<TemplateTypeDto> convertTemplateTypeEntityToDto(List<TemplateType> templateTypesList) {
		if (templateTypesList != null && !templateTypesList.isEmpty()) {
			List<TemplateTypeDto> templateTypeDtos = new ArrayList<>();
			templateTypesList.stream().forEach(entity -> {
			TemplateTypeDto	entityDTO = new TemplateTypeDto(entity.getCode(), entity.getDescription());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			templateTypeDtos.add(entityDTO);
			});
			return templateTypeDtos;
		}
		return null;
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
		List<ValidDocument> validDocuments = null;
		try {
			if(!isChangesFound("ValidDocument", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertValidDocumentEntityToDto(validDocuments));
	}

	/**
	 * this method copy the properties from ValidDocumentEntity to ValidDocumentDTO
	 * 
	 * @param validDocuments
	 * @return
	 */
	private List<ValidDocumentDto> convertValidDocumentEntityToDto(List<ValidDocument> validDocuments) {
		if (validDocuments != null && !validDocuments.isEmpty()) {
			List<ValidDocumentDto> validDocumentsDtos = new ArrayList<>();
			validDocuments.stream().forEach(entity -> {
			ValidDocumentDto entityDTO = new ValidDocumentDto(entity.getDocTypeCode(), entity.getDocCategoryCode());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			validDocumentsDtos.add(entityDTO);
			});
			return validDocumentsDtos;
		}
		return null;
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

		List<Machine> machines = null;
		try {
			if(!isChangesFound("Machine", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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

		List<RegistrationCenterMachineDto> registrationCenterMachineDtos = new ArrayList<>();
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
		List<ApplicantValidDocument> applicantValidDocuments = null;
		try {
			if(!isChangesFound("ApplicantValidDocument", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertApplicantValidDocumentEntityToDto(applicantValidDocuments));
	}

	/**
	 * copy the documentTypeEntity properties to documentTypeDTO 
	 * @param applicantValidDocuments
	 * @return
	 */
	private List<ApplicantValidDocumentDto> convertApplicantValidDocumentEntityToDto(List<ApplicantValidDocument> applicantValidDocuments) {
		if (applicantValidDocuments != null && !applicantValidDocuments.isEmpty()) {
			List<ApplicantValidDocumentDto> applicantValidDocumentseDtos = new ArrayList<>();
			applicantValidDocuments.stream().forEach(entity -> {
				ApplicantValidDocumentDto entityDTO = new ApplicantValidDocumentDto(entity.getApplicantValidDocumentId().getAppTypeCode(),
						entity.getApplicantValidDocumentId().getDocTypeCode(), entity.getApplicantValidDocumentId().getDocCatCode(),
						entity.getLangCode());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				applicantValidDocumentseDtos.add(entityDTO);
			});
			return applicantValidDocumentseDtos;
		}
		return null;
	}
	
	@Async
	public CompletableFuture<List<AppAuthenticationMethodDto>> getAppAuthenticationMethodDetails(
			LocalDateTime lastUpdatedTime, LocalDateTime currentTimeStamp) {
		List<AppAuthenticationMethod> appAuthenticationMethods = null;
		try {
			if(!isChangesFound("AppAuthenticationMethod", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertAppAuthMethodEntityToDto(appAuthenticationMethods));

	}

	/**
	 * this method copy the properties from AppAuthenticationMethodEntity to AppAuthenticationMethodDTO
	 * @param appAuthenticationMethods
	 * @return
	 */
	private List<AppAuthenticationMethodDto> convertAppAuthMethodEntityToDto(List<AppAuthenticationMethod> appAuthenticationMethods) {
		if (appAuthenticationMethods != null && !appAuthenticationMethods.isEmpty()) {
			List<AppAuthenticationMethodDto> appAuthenticationMethodDtos = new ArrayList<>();
			appAuthenticationMethods.stream().forEach(entity -> {
				AppAuthenticationMethodDto entityDTO = new AppAuthenticationMethodDto(entity.getAppId(),entity.getProcessId(), entity.getRoleCode(),
						entity.getAuthMethodCode(),entity.getMethodSequence(), entity.getLangCode());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				entityDTO.setLangCode(entity.getLangCode());
				appAuthenticationMethodDtos.add(entityDTO);
			});
			return appAuthenticationMethodDtos;
		}
		return null;
	}

	@Async
	public CompletableFuture<List<AppRolePriorityDto>> getAppRolePriorityDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<AppRolePriority> appRolePriorities = null;
		try {

			if(!isChangesFound("AppRolePriority", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertAppRolePrioritiesToDto(appRolePriorities));
	}

	private List<AppRolePriorityDto> convertAppRolePrioritiesToDto(List<AppRolePriority> appRolePriorities) {
		   if(appRolePriorities != null && !appRolePriorities.isEmpty()) {
		      List<AppRolePriorityDto> appRolePriorityDtos  = new ArrayList<>();
		      appRolePriorities.stream().forEach( entity -> {
		      AppRolePriorityDto entityDTO= new AppRolePriorityDto(entity.getAppId(), 
		               entity.getProcessId(), entity.getRoleCode(), entity.getPriority(), entity.getLangCode());		      
		      entityDTO.setIsDeleted(entity.getIsDeleted());
		      entityDTO.setIsActive(entity.getIsActive());
		      entityDTO.setLangCode(entity.getLangCode());
		      appRolePriorityDtos.add(entityDTO);
		      });
		      return appRolePriorityDtos;
		   }
		   return null;
	}


	@Async
	public CompletableFuture<List<ScreenAuthorizationDto>> getScreenAuthorizationDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ScreenAuthorization> screenAuthorizationList = null;
		try {
			if(!isChangesFound("ScreenAuthorization", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertScreenAuthorizationToDto(screenAuthorizationList));
	}

	/**
	 * This method is copying the properties from ScreenAuthorizationEntity to ScreenAuthorizationDTO
	 * 
	 * @param screenAuthorizationList
	 * @return
	 */
	private List<ScreenAuthorizationDto> convertScreenAuthorizationToDto(
			List<ScreenAuthorization> screenAuthorizationList) {
		if (screenAuthorizationList != null && !screenAuthorizationList.isEmpty()) {
			List<ScreenAuthorizationDto> screenAuthorizationDtos = new ArrayList<>();
			screenAuthorizationList.stream().forEach(entity -> {
			ScreenAuthorizationDto entityDTO=new ScreenAuthorizationDto(entity.getScreenId(), entity.getRoleCode(),
						entity.getIsPermitted());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			screenAuthorizationDtos.add(entityDTO);	
			});
			return screenAuthorizationDtos;
		}
		return null;
	}
	
	
	@Async
	public CompletableFuture<List<ProcessListDto>> getProcessList(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ProcessList> processList = null;
		try {
			if(!isChangesFound("ProcessList", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertprocessListEntityToDto(processList));
	}

	/**
	 *  copy the properties from ProcessListEntity to ProcessListDto
	 * @param processList
	 * @return
	 */
	private List<ProcessListDto> convertprocessListEntityToDto(List<ProcessList> processList) {
		if (processList != null && !processList.isEmpty()) {
			List<ProcessListDto> processListDtos = new ArrayList<>();
			processList.stream().forEach(entity -> {
			ProcessListDto entityDTO = new ProcessListDto(entity.getId(), entity.getName(), entity.getDescr());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			processListDtos.add(entityDTO);
			});
			return processListDtos;
		}
		return null;
	}
	
	@Async
	public CompletableFuture<List<SyncJobDefDto>> getSyncJobDefDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<SyncJobDef> syncJobDefs = null;
		try {
			if(!isChangesFound("SyncJobDef", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertSyncJobDefEntityToDto(syncJobDefs));
	}

	/**
	 * copy the properties from SyncJobDefEntity to SyncJobDefDTO
	 * 
	 * @param syncJobDefs
	 * @return
	 */
	private List<SyncJobDefDto> convertSyncJobDefEntityToDto(List<SyncJobDef> syncJobDefs) {
		if (syncJobDefs != null && !syncJobDefs.isEmpty()) {
			List<SyncJobDefDto> syncJobDefDtos = new ArrayList<>();
			syncJobDefs.stream().forEach(entity -> {
			 SyncJobDefDto entityDTO = new SyncJobDefDto(entity.getId(), entity.getName(), entity.getApiName(),
						entity.getParentSyncJobId(), entity.getSyncFreq(), entity.getLockDuration(),
						entity.getIsActive(), entity.getIsDeleted(), entity.getLangCode());
				syncJobDefDtos.add(entityDTO);
			});
			return syncJobDefDtos;
		}
		return null;
	}
	
	@Async
	public CompletableFuture<List<ScreenDetailDto>> getScreenDetails(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp) {
		List<ScreenDetail> screenDetails = null;
		try {
			if(!isChangesFound("ScreenDetail", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertScreenDetailToDto(screenDetails));
	}

	/**
	 * copy the properties from screenDetailEntity to convertScreenDetailToDto
	 * 
	 * @param screenDetails
	 * @return
	 */
	private List<ScreenDetailDto> convertScreenDetailToDto(List<ScreenDetail> screenDetails) {
		if (screenDetails != null && !screenDetails.isEmpty()) {
			List<ScreenDetailDto> screenDetailDtos = new ArrayList<>();
			screenDetails.stream().forEach(entity -> {
			ScreenDetailDto	entityDTO =	new ScreenDetailDto(entity.getId(), entity.getAppId(), entity.getName(), entity.getDescr());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			entityDTO.setLangCode(entity.getLangCode());
			screenDetailDtos.add(entityDTO);		
			});
			return screenDetailDtos;
		}
		return null;
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
		List<PermittedLocalConfig> list = null;
		try {
			if(!isChangesFound("PermittedLocalConfig", lastUpdated)) {
				return CompletableFuture.completedFuture(null);
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
		return CompletableFuture.completedFuture(convertPermittedConfigEntityToDto(list));
	}

	/**
	 * this method is copy the properties from PermittedLocalConfigEntity to PermittedLocalConfigDTO
	 * 
	 * @param PermittedLocalConfigList
	 * @return
	 */
	private List<PermittedConfigDto> convertPermittedConfigEntityToDto(List<PermittedLocalConfig> PermittedLocalConfigList) {
		if (PermittedLocalConfigList != null && !PermittedLocalConfigList.isEmpty()) {
			List<PermittedConfigDto> permittedConfigDtos = new ArrayList<>();
			PermittedLocalConfigList.stream().forEach(entity -> {
			PermittedConfigDto entityDTO = new PermittedConfigDto(entity.getCode(), entity.getName(), entity.getType());
			entityDTO.setIsDeleted(entity.getIsDeleted());
			entityDTO.setIsActive(entity.getIsActive());
			permittedConfigDtos.add(entityDTO);	
			});
			return permittedConfigDtos;
		}
		return null;
	}

	@Async
	public CompletableFuture<List<LanguageDto>> getLanguageList(LocalDateTime lastUpdatedTime,
																LocalDateTime currentTimeStamp) {
		List<Language> list = null;
		try {
			if(!isChangesFound("Language", lastUpdatedTime)) {
				return CompletableFuture.completedFuture(null);
			}
			if (lastUpdatedTime == null) {
				lastUpdatedTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}
			list = languageRepository.findAllLatestCreatedUpdateDeleted(lastUpdatedTime, currentTimeStamp);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new SyncDataServiceException(MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.PROCESS_LIST_FETCH_EXCEPTION.getErrorMessage());
		}
		return CompletableFuture.completedFuture(convertLanguageEntityToDto(list));
	}

	private List<LanguageDto> convertLanguageEntityToDto(List<Language> languageList) {
		if (languageList != null && !languageList.isEmpty()) {
			List<LanguageDto> dtoList = new ArrayList<>();
			languageList.stream().forEach(entity -> {
				LanguageDto entityDTO = new LanguageDto(entity.getCode(), entity.getName(), entity.getFamily(), entity.getNativeName());
				entityDTO.setIsDeleted(entity.getIsDeleted());
				entityDTO.setIsActive(entity.getIsActive());
				dtoList.add(entityDTO);
			});
			return dtoList;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void getSyncDataBaseDto(String entityName, String entityType, List entities, RegistrationCenterMachineDto
			registrationCenterMachineDto, List result) {
		if (null != entities) {
			List<String> list = Collections.synchronizedList(new ArrayList<String>());
			entities.parallelStream().filter(Objects::nonNull).forEach(obj -> {
				try {
					String json = mapper.getObjectAsJsonString(obj);
					if (json != null) {
						list.add(json);
					}
				} catch (Exception e) {
					logger.error("Failed to map {} data to json {}", entityName, e);
				}
			});

			try {
				if (list.size() > 0) {
					TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
					tpmCryptoRequestDto
							.setValue(CryptoUtil.encodeToURLSafeBase64(mapper.getObjectAsJsonString(list).getBytes()));
					tpmCryptoRequestDto.setPublicKey(registrationCenterMachineDto.getPublicKey());
					tpmCryptoRequestDto.setClientType(registrationCenterMachineDto.getClientType());
					TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService
							.csEncrypt(tpmCryptoRequestDto);

					//backward compatibility
					result.add(new SyncDataBaseDto(entityName.equalsIgnoreCase(BlocklistedWords.class.getSimpleName()) ?
							"BlacklistedWords" : entityName, entityType, tpmCryptoResponseDto.getValue()));
				}
			} catch (Exception e) {
				logger.error("Failed to encrypt {} data to json", entityName, e);
			}
		}
	}

	public void getSyncDataBaseDtoV2(String entityName, String entityType, List entities, RegistrationCenterMachineDto
			registrationCenterMachineDto, List result) {
		if (null != entities) {
			try {
				entities = (List) entities.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
				if (entities.size() > 0) {
					TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
					tpmCryptoRequestDto
							.setValue(CryptoUtil.encodeToURLSafeBase64(mapper.getObjectAsJsonString(entities).getBytes()));
					tpmCryptoRequestDto.setPublicKey(registrationCenterMachineDto.getPublicKey());
					tpmCryptoRequestDto.setClientType(registrationCenterMachineDto.getClientType());
					TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
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
		Machine machine;
		try {
			// Fetch machine entry (ignore case)
			machine = machineRepository.findOneByKeyIndexIgnoreCase(keyIndex);
		} catch (DataAccessException | DataAccessLayerException e) {
			logger.error("Failed to fetch registrationCenterMachine for keyIndex: {}", keyIndex, e);
			throw new SyncDataServiceException(
					MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
					MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorMessage());
		}

		// Validate machine existence
		if (machine == null) {
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());
		}

		String mappedRegCenterId = machine.getRegCenterId();

		// Validate registration center mapping
		if (mappedRegCenterId == null) {
			throw new RequestException(MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
		}

		// Optional: Only check if caller provided registrationCenterId
		if (registrationCenterId != null && !mappedRegCenterId.equals(registrationCenterId)) {
			throw new RequestException(MasterDataErrorCode.REG_CENTER_UPDATED.getErrorCode(),
					MasterDataErrorCode.REG_CENTER_UPDATED.getErrorMessage());
		}

		return new RegistrationCenterMachineDto(
				mappedRegCenterId,
				machine.getId(),
				machine.getPublicKey(),
				machine.getMachineSpecId(),
				machine.getMachineSpecification() != null
						? machine.getMachineSpecification().getMachineTypeCode()
						: null,
				getClientType(machine)
		);
	}


	public static ClientType getClientType(Machine machine) {
		if(machine.getMachineSpecification() == null)
			return null;

		if(ANDROID_MACHINE_TYPE_CODE.equalsIgnoreCase(machine.getMachineSpecification().getMachineTypeCode()))
			return ClientType.ANDROID;

		return null;
	}

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

	/**
	 * copy the properties from RegistrationCenterEntity to RegistrationCenterDTO
	 * 
	 * @param regEntity
	 * @return
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
}
