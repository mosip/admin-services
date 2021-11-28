package io.mosip.kernel.syncdata.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.mosip.kernel.core.signatureutil.model.SignatureResponse;
import io.mosip.kernel.core.signatureutil.spi.SignatureUtil;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.entity.AppAuthenticationMethod;
import io.mosip.kernel.syncdata.entity.AppDetail;
import io.mosip.kernel.syncdata.entity.AppRolePriority;
import io.mosip.kernel.syncdata.entity.ApplicantValidDocument;
import io.mosip.kernel.syncdata.entity.Application;
import io.mosip.kernel.syncdata.entity.BiometricAttribute;
import io.mosip.kernel.syncdata.entity.BiometricType;
import io.mosip.kernel.syncdata.entity.BlocklistedWords;
import io.mosip.kernel.syncdata.entity.Device;
import io.mosip.kernel.syncdata.entity.DeviceHistory;
import io.mosip.kernel.syncdata.entity.DeviceProvider;
import io.mosip.kernel.syncdata.entity.DeviceService;
import io.mosip.kernel.syncdata.entity.DeviceSpecification;
import io.mosip.kernel.syncdata.entity.DeviceSubTypeDPM;
import io.mosip.kernel.syncdata.entity.DeviceType;
import io.mosip.kernel.syncdata.entity.DeviceTypeDPM;
import io.mosip.kernel.syncdata.entity.DocumentCategory;
import io.mosip.kernel.syncdata.entity.DocumentType;
import io.mosip.kernel.syncdata.entity.FoundationalTrustProvider;
import io.mosip.kernel.syncdata.entity.Holiday;
import io.mosip.kernel.syncdata.entity.IdType;
import io.mosip.kernel.syncdata.entity.Language;
import io.mosip.kernel.syncdata.entity.Location;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.entity.MachineHistory;
import io.mosip.kernel.syncdata.entity.MachineSpecification;
import io.mosip.kernel.syncdata.entity.MachineType;
import io.mosip.kernel.syncdata.entity.ProcessList;
import io.mosip.kernel.syncdata.entity.ReasonCategory;
import io.mosip.kernel.syncdata.entity.ReasonList;
import io.mosip.kernel.syncdata.entity.RegisteredDevice;
import io.mosip.kernel.syncdata.entity.RegistrationCenter;

import io.mosip.kernel.syncdata.entity.RegistrationCenterType;

import io.mosip.kernel.syncdata.entity.ScreenAuthorization;
import io.mosip.kernel.syncdata.entity.ScreenDetail;
import io.mosip.kernel.syncdata.entity.Template;
import io.mosip.kernel.syncdata.entity.TemplateFileFormat;
import io.mosip.kernel.syncdata.entity.TemplateType;
import io.mosip.kernel.syncdata.entity.Title;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.entity.UserDetailsHistory;
import io.mosip.kernel.syncdata.entity.ValidDocument;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.repository.AppAuthenticationMethodRepository;
import io.mosip.kernel.syncdata.repository.AppDetailRepository;
import io.mosip.kernel.syncdata.repository.AppRolePriorityRepository;
import io.mosip.kernel.syncdata.repository.ApplicantValidDocumentRespository;
import io.mosip.kernel.syncdata.repository.ApplicationRepository;
import io.mosip.kernel.syncdata.repository.BiometricAttributeRepository;
import io.mosip.kernel.syncdata.repository.BiometricTypeRepository;
import io.mosip.kernel.syncdata.repository.BlocklistedWordsRepository;
import io.mosip.kernel.syncdata.repository.DeviceHistoryRepository;
import io.mosip.kernel.syncdata.repository.DeviceProviderRepository;
import io.mosip.kernel.syncdata.repository.DeviceRepository;
import io.mosip.kernel.syncdata.repository.DeviceServiceRepository;
import io.mosip.kernel.syncdata.repository.DeviceSpecificationRepository;
import io.mosip.kernel.syncdata.repository.DeviceSubTypeDPMRepository;
import io.mosip.kernel.syncdata.repository.DeviceTypeDPMRepository;
import io.mosip.kernel.syncdata.repository.DeviceTypeRepository;
import io.mosip.kernel.syncdata.repository.DocumentCategoryRepository;
import io.mosip.kernel.syncdata.repository.DocumentTypeRepository;
import io.mosip.kernel.syncdata.repository.FoundationalTrustProviderRepository;
import io.mosip.kernel.syncdata.repository.HolidayRepository;
import io.mosip.kernel.syncdata.repository.IdTypeRepository;
import io.mosip.kernel.syncdata.repository.LanguageRepository;
import io.mosip.kernel.syncdata.repository.LocationRepository;
import io.mosip.kernel.syncdata.repository.MachineHistoryRepository;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.MachineSpecificationRepository;
import io.mosip.kernel.syncdata.repository.MachineTypeRepository;
import io.mosip.kernel.syncdata.repository.ProcessListRepository;
import io.mosip.kernel.syncdata.repository.ReasonCategoryRepository;
import io.mosip.kernel.syncdata.repository.ReasonListRepository;
import io.mosip.kernel.syncdata.repository.RegisteredDeviceRepository;

