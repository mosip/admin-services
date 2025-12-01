package io.mosip.kernel.syncdata.test.helper;


import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.RegistrationCenterMachineDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.AppAuthenticationMethod;
import io.mosip.kernel.syncdata.entity.Holiday;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ClientSettingsHelperTest {

    @InjectMocks
    private ClientSettingsHelper helper;

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private Environment environment;

    @Mock
    private MapperUtils mapper;

    @Mock
    private ClientCryptoManagerService clientCryptoManagerService;


    // ---------------------------------------------------------------------
    // hasURLDetails (private)
    // ---------------------------------------------------------------------

    @Test
    public void testHasURLDetailsNoPropertyReturnsFalse() {
        when(environment.containsProperty(anyString())).thenReturn(false);
        boolean result = ReflectionTestUtils.invokeMethod(
                helper,
                "hasURLDetails",
                AppAuthenticationMethod.class,
                true,   // isV2API
                false   // deltaSync
        );
        assertFalse(result);
    }

    @Test
    public void testHasURLDetailsOnlyOnFullSyncTrueAndDeltaSyncTrueReturnsFalse() {
        when(environment.containsProperty("mosip.sync.entity.url.APPAUTHENTICATIONMETHOD")).thenReturn(true);
        when(environment.getProperty(
                eq("mosip.sync.entity.only-on-fullsync.APPAUTHENTICATIONMETHOD"),
                eq(Boolean.class),
                eq(true)
        )).thenReturn(true); // onlyOnFullSync = true
        boolean result = ReflectionTestUtils.invokeMethod(
                helper,
                "hasURLDetails",
                AppAuthenticationMethod.class,
                true,   // isV2API
                true    // deltaSync
        );
        assertFalse(result);
    }

    @Test
    public void testHasURLDetailsOnlyOnFullSyncFalseAndDeltaSyncTrueReturnsTrue() {
        when(environment.containsProperty("mosip.sync.entity.url.APPAUTHENTICATIONMETHOD")).thenReturn(true);
        when(environment.getProperty(
                eq("mosip.sync.entity.only-on-fullsync.APPAUTHENTICATIONMETHOD"),
                eq(Boolean.class),
                eq(true)
        )).thenReturn(false); // onlyOnFullSync = false
        boolean result = ReflectionTestUtils.invokeMethod(
                helper,
                "hasURLDetails",
                AppAuthenticationMethod.class,
                true,   // isV2API
                true    // deltaSync
        );
        assertTrue(result);
    }

    // ---------------------------------------------------------------------
    // getLastUpdatedTimeFromEntity (private)
    // ---------------------------------------------------------------------

    @Test
    public void testGetLastUpdatedTimeFromEntityFullSyncEntityReturnsNull() {
        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        List<String> fullSync = Collections.singletonList("Machine");
        LocalDateTime result = ReflectionTestUtils.invokeMethod(
                helper,
                "getLastUpdatedTimeFromEntity",
                io.mosip.kernel.syncdata.entity.Machine.class,
                lastUpdated,
                fullSync
        );
        assertNull(result);
    }

    @Test
    public void testGetLastUpdatedTimeFromEntityNotFullSyncEntityReturnsOriginal() {
        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        List<String> fullSync = Collections.singletonList("Holiday");
        LocalDateTime result = ReflectionTestUtils.invokeMethod(
                helper,
                "getLastUpdatedTimeFromEntity",
                io.mosip.kernel.syncdata.entity.Machine.class,
                lastUpdated,
                fullSync
        );
        assertEquals(lastUpdated, result);
    }

    // ---------------------------------------------------------------------
    // getInitiateDataFetch
    // ---------------------------------------------------------------------

    @Test
    public void testGetInitiateDataFetchFullSyncMachinePassesNullLastUpdatedForMachine() {
        String machineId = "M1";
        String regCenterId = "RC1";
        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime current = LocalDateTime.now();
        // No URL details -> fall back to serviceHelper for all entities
        when(serviceHelper.getAppAuthenticationMethodDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getAppRolePriorityDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getRegistrationCenter(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getMachines(any(), any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getTemplates(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getDocumentTypes(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getApplicantValidDocument(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLocationHierarchy(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getReasonCategory(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getReasonList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getHolidays(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getBlackListedWords(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getScreenAuthorizationDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getScreenDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getProcessList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getSyncJobDefDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getPermittedConfig(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLanguageList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getTemplateFileFormats(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getTemplateTypes(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getRegistrationCenterMachines(any(), any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getRegistrationCenterUsers(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getValidDocuments(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLocationHierarchyList(any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getAllDynamicFields(any())).thenReturn(mock(CompletableFuture.class));
        Map<Class<?>, CompletableFuture<?>> result = helper.getInitiateDataFetch(
                machineId,
                regCenterId,
                lastUpdated,
                current,
                false,          // isV2API
                false,          // deltaSync
                "Machine"       // full sync only for Machine
        );
        assertTrue(result.containsKey(io.mosip.kernel.syncdata.entity.Machine.class));
        // Verify getMachines called with lastUpdated = null for full-sync
        verify(serviceHelper).getMachines(eq(regCenterId), isNull(), eq(current), eq(machineId));
    }

    @Test
    public void testGetInitiateDataFetchUsesUrlDetailsWhenConfigured() {
        String machineId = "M1";
        String regCenterId = "RC1";
        LocalDateTime lastUpdated = LocalDateTime.now().minusDays(1);
        LocalDateTime current = LocalDateTime.now();
        when(environment.containsProperty("mosip.sync.entity.url.APPAUTHENTICATIONMETHOD")).thenReturn(true);
        when(environment.getProperty(
                eq("mosip.sync.entity.only-on-fullsync.APPAUTHENTICATIONMETHOD"),
                eq(Boolean.class),
                eq(true)
        )).thenReturn(false);
        when(environment.getProperty(
                "mosip.sync.entity.url.APPAUTHENTICATIONMETHOD"
        )).thenReturn("http://example.com/app-auth");
        when(environment.getProperty(
                "mosip.sync.entity.auth-required.APPAUTHENTICATIONMETHOD"
        )).thenReturn("true");
        when(environment.getProperty(
                "mosip.sync.entity.auth-token.APPAUTHENTICATIONMETHOD"
        )).thenReturn("token");
        when(environment.getProperty(
                "mosip.sync.entity.encrypted.APPAUTHENTICATIONMETHOD"
        )).thenReturn("false");
        when(environment.getProperty(
                "mosip.sync.entity.headers.APPAUTHENTICATIONMETHOD"
        )).thenReturn("header1:value1");
        when(serviceHelper.getAppRolePriorityDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getRegistrationCenter(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getMachines(any(), any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getTemplates(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getDocumentTypes(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getApplicantValidDocument(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLocationHierarchy(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getReasonCategory(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getReasonList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getHolidays(any(), any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getBlackListedWords(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getScreenAuthorizationDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getScreenDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getProcessList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getSyncJobDefDetails(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getPermittedConfig(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLanguageList(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getLocationHierarchyList(any())).thenReturn(mock(CompletableFuture.class));
        when(serviceHelper.getAllDynamicFields(any())).thenReturn(mock(CompletableFuture.class));
        Map<Class<?>, CompletableFuture<?>> result = helper.getInitiateDataFetch(
                machineId,
                regCenterId,
                lastUpdated,
                current,
                true,       // isV2API
                false,      // deltaSync
                null
        );
        CompletableFuture<?> future = (CompletableFuture<?>) result.get(AppAuthenticationMethod.class);
        assertNotNull(future);
        @SuppressWarnings("unchecked")
        Map<String, Object> urlDetails = (Map<String, Object>) future.join();
        assertEquals("http://example.com/app-auth", urlDetails.get("url"));
        assertEquals("true", urlDetails.get("auth-required"));
        assertEquals("token", urlDetails.get("auth-token"));
        assertEquals("false", urlDetails.get("encrypted"));
        assertEquals("header1:value1", urlDetails.get("headers"));
        // Service helper should NOT be called for AppAuthenticationMethod when URL details are present
        verify(serviceHelper, never()).getAppAuthenticationMethodDetails(any(), any());
    }

    // ---------------------------------------------------------------------
    // retrieveData + processEntry + getEncryptedSyncDataBaseDto
    // ---------------------------------------------------------------------

    @Test
    public void testRetrieveDataStructuredUrlSuccess() throws Exception {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", "http://example.com");
        futures.put(AppAuthenticationMethod.class, CompletableFuture.completedFuture(urlMap));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        regCenterMachineDto.setPublicKey("PUB_KEY");
        regCenterMachineDto.setClientType(ClientType.LOCAL);
        when(mapper.getObjectAsJsonString(urlMap)).thenReturn("{\"url\":\"http://example.com\"}");
        TpmCryptoResponseDto enc = new TpmCryptoResponseDto();
        enc.setValue("ENC_VALUE");
        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class))).thenReturn(enc);
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, true);
        assertEquals(1, list.size());
        SyncDataBaseDto dto = list.get(0);
        assertNotNull(dto);
    }

    @Test
    public void testRetrieveDataStructuredUrlEncryptionErrorReturnsEmpty() throws Exception {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", "http://example.com");
        futures.put(AppAuthenticationMethod.class, CompletableFuture.completedFuture(urlMap));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        regCenterMachineDto.setPublicKey("PUB_KEY");
        regCenterMachineDto.setClientType(ClientType.LOCAL);
        when(mapper.getObjectAsJsonString(urlMap)).thenReturn("{\"url\":\"http://example.com\"}");
        when(clientCryptoManagerService.csEncrypt(
                any(TpmCryptoRequestDto.class)
        )).thenThrow(new RuntimeException("enc error"));
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, true);
        // error is caught and entry skipped
        assertTrue(list.isEmpty());
    }

    @Test
    public void testRetrieveDataDynamicV2UsesServiceHelperV2() {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        DynamicFieldDto d1 = new DynamicFieldDto();
        d1.setName("fieldName");
        DynamicFieldDto d2 = new DynamicFieldDto();
        d2.setName("fieldName");
        List<DynamicFieldDto> dynamicList = Arrays.asList(d1, d2);
        futures.put(DynamicFieldDto.class, CompletableFuture.completedFuture(dynamicList));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        // When V2 : getSyncDataBaseDtoV2 should be used once, with grouped list
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String entityType = invocation.getArgument(1);
            @SuppressWarnings("unchecked")
            List<DynamicFieldDto> data = (List<DynamicFieldDto>) invocation.getArgument(2);
            @SuppressWarnings("unchecked")
            List<SyncDataBaseDto> out = (List<SyncDataBaseDto>) invocation.getArgument(4);
            assertEquals("fieldName", key);
            assertEquals("dynamic", entityType);
            assertEquals(2, data.size());
            out.add(new SyncDataBaseDto(key, entityType, "payload"));
            return null;
        }).when(serviceHelper).getSyncDataBaseDtoV2(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, true);
        assertEquals(1, list.size());
        verify(serviceHelper, times(1)).getSyncDataBaseDtoV2(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
    }

    @Test
    public void testRetrieveDataDynamicNonV2UsesServiceHelperLegacy() {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        DynamicFieldDto d1 = new DynamicFieldDto();
        d1.setName("fieldName");
        DynamicFieldDto d2 = new DynamicFieldDto();
        d2.setName("fieldName");
        List<DynamicFieldDto> dynamicList = Arrays.asList(d1, d2);
        futures.put(DynamicFieldDto.class, CompletableFuture.completedFuture(dynamicList));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String entityType = invocation.getArgument(1);
            @SuppressWarnings("unchecked")
            List<DynamicFieldDto> data = (List<DynamicFieldDto>) invocation.getArgument(2);
            @SuppressWarnings("unchecked")
            List<SyncDataBaseDto> out = (List<SyncDataBaseDto>) invocation.getArgument(4);
            assertEquals("fieldName", key);
            assertEquals("dynamic", entityType);
            assertEquals(2, data.size());
            out.add(new SyncDataBaseDto(key, entityType, "payload"));
            return null;
        }).when(serviceHelper).getSyncDataBaseDto(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, false);
        assertEquals(1, list.size());
        verify(serviceHelper, times(1)).getSyncDataBaseDto(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
    }

    @Test
    public void testRetrieveDataStructuredV2UsesSyncDataBaseDtoV2() {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        List<String> data = Collections.singletonList("val");
        futures.put(Holiday.class, CompletableFuture.completedFuture(data));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String entityType = invocation.getArgument(1);
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) invocation.getArgument(2);
            @SuppressWarnings("unchecked")
            List<SyncDataBaseDto> out = (List<SyncDataBaseDto>) invocation.getArgument(4);
            assertEquals("Holiday", key);
            assertEquals("structured", entityType);
            assertEquals(1, list.size());
            out.add(new SyncDataBaseDto(key, entityType, "payload"));
            return null;
        }).when(serviceHelper).getSyncDataBaseDtoV2(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, true);
        assertEquals(1, list.size());
        verify(serviceHelper, times(1)).getSyncDataBaseDtoV2(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
    }

    @Test
    public void testRetrieveDataStructuredNonV2UsesSyncDataBaseDto() {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        List<String> data = Collections.singletonList("val");
        futures.put(Holiday.class, CompletableFuture.completedFuture(data));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String entityType = invocation.getArgument(1);
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) invocation.getArgument(2);
            @SuppressWarnings("unchecked")
            List<SyncDataBaseDto> out = (List<SyncDataBaseDto>) invocation.getArgument(4);
            assertEquals("Holiday", key);
            assertEquals("structured", entityType);
            assertEquals(1, list.size());
            out.add(new SyncDataBaseDto(key, entityType, "payload"));
            return null;
        }).when(serviceHelper).getSyncDataBaseDto(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
        List<SyncDataBaseDto> list = helper.retrieveData(futures, regCenterMachineDto, false);
        assertEquals(1, list.size());
        verify(serviceHelper, times(1)).getSyncDataBaseDto(
                anyString(),
                anyString(),
                anyList(),
                eq(regCenterMachineDto),
                anyList()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testRetrieveDataFutureThrowsRuntimeExceptionWrappedInRuntimeException() {
        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        CompletableFuture<Object> failed = new CompletableFuture<>();
        failed.completeExceptionally(new RuntimeException("boom"));
        futures.put(AppAuthenticationMethod.class, failed);
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        helper.retrieveData(futures, regCenterMachineDto, true);
    }

    // ---------------------------------------------------------------------
    // getConfiguredScriptUrlDetail
    // ---------------------------------------------------------------------

    @Test
    public void testGetConfiguredScriptUrlDetailSuccess() throws Exception {
        // Prepare scriptNames and init() (PostConstruct)
        Set<String> scriptNames = new HashSet<>();
        scriptNames.add("applicanttype.mvel");
        ReflectionTestUtils.setField(helper, "scriptNames", scriptNames);
        // Init will call buildUrlDetailMap() for each script
        when(environment.getProperty(anyString())).thenReturn(null);
        ReflectionTestUtils.invokeMethod(helper, "init");
        // Script detail -> JSON -> base64 -> encrypt
        when(mapper.getObjectAsJsonString(anyMap())).thenReturn("{\"key\":\"value\"}");
        TpmCryptoResponseDto enc = new TpmCryptoResponseDto();
        enc.setValue("ENC_SCRIPT");
        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class))).thenReturn(enc);
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        regCenterMachineDto.setPublicKey("PUB_KEY");
        regCenterMachineDto.setClientType(ClientType.LOCAL);
        List<SyncDataBaseDto> list = helper.getConfiguredScriptUrlDetail(regCenterMachineDto);
        assertEquals(1, list.size());
        SyncDataBaseDto dto = list.get(0);
        assertNotNull(dto);
    }

    @Test
    public void testGetConfiguredScriptUrlDetailMapperThrowsExceptionIsSwallowed() throws Exception {
        Set<String> scriptNames = new HashSet<>();
        scriptNames.add("applicanttype.mvel");
        ReflectionTestUtils.setField(helper, "scriptNames", scriptNames);
        when(environment.getProperty(anyString())).thenReturn(null);
        ReflectionTestUtils.invokeMethod(helper, "init");
        when(mapper.getObjectAsJsonString(anyMap())).thenThrow(new RuntimeException("json error"));
        RegistrationCenterMachineDto regCenterMachineDto = new RegistrationCenterMachineDto();
        regCenterMachineDto.setPublicKey("PUB_KEY");
        regCenterMachineDto.setClientType(ClientType.LOCAL);
        List<SyncDataBaseDto> list = helper.getConfiguredScriptUrlDetail(regCenterMachineDto);
        // Error is logged, script is skipped
        assertTrue(list.isEmpty());
    }

}