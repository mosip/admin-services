package io.mosip.kernel.syncdata.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.core.exception.FileNotFoundException;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.SyncDataResponseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.repository.*;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
import io.mosip.kernel.syncdata.service.impl.SyncMasterDataServiceImpl;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest(classes = TestBootApplication.class)
@RunWith(MockitoJUnitRunner.class)
@DirtiesContext
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class SyncMasterDataServiceHelperTest {

    @Mock
    private SyncMasterDataServiceHelper syncMasterDataServiceHelper;

    @Mock
    private AppAuthenticationMethodRepository appAuthenticationMethodRepository;

    @Mock
    private AppRolePriorityRepository appRolePriorityRepository;

    @Mock
    private ApplicantValidDocumentRespository applicantValidDocumentRespository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private BlocklistedWordsRepository blocklistedWordsRepository;

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private MapperUtils mapperUtils;

    @Mock
    private PermittedLocalConfigRepository permittedLocalConfigRepository;

    @Mock
    private ProcessListRepository processListRepository;

    @Mock
    private ReasonCategoryRepository reasonCategoryRepository;

    @Mock
    private ReasonListRepository reasonListRepository;

    @Mock
    private RegistrationCenterRepository registrationCenterRepository;

    @Mock
    private ScreenAuthorizationRepository screenAuthorizationRepository;

    @Mock
    private ScreenDetailRepository screenDetailRepository;

    @Mock
    private SyncJobDefRepository syncJobDefRepository;

    @Mock
    private TemplateFileFormatRepository templateFileFormatRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TemplateTypeRepository templateTypeRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private ValidDocumentRepository validDocumentRepository;

    @Mock
    private SyncMasterDataServiceImpl syncMasterDataService;

    @Mock
    private SyncJobHelperService syncJobHelperService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate selfTokenRestTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Value("${mosip.kernel.keymanager-service-sign-url}")
    private String signUrl;

    @Value("${mosip.kernel.masterdata.locationhierarchylevels.uri}")
    private String locationHirerarchyUrl;

    @Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
    private String dynamicfieldUrl;

    private MockRestServiceServer mockRestServiceServer;

    private static PageDto<DynamicFieldDto> pagedDynamicFields;

    private static LocationHierarchyLevelResponseDto locationHierarchyLevelResponseDto;

    @Before
    public void setUp() {
        //it is important to ignore the order as its completely async during client settings sync
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate)
                .ignoreExpectOrder(true)
                .build();

        DynamicFieldDto dynamicFieldDto = new DynamicFieldDto();
        dynamicFieldDto.setName("test");
        dynamicFieldDto.setDataType("string");
        dynamicFieldDto.setId("test");
        dynamicFieldDto.setIsActive(true);
        dynamicFieldDto.setIsDeleted(false);
        List<DynamicFieldValueDto> valueDtoList = new ArrayList<>();
        DynamicFieldValueDto value1 = new DynamicFieldValueDto();
        value1.setValue("v1");
        value1.setActive(true);
        value1.setCode("v1");
        valueDtoList.add(value1);
        DynamicFieldValueDto value2 = new DynamicFieldValueDto();
        value2.setValue("v2");
        value2.setActive(true);
        value2.setCode("v2");
        valueDtoList.add(value2);
        DynamicFieldValueDto value3 = new DynamicFieldValueDto();
        value3.setValue("v3");
        value3.setActive(true);
        value3.setCode("v3");
        valueDtoList.add(value3);
        dynamicFieldDto.setFieldVal(valueDtoList);
        pagedDynamicFields = new PageDto<>();
        pagedDynamicFields.setData(Collections.singletonList(dynamicFieldDto));
        pagedDynamicFields.setTotalPages(1);
        pagedDynamicFields.setPageNo(0);
        pagedDynamicFields.setTotalItems(1);

        locationHierarchyLevelResponseDto = new LocationHierarchyLevelResponseDto();
        List<LocationHierarchyDto> locations = new ArrayList<>();
        LocationHierarchyDto locationHierarchyDto = new LocationHierarchyDto();
        locationHierarchyDto.setHierarchyLevel((short) 1);
        locationHierarchyDto.setHierarchyLevelName("Country");
        locationHierarchyDto.setLangCode("eng");
        locationHierarchyDto.setIsActive(true);
        locationHierarchyDto.setIsDeleted(false);
        locations.add(locationHierarchyDto);
        locationHierarchyLevelResponseDto.setLocationHierarchyLevels(locations);
    }

    @Test
    public void validateMapperRegisteredModule() {
       Set<Object> modules = objectMapper.getRegisteredModuleIds();
       boolean afterburnerPresent = false;
       boolean javaTimeModulePresent = false;
       for(Object module : modules) {
           if(module.equals("com.fasterxml.jackson.module.afterburner.AfterburnerModule")) {
               afterburnerPresent = true;
           }
           if(module.equals("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule")) {
               javaTimeModulePresent = true;
           }
       }

       Assert.assertFalse(afterburnerPresent);
       Assert.assertFalse(javaTimeModulePresent);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getInitiateDataFetchV1Test() throws Throwable {
        ResponseWrapper<LocationHierarchyLevelResponseDto> locationsResponse = new ResponseWrapper<>();
        locationsResponse.setResponse(locationHierarchyLevelResponseDto);
        mockRestServiceServer.expect(requestTo(locationHirerarchyUrl)).andRespond(withSuccess()
                .body(objectMapper.writeValueAsString(locationsResponse)));

        ResponseWrapper<PageDto<DynamicFieldDto>> dynamicDataResponseWrapper = new ResponseWrapper<>();
        dynamicDataResponseWrapper.setResponse(pagedDynamicFields);
        dynamicDataResponseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));
        mockRestServiceServer.expect(requestTo(dynamicfieldUrl+"?pageNumber=0"))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(dynamicDataResponseWrapper)));

        LocalDateTime lastUpdated = LocalDateTime.now(ZoneOffset.UTC).minusYears(10);
        mockRestServiceServer.expect(requestTo(locationHirerarchyUrl+"?lastUpdated="+
                DateUtils.formatToISOString(lastUpdated))).andRespond(withSuccess()
                .body(objectMapper.writeValueAsString(locationsResponse)));

        mockRestServiceServer.expect(requestTo(dynamicfieldUrl+"?lastUpdated="+
                        DateUtils.formatToISOString(lastUpdated)+"&pageNumber=0"))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(dynamicDataResponseWrapper)));

        SyncDataResponseDto syncDataResponseDto = syncMasterDataService.syncClientSettings("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                null, syncJobHelperService.getFullSyncCurrentTimestamp());
        assertNotNull(syncDataResponseDto.getDataToSync());

        SyncDataResponseDto syncDataResponseDeltaDto = syncMasterDataService.syncClientSettings("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                lastUpdated, syncJobHelperService.getDeltaSyncCurrentTimestamp());
        assertNotNull(syncDataResponseDeltaDto.getDataToSync());

        assertEquals(syncDataResponseDto.getDataToSync().size(), syncDataResponseDeltaDto.getDataToSync().size(), 12);
    }

    @Test
    public void getInitiateDataFetchV2Test() {
        ResponseWrapper<LocationHierarchyLevelResponseDto> locationsResponse = new ResponseWrapper<>();
        locationsResponse.setResponse(locationHierarchyLevelResponseDto);
        try {
            mockRestServiceServer.expect(requestTo(locationHirerarchyUrl)).andRespond(withSuccess()
                    .body(objectMapper.writeValueAsString(locationsResponse)));
        } catch (Exception e) {
            e.getCause();
        }

        ResponseWrapper<PageDto<DynamicFieldDto>> dynamicDataResponseWrapper = new ResponseWrapper<>();
        dynamicDataResponseWrapper.setResponse(pagedDynamicFields);
        dynamicDataResponseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));
        try {
            mockRestServiceServer.expect(requestTo(dynamicfieldUrl+"?pageNumber=0"))
                    .andRespond(withSuccess().body(objectMapper.writeValueAsString(dynamicDataResponseWrapper)));
        } catch (Exception e) {
            e.getCause();
        }

        LocalDateTime lastUpdated = LocalDateTime.now(ZoneOffset.UTC).minusYears(10);
        try {
            mockRestServiceServer.expect(requestTo(locationHirerarchyUrl+"?lastUpdated="+
                    DateUtils.formatToISOString(lastUpdated))).andRespond(withSuccess()
                    .body(objectMapper.writeValueAsString(locationsResponse)));
        } catch (Exception e) {
            e.getCause();
        }

        try {
            mockRestServiceServer.expect(requestTo(dynamicfieldUrl+"?lastUpdated="+
                            DateUtils.formatToISOString(lastUpdated)+"&pageNumber=0"))
                    .andRespond(withSuccess().body(objectMapper.writeValueAsString(dynamicDataResponseWrapper)));
        } catch (Exception e) {
            e.getCause();
        }

        SyncDataResponseDto syncDataResponseDto = syncMasterDataService.syncClientSettingsV2("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                lastUpdated, syncJobHelperService.getDeltaSyncCurrentTimestamp(), "1.2.0", "LocationHierarchy");

        SyncDataResponseDto syncDataResponseDeltaDto = syncMasterDataService.syncClientSettingsV2("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                lastUpdated, syncJobHelperService.getFullSyncCurrentTimestamp(), "1.2.0", "LocationHierarchy");

        assertEquals(syncDataResponseDto, syncDataResponseDeltaDto);
    }

    @Test
    public void getClientSettingsJsonFileTest2() throws Exception {
        String errorCode = null;
        try {
            syncMasterDataService.getClientSettingsJsonFile("LOCATION1",
                    "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29");
        } catch (FileNotFoundException ex) {
            errorCode = ex.getErrorCode();
        }
        assertEquals(null, errorCode);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getClientSettingsJsonFileTest() throws Exception{
        MockRestServiceServer mockRestServiceServer2 = MockRestServiceServer.bindTo(selfTokenRestTemplate)
                .ignoreExpectOrder(true)
                .build();
        ResponseWrapper<LocationHierarchyLevelResponseDto> locationsResponse = new ResponseWrapper<>();
        locationsResponse.setResponse(locationHierarchyLevelResponseDto);
        mockRestServiceServer2.expect(requestTo(locationHirerarchyUrl)).andRespond(withSuccess()
                .body(objectMapper.writeValueAsString(locationsResponse)));

        ResponseWrapper<PageDto<DynamicFieldDto>> dynamicDataResponseWrapper = new ResponseWrapper<>();
        dynamicDataResponseWrapper.setResponse(pagedDynamicFields);
        dynamicDataResponseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));
        mockRestServiceServer2.expect(requestTo(dynamicfieldUrl+"?pageNumber=0"))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(dynamicDataResponseWrapper)));

        ResponseWrapper<JWTSignatureResponseDto> signResponse = new ResponseWrapper<>();
        JWTSignatureResponseDto jwtSignatureResponseDto = new JWTSignatureResponseDto();
        jwtSignatureResponseDto.setJwtSignedData("header.payload.signature");
        jwtSignatureResponseDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        signResponse.setResponse(jwtSignatureResponseDto);
        mockRestServiceServer.expect(requestTo(signUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(signResponse)));

        syncJobHelperService.createEntitySnapshot();

        ResponseEntity responseEntity = syncMasterDataService.getClientSettingsJsonFile("LOCATION",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29");
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getHeaders().get("file-signature"));
        assertNotNull(responseEntity.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
    }
    
    @Test
    public void getClientSettingsJsonFileTest3() {
        ResponseWrapper<LocationHierarchyLevelResponseDto> locationsResponse = new ResponseWrapper<>();
        locationsResponse.setResponse(locationHierarchyLevelResponseDto);

        ResponseWrapper<PageDto<DynamicFieldDto>> dynamicDataResponseWrapper = new ResponseWrapper<>();
        dynamicDataResponseWrapper.setResponse(pagedDynamicFields);
        dynamicDataResponseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));

        ResponseWrapper<JWTSignatureResponseDto> signResponse = new ResponseWrapper<>();
        JWTSignatureResponseDto jwtSignatureResponseDto = new JWTSignatureResponseDto();
        jwtSignatureResponseDto.setJwtSignedData("header.payload.signature");
        jwtSignatureResponseDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        signResponse.setResponse(jwtSignatureResponseDto);

        syncJobHelperService.createEntitySnapshot();

        assertNotNull(locationsResponse);
        assertNotNull(signResponse);
    }

    @Test
    public void convertAppRolePrioritiesToDto_withValidInput_thenSuccess(){
        List<AppRolePriorityDto> appAuthenticationMethods = new ArrayList<>();
        AppRolePriorityDto appRolePriorityDto = new AppRolePriorityDto();
        appRolePriorityDto.setAppId("id");
        appRolePriorityDto.setPriority(1);
        appRolePriorityDto.setLangCode("eng");
        appRolePriorityDto.setIsActive(true);
        appRolePriorityDto.setRoleCode("code");
        appAuthenticationMethods.add(appRolePriorityDto);

        List<AppRolePriority> appRolePriorities = new ArrayList<>();
        AppRolePriority appRolePriority = new AppRolePriority();
        appRolePriority.setAppId("id");
        appRolePriority.setPriority(1);
        appRolePriority.setLangCode("eng");
        appRolePriority.setIsActive(true);
        appRolePriority.setRoleCode("code");
        appRolePriorities.add(appRolePriority);

        List<AppRolePriorityDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertAppRolePrioritiesToDto",appRolePriorities);
        assertNotNull(result);
        assertEquals(appRolePriorities.size(), result.size());
    }

    @Test
    public void convertScreenAuthorizationToDto_withValidInput_thenSuccess(){
        List<ScreenAuthorizationDto> screenAuthorizationDtos = new ArrayList<>();
        ScreenAuthorizationDto screenAuthorizationDto = new ScreenAuthorizationDto();
        screenAuthorizationDto.setScreenId("id");
        screenAuthorizationDto.setIsActive(true);
        screenAuthorizationDto.setLangCode("eng");
        screenAuthorizationDto.setRoleCode("code");
        screenAuthorizationDto.setIsPermitted(true);
        screenAuthorizationDtos.add(screenAuthorizationDto);

        List<ScreenAuthorization> screenAuthorizationList = new ArrayList<>();
        ScreenAuthorization screenAuthorization = new ScreenAuthorization();
        screenAuthorization.setScreenId("id");
        screenAuthorization.setIsActive(true);
        screenAuthorization.setLangCode("eng");
        screenAuthorization.setRoleCode("code");
        screenAuthorization.setIsPermitted(true);
        screenAuthorizationList.add(screenAuthorization);

        List<ScreenAuthorizationDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertScreenAuthorizationToDto",screenAuthorizationList);
        assertNotNull(result);
        assertEquals(screenAuthorizationList.size(), result.size());
    }

    @Ignore
    @Test
    public void convertprocessListEntityToDto_withValidInput_thenSuccess(){
        List<ProcessListDto> processListDtos = new ArrayList<>();
        ProcessListDto processListDto = new ProcessListDto();
        processListDto.setId("id");
        processListDto.setName("name");
        processListDto.setLangCode("eng");
        processListDto.setIsActive(true);
        processListDto.setDescr("description");
        processListDtos.add(processListDto);

        List<ProcessList> processList = new ArrayList<>();
        ProcessList processList1 = new ProcessList();
        processList1.setId("id");
        processList1.setName("name");
        processList1.setLangCode("eng");
        processList1.setIsActive(true);
        processList1.setDescr("description");
        processList.add(processList1);

        List<ProcessListDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertprocessListEntityToDto",processList);
        assertNotNull(result);
        assertEquals(processList.size(), result.size());
    }

    @Test
    public void convertSyncJobDefEntityToDto_withValidInput_thenSuccess(){
        List<SyncJobDefDto> syncJobDefDtos = new ArrayList<>();
        SyncJobDefDto syncJobDefDto = new SyncJobDefDto();
        syncJobDefDto.setId("id");
        syncJobDefDto.setIsActive(true);
        syncJobDefDto.setLangCode("eng");
        syncJobDefDto.setIsDeleted(false);
        syncJobDefDto.setApiName("api");
        syncJobDefDtos.add(syncJobDefDto);

        List<SyncJobDef> syncJobDefs = new ArrayList<>();
        SyncJobDef syncJobDef = new SyncJobDef();
        syncJobDef.setId("id");
        syncJobDef.setIsActive(true);
        syncJobDef.setLangCode("eng");
        syncJobDef.setIsDeleted(false);
        syncJobDef.setApiName("api");
        syncJobDefs.add(syncJobDef);

        List<SyncJobDefDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertSyncJobDefEntityToDto",syncJobDefs);
        assertNotNull(result);
        assertEquals(syncJobDefs.size(), result.size());
    }

    @Test
    public void convertScreenDetailToDto_withValidInput_thenSuccess(){
        List<ScreenDetailDto> screenDetailDtos = new ArrayList<>();
        ScreenDetailDto screenDetailDto = new ScreenDetailDto();
        screenDetailDto.setAppId("id");
        screenDetailDto.setName("name");
        screenDetailDto.setIsActive(true);
        screenDetailDto.setDescr("description");
        screenDetailDtos.add(screenDetailDto);

        List<ScreenDetail> screenDetails = new ArrayList<>();
        ScreenDetail screenDetail = new ScreenDetail();
        screenDetail.setId("ID");
        screenDetail.setAppId("id");
        screenDetail.setName("name");
        screenDetail.setIsActive(true);
        screenDetail.setDescr("description");
        screenDetails.add(screenDetail);

        List<ScreenDetailDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertScreenDetailToDto",screenDetails);
        assertNotNull(result);
        assertEquals(screenDetails.size(), result.size());
    }

    @Test
    public void convertPermittedConfigEntityToDto_withValidInput_thenSuccess(){
        List<PermittedConfigDto> permittedConfigDtos = new ArrayList<>();
        PermittedConfigDto permittedConfigDto = new PermittedConfigDto();
        permittedConfigDto.setCode("code");
        permittedConfigDto.setName("name");
        permittedConfigDto.setType("type");
        permittedConfigDto.setLangCode("eng");
        permittedConfigDto.setIsActive(true);
        permittedConfigDtos.add(permittedConfigDto);

        List<PermittedLocalConfig> PermittedLocalConfigList = new ArrayList<>();
        PermittedLocalConfig permittedLocalConfig = new PermittedLocalConfig();
        permittedLocalConfig.setCode("code");
        permittedLocalConfig.setName("name");
        permittedLocalConfig.setType("type");
        permittedLocalConfig.setIsActive(true);
        PermittedLocalConfigList.add(permittedLocalConfig);

        List<PermittedConfigDto> result = ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertPermittedConfigEntityToDto",PermittedLocalConfigList);
        assertNotNull(result);
        assertEquals(PermittedLocalConfigList.size(), result.size());
    }

    @Test
    public void convertRegistrationCenterToDto_withEmptyList_thenSuccess(){
        List<RegistrationCenter> list = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertRegistrationCenterToDto",list);
        assertNotNull(list);
    }

    @Test
    public void convertEntityToHoliday_withEmptyHoliday_thenSuccess(){
        List<Holiday> holidays = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertEntityToHoliday",holidays);
        assertNotNull(holidays);
    }

    @Test
    public void convertBlocklistedWordsEntityToDto_withEmptyBlockListedWords_thenSuccess(){
        List<BlocklistedWords> words = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertBlocklistedWordsEntityToDto",words);
        assertNotNull(words);
    }

    @Test
    public void convertDocumentTypeEntityToDto_withEmptyDocumentTypes_thenSuccess(){
        List<DocumentType> documentTypes = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertDocumentTypeEntityToDto",documentTypes);
        assertNotNull(documentTypes);
    }

    @Test
    public void convertLocationsEntityToDto_withEmptyLocations_thenSuccess(){
        List<Location> locations = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertLocationsEntityToDto",locations);
        assertNotNull(locations);
    }

    @Test
    public void convertValidDocumentEntityToDtoWithEmptyValidDocuments_thenSuccess(){
        List<ValidDocument> validDocuments = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertValidDocumentEntityToDto",validDocuments);
        assertNotNull(validDocuments);
    }

    @Test
    public void convertApplicantValidDocumentEntityToDtoWithEmptyApplicantValidDocuments_thenSuccess(){
        List<ApplicantValidDocument> applicantValidDocuments = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertApplicantValidDocumentEntityToDto",applicantValidDocuments);
        assertNotNull(applicantValidDocuments);
    }

    @Test
    public void convertLanguageEntityToDtoWithEmptyLanguageList_thenSuccess(){
        List<Language> languageList = new ArrayList<>();

        ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertLanguageEntityToDto",languageList);
        assertNotNull(languageList);
    }
    @Test
    public void testGetMachines() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getMachines("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetMachines2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getMachines("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetMachines3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getMachines("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetMachines4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getMachines("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetMachines5() {
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getMachines("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenter() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenter("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenter2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenter("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenter3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenter("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenter4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenter("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenter5() {
        lenient().when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenter("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplates() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplates("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplates2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplates("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplates3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplates("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }


    @Test
    public void testGetTemplates4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplates("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplates5() {
        lenient().when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplates("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateFileFormats() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateFileFormats(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateFileFormats2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateFileFormats(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateFileFormats3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateFileFormats(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateFileFormats4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateFileFormats(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateFileFormats5() {
        lenient().when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateFileFormats(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonCategory() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonCategory(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonCategory2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonCategory(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonCategory3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonCategory(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonCategory4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonCategory(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonCategory5() {
        lenient().when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonCategory(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonList() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonList(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonList2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonList(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonList3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonList(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonList4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonList(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetReasonList5() {
        lenient().when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getReasonList(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetHolidays() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getHolidays(lastUpdated, "123", LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetHolidays2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getHolidays(lastUpdated, "123", LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetHolidays3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getHolidays(lastUpdated, "123", LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetHolidays4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getHolidays(lastUpdated, "123", LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetHolidays5() {
        lenient().when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getHolidays(lastUpdated, "123", LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetBlackListedWords() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getBlackListedWords(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetBlackListedWords2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getBlackListedWords(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetBlackListedWords3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getBlackListedWords(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetBlackListedWords4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getBlackListedWords(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetBlackListedWords5() {
        lenient().when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getBlackListedWords(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetDocumentTypes() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getDocumentTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetDocumentTypes2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getDocumentTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetDocumentTypes3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getDocumentTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetDocumentTypes4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getDocumentTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetDocumentTypes5() {
        lenient().when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getDocumentTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLocationHierarchy() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLocationHierarchy(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLocationHierarchy2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLocationHierarchy(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLocationHierarchy3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLocationHierarchy(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }


    @Test
    public void testGetLocationHierarchy4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLocationHierarchy(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLocationHierarchy5() {
        lenient().when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLocationHierarchy(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateTypes() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateTypes2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateTypes3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateTypes4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetTemplateTypes5() {
        lenient().when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getTemplateTypes(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetValidDocuments() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getValidDocuments(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetValidDocuments2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getValidDocuments(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetValidDocuments3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getValidDocuments(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetValidDocuments4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getValidDocuments(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetValidDocuments5() {
        lenient().when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getValidDocuments(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenterMachines() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterMachines("123", lastUpdated,
                LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenterMachines2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterMachines("123", lastUpdated,
                LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenterMachines3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterMachines("123", lastUpdated,
                LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenterMachines4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterMachines("123", lastUpdated,
                LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenterMachines5() {
        lenient().when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterMachines("123", lastUpdated,
                LocalDate.of(2024, 1, 1).atStartOfDay(), "123");

    }

    @Test
    public void testGetRegistrationCenterUsers() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterUsers("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenterUsers2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterUsers("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenterUsers3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterUsers("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenterUsers4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterUsers("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetRegistrationCenterUsers5() {
        lenient().when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getRegistrationCenterUsers("123", lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetApplicantValidDocument() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getApplicantValidDocument(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetApplicantValidDocument2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getApplicantValidDocument(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetApplicantValidDocument3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getApplicantValidDocument(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetApplicantValidDocument4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getApplicantValidDocument(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetApplicantValidDocument5() {
        lenient().when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getApplicantValidDocument(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppAuthenticationMethodDetails() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppAuthenticationMethodDetails(lastUpdatedTime,
                LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppAuthenticationMethodDetails2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppAuthenticationMethodDetails(lastUpdatedTime,
                LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppAuthenticationMethodDetails3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppAuthenticationMethodDetails(lastUpdatedTime,
                LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppAuthenticationMethodDetails4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppAuthenticationMethodDetails(lastUpdatedTime,
                LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppAuthenticationMethodDetails5() {
        lenient().when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppAuthenticationMethodDetails(lastUpdatedTime,
                LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppRolePriorityDetails() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppRolePriorityDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppRolePriorityDetails2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppRolePriorityDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppRolePriorityDetails3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppRolePriorityDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppRolePriorityDetails4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppRolePriorityDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetAppRolePriorityDetails5() {
        lenient().when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getAppRolePriorityDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenAuthorizationDetails() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenAuthorizationDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenAuthorizationDetails2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenAuthorizationDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenAuthorizationDetails3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenAuthorizationDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenAuthorizationDetails4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenAuthorizationDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenAuthorizationDetails5() {
        lenient().when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenAuthorizationDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetProcessList() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getProcessList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetProcessList2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getProcessList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetProcessList3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getProcessList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetProcessList4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getProcessList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetProcessList5() {
        lenient().when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getProcessList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncJobDefDetails() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getSyncJobDefDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncJobDefDetails2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getSyncJobDefDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncJobDefDetails3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getSyncJobDefDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncJobDefDetails4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getSyncJobDefDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncJobDefDetails5() {
        lenient().when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getSyncJobDefDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenDetails() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenDetails2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenDetails3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenDetails4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetScreenDetails5() {
        lenient().when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getScreenDetails(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetPermittedConfig() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getPermittedConfig(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetPermittedConfig2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getPermittedConfig(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetPermittedConfig3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getPermittedConfig(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetPermittedConfig4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getPermittedConfig(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetPermittedConfig5() {
        lenient().when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdated = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getPermittedConfig(lastUpdated, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLanguageList() {
        LocalDateTime createdDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLanguageList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLanguageList2() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLanguageList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLanguageList3() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.now().atStartOfDay();
        lenient().when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.of(2024, 1, 1).atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLanguageList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLanguageList4() {
        LocalDateTime createdDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime updatedDateTime = LocalDate.of(2024, 1, 1).atStartOfDay();
        lenient().when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(new EntityDtimes(createdDateTime, updatedDateTime, LocalDate.now().atStartOfDay()));
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLanguageList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetLanguageList5() {
        lenient().when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime()).thenReturn(null);
        LocalDateTime lastUpdatedTime = LocalDate.of(2024, 1, 1).atStartOfDay();

        syncMasterDataServiceHelper.getLanguageList(lastUpdatedTime, LocalDate.of(2024, 1, 1).atStartOfDay());

    }

    @Test
    public void testGetSyncDataBaseDto() {
        ArrayList<Object> entities = new ArrayList<>();
        RegistrationCenterMachineDto registrationCenterMachineDto = new RegistrationCenterMachineDto("123", "123",
                "Public Key", "123", "123", ClientType.TPM);

        syncMasterDataServiceHelper.getSyncDataBaseDto("Entity Name", "Entity Type", entities, registrationCenterMachineDto,
                new ArrayList<>());

        assertEquals("123", registrationCenterMachineDto.getMachineId());
        assertEquals("123", registrationCenterMachineDto.getMachineSpecId());
        assertEquals("123", registrationCenterMachineDto.getMachineTypeId());
        assertEquals("123", registrationCenterMachineDto.getRegCenterId());
        assertEquals("Public Key", registrationCenterMachineDto.getPublicKey());
        assertEquals(ClientType.TPM, registrationCenterMachineDto.getClientType());
    }

    @Test
    public void testGetSyncDataBaseDto2() throws Exception {
        lenient().when(mapperUtils.getObjectAsJsonString(Mockito.<Object>any())).thenReturn("Object As Json String");
        ArrayList<Object> entities = new ArrayList<>();
        entities.add("123");

        syncMasterDataServiceHelper.getSyncDataBaseDto("Entity Name", "Entity Type", entities, null, new ArrayList<>());

    }

    @Test
    public void testGetSyncDataBaseDtoV2() {
        ArrayList<Object> entities = new ArrayList<>();
        RegistrationCenterMachineDto registrationCenterMachineDto = new RegistrationCenterMachineDto("123", "123",
                "Public Key", "123", "123", ClientType.TPM);


        syncMasterDataServiceHelper.getSyncDataBaseDtoV2("Entity Name", "Entity Type", entities,
                registrationCenterMachineDto, new ArrayList<>());

        assertEquals("123", registrationCenterMachineDto.getMachineId());
        assertEquals("123", registrationCenterMachineDto.getMachineSpecId());
        assertEquals("123", registrationCenterMachineDto.getMachineTypeId());
        assertEquals("123", registrationCenterMachineDto.getRegCenterId());
        assertEquals("Public Key", registrationCenterMachineDto.getPublicKey());
        assertEquals(ClientType.TPM, registrationCenterMachineDto.getClientType());
    }


    @Test
    public void testGetClientType() {
        MachineType machineType = new MachineType();
        machineType.setCode("Code");
        machineType.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machineType.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineType.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineType.setDescription("The characteristics of someone or something");
        machineType.setIsActive(true);
        machineType.setIsDeleted(true);
        machineType.setLangCode("Lang Code");
        machineType.setName("Name");
        machineType.setUpdatedBy("2020-03-01");
        machineType.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());

        MachineSpecification machineSpecification = new MachineSpecification();
        machineSpecification.setBrand("Brand");
        machineSpecification.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machineSpecification.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineSpecification.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineSpecification.setDescription("The characteristics of someone or something");
        machineSpecification.setId("123");
        machineSpecification.setIsActive(true);
        machineSpecification.setIsDeleted(true);
        machineSpecification.setLangCode("Lang Code");
        machineSpecification.setMachineType(machineType);
        machineSpecification.setMachineTypeCode("Machine Type Code");
        machineSpecification.setMinDriverversion("1.0.2");
        machineSpecification.setModel("Model");
        machineSpecification.setName("Name");
        machineSpecification.setUpdatedBy("2020-03-01");
        machineSpecification.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());

        Machine machine = new Machine();
        machine.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machine.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setId("123");
        machine.setIpAddress("123 Main St");
        machine.setIsActive(true);
        machine.setIsDeleted(true);
        machine.setKeyIndex("Key Index");
        machine.setLangCode("Lang Code");
        machine.setMacAddress("123 Main St");
        machine.setMachineSpecId("123");
        machine.setMachineSpecification(machineSpecification);
        machine.setName("Name");
        machine.setPublicKey("Public Key");
        machine.setRegCenterId("123");
        machine.setSerialNum("Serial Num");
        machine.setSignKeyIndex("Sign Key Index");
        machine.setSignPublicKey("Sign Public Key");
        machine.setUpdatedBy("2020-03-01");
        machine.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setValidityDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setZoneCode("Zone Code");

        assertNull(SyncMasterDataServiceHelper.getClientType(machine));
    }

    @Test
    public void testGetClientType2() {
        MachineType machineType = new MachineType();
        machineType.setCode("Code");
        machineType.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machineType.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineType.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineType.setDescription("The characteristics of someone or something");
        machineType.setIsActive(true);
        machineType.setIsDeleted(true);
        machineType.setLangCode("Lang Code");
        machineType.setName("Name");
        machineType.setUpdatedBy("2020-03-01");
        machineType.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());

        MachineSpecification machineSpecification = new MachineSpecification();
        machineSpecification.setBrand("Brand");
        machineSpecification.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machineSpecification.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineSpecification.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machineSpecification.setDescription("The characteristics of someone or something");
        machineSpecification.setId("123");
        machineSpecification.setIsActive(true);
        machineSpecification.setIsDeleted(true);
        machineSpecification.setLangCode("Lang Code");
        machineSpecification.setMachineType(machineType);
        machineSpecification.setMachineTypeCode("ANDROID");
        machineSpecification.setMinDriverversion("1.0.2");
        machineSpecification.setModel("Model");
        machineSpecification.setName("Name");
        machineSpecification.setUpdatedBy("2020-03-01");
        machineSpecification.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());

        Machine machine = new Machine();
        machine.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        machine.setCreatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setDeletedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setId("123");
        machine.setIpAddress("123 Main St");
        machine.setIsActive(true);
        machine.setIsDeleted(true);
        machine.setKeyIndex("Key Index");
        machine.setLangCode("Lang Code");
        machine.setMacAddress("123 Main St");
        machine.setMachineSpecId("123");
        machine.setMachineSpecification(machineSpecification);
        machine.setName("Name");
        machine.setPublicKey("Public Key");
        machine.setRegCenterId("123");
        machine.setSerialNum("Serial Num");
        machine.setSignKeyIndex("Sign Key Index");
        machine.setSignPublicKey("Sign Public Key");
        machine.setUpdatedBy("2020-03-01");
        machine.setUpdatedDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setValidityDateTime(LocalDate.of(2024, 1, 1).atStartOfDay());
        machine.setZoneCode("Zone Code");

        assertEquals(ClientType.ANDROID, SyncMasterDataServiceHelper.getClientType(machine));
    }

}
