package io.mosip.kernel.syncdata.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.signatureutil.model.SignatureResponse;
import io.mosip.kernel.core.signatureutil.spi.SignatureUtil;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.dto.UploadPublicKeyRequestDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.repository.*;
import io.mosip.kernel.syncdata.service.helper.beans.RegistrationCenterDevice;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import org.junit.Before;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class SyncDataIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private CryptomanagerUtils cryptomanagerUtils;

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
	private List<Machine> registrationCenterMachines;
	private List<Device> registrationCenterDevices;
	private List<UserDetails> registrationCenterUsers;
	private List<MachineHistory> registrationCenterMachineHistory;
	private List<DeviceHistory> registrationCenterDeviceHistory;
	private List<UserDetailsHistory> registrationCenterUserHistory;
	private List<ApplicantValidDocument> applicantValidDocumentList;
	private List<Object[]> objectArrayList;
	private List<AppAuthenticationMethod> appAuthenticationMethods = null;
	private List<AppDetail> appDetails = null;
	private List<AppRolePriority> appRolePriorities = null;
	private List<ScreenAuthorization> screenAuthorizations = null;
	private List<ProcessList> processList = null;
	private List<ScreenDetail> screenDetailList = null;
	@MockBean
	private ApplicationRepository applicationRepository;
	@MockBean
	private MachineRepository machineRepository;
	@Mock
	private UserDetailsRepository userDetailsRepository;
	@MockBean
	private UserDetailsHistoryRepository userDetailsHistoryRepository;
	@MockBean
	private DeviceHistoryRepository deviceHistoryRepository;
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


	@Value("${mosip.kernel.syncdata.syncjob-base-url:http://localhost:8099/v1/admin/syncjobdef}")
	private String baseUri;


	// ###########################CONFIG START#########################

	private static final String JSON_SYNC_JOB_DEF = "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-02T07:49:18.454Z\", \"metadata\": null, \"response\": { \"syncJobDefinitions\": [ { \"id\": \"LCS_J00002\", \"name\": \"Login Credentials Sync\", \"apiName\": null, \"parentSyncJobId\": \"NULL\", \"syncFreq\": \"0 0 11 * * ?\", \"lockDuration\": \"NULL\" } ] }, \"errors\": null } ";
	// ###########################CONFIG END#########################

	@MockBean
	private ApplicantValidDocumentRespository applicantValidDocumentRespository;


	StringBuilder builder;

	private DeviceService deviceService;

	private DeviceProvider deviceProvider;

	private RegisteredDevice registeredDevice;

	private DeviceTypeDPM deviceTypeDPM;

	private DeviceSubTypeDPM deviceSubTypeDPM;

	private FoundationalTrustProvider foundationalTrustProvider;

	@Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
	private String authBaseUri;

	@Value("${mosip.kernel.syncdata.auth-manager-roles}")
	private String authAllRolesUri;

	private SignatureResponse signResponse;

	private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
	//private byte[] tpmPublicKey = cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey);
	//private String keyIndex = CryptoUtil.computeFingerPrint(tpmPublicKey, null);
	private static final String ID = "mosip.syncdata.service";
	private static final String VERSION = "V1.0";
	private static final String MACHINE_NAME = "Machine 2";
	private RequestWrapper<UploadPublicKeyRequestDto> reqWrapper;

	@Before
	public void setup() {
		String keyIndex = CryptoUtil.computeFingerPrint(cryptomanagerUtils.decodeBase64Data(encodedTPMPublicKey), null);
		signResponse = new SignatureResponse();
		signResponse.setData("asdasdsadf4e");
		signResponse.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		LocalDateTime localdateTime = LocalDateTime.parse("2018-11-01T01:01:01");
		LocalTime localTime = LocalTime.parse("09:00:00");
		applications = new ArrayList<>();
		applications.add(new Application("101", "ENG", "MOSIP", "MOSIP"));
		machines = new ArrayList<>();
		machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001", "ENG", localdateTime,
				encodedTPMPublicKey, keyIndex, "ZONE","10002", null, encodedTPMPublicKey, keyIndex);
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
		device.setRegCenterId("10002");
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
		registrationCenterDevices = new ArrayList<>();
		RegistrationCenterDevice registrationCenterDevice = new RegistrationCenterDevice();
		
		device.setRegCenterId("10002");
		device.setId("10001");
		device.setIsActive(true);
		device.setLangCode("eng");
		device.setCreatedBy("admin");
		device.setCreatedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		device.setIsDeleted(false);
		registrationCenterDevices.add(device);
		
		registrationCenterUsers = new ArrayList<>();
		UserDetails user=new UserDetails();
		user.setId("10001");
		user.setIsActive(true);
		user.setIsDeleted(false);
		user.setLangCode("eng");
		user.setRegCenterId("10002");
		registrationCenterUsers.add(user);

		builder = new StringBuilder();
		builder.append(authBaseUri).append(authAllRolesUri);

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

		deviceService = new DeviceService();
		deviceService.setId("1111");
		deviceService.setDProviderId("10001");
		deviceService.setSwVersion("0.1v");

		deviceProvider = new DeviceProvider();
		deviceProvider.setId("1111");

		registeredDevice = new RegisteredDevice();
		registeredDevice.setDeviceId("10001");
		registeredDevice.setStatusCode("Registered");
		registeredDevice.setExpiryDate(LocalDateTime.now());

		deviceTypeDPM = new DeviceTypeDPM();
		deviceTypeDPM.setCode("1111");
		deviceTypeDPM.setName("devicetype");

		deviceSubTypeDPM = new DeviceSubTypeDPM();
		deviceSubTypeDPM.setCode("1234");
		deviceSubTypeDPM.setDtypeCode("1111");
		deviceSubTypeDPM.setName("deviceSubType");

		foundationalTrustProvider = new FoundationalTrustProvider();
		foundationalTrustProvider.setId("11111");
		foundationalTrustProvider.setName("ftps");

		reqWrapper = new RequestWrapper<>();
		UploadPublicKeyRequestDto uploadPublicKeyRequestDto = new UploadPublicKeyRequestDto();
		uploadPublicKeyRequestDto.setMachineName(MACHINE_NAME);
		uploadPublicKeyRequestDto.setPublicKey(encodedTPMPublicKey);
		uploadPublicKeyRequestDto.setSignPublicKey(encodedTPMPublicKey);
		reqWrapper.setId(ID);
		reqWrapper.setVersion(VERSION);
		reqWrapper.setRequesttime(DateUtils.parseToLocalDateTime("2018-12-06T12:07:44.403Z"));
		reqWrapper.setRequest(uploadPublicKeyRequestDto);
	}

	private void mockSuccess() {
		when(machineRepository
				.getRegistrationCenterMachineWithSerialNumberAndKeyIndex(Mockito.anyString(), Mockito.anyString()))
						.thenReturn(objectArrayList);
		;
		when(machineRepository
				.getRegistrationCenterMachineWithMacAddressAndKeyIndex(Mockito.anyString(), Mockito.anyString()))
						.thenReturn(objectArrayList);
		;
		when(machineRepository.getRegistrationCenterMachineWithMacAddressAndSerialNumAndKeyIndex(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(objectArrayList);
		;
		when(machineRepository.getRegistrationCenterMachineWithKeyIndex(Mockito.anyString()))
				.thenReturn(objectArrayList);
		when(registrationCenterRepository.findRegistrationCenterByIdAndIsActiveIsTrue(Mockito.anyString()))
				.thenReturn(registrationCenters);
		when(machineRepository.getRegCenterIdWithRegIdAndMachineId(Mockito.anyString(),
				Mockito.anyString())).thenReturn(registrationCenterMachines);
		when(machineRepository.getRegistrationCenterMachineWithMacAddress(Mockito.anyString()))
				.thenReturn(objectArrayList);
		when(machineRepository.getRegistrationCenterMachineWithSerialNumber(Mockito.anyString()))
				.thenReturn(objectArrayList);
		when(machineRepository
				.getRegistrationCenterMachineWithMacAddressAndSerialNum(Mockito.anyString(), Mockito.anyString()))
						.thenReturn(objectArrayList);
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
		
		when(deviceHistoryRepository.findLatestRegistrationCenterDeviceHistory(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterDeviceHistory);
		when(machineHistoryRepository.findLatestRegistrationCenterMachineHistory(Mockito.anyString(),
				Mockito.any(), Mockito.any())).thenReturn(registrationCenterMachineHistory);
		
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
		MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri).queryParam("lastupdatedtimestamp",
				"1970-01-01T00:00:00.000Z");
		server.expect(requestTo(builder.toUriString())).andRespond(withSuccess().body(JSON_SYNC_JOB_DEF));
		when(signatureUtil.sign(Mockito.anyString())).thenReturn(signResponse);


	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void getRegistrationCenterUserMasterDataNotFoundExcepetion() {
		lenient().when(userDetailsRepository.findByUsersByRegCenterId(Mockito.anyString()))
				.thenReturn(new ArrayList<>());

		assertNotNull(userDetailsRepository);
	}

}