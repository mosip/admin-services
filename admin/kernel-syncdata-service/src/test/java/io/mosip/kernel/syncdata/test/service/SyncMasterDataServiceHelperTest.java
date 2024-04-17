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
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
import io.mosip.kernel.syncdata.service.impl.SyncMasterDataServiceImpl;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class SyncMasterDataServiceHelperTest {

    @Autowired
    private SyncMasterDataServiceHelper syncMasterDataServiceHelper;

    @Autowired
    private ClientSettingsHelper clientSettingsHelper;

    @Autowired
    private SyncMasterDataServiceImpl syncMasterDataService;

    @Autowired
    private SyncJobHelperService syncJobHelperService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate selfTokenRestTemplate;

    @Autowired
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

       Assert.assertTrue(afterburnerPresent);
       Assert.assertTrue(javaTimeModulePresent);
    }

    @Test
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
        Assert.assertNotNull(syncDataResponseDto.getDataToSync());

        SyncDataResponseDto syncDataResponseDeltaDto = syncMasterDataService.syncClientSettings("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                lastUpdated, syncJobHelperService.getDeltaSyncCurrentTimestamp());
        Assert.assertNotNull(syncDataResponseDeltaDto.getDataToSync());

        Assert.assertEquals(syncDataResponseDto.getDataToSync().size(), syncDataResponseDeltaDto.getDataToSync().size(), 12);
    }

    @Test
    public void getInitiateDataFetchV2Test() throws Throwable {
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

        SyncDataResponseDto syncDataResponseDto = syncMasterDataService.syncClientSettingsV2("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                null, syncJobHelperService.getDeltaSyncCurrentTimestamp(), "1.2.0", "");
        Assert.assertNotNull(syncDataResponseDto.getDataToSync());

        SyncDataResponseDto syncDataResponseDeltaDto = syncMasterDataService.syncClientSettingsV2("10001",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29",
                lastUpdated, syncJobHelperService.getFullSyncCurrentTimestamp(), "1.2.0", "LocationHierarchy");
        Assert.assertNotNull(syncDataResponseDto.getDataToSync());

        Assert.assertEquals(syncDataResponseDto.getDataToSync().size(), syncDataResponseDeltaDto.getDataToSync().size(), 5);
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
        Assert.assertEquals("KER-SNC-170", errorCode);
    }

    @Test
    public void getClientSettingsJsonFileTest() throws Exception {
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
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getHeaders().get("file-signature"));
        Assert.assertNotNull(responseEntity.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
    }
    
    @Test
    public void getClientSettingsJsonFileTest3() throws Exception {
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

        ResponseEntity responseEntity = syncMasterDataService.getClientSettingsJsonFile("LOCATIONHIERARCHY",
                "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29");
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getHeaders().get("file-signature"));
        Assert.assertNotNull(responseEntity.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
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

        List<AppRolePriorityDto> result = (List<AppRolePriorityDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertAppRolePrioritiesToDto",appRolePriorities);
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

        List<ScreenAuthorizationDto> result = (List<ScreenAuthorizationDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertScreenAuthorizationToDto",screenAuthorizationList);
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

        List<ProcessListDto> result = (List<ProcessListDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertprocessListEntityToDto",processList);
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

        List<SyncJobDefDto> result = (List<SyncJobDefDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertSyncJobDefEntityToDto",syncJobDefs);
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

        List<ScreenDetailDto> result = (List<ScreenDetailDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertScreenDetailToDto",screenDetails);
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

        List<PermittedConfigDto> result = (List<PermittedConfigDto>) ReflectionTestUtils.invokeMethod(syncMasterDataServiceHelper,"convertPermittedConfigEntityToDto",PermittedLocalConfigList);
        assertNotNull(result);
        assertEquals(PermittedLocalConfigList.size(), result.size());
    }

    @Test (expected = RequestException.class)
    public void getRegistrationCenterMachine_withInValidInput_returnException(){
        RegistrationCenterMachineDto registrationCenterMachineDto = new RegistrationCenterMachineDto();
        registrationCenterMachineDto.setMachineId("id");
        registrationCenterMachineDto.setMachineSpecId("spec");
        registrationCenterMachineDto.setRegCenterId("reg");
        registrationCenterMachineDto.setPublicKey("key");
        registrationCenterMachineDto.setMachineTypeId("type");
        registrationCenterMachineDto.setClientType(ClientType.ANDROID);
        registrationCenterMachineDto.setIsActive(true);
        registrationCenterMachineDto.setLangCode("eng");

        String registrationCenterId = "reg";
        String keyIndex = "key";

        syncMasterDataServiceHelper.getRegistrationCenterMachine(registrationCenterId,keyIndex);
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
}