import io.mosip.kernel.syncdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.syncdata.repository.RegistrationCenterTypeRepository;

import io.mosip.kernel.syncdata.repository.ScreenAuthorizationRepository;
import io.mosip.kernel.syncdata.repository.ScreenDetailRepository;
import io.mosip.kernel.syncdata.repository.TemplateFileFormatRepository;
import io.mosip.kernel.syncdata.repository.TemplateRepository;
import io.mosip.kernel.syncdata.repository.TemplateTypeRepository;
import io.mosip.kernel.syncdata.repository.TitleRepository;
import io.mosip.kernel.syncdata.repository.UserDetailsHistoryRepository;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.repository.ValidDocumentRepository;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.utils.MapperUtils;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SyncClientSettingsIntegrationTest {
		
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MapperUtils mapper;	
	
	@Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
	private String dynamicfieldUrl;
	
	private Machine machine;
	private List<Application> applications;
	private List<Machine> machines;
	private List<MachineSpecification> machineSpecification;
	private List<MachineType> machineType;
	private List<RegistrationCenter> registrationCenters;
	private List<RegistrationCenterType> registrationCenterType;
	private List<Device> devices;
	private List<DeviceSpecification> deviceSpecification;
	private List<DeviceType> deviceType;
	private List<Holiday> holidays;
	private List<BlocklistedWords> blackListedWords;
	private List<Title> titles;
	private List<Language> languages;
	private List<Template> templates;
	private List<TemplateFileFormat> templateFileFormats;
	private List<TemplateType> templateTypes;
	private List<BiometricAttribute> biometricAttributes;
	private List<BiometricType> biometricTypes;
	private List<DocumentCategory> documentCategories;
	private List<DocumentType> documentTypes;
	private List<ValidDocument> validDocuments;
	private List<ReasonCategory> reasonCategories;
	private List<ReasonList> reasonLists;
	private List<IdType> idTypes;
	private List<Location> locations;
	
	private List<ApplicantValidDocument> applicantValidDocumentList;
	private List<Object[]> objectArrayList;
	private List<AppAuthenticationMethod> appAuthenticationMethods = null;
	private List<AppDetail> appDetails = null;
	private List<AppRolePriority> appRolePriorities = null;
	private List<ScreenAuthorization> screenAuthorizations = null;
	private List<ProcessList> processList = null;
	private List<ScreenDetail> screenDetailList = null;
	private DeviceService deviceService;
	private DeviceProvider deviceProvider;
	private RegisteredDevice registeredDevice;
	private DeviceTypeDPM deviceTypeDPM;
	private DeviceSubTypeDPM deviceSubTypeDPM;
	private FoundationalTrustProvider foundationalTrustProvider;
	
	@MockBean
	private ApplicationRepository applicationRepository;
	@MockBean
	private MachineRepository machineRepository;
	@MockBean
	private DeviceHistoryRepository deviceHistoryRepository;
	@MockBean
	private UserDetailsRepository userDetailsRepository;
	@MockBean
	private UserDetailsHistoryRepository userDetailsHistoryRepository;
	
	@MockBean
	private MachineTypeRepository machineTypeRepository;
	@MockBean
	private RegistrationCenterRepository registrationCenterRepository;
	@MockBean
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;
	@MockBean
	private TemplateRepository templateRepository;
	@MockBean
	private TemplateFileFormatRepository templateFileFormatRepository;
	@MockBean
	private ReasonCategoryRepository reasonCategoryRepository;
	@MockBean
	private HolidayRepository holidayRepository;
	@MockBean
	private BlocklistedWordsRepository blocklistedWordsRepository;
	@MockBean
	private BiometricTypeRepository biometricTypeRepository;
	@MockBean
	private BiometricAttributeRepository biometricAttributeRepository;
	@MockBean
	private TitleRepository titleRepository;
	@MockBean
	private LanguageRepository languageRepository;
	@MockBean
	private DeviceRepository deviceRepository;
	@MockBean
	private DocumentCategoryRepository documentCategoryRepository;
	@MockBean
	private DocumentTypeRepository documentTypeRepository;
	@MockBean
	private IdTypeRepository idTypeRepository;
	@MockBean
	private DeviceSpecificationRepository deviceSpecificationRepository;
	@MockBean
	private LocationRepository locationRepository;
	@MockBean
	private TemplateTypeRepository templateTypeRepository;
	@MockBean
	private MachineSpecificationRepository machineSpecificationRepository;
	@MockBean
	private DeviceTypeRepository deviceTypeRepository;
	@MockBean
	private ValidDocumentRepository validDocumentRepository;
	@MockBean
	private ReasonListRepository reasonListRepository;

	@MockBean
	private AppAuthenticationMethodRepository appAuthenticationMethodRepository;
	@MockBean
	private AppDetailRepository appDetailRepository;
	@MockBean
	private AppRolePriorityRepository appRolePriorityRepository;
	@MockBean
	private ScreenAuthorizationRepository screenAuthorizationRepository;
	@MockBean
	private ProcessListRepository processListRepository;

	@MockBean
	private MachineHistoryRepository machineHistoryRepository;

	@MockBean
	private ScreenDetailRepository screenDetailRepo;
	@MockBean
	private SignatureUtil signatureUtil;
	@MockBean
	private DeviceProviderRepository deviceProviderRepository;
	@MockBean
	private DeviceServiceRepository deviceServiceRepository;
	@MockBean
	private RegisteredDeviceRepository registeredDeviceRepository;
	@MockBean
	private FoundationalTrustProviderRepository foundationalTrustProviderRepository;
	@MockBean
	private DeviceTypeDPMRepository deviceTypeDPMRepository;
	@MockBean
	private DeviceSubTypeDPMRepository deviceSubTypeDPMRepository;	
	
	@MockBean
	private ApplicantValidDocumentRespository applicantValidDocumentRespository;

	private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
	private byte[] tpmPublicKey = CryptoUtil.decodeURLSafeBase64(encodedTPMPublicKey);
	private String keyIndex = CryptoUtil.computeFingerPrint(tpmPublicKey, null);
	
	private SignatureResponse signResponse;
		
	private MockRestServiceServer mockRestServer;

	private ArrayList<Machine> registrationCenterMachines;

	private ArrayList<Device> registrationCenterDevices;

	private ArrayList<UserDetails> registrationCenterUsers;

	private ArrayList<DeviceHistory> registrationCenterDeviceHistory;

	private ArrayList<MachineHistory> registrationCenterMachineHistory;

	private ArrayList<UserDetailsHistory> registrationCenterUserHistory;
				
	@Before
	public void setup() {
		
		LocalDateTime localdateTime = LocalDateTime.parse("2018-11-01T01:01:01");
		LocalTime localTime = LocalTime.parse("09:00:00");
		applications = new ArrayList<>();
		applications.add(new Application("101", "ENG", "MOSIP", "MOSIP"));
		machines = new ArrayList<>();
		machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001", "ENG", localdateTime,
				encodedTPMPublicKey, keyIndex, "ZONE", "10002", null, encodedTPMPublicKey, keyIndex);
		machines.add(machine);
		machineSpecification = new ArrayList<>();
		machineSpecification.add(
				new MachineSpecification("1001", "Laptop", "Lenovo", "T480", "1001", "1.0", "Laptop", "ENG", null));
		machineType = new ArrayList<>();
		machineType.add(new MachineType("1001", "ENG", "System", "System"));
		devices = new ArrayList<>();
		Device device = new Device();
		device.setId("1000");
		device.setName("Printer");
		device.setLangCode("eng");
		device.setIsActive(true);
		device.setMacAddress("127.0.0.0");
		device.setIpAddress("127.0.0.10");
		device.setSerialNum("234");
		device.setDeviceSpecId("234");
		device.setValidityDateTime(localdateTime);
		devices.add(device);

		deviceSpecification = new ArrayList<>();
		deviceSpecification.add(new DeviceSpecification("1011", "SP-1011", "HP", "E1011", "T1011", "1.0", "HP-SP1011",
				"Hp Printer", null));
		deviceType = new ArrayList<>();
		deviceType.add(new DeviceType("T1011", "ENG", "device", "deviceDescriptiom"));
		registrationCenters = new ArrayList<>();
		RegistrationCenter registrationCenter = new RegistrationCenter();
		registrationCenter.setId("1011");
		registrationCenter.setAddressLine1("address-line1");
		registrationCenter.setAddressLine2("address-line2");
		registrationCenter.setAddressLine3("address-line3");
		registrationCenter.setCenterEndTime(localTime);
		registrationCenter.setCenterStartTime(localTime);
		registrationCenter.setCenterTypeCode("T1011");
		registrationCenter.setContactPerson("admin");
		registrationCenter.setContactPhone("9865123456");
		registrationCenter.setHolidayLocationCode("LOC01");
		registrationCenter.setIsActive(true);
		registrationCenter.setLangCode("ENG");
		registrationCenter.setWorkingHours("9");
		registrationCenter.setLunchEndTime(localTime);
		registrationCenter.setLunchStartTime(localTime);
		registrationCenters.add(registrationCenter);

		registrationCenterType = new ArrayList<>();
		RegistrationCenterType regCenterType = new RegistrationCenterType();
		regCenterType.setCode("T01");
		registrationCenterType.add(regCenterType);

		templates = new ArrayList<>();
		Template template = new Template();
		template.setId("T222");
		template.setLangCode("eng");
		template.setName("Email template");
		template.setTemplateTypeCode("EMAIL");
		template.setFileFormatCode("XML");
		template.setModuleId("preregistation");
		template.setIsActive(Boolean.TRUE);
		templates.add(template);
		templateFileFormats = new ArrayList<>();
		templateFileFormats.add(new TemplateFileFormat("T101", "ENG", "Email"));
		templateTypes = new ArrayList<>();
		templateTypes.add(new TemplateType("T101", "ENG", "Description"));
		holidays = new ArrayList<>();
		Holiday holiday = new Holiday();
		LocalDate date = LocalDate.of(2018, Month.NOVEMBER, 7);
		holiday = new Holiday();
		holiday.setHolidayId(new HolidayID("KAR", date, "eng", "Diwali"));
		holiday.setId(1);
		holiday.setCreatedBy("John");
		holiday.setCreatedDateTime(localdateTime);
		holiday.setHolidayDesc("Diwali");
		holiday.setIsActive(true);

		Holiday holiday2 = new Holiday();
		holiday2.setHolidayId(new HolidayID("KAH", date, "eng", "Durga Puja"));
		holiday2.setId(1);
		holiday2.setCreatedBy("John");
		holiday2.setCreatedDateTime(localdateTime);
		holiday2.setHolidayDesc("Diwali");
		holiday2.setIsActive(true);

		holidays.add(holiday);
		holidays.add(holiday2);
		blackListedWords = new ArrayList<>();
		blackListedWords.add(new BlocklistedWords("ABC", "ENG", "description"));
		titles = new ArrayList<>();
		titles.add(new Title(new CodeAndLanguageCodeID("1011", "ENG"), "title", "titleDescription"));

		languages = new ArrayList<>();
		languages.add(new Language("ENG", "english", "family", "native name"));
		idTypes = new ArrayList<>();
		idTypes.add(new IdType("ID101", "ENG", "ID", "descr"));
		validDocuments = new ArrayList<>();
		validDocuments.add(new ValidDocument("D101", "DC101", null, null, "ENG"));
		biometricAttributes = new ArrayList<>();
		biometricAttributes.add(new BiometricAttribute("B101", "101", "Fingerprint", "description", "BT101", null));
		biometricTypes = new ArrayList<>();
		biometricTypes.add(new BiometricType("BT101", "ENG", "name", "description"));
		documentCategories = new ArrayList<>();
		documentCategories.add(new DocumentCategory("DC101", "ENG", "DC name", "description"));
		documentTypes = new ArrayList<>();
		documentTypes.add(new DocumentType("DT101", "ENG", "DT Type", "description"));
		reasonCategories = new ArrayList<>();
		reasonCategories.add(new ReasonCategory("RC101", "101", "R-1", "description", null));
		reasonLists = new ArrayList<>();
		reasonLists.add(new ReasonList("RL101", "RL1", "ENG", "RL", "description", null));
		locations = new ArrayList<>();
		Location locationHierarchy = new Location();
		locationHierarchy.setCode("PAT");
		locationHierarchy.setName("PATANA");
		locationHierarchy.setHierarchyLevel(2);
		locationHierarchy.setHierarchyName("Distic");
		locationHierarchy.setParentLocCode("BHR");
		locationHierarchy.setLangCode("ENG");
		locationHierarchy.setCreatedBy("admin");
		locationHierarchy.setUpdatedBy("admin");
		locationHierarchy.setIsActive(true);
		locations.add(locationHierarchy);
		registrationCenterMachines = new ArrayList<>();
		
		Machine registrationCenterMachine = new Machine();
		registrationCenterMachine.setId("10001");
		registrationCenterMachine.setRegCenterId("10002");
		registrationCenterMachine.setIsActive(true);
		registrationCenterMachine.setLangCode("eng");
		registrationCenterMachine.setCreatedBy("admin");
		registrationCenterMachine.setCreatedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		registrationCenterMachine.setIsDeleted(false);
		registrationCenterMachines.add(registrationCenterMachine);

		
		registrationCenterUsers = new ArrayList<>();
		UserDetails user=new UserDetails();
		user.setId("10001");
		user.setIsActive(true);
		user.setIsDeleted(false);
		user.setLangCode("eng");
		user.setRegCenterId("10002");
		registrationCenterUsers.add(user);

		registrationCenterDeviceHistory = new ArrayList<>();
		DeviceHistory deviceHistory=new DeviceHistory();
		deviceHistory.setEffectDateTime(LocalDateTime.now());
		deviceHistory.setId("10001");
		deviceHistory.setRegCenterId("10002");
		deviceHistory.setIsActive(true);
		deviceHistory.setIsDeleted(false);
		deviceHistory.setLangCode("eng");
		registrationCenterDeviceHistory.add(deviceHistory);

		registrationCenterMachineHistory = new ArrayList<>();
		MachineHistory machineHistory=new MachineHistory();
		machineHistory.setId("10001");
		machineHistory.setRegCenterId("10002");
		machineHistory.setLangCode("eng");
		machineHistory.setEffectDateTime(LocalDateTime.now());
		machineHistory.setIsActive(true);
		machineHistory.setIsDeleted(false);
		registrationCenterMachineHistory.add(machineHistory);

		registrationCenterUserHistory = new ArrayList<>();
		UserDetailsHistory userDetailsHistory=new UserDetailsHistory();
		userDetailsHistory.setId("10001");
		userDetailsHistory.setRegCenterId("10002");
		userDetailsHistory.setLangCode("eng");
		userDetailsHistory.setEffDTimes(LocalDateTime.now());
		userDetailsHistory.setIsActive(true);
		userDetailsHistory.setIsDeleted(false);
		registrationCenterUserHistory
				.add(userDetailsHistory);

		ApplicantValidDocument applicantValidDoc = new ApplicantValidDocument();
		ApplicantValidDocumentID appId = new ApplicantValidDocumentID();
		appId.setAppTypeCode("001");
		appId.setDocCatCode("POA");
		appId.setDocTypeCode("RNC");
		applicantValidDoc.setApplicantValidDocumentId(appId);
		applicantValidDoc.setLangCode("eng");
		applicantValidDocumentList = new ArrayList<>();
		applicantValidDocumentList.add(applicantValidDoc);
		Object[] objects = { "10001", "10001" };
		objectArrayList = new ArrayList<>();
		objectArrayList.add(objects);
		AppAuthenticationMethod appAuthenticationMethod = new AppAuthenticationMethod();
		appAuthenticationMethod.setAppId("REGISTRATION");
		appAuthenticationMethod.setAuthMethodCode("sddd");
		appAuthenticationMethod.setMethodSequence(1000);
		appAuthenticationMethods = new ArrayList<>();
		appAuthenticationMethods.add(appAuthenticationMethod);
		AppDetail appDetail = new AppDetail();
		appDetail.setDescr("reg");
		appDetail.setId("1");
		appDetail.setLangCode("eng");
		appDetail.setName("reg");
		appDetails = new ArrayList<>();
		appDetails.add(appDetail);
		AppRolePriority appRolePriority = new AppRolePriority();
		appRolePriority.setAppId("10001");
		appRolePriority.setLangCode("eng");
		appRolePriority.setPriority(1);
		appRolePriority.setProcessId("login_auth");
		appRolePriority.setRoleCode("OFFICER");
		appRolePriorities = new ArrayList<>();
		appRolePriorities.add(appRolePriority);
		ScreenAuthorization screenAuthorization = new ScreenAuthorization();
		screenAuthorization.setIsPermitted(true);
		screenAuthorization.setRoleCode("OFFICER");
		screenAuthorization.setScreenId("loginroot");
		screenAuthorizations = new ArrayList<>();
		screenAuthorizations.add(screenAuthorization);
		ProcessList processListObj = new ProcessList();
		processListObj.setDescr("Packet authentication");
		processListObj.setName("packet authentication");
		processListObj.setLangCode("eng");
		processList = new ArrayList<>();
		processList.add(processListObj);
		ScreenDetail screenDetail = new ScreenDetail();
		screenDetail.setAppId("REGISTRATION");
		screenDetail.setId("REG");
		screenDetail.setDescr("registration");
		screenDetail.setLangCode("eng");
		screenDetailList = new ArrayList<>();
		screenDetailList.add(screenDetail);
		
		signResponse = new SignatureResponse();
		signResponse.setData("asdasdsadf4e");
		signResponse.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		
		mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
				
		UriComponentsBuilder fieldbuilder = UriComponentsBuilder.fromUriString(dynamicfieldUrl);
		mockRestServer.expect(MockRestRequestMatchers.requestTo(fieldbuilder.build().toString()))
		.andRespond(withSuccess().body(
				"{\"id\":null,\"version\":null,\"responsetime\":\"2019-04-24T09:07:42.017Z\",\"metadata\":null,\"response\":{\"pageNo\":1,\"totalPages\":10,\"totalItems\":10,\"data\":[{\"name\":\"bloodType\",\"valueJson\":\"[{\\\"code\\\":\\\"BT1\\\",\\\"value\\\":\\\"Ove\\\",\\\"isActive\\\":true}]\",\"dataType\":\"simpleType\",\"isDeleted\":false,\"isActive\":true,\"langCode\":\"eng\"}]},\"errors\":null}"));


	}

	private void mockSuccess() {
		when(machineRepository
				.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
						.thenReturn(objectArrayList);
				
		when(registrationCenterRepository.findRegistrationCenterByIdAndIsActiveIsTrue(Mockito.anyString()))
				.thenReturn(registrationCenters);
		
		when(machineRepository.getRegCenterIdWithRegIdAndMachineId(Mockito.anyString(),
				Mockito.anyString())).thenReturn(registrationCenterMachines);
		
		when(applicationRepository.findAll()).thenReturn(applications);
		when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(applications);
		when(machineRepository.findMachineById(Mockito.anyString())).thenReturn(machines);
		when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machines);
		when(machineSpecificationRepository.findByMachineId(Mockito.anyString())).thenReturn(machineSpecification);
		when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machineSpecification);
		when(machineTypeRepository.findAllByMachineId(Mockito.anyString())).thenReturn(machineType);
		when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machineType);
		when(templateRepository.findAll()).thenReturn(templates);
		when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(templates);
		when(templateFileFormatRepository.findAllTemplateFormat()).thenReturn(templateFileFormats);
		when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(templateFileFormats);
		when(templateTypeRepository.findAll()).thenReturn(templateTypes);
		when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(templateTypes);
		when(holidayRepository.findAllByMachineId(Mockito.anyString())).thenReturn(holidays);
		when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(holidays);
		when(blocklistedWordsRepository.findAll()).thenReturn(blackListedWords);
		when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(blackListedWords);
		when(registrationCenterRepository.findRegistrationCenterByMachineId(Mockito.anyString()))
				.thenReturn(registrationCenters);
		when(registrationCenterRepository.findLatestRegistrationCenterByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenters);
		when(registrationCenterTypeRepository.findRegistrationCenterTypeByMachineId(Mockito.anyString()))
				.thenReturn(registrationCenterType);
		when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterType);
		when(idTypeRepository.findAll()).thenReturn(idTypes);
		when(idTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(idTypes);
		when(deviceRepository.findDeviceByMachineId(Mockito.anyString())).thenReturn(devices);
		when(deviceRepository.findLatestDevicesByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(devices);
		when(deviceSpecificationRepository.findDeviceTypeByMachineId(Mockito.anyString()))
				.thenReturn(deviceSpecification);
		when(deviceSpecificationRepository.findLatestDeviceTypeByRegCenterId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(deviceSpecification);
		when(deviceTypeRepository.findDeviceTypeByMachineId(Mockito.anyString())).thenReturn(deviceType);
		when(deviceTypeRepository.findLatestDeviceTypeByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(deviceType);
		when(languageRepository.findAll()).thenReturn(languages);
		when(languageRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(languages);
		when(reasonCategoryRepository.findAllReasons()).thenReturn(reasonCategories);
		when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(reasonCategories);
		when(reasonListRepository.findAll()).thenReturn(reasonLists);
		when(reasonListRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(reasonLists);
		when(documentCategoryRepository.findAll()).thenReturn(documentCategories);
		when(documentCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(documentCategories);
		when(documentTypeRepository.findAll()).thenReturn(documentTypes).thenReturn(documentTypes);
		when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(documentTypes);
		when(validDocumentRepository.findAll()).thenReturn(validDocuments);
		when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(validDocuments);
		when(biometricAttributeRepository.findAll()).thenReturn(biometricAttributes);
		when(biometricAttributeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(biometricAttributes);
		when(biometricTypeRepository.findAll()).thenReturn(biometricTypes);
		when(titleRepository.findAll()).thenReturn(titles);
		when(titleRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(titles);
		when(locationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(locations);
		when(locationRepository.findAll()).thenReturn(locations);
//		when(machineRepository.findAllByMachineId(Mockito.any()))
//				.thenReturn(registrationCenterMachines);
		when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenterMachines);
//		when(deviceRepository.findAllByRegistrationCenter(Mockito.any()))
//				.thenReturn(registrationCenterDevices);
		when(deviceRepository.findAllLatestByRegistrationCenterCreatedUpdatedDeleted(Mockito.any(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterDevices);
		
//		when(userDetailsRepository.findAllByRegistrationCenterId(Mockito.any()))
//				.thenReturn(registrationCenterUsers);
		when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(Mockito.any(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterUsers);
		when(userDetailsHistoryRepository.findLatestRegistrationCenterUserHistory(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterUserHistory);
		
		when(machineHistoryRepository.findLatestRegistrationCenterMachineHistory(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterMachineHistory);
		when(deviceHistoryRepository
				.findLatestRegistrationCenterDeviceHistory(Mockito.anyString(), Mockito.any(), Mockito.any()))
						.thenReturn(registrationCenterDeviceHistory);
		
		when(applicantValidDocumentRespository.findAllByTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(applicantValidDocumentList);
		when(appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appAuthenticationMethods);
		when(appDetailRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appDetails);
		when(appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appRolePriorities);
		when(screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(screenAuthorizations);
		when(processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(processList);

		when(screenDetailRepo.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(screenDetailList);
		
		when(deviceProviderRepository.findAllLatestCreatedUpdateDeleted(Mockito.any() , Mockito.any()))
			.thenReturn(Arrays.asList(deviceProvider));
		
		when(deviceServiceRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(),
		  Mockito.any())) .thenReturn(Arrays.asList(deviceService));
		
		when(registeredDeviceRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(),
		  Mockito.any())).thenReturn(Arrays.asList(registeredDevice));
		 
		when(foundationalTrustProviderRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
		  .thenReturn(Arrays.asList(foundationalTrustProvider));
		
		when(deviceTypeDPMRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
			.thenReturn(Arrays.asList(deviceTypeDPM));
		
		when(deviceSubTypeDPMRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), 
				Mockito.any())) .thenReturn(Arrays.asList(deviceSubTypeDPM));
		
		when(signatureUtil.sign(Mockito.anyString())).thenReturn(signResponse);

		when(machineRepository.findByMachineIdAndIsActive(Mockito.anyString())).thenReturn(machines);
	}
	
		
	private String syncDataUrl = "/clientsettings?lastUpdated=2018-11-01T12:10:01.021Z&keyindex=abcd";
	private String syncDataUrlWithoutInput = "/clientsettings";
	private String syncDataUrlWithOnlyLastUpdated = "/clientsettings?lastUpdated=2018-11-01T12:10:01.021Z";
	private String syncDataUrlWithOnlyKeyIndex = "/clientsettings?keyindex=abcd";
	private String syncDataUrlRegCenterId = "/clientsettings/{regcenterId}";
	private String syncDataUrlRegCenterIdWithKeyIndex = "/clientsettings/{regcenterId}?keyindex=abcd";
	private String syncDataUrlRegCenterIdWithKeyIndexAndLastUpdated = "/clientsettings/{regcenterId}?keyindex=abcd&lastUpdated=2018-11-01T12:10:01.021Z";
	private String syncDataUrlWithInvalidTimestamp = "/clientsettings?lastUpdated=2018-15-01T123:101:01.021Z&keyindex=abcd";
	private String syncDataUrlWithKeyIndexAndRegCenterId = "/clientsettings?keyindex=abcd&regcenterId=1002";

	private String v2syncDataUrl = "/v2/clientsettings?lastUpdated=2018-11-01T12:10:01.021Z&keyindex=abcd";
	private String v2syncDataUrlWithoutInput = "/v2/clientsettings";
	private String v2syncDataUrlWithOnlyLastUpdated = "/v2/clientsettings?lastUpdated=2018-11-01T12:10:01.021Z";
	private String v2syncDataUrlWithOnlyKeyIndex = "/v2/clientsettings?keyindex=abcd";
	private String v2syncDataUrlRegCenterId = "/v2/clientsettings/{regcenterId}";
	private String v2syncDataUrlRegCenterIdWithKeyIndex = "/v2/clientsettings/{regcenterId}?keyindex=abcd";
	private String v2syncDataUrlRegCenterIdWithKeyIndexAndLastUpdated = "/v2/clientsettings/{regcenterId}?keyindex=abcd&lastUpdated=2018-11-01T12:10:01.021Z";
	private String v2syncDataUrlWithInvalidTimestamp = "/v2/clientsettings?lastUpdated=2018-15-01T123:101:01.021Z&keyindex=abcd";
	private String v2syncDataUrlWithKeyIndexAndRegCenterId = "/v2/clientsettings?keyindex=abcd&regcenterId=1002";
	

	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncSuccess() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertNotNull(jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncSuccess() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertNotNull(jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncSuccessWithOnlyKeyIndex() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithOnlyKeyIndex)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertNotNull(jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncSuccessWithOnlyKeyIndex() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithOnlyKeyIndex)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertNotNull(jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
	
	
		
	/*@Test
	@WithUserDetails(value = "reg-officer")
	public void syncSuccessBasedOnRegCenterIdWithKeyIndex() throws Exception {
		mockSuccess();
		mockMvc.perform(get(syncDataUrlRegCenterIdWithKeyIndex, "1001")).andExpect(status().isOk());
	}*/
	
	/*@Test
	@WithUserDetails(value = "reg-officer")
	public void syncSuccessBasedOnRegCenterIdWithKeyIndexAndLastUpdated() throws Exception {
		mockSuccess();
		mockMvc.perform(get(syncDataUrlRegCenterIdWithKeyIndexAndLastUpdated, "1001")).andExpect(status().isOk());
	}*/

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncFailureWithoutAnyInput() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithoutInput)).andExpect(status().isInternalServerError()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncFailureWithoutAnyInput() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithoutInput)).andExpect(status().isInternalServerError()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
	
	/*@Test
	@WithUserDetails(value = "reg-officer")
	public void syncFailureWithOnlyRegCenterId() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlRegCenterId, "1001")).andExpect(status().isInternalServerError()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}*/
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncWithOnlyUpdatedTime() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithOnlyLastUpdated)).andExpect(status().isInternalServerError()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncWithOnlyUpdatedTime() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithOnlyLastUpdated)).andExpect(status().isInternalServerError()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncFailureWithInvalidTimeStamp() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithInvalidTimestamp)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncFailureWithInvalidTimeStamp() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithInvalidTimestamp)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncApplicationFetchException() throws Exception {
		mockSuccess();
		when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncApplicationFetchException() throws Exception {
		mockSuccess();
		when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineFetchException() throws Exception {
		mockSuccess();
		when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineFetchException() throws Exception {
		mockSuccess();
		when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineSpecFetchException() throws Exception {
		mockSuccess();
		when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineSpecFetchException() throws Exception {
		mockSuccess();
		when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineTypeFetchException() throws Exception {
		mockSuccess();
		when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineTypeFetchException() throws Exception {
		mockSuccess();
		when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateFetchException() throws Exception {
		mockSuccess();
		when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateFetchException() throws Exception {
		mockSuccess();
		when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateFileFormatFetchException() throws Exception {
		mockSuccess();
		when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateFileFormatFetchException() throws Exception {
		mockSuccess();
		when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateTypeFetchException() throws Exception {
		mockSuccess();
		when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateTypeFetchException() throws Exception {
		mockSuccess();
		when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataHolidayFetchException() throws Exception {
		mockSuccess();
		when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataDocCategoryFetchException() throws Exception {
		mockSuccess();
		when(documentCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataDocTypeFetchException() throws Exception {
		mockSuccess();
		when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataLocationFetchException() throws Exception {
		mockSuccess();
		when(locationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataIdTypesFetchException() throws Exception {
		mockSuccess();
		when(idTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterFetchException() throws Exception {
		mockSuccess();
		when(registrationCenterRepository.findLatestRegistrationCenterByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterTypeFetchException() throws Exception {
		mockSuccess();
		when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataValidFetchException() throws Exception {
		mockSuccess();
		when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataBlackListedWordFetchException() throws Exception {
		mockSuccess();
		when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataBlackListedWordFetchException() throws Exception {
		mockSuccess();
		when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataReasonCatFetchException() throws Exception {
		mockSuccess();
		when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataReasonListFetchException() throws Exception {
		mockSuccess();
		when(reasonListRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDatavalidDocumentFetchException() throws Exception {
		mockSuccess();
		when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterMachineFetchException() throws Exception {
		mockSuccess();
		when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterDeviceFetchException() throws Exception {
		mockSuccess();
		when(deviceRepository.findAllLatestByRegistrationCenterCreatedUpdatedDeleted(
				Mockito.anyString(), Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterMachineDeviceFetchException() throws Exception {
		mockSuccess();
		when(machineRepository
				.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
						.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterUserMachineFetchException() throws Exception {
		mockSuccess();
		when(machineRepository
				.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
						.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Ignore
	@WithUserDetails(value = "reg-officer")
	public void IsMachineIdPresentServiceExceptionTest() throws Exception {
		when(machineRepository.findByMachineIdAndIsActive(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void findApplicantValidDocServiceExceptionTest() throws Exception {
		mockSuccess();
		when(applicantValidDocumentRespository.findAllByTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void appAuthMethodExceptionTest() throws Exception {

		mockSuccess();
		when(appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void registrationCenterTest() throws Exception {

		mockSuccess();
		when(registrationCenterRepository.findRegistrationCenterByIdAndIsActiveIsTrue(Mockito.anyString()))
				.thenReturn(new ArrayList<RegistrationCenter>());
		mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk());

	}	
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void appDetailExceptionTest() throws Exception {

		mockSuccess();
		when(appDetailRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void appPriorityExceptionTest() throws Exception {
		mockSuccess();
		when(appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void screenAuthExceptionTest() throws Exception {

		mockSuccess();
		when(screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void processListExceptionTest() throws Exception {

		mockSuccess();
		when(processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void screenDetailException() throws Exception {
		mockSuccess();
		when(screenDetailRepo.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}

	}

	// test cases to find if valid registration center machine is available for provided keyIndex and regCenterId
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterdataWithMachineListEmpty() throws Exception {
		mockSuccess();
		when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenReturn(new ArrayList<Object[]>());
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			JSONArray errors =  jsonObject.getJSONArray("errors");
			assertNotNull(errors);
			assertEquals(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(), errors.getJSONObject(0).getString("errorCode"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}	

	
	@WithUserDetails(value = "reg-officer")
	public void syncMasterdataWithServiceException() throws Exception {
		mockSuccess();
		when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");}
	}	
	
	@Test
	@WithUserDetails(value= "reg-officer")
	public void testwithRuntimeExceptioninAsyncMethod() throws Exception {
		mockSuccess();
		when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any())).
			thenThrow(RuntimeException.class);
		
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(JSONObject.NULL,jsonObject.get("response"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
	
	@Test
	@WithUserDetails(value= "reg-officer")
	public void syncClientSettingsForUpdatedRegCenterId() throws Exception {
		mockSuccess();
		String [] regcenterMachineId = {"1001", "230030", "ewerwerwerer"};
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(regcenterMachineId);
		
		when(machineRepository.getRegistrationCenterMachineWithKeyIndexWithoutStatusCheck(Mockito.anyString()))
				.thenReturn(data);
		
		MvcResult result = mockMvc.perform(get(syncDataUrlWithKeyIndexAndRegCenterId)).andExpect(status().isOk()).andReturn();
		try {
			JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
			JSONArray errors =  jsonObject.getJSONArray("errors");
			assertNotNull(errors);
			assertEquals(MasterDataErrorCode.REG_CENTER_UPDATED.getErrorCode(), errors.getJSONObject(0).getString("errorCode"));
		} catch(Throwable t) {
			Assert.fail("Not expected response!");
		}
	}
}
