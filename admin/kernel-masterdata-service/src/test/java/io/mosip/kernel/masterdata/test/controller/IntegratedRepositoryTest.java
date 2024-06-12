package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.MachinePutReqDto;
import io.mosip.kernel.masterdata.dto.*;
import io.mosip.kernel.masterdata.dto.request.*;
import io.mosip.kernel.masterdata.entity.*;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.*;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegratedRepositoryTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;

	@MockBean
	private MachineHistoryRepository historyRepository;

	@Autowired
	private ValidDocumentRepository validDocumentRepository;

	@MockBean
	private UserDetailsHistoryRepository userHistoryRepository;

	private List<MachineHistory> machinesHistories = new ArrayList<>();
	private List<UserDetailsHistory> usersHistories = new ArrayList<>();

	@MockBean
	private LocationHierarchyRepository locHierarchyRepo;

	@MockBean
	private TemplateFileFormatRepository templateFileFormatRepository;

	@MockBean
	private DocumentTypeRepository documentTypeRepository;

	@MockBean
	private DocumentCategoryRepository documentCategoryRepository;

	@MockBean
	private RegistrationCenterTypeRepository registrationCenterTypeRepository;

	@MockBean
	private DeviceTypeRepository deviceTypeRepository;

	@MockBean
	private BlocklistedWordsRepository blocklistedWordsRepository;

	@MockBean
	private TemplateRepository templateRepository;

	@MockBean
	private ZoneRepository zoneRepository;

	@MockBean
	private ExceptionalHolidayRepository expHoliday;

	@MockBean
	private ValidDocumentRepository vdocumentRepository;

	@MockBean
	private DeviceSpecificationRepository deviceSpecificationRepository;

	@MockBean
	private MachineTypeRepository machineTypeRepository;

	@MockBean
	private MachineSpecificationRepository machineSpecificationRepository;

	@MockBean
	private DynamicFieldRepository dynamicFieldRepository;

	@MockBean
	private DaysOfWeekListRepo daysOfWeekRepo;

	@MockBean
	@Qualifier("workingDaysRepo")
	private RegWorkingNonWorkingRepo workingDaysRepo;

	@MockBean
	private MachineRepository machineRepository;

	@MockBean
	private DeviceRepository deviceRepository;

	@MockBean
	private RegistrationCenterRepository registrationCenterRepository;

	@MockBean
	private ZoneUserHistoryRepository zoneUserHistoryRepo;

	@MockBean
	private ZoneUserRepository zoneUserRepo;

	@MockBean
	private LanguageRepository languageRepository;
	@MockBean
	private LocationRepository locReg;

	@MockBean
	private UserDetailsRepository userDetailsRepository;

	@MockBean
	private IdentitySchemaRepository identitySchemaRepository;

	@MockBean
	private UISpecService uiSpecService;

	@MockBean
	private TitleRepository titleRepository;

	IdentitySchema is = null;
	List<UISpecResponseDto> lstui = new ArrayList<UISpecResponseDto>();

	@Before
	public void setUp() {
		// MockitoAnnotations.initMocks(this);
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		MachineHistory mh = new MachineHistory();
		mh.setIpAddress("192.168.0.122");
		mh.setIsActive(true);
		mh.setLangCode("eng");
		mh.setMacAddress("E8-A9-64-1F-27-E6");
		mh.setMachineSpecId("1001");
		mh.setName("machine11");
		mh.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		mh.setRegCenterId("10001");
		mh.setSerialNum("NM10037379");
		mh.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		mh.setZoneCode("NTH");
		mh.setIsActive(true);
		mh.setEffectDateTime(LocalDateTime.of(2021, Month.DECEMBER, 21, 19, 30, 40));
		machinesHistories.add(mh);
		UserDetailsHistory udh = new UserDetailsHistory();
		udh.setCreatedBy("test");
		udh.setCreatedDateTime(LocalDateTime.now());
		udh.setDeletedDateTime(LocalDateTime.now());
		udh.setEffDTimes(LocalDateTime.of(2021, Month.NOVEMBER, 29, 19, 30, 40));
		udh.setId("10");
		udh.setIsActive(null);
		udh.setIsDeleted(false);
		udh.setLangCode("test");
		udh.setLastLoginDateTime(LocalDateTime.now());
		udh.setLastLoginMethod("test");
		udh.setName("test");
		udh.setRegCenterId("test");
		udh.setStatusCode("test");
		udh.setUpdatedBy("test");
		udh.setUpdatedDateTime(LocalDateTime.now());
		usersHistories.add(udh);
		getZoneUser();
		getZone();
		devicePutDto();
		docCategory();
		blockListedWordsRequestDto();
		deviceSpecification();
		machinetype();
		dynamicFieldDtoReq();
		workDay();

		is = new IdentitySchema();
		is.setAdditionalProperties(false);
		is.setCreatedBy("superuser");
		is.setCreatedDateTime(LocalDateTime.now());
		is.setDescription("test");
		is.setEffectiveFrom(LocalDateTime.now());
		is.setId("test");
		is.setIdVersion(0.1);
		is.setIsActive(false);
		is.setIsDeleted(false);
		is.setLangCode("eng");
		is.setSchemaJson("{\"code\":\"test\"}");
		is.setStatus("active");
		is.setTitle("test");

		lstui = new ArrayList<UISpecResponseDto>();
		UISpecResponseDto u = new UISpecResponseDto();

		u.setCreatedBy("superuser");
		u.setCreatedOn(LocalDateTime.now());
		u.setDescription("test");
		u.setEffectiveFrom(LocalDateTime.now());
		u.setId("test");
		u.setIdentitySchemaId("test");
		u.setIdSchemaVersion(0.1);

		u.setStatus("active");
		u.setTitle("test");
		lstui.add(u);

	}

	private List<Machine> getMachines() {
		Machine mh = new Machine();
		mh.setIpAddress("192.168.0.122");
		mh.setIsActive(true);
		mh.setLangCode("eng");
		mh.setMacAddress("E8-A9-64-1F-27-E6");
		mh.setMachineSpecId("1001");
		mh.setName("machine11");
		mh.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		mh.setRegCenterId("10001");
		mh.setSerialNum("NM10037379");
		mh.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		mh.setZoneCode("NTH");
		mh.setIsActive(true);
		List l = new ArrayList<>();
		l.add(mh);
		return l;

	}

	private RequestWrapper<DynamicFieldDto> dynamicFieldDtoReq() {
		RequestWrapper<DynamicFieldDto> dynamicFieldDtoReq = new RequestWrapper<DynamicFieldDto>();
		DynamicFieldDto dynamicFieldDto = new DynamicFieldDto();
		dynamicFieldDto.setDataType("string");
		dynamicFieldDto.setDescription("Blood Type");
		dynamicFieldDto.setLangCode("eng");
		dynamicFieldDto.setName("bloodtype");

		Map m = new HashMap<>();
		m.put("code", "anbc");
		JsonNode jsonNode = mapper.valueToTree(m);

		// JsonNode node = mapper.valueToTree(fromValue);
		dynamicFieldDto.setFieldVal(jsonNode);

		dynamicFieldDtoReq.setRequest(dynamicFieldDto);
		return dynamicFieldDtoReq;
	}

	private ZoneUser getZoneUser() {
		ZoneUser z = new ZoneUser();
		z.setCreatedBy("superadmin");
		z.setCreatedDateTime(LocalDateTime.now());
		z.setDeletedDateTime(null);
		z.setIsActive(true);
		z.setLangCode("eng");
		z.setUpdatedBy(null);
		z.setUserId("global-admin");
		z.setZoneCode("NTH");
		return z;
	}

	private RegistrationCenter getRegistrationCenter() {
		RegistrationCenter r = new RegistrationCenter();

		r.setAddressLine1("add1");
		r.setAddressLine2("add2");
		r.setAddressLine3("add3");
		r.setCenterEndTime(LocalTime.NOON);
		r.setCenterStartTime(LocalTime.MIDNIGHT);
		r.setCenterTypeCode("REG");
		r.setContactPerson("Magic");
		r.setContactPhone("1234567891");
		r.setZoneCode("NTH");
		r.setWorkingHours("8");
		r.setTimeZone("(GTM+01:00) CENTRAL EUROPEAN TIME");
		r.setNumberOfKiosks((short) 2);
		r.setPerKioskProcessTime(LocalTime.NOON);
		r.setName("Mysore road");
		r.setLocationCode("14022");
		// r.setLunchEndTime(new LocalTimeType().fromString("12:00"));
		// r.setLunchStartTime(new LocalTimeType().fromString("12:30"));
		r.setLongitude("32.3423");
		// r.setId("1");
		r.setLatitude("23.3434");
		r.setLangCode("eng");
		r.setHolidayLocationCode("14022");
		r.setIsActive(true);
		return r;
	}

	private RequestWrapper<WorkingDaysPutRequestDto> workDay() {
		RequestWrapper<WorkingDaysPutRequestDto> workingDayPutReq = new RequestWrapper<>();

		WorkingDaysPutRequestDto putRequestDto = new WorkingDaysPutRequestDto();
		putRequestDto.setCode("101");
		putRequestDto.setDaySeq((short) 1);
		putRequestDto.setGlobalWorking(false);
		putRequestDto.setLangCode("eng");
		putRequestDto.setName("SUN");

		workingDayPutReq.setRequest(putRequestDto);
		return workingDayPutReq;
	}

	private Zone getZone() {
		Zone z = new Zone();
		z.setCreatedBy("superadmin");
		z.setCreatedDateTime(LocalDateTime.now());
		z.setDeletedDateTime(null);
		z.setIsActive(true);
		z.setLangCode("eng");
		z.setUpdatedBy(null);
		z.setCode("NTH");
		z.setHierarchyLevel((short) 0);
		z.setHierarchyName("North");
		z.setHierarchyPath("/NTH");

		return z;
	}

	private RequestWrapper<MachineTypePutDto> machinetype() {
		RequestWrapper<MachineTypePutDto> machineTypePut = new RequestWrapper<MachineTypePutDto>();
		MachineTypePutDto dtop = new MachineTypePutDto();
		dtop.setCode("Vostro");
		dtop.setDescription("Vostro updated");
		dtop.setIsActive(true);
		dtop.setLangCode("eng");
		dtop.setName("Laptop 2");

		machineTypePut.setRequest(dtop);
		return machineTypePut;
	}

	private List<Zone> getZoneLst() {
		List l = new ArrayList();
		l.add(getZone());
		return l;
	}

	private RequestWrapper<DevicePutReqDto> devicePutDto() {
		RequestWrapper<DevicePutReqDto> devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
		DevicePutReqDto devicePutReqDto = new DevicePutReqDto();
		devicePutReqDto.setId("3000038");
		devicePutReqDto.setDeviceSpecId("327");
		devicePutReqDto.setIsActive(true);
		devicePutReqDto.setLangCode("eng");

		devicePutReqDto.setName("Mock Iris Scanner updted");
		devicePutReqDto.setMacAddress("85-BB-97-4B-14-05");
		devicePutReqDto.setRegCenterId("10001");
		devicePutReqDto.setSerialNum("3456789012");

		devicePutReqDto.setZoneCode("RBT");
		devicePutReqDtoReq.setRequest(devicePutReqDto);
		return devicePutReqDtoReq;
	}

	private List<Device> getDevices() {
		Device d = new Device();
		d.setCreatedBy("superuser");
		d.setDeviceSpecId("327");
		d.setId("10");
		d.setIpAddress("12.12.12.12");
		d.setName("Mock Iris Scanner updted");
		d.setMacAddress("85-BB-97-4B-14-05");
		d.setRegCenterId("10001");
		d.setSerialNum("3456789012");
		d.setZoneCode("NTH");
		d.setIsActive(true);
		d.setIsDeleted(null);
		d.setLangCode("eng");

		List<Device> dl = new ArrayList<>();
		dl.add(d);
		return dl;
	}

	private RequestWrapper<DocumentCategoryPutDto> docCategory() {
		RequestWrapper<DocumentCategoryPutDto> doPutCatDto = new RequestWrapper<DocumentCategoryPutDto>();
		DocumentCategoryPutDto doc = new DocumentCategoryPutDto();
		doPutCatDto.setRequest(doc);
		DocumentCategoryPutDto docCatDto = new DocumentCategoryPutDto();

		docCatDto.setCode("POA");
		docCatDto.setDescription("Address proof");
		docCatDto.setIsActive(true);
		docCatDto.setLangCode("eng");
		docCatDto.setName("Proof of Address");

		doPutCatDto.setRequest(docCatDto);
		return doPutCatDto;
	}

	private RequestWrapper<BlocklistedWordsDto> blockListedWordsRequestDto() {
		RequestWrapper<BlocklistedWordsDto> blockListedWordsRequestDto = new RequestWrapper<BlocklistedWordsDto>();
		BlocklistedWordsDto b = new BlocklistedWordsDto();
		b.setDescription("test");
		b.setIsActive(true);
		b.setLangCode("eng");
		b.setWord("test");
		blockListedWordsRequestDto.setRequest(b);
		return blockListedWordsRequestDto;

	}

	private RequestWrapper<DeviceSpecificationDto> deviceSpecification() {
		RequestWrapper<DeviceSpecificationDto> d = new RequestWrapper<DeviceSpecificationDto>();
		DeviceSpecificationDto devicespecitionDto = new DeviceSpecificationDto();
		devicespecitionDto.setBrand("Safran Morpho");
		devicespecitionDto.setDescription("To scan fingerprint");
		devicespecitionDto.setDeviceTypeCode("FRS");
		devicespecitionDto.setId("165");
		devicespecitionDto.setIsActive(true);
		devicespecitionDto.setLangCode("eng");
		devicespecitionDto.setMinDriverversion("1.12");
		devicespecitionDto.setModel("1300 E2");
		devicespecitionDto.setName("Fingerprint Scanner");

		d.setRequest(devicespecitionDto);
		return d;
	}

	private List<DeviceType> deviceTypes() {
		DeviceType dt = new DeviceType();
		dt.setCode("FRS");
		dt.setDescription("For capturing photo");
		dt.setIsActive(true);
		dt.setLangCode("eng");
		dt.setName("Camera");
		dt.setCreatedBy("superuser");
		dt.setCreatedDateTime(LocalDateTime.now());

		List<DeviceType> l = new ArrayList<>();
		l.add(dt);
		return l;

	}

	@Test //
	@WithUserDetails("global-admin")
	public void tst001getLocationHierarchyLevelTest4() throws Exception {
		when(locHierarchyRepo.findByLastUpdatedAndCurrentTimeStamp(Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/locationHierarchyLevels").param("lastUpdated", "2019-12-12T10:10:30.956Z"))
				.andReturn(), "KER-MSD-399");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst002updateLanguageStatusFailTest() throws Exception {
		when(languageRepository.findLanguageByCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/languages").param("code", "ara").param("isActive", "true"))
				.andReturn(), "KER-MSD-701");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001getRegistrationCentersMachineUserMappingTest2() throws Exception {
		Mockito.when(historyRepository
				.findByCntrIdAndMachineIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(machinesHistories);
		Mockito.when(userHistoryRepository
				.findByCntrIdAndUsrIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(usersHistories);

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10003/10001/100"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001getRegistrationCentersMachineUserMappingTest3() throws Exception {
		machinesHistories.get(0).setIsActive(null);
		Mockito.when(historyRepository
				.findByCntrIdAndMachineIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(machinesHistories);
		usersHistories.get(0).setIsActive(false);
		Mockito.when(userHistoryRepository
				.findByCntrIdAndUsrIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(usersHistories);

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10003/10001/100"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001getRegistrationCentersMachineUserMappingTest4() throws Exception {
		machinesHistories.get(0).setEffectDateTime(null);
		Mockito.when(historyRepository
				.findByCntrIdAndMachineIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(machinesHistories);
		usersHistories.get(0).setIsActive(true);
		Mockito.when(userHistoryRepository
				.findByCntrIdAndUsrIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(usersHistories);

		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10003/10001/100"))
				.andReturn());

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001getRegistrationCentersMachineUserMappingTest5() throws Exception {

		Mockito.when(historyRepository
				.findByCntrIdAndMachineIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(machinesHistories);
		usersHistories.get(0).setIsActive(true);
		usersHistories.get(0).setEffDTimes(null);
		Mockito.when(userHistoryRepository
				.findByCntrIdAndUsrIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(Mockito.any(),
						Mockito.any(), Mockito.any()))
				.thenReturn(usersHistories);

		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10003/10001/100"))
				.andReturn());

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst009updateFileFormatStatusTest() throws Exception {
		when(templateFileFormatRepository.findtoUpdateTemplateFileFormatByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/templatefileformats").param("isActive", "true").param("code", "json1"))
				.andReturn(), "KER-MSD-246");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst017updateDocumentTypeStatusFailTest() throws Exception {
		when(documentTypeRepository.findtoUpdateDocumentTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/documenttypes").param("isActive", "false").param("code", "CIN"))
				.andReturn(), "KER-MSD-015");

	}

	/*
	 * @Test issue
	 * 
	 * @WithUserDetails("global-admin") public void tst017updateDocumentTypeTest()
	 * throws Exception { RequestWrapper<DocumentTypePutReqDto>
	 * documentTypeupdateDtoReq = new RequestWrapper<DocumentTypePutReqDto>();
	 * DocumentTypePutReqDto dto1 = new DocumentTypePutReqDto();
	 * dto1.setCode("CIN1"); dto1.setDescription("Reference of Identity updated");
	 * dto1.setIsActive(true); dto1.setLangCode("eng");
	 * dto1.setName("Identity card"); documentTypeupdateDtoReq.setRequest(dto1);
	 * when(documentTypeRepository.update(Mockito.any())).thenThrow(new
	 * NoSuchFieldException("...") { }); MasterDataTest.checkErrorResponse(mockMvc
	 * .perform(MockMvcRequestBuilders.put("/documenttypes").contentType(MediaType.
	 * APPLICATION_JSON)
	 * .content(mapper.writeValueAsString(mapper.writeValueAsString(
	 * documentTypeupdateDtoReq)))) .andReturn());
	 * 
	 * }
	 */

	@Test
	@WithUserDetails("global-admin")
	public void tst002updateDocumentCategoryFailTest1() throws Exception {

		when(documentCategoryRepository.findByCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(docCategory()))).andReturn(),
				"KER-MSD-089");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst002updateRegistrationCenterTypeStatusFailTest1() throws Exception {
		when(registrationCenterTypeRepository.findtoUpdateRegistrationCenterTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/registrationcentertypes").param("code", "reg").param("isActive", "true"))
				.andReturn(), "KER-MSD-013");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst00updateDeviceTypeStatusTest2() throws Exception {
		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/devicetypes").param("code", "reg").param("isActive", "true"))
				.andReturn(), "KER-MSD-230");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001updateBlockListedWordStatusTest2() throws Exception {
		when(blocklistedWordsRepository.findtoUpdateBlocklistedWordByWord(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/blocklistedwords").param("word", "reg").param("isActive", "true"))
				.andReturn(), "KER-MSD-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001updateBlockListedWordExceptWordTest2() throws Exception {
		when(blocklistedWordsRepository.updateBlockListedWordDetails(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString())).thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.put("/blocklistedwords/details").contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(blockListedWordsRequestDto())))
						.andReturn(),
				"KER-MSD-105");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001TemplateUpdateTest2() throws Exception {
		when(templateRepository.findAllByTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/temp")).andReturn(),
				"KER-MSD-045");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001TemplateUpdateTest3() throws Exception {
		when(templateRepository.findtoUpdateTemplateById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/templates").param("isActive", "true").param("id", "test"))
				.andReturn(), "KER-MSD-045");
	}

	/*@Test
	@WithUserDetails("global-admin")
	public void tst001TemplateUpdateTest4() throws Exception {
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode", "eng"))
				.andReturn(), "KER-MSD-393");
	}*/

	@Test
	@WithUserDetails("global-admin")
	public void tst001getAllExceptionalHolidaysTest() throws Exception {
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(expHoliday.findDistinctByRegistrationCenterId(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10005")).andReturn(), "KER-EHD-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001mapDocCategoryAndDocTypeTest() throws Exception {
		when(vdocumentRepository.findByDocCategoryCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/POA1/eng")).andReturn(), "KER-MSD-205");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001mapDocCategoryAndDocTypeTest2() throws Exception {
		when(vdocumentRepository.deleteValidDocument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/validdocuments/POA1/eng")).andReturn(), "KER-MSD-113");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001mapDocCategoryAndDocTypeTest3() throws Exception {
		when(vdocumentRepository.updateDocCategoryAndDocTypeMapping(Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/POA1/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001unMapDocCategoryAndDocTypeTest3() throws Exception {
		when(vdocumentRepository.findByDocCategoryCodeAndDocTypeCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		when(vdocumentRepository.updateDocCategoryAndDocTypeMapping(Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA2/COR2")).andReturn(),
				"KER-MSD-205");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001unMapDocCategoryAndDocTypeTest4() throws Exception {
		when(vdocumentRepository.findByDocCategoryCodeAndDocTypeCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RequestException("KER-MSD-361", "...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA2/COR2")).andReturn(),
				"KER-MSD-361");
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("global-admin") public void
	 * tst001createDeviceSpecification4() throws Exception {
	 * when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString())).
	 * thenReturn(deviceTypes());
	 * when(deviceSpecificationRepository.create(Mockito.any())).thenThrow(new
	 * RequestException("KER-MSD-054","...") { });
	 * MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post(
	 * "/devicespecifications")
	 * .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(
	 * deviceSpecification()))) .andReturn(), "KER-MSD-054"); }
	 */

	@Test
	@WithUserDetails("global-admin")
	public void tst001createDeviceSpecification() throws Exception {
		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString())).thenReturn(deviceTypes());
		when(deviceSpecificationRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceSpecification())))
				.andReturn(), "KER-MSD-054");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001DeviceSpecificationUpdateStatus4() throws Exception {
		when(deviceSpecificationRepository.findtoUpdateDeviceSpecById(Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/devicespecifications").param("isActive", "true").param("id", "1"))
				.andReturn(), "KER-MSD-011");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst003updateMachineTypeTest() throws Exception {
		when(machineTypeRepository.findtoUpdateMachineTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/machinetypes")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machinetype()))).andReturn(),
				"KER-MSD-064");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateMachineTypeStatusTest2() throws Exception {
		when(machineTypeRepository.findtoUpdateMachineTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(machineSpecificationRepository
				.findMachineSpecificationByMachineTypeCodeAndLangCodeAndIsDeletedFalseorIsDeletedIsNull(
						Mockito.anyString())).thenThrow(new DataAccessException("...") {
						});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/machinetypes").param("code", "DKS").param("isActive", "false"))
				.andReturn(), "KER-MSD-064");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst008getAllDynamicFieldsTest() throws Exception {
		when(dynamicFieldRepository.findAllLatestDynamicFieldNames(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.get("/dynamicfields").param("pageNumber", "0").param("pageSize", "10")
								.param("sortBy", "name").param("orderBy", "desc").param("langCode", "eng"))
						.andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst008getDistinctDynamicFieldsBasedOnLangTest() throws Exception {
		when(dynamicFieldRepository.getDistinctDynamicFields()).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct")).andReturn(),
				"KER-SCH-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst008getDistinctDynamicFieldsBasedOnLangTest1() throws Exception {
		when(dynamicFieldRepository.getDistinctDynamicFieldsWithDescription())
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct/eng")).andReturn(), "KER-SCH-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001createDynamicFieldTest1() throws Exception {
		when(dynamicFieldRepository.findAllByFieldNameAndCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(dynamicFieldDtoReq())))
								.andReturn(),
						"KER-SCH-002");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001updateDynamicFieldTest1() throws Exception {

		when(dynamicFieldRepository.updateDynamicField(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyString()))
						.thenThrow(new DataAccessException("...") {
						});
		RequestWrapper<DynamicFieldPutDto> dynamicFieldPutDtoReq = new RequestWrapper<DynamicFieldPutDto>();
		DynamicFieldPutDto dynamicFieldPutDto = new DynamicFieldPutDto();
		dynamicFieldPutDto.setDataType("string");
		dynamicFieldPutDto.setDescription("BloodType");
		dynamicFieldPutDto.setLangCode("eng");
		dynamicFieldPutDto.setName("bloodtype");
		Map m = new HashMap<>();
		m.put("code", "anbc");
		m.put("value", "anbcval");
		JsonNode jsonNode = mapper.valueToTree(m);

		dynamicFieldPutDto.setFieldVal(jsonNode);
		dynamicFieldPutDtoReq.setRequest(dynamicFieldPutDto);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "10001")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-SCH-011");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst015deleteDynamicFieldTest() throws Exception {
		when(dynamicFieldRepository.findDynamicFieldById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/90")).andReturn(),
				"KER-SCH-020");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst015deleteDynamicFieldTest1() throws Exception {
		when(dynamicFieldRepository.deleteAllDynamicField(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/bloodType")).andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst011updateDynamicFieldStatusTest() throws Exception {
		when(dynamicFieldRepository.findDynamicFieldById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "false").param("id", "10001"))
				.andReturn(), "KER-SCH-011");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst011updateDynamicFieldStatusTest1() throws Exception {
		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.anyString())).thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "false").param("id", "10001"))
				.andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001getWeekDaysTest() throws Exception {
		when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(daysOfWeekRepo.findBylangCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/weekdays/10001/eng")).andReturn(),
				"KER-MSD-800");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst003getWorkindaysTest() throws Exception {
		when(registrationCenterRepository.countByIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenReturn((long) 2);
		when(workingDaysRepo.findByregistrationCenterIdAndlanguagecodeForWorkingDays(Mockito.anyString(),
				Mockito.any())).thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/10001/eng")).andReturn(),
				"KER-MSD-800");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst007updateWorkingDaysTest() throws Exception {
		when(daysOfWeekRepo.findBylangCodeAndCode(Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/workingdays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(workDay()))).andReturn(),
				"KER-MSD-800");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst007updateWorkingDaysTest1() throws Exception {
		when(daysOfWeekRepo.findBylangCodeAndCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/workingdays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(workDay()))).andReturn(),
				"KER-MSD-800");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst002createMachineSpecificationFailTest() throws Exception {
		RequestWrapper<MachineSpecificationDto> m = new RequestWrapper<MachineSpecificationDto>();

		MachineSpecificationDto dto = new MachineSpecificationDto();
		dto.setBrand("DELL");
		dto.setDescription("Dell brand");
		dto.setId("1002");
		dto.setLangCode("eng");
		dto.setMachineTypeCode("Vostro");

		dto.setMinDriverversion("1.3");
		dto.setModel("Dell");
		dto.setName("Dell");
		dto.setIsActive(true);

		m.setRequest(dto);
		when(machineSpecificationRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(m))).andReturn(),
				"KER-MSD-722");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst003updateMachineSpecificationTest() throws Exception {
		RequestWrapper<MachineSpecificationPutDto> machineSpecification = new RequestWrapper<MachineSpecificationPutDto>();
		when(machineSpecificationRepository.findMachineSpecById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MachineSpecificationPutDto dto1 = new MachineSpecificationPutDto();
		dto1.setBrand("DELLupdate");
		dto1.setDescription("Dell brandupdate");
		dto1.setId("1001");
		dto1.setLangCode("eng");
		dto1.setMachineTypeCode("Vostro");
		dto1.setMinDriverversion("1.3");
		dto1.setModel("Dell");
		dto1.setName("Dell");

		machineSpecification.setRequest(dto1);

		when(machineSpecificationRepository.update(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/machinespecifications")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineSpecification)))
				.andReturn(), "KER-MSD-722");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateMachineSpecificationStatusTest() throws Exception {
		when(machineSpecificationRepository.findMachineSpecById(Mockito.anyString()))
				.thenThrow(new RequestException("KER-MSD-017", "...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/machinespecifications").param("id", "1001").param("isActive", "true"))
				.andReturn(), "KER-MSD-017");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateMachineSpecificationStatusTest1() throws Exception {
		when(machineRepository.findMachineBymachineSpecIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(new RequestException("...", "...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/machinespecifications").param("id", "1001").param("isActive", "true"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateMachineSpecificationStatusTest2() throws Exception {
		when(machineRepository.findMachineBymachineSpecIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/machinespecifications").param("id", "1001").param("isActive", "true"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst006decommissionMachineTest1() throws Exception {
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		List lm = new ArrayList<>();
		Machine m = getMachines().get(0);
		m.setRegCenterId(null);
		lm.add(m);

		when(machineRepository.findMachineByIdAndIsDeletedFalseorIsDeletedIsNullNoIsActive(Mockito.anyString()))
				.thenReturn(lm);
		when(machineRepository.decommissionMachine(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines/decommission/40")).andReturn(), "KER-MSD-251");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst022createMachineTest1() throws Exception {
		List rl = new ArrayList();
		getRegistrationCenter().setZoneCode("NTH");
		rl.add(getRegistrationCenter());
		List zl = new ArrayList();
		zl.add(getZoneUser());
		when(zoneUserRepo.findByUserIdNonDeleted(Mockito.any())).thenReturn(zl);
	
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(rl);
		List<Zone> zlst=getZoneLst();
		
		zlst.get(0).setHierarchyPath("/N");
		when(zoneRepository.findAllNonDeleted()).thenReturn(zlst);
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString())).thenReturn(rl);
		when(machineRepository.findByMachineName(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<MachinePostReqDto> machineRequest = new RequestWrapper<>();
		MachinePostReqDto dto = new MachinePostReqDto();
		// dto.setId("50");
		dto.setIpAddress("192.168.0.122");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setMacAddress("E8-A9-64-1F-27-E6");
		dto.setMachineSpecId("1001");
		dto.setName("machine11");
		dto.setPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		dto.setRegCenterId("10001");
		dto.setSerialNum("NM10037379");
		dto.setSignPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		dto.setZoneCode("NTH");
		machineRequest.setRequest(dto);
		when(machineRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-250");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst022createMachineTest() throws Exception {
		List rl = new ArrayList();
		getRegistrationCenter().setZoneCode("NTH");
		rl.add(getRegistrationCenter());
		List zl = new ArrayList();
		zl.add(getZoneUser());
		when(zoneUserRepo.findByUserIdNonDeleted(Mockito.any())).thenReturn(zl);
	
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString())).thenReturn(rl);
		when(machineRepository.findByMachineName(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<MachinePostReqDto> machineRequest = new RequestWrapper<>();
		MachinePostReqDto dto = new MachinePostReqDto();
		// dto.setId("50");
		dto.setIpAddress("192.168.0.122");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setMacAddress("E8-A9-64-1F-27-E6");
		dto.setMachineSpecId("1001");
		dto.setName("machine11");
		dto.setPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		dto.setRegCenterId("10001");
		dto.setSerialNum("NM10037379");
		dto.setSignPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		dto.setZoneCode("NTH");
		machineRequest.setRequest(dto);
		when(machineRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineRequest))).andReturn(),
				"KER-MSD-250");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst024updateMachineAdminTest() throws Exception {
		RequestWrapper<MachinePutReqDto> machineCenterDto = new RequestWrapper<MachinePutReqDto>();

		MachinePutReqDto dto2 = new MachinePutReqDto();
		dto2.setId("10");
		dto2.setIpAddress("192.168.0.122");
		dto2.setIsActive(true);
		dto2.setLangCode("eng");
		dto2.setMacAddress("E8-A9-64-1F-27-E6");
		dto2.setMachineSpecId("1001");
		dto2.setName("machine11");
		dto2.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto2.setRegCenterId("10001");
		dto2.setSerialNum("NM10037379");
		dto2.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto2.setZoneCode("NTH");
		machineCenterDto.setRequest(dto2);

		List rl = new ArrayList();
		rl.add(getRegistrationCenter());
		List zl = new ArrayList();
		zl.add(getZoneUser());
		when(zoneUserRepo.findByUserIdNonDeleted(Mockito.any())).thenReturn(zl);
	
		when(registrationCenterRepository.findByRegId("10001")).thenReturn(rl);
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull("10001")).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted("global-admin")).thenReturn(getZoneUser());

		when(machineRepository.findMachineById(Mockito.any())).thenReturn(null);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				"KER-MSD-030");
	}

	public void tst024updateMachineAdminTest4() throws Exception {
		when(machineRepository.findMachineById(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<MachinePutReqDto> machineCenterDto = new RequestWrapper<MachinePutReqDto>();

		MachinePutReqDto dto2 = new MachinePutReqDto();
		dto2.setId("10");
		dto2.setIpAddress("192.168.0.122");
		dto2.setIsActive(true);
		dto2.setLangCode("eng");
		dto2.setMacAddress("E8-A9-64-1F-27-E6");
		dto2.setMachineSpecId("1001");
		dto2.setName("machine11");
		dto2.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");
		dto2.setRegCenterId("10001");
		dto2.setZoneCode("NTH");
		dto2.setSerialNum("NM10037379");
		dto2.setSignPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq5TnNAD1FMWWektYigmUMGw");

		machineCenterDto.setRequest(dto2);
		List l = new ArrayList();
		l.add(getZone());
		List rl = new ArrayList();
		getRegistrationCenter().setZoneCode("NTH");
		rl.add(getRegistrationCenter());
		when(registrationCenterRepository.findByRegId("10001")).thenReturn(rl);
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull("10001")).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(l);
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted("global-admin")).thenReturn(getZoneUser());

		when(machineRepository.findMachineById(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machines").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(machineCenterDto))).andReturn(),
				"KER-MSD-219");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst008updateMachineStatusTest() throws Exception {
		when(machineRepository.findMachineById(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machines").param("id", "10").param("isActive", "true"))
						.andReturn(),
				"KER-MSD-252");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst017decommissionDeviceTest1() throws Exception {
		List<Device> d = getDevices();
		d.get(0).setRegCenterId(null);
		List<Device> d1 = new ArrayList<>();
		d1.add(d.get(0));
		when(deviceRepository.findDeviceByIdAndIsDeletedFalseorIsDeletedIsNullNoIsActive(Mockito.anyString()))
				.thenReturn(d1);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(deviceRepository.decommissionDevice(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices/decommission/3000058")).andReturn(),
				"KER-MSD-084");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst004updateDeviceTest() throws Exception {
		List rl = new ArrayList();
		getRegistrationCenter().setZoneCode("NTH");
		rl.add(getRegistrationCenter());
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		when(deviceRepository.update(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<DevicePutReqDto> devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
		DevicePutReqDto devicePutReqDto = new DevicePutReqDto();
		devicePutReqDto.setId("3000038");
		devicePutReqDto.setDeviceSpecId("327");
		devicePutReqDto.setIsActive(true);
		devicePutReqDto.setLangCode("eng");

		devicePutReqDto.setName("Mock Iris Scanner updted");
		devicePutReqDto.setMacAddress("85-BB-97-4B-14-05");
		// devicePutReqDto.setRegCenterId("10001");
		devicePutReqDto.setSerialNum("3456789012");

		devicePutReqDto.setZoneCode("NTH");
		devicePutReqDtoReq.setRequest(devicePutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-083");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst004updateDeviceTest1() throws Exception {
		List rl = new ArrayList();
		RegistrationCenter rc = getRegistrationCenter();
		rc.setZoneCode("NTH");//RTH
		rl.add(rc);
		List zl = new ArrayList();
		zl.add(getZoneUser());
		when(zoneUserRepo.findByUserIdNonDeleted(Mockito.any())).thenReturn(zl);
		
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString())).thenReturn(rl);
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		when(deviceRepository.update(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<DevicePutReqDto> devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
		DevicePutReqDto devicePutReqDto = new DevicePutReqDto();
		devicePutReqDto.setId("3000038");
		devicePutReqDto.setDeviceSpecId("327");
		devicePutReqDto.setIsActive(true);
		devicePutReqDto.setLangCode("eng");

		devicePutReqDto.setName("Mock Iris Scanner updted");
		devicePutReqDto.setMacAddress("85-BB-97-4B-14-05");
		devicePutReqDto.setRegCenterId("10001");
		devicePutReqDto.setSerialNum("3456789012");

		devicePutReqDto.setZoneCode("NTH");
		devicePutReqDtoReq.setRequest(devicePutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-083");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst004updateDeviceTest2() throws Exception {
		List rl = new ArrayList();
		RegistrationCenter rc = getRegistrationCenter();
		rc.setZoneCode("RTH");
		rl.add(rc);
		List zl = new ArrayList();
		zl.add(getZoneUser());
		when(zoneUserRepo.findByUserIdNonDeleted(Mockito.any())).thenReturn(zl);
		
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString())).thenReturn(rl);
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(rl);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(deviceRepository.findtoUpdateDeviceById(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		when(deviceRepository.update(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<DevicePutReqDto> devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
		DevicePutReqDto devicePutReqDto = new DevicePutReqDto();
		devicePutReqDto.setId("3000038");
		devicePutReqDto.setDeviceSpecId("327");
		devicePutReqDto.setIsActive(true);
		devicePutReqDto.setLangCode("eng");

		devicePutReqDto.setName("Mock Iris Scanner updted");
		devicePutReqDto.setMacAddress("85-BB-97-4B-14-05");
		devicePutReqDto.setRegCenterId("10001");
		devicePutReqDto.setSerialNum("3456789012");

		devicePutReqDto.setZoneCode("NTH");
		devicePutReqDtoReq.setRequest(devicePutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-219");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst008updateDeviceStatusTest() throws Exception {
		when(deviceRepository.findtoUpdateDeviceById(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/devices").param("isActive", "true").param("id", "3000038"))
				.andReturn(), "KER-MSD-009");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001mapUserRegCenterTest() throws Exception {
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		RequestWrapper<UserDetailsDto> ud = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10001");
		detailsDto.setStatusCode("Act");
		ud.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(ud))).andReturn(),
				"KER-MSD-041");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst015validateTimestampTest1() throws Exception {
		when(registrationCenterRepository.findByIdAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(registrationCenterRepository.validateDateWithHoliday(Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/10001/eng/2019-12-10T13:09:19.695Z"))
				.andReturn(), "KER-MSD-041");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst022decommissionRegCenterTest5() throws Exception {
		when(registrationCenterRepository.decommissionRegCenter(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/100031")).andReturn(),
				"KER-MSD-353");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst001mapUserZoneTest() throws Exception {
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new IllegalArgumentException("...") {
				});
		when(zoneUserHistoryRepo.create(Mockito.any())).thenThrow(new IllegalArgumentException("...") {
		});
		RequestWrapper<ZoneUserDto> zoneUserDto = new RequestWrapper<ZoneUserDto>();
		ZoneUserDto dto = new ZoneUserDto();
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setUserId("3");
		dto.setZoneCode("NTH");

		zoneUserDto.setRequest(dto);

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserDto))).andReturn(),
				"KER-USR-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateapUserZoneStatusTest1() throws Exception {
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		when(zoneUserHistoryRepo.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/zoneuser").param("isActive", "true").param("userId", "7"))
				.andReturn(), "KER-USR-020");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst003updateapUserZoneTest() throws Exception {
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(zoneUserRepo.findByUserId(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		 RequestWrapper<ZoneUserPutDto> zoneUserPutDto =new RequestWrapper<ZoneUserPutDto>();
		ZoneUserPutDto putDto = new ZoneUserPutDto();
		putDto.setIsActive(true);
		putDto.setLangCode("eng");
		putDto.setUserId("3");
		putDto.setZoneCode("NTH");
		zoneUserPutDto.setRequest(putDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/zoneuser").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(zoneUserPutDto))).andReturn(),
				"KER-USR-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005deleteMapUserZoneTest() throws Exception {

		when(zoneUserRepo.findByUserIdAndZoneCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(zoneUserHistoryRepo.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/zoneuser/3/CST")).andReturn(),
				"KER-USR-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst013getHistoryByUserIdAndTimestampTest1() throws Exception {
		when(zoneUserHistoryRepo.getByUserIdAndTimestamp(Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/zoneuser/history/5/2021-11-16tst05:12:27.164Z")).andReturn(),
				"KER-USR-002");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst013getHistoryByUserIdAndTimestampTest() throws Exception {
		when(zoneUserHistoryRepo.getByUserIdAndTimestamp(Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/zoneuser/history/5/2021-12-13T05:12:27.164Z")).andReturn(),
				"KER-USR-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateapUserZoneStatusTest4() throws Exception {
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/zoneuser").param("isActive", "true").param("userId", "7"))
				.andReturn(), "KER-USR-020");
	}

	public void tst005createLocationHierarchyDetailsTest1() throws Exception {
		when(locReg.findLocationHierarchyByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new IllegalArgumentException("...") {
				});

		RequestWrapper<LocationCreateDto> locationCreateDtoReq = new RequestWrapper<LocationCreateDto>();
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Country");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("");

		locationCreateDtoReq.setRequest(createDto);
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(locationCreateDtoReq)))
								.andReturn(),
						"KER-MSD-242");
	}

	public void tst005createLocationHierarchyDetailsTest() throws Exception {
		when(locReg.findLocationHierarchyByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		RequestWrapper<LocationCreateDto> locationCreateDtoReq = new RequestWrapper<LocationCreateDto>();
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Country");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("");

		locationCreateDtoReq.setRequest(createDto);
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(locationCreateDtoReq)))
								.andReturn(),
						"KER-MSD-242");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst021updateLocationStatusFailTest() throws Exception {
		when(locReg.findLocationByCode(Mockito.anyString())).thenThrow(new IllegalArgumentException("..."));
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/locations").param("code", "10099").param("isActive", "false"))
				.andReturn(), "KER-MSD-097");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst017locationFilterValuesTest() throws Exception {
		FilterValueDto f = new FilterValueDto();
		FilterDto fdto = new FilterDto();
		fdto.setColumnName("code");
		fdto.setText("RSK");
		fdto.setType("all");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(fdto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		RequestWrapper<FilterValueDto> filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);
		when(locReg.findLocationAllHierarchyNames())
				.thenThrow(new DataAccessLayerException("...", "...", new Throwable()));
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("global-admin") public void
	 * tst023updateLocationHierarchyDetailsTest() throws Exception {
	 * RequestWrapper<LocationPutDto> locationRequestDto=new
	 * RequestWrapper<LocationPutDto>(); LocationPutDto dto = new LocationPutDto();
	 * dto.setCode("11111"); dto.setHierarchyLevel((short) 1);
	 * dto.setIsActive(true); dto.setHierarchyName("Postal Code");
	 * dto.setLangCode("eng"); dto.setName("11111"); //dto.setParentLocCode("QRHS");
	 * locationRequestDto.setRequest(dto);
	 * 
	 * when(locHierarchyRepo.findByLangCodeAndLevelAndName(Mockito.anyString(),
	 * Mockito.any(),Mockito.anyString())).thenThrow(new
	 * IllegalArgumentException("KER-MSD-097") {}); MasterDataTest.checkResponse(
	 * mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(
	 * MediaType.APPLICATION_JSON)
	 * .content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
	 * "KER-MSD-027"); }
	 */

	@Test
	@WithUserDetails("reg-processor")
	public void tst003getUsersTest() throws Exception {
		when(userDetailsRepository.findAllByIsDeletedFalseorIsDeletedIsNull(Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkErrorResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/0/1/cr_dtimes/DESC")).andReturn());

	}



	@Test
	@WithUserDetails("global-admin")
	public void getAllSchema() throws Exception {
		Mockito.when(identitySchemaRepository.findAllIdentitySchema(Mockito.anyBoolean(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/all")).andReturn(),
				"KER-SCH-004");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchema() throws Exception {
		Mockito.when(uiSpecService.getUISpec(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lstui);
		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(is);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest")
				.param("schemaVersion", "0").param("domain", "").param("type", "")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchema1() throws Exception {
		IdentitySchema is = null;

		Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).thenReturn(is);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest")
				.param("schemaVersion", "0").param("domain", "").param("type", "")).andReturn(), "KER-SCH-007");
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("global-admin") public void getLatestPublishedSchema2()
	 * throws Exception {
	 * 
	 * Mockito.when(uiSpecService.getUISpec(Mockito.any(),Mockito.any(),Mockito.any(
	 * ))).thenReturn(lstui);
	 * Mockito.when(identitySchemaRepository.findLatestPublishedIdentitySchema()).
	 * thenReturn(is);
	 * MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get(
	 * "/idschema/latest").param("schemaVersion", "0").param("domain",
	 * "").param("type","a")).andReturn(),null); }
	 */

	@Test
	@WithUserDetails("global-admin")
	public void tst001createMachineSpecificationTest() throws Exception {
		RequestWrapper<MachineSpecificationDto> m = new RequestWrapper<MachineSpecificationDto>();
		MachineSpecificationDto dto = new MachineSpecificationDto();
		dto.setBrand("DELL");
		dto.setDescription("Dell brand");
		dto.setId("1002");
		dto.setLangCode("eng");
		dto.setMachineTypeCode("Vostro");
		dto.setMinDriverversion("1.3");
		dto.setModel("Dell");
		dto.setName("Dell");
		dto.setIsActive(true);

		m.setRequest(dto);
		List<MachineType> mt = new ArrayList<>();
		MachineType mtype = new MachineType();
		mtype.setCode("test");
		mtype.setDescription("test");
		mt.add(mtype);
		when(machineTypeRepository.findtoUpdateMachineTypeByCode(Mockito.anyString())).thenReturn(mt);
		when(machineSpecificationRepository.findMachineSpecById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		when(machineSpecificationRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(m))).andReturn(),
				"KER-MSD-258");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst003updateMachineSpecificationTest1() throws Exception {
		List<MachineType> mt = new ArrayList<>();
		MachineType mtype = new MachineType();
		mtype.setCode("test");
		mtype.setDescription("test");
		mt.add(mtype);
		when(machineTypeRepository.findtoUpdateMachineTypeByCode(Mockito.anyString())).thenReturn(mt);

		RequestWrapper<MachineSpecificationPutDto> machineSpecification = new RequestWrapper<MachineSpecificationPutDto>();
		when(machineSpecificationRepository.findMachineSpecById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MachineSpecificationPutDto dto1 = new MachineSpecificationPutDto();
		dto1.setBrand("DELLupdate");
		dto1.setDescription("Dell brandupdate");
		dto1.setId("1001");
		dto1.setLangCode("eng");
		dto1.setMachineTypeCode("Vostro");
		dto1.setMinDriverversion("1.3");
		dto1.setModel("Dell");
		dto1.setName("Dell");

		machineSpecification.setRequest(dto1);

		when(machineSpecificationRepository.update(Mockito.any())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/machinespecifications")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineSpecification)))
				.andReturn(), "KER-MSD-085");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst005updateMachineSpecificationStatusTest3() throws Exception {
		when(machineSpecificationRepository.findMachineSpecById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/machinespecifications").param("id", "1001").param("isActive", "false"))
				.andReturn(), "KER-MSD-085");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006getLatestPublishedSchemaTest() throws Exception {
		when(identitySchemaRepository.findIdentitySchemaById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion", "1.1")).andReturn(),
				"KER-SCH-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createSchemaTest() throws Exception {
		when(identitySchemaRepository.create(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<IdentitySchemaDto> schema = new RequestWrapper<>();
		IdentitySchemaDto request = new IdentitySchemaDto();
		request.setDescription("mp test");
		request.setSchema("{\"schema\":\"schema\"}");
		request.setTitle("mp test");
		schema.setRequest(request);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/idschema")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema))).andReturn(),
				"KER-SCH-005");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002updateSchemaTest() throws Exception {
		IdentitySchema ischema = new IdentitySchema();
		ischema.setId("test");
		ischema.setIdVersion(1.0);
		ischema.setTitle("test");
		ischema.setDescription("test");
		ischema.setSchemaJson("test");
		when(identitySchemaRepository.findIdentitySchemaById(Mockito.any())).thenReturn(ischema);
		RequestWrapper<IdentitySchemaDto> schema = new RequestWrapper<>();
		IdentitySchemaDto request = new IdentitySchemaDto();
		request.setDescription("mp test");
		request.setSchema("{\"schema\":\"schema\"}");
		request.setTitle("mp test");
		schema.setRequest(request);
		when(identitySchemaRepository.save(Mockito.any())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/idschema").param("id", "1")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema)))
				.andReturn(), "KER-SCH-006");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003publishSchemaTest1() throws Exception {
		when(identitySchemaRepository.findIdentitySchemaById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		RequestWrapper<IdSchemaPublishDto> idSchemaPublishDto = new RequestWrapper<IdSchemaPublishDto>();

		IdSchemaPublishDto dto = new IdSchemaPublishDto();
		dto.setId("2");
		dto.setEffectiveFrom(LocalDateTime.now());
		idSchemaPublishDto.setRequest(dto);
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.put("/idschema/publish").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(idSchemaPublishDto)))
								.andReturn(),
						"KER-SCH-006");
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void tst001getUserdetailsByLangIdAndEffTimeTest1() throws Exception {
		when(userHistoryRepository.getByUserIdAndTimestamp(Mockito.anyString(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/1/2023-12-10T11:42:52.994Z")).andReturn(),
				"KER-USR-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateDeviceTypeTest() throws Exception {

		RequestWrapper<DeviceTypePutDto> filPutDto = new RequestWrapper<DeviceTypePutDto>();
		DeviceTypePutDto dp = new DeviceTypePutDto();
		dp.setCode("CMR1");
		dp.setDescription("For capturing photo");
		dp.setIsActive(true);
		dp.setLangCode("eng");
		dp.setName("Camera");
		filPutDto = new RequestWrapper<DeviceTypePutDto>();
		filPutDto.setRequest(dp);
		when(deviceTypeRepository.findtoUpdateDeviceTypeByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicetypes")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filPutDto))).andReturn(),
				"KER-MSD-231");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t3getExceptionalHolidaysFailTest() throws Exception {
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001/eng")).andReturn(),
				"KER-EHD-001");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009updateWorkingDaysStatusTest() throws Exception {
		when(daysOfWeekRepo.findByCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/workingdays").param("code", "101").param("isActive", "true"))
				.andReturn(), "KER-MSD-800");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst003updateHolidayTest3() throws Exception {

		when(locReg.findByCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2021-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"1\",\n" + "    \"locationCode\": \"KTAA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"holidayName\": \"May day\",\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"holidayDesc\": \"National holiday\"\n"
								+ "  }\n" + "}"))

				.andReturn(), "KER-MSD-731");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst011searchMachineTest1() throws Exception {
		when(locReg.findByLangCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});
		RequestWrapper<SearchDto> searchDtoReq = new RequestWrapper<SearchDto>();
		Pagination pagination = new Pagination(0, 1);
		List<SearchSort> ss = new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		sf.setColumnName("holidayName");
		sf.setType("equals");
		sf.setValue("New Year Day");
		ls.add(sf);
		SearchSort s = new SearchSort("holidayName", "ASC");
		SearchDto sd = new SearchDto();
		sd.setFilters(ls);
		sd.setLanguageCode("eng");
		sd.setPagination(pagination);
		ss.add(s);
		sd.setSort(ss);

		searchDtoReq.setRequest(sd);
		searchDtoReq.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				"KER-MSD-025");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007getAllTemplateByTemplateTypeCodeFailTest() throws Exception {

		when(templateRepository.findAllByTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EM")).andReturn(),
				"KER-MSD-045");
	}

	@Test
	public void tst0getTitlesBylangCodeNotFound() throws Exception {
		when(titleRepository.getThroughLanguageCode(Mockito.anyString())).thenThrow(new DataAccessException("...") {
		});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/title/tam")).andReturn(),
				"KER-MSD-047");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst015deleteDeviceSpecificationTest() throws Exception {
		when(deviceSpecificationRepository.findDeviceSpecById(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/devicespecifications/165")).andReturn(), "KER-MSD-082");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015updateDocumentCategoryStatusTest() throws Exception {
		when(documentCategoryRepository.findtoUpdateDocumentCategoryByCode(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/documentcategories").param("isActive", "true").param("code", "POI"))
				.andReturn(), "KER-MSD-089");

	}

	@Test
	@WithUserDetails("global-admin")
	public void tst009mapDocCategoryAndDocTypeTest1() throws Exception {
		when(validDocumentRepository.findByDocCategoryCodeAndDocTypeCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/CIN")).andReturn(), "KER-MSD-205");
	}

	@Test
	@WithUserDetails("global-admin")
	public void tst009mapDocCategoryAndDocTypeTest2() throws Exception {
		ValidDocument validDocument = new ValidDocument();
		validDocument.setIsActive(false);

		when(validDocumentRepository.findByDocCategoryCodeAndDocTypeCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(validDocument);
		when(validDocumentRepository.updateDocCategoryAndDocTypeMapping(Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(0);

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/CIN1")).andReturn(), "KER-MSD-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007unmapDocCategoryAndDocTypeTest() throws Exception {
		ValidDocument validDocument = new ValidDocument();
		validDocument.setIsActive(true);
		when(validDocumentRepository.findByDocCategoryCodeAndDocTypeCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(validDocument);
		when(validDocumentRepository.updateDocCategoryAndDocTypeMapping(Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(0);

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA/COR")).andReturn(),
				"KER-MSD-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018authorizeZoneTest() throws Exception {
		when(registrationCenterRepository.findByIdAndIsDeletedFalseOrNull(Mockito.anyString()))
				.thenThrow(new DataAccessException("...") {
				});
		MasterDataTest.checkErrorResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/zones/authorize").param("rid", "10001")).andReturn());

	}

	@Test
	@WithUserDetails("global-admin")
	public void t015deleteDynamicFieldTest() throws Exception {

		DynamicField dynamicFieldDto = new DynamicField();
		dynamicFieldDto.setDataType("string");
		dynamicFieldDto.setDescription("Blood Type");
		dynamicFieldDto.setLangCode("eng");
		dynamicFieldDto.setName("blood type");

		dynamicFieldDto.setValueJson("{\"code\":\"oo\",\"value\":\"ooo\"}");
		when(dynamicFieldRepository.findDynamicFieldById(Mockito.anyString())).thenReturn(dynamicFieldDto);
		when(dynamicFieldRepository.deleteDynamicField(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any())).thenReturn(0);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/10001")).andReturn(),
				"KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015deleteDynamicFieldTest1() throws Exception {
		when(dynamicFieldRepository.deleteAllDynamicField(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenThrow(new DataAccessException("...") {
				});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/10001")).andReturn(),
				"KER-SCH-003");
	}

	/*@Test
	@WithUserDetails("global-admin")
	public void tst011updateDyamicFieldStatusTest() throws Exception {
		when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.any())).thenThrow(new NullPointerException("KER-SCH-011") {
				});
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "false").param("id", "10001"))
				.andReturn(), "KER-SCH-011");
	}*/
	
	@Test
	@WithUserDetails("global-admin")
	public void tst005createLocationHierarchyDetailsTest4() throws Exception {
		 RequestWrapper<LocationCreateDto> locationCreateDtoReq=new RequestWrapper<>();
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 1);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Region");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("");
		
		locationCreateDtoReq.setRequest(createDto);
		when(locReg.findLocationHierarchyByCodeAndLanguageCode(Mockito.any(),Mockito.any())).thenThrow(new DataAccessException("...") {});
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-244");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst005createLocationHierarchyDetailsTest5() throws Exception {
		 RequestWrapper<LocationCreateDto> locationCreateDtoReq=new RequestWrapper<>();
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Region");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("Loc");
		
		locationCreateDtoReq.setRequest(createDto);
		when(locReg.findLocationHierarchyByCodeAndLanguageCode(Mockito.any(),Mockito.any())).thenThrow(new IllegalArgumentException("...") {});
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-242");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t027updateLocationHierarchyDetailsTest() throws Exception {
		
		RequestWrapper<LocationPutDto> locationRequestDto=new RequestWrapper<LocationPutDto>();
		LocationPutDto dto = new LocationPutDto();
		dto.setCode("11111");
		dto.setHierarchyLevel((short) 1);
		dto.setIsActive(true);
		dto.setHierarchyName("Postal Code");
		dto.setLangCode("eng");
		dto.setName("11111");
		dto.setParentLocCode("QRHS");
		locationRequestDto.setRequest(dto);

		when(locReg.findLocationHierarchyByCodeAndLanguageCode(Mockito.any(),Mockito.any())).thenThrow(new IllegalArgumentException("...") {});
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-097");
	}

	@Test
	@WithUserDetails("reg-processor")
	public void tst003getUsersTest2() throws Exception {
		when(userDetailsRepository.findAllByIsDeletedFalseorIsDeletedIsNull(Mockito.any())).thenReturn(null);
		
		MasterDataTest.checkErrorResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/0/1/cr_dtimes/DESC")).andReturn());

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t010updateUserRegCenterStatusTest1() throws Exception {
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString())).thenThrow(new DataAccessException("...") {});

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/usercentermapping").param("isActive", "false").param("id", "4"))
				.andReturn(), "KER-USR-007");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010updateUserRegCenterStatusTest2() throws Exception {
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString())).thenReturn(null);

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/usercentermapping").param("isActive", "false").param("id", "4"))
				.andReturn(), "KER-USR-007");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t012deleteUserRegCenterMappingTest() throws Exception {
		when(userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString())).thenThrow(new DataAccessException("...") {});

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/usercentermapping/2")).andReturn(),
				"KER-USR-005");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001mapUserRegCenterTest1() throws Exception {
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		RequestWrapper<UserDetailsDto> ud = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("79");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10001");
		detailsDto.setStatusCode("Act");
		ud.setRequest(detailsDto);
	
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(ud))).andReturn(),
				"KER-USR-005");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest4() throws Exception {
		
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		RequestWrapper<UserDetailsPutReqDto> udp = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("7");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10001");
		detailsPutReqDto.setStatusCode("Act");
		udp.setRequest(detailsPutReqDto);
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(udp))).andReturn(),
				"KER-USR-005");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst023decommissionRegCenterFailTest() throws Exception {
		List<RegistrationCenter> regCenters =new ArrayList<>();
		RegistrationCenter centerPostReqDto=new RegistrationCenter();
		centerPostReqDto.setAddressLine1("add1");
		centerPostReqDto.setAddressLine2("add2");
		centerPostReqDto.setAddressLine3("add3");
		centerPostReqDto.setCenterEndTime(LocalTime.NOON);
		centerPostReqDto.setCenterStartTime(LocalTime.MIDNIGHT);
		centerPostReqDto.setCenterTypeCode("REG");
		centerPostReqDto.setContactPerson("Magic");
		centerPostReqDto.setContactPhone("1234567891");
		centerPostReqDto.setZoneCode("NTH");
		centerPostReqDto.setWorkingHours("8");
		centerPostReqDto.setTimeZone("(GTM+01:00) CENTRAL EUROPEAN TIME");
		centerPostReqDto.setNumberOfKiosks((short)2);
		centerPostReqDto.setPerKioskProcessTime(LocalTime.NOON);
		centerPostReqDto.setName("Mysore road");
		centerPostReqDto.setLocationCode("14022");
		regCenters.add(centerPostReqDto);
		when(registrationCenterRepository.findByRegId(Mockito.anyString())).thenReturn(regCenters);
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());

		when(userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10103")).andReturn(),
				"KER-MSD-354");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t028getCenterSpecificToZoneTest() throws Exception {
		when(zoneRepository.findAllNonDeleted()).thenReturn(getZoneLst());
		when(zoneUserRepo.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());

		when(registrationCenterRepository.findAllActiveByZoneCodeAndLangCode(Mockito.anyString(),Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getzonespecificregistrationcenters/eng/NTH")).andReturn(),
				"KER-MSD-041");

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void tst022getDynamicFieldByNameTest1() throws Exception {
		when( dynamicFieldRepository.findAllDynamicFieldByNameLangCodeAndisDeleted(Mockito.anyString(), Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/blod/eng")).andReturn(),
				"KER-SCH-001");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void tst009getZoneNameBasedOnUserIDAndLangCodeTest() throws Exception {
		when(zoneUserRepo.count()).thenReturn((long) 1);
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(null);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode","eng")).andReturn(), "KER-MSD-391");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst009getZoneNameBasedOnUserIDAndLangCodeTest1() throws Exception {
		when(zoneUserRepo.count()).thenReturn((long) 1);
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode","eng")).andReturn(), "KER-MSD-392");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst009getZoneNameBasedOnUserIDAndLangCodeTest2() throws Exception {
		when(zoneUserRepo.count()).thenReturn((long) 1);
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode","eng")).andReturn(), "KER-MSD-392");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void tst009getZoneNameBasedOnUserIDAndLangCodeTest3() throws Exception {
		when(zoneUserRepo.count()).thenReturn((long) 1);
		when(zoneUserRepo.findZoneByUserIdNonDeleted(Mockito.anyString())).thenReturn(getZoneUser());
		when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(Mockito.anyString(), Mockito.anyString())).thenThrow(new DataAccessException("...") {});
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode","eng")).andReturn(), "KER-MSD-393");

	}
	
	
	/*@Test
	@WithUserDetails("global-admin")
	public void tst019getRegistrationCenterByHierarchyLevelAndListTextAndlangCodeTest1() throws Exception {
		Location l=new Location();
		List<Location> lts=new ArrayList<>(); 
		when(locReg.getAllLocationsByLangCodeAndLevel(Mockito.anyString(),Mockito.any())).thenReturn(lts);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/2/names?name=MyCountry")).andReturn(),
				"KER-MSD-215");

	}
	*/

	@Test
	@WithUserDetails("reg-processor")
	public void tst003getUsersTest3() throws Exception {
		when(userDetailsRepository.findAllByIsDeletedFalseorIsDeletedIsNull(Mockito.any())).thenReturn(null);
		MasterDataTest.checkErrorResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/0/1/cr_dtimes/DESC")).andReturn());

	}
	
}
