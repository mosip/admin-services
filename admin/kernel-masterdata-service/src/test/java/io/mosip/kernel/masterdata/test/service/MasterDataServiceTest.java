package io.mosip.kernel.masterdata.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.masterdata.dto.request.WorkingDaysPutRequestDto;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.entity.*;
import io.mosip.kernel.masterdata.repository.*;
import io.mosip.kernel.masterdata.utils.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.orm.hibernate5.HibernateObjectRetrievalFailureException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.ApplicationDto;
import io.mosip.kernel.masterdata.dto.BiometricAttributeDto;
import io.mosip.kernel.masterdata.dto.BiometricTypeDto;
import io.mosip.kernel.masterdata.dto.DayNameAndSeqListDto;
import io.mosip.kernel.masterdata.dto.DeviceSpecificationDto;
import io.mosip.kernel.masterdata.dto.DocumentCategoryDto;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.DocumentTypePutReqDto;
import io.mosip.kernel.masterdata.dto.FilterData;
import io.mosip.kernel.masterdata.dto.LanguageDto;
import io.mosip.kernel.masterdata.dto.LocationDto;
import io.mosip.kernel.masterdata.dto.LocationHierarchyLevelResponseDto;
import io.mosip.kernel.masterdata.dto.LocationPutDto;
import io.mosip.kernel.masterdata.dto.RegCenterPostReqDto;
import io.mosip.kernel.masterdata.dto.RegCenterPutReqDto;
import io.mosip.kernel.masterdata.dto.RegistrationCenterMachineDeviceHistoryDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatPutDto;
import io.mosip.kernel.masterdata.dto.WorkingDaysResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ApplicationResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.BiometricTypeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.BlocklistedWordsResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DocumentCategoryResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LanguageResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationCodeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ResgistrationCenterStatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.TemplateResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.WeekDaysDto;
import io.mosip.kernel.masterdata.dto.getresponse.WorkingDaysDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.RegCenterMachineDeviceHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.response.ColumnCodeValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.service.ApplicationService;
import io.mosip.kernel.masterdata.service.BiometricAttributeService;
import io.mosip.kernel.masterdata.service.BiometricTypeService;
import io.mosip.kernel.masterdata.service.BlocklistedWordsService;
import io.mosip.kernel.masterdata.service.DeviceHistoryService;
import io.mosip.kernel.masterdata.service.DeviceService;
import io.mosip.kernel.masterdata.service.DeviceSpecificationService;
import io.mosip.kernel.masterdata.service.DeviceTypeService;
import io.mosip.kernel.masterdata.service.DocumentCategoryService;
import io.mosip.kernel.masterdata.service.DocumentTypeService;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.service.ExceptionalHolidayService;
import io.mosip.kernel.masterdata.service.LanguageService;
import io.mosip.kernel.masterdata.service.LocationHierarchyService;
import io.mosip.kernel.masterdata.service.LocationService;
import io.mosip.kernel.masterdata.service.MachineHistoryService;
import io.mosip.kernel.masterdata.service.RegWorkingNonWorkingService;
import io.mosip.kernel.masterdata.service.RegistrationCenterDeviceHistoryService;
import io.mosip.kernel.masterdata.service.RegistrationCenterService;
import io.mosip.kernel.masterdata.service.RegistrationCenterTypeService;
import io.mosip.kernel.masterdata.service.TemplateFileFormatService;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;

