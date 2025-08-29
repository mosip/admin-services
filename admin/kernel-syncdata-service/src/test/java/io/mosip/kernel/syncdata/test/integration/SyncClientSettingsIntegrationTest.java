package io.mosip.kernel.syncdata.test.integration;

import io.mosip.kernel.core.signatureutil.model.SignatureResponse;
import io.mosip.kernel.core.signatureutil.spi.SignatureUtil;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.repository.*;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class SyncClientSettingsIntegrationTest {

	@Mock
	RestTemplate restTemplate;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private CryptomanagerUtils cryptomanagerUtils;

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

	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private MachineRepository machineRepository;
	@Mock
	private DeviceHistoryRepository deviceHistoryRepository;
	@Mock
	private UserDetailsRepository userDetailsRepository;
	@Mock
	private UserDetailsHistoryRepository userDetailsHistoryRepository;

	@Mock
	private MachineTypeRepository machineTypeRepository;
	@Mock
	private RegistrationCenterRepository registrationCenterRepository;
	@Mock
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;
	@Mock
	private TemplateRepository templateRepository;
	@Mock
	private TemplateFileFormatRepository templateFileFormatRepository;
	@Mock
	private ReasonCategoryRepository reasonCategoryRepository;
	@Mock
	private HolidayRepository holidayRepository;
	@Mock
	private BlocklistedWordsRepository blocklistedWordsRepository;
	@Mock
	private BiometricTypeRepository biometricTypeRepository;
	@Mock
	private BiometricAttributeRepository biometricAttributeRepository;
	@Mock
	private TitleRepository titleRepository;
	@Mock
	private LanguageRepository languageRepository;
	@Mock
	private DeviceRepository deviceRepository;
	@Mock
	private DocumentCategoryRepository documentCategoryRepository;
	@Mock
	private DocumentTypeRepository documentTypeRepository;
	@Mock
	private IdTypeRepository idTypeRepository;
	@Mock
	private DeviceSpecificationRepository deviceSpecificationRepository;
	@Mock
	private LocationRepository locationRepository;
	@Mock
	private TemplateTypeRepository templateTypeRepository;
	@Mock
	private MachineSpecificationRepository machineSpecificationRepository;
	@Mock
	private DeviceTypeRepository deviceTypeRepository;
	@Mock
	private ValidDocumentRepository validDocumentRepository;
	@Mock
	private ReasonListRepository reasonListRepository;

	@Mock
	private AppAuthenticationMethodRepository appAuthenticationMethodRepository;
	@Mock
	private AppDetailRepository appDetailRepository;
	@Mock
	private AppRolePriorityRepository appRolePriorityRepository;
	@Mock
	private ScreenAuthorizationRepository screenAuthorizationRepository;
	@Mock
	private ProcessListRepository processListRepository;

	@Mock
	private MachineHistoryRepository machineHistoryRepository;

	@Mock
	private ScreenDetailRepository screenDetailRepo;
	@Mock
	private SignatureUtil signatureUtil;
	@Mock
	private DeviceProviderRepository deviceProviderRepository;
	@Mock
	private DeviceServiceRepository deviceServiceRepository;
	@Mock
	private RegisteredDeviceRepository registeredDeviceRepository;
	@Mock
	private FoundationalTrustProviderRepository foundationalTrustProviderRepository;
	@Mock
	private DeviceTypeDPMRepository deviceTypeDPMRepository;
	@Mock
	private DeviceSubTypeDPMRepository deviceSubTypeDPMRepository;

	@Mock
	private ApplicantValidDocumentRespository applicantValidDocumentRespository;

	private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
	// private byte[] tpmPublicKey =
	// cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey);
	// private String keyIndex =
	// CryptoUtil.computeFingerPrint(cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey),
	// null);

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


		// Mock cryptomanagerUtils so decodeBase64Data never returns null
		cryptomanagerUtils = Mockito.mock(CryptomanagerUtils.class);

		encodedTPMPublicKey = "FAKE_BASE64_STRING";

		String keyIndex = CryptoUtil.computeFingerPrint("fake-public-key".getBytes(), null);

		machines = new ArrayList<>();
		machine = new Machine(
				"1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12",
				"1001", "ENG", localdateTime,
				encodedTPMPublicKey, keyIndex,
				"ZONE", "10002", null, encodedTPMPublicKey, keyIndex
		);
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
		Holiday holiday;
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
		UserDetails user = new UserDetails();
		user.setId("10001");
		user.setIsActive(true);
		user.setIsDeleted(false);
		user.setLangCode("eng");
		user.setRegCenterId("10002");
		registrationCenterUsers.add(user);

		registrationCenterDeviceHistory = new ArrayList<>();
		DeviceHistory deviceHistory = new DeviceHistory();
		deviceHistory.setEffectDateTime(LocalDateTime.now());
		deviceHistory.setId("10001");
		deviceHistory.setRegCenterId("10002");
		deviceHistory.setIsActive(true);
		deviceHistory.setIsDeleted(false);
		deviceHistory.setLangCode("eng");
		registrationCenterDeviceHistory.add(deviceHistory);

		registrationCenterMachineHistory = new ArrayList<>();
		MachineHistory machineHistory = new MachineHistory();
		machineHistory.setId("10001");
		machineHistory.setRegCenterId("10002");
		machineHistory.setLangCode("eng");
		machineHistory.setEffectDateTime(LocalDateTime.now());
		machineHistory.setIsActive(true);
		machineHistory.setIsDeleted(false);
		registrationCenterMachineHistory.add(machineHistory);

		registrationCenterUserHistory = new ArrayList<>();
		UserDetailsHistory userDetailsHistory = new UserDetailsHistory();
		userDetailsHistory.setId("10001");
		userDetailsHistory.setRegCenterId("10002");
		userDetailsHistory.setLangCode("eng");
		userDetailsHistory.setEffDTimes(LocalDateTime.now());
		userDetailsHistory.setIsActive(true);
		userDetailsHistory.setIsDeleted(false);
		registrationCenterUserHistory.add(userDetailsHistory);

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

		/*if (dynamicfieldUrl != null && !dynamicfieldUrl.isEmpty()) {
			UriComponentsBuilder fieldbuilder = UriComponentsBuilder.fromUriString(dynamicfieldUrl);
			mockRestServer.expect(MockRestRequestMatchers.requestTo(fieldbuilder.build().toString()))
					.andRespond(withSuccess().body(
							"{\"id\":null,\"version\":null,\"responsetime\":\"2019-04-24T09:07:42.017Z\",\"metadata\":null,\"response\":{\"pageNo\":1,\"totalPages\":10,\"totalItems\":10,\"data\":[{\"name\":\"bloodType\",\"valueJson\":\"[{\\\"code\\\":\\\"BT1\\\",\\\"value\\\":\\\"Ove\\\",\\\"isActive\\\":true}]\",\"dataType\":\"simpleType\",\"isDeleted\":false,\"isActive\":true,\"langCode\":\"eng\"}]},\"errors\":null}"));
		}
		else {
			throw new IllegalArgumentException("dynamicfieldUrl is null or empty");
		}*/
	}

	private void mockSuccess() {
		lenient().when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenReturn(objectArrayList);

		lenient().when(registrationCenterRepository.findRegistrationCenterByIdAndIsActiveIsTrue(Mockito.anyString()))
				.thenReturn(registrationCenters);

		lenient().when(machineRepository.getRegCenterIdWithRegIdAndMachineId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(registrationCenterMachines);

		lenient().when(applicationRepository.findAll()).thenReturn(applications);
		lenient().when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(applications);
		lenient().when(machineRepository.findMachineById(Mockito.anyString())).thenReturn(machines);
		lenient().when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machines);
		lenient().when(machineSpecificationRepository.findByMachineId(Mockito.anyString())).thenReturn(machineSpecification);
		lenient().when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machineSpecification);
		lenient().when(machineTypeRepository.findAllByMachineId(Mockito.anyString())).thenReturn(machineType);
		lenient().when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(machineType);
		lenient().when(templateRepository.findAll()).thenReturn(templates);
		lenient().when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(templates);
		lenient().when(templateFileFormatRepository.findAllTemplateFormat()).thenReturn(templateFileFormats);
		lenient().when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(templateFileFormats);
		lenient().when(templateTypeRepository.findAll()).thenReturn(templateTypes);
		lenient().when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(templateTypes);
		lenient().when(holidayRepository.findAllByMachineId(Mockito.anyString())).thenReturn(holidays);
		lenient().when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(holidays);
		lenient().when(blocklistedWordsRepository.findAll()).thenReturn(blackListedWords);
		lenient().when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(blackListedWords);
		lenient().when(registrationCenterRepository.findRegistrationCenterByMachineId(Mockito.anyString()))
				.thenReturn(registrationCenters);
		lenient().when(registrationCenterRepository.findLatestRegistrationCenterByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenters);
		lenient().when(registrationCenterTypeRepository.findRegistrationCenterTypeByMachineId(Mockito.anyString()))
				.thenReturn(registrationCenterType);
		lenient().when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterType);
		lenient().when(idTypeRepository.findAll()).thenReturn(idTypes);
		lenient().when(idTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(idTypes);
		lenient().when(deviceRepository.findDeviceByMachineId(Mockito.anyString())).thenReturn(devices);
		lenient().when(deviceRepository.findLatestDevicesByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(devices);
		lenient().when(deviceSpecificationRepository.findDeviceTypeByMachineId(Mockito.anyString()))
				.thenReturn(deviceSpecification);
		lenient().when(deviceSpecificationRepository.findLatestDeviceTypeByRegCenterId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(deviceSpecification);
		lenient().when(deviceTypeRepository.findDeviceTypeByMachineId(Mockito.anyString())).thenReturn(deviceType);
		lenient().when(deviceTypeRepository.findLatestDeviceTypeByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(deviceType);
		lenient().when(languageRepository.findAll()).thenReturn(languages);
		lenient().when(languageRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(languages);
		lenient().when(reasonCategoryRepository.findAllReasons()).thenReturn(reasonCategories);
		lenient().when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(reasonCategories);
		lenient().when(reasonListRepository.findAll()).thenReturn(reasonLists);
		lenient().when(reasonListRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(reasonLists);
		lenient().when(documentCategoryRepository.findAll()).thenReturn(documentCategories);
		lenient().when(documentCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(documentCategories);
		lenient().when(documentTypeRepository.findAll()).thenReturn(documentTypes).thenReturn(documentTypes);
		lenient().when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(documentTypes);
		lenient().when(validDocumentRepository.findAll()).thenReturn(validDocuments);
		lenient().when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(validDocuments);
		lenient().when(biometricAttributeRepository.findAll()).thenReturn(biometricAttributes);
		lenient().when(biometricAttributeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(biometricAttributes);
		lenient().when(biometricTypeRepository.findAll()).thenReturn(biometricTypes);
		lenient().when(titleRepository.findAll()).thenReturn(titles);
		lenient().when(titleRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(titles);
		lenient().when(locationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any())).thenReturn(locations);
		lenient().when(locationRepository.findAll()).thenReturn(locations);
//		lenient().when(machineRepository.findAllByMachineId(Mockito.any()))
//				.thenReturn(registrationCenterMachines);
		lenient().when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(registrationCenterMachines);
//		lenient().when(deviceRepository.findAllByRegistrationCenter(Mockito.any()))
//				.thenReturn(registrationCenterDevices);
		lenient().when(deviceRepository.findAllLatestByRegistrationCenterCreatedUpdatedDeleted(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenterDevices);

//		lenient().when(userDetailsRepository.findAllByRegistrationCenterId(Mockito.any()))
//				.thenReturn(registrationCenterUsers);
		lenient().when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(registrationCenterUsers);
		lenient().when(userDetailsHistoryRepository.findLatestRegistrationCenterUserHistory(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenterUserHistory);

		lenient().when(machineHistoryRepository.findLatestRegistrationCenterMachineHistory(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenterMachineHistory);
		lenient().when(deviceHistoryRepository.findLatestRegistrationCenterDeviceHistory(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(registrationCenterDeviceHistory);

		lenient().when(applicantValidDocumentRespository.findAllByTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(applicantValidDocumentList);
		lenient().when(appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appAuthenticationMethods);
		lenient().when(appDetailRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appDetails);
		lenient().when(appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(appRolePriorities);
		lenient().when(screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(screenAuthorizations);
		lenient().when(processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(processList);

		lenient().when(screenDetailRepo.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenReturn(screenDetailList);

		lenient().when(deviceProviderRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(deviceProvider));

		lenient().when(deviceServiceRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(deviceService));

		lenient().when(registeredDeviceRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(Arrays.asList(registeredDevice));

		lenient().when(foundationalTrustProviderRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(foundationalTrustProvider));

		lenient().when(deviceTypeDPMRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(deviceTypeDPM));

		lenient().when(deviceSubTypeDPMRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenReturn(Arrays.asList(deviceSubTypeDPM));

		lenient().when(signatureUtil.sign(Mockito.anyString())).thenReturn(signResponse);

		lenient().when(machineRepository.findByMachineIdAndIsActive(Mockito.anyString())).thenReturn(machines);
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

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void syncSuccess() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertNotNull(jsonObject.get("response"));
	}

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void v2syncSuccess() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrl)).andExpect(status().isOk()).andReturn();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertNotNull(jsonObject.get("response"));
	}

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void syncSuccessWithOnlyKeyIndex() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithOnlyKeyIndex)).andExpect(status().isOk()).andReturn();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertNotNull(jsonObject.get("response"));
	}

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void v2syncSuccessWithOnlyKeyIndex() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithOnlyKeyIndex)).andExpect(status().isOk()).andReturn();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertNotNull(jsonObject.get("response"));
	}

	/*
	 * @Test
	 *
	 * @WithUserDetails(value = "reg-officer") public void
	 * syncSuccessBasedOnRegCenterIdWithKeyIndex() throws Exception { mockSuccess();
	 * mockMvc.perform(get(syncDataUrlRegCenterIdWithKeyIndex,
	 * "1001")).andExpect(status().isOk()); }
	 */

	/*
	 * @Test
	 *
	 * @WithUserDetails(value = "reg-officer") public void
	 * syncSuccessBasedOnRegCenterIdWithKeyIndexAndLastUpdated() throws Exception {
	 * mockSuccess();
	 * mockMvc.perform(get(syncDataUrlRegCenterIdWithKeyIndexAndLastUpdated,
	 * "1001")).andExpect(status().isOk()); }
	 */

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void syncFailureWithoutAnyInput() throws Exception {
		mockSuccess();
		MvcResult result = mockMvc.perform(get(syncDataUrlWithoutInput)).andExpect(status().isInternalServerError())
				.andReturn();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertEquals(JSONObject.NULL, jsonObject.get("response"));
	}

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void v2syncFailureWithoutAnyInput() throws Exception {
		mockSuccess();
		JSONObject jsonObject = null;
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithoutInput)).andExpect(status().isInternalServerError())
				.andReturn();
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertEquals(JSONObject.NULL, jsonObject.get("response"));
	}

	/*
	 * @Test
	 *
	 * @WithUserDetails(value = "reg-officer") public void
	 * syncFailureWithOnlyRegCenterId() throws Exception { mockSuccess(); MvcResult
	 * result = mockMvc.perform(get(syncDataUrlRegCenterId,
	 * "1001")).andExpect(status().isInternalServerError()).andReturn(); try {
	 * JSONObject jsonObject = new
	 * JSONObject(result.getResponse().getContentAsString());
	 * assertEquals(JSONObject.NULL,jsonObject.get("response")); } catch(Throwable
	 * t) { Assert.fail("Not expected response!"); } }
	 */

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void syncWithOnlyUpdatedTime() throws Exception {
		mockSuccess();
		JSONObject jsonObject = null;
		MvcResult result = mockMvc.perform(get(syncDataUrlWithOnlyLastUpdated))
				.andExpect(status().isInternalServerError()).andReturn();
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertEquals(JSONObject.NULL, jsonObject.get("response"));

	}

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void v2syncWithOnlyUpdatedTime() throws Exception {
		mockSuccess();
		JSONObject jsonObject = null;
		MvcResult result = mockMvc.perform(get(v2syncDataUrlWithOnlyLastUpdated))
				.andExpect(status().isInternalServerError()).andReturn();
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());

		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertEquals(JSONObject.NULL, jsonObject.get("response"));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncFailureWithInvalidTimeStamp() {
		mockSuccess();
		lenient().when(userDetailsHistoryRepository.getByUserIdAndTimestamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncFailureWithInvalidTimeStamp() {
		mockSuccess();
		lenient().when(machineHistoryRepository.findByIdAndLangCodeAndEffectDateTimeLessThanEqualAndIsDeletedFalse
				(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncApplicationFetchException() {
		mockSuccess();
		lenient().when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncApplicationFetchException() {
		mockSuccess();
		lenient().when(applicationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineFetchException() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineFetchException() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineSpecFetchException() {
		mockSuccess();
		lenient().when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineSpecFetchException() {
		mockSuccess();
		lenient().when(machineSpecificationRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataMachineTypeFetchException() {
		mockSuccess();
		lenient().when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataMachineTypeFetchException() {
		mockSuccess();
		lenient().when(machineTypeRepository.findLatestByRegCenterId(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateFetchException() {
		mockSuccess();
		lenient().when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateFetchException() {
		mockSuccess();
		lenient().when(templateRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateFileFormatFetchException() {
		mockSuccess();
		lenient().when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateFileFormatFetchException() {
		mockSuccess();
		lenient().when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataTemplateTypeFetchException() {
		mockSuccess();
		lenient().when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataTemplateTypeFetchException() {
		mockSuccess();
		lenient().when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataHolidayFetchException() {
		mockSuccess();
		lenient().when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataDocCategoryFetchException() {
		mockSuccess();
		lenient().when(documentCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataDocTypeFetchException() {
		mockSuccess();
		lenient().when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataLocationFetchException() {
		mockSuccess();
		lenient().when(locationRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataIdTypesFetchException() {
		mockSuccess();
		lenient().when(idTypeRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterFetchException() {
		mockSuccess();
		lenient().when(registrationCenterRepository.findLatestRegistrationCenterByMachineId(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterTypeFetchException() {
		mockSuccess();
		lenient().when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataValidFetchException() {
		mockSuccess();
		lenient().when(registrationCenterTypeRepository.findLatestRegistrationCenterTypeByMachineId(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataBlackListedWordFetchException() {
		mockSuccess();
		lenient().when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void v2syncMasterDataBlackListedWordFetchException() {
		mockSuccess();
		lenient().when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataReasonCatFetchException() {
		mockSuccess();
		lenient().when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataReasonListFetchException() {
		mockSuccess();
		lenient().when(reasonListRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDatavalidDocumentFetchException() {
		mockSuccess();
		lenient().when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterMachineFetchException() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterDeviceFetchException() {
		mockSuccess();
		lenient().when(deviceRepository.findAllLatestByRegistrationCenterCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterMachineDeviceFetchException() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncMasterDataRegistrationCenterUserMachineFetchException() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdatedDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Ignore
	@WithUserDetails(value = "reg-officer")
	public void IsMachineIdPresentServiceExceptionTest() {
		lenient().when(machineRepository.findByMachineIdAndIsActive(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void findApplicantValidDocServiceExceptionTest() {
		mockSuccess();
		lenient().when(applicantValidDocumentRespository.findAllByTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void appAuthMethodExceptionTest() {

		mockSuccess();
		lenient().when(appAuthenticationMethodRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void registrationCenterTest() {

		mockSuccess();
		lenient().when(registrationCenterRepository.findRegistrationCenterByIdAndIsActiveIsTrue(Mockito.anyString()))
				.thenReturn(new ArrayList<RegistrationCenter>());
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void appDetailExceptionTest() {

		mockSuccess();
		lenient().when(appDetailRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void appPriorityExceptionTest() {
		mockSuccess();
		lenient().when(appRolePriorityRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void screenAuthExceptionTest() {

		mockSuccess();
		lenient().when(screenAuthorizationRepository.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void processListExceptionTest() {

		mockSuccess();
		lenient().when(processListRepository.findByLastUpdatedTimeAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void screenDetailException() {
		mockSuccess();
		lenient().when(screenDetailRepo.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	// test cases to find if valid registration center machine is available for
	// provided keyIndex and regCenterId

	@Test (expected = Exception.class)
	@WithUserDetails(value = "reg-officer")
	public void syncMasterdataWithMachineListEmpty() throws Exception {
		mockSuccess();
		lenient().when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenReturn(new ArrayList<Object[]>());
		MvcResult result = mockMvc.perform(get(syncDataUrl)).andExpect(status().isOk()).andReturn();
		JSONObject jsonObject = null;
		JSONArray errors = null;
		try {
			jsonObject = new JSONObject(result.getResponse().getContentAsString());
			errors = jsonObject.getJSONArray("errors");
		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertNotNull(errors);
		assertEquals(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
				errors.getJSONObject(0).getString("errorCode"));

	}

	@WithUserDetails(value = "reg-officer")
	public void syncMasterdataWithServiceException() {
		mockSuccess();
		lenient().when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenThrow(DataRetrievalFailureException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testwithRuntimeExceptioninAsyncMethod() {
		mockSuccess();
		lenient().when(machineRepository.findAllLatestCreatedUpdateDeleted(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(RuntimeException.class);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void syncClientSettingsForUpdatedRegCenterId() {
		mockSuccess();
		Machine machine = new Machine();
		machine.setId("230030");
		machine.setRegCenterId("1001");
		machine.setPublicKey("ewerwerwerer");

		lenient().when(machineRepository.findOneByKeyIndexIgnoreCase(Mockito.anyString()))
				.thenReturn(machine);
		assertNotNull(machine);
	}

	private void testExecution(String url) throws JSONException {
		JSONObject jsonObject = null;
		try {
			MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

			jsonObject = new JSONObject(result.getResponse().getContentAsString());
		} catch (Throwable t) {
			Assert.fail("Not expected response!");
		}
		assertEquals(JSONObject.NULL, jsonObject.get("response"));

	}

}
