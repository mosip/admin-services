package io.mosip.kernel.syncdata.test.service.helper;

import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.RegistrationCenterMachineDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataBaseDto;
import io.mosip.kernel.syncdata.entity.*;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientSettingsHelperTest {

    @InjectMocks
    private ClientSettingsHelper clientSettingsHelper;

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private ModuleDetailRepository moduleDetailRepository;

    @Mock
    private Environment environment;

    @Mock
    private MapperUtils mapper;

    @Mock
    private ClientCryptoManagerService clientCryptoManagerService;

    private RegistrationCenterMachineDto machineDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(clientSettingsHelper, "regClientModuleId", "10002");
        ReflectionTestUtils.setField(clientSettingsHelper, "scriptNames",
                Set.of("testscript.mvel"));

        machineDto = new RegistrationCenterMachineDto();
        machineDto.setPublicKey("publicKey");
        machineDto.setClientType(ClientType.TPM);

        // Lenient stubs for all serviceHelper methods called by getInitiateDataFetch
        lenient().when(serviceHelper.getAppAuthenticationMethodDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getAppRolePriorityDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getMachines(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getRegistrationCenter(any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getTemplates(any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getDocumentTypes(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getApplicantValidDocument(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getLocationHierarchy(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getReasonCategory(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getReasonList(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getHolidays(any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getBlackListedWords(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getScreenAuthorizationDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getScreenDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getProcessList(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getSyncJobDefDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getPermittedConfig(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getLanguageList(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getTemplateFileFormats(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getTemplateTypes(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getRegistrationCenterMachines(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getRegistrationCenterUsers(any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getValidDocuments(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getLocationHierarchyList(any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));
        lenient().when(serviceHelper.getAllDynamicFields(any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        // @PostConstruct is not invoked by @InjectMocks — call init() manually
        ReflectionTestUtils.invokeMethod(clientSettingsHelper, "init");
    }

    @Test
    void testGetInitiateDataFetch_nonV2API() {

        when(serviceHelper.getAppAuthenticationMethodDetails(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        Map<Class<?>, CompletableFuture<?>> result =
                clientSettingsHelper.getInitiateDataFetch(
                        "machine1",
                        "center1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        false,
                        null
                );

        assertNotNull(result);
        assertTrue(result.containsKey(AppAuthenticationMethod.class));

        verify(serviceHelper, atLeastOnce())
                .getAppAuthenticationMethodDetails(any(), any());
    }

    @Test
    void testRetrieveData_structured_v2() throws Exception {

        List<String> data = List.of("test");

        CompletableFuture<List<String>> future =
                CompletableFuture.completedFuture(data);

        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        futures.put(Language.class, future);

        List<SyncDataBaseDto> response =
                clientSettingsHelper.retrieveData(futures, machineDto, true);

        verify(serviceHelper)
                .getSyncDataBaseDtoV2(eq("Language"),
                        eq("structured"),
                        eq(data),
                        eq(machineDto),
                        anyList());
    }

    @Test
    void testRetrieveData_dynamicData() throws Exception {

        DynamicFieldDto dto1 = new DynamicFieldDto();
        dto1.setName("field1");

        DynamicFieldDto dto2 = new DynamicFieldDto();
        dto2.setName("field1");

        CompletableFuture<List<DynamicFieldDto>> future =
                CompletableFuture.completedFuture(List.of(dto1, dto2));

        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        futures.put(DynamicFieldDto.class, future);

        clientSettingsHelper.retrieveData(futures, machineDto, false);

        verify(serviceHelper)
                .getSyncDataBaseDto(eq("field1"),
                        eq("dynamic"),
                        anyList(),
                        eq(machineDto),
                        anyList());
    }

    @Test
    void testRetrieveData_structuredUrl_encryptionFlow() throws Exception {

        Map<String, Object> urlMap = Map.of("url", "http://test");

        CompletableFuture<Map<String, Object>> future =
                CompletableFuture.completedFuture(urlMap);

        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        futures.put(Template.class, future);

        when(mapper.getObjectAsJsonString(any()))
                .thenReturn("{\"url\":\"http://test\"}");

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("encryptedValue");

        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class)))
                .thenReturn(cryptoResponse);

        List<SyncDataBaseDto> result =
                clientSettingsHelper.retrieveData(futures, machineDto, true);

        assertEquals(1, result.size());
        assertEquals("encryptedValue", result.get(0).getData());
    }

    @Test
    void testGetConfiguredScriptUrlDetail() throws Exception {

        when(mapper.getObjectAsJsonString(any()))
                .thenReturn("{json}");

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("encryptedScript");

        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class)))
                .thenReturn(cryptoResponse);

        List<SyncDataBaseDto> result =
                clientSettingsHelper.getConfiguredScriptUrlDetail(machineDto);

        assertEquals(1, result.size());
        assertEquals("script", result.get(0).getEntityType());
        assertEquals("encryptedScript", result.get(0).getData());
    }

    @Test
    void testRetrieveData_interruptedException() throws Exception {

        CompletableFuture<Object> future = mock(CompletableFuture.class);
        when(future.join()).thenThrow(new RuntimeException("interrupted"));

        Map<Class<?>, CompletableFuture<?>> futures = new HashMap<>();
        futures.put(Language.class, future);

        assertThrows(RuntimeException.class,
                () -> clientSettingsHelper.retrieveData(futures, machineDto, true));
    }

    @Test
    void testFullSyncEntity_lastUpdatedNull() {

        when(serviceHelper.getAppAuthenticationMethodDetails(isNull(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        clientSettingsHelper.getInitiateDataFetch(
                "machine1",
                "center1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                false,
                "AppAuthenticationMethod"
        );

        verify(serviceHelper)
                .getAppAuthenticationMethodDetails(isNull(), any());
    }

    @Test
    void testHasURLDetails_propertyNotPresent() {

        Map<Class<?>, CompletableFuture<?>> result =
                clientSettingsHelper.getInitiateDataFetch(
                        "machine1",
                        "center1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true,   // isV2API
                        false,
                        null
                );

        verify(serviceHelper).getTemplates(any(), any(), any());
    }

    @Test
    void testHasURLDetails_onlyFullSync_deltaSyncTrue() {

        clientSettingsHelper.getInitiateDataFetch(
                "machine1",
                "center1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                true,   // deltaSync = true
                null
        );

        verify(serviceHelper).getTemplates(any(), any(), any());
    }

    @Test
    void testHasURLDetails_onlyFullSync_deltaSyncFalse() {

        Map<Class<?>, CompletableFuture<?>> result =
                clientSettingsHelper.getInitiateDataFetch(
                        "machine1",
                        "center1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true,
                        false,   // deltaSync = false
                        null
                );

        assertNotNull(result.get(Template.class));
    }

    @Test
    void testHasURLDetails_onlyFullSyncFalse() {

        Map<Class<?>, CompletableFuture<?>> result =
                clientSettingsHelper.getInitiateDataFetch(
                        "machine1",
                        "center1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true,
                        true,   // deltaSync true
                        null
                );

        assertNotNull(result.get(Template.class));
    }
}