/**
 * @author Bal Vikash Sharma
 * @author Neha Sinha
 * @author tapaswini
 * @author srinivasan
 * @since 1.0.0
 *
 * 
 * @since 1.0.0
 *
 */

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MasterDataServiceTest {

	@MockBean
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationService applicationService;

	private Application application1;
	private Application application2;

	private List<Application> applicationList;
	private ApplicationDto applicationDto;

	private RequestWrapper<ApplicationDto> applicationRequestWrapper;
	private RequestWrapper<DocumentCategoryDto> documentCategoryRequestDto;

	private RegistrationCenterMachineDeviceHistoryDto registrationCenterMachimeDeviceHistoryDto;
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;

	@MockBean
	BiometricAttributeRepository biometricAttributeRepository;

	@Autowired
	BiometricAttributeService biometricAttributeService;

	List<BiometricAttribute> biometricattributes = null;

	@MockBean
	private BiometricTypeRepository biometricTypeRepository;

	@Autowired
	private BiometricTypeService biometricTypeService;

	private BiometricType biometricType1 = new BiometricType();
	private BiometricType biometricType2 = new BiometricType();

	List<BiometricType> biometricTypeList = new ArrayList<>();
	@MockBean
	private LocationHierarchyRepository locationHierarchyRepository1;

	@Autowired
	private BlocklistedWordsService blocklistedWordsService;
	
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private RegistrationCenterService registrationCenterService;
	
	@Autowired
	private RegistrationCenterTypeService registrationCenterTypeService;

	private RegistrationCenter registrationCenter;
	
	private List<RegistrationCenter> registrationCenters = new ArrayList<RegistrationCenter>();
	List<RegistrationCenterType> registrationCenterTypes = new ArrayList<RegistrationCenterType>();

	@MockBean
	private RegistrationCenterRepository registrationCenterRepository;
	
	@MockBean
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;

	@MockBean
	private BlocklistedWordsRepository wordsRepository;

	List<BlocklistedWords> words;

	@MockBean
	DeviceSpecificationRepository deviceSpecificationRepository;

	@Autowired
	DeviceSpecificationService deviceSpecificationService;

	List<DeviceSpecification> deviceSpecifications = null;
	List<DeviceSpecification> deviceSpecificationListWithDeviceTypeCode = null;

	@MockBean
	DocumentCategoryRepository documentCategoryRepository;

	@Autowired
	DocumentCategoryService documentCategoryService;

	@MockBean
	RegWorkingNonWorkingRepo regWorkingNonWorkingRepo;
	@MockBean
	private DaysOfWeekListRepo daysOfWeekRepo;

	@Autowired
	RegWorkingNonWorkingService regWorkingNonWorkingService;

	@Autowired
	ExceptionalHolidayService exceptionalHolidayService;

	@MockBean
	ExceptionalHolidayRepository exceptionalHolidayRepository;
	
	@MockBean
	private FilterColumnValidator filterColumnValidator;
	
	@MockBean
	private MasterDataFilterHelper masterDataFilterHelper;

	private DocumentCategory documentCategory1;
	private DocumentCategory documentCategory2;

	private List<DocumentCategory> documentCategoryList = new ArrayList<>();

	@Autowired
	private LanguageService languageService;
	@Autowired
	private RegistrationCenterDeviceHistoryService registrationCenterDeviceHistoryService;

	private RegCenterMachineDeviceHistoryResponseDto regCenterMachineDeviceHistroyResponseDto;
	@MockBean
	private LanguageRepository languageRepository;

	private List<Language> languages;
	private LanguageResponseDto resp;
	private List<LanguageDto> languageDtos;
	private Language hin;
	private Language eng;
	private LanguageDto hinDto;
	private LanguageDto engDto;

	@MockBean
	LocationRepository locationHierarchyRepository;

	@Autowired
	LocationService locationHierarchyService;
	
	@Autowired
	private ZoneService zoneService;

	@Autowired
	LocationHierarchyService locationHierarchyLevelService;

	@MockBean
	private DeviceHistoryRepository deviceHistoryRepository;

	List<Location> locationHierarchies = null;
	List<Location> locationHierarchyList = null;
	List<LocationHierarchy> locationHierarchyLevelList = null;
	List<Object[]> locObjList = null;
	LocationCodeResponseDto locationCodeResponseDto = null;
	Location locationHierarchy = null;
	LocationHierarchy locationHierarchyLevel = null;
	LocationHierarchy locationHierarchyLevel1 = null;
	Location locationHierarchy1 = null;
	LocationDto locationDtos = null;
	Location locationHierarchy2 = null;
	Location locationHierarchy3 = null;

	RequestWrapper<LocationDto> requestLocationDto = null;
	RequestWrapper<LocationDto> requestLocationDto1 = null;

	RequestWrapper<LocationPutDto> requestLocationPutDto = null;
	RequestWrapper<LocationPutDto> requestLocationPutDto1 = null;

	@MockBean
	private TemplateRepository templateRepository;

	@MockBean
	private TemplateFileFormatRepository templateFileFormatRepository;

	@Autowired
	private TemplateFileFormatService templateFileFormatService;

	private TemplateFileFormat templateFileFormat;
	private List<TemplateFileFormat> templateFileFormats = new ArrayList<TemplateFileFormat>();

	private RequestWrapper<TemplateFileFormatDto> templateFileFormatRequestDto;

	private RequestWrapper<TemplateFileFormatPutDto> templateFileFormatPutRequestDto;

	@Autowired
	private TemplateService templateService;

	private List<Template> templateList = new ArrayList<>();

	private TemplateResponseDto templateResponseDto;
	
	@MockBean
	private DynamicFieldRepository dynamicFieldRepository;
	
	@Autowired
	private DynamicFieldService dynamicFieldService;

	@MockBean
	DocumentTypeRepository documentTypeRepository;
	
	@MockBean
	DeviceRepository deviceRepository;

	@Autowired
	DocumentTypeService documentTypeService;
	
	@Autowired
	private DeviceTypeService deviceTypeService;

	List<DocumentType> documents = null;
	List<Device> devices = null;
	List<DeviceType> deviceTypes = null;
	List<DynamicField> dynamicFields = null;
	
	// -----------------------------DeviceType-------------------------------------------------
	@MockBean
	private DeviceTypeRepository deviceTypeRepository;

	@MockBean
	private MetaDataUtils metaUtils;
	
	@MockBean
	MasterdataCreationUtil masterdataCreationUtil;

	// -----------------------------DeviceSpecification----------------------------------

	private List<DeviceSpecification> deviceSpecificationList;
	private DeviceSpecification deviceSpecification;

	@Autowired
	MachineHistoryService machineHistoryService;

	@Autowired
	DeviceHistoryService deviceHistoryService;

	private RequestWrapper<BiometricTypeDto> biometricTypeRequestWrapper;

	private BiometricTypeDto biometricTypeDto;

	@Autowired
	RegistrationCenterValidator registrationCenterValidator;

	@MockBean
	ZoneUserRepository zoneUserRepository;

	@MockBean
	ZoneRepository zoneRepository;

	@Before
	public void setUp() {

		applicationSetup();

		biometricAttrSetup();

		biometricTypeSetup();

		blockListedSetup();

		deviceSpecSetup();

		docCategorySetup();

		langServiceSetup();

		locationServiceSetup();

		locationHierarchyLevelSetup();

		templateServiceSetup();

		templateFileFormatSetup();

		documentTypeSetup();
		
		deviceSetup();
		
		deviceTypeSetup();
		
		dynamicFieldsSetup();

		registrationCenterSetup();
		registrationCenterTypeSetup();
		updateRegistrationCenter();

		LocationHierarchy hierarchy = new LocationHierarchy((short) 3, "City", "eng");
		when(locationHierarchyRepository1.findByLangCodeAndLevelAndName(Mockito.anyString(), Mockito.anyShort(),
				Mockito.anyString())).thenReturn(hierarchy);
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
	}

	private void documentTypeSetup() {
		documents = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setCode("addhar");
		documentType.setName("addhar_card");
		documentType.setDescription("adhar_card_desc");
		documentType.setIsActive(true);
		documents.add(documentType);
		DocumentType documentType1 = new DocumentType();
		documentType1.setCode("residensial");
		documentType1.setName("residensial_proof");
		documentType1.setDescription("residensial_proof_desc");
		documentType1.setIsActive(true);
		documents.add(documentType1);
	}

	private void deviceSetup() {
		devices = new ArrayList<Device>();
		Device device = new Device();
		device.setId("123");
		device.setIsActive(true);
		device.setLangCode("eng");
		device.setName("mock");
		devices.add(device);
	}
	
	private void deviceTypeSetup() {
		deviceTypes = new ArrayList<DeviceType>();
		DeviceType deviceType = new DeviceType();
		deviceType.setCode("ECR");
		deviceType.setIsActive(true);
		deviceType.setLangCode("eng");
		deviceType.setName("mock");
		deviceTypes.add(deviceType);
	}
	
	private void dynamicFieldsSetup() {
		dynamicFields = new ArrayList<DynamicField>();
		DynamicField dynamicField = new DynamicField();
		dynamicField.setId("COD");
		dynamicField.setIsActive(true);
		dynamicField.setLangCode("eng");
		dynamicField.setName("mock");
		dynamicFields.add(dynamicField);
	}

	private void templateServiceSetup() {
		Template template = new Template();
		template.setId("3");
		template.setName("Email template");
		template.setFileFormatCode("xml");
		template.setTemplateTypeCode("EMAIL");
		template.setLangCode("HIN");
		template.setCreatedBy("Neha");
		template.setCreatedDateTime(LocalDateTime.of(2018, Month.NOVEMBER, 12, 0, 0, 0));
		template.setIsActive(true);
		template.setIsDeleted(false);

		templateList.add(template);
	}

	private void locationServiceSetup() {
		locationHierarchies = new ArrayList<>();
		locationHierarchy = new Location();
		locationHierarchy.setCode("IND");
		locationHierarchy.setName("INDIA");
		locationHierarchy.setHierarchyLevel((short) 0);
		locationHierarchy.setHierarchyName("country");
		locationHierarchy.setParentLocCode(null);
		locationHierarchy.setLangCode("HIN");
		locationHierarchy.setCreatedBy("dfs");
		locationHierarchy.setUpdatedBy("sdfsd");
		locationHierarchy.setIsActive(true);
		locationHierarchies.add(locationHierarchy);
		locationHierarchy1 = new Location();
		locationHierarchy1.setCode("KAR");
		locationHierarchy1.setName("KARNATAKA");
		locationHierarchy1.setHierarchyLevel((short) 1);
		locationHierarchy1.setHierarchyName(null);
		locationHierarchy1.setParentLocCode("TEST");
		locationHierarchy1.setLangCode("KAN");
		locationHierarchy1.setCreatedBy("dfs");
		locationHierarchy1.setUpdatedBy("sdfsd");
		locationHierarchy1.setIsActive(true);
		locationHierarchies.add(locationHierarchy1);
		Object[] objectArray = new Object[3];
		objectArray[0] = (short) 0;
		objectArray[1] = "COUNTRY";
		objectArray[2] = true;
		locObjList = new ArrayList<>();
		locObjList.add(objectArray);
		LocationDto locationDto = new LocationDto();
		locationDto.setCode("KAR");
		locationDto.setName("KARNATAKA");
		locationDto.setHierarchyLevel((short) 2);
		locationDto.setHierarchyName("STATE");
		locationDto.setLangCode("FRA");
		locationDto.setParentLocCode("IND");
		locationDto.setIsActive(true);
		requestLocationDto = new RequestWrapper<>();
		requestLocationDto.setRequest(locationDto);

		LocationPutDto locationPutDto = new LocationPutDto();
		locationPutDto.setCode("KAR");
		locationPutDto.setName("KARNATAKA");
		locationPutDto.setHierarchyLevel((short) 2);
		locationPutDto.setHierarchyName("STATE");
		locationPutDto.setLangCode("FRA");
		locationPutDto.setParentLocCode("IND");
		requestLocationPutDto = new RequestWrapper<LocationPutDto>();
		requestLocationPutDto.setRequest(locationPutDto);

		locationHierarchyList = new ArrayList<>();
		locationHierarchy3 = new Location();
		locationHierarchy3.setCode("KAR");
		locationHierarchy3.setName("KARNATAKA");
		locationHierarchy3.setHierarchyLevel((short) 1);
		locationHierarchy3.setHierarchyName(null);
		locationHierarchy3.setParentLocCode("IND");
		locationHierarchy3.setLangCode("KAN");
		locationHierarchy3.setCreatedBy("dfs");
		locationHierarchy3.setUpdatedBy("sdfsd");
		locationHierarchy3.setIsActive(true);
		locationHierarchyList.add(locationHierarchy3);

		LocationDto locationDto1 = new LocationDto();
		locationDto1.setCode("IND");
		locationDto1.setName("INDIA");
		locationDto1.setHierarchyLevel((short) 1);
		locationDto1.setHierarchyName("CONTRY");
		locationDto1.setLangCode("HIN");
		locationDto1.setParentLocCode(null);
		locationDto1.setIsActive(false);
		requestLocationDto1 = new RequestWrapper<>();
		requestLocationDto1.setRequest(locationDto1);

		LocationPutDto locationPutDto1 = new LocationPutDto();
		locationPutDto1.setCode("IND");
		locationPutDto1.setName("INDIA");
		locationPutDto1.setHierarchyLevel((short) 1);
		locationPutDto1.setHierarchyName("CONTRY");
		locationPutDto1.setLangCode("HIN");
		locationPutDto1.setParentLocCode(null);
		requestLocationPutDto1 = new RequestWrapper<LocationPutDto>();
		requestLocationPutDto1.setRequest(locationPutDto1);
	}

	private void locationHierarchyLevelSetup() {

		locationHierarchyLevelList = new ArrayList<LocationHierarchy>();
		locationHierarchyLevel = new LocationHierarchy();
		locationHierarchyLevel.setCreatedBy("sds");
		locationHierarchyLevel.setHierarchyLevel((short) 0);
		locationHierarchyLevel.setHierarchyLevelName("Country");
		locationHierarchyLevel.setIsActive(true);
		locationHierarchyLevel.setIsDeleted(false);
		locationHierarchyLevel.setLangCode("eng");
		locationHierarchyLevel.setUpdatedBy(null);
		locationHierarchyLevelList.add(locationHierarchyLevel);

		locationHierarchyLevel1 = new LocationHierarchy();
		locationHierarchyLevel1.setCreatedBy("sds");
		locationHierarchyLevel1.setHierarchyLevel((short) 1);
		locationHierarchyLevel1.setHierarchyLevelName("Region");
		locationHierarchyLevel1.setIsActive(true);
		locationHierarchyLevel1.setIsDeleted(false);
		locationHierarchyLevel1.setLangCode("eng");
		locationHierarchyLevel1.setUpdatedBy(null);
		locationHierarchyLevelList.add(locationHierarchyLevel1);

	}

	private void langServiceSetup() {
		languages = new ArrayList<>();

		// creating language
		hin = new Language();
		hin.setCode("hin");
		hin.setName("hindi");
		hin.setFamily("hindi");
		hin.setNativeName("hindi");
		hin.setIsActive(Boolean.TRUE);

		eng = new Language();
		eng.setCode("en");
		eng.setName("english");
		eng.setFamily("english");
		eng.setNativeName("english");
		eng.setIsActive(Boolean.TRUE);

		// adding language to list
		languages.add(hin);
		languages.add(eng);

		languageDtos = new ArrayList<>();
		// creating language
		hinDto = new LanguageDto();
		hinDto.setCode("hin");
		hinDto.setName("hindi");
		hinDto.setFamily("hindi");
		hinDto.setNativeName("hindi");

		engDto = new LanguageDto();
		engDto.setCode("en");
		engDto.setName("english");
		engDto.setFamily("english");
		engDto.setNativeName("english");

		languageDtos.add(hinDto);
		languageDtos.add(engDto);

		resp = new LanguageResponseDto();
		resp.setLanguages(languageDtos);
	}

	private void docCategorySetup() {
		documentCategory1 = new DocumentCategory();
		documentCategory1.setCode("101");
		documentCategory1.setName("POI");
		documentCategory1.setLangCode("eng");
		documentCategory1.setIsActive(true);
		documentCategory1.setIsDeleted(false);
		documentCategory1.setDescription(null);
		documentCategory1.setCreatedBy("Neha");
		documentCategory1.setUpdatedBy(null);

		documentCategory2 = new DocumentCategory();
		documentCategory2.setCode("102");
		documentCategory2.setName("POR");
		documentCategory2.setLangCode("eng");
		documentCategory2.setIsActive(true);
		documentCategory2.setIsDeleted(false);
		documentCategory2.setDescription(null);
		documentCategory2.setCreatedBy("Neha");
		documentCategory2.setUpdatedBy(null);

		documentCategoryList.add(documentCategory1);
		documentCategoryList.add(documentCategory2);

		documentCategoryRequestDto = new RequestWrapper<DocumentCategoryDto>();
		DocumentCategoryDto documentCategoryDto = new DocumentCategoryDto();
		documentCategoryDto.setCode("102");
		documentCategoryDto.setName("POR");
		documentCategoryDto.setDescription(null);
		documentCategoryDto.setLangCode("eng");
		documentCategoryDto.setIsActive(true);

		documentCategoryRequestDto.setRequest(documentCategoryDto);
	}

	private void deviceSpecSetup() {
		deviceSpecifications = new ArrayList<>();
		deviceSpecification = new DeviceSpecification();
		deviceSpecification.setId("lp");
		deviceSpecification.setName("laptop");
		deviceSpecification.setBrand("hp");
		deviceSpecification.setModel("pavalian_dv6");
		deviceSpecification.setDeviceTypeCode("operating_sys");
		deviceSpecification.setMinDriverversion("window_10");
		deviceSpecification.setDescription("laptop discription");
		deviceSpecification.setLangCode("ENG");
		deviceSpecification.setIsActive(true);
		deviceSpecifications.add(deviceSpecification);
		DeviceSpecification deviceSpecification1 = new DeviceSpecification();
		deviceSpecification1.setId("printer");
		deviceSpecification1.setName("printer");
		deviceSpecification1.setBrand("hp");
		deviceSpecification1.setModel("marker_dv6");
		deviceSpecification1.setDeviceTypeCode("printer_id");
		deviceSpecification1.setMinDriverversion("ver_5.0");
		deviceSpecification1.setDescription("printer discription");
		deviceSpecification1.setLangCode("ENG");
		deviceSpecification1.setIsActive(true);
		deviceSpecifications.add(deviceSpecification1);
		deviceSpecificationListWithDeviceTypeCode = new ArrayList<DeviceSpecification>();
		deviceSpecificationListWithDeviceTypeCode.add(deviceSpecification);

		deviceSpecificationList = new ArrayList<>();
		deviceSpecification = new DeviceSpecification();
		deviceSpecification.setId("100");
		deviceSpecification.setDeviceTypeCode("Laptop");
		deviceSpecification.setLangCode("ENG");
		deviceSpecificationList.add(deviceSpecification);

		IdResponseDto idResponseDto = new IdResponseDto();
		idResponseDto.setId("1111");
	}

	private void blockListedSetup() {
		words = new ArrayList<>();

		BlocklistedWords blocklistedWords = new BlocklistedWords();
		blocklistedWords.setWord("abc");
		blocklistedWords.setLangCode("ENG");
		blocklistedWords.setDescription("no description available");

		words.add(blocklistedWords);
	}

	private void biometricTypeSetup() {
		biometricType1.setCode("1");
		biometricType1.setName("DNA MATCHING");
		biometricType1.setDescription(null);
		biometricType1.setLangCode("eng");
		biometricType1.setIsActive(true);
		biometricType1.setCreatedBy("Neha");
		biometricType1.setUpdatedBy(null);
		biometricType1.setIsDeleted(false);

		biometricType2.setCode("3");
		biometricType2.setName("EYE SCAN");
		biometricType2.setDescription(null);
		biometricType2.setLangCode("eng");
		biometricType2.setIsActive(true);
		biometricType2.setCreatedBy("Neha");
		biometricType2.setUpdatedBy(null);
		biometricType2.setIsDeleted(false);

		biometricTypeList.add(biometricType1);
		biometricTypeList.add(biometricType2);

		biometricTypeRequestWrapper = new RequestWrapper<BiometricTypeDto>();
		biometricTypeDto = new BiometricTypeDto();
		biometricTypeDto.setCode("1");
		biometricTypeDto.setName("DNA MATCHING");
		biometricTypeDto.setDescription(null);
		biometricTypeDto.setIsActive(true);
		biometricTypeRequestWrapper.setRequest(biometricTypeDto);
	}

	private void biometricAttrSetup() {
		biometricattributes = new ArrayList<>();
		BiometricAttribute biometricAttribute = new BiometricAttribute();
		biometricAttribute.setCode("iric_black");
		biometricAttribute.setName("black");
		biometricAttribute.setIsActive(true);
		biometricattributes.add(biometricAttribute);
		BiometricAttribute biometricAttribute1 = new BiometricAttribute();
		biometricAttribute1.setCode("iric_brown");
		biometricAttribute1.setName("brown");
		biometricAttribute1.setIsActive(true);
		biometricattributes.add(biometricAttribute1);

	}

	private void applicationSetup() {
		application1 = new Application();
		application2 = new Application();

		applicationList = new ArrayList<>();
		application1.setCode("101");
		application1.setName("pre-registeration");
		application1.setDescription("Pre-registration Application Form");
		application1.setLangCode("eng");
		application1.setIsActive(true);
		application1.setCreatedBy("Neha");
		application1.setUpdatedBy(null);
		application1.setIsDeleted(false);

		application2.setCode("102");
		application2.setName("registeration");
		application2.setDescription("Registeration Application Form");
		application2.setLangCode("eng");
		application2.setIsActive(true);
		application2.setCreatedBy("Neha");
		application2.setUpdatedBy(null);
		application2.setIsDeleted(false);

		applicationList.add(application1);
		applicationList.add(application2);

		applicationRequestWrapper = new RequestWrapper<ApplicationDto>();
		applicationDto = new ApplicationDto();
		applicationDto.setCode("101");
		applicationDto.setName("pre-registeration");
		applicationDto.setDescription("Pre-registration Application Form");
		applicationDto.setLangCode("eng");
		applicationDto.setIsActive(true);
		applicationRequestWrapper.setRequest(applicationDto);
	}

	private void templateFileFormatSetup() {
		templateFileFormat = new TemplateFileFormat();
		templateFileFormat.setCode("xml");
		templateFileFormat.setLangCode("eng");
		templateFileFormats.add(templateFileFormat);

		templateFileFormatRequestDto = new RequestWrapper<TemplateFileFormatDto>();
		TemplateFileFormatDto templateFileFormatDto = new TemplateFileFormatDto();
		templateFileFormatDto.setCode("xml");
		templateFileFormatDto.setIsActive(true);
		TemplateFileFormatPutDto templateFileFormatPutDto = new TemplateFileFormatPutDto();
		templateFileFormatPutDto.setCode("xml");
		templateFileFormatPutDto.setLangCode("eng");
		templateFileFormatPutDto.setDescription("test");
		templateFileFormatPutRequestDto = new RequestWrapper<TemplateFileFormatPutDto>();
		templateFileFormatPutRequestDto.setRequest(templateFileFormatPutDto);
		templateFileFormatRequestDto.setRequest(templateFileFormatDto);
	}

	List<RegCenterPostReqDto> requestNotAllLang = null;
	List<RegCenterPostReqDto> requestDuplicateLang = null;
	List<RegCenterPostReqDto> requestSetLongitudeInvalide = null;
	List<RegCenterPostReqDto> requestCenterTime = null;
	List<RegCenterPostReqDto> requestLunchTime = null;
	List<RegCenterPostReqDto> requestZoneCode = null;
	RegCenterPostReqDto registrationCenterDto1 = null;
	RegCenterPostReqDto registrationCenterDto2 = null;
	RegCenterPostReqDto registrationCenterDto3 = null;
	RegCenterPostReqDto registrationCenterDto4 = null;
	RegCenterPostReqDto registrationCenterDto5 = null;
	RegCenterPostReqDto registrationCenterDto6 = null;
	RegCenterPostReqDto registrationCenterDto7 = null;
	RegCenterPostReqDto registrationCenterDto8 = null;

	@MockBean
	ZoneUtils zoneUtils;

	@MockBean
	private AuditUtil auditUtil;

	List<Zone> zones = null;

	private void registrationCenterSetup() {
		Zone zone = new Zone();
		zone.setCode("JRD");
		zones = new ArrayList<>();
		zones.add(zone);

		// ----------
		registrationCenter = new RegistrationCenter();
		registrationCenter.setId("1");
		registrationCenter.setName("bangalore");
		registrationCenter.setLatitude("12.9180722");
		registrationCenter.setLongitude("77.5028792");
		registrationCenter.setLangCode("ENG");
		registrationCenter.setCreatedBy("system");
		registrationCenter.setCreatedDateTime(LocalDateTime.now());
		registrationCenter.setHolidayLocationCode("BLR");
		registrationCenter.setLocationCode("BLR");
		registrationCenter.setZoneCode("test");
		registrationCenters.add(registrationCenter);

		// ----
		LocalTime centerStartTime = LocalTime.of(1, 10, 10, 30);
		LocalTime centerEndTime = LocalTime.of(1, 10, 10, 30);
		LocalTime lunchStartTime = LocalTime.of(1, 10, 10, 30);
		LocalTime lunchEndTime = LocalTime.of(1, 10, 10, 30);
		LocalTime perKioskProcessTime = LocalTime.of(1, 10, 10, 30);

		LocalTime centerStartTimeGrt = LocalTime.parse("18:00:00");
		LocalTime centerEndTimeSm = LocalTime.parse("17:00:00");
		LocalTime lunchStartTimeGrt = LocalTime.parse("18:00:00");
		LocalTime lunchEndTimeSm = LocalTime.parse("17:00:00");

		requestNotAllLang = new ArrayList<>();
		requestDuplicateLang = new ArrayList<>();
		requestSetLongitudeInvalide = new ArrayList<>();
		requestCenterTime = new ArrayList<>();
		requestLunchTime = new ArrayList<>();
		requestZoneCode = new ArrayList<>();

		// 1st obj
		registrationCenterDto1 = new RegCenterPostReqDto();
		registrationCenterDto1.setName("TEST CENTER");
		registrationCenterDto1.setAddressLine1("Address Line 1");
		registrationCenterDto1.setAddressLine2("Address Line 2");
		registrationCenterDto1.setAddressLine3("Address Line 3");
		registrationCenterDto1.setCenterTypeCode("REG");
		registrationCenterDto1.setContactPerson("Test");
		registrationCenterDto1.setContactPhone("9999999999");
		registrationCenterDto1.setHolidayLocationCode("HLC01");
		registrationCenterDto1.setLangCode("eng");
		registrationCenterDto1.setLatitude("12.9646818");
		registrationCenterDto1.setLocationCode("10190");
		registrationCenterDto1.setLongitude("77.70168");
		registrationCenterDto1.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto1.setCenterStartTime(centerStartTime);
		registrationCenterDto1.setCenterEndTime(centerEndTime);
		registrationCenterDto1.setLunchStartTime(lunchStartTime);
		registrationCenterDto1.setLunchEndTime(lunchEndTime);
		registrationCenterDto1.setTimeZone("UTC");
		registrationCenterDto1.setWorkingHours("9");
		registrationCenterDto1.setZoneCode("JRD");
		requestNotAllLang.add(registrationCenterDto1);
		requestDuplicateLang.add(registrationCenterDto1);
		requestCenterTime.add(registrationCenterDto1);
		requestLunchTime.add(registrationCenterDto1);
		requestZoneCode.add(registrationCenterDto1);

		// 2nd obj
		registrationCenterDto2 = new RegCenterPostReqDto();
		registrationCenterDto2.setName("TEST CENTER");
		registrationCenterDto2.setAddressLine1("Address Line 1");
		registrationCenterDto2.setAddressLine2("Address Line 2");
		registrationCenterDto2.setAddressLine3("Address Line 3");
		registrationCenterDto2.setCenterTypeCode("REG");
		registrationCenterDto2.setContactPerson("Test");
		registrationCenterDto2.setContactPhone("9999999999");
		registrationCenterDto2.setHolidayLocationCode("HLC01");
		registrationCenterDto2.setLangCode("ara");
		registrationCenterDto2.setLatitude("12.9646818");
		registrationCenterDto2.setLocationCode("10190");
		registrationCenterDto2.setLongitude("77.70168");
		registrationCenterDto2.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto2.setCenterStartTime(centerStartTime);
		registrationCenterDto2.setCenterEndTime(centerEndTime);
		registrationCenterDto2.setLunchStartTime(lunchStartTime);
		registrationCenterDto2.setLunchEndTime(lunchEndTime);
		registrationCenterDto2.setTimeZone("UTC");
		registrationCenterDto2.setWorkingHours("9");
		registrationCenterDto2.setZoneCode("JRD");
		requestNotAllLang.add(registrationCenterDto2);
		requestDuplicateLang.add(registrationCenterDto2);
		requestCenterTime.add(registrationCenterDto2);
		requestLunchTime.add(registrationCenterDto2);
		requestZoneCode.add(registrationCenterDto2);

		// 3rd obj
		registrationCenterDto3 = new RegCenterPostReqDto();
		registrationCenterDto3.setName("TEST CENTER");
		registrationCenterDto3.setAddressLine1("Address Line 1");
		registrationCenterDto3.setAddressLine2("Address Line 2");
		registrationCenterDto3.setAddressLine3("Address Line 3");
		registrationCenterDto3.setCenterTypeCode("REG");
		registrationCenterDto3.setContactPerson("Test");
		registrationCenterDto3.setContactPhone("9999999999");
		registrationCenterDto3.setHolidayLocationCode("HLC01");
		registrationCenterDto3.setLangCode("fra");
		registrationCenterDto3.setLatitude("12.9646818");
		registrationCenterDto3.setLocationCode("10190");
		registrationCenterDto3.setLongitude("77.70168");
		registrationCenterDto3.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto3.setCenterStartTime(centerStartTime);
		registrationCenterDto3.setCenterEndTime(centerEndTime);
		registrationCenterDto3.setLunchStartTime(lunchStartTime);
		registrationCenterDto3.setLunchEndTime(lunchEndTime);
		registrationCenterDto3.setTimeZone("UTC");
		registrationCenterDto3.setWorkingHours("9");
		registrationCenterDto3.setZoneCode("JRD");

		registrationCenterDto4 = new RegCenterPostReqDto();
		registrationCenterDto4.setName("TEST CENTER");
		registrationCenterDto4.setAddressLine1("Address Line 1");
		registrationCenterDto4.setAddressLine2("Address Line 2");
		registrationCenterDto4.setAddressLine3("Address Line 3");
		registrationCenterDto4.setCenterTypeCode("REG");
		registrationCenterDto4.setContactPerson("Test");
		registrationCenterDto4.setContactPhone("9999999999");
		registrationCenterDto4.setHolidayLocationCode("HLC01");
		registrationCenterDto4.setLangCode("eng");
		registrationCenterDto4.setLatitude("-7.333");
		registrationCenterDto4.setLocationCode("10190");
		registrationCenterDto4.setLongitude("77.70168");
		registrationCenterDto4.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto4.setCenterStartTime(centerStartTime);
		registrationCenterDto4.setCenterEndTime(centerEndTime);
		registrationCenterDto4.setLunchStartTime(lunchStartTime);
		registrationCenterDto4.setLunchEndTime(lunchEndTime);
		registrationCenterDto4.setTimeZone("UTC");
		registrationCenterDto4.setWorkingHours("9");
		registrationCenterDto4.setZoneCode("JRD");
		requestSetLongitudeInvalide.add(registrationCenterDto4);

		registrationCenterDto5 = new RegCenterPostReqDto();
		registrationCenterDto5.setName("TEST CENTER");
		registrationCenterDto5.setAddressLine1("Address Line 1");
		registrationCenterDto5.setAddressLine2("Address Line 2");
		registrationCenterDto5.setAddressLine3("Address Line 3");
		registrationCenterDto5.setCenterTypeCode("REG");
		registrationCenterDto5.setContactPerson("Test");
		registrationCenterDto5.setContactPhone("9999999999");
		registrationCenterDto5.setHolidayLocationCode("HLC01");
		registrationCenterDto5.setLangCode("fra");
		registrationCenterDto5.setLatitude("12.9646818");
		registrationCenterDto5.setLocationCode("10190");
		registrationCenterDto5.setLongitude("77.70168");
		registrationCenterDto5.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto5.setCenterStartTime(centerStartTime);
		registrationCenterDto5.setCenterEndTime(centerEndTime);
		registrationCenterDto5.setLunchStartTime(lunchStartTime);
		registrationCenterDto5.setLunchEndTime(lunchEndTime);
		registrationCenterDto5.setTimeZone("UTC");
		registrationCenterDto5.setWorkingHours("9");
		registrationCenterDto5.setZoneCode("JRD");
		requestDuplicateLang.add(registrationCenterDto5);
		requestDuplicateLang.add(registrationCenterDto5);

		registrationCenterDto6 = new RegCenterPostReqDto();
		registrationCenterDto6.setName("TEST CENTER");
		registrationCenterDto6.setAddressLine1("Address Line 1");
		registrationCenterDto6.setAddressLine2("Address Line 2");
		registrationCenterDto6.setAddressLine3("Address Line 3");
		registrationCenterDto6.setCenterTypeCode("REG");
		registrationCenterDto6.setContactPerson("Test");
		registrationCenterDto6.setContactPhone("9999999999");
		registrationCenterDto6.setHolidayLocationCode("HLC01");
		registrationCenterDto6.setLangCode("eng");
		registrationCenterDto6.setLatitude("12.9646818");
		registrationCenterDto6.setLocationCode("10190");
		registrationCenterDto6.setLongitude("77.70168");
		registrationCenterDto6.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto6.setCenterStartTime(centerStartTimeGrt);
		registrationCenterDto6.setCenterEndTime(centerEndTimeSm);
		registrationCenterDto6.setLunchStartTime(lunchStartTime);
		registrationCenterDto6.setLunchEndTime(lunchEndTime);
		registrationCenterDto6.setTimeZone("UTC");
		registrationCenterDto6.setWorkingHours("9");
		registrationCenterDto6.setZoneCode("JRD");
		requestCenterTime.add(registrationCenterDto6);

		registrationCenterDto7 = new RegCenterPostReqDto();
		registrationCenterDto7.setName("TEST CENTER");
		registrationCenterDto7.setAddressLine1("Address Line 1");
		registrationCenterDto7.setAddressLine2("Address Line 2");
		registrationCenterDto7.setAddressLine3("Address Line 3");
		registrationCenterDto7.setCenterTypeCode("REG");
		registrationCenterDto7.setContactPerson("Test");
		registrationCenterDto7.setContactPhone("9999999999");
		registrationCenterDto7.setHolidayLocationCode("HLC01");
		registrationCenterDto7.setLangCode("eng");
		registrationCenterDto7.setLatitude("12.9646818");
		registrationCenterDto7.setLocationCode("10190");
		registrationCenterDto7.setLongitude("77.70168");
		registrationCenterDto7.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto7.setCenterStartTime(centerStartTime);
		registrationCenterDto7.setCenterEndTime(centerEndTime);
		registrationCenterDto7.setLunchStartTime(lunchStartTimeGrt);
		registrationCenterDto7.setLunchEndTime(lunchEndTimeSm);
		registrationCenterDto7.setTimeZone("UTC");
		registrationCenterDto7.setWorkingHours("9");
		registrationCenterDto7.setZoneCode("JRD");
		requestLunchTime.add(registrationCenterDto7);

		registrationCenterDto8 = new RegCenterPostReqDto();
		registrationCenterDto8.setName("TEST CENTER");
		registrationCenterDto8.setAddressLine1("Address Line 1");
		registrationCenterDto8.setAddressLine2("Address Line 2");
		registrationCenterDto8.setAddressLine3("Address Line 3");
		registrationCenterDto8.setCenterTypeCode("REG");
		registrationCenterDto8.setContactPerson("Test");
		registrationCenterDto8.setContactPhone("9999999999");
		registrationCenterDto8.setHolidayLocationCode("HLC01");
		registrationCenterDto8.setLangCode("eng");
		registrationCenterDto8.setLatitude("12.9646818");
		registrationCenterDto8.setLocationCode("10190");
		registrationCenterDto8.setLongitude("77.70168");
		registrationCenterDto8.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterDto8.setCenterStartTime(centerStartTime);
		registrationCenterDto8.setCenterEndTime(centerEndTime);
		registrationCenterDto8.setLunchStartTime(lunchStartTime);
		registrationCenterDto8.setLunchEndTime(lunchEndTime);
		registrationCenterDto8.setTimeZone("UTC");
		registrationCenterDto8.setWorkingHours("9");
		registrationCenterDto8.setZoneCode("JJJ");
		requestLunchTime.add(registrationCenterDto8);

	}
	
	private void registrationCenterTypeSetup() {
		RegistrationCenterType centerType = new RegistrationCenterType();
		centerType.setCode("AFD");
		centerType.setIsActive(true);
		centerType.setLangCode("eng");
		registrationCenterTypes.add(centerType);
		
	}

	List<RegCenterPutReqDto> updRequestNotAllLang = null;
	List<RegCenterPutReqDto> updRequestInvalideID = null;
	List<RegCenterPutReqDto> updRequestDuplicateIDLang = null;
	List<RegCenterPutReqDto> updRequestSetLongitudeInvalide = null;
	List<RegCenterPutReqDto> updRequestCenterTime = null;
	List<RegCenterPutReqDto> updRequestLunchTime = null;
	List<RegCenterPutReqDto> updRequestZoneCode = null;

	RegCenterPutReqDto registrationCenterPutReqAdmDto1 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto2 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto3 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto4 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto5 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto6 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto7 = null;
	RegCenterPutReqDto registrationCenterPutReqAdmDto8 = null;

	private void updateRegistrationCenter() {

		registrationCenter = new RegistrationCenter();
		registrationCenter.setId("1");
		registrationCenter.setName("bangalore");
		registrationCenter.setLatitude("12.9180722");
		registrationCenter.setLongitude("77.5028792");
		registrationCenter.setLangCode("ENG");

		// ----
		LocalTime centerStartTime = LocalTime.of(1, 10, 10, 30);
		LocalTime centerEndTime = LocalTime.of(1, 10, 10, 30);
		LocalTime lunchStartTime = LocalTime.of(1, 10, 10, 30);
		LocalTime lunchEndTime = LocalTime.of(1, 10, 10, 30);
		LocalTime perKioskProcessTime = LocalTime.of(1, 10, 10, 30);

		LocalTime centerStartTimeGrt = LocalTime.parse("18:00:00");
		LocalTime centerEndTimeSm = LocalTime.parse("17:00:00");
		LocalTime lunchStartTimeGrt = LocalTime.parse("18:00:00");
		LocalTime lunchEndTimeSm = LocalTime.parse("17:00:00");

		updRequestNotAllLang = new ArrayList<>();
		updRequestInvalideID = new ArrayList<>();
		updRequestDuplicateIDLang = new ArrayList<>();
		updRequestSetLongitudeInvalide = new ArrayList<>();
		updRequestCenterTime = new ArrayList<>();
		updRequestLunchTime = new ArrayList<>();
		updRequestZoneCode = new ArrayList<>();

		// 1st obj
		registrationCenterPutReqAdmDto1 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto1.setName("TEST CENTER");
		registrationCenterPutReqAdmDto1.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto1.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto1.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto1.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto1.setContactPerson("Test");
		registrationCenterPutReqAdmDto1.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto1.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto1.setId("676");
		registrationCenterPutReqAdmDto1.setLangCode("eng");
		registrationCenterPutReqAdmDto1.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto1.setLocationCode("10190");
		registrationCenterPutReqAdmDto1.setLongitude("77.70168");
		registrationCenterPutReqAdmDto1.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto1.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto1.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto1.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto1.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto1.setTimeZone("UTC");
		registrationCenterPutReqAdmDto1.setWorkingHours("9");
		registrationCenterPutReqAdmDto1.setZoneCode("JRD");
		updRequestNotAllLang.add(registrationCenterPutReqAdmDto1);
		updRequestInvalideID.add(registrationCenterPutReqAdmDto1);
		updRequestDuplicateIDLang.add(registrationCenterPutReqAdmDto1);
		updRequestCenterTime.add(registrationCenterPutReqAdmDto1);
		updRequestLunchTime.add(registrationCenterPutReqAdmDto1);
		updRequestZoneCode.add(registrationCenterPutReqAdmDto1);

		// 2nd obj
		registrationCenterPutReqAdmDto2 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto2.setName("TEST CENTER");
		registrationCenterPutReqAdmDto2.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto2.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto2.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto2.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto2.setContactPerson("Test");
		registrationCenterPutReqAdmDto2.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto2.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto2.setId("676");
		registrationCenterPutReqAdmDto2.setLangCode("ara");
		registrationCenterPutReqAdmDto2.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto2.setLocationCode("10190");
		registrationCenterPutReqAdmDto2.setLongitude("77.70168");
		registrationCenterPutReqAdmDto2.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto2.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto2.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto2.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto2.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto2.setTimeZone("UTC");
		registrationCenterPutReqAdmDto2.setWorkingHours("9");
		registrationCenterPutReqAdmDto2.setZoneCode("JRD");
		updRequestNotAllLang.add(registrationCenterPutReqAdmDto2);
		updRequestInvalideID.add(registrationCenterPutReqAdmDto2);
		updRequestDuplicateIDLang.add(registrationCenterPutReqAdmDto2);
		updRequestCenterTime.add(registrationCenterPutReqAdmDto2);
		updRequestLunchTime.add(registrationCenterPutReqAdmDto2);
		updRequestZoneCode.add(registrationCenterPutReqAdmDto2);

		// 3rd obj
		registrationCenterPutReqAdmDto3 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto3.setName("TEST CENTER");
		registrationCenterPutReqAdmDto3.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto3.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto3.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto3.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto3.setContactPerson("Test");
		registrationCenterPutReqAdmDto3.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto3.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto3.setId("6767");
		registrationCenterPutReqAdmDto3.setLangCode("fra");
		registrationCenterPutReqAdmDto3.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto3.setLocationCode("10190");
		registrationCenterPutReqAdmDto3.setLongitude("77.70168");
		registrationCenterPutReqAdmDto3.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto3.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto3.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto3.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto3.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto3.setTimeZone("UTC");
		registrationCenterPutReqAdmDto3.setWorkingHours("9");
		registrationCenterPutReqAdmDto3.setZoneCode("JRD");
		updRequestInvalideID.add(registrationCenterPutReqAdmDto3);

		registrationCenterPutReqAdmDto4 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto4.setName("TEST CENTER");
		registrationCenterPutReqAdmDto4.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto4.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto4.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto4.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto4.setContactPerson("Test");
		registrationCenterPutReqAdmDto4.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto4.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto4.setId("676");
		registrationCenterPutReqAdmDto4.setLangCode("eng");
		registrationCenterPutReqAdmDto4.setLatitude("-7.333");
		registrationCenterPutReqAdmDto4.setLocationCode("10190");
		registrationCenterPutReqAdmDto4.setLongitude("77.70168");
		registrationCenterPutReqAdmDto4.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto4.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto4.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto4.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto4.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto4.setTimeZone("UTC");
		registrationCenterPutReqAdmDto4.setWorkingHours("9");
		registrationCenterPutReqAdmDto4.setZoneCode("JRD");
		updRequestSetLongitudeInvalide.add(registrationCenterPutReqAdmDto4);

		registrationCenterPutReqAdmDto5 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto5.setName("TEST CENTER");
		registrationCenterPutReqAdmDto5.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto5.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto5.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto5.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto5.setContactPerson("Test");
		registrationCenterPutReqAdmDto5.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto5.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto5.setId("676");
		registrationCenterPutReqAdmDto5.setLangCode("fra");
		registrationCenterPutReqAdmDto5.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto5.setLocationCode("10190");
		registrationCenterPutReqAdmDto5.setLongitude("77.70168");
		registrationCenterPutReqAdmDto5.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto5.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto5.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto5.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto5.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto5.setTimeZone("UTC");
		registrationCenterPutReqAdmDto5.setWorkingHours("9");
		registrationCenterPutReqAdmDto5.setZoneCode("JRD");
		updRequestDuplicateIDLang.add(registrationCenterPutReqAdmDto5);
		updRequestDuplicateIDLang.add(registrationCenterPutReqAdmDto5);

		registrationCenterPutReqAdmDto6 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto6.setName("TEST CENTER");
		registrationCenterPutReqAdmDto6.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto6.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto6.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto6.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto6.setContactPerson("Test");
		registrationCenterPutReqAdmDto6.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto6.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto6.setId("676");
		registrationCenterPutReqAdmDto6.setLangCode("eng");
		registrationCenterPutReqAdmDto6.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto6.setLocationCode("10190");
		registrationCenterPutReqAdmDto6.setLongitude("77.70168");
		registrationCenterPutReqAdmDto6.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto6.setCenterStartTime(centerStartTimeGrt);
		registrationCenterPutReqAdmDto6.setCenterEndTime(centerEndTimeSm);
		registrationCenterPutReqAdmDto6.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto6.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto6.setTimeZone("UTC");
		registrationCenterPutReqAdmDto6.setWorkingHours("9");
		registrationCenterPutReqAdmDto6.setZoneCode("JRD");
		updRequestCenterTime.add(registrationCenterPutReqAdmDto6);

		registrationCenterPutReqAdmDto7 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto7.setName("TEST CENTER");
		registrationCenterPutReqAdmDto7.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto7.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto7.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto7.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto7.setContactPerson("Test");
		registrationCenterPutReqAdmDto7.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto7.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto7.setId("676");
		registrationCenterPutReqAdmDto7.setLangCode("eng");
		registrationCenterPutReqAdmDto7.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto7.setLocationCode("10190");
		registrationCenterPutReqAdmDto7.setLongitude("77.70168");
		registrationCenterPutReqAdmDto7.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto7.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto7.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto7.setLunchStartTime(lunchStartTimeGrt);
		registrationCenterPutReqAdmDto7.setLunchEndTime(lunchEndTimeSm);
		registrationCenterPutReqAdmDto7.setTimeZone("UTC");
		registrationCenterPutReqAdmDto7.setWorkingHours("9");
		registrationCenterPutReqAdmDto7.setZoneCode("JRD");
		updRequestLunchTime.add(registrationCenterPutReqAdmDto7);

		registrationCenterPutReqAdmDto8 = new RegCenterPutReqDto();
		registrationCenterPutReqAdmDto8.setName("TEST CENTER");
		registrationCenterPutReqAdmDto8.setAddressLine1("Address Line 1");
		registrationCenterPutReqAdmDto8.setAddressLine2("Address Line 2");
		registrationCenterPutReqAdmDto8.setAddressLine3("Address Line 3");
		registrationCenterPutReqAdmDto8.setCenterTypeCode("REG");
		registrationCenterPutReqAdmDto8.setContactPerson("Test");
		registrationCenterPutReqAdmDto8.setContactPhone("9999999999");
		registrationCenterPutReqAdmDto8.setHolidayLocationCode("HLC01");
		registrationCenterPutReqAdmDto8.setId("676");
		registrationCenterPutReqAdmDto8.setLangCode("eng");
		registrationCenterPutReqAdmDto8.setLatitude("12.9646818");
		registrationCenterPutReqAdmDto8.setLocationCode("10190");
		registrationCenterPutReqAdmDto8.setLongitude("77.70168");
		registrationCenterPutReqAdmDto8.setPerKioskProcessTime(perKioskProcessTime);
		registrationCenterPutReqAdmDto8.setCenterStartTime(centerStartTime);
		registrationCenterPutReqAdmDto8.setCenterEndTime(centerEndTime);
		registrationCenterPutReqAdmDto8.setLunchStartTime(lunchStartTime);
		registrationCenterPutReqAdmDto8.setLunchEndTime(lunchEndTime);
		registrationCenterPutReqAdmDto8.setTimeZone("UTC");
		registrationCenterPutReqAdmDto8.setWorkingHours("9");
		registrationCenterPutReqAdmDto8.setZoneCode("JJJ");
		updRequestZoneCode.add(registrationCenterPutReqAdmDto8);
	}

	// ----------------------- ApplicationServiceTest ----------------//
	@Test
	public void getAllApplicationSuccess() {
		Mockito.when(applicationRepository.findAllByIsDeletedFalseOrIsDeletedNull(Mockito.eq(Application.class)))
				.thenReturn(applicationList);
		ApplicationResponseDto applicationResponseDto = applicationService.getAllApplication();
		List<ApplicationDto> applicationDtos = applicationResponseDto.getApplicationtypes();
		assertEquals(applicationList.get(0).getCode(), applicationDtos.get(0).getCode());
		assertEquals(applicationList.get(0).getName(), applicationDtos.get(0).getName());
	}

	@Test
	public void getAllApplicationByLanguageCodeSuccess() {
		Mockito.when(applicationRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(applicationList);
		ApplicationResponseDto applicationResponseDto = applicationService
				.getAllApplicationByLanguageCode(Mockito.anyString());
		List<ApplicationDto> applicationDtoList = applicationResponseDto.getApplicationtypes();
		assertEquals(applicationList.get(0).getCode(), applicationDtoList.get(0).getCode());
		assertEquals(applicationList.get(0).getName(), applicationDtoList.get(0).getName());
	}

	@Test
	public void getApplicationByCodeAndLangCodeSuccess() {
		Mockito.when(applicationRepository.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),
				Mockito.anyString())).thenReturn(application1);
		ApplicationResponseDto applicationResponseDto = applicationService
				.getApplicationByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString());
		List<ApplicationDto> actual = applicationResponseDto.getApplicationtypes();
		assertEquals(application1.getCode(), actual.get(0).getCode());
		assertEquals(application1.getName(), actual.get(0).getName());
	}

	@Test
	public void addApplicationDataSuccess()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(applicationRepository.create(Mockito.any())).thenReturn(application1);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(Application.class), Mockito.any()))
				.thenReturn(applicationRequestWrapper.getRequest());

		CodeAndLanguageCodeID codeAndLanguageCodeId = applicationService
				.createApplication(applicationRequestWrapper.getRequest());
		assertEquals(applicationRequestWrapper.getRequest().getCode(), codeAndLanguageCodeId.getCode());
		assertEquals(applicationRequestWrapper.getRequest().getLangCode(), codeAndLanguageCodeId.getLangCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void addApplicationDataFetchException()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(applicationRepository.create(Mockito.any())).thenThrow(DataAccessLayerException.class);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(Application.class), Mockito.any()))
				.thenReturn(applicationRequestWrapper.getRequest());
		applicationService.createApplication(applicationRequestWrapper.getRequest());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllApplicationFetchException() {
		Mockito.when(applicationRepository.findAllByIsDeletedFalseOrIsDeletedNull(Mockito.eq(Application.class)))
				.thenThrow(DataRetrievalFailureException.class);
		applicationService.getAllApplication();
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllApplicationNotFoundException() {
		applicationList = new ArrayList<>();
		Mockito.when(applicationRepository.findAllByIsDeletedFalseOrIsDeletedNull(Application.class))
				.thenReturn(applicationList);
		applicationService.getAllApplication();
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllApplicationByLanguageCodeFetchException() {
		Mockito.when(applicationRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		applicationService.getAllApplicationByLanguageCode(Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllApplicationByLanguageCodeNotFoundException() {
		Mockito.when(applicationRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(new ArrayList<Application>());
		applicationService.getAllApplicationByLanguageCode(Mockito.anyString());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getApplicationByCodeAndLangCodeFetchException() {
		Mockito.when(applicationRepository.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),
				Mockito.anyString())).thenThrow(DataRetrievalFailureException.class);
		applicationService.getApplicationByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getApplicationByCodeAndLangCodeNotFoundException() {
		Mockito.when(applicationRepository.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),
				Mockito.anyString())).thenReturn(null);
		applicationService.getApplicationByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString());
	}

	// ------------------ BiometricAttributeServiceTest -----------------//
	@Test
	public void getBiometricAttributeTest() {
		String biometricTypeCode = "iric";
		String langCode = "eng";
		Mockito.when(biometricAttributeRepository
				.findByBiometricTypeCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(biometricTypeCode, langCode))
				.thenReturn(biometricattributes);

		List<BiometricAttributeDto> attributes = biometricAttributeService.getBiometricAttribute(biometricTypeCode,
				langCode);
		Assert.assertEquals(attributes.get(0).getCode(), biometricattributes.get(0).getCode());
		Assert.assertEquals(attributes.get(0).getName(), biometricattributes.get(0).getName());

	}

	@Test(expected = DataNotFoundException.class)
	public void noRecordsFoudExceptionTest() {
		List<BiometricAttribute> empityList = new ArrayList<BiometricAttribute>();
		String biometricTypeCode = "face";
		String langCode = "eng";
		Mockito.when(biometricAttributeRepository
				.findByBiometricTypeCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(biometricTypeCode, langCode))
				.thenReturn(empityList);
		biometricAttributeService.getBiometricAttribute(biometricTypeCode, langCode);
	}

	@Test(expected = DataNotFoundException.class)
	public void noRecordsFoudExceptionForNullTest() {
		String biometricTypeCode = "face";
		String langCode = "eng";
		Mockito.when(biometricAttributeRepository
				.findByBiometricTypeCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(biometricTypeCode, langCode))
				.thenReturn(null);
		biometricAttributeService.getBiometricAttribute(biometricTypeCode, langCode);
	}

	@Test(expected = MasterDataServiceException.class)
	public void dataAccessExceptionInGetAllTest() {
		String biometricTypeCode = "face";
		String langCode = "eng";
		Mockito.when(biometricAttributeRepository
				.findByBiometricTypeCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(biometricTypeCode, langCode))
				.thenThrow(DataAccessResourceFailureException.class);
		biometricAttributeService.getBiometricAttribute(biometricTypeCode, langCode);
	}

	// ------------------ BiometricTypeServiceTest -----------------//

	@Test(expected = MasterDataServiceException.class)
	public void getAllBiometricTypesFetchException() {
		Mockito.when(biometricTypeRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(BiometricType.class)))
				.thenThrow(DataRetrievalFailureException.class);
		biometricTypeService.getAllBiometricTypes();
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllBiometricTypesNotFoundException() {
		biometricTypeList = new ArrayList<>();
		Mockito.when(biometricTypeRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(BiometricType.class))
				.thenReturn(biometricTypeList);
		biometricTypeService.getAllBiometricTypes();
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllBiometricTypesByLanguageCodeFetchException() {
		Mockito.when(biometricTypeRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		biometricTypeService.getAllBiometricTypesByLanguageCode(Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllBiometricTypesByLanguageCodeNotFoundException() {
		Mockito.when(biometricTypeRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(new ArrayList<BiometricType>());
		biometricTypeService.getAllBiometricTypesByLanguageCode(Mockito.anyString());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getBiometricTypeByCodeAndLangCodeFetchException() {
		Mockito.when(biometricTypeRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		biometricTypeService.getBiometricTypeByCodeAndLangCode(Mockito.anyString(), Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getBiometricTypeByCodeAndLangCodeNotFoundException() {
		Mockito.when(biometricTypeRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		biometricTypeService.getBiometricTypeByCodeAndLangCode(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void addBiometricTypeDataSuccess()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(biometricTypeRepository.create(Mockito.any())).thenReturn(biometricType1);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(BiometricType.class), Mockito.any()))
				.thenReturn(biometricTypeRequestWrapper.getRequest());

		CodeAndLanguageCodeID codeAndLanguageCodeId = biometricTypeService
				.createBiometricType(biometricTypeRequestWrapper.getRequest());
		assertEquals(biometricTypeRequestWrapper.getRequest().getCode(), codeAndLanguageCodeId.getCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void addBiometricTypeDataInsertException()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(biometricTypeRepository.create(Mockito.any())).thenThrow(DataAccessLayerException.class);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(BiometricType.class), Mockito.any()))
				.thenReturn(biometricTypeRequestWrapper.getRequest());
		biometricTypeService.createBiometricType(biometricTypeRequestWrapper.getRequest());
	}

	@Test
	public void getAllBioTypesSuccess() {
		Mockito.when(biometricTypeRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(BiometricType.class)))
				.thenReturn(biometricTypeList);
		BiometricTypeResponseDto biometricTypeResponseDto = biometricTypeService.getAllBiometricTypes();
		assertEquals(biometricTypeList.get(0).getCode(), biometricTypeResponseDto.getBiometrictypes().get(0).getCode());
		assertEquals(biometricTypeList.get(0).getName(), biometricTypeResponseDto.getBiometrictypes().get(0).getName());
	}

	@Test
	public void getAllBioTypesByLanguageCodeSuccess() {
		Mockito.when(biometricTypeRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(biometricTypeList);
		BiometricTypeResponseDto biometricTypeResponseDto = biometricTypeService
				.getAllBiometricTypesByLanguageCode(Mockito.anyString());
		assertEquals(biometricTypeList.get(0).getCode(), biometricTypeResponseDto.getBiometrictypes().get(0).getCode());
		assertEquals(biometricTypeList.get(0).getName(), biometricTypeResponseDto.getBiometrictypes().get(0).getName());
	}

	@Test
	public void getBioTypeByCodeAndLangCodeSuccess() {
		Mockito.when(biometricTypeRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(biometricType1);
		BiometricTypeResponseDto biometricTypeResponseDto = biometricTypeService
				.getBiometricTypeByCodeAndLangCode(Mockito.anyString(), Mockito.anyString());
		assertEquals(biometricType1.getCode(), biometricTypeResponseDto.getBiometrictypes().get(0).getCode());
		assertEquals(biometricType1.getName(), biometricTypeResponseDto.getBiometrictypes().get(0).getName());
	}

	// ------------------ BlocklistedServiceTest -----------------//

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlocklistedWordsNullvalue() {
		blocklistedWordsService.getAllBlocklistedWordsBylangCode(null);
	}

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlocklistedWordsEmptyvalue() {
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("");
	}

	@Test
	public void testGetAllBlockListedWordsSuccess() {
		int expected = 1;
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenReturn(words);
		BlocklistedWordsResponseDto actual = blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
		assertEquals(actual.getBlocklistedwords().size(), expected);
	}
	
	@Test(expected = MasterDataServiceException.class)
	public void testGetAllBlockListedWordsFetchException() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenThrow(DataRetrievalFailureException.class);
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlockListedWordsNoDataFound() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenReturn(null);
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlockListedWordsEmptyData() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenReturn(new ArrayList<>());
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlockListedWordsDataNotFoundException() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenReturn(null);
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}

	@Test(expected = DataNotFoundException.class)
	public void testGetAllBlockListedWordsEmptyDataException() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenReturn(new ArrayList<>());
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}

	@Test(expected = MasterDataServiceException.class)
	public void testGetAllBlockListedWordsServiceException() {
		when(wordsRepository.findAllByLangCode(Mockito.anyString())).thenThrow(DataRetrievalFailureException.class);
		blocklistedWordsService.getAllBlocklistedWordsBylangCode("ENG");
	}
	
	@Test
	public void updateBlockListedWordStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for BlocklistedWords");

		when(wordsRepository.findtoUpdateBlocklistedWordByWord(Mockito.anyString())).thenReturn(words);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(BlocklistedWords.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = blocklistedWordsService.updateBlockListedWordStatus("abc", false);
		Assert.assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateBlockListedWordStatusFailureTest() {
		when(wordsRepository.findtoUpdateBlocklistedWordByWord(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		blocklistedWordsService.updateBlockListedWordStatus("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateBlockListedWordStatusFailureDataNotFoundTest() {
		when(wordsRepository.findtoUpdateBlocklistedWordByWord(Mockito.anyString())).thenReturn(null);
		blocklistedWordsService.updateBlockListedWordStatus("abc", false);
	}

	// ------------------ DeviceSpecificationServiceTest -----------------//

	@Test
	public void findDeviceSpecificationByLangugeCodeTest() {
		String languageCode = "ENG";
		Mockito.when(deviceSpecificationRepository.findAllDeviceSpecByIsActiveAndIsDeletedIsNullOrFalse())
				.thenReturn(deviceSpecifications);

		List<DeviceSpecificationDto> deviceSpecificationDtos = deviceSpecificationService
				.findDeviceSpecificationByLangugeCode(languageCode);
		Assert.assertEquals(deviceSpecificationDtos.get(0).getId(), deviceSpecifications.get(0).getId());
		Assert.assertEquals(deviceSpecificationDtos.get(0).getName(), deviceSpecifications.get(0).getName());

	}

	@Test(expected = DataNotFoundException.class)
	public void noDeviceSpecRecordsFoudExceptionTest() {
		List<DeviceSpecification> empityList = new ArrayList<DeviceSpecification>();
		String languageCode = "FRN";
		Mockito.when(deviceSpecificationRepository.findByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(languageCode))
				.thenReturn(empityList);
		deviceSpecificationService.findDeviceSpecificationByLangugeCode(languageCode);
	}

	@Test(expected = DataNotFoundException.class)
	public void noDeviceSpecRecordsFoudExceptionForNullTest() {
		String languageCode = "FRN";
		Mockito.when(deviceSpecificationRepository.findByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(languageCode))
				.thenReturn(null);
		deviceSpecificationService.findDeviceSpecificationByLangugeCode(languageCode);

	}

	@Test(expected = MasterDataServiceException.class)
	public void dataDeviceSpecAccessExceptionInGetAllTest() {
		String languageCode = "eng";
		Mockito.when(deviceSpecificationRepository.findAllDeviceSpecByIsActiveAndIsDeletedIsNullOrFalse())
				.thenThrow(DataAccessResourceFailureException.class);
		deviceSpecificationService.findDeviceSpecificationByLangugeCode(languageCode);
	}

	@Test
	public void findDeviceSpecificationByLangugeCodeAndDeviceTypeCodeTest() {
		String languageCode = "ENG";
		String deviceTypeCode = "operating_sys";
		Mockito.when(
				deviceSpecificationRepository.findByDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(deviceTypeCode))
				.thenReturn(deviceSpecificationListWithDeviceTypeCode);

		List<DeviceSpecificationDto> deviceSpecificationDtos = deviceSpecificationService
				.findDeviceSpecByLangCodeAndDevTypeCode(languageCode, deviceTypeCode);
		Assert.assertEquals(deviceSpecificationDtos.get(0).getId(),
				deviceSpecificationListWithDeviceTypeCode.get(0).getId());
		Assert.assertEquals(deviceSpecificationDtos.get(0).getName(),
				deviceSpecificationListWithDeviceTypeCode.get(0).getName());
		Assert.assertEquals(deviceSpecificationDtos.get(0).getDeviceTypeCode(),
				deviceSpecificationListWithDeviceTypeCode.get(0).getDeviceTypeCode());
	}

	@Test(expected = DataNotFoundException.class)
	public void noRecordsFoudExceptionInDeviceSpecificationByDevicTypeCodeTest() {
		List<DeviceSpecification> empityList = new ArrayList<DeviceSpecification>();
		String languageCode = "FRN";
		String deviceTypeCode = "operating_sys";
		Mockito.when(deviceSpecificationRepository
				.findByLangCodeAndDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(deviceTypeCode, deviceTypeCode))
				.thenReturn(empityList);
		deviceSpecificationService.findDeviceSpecByLangCodeAndDevTypeCode(languageCode, deviceTypeCode);
	}

	@Test(expected = DataNotFoundException.class)
	public void noRecordsFoudExceptionnDeviceSpecificationByDevicTypeCodeForNullTest() {
		String languageCode = "FRN";
		String deviceTypeCode = "operating_sys";
		Mockito.when(deviceSpecificationRepository
				.findByLangCodeAndDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(deviceTypeCode, deviceTypeCode))
				.thenReturn(null);
		deviceSpecificationService.findDeviceSpecByLangCodeAndDevTypeCode(languageCode, deviceTypeCode);

	}

	@Test(expected = MasterDataServiceException.class)
	public void dataAccessExceptionnDeviceSpecificationByDevicTypeCodeTest() {
		String languageCode = "ENG";
		String deviceTypeCode = "operating_sys";
		Mockito.when(
				deviceSpecificationRepository.findByDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(deviceTypeCode))
				.thenThrow(DataAccessResourceFailureException.class);
		deviceSpecificationService.findDeviceSpecByLangCodeAndDevTypeCode(languageCode, deviceTypeCode);
	}
	
	@Test
	public void updateDeviceSpecificationStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Device Specifications");

		when(deviceSpecificationRepository.findtoUpdateDeviceSpecById(Mockito.anyString()))
				.thenReturn(deviceSpecifications);
		when(deviceRepository.findDeviceByDeviceSpecIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(null);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(DeviceSpecification.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = deviceSpecificationService.updateDeviceSpecification("abc", false);
		Assert.assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateDeviceSpecificationStatusFailureTest() {
		when(deviceSpecificationRepository.findtoUpdateDeviceSpecById(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		deviceSpecificationService.updateDeviceSpecification("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateDeviceSpecificationStatusFailureDataNotFoundTest() {
		when(deviceSpecificationRepository.findtoUpdateDeviceSpecById(Mockito.anyString())).thenReturn(null);
		deviceSpecificationService.updateDeviceSpecification("abc", false);
	}

	// ------------------ DocumentCategoryServiceTest -----------------//

	@Test
	public void getAllDocumentCategorySuccessTest() {
		Mockito.when(
				documentCategoryRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(DocumentCategory.class)))
				.thenReturn(documentCategoryList);
		DocumentCategoryResponseDto documentCategoryResponseDto = documentCategoryService.getAllDocumentCategory();
		assertEquals(documentCategoryList.get(0).getName(),
				documentCategoryResponseDto.getDocumentcategories().get(0).getName());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllDocumentCategoryFetchException() {
		Mockito.when(
				documentCategoryRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(DocumentCategory.class)))
				.thenThrow(DataRetrievalFailureException.class);
		documentCategoryService.getAllDocumentCategory();
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllDocumentCategoryNotFoundException() {
		Mockito.when(documentCategoryRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(DocumentCategory.class))
				.thenReturn(new ArrayList<DocumentCategory>());
		documentCategoryService.getAllDocumentCategory();
	}

	@Test
	public void getAllDocumentCategoryByLaguageCodeSuccessTest() {
		Mockito.when(
				documentCategoryRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(documentCategoryList);
		DocumentCategoryResponseDto documentCategoryResponseDto = documentCategoryService
				.getAllDocumentCategoryByLaguageCode("ENG");
		assertEquals(documentCategoryList.get(0).getName(),
				documentCategoryResponseDto.getDocumentcategories().get(0).getName());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllDocumentCategoryByLaguageCodeFetchException() {
		Mockito.when(
				documentCategoryRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		documentCategoryService.getAllDocumentCategoryByLaguageCode(Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllDocumentCategoryByLaguageCodeNotFound() {
		Mockito.when(
				documentCategoryRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(new ArrayList<DocumentCategory>());
		documentCategoryService.getAllDocumentCategoryByLaguageCode(Mockito.anyString());
	}

	@Test
	public void getDocumentCategoryByCodeAndLangCodeSuccessTest() {
		Mockito.when(documentCategoryRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(documentCategory1);
		DocumentCategoryResponseDto documentCategoryResponseDto = documentCategoryService
				.getDocumentCategoryByCodeAndLangCode("123", "ENG");
		assertEquals(documentCategory1.getName(), documentCategoryResponseDto.getDocumentcategories().get(0).getName());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getDocumentCategoryByCodeAndLangCodeFetchException() {
		Mockito.when(documentCategoryRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		documentCategoryService.getDocumentCategoryByCodeAndLangCode(Mockito.anyString(), Mockito.anyString());
	}

	@Test(expected = DataNotFoundException.class)
	public void getDocumentCategoryByCodeAndLangCodeNotFoundException() {
		Mockito.when(documentCategoryRepository
				.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		documentCategoryService.getDocumentCategoryByCodeAndLangCode(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void addDocumentcategoryDataSuccess()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(documentCategoryRepository.create(Mockito.any())).thenReturn(documentCategory2);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(DocumentCategory.class), Mockito.any()))
				.thenReturn(documentCategoryRequestDto.getRequest());

		CodeAndLanguageCodeID codeAndLanguageCodeId = documentCategoryService
				.createDocumentCategory(documentCategoryRequestDto.getRequest());
		assertEquals(documentCategoryRequestDto.getRequest().getCode(), codeAndLanguageCodeId.getCode());
		assertEquals(documentCategoryRequestDto.getRequest().getLangCode(), codeAndLanguageCodeId.getLangCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void addDocumentcategoryDataFetchException()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(documentCategoryRepository.create(Mockito.any())).thenThrow(DataAccessLayerException.class);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(DocumentCategory.class), Mockito.any()))
				.thenReturn(documentCategoryRequestDto.getRequest());
		documentCategoryService.createDocumentCategory(documentCategoryRequestDto.getRequest());
	}
	
	@Test
	public void updateDocumentCategoryStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Document Categories");

		when(documentCategoryRepository.findtoUpdateDocumentCategoryByCode(Mockito.anyString()))
				.thenReturn(documentCategoryList);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(DocumentCategory.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = documentCategoryService.updateDocumentCategory("abc", true);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateDocumentCategoryStatusFailureTest() {
		when(documentCategoryRepository.findtoUpdateDocumentCategoryByCode(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		documentCategoryService.updateDocumentCategory("abc", false);
	}

	@Test(expected = RequestException.class)
	public void updateDocumentCategoryStatusFailureDataNotFoundTest() {
		when(documentCategoryRepository.findtoUpdateDocumentCategoryByCode(Mockito.anyString())).thenReturn(null);
		documentCategoryService.updateDocumentCategory("abc", false);
	}

	// ------------------ LanguageServiceTest -----------------//

	@Test
	public void testSucessGetAllLaguages() {
		Mockito.when(languageRepository.findAllByIsDeletedFalseOrIsDeletedIsNull()).thenReturn(languages);
		LanguageResponseDto dto = languageService.getAllLaguages();
		assertNotNull(dto);
		assertEquals(2, dto.getLanguages().size());
	}

	@Test(expected = DataNotFoundException.class)
	public void testLanguageNotFoundException() {
		Mockito.when(languageRepository.findAllByIsDeletedFalseOrIsDeletedIsNull()).thenReturn(null);
		languageService.getAllLaguages();
	}

	@Test(expected = DataNotFoundException.class)
	public void testLanguageNotFoundExceptionWhenNoLanguagePresent() {
		Mockito.when(languageRepository.findAllByIsDeletedFalseOrIsDeletedIsNull())
				.thenReturn(new ArrayList<Language>());
		languageService.getAllLaguages();
	}

	@Test(expected = MasterDataServiceException.class)
	public void testLanguageFetchException() {
		Mockito.when(languageRepository.findAllByIsDeletedFalseOrIsDeletedIsNull())
				.thenThrow(HibernateObjectRetrievalFailureException.class);
		languageService.getAllLaguages();
	}

	// ------------------ LocationServiceTest -----------------//

	@Test()
	public void getLocationHierarchyTest() {
		Mockito.when(locationHierarchyRepository.findDistinctLocationHierarchyByIsDeletedFalse(Mockito.anyString()))
				.thenReturn(locObjList);
		LocationHierarchyResponseDto locationHierarchyResponseDto = locationHierarchyService
				.getLocationDetails(Mockito.anyString());
		Assert.assertEquals("COUNTRY", locationHierarchyResponseDto.getLocations().get(0).getLocationHierarchyName());
	}

	@Test(expected = DataNotFoundException.class)
	public void getLocationHierarchyNoDataFoundExceptionTest() {
		Mockito.when(locationHierarchyRepository.findDistinctLocationHierarchyByIsDeletedFalse(Mockito.anyString()))
				.thenReturn(new ArrayList<Object[]>());
		locationHierarchyService.getLocationDetails(Mockito.anyString());

	}

	@Test(expected = MasterDataServiceException.class)
	public void getLocationHierarchyFetchExceptionTest() {
		Mockito.when(locationHierarchyRepository.findDistinctLocationHierarchyByIsDeletedFalse(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.getLocationDetails(Mockito.anyString());

	}

	@Test()
	public void getLocationHierachyBasedOnLangAndLoc() {
		Mockito.when(locationHierarchyRepository.findLocationHierarchyByCodeAndLanguageCode("IND", "HIN"))
				.thenReturn(locationHierarchies);

		LocationResponseDto locationHierarchyResponseDto = locationHierarchyService
				.getLocationHierarchyByLangCode("IND", "HIN");
		Assert.assertEquals("IND", locationHierarchyResponseDto.getLocations().get(0).getCode());

	}

	@Test(expected = DataNotFoundException.class)
	public void getLocationHierarchyExceptionTest() {
		Mockito.when(locationHierarchyRepository.findLocationHierarchyByCodeAndLanguageCode("IND", "HIN"))
				.thenReturn(null);
		locationHierarchyService.getLocationHierarchyByLangCode("IND", "HIN");

	}

	@Test(expected = DataNotFoundException.class)
	public void getLocationHierarchyExceptionTestWithEmptyList() {
		Mockito.when(locationHierarchyRepository.findLocationHierarchyByCodeAndLanguageCode("IND", "HIN"))
				.thenReturn(new ArrayList<Location>());
		locationHierarchyService.getLocationHierarchyByLangCode("IND", "HIN");

	}

	@Test(expected = MasterDataServiceException.class)
	public void locationHierarchyDataAccessExceptionTest() {
		Mockito.when(locationHierarchyRepository.findLocationHierarchyByCodeAndLanguageCode("IND", "HIN"))
				.thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.getLocationHierarchyByLangCode("IND", "HIN");
	}

	@Test()
	public void getLocationHierachyLevelTest() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime dateTime = LocalDateTime.parse("2020-03-23T07:39:19.342Z", formatter);
		LocalDateTime currentTimestamp = LocalDateTime.now();

		Mockito.when(locationHierarchyRepository1.findByLastUpdatedAndCurrentTimeStamp(dateTime, currentTimestamp))
				.thenReturn(locationHierarchyLevelList);
		LocationHierarchyLevelResponseDto locationHierarchyResponseDto = locationHierarchyLevelService
				.getLocationHierarchy(dateTime, currentTimestamp);
		Assert.assertEquals("Country",
				locationHierarchyResponseDto.getLocationHierarchyLevels().get(0).getHierarchyLevelName());

	}

	/**
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @Test public void locationHierarchySaveTest() {
	 *       Mockito.when(locationHierarchyRepository.create(Mockito.any())).thenReturn(locationHierarchy);
	 *       locationHierarchyService.createLocationHierarchy(requestLocationDto.getRequest());
	 *       }
	 * 
	 * @Test(expected = MasterDataServiceException.class) public void
	 *                locationHierarchySaveNegativeTest() {
	 *                Mockito.when(locationHierarchyRepository.create(Mockito.any())).thenThrow(DataAccessLayerException.class);
	 *                locationHierarchyService.createLocationHierarchy(requestLocationDto.getRequest());
	 *                }
	 * 
	 **/
	@Test(expected = MasterDataServiceException.class)
	public void updateLocationDetailsIsActiveTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		Mockito.when(locationHierarchyRepository.findById(Mockito.any(), Mockito.any())).thenReturn(locationHierarchy2);
		Mockito.when(locationHierarchyRepository
				.findLocationHierarchyByParentLocCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(locationHierarchyList);
		when(masterdataCreationUtil.updateMasterData(Mockito.eq(Location.class), Mockito.any()))
				.thenReturn(requestLocationPutDto1.getRequest());

		locationHierarchyService.updateLocationDetails(requestLocationPutDto1.getRequest());
	}


	@Test(expected = RequestException.class)
	public void updateLocationDetailsTest() {
		Mockito.when(locationHierarchyRepository.findById(Mockito.any(), Mockito.any())).thenReturn(locationHierarchy);
		Mockito.when(locationHierarchyRepository.update(Mockito.any())).thenReturn(locationHierarchy);

		locationHierarchyService.updateLocationDetails(requestLocationPutDto.getRequest());
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateLocationDetailsExceptionTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(locationHierarchyRepository.findById(Mockito.any(), Mockito.any())).thenReturn(locationHierarchy);
		Mockito.when(locationHierarchyRepository.update(Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		Mockito.when(locationHierarchyRepository.findByNameAndLevelLangCode(Mockito.any(), Mockito.anyShort(),
				Mockito.any())).thenReturn(null);
		Mockito.when(
				locationHierarchyRepository.findLocationHierarchyByCodeAndLanguageCode(Mockito.any(), Mockito.any()))
				.thenReturn(locationHierarchyList);
		when(masterdataCreationUtil.updateMasterData(Mockito.eq(Location.class), Mockito.any()))
				.thenReturn(requestLocationPutDto.getRequest());
		locationHierarchyService.updateLocationDetails(requestLocationPutDto.getRequest());
	}

	@Test(expected = RequestException.class)
	public void updateLocationDetailsDataNotFoundTest() {
		Mockito.when(locationHierarchyRepository.findById(Mockito.any(), Mockito.any())).thenReturn(null);
		locationHierarchyService.updateLocationDetails(requestLocationPutDto.getRequest());
	}

	@Test
	public void deleteLocationDetailsTest() {

		Mockito.when(locationHierarchyRepository.findByCode(Mockito.anyString())).thenReturn(locationHierarchies);
		Mockito.when(locationHierarchyRepository.update(Mockito.any())).thenReturn(locationHierarchy);
		locationHierarchyService.deleteLocationDetials("KAR");

	}

	@Test(expected = MasterDataServiceException.class)
	public void deleteLocationDetailsServiceExceptionTest() {

		Mockito.when(locationHierarchyRepository.findByCode(Mockito.anyString())).thenReturn(locationHierarchies);
		Mockito.when(locationHierarchyRepository.update(Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.deleteLocationDetials("KAR");

	}

	@Test(expected = RequestException.class)
	public void deleteLocationDetailDataNotFoundExceptionTest() {

		Mockito.when(locationHierarchyRepository.findByCode(Mockito.anyString())).thenReturn(new ArrayList<Location>());

		locationHierarchyService.deleteLocationDetials("KAR");

	}

	@Test()
	public void getLocationHierachyBasedOnHierarchyNameTest() {
		Mockito.when(locationHierarchyRepository.findAllByHierarchyNameIgnoreCase("country"))
				.thenReturn(locationHierarchies);

		LocationResponseDto locationResponseDto = locationHierarchyService.getLocationDataByHierarchyName("country");

		Assert.assertEquals("country", locationResponseDto.getLocations().get(0).getHierarchyName());

	}

	@Test(expected = DataNotFoundException.class)
	public void dataNotFoundExceptionTest() {

		Mockito.when(locationHierarchyRepository.findAllByHierarchyNameIgnoreCase("123")).thenReturn(null);

		locationHierarchyService.getLocationDataByHierarchyName("country");

	}

	@Test(expected = MasterDataServiceException.class)
	public void masterDataServiceExceptionTest() {
		Mockito.when(locationHierarchyRepository.findAllByHierarchyNameIgnoreCase("country"))
				.thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.getLocationDataByHierarchyName("country");

	}

	@Test
	public void getImmediateChildrenTest() {
		Mockito.when(locationHierarchyRepository
				.findLocationHierarchyByParentLocCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(locationHierarchies);
		locationHierarchyService.getImmediateChildrenByLocCodeAndLangCode("KAR", "KAN");
	}

	@Test(expected = MasterDataServiceException.class)
	public void getImmediateChildrenServiceExceptionTest() {
		Mockito.when(locationHierarchyRepository
				.findLocationHierarchyByParentLocCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.getImmediateChildrenByLocCodeAndLangCode("KAR", "KAN");
	}

	@Test(expected = DataNotFoundException.class)
	public void getImmediateChildrenDataExceptionTest() {
		Mockito.when(locationHierarchyRepository
				.findLocationHierarchyByParentLocCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ArrayList<Location>());
		locationHierarchyService.getImmediateChildrenByLocCodeAndLangCode("KAR", "KAN");
	}

	// ------------------ TemplateServiceTest -----------------//

	@Test(expected = MasterDataServiceException.class)
	public void getAllTemplateFetchExceptionTest() {
		Mockito.when(templateRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(Template.class)))
				.thenThrow(DataRetrievalFailureException.class);

		templateService.getAllTemplate();
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllTemplateNotFoundExceptionTest() {
		templateList = new ArrayList<>();
		Mockito.when(templateRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Mockito.eq(Template.class)))
				.thenReturn(templateList);
		templateService.getAllTemplate();
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllTemplateByLanguageCodeFetchExceptionTest() {
		hin.setCode("hin");
		Mockito.when(languageRepository.findLanguageByCodeNameAndNativeName(Mockito.anyString()))
		.thenReturn(hin);
		Mockito.when(templateRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);

		templateService.getAllTemplateByLanguageCode("hin");
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllTemplateByLanguageCodeNotFoundExceptionTest() {
		templateList = new ArrayList<>();
		hin.setCode("hin");
		Mockito.when(languageRepository.findLanguageByCodeNameAndNativeName(Mockito.anyString()))
		.thenReturn(hin);
		Mockito.when(templateRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(templateList);

		templateService.getAllTemplateByLanguageCode("hin");
	}

	@Test(expected = MasterDataServiceException.class)
	public void getAllTemplateByLanguageCodeAndTemplateTypeCodeFetchExceptionTest() {
		Mockito.when(templateRepository.findAllByLangCodeAndTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(
				Mockito.anyString(), Mockito.anyString())).thenThrow(DataRetrievalFailureException.class);
		templateService.getAllTemplateByLanguageCodeAndTemplateTypeCode("HIN", "EMAIL");
	}

	@Test(expected = DataNotFoundException.class)
	public void getAllTemplateByLanguageCodeAndTemplateTypeCodeNotFoundExceptionTest() {
		templateList = new ArrayList<>();
		hin.setCode("hin");
		Mockito.when(languageRepository.findLanguageByCodeNameAndNativeName(Mockito.anyString()))
		.thenReturn(hin);
		Mockito.when(templateRepository.findAllByLangCodeAndTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(
				Mockito.anyString(), Mockito.anyString())).thenReturn(templateList);
		templateService.getAllTemplateByLanguageCodeAndTemplateTypeCode("HIN", "EMAIL");
	}

	@Test
	public void getAllTemplateTest() {
		Mockito.when(templateRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Template.class))
				.thenReturn(templateList);
		templateResponseDto = templateService.getAllTemplate();

		assertEquals(templateList.get(0).getId(), templateResponseDto.getTemplates().get(0).getId());
		assertEquals(templateList.get(0).getName(), templateResponseDto.getTemplates().get(0).getName());
	}

	@Test
	public void getAllTemplateByLanguageCodeTest() {
		hin.setCode("hin");
		Mockito.when(languageRepository.findLanguageByCodeNameAndNativeName(Mockito.anyString()))
		.thenReturn(hin);
		Mockito.when(templateRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(templateList);
		templateResponseDto = templateService.getAllTemplateByLanguageCode("hin");

		assertEquals(templateList.get(0).getId(), templateResponseDto.getTemplates().get(0).getId());
		assertEquals(templateList.get(0).getName(), templateResponseDto.getTemplates().get(0).getName());
	}

	@Test
	public void getAllTemplateByLanguageCodeAndTemplateTypeCodeTest() {
		hin.setCode("hin");
		Mockito.when(languageRepository.findLanguageByCodeNameAndNativeName(Mockito.anyString()))
		.thenReturn(hin);
		Mockito.when(templateRepository.findAllByLangCodeAndTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(
				Mockito.anyString(), Mockito.anyString())).thenReturn(templateList);
		templateResponseDto = templateService.getAllTemplateByLanguageCodeAndTemplateTypeCode("HIN","EMAIL");

		assertEquals(templateList.get(0).getId(), templateResponseDto.getTemplates().get(0).getId());
		assertEquals(templateList.get(0).getName(), templateResponseDto.getTemplates().get(0).getName());
	}
	
	@Test
	public void updateTemplateStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Templates");

		when(templateRepository.findtoUpdateTemplateById(Mockito.anyString())).thenReturn(templateList);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(Template.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = templateService.updateTemplates("abc", false);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateTemplateStatusFailureTest() {
		when(templateRepository.findtoUpdateTemplateById(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		templateService.updateTemplates("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateTemplateStatusFailureDataNotFoundTest() {
		when(templateRepository.findtoUpdateTemplateById(Mockito.anyString())).thenReturn(null);
		templateService.updateTemplates("abc", false);
	}

	// ------------------------------------TemplateFileFormatServiceTest---------------------------//
	@Test
	public void addTemplateFileFormatSuccess()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(templateFileFormatRepository.create(Mockito.any())).thenReturn(templateFileFormat);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(TemplateFileFormat.class), Mockito.any()))
				.thenReturn(templateFileFormatRequestDto.getRequest());

		CodeAndLanguageCodeID codeAndLanguageCodeId = templateFileFormatService
				.createTemplateFileFormat(templateFileFormatRequestDto.getRequest());
		assertEquals(templateFileFormat.getCode(), codeAndLanguageCodeId.getCode());
		assertEquals(templateFileFormat.getLangCode(), codeAndLanguageCodeId.getLangCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void addTemplateFileFormatInsertExceptionTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Mockito.when(templateFileFormatRepository.create(Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		when(masterdataCreationUtil.createMasterData(Mockito.eq(TemplateFileFormat.class), Mockito.any()))
				.thenReturn(templateFileFormatRequestDto.getRequest());
		templateFileFormatService.createTemplateFileFormat(templateFileFormatRequestDto.getRequest());
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateTemplateFileFormatDataAccessExceptionTest() {
		Mockito.when(templateFileFormatRepository.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
				Mockito.any())).thenReturn(templateFileFormat);
		Mockito.when(templateFileFormatRepository.update(Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		templateFileFormatService.updateTemplateFileFormat(templateFileFormatPutRequestDto.getRequest());
	}

	@Test(expected = MasterDataServiceException.class)
	public void deleteTemplateFileFormatDataAccessExceptionTest() {
		Mockito.when(templateRepository.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any()))
				.thenReturn(templateList);
		Mockito.when(templateFileFormatRepository.deleteTemplateFileFormat(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		templateFileFormatService.deleteTemplateFileFormat(templateFileFormatRequestDto.getRequest().getCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void deleteTemplateFileFormatDataAccessExceptionTest2() {
		Mockito.when(templateRepository.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		templateFileFormatService.deleteTemplateFileFormat(templateFileFormatRequestDto.getRequest().getCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void deleteTemplateFileFormatDataAccessExceptionTest3() {
		Mockito.when(templateRepository.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any()))
				.thenReturn(templateList);
		Mockito.when(templateFileFormatRepository.deleteTemplateFileFormat(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(0);
		templateFileFormatService.deleteTemplateFileFormat(templateFileFormatRequestDto.getRequest().getCode());
	}
	
	@Test
	public void updateFileFormatStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Template File Formats");
		Mockito.when(templateRepository.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any()))
				.thenReturn(null);
		when(templateFileFormatRepository.findtoUpdateTemplateFileFormatByCode(Mockito.anyString()))
				.thenReturn(templateFileFormats);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(Template.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = templateFileFormatService.updateTemplateFileFormat("abc", false);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateFileFormatStatusFailureTest() {
		when(templateFileFormatRepository.findtoUpdateTemplateFileFormatByCode(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		templateFileFormatService.updateTemplateFileFormat("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateFileFormatStatusFailureDataNotFoundTest() {
		when(templateFileFormatRepository.findtoUpdateTemplateFileFormatByCode(Mockito.anyString())).thenReturn(null);
		templateFileFormatService.updateTemplateFileFormat("abc", false);
	}

	// ----------------------------------DocumentTypeServiceTest-------------------------//

	@Test
	public void getAllValidDocumentTypeTest() {
		String documentCategoryCode = "iric";
		String langCode = "eng";

		Mockito.when(documentTypeRepository.findByCodeAndLangCodeAndIsDeletedFalse(documentCategoryCode, langCode))
				.thenReturn(documents);

		List<DocumentTypeDto> documentTypes = documentTypeService.getAllValidDocumentType(documentCategoryCode,
				langCode);
		Assert.assertEquals(documentTypes.get(0).getCode(), documents.get(0).getCode());
		Assert.assertEquals(documentTypes.get(0).getName(), documents.get(0).getName());

	}

	@Test(expected = DataNotFoundException.class)
	public void documentTypeNoRecordsFoudExceptionTest() {
		String documentCategoryCode = "poc";
		String langCode = "eng";
		List<DocumentType> entitydocuments = new ArrayList<DocumentType>();
		Mockito.when(documentTypeRepository.findByCodeAndLangCodeAndIsDeletedFalse(documentCategoryCode, langCode))
				.thenReturn(entitydocuments);
		documentTypeService.getAllValidDocumentType(documentCategoryCode, langCode);

	}

	@Test(expected = DataNotFoundException.class)
	public void documentTypeNoRecordsFoudExceptionForNullTest() {
		String documentCategoryCode = "poc";
		String langCode = "eng";
		Mockito.when(documentTypeRepository.findByCodeAndLangCodeAndIsDeletedFalse(documentCategoryCode, langCode))
				.thenReturn(null);
		documentTypeService.getAllValidDocumentType(documentCategoryCode, langCode);

	}

	@Test(expected = MasterDataServiceException.class)
	public void documentTypeDataAccessExceptionInGetAllTest() {
		String documentCategoryCode = "poc";
		String langCode = "eng";
		Mockito.when(documentTypeRepository.findByCodeAndLangCodeAndIsDeletedFalse(documentCategoryCode, langCode))
				.thenThrow(DataAccessResourceFailureException.class);
		documentTypeService.getAllValidDocumentType(documentCategoryCode, langCode);

	}

	@Test
	public void updateDocumentTypeSuccessTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		DocumentTypePutReqDto documentTypeDto = new DocumentTypePutReqDto();
		documentTypeDto.setCode("code");
		documentTypeDto.setLangCode("eng");

		DocumentType documentType = new DocumentType();
		documentType.setCode(documentTypeDto.getCode());
		documentType.setLangCode(documentTypeDto.getLangCode());

		Mockito.when(
				documentTypeRepository.findByCodeAndLangCode(documentTypeDto.getCode(), documentTypeDto.getLangCode()))
				.thenReturn(documentType);
		when(masterdataCreationUtil.updateMasterData(Mockito.eq(DocumentType.class), Mockito.any()))
				.thenReturn(documentTypeDto);

		documentTypeService.updateDocumentType(documentTypeDto);

	}
	
	@Test
	public void updateDocumentTypeStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Document Types");

		when(documentTypeRepository.findtoUpdateDocumentTypeByCode(Mockito.anyString())).thenReturn(documents);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(DocumentType.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = documentTypeService.updateDocumentType("abc", true);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateDocumentTypeStatusFailureTest() {
		when(documentTypeRepository.findtoUpdateDocumentTypeByCode(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		documentTypeService.updateDocumentType("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateDocumentTypeStatusFailureDataNotFoundTest() {
		when(documentTypeRepository.findtoUpdateDocumentTypeByCode(Mockito.anyString())).thenReturn(null);
		documentTypeService.updateDocumentType("abc", false);
	}

	/*---------------------- Blocklisted word validator----------------------*/

	@Test
	public void validateWordNegativeTest() {
		List<String> badWords = new ArrayList<>();
		badWords.add("not-allowed");
		doReturn(badWords).when(wordsRepository).getAllBlockListedWords();
		List<String> wordsList = new ArrayList<>();
		wordsList.add("not-allowed");
		boolean isValid = blocklistedWordsService.validateWord(wordsList);
		assertEquals("Invalid word", false, isValid);
	}

	@Test
	public void validateWordPositiveTest() {
		List<BlocklistedWords> badWords = new ArrayList<>();
		BlocklistedWords word = new BlocklistedWords();
		word.setWord("nun");
		badWords.add(word);
		doReturn(badWords).when(wordsRepository).findAllByIsDeletedFalseOrIsDeletedNull();
		List<String> wordsList = new ArrayList<>();
		wordsList.add("allowed");
		boolean isValid = blocklistedWordsService.validateWord(wordsList);
		assertEquals("Valid word", true, isValid);
	}

	@Test(expected = MasterDataServiceException.class)
	public void validateWordExceptionTest() {
		doThrow(DataRetrievalFailureException.class).when(wordsRepository).getAllBlockListedWords();
		List<String> wordsList = new ArrayList<>();
		wordsList.add("allowed");
		blocklistedWordsService.validateWord(wordsList);
	}

	// -------------------------------------MachineHistroyTest----------------------------
	@Test(expected = RequestException.class)
	public void getMachineHistroyIdLangEffDTimeParseDateException() {
		machineHistoryService.getMachineHistroyIdLangEffDTime("1000", "ENG", "2018-12-11T11:18:261.033Z");
	}

	// ----------------------------------
	@Test(expected = RequestException.class)
	public void getRegCentDevHistByregCentIdDevIdEffTimeinvalidDateFormateTest() {
		registrationCenterDeviceHistoryService.getRegCenterDeviceHisByregCenterIdDevIdEffDTime("RCI100", "DI001",
				"2018-12-11T11:18:261.033Z");
	}

	// -------------------------------------DeviceHistroyTest------------------------------------------
	@Test(expected = RequestException.class)
	public void getDeviceHistroyIdLangEffDTimeParseDateException() {
		deviceHistoryService.getDeviceHistroyIdLangEffDTime("1000", "ENG", "2018-12-11T11:18:261.033Z");
	}

	// ---------------------RegistrationCenterIntegrationTest-validatetimestamp----------------//


	@Test
	public void getStatusOfWorkingHoursTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenReturn(false);
		Mockito.when(registrationCenterRepository.findByIdAndLangCode(Mockito.any(), Mockito.anyString()))
				.thenReturn(registrationCenter);
		LocalTime startTime = LocalTime.of(10, 00, 000);
		LocalTime endTime = LocalTime.of(18, 00, 000);
		registrationCenter.setCenterStartTime(startTime);
		registrationCenter.setCenterEndTime(endTime);

		ResgistrationCenterStatusResponseDto resgistrationCenterStatusResponseDto = registrationCenterService
				.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-12T17:59:59.999Z");

		Assert.assertEquals(MasterDataConstant.VALID, resgistrationCenterStatusResponseDto.getStatus());

	}

	@Test(expected = DataNotFoundException.class)
	public void getStatusOfWorkingHoursServiceExceptionTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		Mockito.when(registrationCenterRepository.findById(Mockito.any(), Mockito.anyString()))
				.thenReturn(registrationCenter);

		registrationCenterService.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-12T17:59:59.999Z");

	}

	@Test(expected = DataNotFoundException.class)
	public void getStatusOfWorkingHoursDataNotFoundExceptionTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenReturn(false);
		Mockito.when(registrationCenterRepository.findById(Mockito.any(), Mockito.anyString())).thenReturn(null);

		registrationCenterService.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-12T17:59:59.999Z");

	}

	@Test(expected = DataNotFoundException.class)
	public void getStatusOfWorkingHoursDataNotFoundTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenReturn(false);
		Mockito.when(registrationCenterRepository.findById(Mockito.any(), Mockito.anyString()))
				.thenReturn(registrationCenter);

		registrationCenterService.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-12T17:59:59.999Z");

	}

	@Test
	public void getStatusOfWorkingHoursRejectedWorkingHourTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenReturn(false);
		Mockito.when(registrationCenterRepository.findByIdAndLangCode(Mockito.any(), Mockito.anyString()))
				.thenReturn(registrationCenter);
		LocalTime startTime = LocalTime.of(10, 00, 000);
		LocalTime endTime = LocalTime.of(15, 00, 000);
		registrationCenter.setCenterStartTime(startTime);
		registrationCenter.setCenterEndTime(endTime);

		ResgistrationCenterStatusResponseDto resgistrationCenterStatusResponseDto = registrationCenterService
				.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-12T17:59:59.999Z");

		Assert.assertEquals(MasterDataConstant.VALID, resgistrationCenterStatusResponseDto.getStatus());

	}

	@Test(expected = RequestException.class)
	public void invalidDateFormatTest() throws Exception {
		Mockito.when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenReturn(false);
		Mockito.when(registrationCenterRepository.findById(Mockito.any(), Mockito.anyString()))
				.thenReturn(registrationCenter);

		registrationCenterService.validateTimeStampWithRegistrationCenter("1", "eng", "2017-12-1217:59:59.999Z");

	}
	
	@Test
	public void updateRegistrationCenterAdminStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Registration Centers");
		List<Zone> zones = new ArrayList<>();
		Zone zone = new Zone();
		zone.setCode("test");
		zone.setLangCode("eng");
		zones.add(zone);

		when(registrationCenterRepository.findByRegCenterIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenReturn(registrationCenters);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(RegistrationCenter.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		when(zoneUtils.getSubZones(Mockito.anyString())).thenReturn(zones);
		StatusResponseDto actual = registrationCenterService.updateRegistrationCenter("abc", false);
		assertEquals(dto, actual);
	}

	@Test(expected = RequestException.class)
	public void updateRegistrationCenterAdminStatusFailureTestWithInvalidZone() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Registration Centers");
		List<Zone> zones = new ArrayList<>();
		Zone zone = new Zone();
		zone.setCode("test1");
		zone.setLangCode("eng");
		zones.add(zone);

		when(registrationCenterRepository.findByRegCenterIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenReturn(registrationCenters);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(RegistrationCenter.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		when(zoneUtils.getSubZones(Mockito.anyString())).thenReturn(zones);
		registrationCenterService.updateRegistrationCenter("abc", false);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateRegistrationCenterAdminStatusFailureTest() {
		when(registrationCenterRepository.findByRegCenterIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		registrationCenterService.updateRegistrationCenter("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateRegistrationCenterAdminStatusFailureDataNotFoundTest() {
		when(registrationCenterRepository.findByRegCenterIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenReturn(null);
		registrationCenterService.updateRegistrationCenter("abc", false);
	}
	
	@Test
	public void updateRegistrationCenterTypeStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Registration Center Types");

		when(registrationCenterTypeRepository.findtoUpdateRegistrationCenterTypeByCode(Mockito.anyString()))
				.thenReturn(registrationCenterTypes);
		when(registrationCenterRepository.findByCenterTypeCode(Mockito.anyString())).thenReturn(null);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(RegistrationCenterType.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = registrationCenterTypeService.updateRegistrationCenterType("abc", false);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateRegistrationCenterTypeStatusFailureTest() {
		when(registrationCenterTypeRepository.findtoUpdateRegistrationCenterTypeByCode(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		registrationCenterTypeService.updateRegistrationCenterType("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateRegistrationCenterTypeStatusFailureDataNotFoundTest() {
		when(registrationCenterTypeRepository.findtoUpdateRegistrationCenterTypeByCode(Mockito.anyString()))
				.thenReturn(null);
		registrationCenterTypeService.updateRegistrationCenterType("abc", false);
	}

	// -----------------------------WorkingDayControllerTest------------------------

	@Test
	public void getWorkingDaysService() {

		List<DayNameAndSeqListDto> nameSeqDtoList = new ArrayList<>();
		DayNameAndSeqListDto nameSeqDto = new DayNameAndSeqListDto();
		nameSeqDto.setDaySeq((short) 1);
		nameSeqDto.setName("Monday");
		nameSeqDtoList.add(nameSeqDto);

		List<WorkingDaysDto> weekdayList = new ArrayList<>();
		WorkingDaysResponseDto weekdays = new WorkingDaysResponseDto();
		WorkingDaysDto daysDto = new WorkingDaysDto();
		daysDto.setLanguageCode("eng");
		daysDto.setName("Monday");
		daysDto.setOrder((short) 1);
		weekdayList.add(daysDto);
		weekdays.setWorkingdays(weekdayList);
		RegistrationCenter registCent = new RegistrationCenter();
		registCent.setId("10001");
		registCent.setLangCode("eng");

		Mockito.when(regWorkingNonWorkingRepo
				.findByregistrationCenterIdAndlanguagecodeForWorkingDays(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(nameSeqDtoList);
		Mockito.when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(3L);
		assertEquals("Monday",
				regWorkingNonWorkingService.getWorkingDays("10001", "eng").getWorkingdays().get(0).getName());
	}

	@Test
	public void getWeekDaysServiceTest() {
		List<WeekDaysDto> workingDaysDtos = new ArrayList<>();
		WeekDaysDto workingDaysDto = new WeekDaysDto();
		workingDaysDto.setDayCode("101");
		workingDaysDto.setName("Monday");
		workingDaysDto.setLanguagecode("eng");
		workingDaysDtos.add(workingDaysDto);
		RegistrationCenter registCent = new RegistrationCenter();
		registCent.setId("10001");
		registCent.setLangCode("eng");
		Mockito.when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(3L);

		Mockito.when(regWorkingNonWorkingRepo.findByregistrationCenterIdAndlangCodeForWeekDays("10001", "eng"))
				.thenReturn(workingDaysDtos);
		assertEquals("Monday",
				regWorkingNonWorkingService.getWeekDaysList("10001", "eng").getWeekdays().get(0).getName());

	}

	@Test
	public void getWeekDaysServiceGlobalTest() {
		List<DaysOfWeek> globalDaysList = new ArrayList<>();
		DaysOfWeek daysOfWeek = new DaysOfWeek();
		daysOfWeek.setCode("101");
		daysOfWeek.setGlobalWorking(true);
		daysOfWeek.setDaySeq((short) 1);
		daysOfWeek.setLangCode("eng");
		daysOfWeek.setName("Monday");
		globalDaysList.add(daysOfWeek);
		RegistrationCenter registCent = new RegistrationCenter();
		registCent.setId("10001");
		registCent.setLangCode("eng");

		Mockito.when(regWorkingNonWorkingRepo.findByregistrationCenterIdAndlangCodeForWeekDays("10001", "eng"))
				.thenReturn(null);
		Mockito.when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(2L);
		Mockito.when(daysOfWeekRepo.findBylangCode(Mockito.anyString())).thenReturn(globalDaysList);

		assertEquals("Monday",
				regWorkingNonWorkingService.getWeekDaysList("10001", "eng").getWeekdays().get(0).getName());

	}

	@Test(expected = DataNotFoundException.class)
	public void getWeekDaysServiceGlobalFailTest() {

		RegistrationCenter registCent = new RegistrationCenter();
		registCent.setId("10001");
		registCent.setLangCode("eng");
		Mockito.when(regWorkingNonWorkingRepo.findByregistrationCenterIdAndlangCodeForWeekDays("10001", "eng"))
				.thenReturn(null);
		Mockito.when(registrationCenterRepository.findByIdAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(registCent);
		Mockito.when(daysOfWeekRepo.findByAllGlobalWorkingTrue(Mockito.anyString())).thenReturn(null);

		regWorkingNonWorkingService.getWeekDaysList("10001", "eng");
	}

	@Test(expected = DataNotFoundException.class)
	public void getWeekDaysServiceFailureTest() {
		List<WorkingDaysDto> workingDaysDtos = new ArrayList<>();
		WorkingDaysDto workingDaysDto = new WorkingDaysDto();

		workingDaysDtos.add(workingDaysDto);

		Mockito.when(regWorkingNonWorkingRepo.findByregistrationCenterIdAndlangCodeForWeekDays("10001", "eng"))
				.thenThrow(DataAccessLayerException.class);
		regWorkingNonWorkingService.getWeekDaysList("10001", "eng");
	}


	@Test(expected = MasterDataServiceException.class)
	public void getWeekDaysServiceFailureTest2() {
		List<WorkingDaysDto> workingDaysDtos = new ArrayList<>();
		WorkingDaysDto workingDaysDto = new WorkingDaysDto();

		workingDaysDtos.add(workingDaysDto);
		Mockito.when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull("10001"))
				.thenReturn(1L);
		Mockito.when(regWorkingNonWorkingRepo.findByregistrationCenterIdAndlangCodeForWeekDays("10001", "eng"))
				.thenThrow(DataAccessLayerException.class);
		regWorkingNonWorkingService.getWeekDaysList("10001", "eng");
	}

	@Test(expected = DataNotFoundException.class)
	public void getWorkingServiceFailureTest() {

		Mockito.when(regWorkingNonWorkingRepo
				.findByregistrationCenterIdAndlanguagecodeForWorkingDays(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		regWorkingNonWorkingService.getWorkingDays("10001", "eng");
	}

	@Test(expected = DataNotFoundException.class)
	public void getWorkingServiceFailureTest2() {

		Mockito.when(regWorkingNonWorkingRepo
				.findByregistrationCenterIdAndlanguagecodeForWorkingDays(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessLayerException("", "", new Throwable()));
		regWorkingNonWorkingService.getWorkingDays("10001", "eng");
	}

	@Test(expected = MasterDataServiceException.class)
	public void exceptionalHolidaysFailureTest2() {

		Mockito.when(exceptionalHolidayRepository.findAllNonDeletedExceptionalHoliday("10001", "eng"))
				.thenThrow(new DataAccessLayerException("", "", new Throwable()));
		exceptionalHolidayService.getAllExceptionalHolidays("10001", "eng");
	}
	
	@Test
	public void getWorkingDaysByLangCodeService() {

		List<DaysOfWeek> globalDaysList = new ArrayList<DaysOfWeek>();

		DaysOfWeek daysOfWeek = new DaysOfWeek();
		daysOfWeek.setCode("101");
		daysOfWeek.setGlobalWorking(true);
		daysOfWeek.setDaySeq((short) 1);
		daysOfWeek.setLangCode("eng");
		daysOfWeek.setName("Monday");
		globalDaysList.add(daysOfWeek);

		Mockito.when(daysOfWeekRepo.findByAllGlobalWorkingTrue(Mockito.anyString()))
				.thenReturn(globalDaysList);
		assertEquals("Monday",
				regWorkingNonWorkingService.getWorkingDays("eng").getWorkingdays().get(0).getName());
	}
	@Test
	public void updateWorkingDaysServiceTest() throws NoSuchFieldException, IllegalAccessException {
		DaysOfWeek daysOfWeek = new DaysOfWeek();
		WorkingDaysPutRequestDto workingDaysPutRequestDto=new WorkingDaysPutRequestDto();
		daysOfWeek.setCode("101");
		daysOfWeek.setGlobalWorking(true);
		daysOfWeek.setDaySeq((short) 1);
		daysOfWeek.setLangCode("eng");
		daysOfWeek.setName("Monday");
		workingDaysPutRequestDto= MapperUtils.map(daysOfWeek,workingDaysPutRequestDto);

		Mockito.when(daysOfWeekRepo.findBylangCodeAndCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(daysOfWeek);
		when(masterdataCreationUtil.updateMasterData(Mockito.eq(DaysOfWeek.class), Mockito.any()))
				.thenReturn(workingDaysPutRequestDto);
		assertEquals("Monday",
				regWorkingNonWorkingService.updateWorkingDays(workingDaysPutRequestDto).getName());
	}

	@Test
	public void updateWorkingDaysStatusServiceTest() throws NoSuchFieldException, IllegalAccessException {
		List<DaysOfWeek> globalDaysList = new ArrayList<DaysOfWeek>();
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for workingDays");
		DaysOfWeek daysOfWeek = new DaysOfWeek();
		daysOfWeek.setCode("101");
		daysOfWeek.setGlobalWorking(true);
		daysOfWeek.setDaySeq((short) 1);
		daysOfWeek.setLangCode("eng");
		daysOfWeek.setName("Monday");
		globalDaysList.add(daysOfWeek);
		Mockito.when(daysOfWeekRepo.findByCode(Mockito.anyString()))
				.thenReturn(globalDaysList);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(DaysOfWeek.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual=regWorkingNonWorkingService.updateWorkingDaysStatus("abc",false);
		assertEquals(dto,actual);
	}
	@Test(expected = MasterDataServiceException.class)
	public void getWorkingDaysServiceFailureTest() {
		List<WorkingDaysDto> workingDaysDtos = new ArrayList<>();
		WorkingDaysDto workingDaysDto = new WorkingDaysDto();

		workingDaysDtos.add(workingDaysDto);

		Mockito.when(daysOfWeekRepo.findByAllGlobalWorkingTrue(Mockito.anyString()))
				.thenThrow(DataAccessLayerException.class);
		regWorkingNonWorkingService.getWorkingDays("eng");
	}

	@Test(expected = DataNotFoundException.class)
	public void getWorkingServiceFailureTest1() {

		Mockito.when(daysOfWeekRepo.findByAllGlobalWorkingTrue(Mockito.anyString()))
				.thenReturn(null);
		regWorkingNonWorkingService.getWorkingDays("eng");
	}
	
	@Test()
	public void zoneFilterValuesTest() {

		FilterValueDto filterValueDto = new FilterValueDto();
		List<FilterDto> filters = new ArrayList<FilterDto>();
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("SAFi");
		filterDto.setType("unique");
		filters.add(filterDto);
		filterValueDto.setFilters(filters);
		filterValueDto.setLanguageCode("eng");

		FilterResponseCodeDto filterResponseCodeDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> ResponseFilters = new ArrayList<ColumnCodeValue>();
		ColumnCodeValue codeValue = new ColumnCodeValue();
		codeValue.setFieldCode("MRS");
		codeValue.setFieldID("name");
		codeValue.setFieldValue("Marrakesh-Safi");
		ResponseFilters.add(codeValue);
		filterResponseCodeDto.setFilters(ResponseFilters);

		List<FilterData> filterValues = new ArrayList<FilterData>();
		FilterData filterData = new FilterData("MRS", "Marrakesh-Safi");
		filterValues.add(filterData);

		Mockito.when(filterColumnValidator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(masterDataFilterHelper.filterValuesWithCode(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(new FilterResult(filterValues, 0));
		FilterResponseCodeDto response = zoneService.zoneFilterValues(filterValueDto);
		Assert.assertEquals(filterResponseCodeDto, response);
	}

	@Test(expected = MasterDataServiceException.class)
	public void zoneFilterValuesFailureTest() {

		FilterValueDto filterValueDto = new FilterValueDto();
		List<FilterDto> filters = new ArrayList<FilterDto>();
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("code");
		filterDto.setText("SAFi");
		filterDto.setType("unique");
		filters.add(filterDto);
		filterValueDto.setFilters(filters);
		filterValueDto.setLanguageCode("eng");

		Mockito.when(filterColumnValidator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(masterDataFilterHelper.filterValuesWithCode(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenThrow(MasterDataServiceException.class);
		zoneService.zoneFilterValues(filterValueDto);
	}
	
	@Test
	public void updateDeviceStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Devices");
		DeviceHistory createdHistory = new DeviceHistory();
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenReturn(devices);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(Device.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		when(deviceHistoryRepository.create(Mockito.any())).thenReturn(createdHistory);
		StatusResponseDto actual = deviceService.updateDeviceStatus("abc", false);
		Assert.assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateDeviceStatusFailureTest() {
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenThrow(MasterDataServiceException.class);
		deviceService.updateDeviceStatus("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateDeviceStatusFailureDataNotFoundTest() {
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenReturn(null);
		deviceService.updateDeviceStatus("abc", false);
	}

	@Test
	public void updateDeviceTypeStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Device Types");

		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString())).thenReturn(deviceTypes);
		when(deviceSpecificationRepository.findByDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn(null);
		when(masterdataCreationUtil.updateMasterDataStatus(Mockito.eq(DeviceType.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = deviceTypeService.updateDeviceType("abc", false);
		Assert.assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	public void updateDeviceTypeStatusFailureTest() {
		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString()))
				.thenThrow(MasterDataServiceException.class);
		deviceTypeService.updateDeviceType("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	public void updateDeviceTypeStatusFailureDataNotFoundTest() {
		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString())).thenReturn(null);
		deviceTypeService.updateDeviceType("abc", false);
	}
	
	@Test
	@WithUserDetails("reg-officer")
	public void updateAllDynamicFieldStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Dynamic Fields");

		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = dynamicFieldService.updateDynamicFieldStatus("abc", false);
		assertEquals(dto, actual);
	}

	@Test
	@WithUserDetails("reg-officer")
	public void updateDynamicFieldStatusSuccessTest() {
		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Dynamic Fields");
		DynamicField dynamicField = new DynamicField();
		dynamicField.setId("10001");
		dynamicField.setName("bloodType1");
		dynamicField.setValueJson("{\"code\":\"code\",\"value\":\"value\"}");
		when(dynamicFieldRepository.findDynamicFieldById(Mockito.anyString())).thenReturn(dynamicField);
		when(dynamicFieldRepository.updateDynamicFieldIsActive(Mockito.anyString(),Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenReturn(1);
		StatusResponseDto actual = dynamicFieldService.updateDynamicFieldValueStatus("10001", false);
		assertEquals(dto, actual);
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("reg-officer")
	public void updateAllDynamicFieldStatusFailureTest() {
		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenThrow(MasterDataServiceException.class);
		dynamicFieldService.updateDynamicFieldStatus("abc", false);
	}

	@Test(expected = DataNotFoundException.class)
	@WithUserDetails("reg-officer")
	public void updateAllDynamicFieldStatusFailureDataNotFoundTest() {
		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenReturn(0);
		dynamicFieldService.updateDynamicFieldStatus("abc", false);
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("reg-officer")
	public void updateDynamicFieldStatusFailureTest() {
		when(dynamicFieldRepository.updateDynamicFieldIsActive(Mockito.anyString(),Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenThrow(MasterDataServiceException.class);
		DynamicField dynamicField = new DynamicField();
		dynamicField.setId("10001");
		dynamicField.setName("bloodType1");
		dynamicField.setValueJson("{\"code\":\"code\",\"value\":\"value\"}");
		when(dynamicFieldRepository.findDynamicFieldById(Mockito.anyString())).thenReturn(dynamicField);

		dynamicFieldService.updateDynamicFieldValueStatus("10001", false);
	}

	@Test(expected = DataNotFoundException.class)
	@WithUserDetails("reg-officer")
	public void updateDynamicFieldStatusFailureDataNotFoundTest() {
		when(dynamicFieldRepository.updateDynamicFieldIsActive(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenReturn(0);
		dynamicFieldService.updateDynamicFieldValueStatus("abc", false);
	}

	@Test
	public void validateRegCenterCreateTest() {
		List<ServiceError> serviceErrors = new ArrayList<>();
		RegCenterPostReqDto registrationCenterDto = new RegCenterPostReqDto();
		registrationCenterDto.setName("TEST CENTER");
		registrationCenterDto.setAddressLine1("Address Line 1");
		registrationCenterDto.setAddressLine2("Address Line 2");
		registrationCenterDto.setAddressLine3("Address Line 3");
		registrationCenterDto.setCenterTypeCode("REG");
		registrationCenterDto.setContactPerson("Test");
		registrationCenterDto.setContactPhone("9999999999");
		registrationCenterDto.setHolidayLocationCode("HLC01");
		registrationCenterDto.setLangCode("eng");
		registrationCenterDto.setLatitude("12.9646818");
		registrationCenterDto.setLocationCode("10190");
		registrationCenterDto.setLongitude("77.70168");
		registrationCenterDto.setPerKioskProcessTime(LocalTime.of(0,15,0));
		registrationCenterDto.setCenterStartTime(LocalTime.of(7,0));
		registrationCenterDto.setCenterEndTime(LocalTime.of(17,0));
		registrationCenterDto.setLunchStartTime(LocalTime.of(12,0));
		registrationCenterDto.setLunchEndTime(LocalTime.of(13,0));
		registrationCenterDto.setTimeZone("UTC");
		registrationCenterDto.setWorkingHours("9");
		registrationCenterDto.setZoneCode("JRD");
		registrationCenterValidator.validateRegCenterCreate(registrationCenterDto, serviceErrors);
		Assert.assertEquals(1, serviceErrors.size());
		Assert.assertEquals("ADM-MSD-446", serviceErrors.get(0).getErrorCode());
	}

	@Test
	@WithUserDetails("reg-officer")
	public void getSubZoneIdsForUserTest() {
		when(zoneUtils.getSubZones(Mockito.anyString())).thenReturn(zones);
		List<String> subZoneIds = registrationCenterValidator.getSubZoneIdsForUser("eng");
		Assert.assertTrue(subZoneIds != null && !subZoneIds.isEmpty());
	}

	@Test
	public void validateRegCenterUpdateTest() {
		List<ServiceError> serviceErrors = new ArrayList<>();
		registrationCenterValidator.validateRegCenterUpdate("JRD",
				LocalTime.of(17,0), LocalTime.of(7,0),
				LocalTime.of(12,30), LocalTime.of(12,0),
				"12.9646818", "77.70168", null, "eng",
				"RBT", serviceErrors);
		Assert.assertEquals(5, serviceErrors.size());
		Assert.assertEquals("ADM-MSD-446", serviceErrors.get(0).getErrorCode());
		Assert.assertEquals("KER-MSD-309", serviceErrors.get(1).getErrorCode());
		Assert.assertEquals("KER-MSD-308", serviceErrors.get(2).getErrorCode());
		Assert.assertEquals("KER-MSD-260", serviceErrors.get(3).getErrorCode());
		Assert.assertEquals("KER-MSD-259", serviceErrors.get(4).getErrorCode());
	}

	@Test
	public void getImmediateChildrenByLocCodeTest() {
		Mockito.when(locationHierarchyRepository
						.findLocationHierarchyByParentLocCode(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(locationHierarchies);
		Assert.assertEquals("IND", locationHierarchyService.getImmediateChildrenByLocCode("KAR", List.of("eng")).getLocations().get(0).getCode());
	}

	@Test(expected = MasterDataServiceException.class)
	public void getImmediateChildrenByLocCodeTestExceptionTest() {
		Mockito.when(locationHierarchyRepository
						.findLocationHierarchyByParentLocCode(Mockito.anyString(), Mockito.anyList()))
				.thenThrow(DataRetrievalFailureException.class);
		locationHierarchyService.getImmediateChildrenByLocCode("KAR", List.of("eng"));
	}

	@Test(expected = DataNotFoundException.class)
	public void getImmediateChildrenByLocCodeTestDataExceptionTest() {
		Mockito.when(locationHierarchyRepository
						.findLocationHierarchyByParentLocCode(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(new ArrayList<Location>());
		locationHierarchyService.getImmediateChildrenByLocCode("KAR", List.of("eng"));
	}

	@Test
	@WithUserDetails("reg-officer")
	public void testFetchAllDynamicFieldsAllLang() throws Exception {
		Mockito.when(dynamicFieldRepository.findAllDynamicFieldByName(Mockito.anyString())).thenReturn(dynamicFields);
		assertEquals(1, dynamicFieldService.getAllDynamicFieldByName("gender").size());
	}

}
