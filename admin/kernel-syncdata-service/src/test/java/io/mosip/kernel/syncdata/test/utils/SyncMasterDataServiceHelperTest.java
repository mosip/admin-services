package io.mosip.kernel.syncdata.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import io.mosip.kernel.syncdata.entity.id.HolidayID;
import io.mosip.kernel.syncdata.exception.AdminServiceException;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import org.mockito.InjectMocks;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
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

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest(classes = TestBootApplication.class)
@RunWith(MockitoJUnitRunner.class)
@DirtiesContext
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class SyncMasterDataServiceHelperTest {

    @Mock
    private SyncMasterDataServiceHelper syncMasterDataServiceHelper;

    @InjectMocks
    private SyncMasterDataServiceHelper syncMasterData;

    private SyncMasterDataServiceHelper realHelper;

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

    private RegistrationCenterMachineDto registrationCenterMachineDto;

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

        realHelper = new SyncMasterDataServiceHelper();
        ReflectionTestUtils.setField(realHelper, "documentTypeRepository", documentTypeRepository);
        ObjectMapper realObjectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(realHelper, "objectMapper", realObjectMapper);
        ReflectionTestUtils.setField(realHelper, "locationHirerarchyUrl", "http://localhost/location-hierarchy");
        ReflectionTestUtils.setField(realHelper, "machineRepository", machineRepository);
        ReflectionTestUtils.setField(realHelper, "registrationCenterRepository", registrationCenterRepository);
        ReflectionTestUtils.setField(realHelper, "templateRepository", templateRepository);
        ReflectionTestUtils.setField(realHelper, "templateFileFormatRepository", templateFileFormatRepository);
        ReflectionTestUtils.setField(realHelper, "reasonCategoryRepository", reasonCategoryRepository);
        ReflectionTestUtils.setField(realHelper, "reasonListRepository", reasonListRepository);
        ReflectionTestUtils.setField(realHelper, "holidayRepository", holidayRepository);
        ReflectionTestUtils.setField(realHelper, "blocklistedWordsRepository", blocklistedWordsRepository);
        ReflectionTestUtils.setField(realHelper, "locationRepository", locationRepository);

        registrationCenterMachineDto = new RegistrationCenterMachineDto();
        registrationCenterMachineDto.setMachineId("M1");
        registrationCenterMachineDto.setPublicKey("PUBLIC_KEY_SAMPLE");
        registrationCenterMachineDto.setMachineSpecId("SPEC1");
        registrationCenterMachineDto.setClientType(ClientType.TPM);
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

    @Test
    public void testGetMachines_NoChangesFound() throws Exception {

        // Arrange
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // This makes isChangesFound return false

        // Act
        CompletableFuture<List<MachineDto>> future =
                syncMasterData.getMachines(
                        "1001",
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC),
                        null);

        List<MachineDto> result = future.get();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetMachines_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        // Make isChangesFound return true
        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        entityDtimes.setCreatedDateTime(now);
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        // Prepare entity
        Machine machine = new Machine();
        machine.setId("M1");
        machine.setName("Machine1");
        machine.setIpAddress("192.168.1.1");
        machine.setPublicKey("pubKey");
        machine.setIsActive(true);
        machine.setIsDeleted(false);
        machine.setKeyIndex("1");
        machine.setLangCode("eng");
        machine.setMacAddress("AA:BB:CC");
        machine.setMachineSpecId("SPEC1");
        machine.setSerialNum("SER123");
        machine.setValidityDateTime(now.plusDays(10));
        machine.setRegCenterId("1001");

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(machine));

        CompletableFuture<List<MachineDto>> future =
                syncMasterData.getMachines("1001", lastUpdated, now, null);

        List<MachineDto> result = future.get();

        assertEquals(1, result.size());

        MachineDto dto = result.get(0);

        assertEquals("M1", dto.getId());
        assertEquals("Machine1", dto.getName());
        assertEquals("192.168.1.1", dto.getIpAddress());
        assertEquals("pubKey", dto.getPublicKey());
        assertEquals("1001", dto.getRegCenterId());
    }

    @Test
    public void testGetMachines_EmptyRepositoryResult() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(5);

        EntityDtimes entityDtimes =
                new EntityDtimes(null, now, null);
        entityDtimes.setUpdatedDateTime(now);
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<MachineDto>> future =
                syncMasterData.getMachines("1001", lastUpdated, now, null);

        List<MachineDto> result = future.get();

        assertTrue(result.isEmpty());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetMachines_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        // deletedDateTime is after lastUpdated → changes found
        EntityDtimes entityDtimes =
                new EntityDtimes(null, null, now);

        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getMachines("1001", lastUpdated, now, null)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetLocationHierarchyList_Success() throws Exception {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",
                "http://localhost/location");
        LocalDateTime lastUpdated = LocalDateTime.now(ZoneOffset.UTC);

        // Prepare response DTO
        LocationHierarchyDto dto = new LocationHierarchyDto();
        dto.setHierarchyLevel((short)1);
        dto.setHierarchyLevelName("Country");

        LocationHierarchyLevelResponseDto responseDto =
                new LocationHierarchyLevelResponseDto();
        responseDto.setLocationHierarchyLevels(
                Collections.singletonList(dto));

        ResponseWrapper<LocationHierarchyLevelResponseDto> wrapper =
                new ResponseWrapper<>();
        wrapper.setResponse(responseDto);

        String json = "{}"; // actual content doesn't matter because ObjectMapper is mocked

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(json, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readValue(anyString(), eq(ResponseWrapper.class)))
                .thenReturn(wrapper);

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        when(objectMapper.readValue(anyString(),
                eq(LocationHierarchyLevelResponseDto.class)))
                .thenReturn(responseDto);

        CompletableFuture<List<LocationHierarchyDto>> future =
                syncMasterData.getLocationHierarchyList(lastUpdated);

        List<LocationHierarchyDto> result = future.get();

        assertEquals(1, result.size());
        assertEquals("Country", result.get(0).getHierarchyLevelName());
    }

    @Test(expected = SyncServiceException.class)
    public void testGetLocationHierarchyList_WithValidationErrors() {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",   // EXACT field name
                "http://localhost/test");
        String errorJson = "{\"errors\":[{\"errorCode\":\"100\",\"message\":\"error\"}]}";


        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(errorJson, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        syncMasterData.getLocationHierarchyList(null);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetLocationHierarchyList_DeserializationFailure() throws Exception {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",   // EXACT field name
                "http://localhost/test");
        String jsonResponse = "{\"response\": {}}";

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        // Force exception inside try block
        when(objectMapper.readValue(anyString(), eq(ResponseWrapper.class)))
                .thenThrow(new RuntimeException("JSON parsing failed"));

        syncMasterData.getLocationHierarchyList(null);
    }

    @Test
    public void testGetLocationHierarchyList() throws Exception {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",   // EXACT field name
                "http://localhost/test");
        String jsonResponse = "{\"response\":{}}";

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        // No validation errors (assuming empty list returned naturally)

        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.setResponse(new Object());

        LocationHierarchyLevelResponseDto dto =
                new LocationHierarchyLevelResponseDto();

        List<LocationHierarchyDto> list = new ArrayList<>();
        list.add(new LocationHierarchyDto());
        dto.setLocationHierarchyLevels(list);

        when(objectMapper.readValue(anyString(), eq(ResponseWrapper.class)))
                .thenReturn(wrapper);

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        when(objectMapper.readValue(anyString(),
                eq(LocationHierarchyLevelResponseDto.class)))
                .thenReturn(dto);

        CompletableFuture<List<LocationHierarchyDto>> result =
                syncMasterData.getLocationHierarchyList(null, restTemplate);

        assertNotNull(result);
        assertEquals(1, result.get().size());
    }

    @Test(expected = SyncServiceException.class)
    public void testGetLocationHierarchyList_ValidationErrors() {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",   // EXACT field name
                "http://localhost/test");
        String errorJson = "{\"errors\":[{\"errorCode\":\"100\",\"message\":\"error\"}]}";

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(errorJson, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        syncMasterData.getLocationHierarchyList(null, restTemplate);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetLocationHierarchyList_ShouldEnterCatchBlock() throws Exception {

        ReflectionTestUtils.setField(syncMasterData,
                "locationHirerarchyUrl",   // EXACT field name
                "http://localhost/test");
        String jsonResponse = "{\"response\":{}}";

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readValue(anyString(), eq(ResponseWrapper.class)))
                .thenThrow(new RuntimeException("JSON parse error"));

        syncMasterData.getLocationHierarchyList(null, restTemplate);
    }

    @Test
    public void testGetRegistrationCenter_LastUpdatedNull_ShouldProceed() throws Exception {

        // repository for actual data fetch
        when(registrationCenterRepository.findRegistrationCentersById(
                anyString(), any(), any()))
                .thenReturn(new ArrayList<>());

        CompletableFuture<List<RegistrationCenterDto>> result =
                syncMasterData.getRegistrationCenter(
                        "C1",
                        null,
                        LocalDateTime.now());

        // Since repo returns empty list -> converter returns null
        assertNull(result.get());
    }

    @Test
    public void testGetRegistrationCenter_NoDataInTable_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);

        // Mock delta table max dates = null
        when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);

        CompletableFuture<List<RegistrationCenterDto>> result =
                syncMasterData.getRegistrationCenter(
                        "C1",
                        lastUpdated,
                        LocalDateTime.now());

        // isChangesFound returns false → method returns completedFuture(null)
        assertNull(result.get());
    }

    @Test
    public void testGetRegistrationCenter_ChangesFound_ShouldFetchData() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(5);

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),   // createdDateTime
                null,
                null
        );

        when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        RegistrationCenter entity = new RegistrationCenter();
        entity.setId("C1");

        List<RegistrationCenter> list = new ArrayList<>();
        list.add(entity);

        when(registrationCenterRepository.findRegistrationCentersById(
                anyString(), any(), any()))
                .thenReturn(list);

        CompletableFuture<List<RegistrationCenterDto>> result =
                syncMasterData.getRegistrationCenter(
                        "C1",
                        lastUpdated,
                        LocalDateTime.now());

        assertNotNull(result.get());
        assertEquals(1, result.get().size());
    }

    @Test
    public void testGetRegistrationCenter_NoChanges_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                lastUpdated.minusDays(2),
                null,
                null
        );

        when(registrationCenterRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        CompletableFuture<List<RegistrationCenterDto>> result =
                syncMasterData.getRegistrationCenter(
                        "C1",
                        lastUpdated,
                        LocalDateTime.now());

        assertNull(result.get());
    }

    @Test
    public void testGetTemplates_NoChangesFound_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        // Mock repository used inside isChangesFound()
        when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // result == null → isChangesFound() returns false

        CompletableFuture<List<TemplateDto>> future =
                syncMasterData.getTemplates("REG", lastUpdated, currentTime);

        List<TemplateDto> result = future.get();

        assertNull(result);

        // Ensure actual fetch method is NOT called
        verify(templateRepository, never())
                .findAllLatestCreatedUpdateDeletedByModule(any(), any(), any());
    }

    @Test
    public void testGetTemplates_WithChanges_ShouldReturnTemplateList() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        Template template = new Template();
        template.setId("1");
        template.setName("TestTemplate");
        template.setDescription("Desc");
        template.setFileFormatCode("PDF");
        template.setModel("model");
        template.setFileText("text");
        template.setModuleId("REG");
        template.setModuleName("Registration");
        template.setTemplateTypeCode("TYPE1");
        template.setIsActive(true);
        template.setIsDeleted(false);
        template.setLangCode("eng");

        when(templateRepository.findAllLatestCreatedUpdateDeletedByModule(
                any(), any(), any()))
                .thenReturn(List.of(template));

        CompletableFuture<List<TemplateDto>> future =
                syncMasterData.getTemplates("REG", lastUpdated, currentTime);

        List<TemplateDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    public void testGetTemplates_LastUpdatedNull_ShouldSetEpoch() throws Exception {

        LocalDateTime currentTime = LocalDateTime.now();

        Template template = new Template();
        template.setId("1");

        when(templateRepository.findAllLatestCreatedUpdateDeletedByModule(
                any(), any(), any()))
                .thenReturn(List.of(template));

        CompletableFuture<List<TemplateDto>> future =
                syncMasterData.getTemplates("REG", null, currentTime);

        List<TemplateDto> result = future.get();

        assertNotNull(result);
        verify(templateRepository)
                .findAllLatestCreatedUpdateDeletedByModule(any(), any(), eq("REG"));
    }

    @Test
    public void testGetTemplates_DataAccessException_ShouldThrowSyncDataServiceException() {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(templateRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(templateRepository.findAllLatestCreatedUpdateDeletedByModule(
                any(), any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        assertThrows(SyncDataServiceException.class,
                () -> syncMasterData.getTemplates("REG", lastUpdated, currentTime));
    }

    @Test
    public void testGetTemplateFileFormats_NoChangesFound_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // isChangesFound() returns false

        CompletableFuture<List<TemplateFileFormatDto>> future =
                syncMasterData.getTemplateFileFormats(lastUpdated, currentTime);

        List<TemplateFileFormatDto> result = future.get();

        assertNull(result);

        verify(templateFileFormatRepository, never())
                .findAllLatestCreatedUpdateDeleted(any(), any());
    }

    @Test
    public void testGetTemplateFileFormats_WithChanges_ShouldReturnList() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        TemplateFileFormat entity = new TemplateFileFormat();
        entity.setCode("PDF");
        entity.setDescription("PDF Format");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        CompletableFuture<List<TemplateFileFormatDto>> future =
                syncMasterData.getTemplateFileFormats(lastUpdated, currentTime);

        List<TemplateFileFormatDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PDF", result.get(0).getCode());
    }

    @Test
    public void testGetTemplateFileFormats_LastUpdatedNull_ShouldSetEpoch() throws Exception {

        LocalDateTime currentTime = LocalDateTime.now();

        TemplateFileFormat entity = new TemplateFileFormat();
        entity.setCode("DOC");

        when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        CompletableFuture<List<TemplateFileFormatDto>> future =
                syncMasterData.getTemplateFileFormats(null, currentTime);

        List<TemplateFileFormatDto> result = future.get();

        assertNotNull(result);

        verify(templateFileFormatRepository)
                .findAllLatestCreatedUpdateDeleted(any(), eq(currentTime));
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetTemplateFileFormats_DataAccessException_ShouldThrowException() {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(templateFileFormatRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(templateFileFormatRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getTemplateFileFormats(lastUpdated, currentTime);
    }

    @Test
    public void testGetReasonCategory_NoChangesFound_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);

        CompletableFuture<List<PostReasonCategoryDto>> future =
                syncMasterData.getReasonCategory(lastUpdated, currentTime);

        assertNull(future.get());

        verify(reasonCategoryRepository, never())
                .findAllLatestCreatedUpdateDeleted(any(), any());
    }

    @Test
    public void testGetReasonCategory_WithChanges_ShouldReturnList() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        ReasonCategory entity = new ReasonCategory();
        entity.setCode("RC1");
        entity.setDescription("Reason Desc");
        entity.setName("Reason Name");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        CompletableFuture<List<PostReasonCategoryDto>> future =
                syncMasterData.getReasonCategory(lastUpdated, currentTime);

        List<PostReasonCategoryDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("RC1", result.get(0).getCode());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetReasonCategory_DataAccessException_ShouldThrowException() {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(reasonCategoryRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(reasonCategoryRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getReasonCategory(lastUpdated, currentTime);
    }

    @Test
    public void testGetReasonList_NoChangesFound_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);   // isChangesFound() returns false

        CompletableFuture<List<ReasonListDto>> future =
                syncMasterData.getReasonList(lastUpdated, currentTime);

        assertNull(future.get());

        verify(reasonListRepository, never())
                .findAllLatestCreatedUpdateDeleted(any(), any());
    }

    @Test
    public void testGetReasonList_WithChanges_ShouldReturnList() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        ReasonList entity = new ReasonList();
        entity.setCode("R001");
        entity.setName("Reason Name");
        entity.setDescription("Reason Desc");
        entity.setRsnCatCode("CAT1");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(reasonListRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        CompletableFuture<List<ReasonListDto>> future =
                syncMasterData.getReasonList(lastUpdated, currentTime);

        List<ReasonListDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("R001", result.get(0).getCode());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetReasonList_DataAccessException_ShouldThrowException() {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(reasonListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(reasonListRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getReasonList(lastUpdated, currentTime);
    }

    @Test
    public void testGetHolidays_NoChangesFound_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);

        CompletableFuture<List<HolidayDto>> future =
                syncMasterData.getHolidays(lastUpdated, "M1", currentTime);

        assertNull(future.get());

        verify(holidayRepository, never())
                .findAllLatestCreatedUpdateDeletedByMachineId(any(), any(), any());
    }

    @Test
    public void testGetHolidays_WithChanges_ShouldReturnList() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        // Prepare HolidayID
        HolidayID holidayID = new HolidayID();
        holidayID.setHolidayDate(LocalDate.of(2025, 1, 26));
        holidayID.setHolidayName("Republic Day");
        holidayID.setLangCode("eng");
        holidayID.setLocationCode("LOC1");

        Holiday holiday = new Holiday();
        holiday.setId(1);
        holiday.setHolidayId(holidayID);
        holiday.setIsActive(true);
        holiday.setIsDeleted(false);

        when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(
                any(), any(), any()))
                .thenReturn(List.of(holiday));

        CompletableFuture<List<HolidayDto>> future =
                syncMasterData.getHolidays(lastUpdated, "M1", currentTime);

        List<HolidayDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        HolidayDto dto = result.get(0);
        assertEquals("Republic Day", dto.getHolidayName());
        assertEquals("2025", dto.getHolidayYear());
    }

    @Test
    public void testGetHolidays_EmptyList_ShouldReturnNull() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(2);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(
                any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<HolidayDto>> future =
                syncMasterData.getHolidays(lastUpdated, "M1", currentTime);

        assertNull(future.get());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetHolidays_DataAccessException_ShouldThrowException() {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime currentTime = LocalDateTime.now();

        EntityDtimes entityDtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(holidayRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(holidayRepository.findAllLatestCreatedUpdateDeletedByMachineId(
                any(), any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getHolidays(lastUpdated, "M1", currentTime);
    }

    @Test
    public void testGetBlackListedWords_NoChangesFound_ShouldReturnNull() throws Exception {

        when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);

        CompletableFuture<List<BlacklistedWordsDto>> future =
                syncMasterData.getBlackListedWords(
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now());

        assertNull(future.get());

        verify(blocklistedWordsRepository, never())
                .findAllLatestCreatedUpdateDeleted(any(), any());
    }

    @Test
    public void testGetBlackListedWords_WithChanges_ShouldReturnList() throws Exception {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        BlocklistedWords entity = new BlocklistedWords();
        entity.setWord("badword");
        entity.setDescription("desc");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        List<BlacklistedWordsDto> result =
                syncMasterData.getBlackListedWords(
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now()).get();

        assertNotNull(result);
        assertEquals("badword", result.get(0).getWord());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetBlackListedWords_DataAccessException() {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(blocklistedWordsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        when(blocklistedWordsRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getBlackListedWords(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now());
    }

    @Test
    public void testGetDocumentTypes_WithChanges_ShouldReturnList() throws Exception {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        DocumentType entity = new DocumentType();
        entity.setCode("DOC1");
        entity.setName("Passport");
        entity.setDescription("Passport Doc");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        List<DocumentTypeDto> result =
                syncMasterData.getDocumentTypes(
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now()).get();

        assertEquals("DOC1", result.get(0).getCode());
    }

    @Test
    public void testGetLocationHierarchy_WithChanges_ShouldReturnList() throws Exception {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(locationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        Location entity = new Location();
        entity.setCode("LOC1");
        entity.setName("Bihar");
        entity.setHierarchyLevel(1);
        entity.setHierarchyName("State");
        entity.setParentLocCode(null);
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(locationRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        List<LocationDto> result =
                syncMasterData.getLocationHierarchy(
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now()).get();

        assertEquals("LOC1", result.get(0).getCode());
    }

    @Test
    public void testGetTemplateTypes_WithChanges_ShouldReturnList() throws Exception {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(templateTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        TemplateType entity = new TemplateType();
        entity.setCode("TT1");
        entity.setDescription("Template Type");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(templateTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        List<TemplateTypeDto> result =
                syncMasterData.getTemplateTypes(
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now()).get();

        assertEquals("TT1", result.get(0).getCode());
    }

    @Test
    public void testGetDocumentTypes_LastUpdatedNull_ShouldFetch() throws Exception {

        DocumentType entity = new DocumentType();
        entity.setCode("DOC1");

        when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(entity));

        List<DocumentTypeDto> result =
                syncMasterData.getDocumentTypes(null, LocalDateTime.now()).get();

        assertNotNull(result);
    }

    @Test
    public void testGetDocumentTypes_ResultNull_ShouldReturnNull() throws Exception {

        when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);

        List<DocumentTypeDto> result =
                syncMasterData.getDocumentTypes(
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now()).get();

        assertNull(result);
    }

    @Test
    public void testDeletedDateTimeCondition_ShouldFetch() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(5);

        EntityDtimes dtimes = new EntityDtimes(
                null,
                null,
                LocalDateTime.now().minusDays(1)
        );

        when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(new DocumentType()));

        List<DocumentTypeDto> result =
                syncMasterData.getDocumentTypes(lastUpdated, LocalDateTime.now()).get();

        assertNotNull(result);
    }

    @Test
    public void testUpdatedDateTimeCondition_ShouldFetch() throws Exception {

        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(5);

        EntityDtimes dtimes = new EntityDtimes(
                null,
                LocalDateTime.now().minusDays(1),
                null
        );

        when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(new DocumentType()));

        List<DocumentTypeDto> result =
                syncMasterData.getDocumentTypes(lastUpdated, LocalDateTime.now()).get();

        assertNotNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testDataAccessException_ShouldThrowSyncDataServiceException() {

        EntityDtimes dtimes = new EntityDtimes(
                LocalDateTime.now(),
                null,
                null
        );

        when(documentTypeRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(dtimes);

        when(documentTypeRepository.findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB Error"));

        syncMasterData.getDocumentTypes(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now());
    }

    @Test
    public void testGetValidDocuments_NoChangesFound() throws Exception {

        // Arrange
        when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null); // makes isChangesFound return false

        // Act
        CompletableFuture<List<ValidDocumentDto>> future =
                syncMasterData.getValidDocuments(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<ValidDocumentDto> result = future.get();

        // Assert
        assertNull(result);  // method returns completedFuture(null)
    }

    @Test
    public void testGetValidDocuments_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Make isChangesFound return true
        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(
                any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        CompletableFuture<List<ValidDocumentDto>> future =
                syncMasterData.getValidDocuments(null, now);

        List<ValidDocumentDto> result = future.get();

        // Assert
        assertNull(result); // because convert method returns null for empty list
    }

    @Test
    public void testGetValidDocuments_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        // Changes found
        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        // Prepare entity
        ValidDocument entity = new ValidDocument();
        entity.setDocTypeCode("DOC1");
        entity.setDocCategoryCode("CAT1");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(
                any(), any()))
                .thenReturn(Collections.singletonList(entity));

        // Act
        CompletableFuture<List<ValidDocumentDto>> future =
                syncMasterData.getValidDocuments(lastUpdated, now);

        List<ValidDocumentDto> result = future.get();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        ValidDocumentDto dto = result.get(0);

        assertEquals("DOC1", dto.getDocTypeCode());
        assertEquals("CAT1", dto.getDocCategoryCode());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetValidDocuments_EmptyRepositoryResult() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(3);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(
                any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<ValidDocumentDto>> future =
                syncMasterData.getValidDocuments(lastUpdated, now);

        List<ValidDocumentDto> result = future.get();

        assertNull(result);  // convert method returns null for empty list
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetValidDocuments_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(validDocumentRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(validDocumentRepository.findAllLatestCreatedUpdateDeleted(
                any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getValidDocuments(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetRegistrationCenterMachines_NoChangesFound() throws Exception {

        // Arrange
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        // Act
        CompletableFuture<List<RegistrationCenterMachineDto>> future =
                syncMasterData.getRegistrationCenterMachines(
                        "1001",
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC),
                        null);

        List<RegistrationCenterMachineDto> result = future.get();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetRegistrationCenterMachines_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<RegistrationCenterMachineDto>> future =
                syncMasterData.getRegistrationCenterMachines(
                        "1001",
                        null,
                        now,
                        null);

        List<RegistrationCenterMachineDto> result = future.get();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRegistrationCenterMachines_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        Machine machine = new Machine();
        machine.setId("M1");
        machine.setRegCenterId("1001");
        machine.setIsActive(true);
        machine.setIsDeleted(false);
        machine.setLangCode("eng");

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(machine));

        CompletableFuture<List<RegistrationCenterMachineDto>> future =
                syncMasterData.getRegistrationCenterMachines(
                        "1001",
                        lastUpdated,
                        now,
                        null);

        List<RegistrationCenterMachineDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        RegistrationCenterMachineDto dto = result.get(0);

        assertEquals("M1", dto.getMachineId());
        assertEquals("1001", dto.getRegCenterId());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetRegistrationCenterMachines_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenReturn(null);

        CompletableFuture<List<RegistrationCenterMachineDto>> future =
                syncMasterData.getRegistrationCenterMachines(
                        "1001",
                        lastUpdated,
                        now,
                        null);

        List<RegistrationCenterMachineDto> result = future.get();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetRegistrationCenterMachines_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(machineRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(machineRepository.findMachineLatestCreatedUpdatedDeleted(
                any(), any(), any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getRegistrationCenterMachines("1001", lastUpdated, now, null)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetRegistrationCenterUsers_NoChangesFound() throws Exception {

        // Arrange
        when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        // Act
        CompletableFuture<List<RegistrationCenterUserDto>> future =
                syncMasterData.getRegistrationCenterUsers(
                        "1001",
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<RegistrationCenterUserDto> result = future.get();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRegistrationCenterUsers_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(
                any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<RegistrationCenterUserDto>> future =
                syncMasterData.getRegistrationCenterUsers(
                        "1001",
                        null,
                        now);

        List<RegistrationCenterUserDto> result = future.get();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRegistrationCenterUsers_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        UserDetails user = new UserDetails();
        user.setId("U1");
        user.setRegCenterId("1001");
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setLangCode("eng");

        when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(
                any(), any(), any()))
                .thenReturn(Collections.singletonList(user));

        CompletableFuture<List<RegistrationCenterUserDto>> future =
                syncMasterData.getRegistrationCenterUsers(
                        "1001",
                        lastUpdated,
                        now);

        List<RegistrationCenterUserDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        RegistrationCenterUserDto dto = result.get(0);

        assertEquals("U1", dto.getUserId());
        assertEquals("1001", dto.getRegCenterId());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetRegistrationCenterUsers_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(
                any(), any(), any()))
                .thenReturn(null);

        CompletableFuture<List<RegistrationCenterUserDto>> future =
                syncMasterData.getRegistrationCenterUsers(
                        "1001",
                        lastUpdated,
                        now);

        List<RegistrationCenterUserDto> result = future.get();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetRegistrationCenterUsers_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(userDetailsRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(userDetailsRepository.findAllLatestCreatedUpdatedDeleted(
                any(), any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getRegistrationCenterUsers("1001", lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetApplicantValidDocument_NoChangesFound() throws Exception {

        when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);   // makes isChangesFound return false

        CompletableFuture<List<ApplicantValidDocumentDto>> future =
                syncMasterData.getApplicantValidDocument(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<ApplicantValidDocumentDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetApplicantValidDocument_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        when(applicantValidDocumentRespository.findAllByTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<ApplicantValidDocumentDto>> future =
                syncMasterData.getApplicantValidDocument(null, now);

        List<ApplicantValidDocumentDto> result = future.get();

        assertNull(result);  // convert method returns null
    }

    @Test
    public void testGetApplicantValidDocument_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        // Prepare composite ID
        ApplicantValidDocumentID id = new ApplicantValidDocumentID();
        id.setAppTypeCode("APP1");
        id.setDocTypeCode("DOC1");
        id.setDocCatCode("CAT1");

        ApplicantValidDocument entity = new ApplicantValidDocument();
        entity.setApplicantValidDocumentId(id);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        when(applicantValidDocumentRespository.findAllByTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<ApplicantValidDocumentDto>> future =
                syncMasterData.getApplicantValidDocument(lastUpdated, now);

        List<ApplicantValidDocumentDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        ApplicantValidDocumentDto dto = result.get(0);

        assertEquals("APP1", dto.getAppTypeCode());
        assertEquals("DOC1", dto.getDocTypeCode());
        assertEquals("CAT1", dto.getDocCatCode());
        assertEquals("eng", dto.getLangCode());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
    }

    @Test
    public void testGetApplicantValidDocument_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(applicantValidDocumentRespository.findAllByTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<ApplicantValidDocumentDto>> future =
                syncMasterData.getApplicantValidDocument(lastUpdated, now);

        List<ApplicantValidDocumentDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetApplicantValidDocument_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(applicantValidDocumentRespository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(applicantValidDocumentRespository.findAllByTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getApplicantValidDocument(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetAppAuthenticationMethodDetails_NoChangesFound() throws Exception {

        when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);   // makes isChangesFound return false

        CompletableFuture<List<AppAuthenticationMethodDto>> future =
                syncMasterData.getAppAuthenticationMethodDetails(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<AppAuthenticationMethodDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetAppAuthenticationMethodDetails_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(appAuthenticationMethodRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<AppAuthenticationMethodDto>> future =
                syncMasterData.getAppAuthenticationMethodDetails(null, now);

        List<AppAuthenticationMethodDto> result = future.get();

        assertNull(result);  // convert returns null
    }

    @Test
    public void testGetAppAuthenticationMethodDetails_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        AppAuthenticationMethod entity = new AppAuthenticationMethod();
        entity.setAppId("APP1");
        entity.setProcessId("PROC1");
        entity.setRoleCode("ROLE1");
        entity.setAuthMethodCode("AUTH1");
        entity.setMethodSequence(1);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        when(appAuthenticationMethodRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<AppAuthenticationMethodDto>> future =
                syncMasterData.getAppAuthenticationMethodDetails(lastUpdated, now);

        List<AppAuthenticationMethodDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        AppAuthenticationMethodDto dto = result.get(0);

        assertEquals("APP1", dto.getAppId());
        assertEquals("PROC1", dto.getProcessId());
        assertEquals("ROLE1", dto.getRoleCode());
        assertEquals("AUTH1", dto.getAuthMethodCode());
        assertEquals(Integer.valueOf(1), dto.getMethodSequence());
        assertEquals("eng", dto.getLangCode());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
    }

    @Test
    public void testGetAppAuthenticationMethodDetails_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(appAuthenticationMethodRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<AppAuthenticationMethodDto>> future =
                syncMasterData.getAppAuthenticationMethodDetails(lastUpdated, now);

        List<AppAuthenticationMethodDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetAppAuthenticationMethodDetails_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appAuthenticationMethodRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(appAuthenticationMethodRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getAppAuthenticationMethodDetails(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetAppRolePriorityDetails_NoChangesFound() throws Exception {

        when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);   // makes isChangesFound return false

        CompletableFuture<List<AppRolePriorityDto>> future =
                syncMasterData.getAppRolePriorityDetails(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<AppRolePriorityDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetAppRolePriorityDetails_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(appRolePriorityRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<AppRolePriorityDto>> future =
                syncMasterData.getAppRolePriorityDetails(null, now);

        List<AppRolePriorityDto> result = future.get();

        assertNull(result);  // convert returns null
    }

    @Test
    public void testGetAppRolePriorityDetails_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        AppRolePriority entity = new AppRolePriority();
        entity.setAppId("APP1");
        entity.setProcessId("PROC1");
        entity.setRoleCode("ROLE1");
        entity.setPriority(5);
        entity.setLangCode("eng");
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        when(appRolePriorityRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<AppRolePriorityDto>> future =
                syncMasterData.getAppRolePriorityDetails(lastUpdated, now);

        List<AppRolePriorityDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        AppRolePriorityDto dto = result.get(0);

        assertEquals("APP1", dto.getAppId());
        assertEquals("PROC1", dto.getProcessId());
        assertEquals("ROLE1", dto.getRoleCode());
        assertEquals(Integer.valueOf(5), dto.getPriority());
        assertEquals("eng", dto.getLangCode());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
    }

    @Test
    public void testGetAppRolePriorityDetails_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(appRolePriorityRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<AppRolePriorityDto>> future =
                syncMasterData.getAppRolePriorityDetails(lastUpdated, now);

        List<AppRolePriorityDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetAppRolePriorityDetails_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(appRolePriorityRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(appRolePriorityRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getAppRolePriorityDetails(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetScreenAuthorizationDetails_NoChangesFound() throws Exception {

        when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<ScreenAuthorizationDto>> future =
                syncMasterData.getScreenAuthorizationDetails(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<ScreenAuthorizationDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetScreenAuthorizationDetails_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(screenAuthorizationRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<ScreenAuthorizationDto>> future =
                syncMasterData.getScreenAuthorizationDetails(null, now);

        List<ScreenAuthorizationDto> result = future.get();

        assertNull(result); // convert returns null
    }

    @Test
    public void testGetScreenAuthorizationDetails_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        ScreenAuthorization entity = new ScreenAuthorization();
        entity.setScreenId("SCR001");
        entity.setRoleCode("ADMIN");
        entity.setIsPermitted(true);
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(screenAuthorizationRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<ScreenAuthorizationDto>> future =
                syncMasterData.getScreenAuthorizationDetails(lastUpdated, now);

        List<ScreenAuthorizationDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        ScreenAuthorizationDto dto = result.get(0);

        assertEquals("SCR001", dto.getScreenId());
        assertEquals("ADMIN", dto.getRoleCode());
        assertTrue(dto.getIsPermitted());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetScreenAuthorizationDetails_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(screenAuthorizationRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<ScreenAuthorizationDto>> future =
                syncMasterData.getScreenAuthorizationDetails(lastUpdated, now);

        List<ScreenAuthorizationDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetScreenAuthorizationDetails_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);
        when(screenAuthorizationRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(screenAuthorizationRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getScreenAuthorizationDetails(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetProcessList_NoChangesFound() throws Exception {

        when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<ProcessListDto>> future =
                syncMasterData.getProcessList(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<ProcessListDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetProcessList_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(processListRepository
                .findByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<ProcessListDto>> future =
                syncMasterData.getProcessList(null, now);

        List<ProcessListDto> result = future.get();

        assertNull(result); // convert returns null
    }

    @Test
    public void testGetProcessList_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        ProcessList entity = new ProcessList();
        entity.setId("P001");
        entity.setName("Registration");
        entity.setDescr("Registration Process");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(processListRepository
                .findByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<ProcessListDto>> future =
                syncMasterData.getProcessList(lastUpdated, now);

        List<ProcessListDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        ProcessListDto dto = result.get(0);

        assertEquals("P001", dto.getId());
        assertEquals("Registration", dto.getName());
        assertEquals("Registration Process", dto.getDescr());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetProcessList_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(processListRepository
                .findByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<ProcessListDto>> future =
                syncMasterData.getProcessList(lastUpdated, now);

        List<ProcessListDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetProcessList_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(processListRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(processListRepository
                .findByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getProcessList(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetSyncJobDefDetails_NoChangesFound() throws Exception {

        when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<SyncJobDefDto>> future =
                syncMasterData.getSyncJobDefDetails(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<SyncJobDefDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetSyncJobDefDetails_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(syncJobDefRepository
                .findLatestByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<SyncJobDefDto>> future =
                syncMasterData.getSyncJobDefDetails(null, now);

        List<SyncJobDefDto> result = future.get();

        assertNull(result); // convert method returns null
    }

    @Test
    public void testGetSyncJobDefDetails_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        SyncJobDef entity = new SyncJobDef();
        entity.setId("JOB001");
        entity.setName("Sync Master Data");
        entity.setApiName("syncMasterDataApi");
        entity.setParentSyncJobId("PARENT001");
        entity.setSyncFreq("DAILY");
        entity.setLockDuration("30");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(syncJobDefRepository
                .findLatestByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<SyncJobDefDto>> future =
                syncMasterData.getSyncJobDefDetails(lastUpdated, now);

        List<SyncJobDefDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        SyncJobDefDto dto = result.get(0);

        assertEquals("JOB001", dto.getId());
        assertEquals("Sync Master Data", dto.getName());
        assertEquals("syncMasterDataApi", dto.getApiName());
        assertEquals("PARENT001", dto.getParentSyncJobId());
        assertEquals("DAILY", dto.getSyncFreq());
        assertEquals("30", dto.getLockDuration());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetSyncJobDefDetails_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(syncJobDefRepository
                .findLatestByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<SyncJobDefDto>> future =
                syncMasterData.getSyncJobDefDetails(lastUpdated, now);

        List<SyncJobDefDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = AdminServiceException.class)
    public void testGetSyncJobDefDetails_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(syncJobDefRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(syncJobDefRepository
                .findLatestByLastUpdatedTimeAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getSyncJobDefDetails(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetScreenDetails_NoChangesFound() throws Exception {

        when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<ScreenDetailDto>> future =
                syncMasterData.getScreenDetails(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<ScreenDetailDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetScreenDetails_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(screenDetailRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<ScreenDetailDto>> future =
                syncMasterData.getScreenDetails(null, now);

        List<ScreenDetailDto> result = future.get();

        assertNull(result); // convert returns null
    }

    @Test
    public void testGetScreenDetails_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        ScreenDetail entity = new ScreenDetail();
        entity.setId("SCR001");
        entity.setAppId("APP01");
        entity.setName("Home Screen");
        entity.setDescr("Main Screen");
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setLangCode("eng");

        when(screenDetailRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<ScreenDetailDto>> future =
                syncMasterData.getScreenDetails(lastUpdated, now);

        List<ScreenDetailDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        ScreenDetailDto dto = result.get(0);

        assertEquals("SCR001", dto.getId());
        assertEquals("APP01", dto.getAppId());
        assertEquals("Home Screen", dto.getName());
        assertEquals("Main Screen", dto.getDescr());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
        assertEquals("eng", dto.getLangCode());
    }

    @Test
    public void testGetScreenDetails_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(screenDetailRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<ScreenDetailDto>> future =
                syncMasterData.getScreenDetails(lastUpdated, now);

        List<ScreenDetailDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetScreenDetails_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(screenDetailRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(screenDetailRepository
                .findByLastUpdatedAndCurrentTimeStamp(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getScreenDetails(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetPermittedConfig_NoChangesFound() throws Exception {

        when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<PermittedConfigDto>> future =
                syncMasterData.getPermittedConfig(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<PermittedConfigDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetPermittedConfig_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(permittedLocalConfigRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(Collections.emptyList());

        CompletableFuture<List<PermittedConfigDto>> future =
                syncMasterData.getPermittedConfig(null, now);

        List<PermittedConfigDto> result = future.get();

        assertNull(result); // convert returns null
    }

    @Test
    public void testGetPermittedConfig_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        PermittedLocalConfig entity = new PermittedLocalConfig();
        entity.setCode("CFG001");
        entity.setName("BiometricEnabled");
        entity.setType("BOOLEAN");
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        when(permittedLocalConfigRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<PermittedConfigDto>> future =
                syncMasterData.getPermittedConfig(lastUpdated, now);

        List<PermittedConfigDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        PermittedConfigDto dto = result.get(0);

        assertEquals("CFG001", dto.getCode());
        assertEquals("BiometricEnabled", dto.getName());
        assertEquals("BOOLEAN", dto.getType());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
    }

    @Test
    public void testGetPermittedConfig_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(permittedLocalConfigRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<PermittedConfigDto>> future =
                syncMasterData.getPermittedConfig(lastUpdated, now);

        List<PermittedConfigDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetPermittedConfig_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(permittedLocalConfigRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(permittedLocalConfigRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getPermittedConfig(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetLanguageList_NoChangesFound() throws Exception {

        when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(null);  // makes isChangesFound return false

        CompletableFuture<List<LanguageDto>> future =
                syncMasterData.getLanguageList(
                        LocalDateTime.now(ZoneOffset.UTC).minusDays(1),
                        LocalDateTime.now(ZoneOffset.UTC));

        List<LanguageDto> result = future.get();

        assertNull(result);
    }

    @Test
    public void testGetLanguageList_LastUpdatedNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        CompletableFuture<List<LanguageDto>> future =
                syncMasterData.getLanguageList(null, now);

        List<LanguageDto> result = future.get();

        assertNull(result); // convert returns null
    }

    @Test
    public void testGetLanguageList_WithData_ShouldMapFields() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        Language entity = new Language();
        entity.setCode("eng");
        entity.setName("English");
        entity.setFamily("Indo-European");
        entity.setNativeName("English");
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        when(languageRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(Collections.singletonList(entity));

        CompletableFuture<List<LanguageDto>> future =
                syncMasterData.getLanguageList(lastUpdated, now);

        List<LanguageDto> result = future.get();

        assertNotNull(result);
        assertEquals(1, result.size());

        LanguageDto dto = result.get(0);

        assertEquals("eng", dto.getCode());
        assertEquals("English", dto.getName());
        assertEquals("Indo-European", dto.getFamily());
        assertEquals("English", dto.getNativeName());
        assertTrue(dto.getIsActive());
        assertFalse(dto.getIsDeleted());
    }

    @Test
    public void testGetLanguageList_RepositoryReturnsNull() throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(1);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(languageRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(null);

        CompletableFuture<List<LanguageDto>> future =
                syncMasterData.getLanguageList(lastUpdated, now);

        List<LanguageDto> result = future.get();

        assertNull(result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetLanguageList_DataAccessException() throws Throwable {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime lastUpdated = now.minusDays(2);

        EntityDtimes entityDtimes = new EntityDtimes(now, null, null);

        when(languageRepository.getMaxCreatedDateTimeMaxUpdatedDateTime())
                .thenReturn(entityDtimes);

        when(languageRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenThrow(new DataAccessResourceFailureException("DB error"));

        try {
            syncMasterData
                    .getLanguageList(lastUpdated, now)
                    .get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetRegistrationCenterMachine_machineNotFound() {
        String keyIndex = "KEY1";
        when(machineRepository.findOneByKeyIndexIgnoreCase(keyIndex)).thenReturn(null);

        RequestException exception = assertThrows(RequestException.class,
                () -> realHelper.getRegistrationCenterMachine("RC1", keyIndex));
        assertEquals(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetRegistrationCenterMachine_registrationCenterNotFound() {
        Machine machine = new Machine();
        machine.setRegCenterId(null);
        when(machineRepository.findOneByKeyIndexIgnoreCase("KEY2")).thenReturn(machine);

        RequestException exception = assertThrows(RequestException.class,
                () -> realHelper.getRegistrationCenterMachine("RC1", "KEY2"));
        assertEquals(MasterDataErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetRegistrationCenterMachine_registrationCenterMismatch() {
        Machine machine = new Machine();
        machine.setRegCenterId("RC2");
        when(machineRepository.findOneByKeyIndexIgnoreCase("KEY3")).thenReturn(machine);

        RequestException exception = assertThrows(RequestException.class,
                () -> realHelper.getRegistrationCenterMachine("RC1", "KEY3"));
        assertEquals(MasterDataErrorCode.REG_CENTER_UPDATED.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetRegistrationCenterMachine_success() throws SyncDataServiceException {
        Machine machine = new Machine();
        machine.setId("M1");
        machine.setRegCenterId("RC1");
        machine.setPublicKey("PUBKEY");
        machine.setMachineSpecId("SPEC1");

        MachineSpecification spec = new MachineSpecification();
        spec.setMachineTypeCode("TYPE1");
        machine.setMachineSpecification(spec);

        when(machineRepository.findOneByKeyIndexIgnoreCase("KEY4")).thenReturn(machine);

        RegistrationCenterMachineDto dto = realHelper.getRegistrationCenterMachine("RC1", "KEY4");

        assertNotNull(dto);
        assertEquals("M1", dto.getMachineId());
        assertEquals("PUBKEY", dto.getPublicKey());
        assertEquals("SPEC1", dto.getMachineSpecId());
    }

    @Test
    public void testGetRegistrationCenterMachine_machineSpecNull() throws SyncDataServiceException {
        Machine machine = new Machine();
        machine.setId("M2");
        machine.setRegCenterId("RC1");
        machine.setPublicKey("PUBKEY");
        machine.setMachineSpecId("SPEC2");
        machine.setMachineSpecification(null);

        when(machineRepository.findOneByKeyIndexIgnoreCase("KEY5")).thenReturn(machine);

        RegistrationCenterMachineDto dto = realHelper.getRegistrationCenterMachine("RC1", "KEY5");

        assertNotNull(dto);
    }

    @Test
    public void testGetRegistrationCenterMachine_dataAccessException() {
        when(machineRepository.findOneByKeyIndexIgnoreCase("KEY6"))
                .thenThrow(new DataAccessException("DB error") {});

        SyncDataServiceException exception = assertThrows(SyncDataServiceException.class,
                () -> realHelper.getRegistrationCenterMachine("RC1", "KEY6"));

        assertEquals(MasterDataErrorCode.REG_CENTER_MACHINE_FETCH_EXCEPTION.getErrorCode(),
                exception.getErrorCode());
    }

    @Test
    public void testGetSyncDataBaseDtoV2_entitiesNull() {
        List<SyncDataBaseDto> result = new ArrayList<>();
        realHelper.getSyncDataBaseDtoV2("entityName", "entityType", null,
                registrationCenterMachineDto, result);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSyncDataBaseDtoV2_entitiesAllNull() {
        List<Object> entities = Arrays.asList(null, null);
        List<SyncDataBaseDto> result = new ArrayList<>();
        realHelper.getSyncDataBaseDtoV2("entityName", "entityType", entities,
                registrationCenterMachineDto, result);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSyncDataBaseDtoV2_success() throws Exception {
        List<Object> entities = Arrays.asList("val1", "val2");
        List<SyncDataBaseDto> result = new ArrayList<>();

        // Mock crypto service
        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("ENCRYPTED_DATA");
        realHelper.getSyncDataBaseDtoV2("entityName", "entityType", entities,
                registrationCenterMachineDto, result);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetSyncDataBaseDtoV2_mapperThrowsException() throws Exception {
        List<Object> entities = Arrays.asList("val1");
        List<SyncDataBaseDto> result = new ArrayList<>();

        // Should catch exception and result remains empty
        realHelper.getSyncDataBaseDtoV2("entityName", "entityType", entities,
                registrationCenterMachineDto, result);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSyncDataBaseDto_entitiesNull() {
        List<SyncDataBaseDto> result = new ArrayList<>();
        realHelper.getSyncDataBaseDto("entityName", "entityType", null,
                registrationCenterMachineDto, result);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSyncDataBaseDto_entitiesAllNull() {
        List<Object> entities = Arrays.asList(null, null);
        List<SyncDataBaseDto> result = new ArrayList<>();
        realHelper.getSyncDataBaseDto("entityName", "entityType", entities,
                registrationCenterMachineDto, result);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSyncDataBaseDto_blocklistedWords() throws Exception {
        List<Object> entities = Arrays.asList("val1");
        List<SyncDataBaseDto> result = new ArrayList<>();

        realHelper.getSyncDataBaseDto(BlocklistedWords.class.getSimpleName(), "entityType",
                entities, registrationCenterMachineDto, result);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetSyncDataBaseDto_mapperThrowsPerEntity() {
        List<Object> entities = Arrays.asList("val1", "val2");
        List<SyncDataBaseDto> result = new ArrayList<>();

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("ENCRYPTED_DATA");

        realHelper.getSyncDataBaseDto("entityName", "entityType", entities,
                registrationCenterMachineDto, result);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllDynamicFields_success() throws Exception {

        ReflectionTestUtils.setField(realHelper, "dynamicfieldUrl", "http://localhost/dynamic-fields");
        // --- Prepare mock dynamic fields ---
        DynamicFieldDto field1 = new DynamicFieldDto();
        field1.setId("f1");
        field1.setName("Field1");

        DynamicFieldDto field2 = new DynamicFieldDto();
        field2.setId("f2");
        field2.setName("Field2");

        // --- Prepare paged responses as JSON ---
        String resp0Json = "{ \"response\": { \"data\": [ {\"id\": \"f1\", \"name\": \"Field1\"} ], \"totalPages\": 2 } }";
        String resp1Json = "{ \"response\": { \"data\": [ {\"id\": \"f2\", \"name\": \"Field2\"} ], \"totalPages\": 2 } }";

        doReturn(new ResponseEntity<>(resp1Json, HttpStatus.OK))
                .when(restTemplate).getForEntity(any(URI.class), eq(String.class));

        // --- Call the async method ---
        CompletableFuture<List<DynamicFieldDto>> future = realHelper.getAllDynamicFields(null, restTemplate);

        // Wait for completion
        List<DynamicFieldDto> result = future.get();

        // --- Assertions ---
        assertEquals(2, result.size());
        assertEquals("f2", result.get(0).getId());
        assertEquals("Field2", result.get(0).getName());
    }

    @Test
    public void testGetAllDynamicFields_WithLastUpdated_ShouldSuccess() throws Exception {
        // --- Step 1: Set the dynamicfieldUrl in the helper ---
        ReflectionTestUtils.setField(realHelper, "dynamicfieldUrl", "http://localhost/dynamic-fields");
        realHelper = new SyncMasterDataServiceHelper();

        // Inject the mock RestTemplate
        ReflectionTestUtils.setField(realHelper, "restTemplate", restTemplate);

        // Inject dynamicfieldUrl (cannot be null)
        ReflectionTestUtils.setField(realHelper, "dynamicfieldUrl", "http://localhost/dynamic-fields");

        // Inject ObjectMapper
        ReflectionTestUtils.setField(realHelper, "objectMapper", new ObjectMapper());

        // --- Step 2: Prepare mock dynamic fields ---
        DynamicFieldDto field1 = new DynamicFieldDto();
        field1.setId("f1");
        field1.setName("Field1");

        DynamicFieldDto field2 = new DynamicFieldDto();
        field2.setId("f2");
        field2.setName("Field2");

        // --- Step 3: Prepare paged responses ---
        PageDto<DynamicFieldDto> page0 = new PageDto<>();
        page0.setData(Collections.singletonList(field1));
        page0.setTotalPages(2);
        page0.setPageNo(0);

        PageDto<DynamicFieldDto> page1 = new PageDto<>();
        page1.setData(Collections.singletonList(field2));
        page1.setTotalPages(2);
        page1.setPageNo(1);

        ResponseWrapper<PageDto<DynamicFieldDto>> resp0 = new ResponseWrapper<>();
        resp0.setResponse(page0);

        ResponseWrapper<PageDto<DynamicFieldDto>> resp1 = new ResponseWrapper<>();
        resp1.setResponse(page1);

        // --- Step 4: Mock RestTemplate calls ---
        doReturn(new ResponseEntity<>("{\"response\":{\"data\":[{\"id\":\"f1\",\"name\":\"Field1\"}],\"totalPages\":2}}", HttpStatus.OK))
                .doReturn(new ResponseEntity<>("{\"response\":{\"data\":[{\"id\":\"f2\",\"name\":\"Field2\"}],\"totalPages\":2}}", HttpStatus.OK))
                .when(restTemplate).getForEntity(any(URI.class), eq(String.class));

        // --- Step 5: Call the async method ---
        CompletableFuture<List<DynamicFieldDto>> future = realHelper.getAllDynamicFields(null);
        List<DynamicFieldDto> result = future.get(); // block until completion

        // --- Step 6: Assertions ---
        assertEquals(2, result.size());
        assertEquals("f1", result.get(0).getId());
        assertEquals("Field1", result.get(0).getName());
        assertEquals("f2", result.get(1).getId());
        assertEquals("Field2", result.get(1).getName());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetAllDynamicFields_restTemplateException() throws Exception {
        // Set the dynamicfieldUrl
        ReflectionTestUtils.setField(realHelper, "dynamicfieldUrl", "http://localhost/dynamic-fields");

        // Call the method (should throw SyncDataServiceException)
        realHelper.getAllDynamicFields(null).get();
    }
}
