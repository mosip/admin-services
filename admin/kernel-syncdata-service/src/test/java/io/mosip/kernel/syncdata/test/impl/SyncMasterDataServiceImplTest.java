package io.mosip.kernel.syncdata.test.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.exception.FileNotFoundException;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.keymanagerservice.entity.CACertificateStore;
import io.mosip.kernel.keymanagerservice.repository.CACertificateStoreRepository;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.service.impl.SyncMasterDataServiceImpl;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SyncMasterDataServiceImplTest {

    @InjectMocks
    private SyncMasterDataServiceImpl service;

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private MachineRepository machineRepo;

    @Mock
    private MapperUtils mapper;

    @Mock
    private IdentitySchemaHelper identitySchemaHelper;

    @Mock
    private KeymanagerHelper keymanagerHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CACertificateStoreRepository caCertificateStoreRepository;

    @Mock
    private ModuleDetailRepository moduleDetailRepository;

    @Mock
    private ClientSettingsHelper clientSettingsHelper;

    @Mock
    private Environment environment;

    @Mock
    private ClientCryptoManagerService clientCryptoManagerService;

    @Mock
    private CryptomanagerUtils cryptomanagerUtils;

    private Path tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = Files.createTempDirectory("syncdata-test");
        ReflectionTestUtils.setField(service, "machineUrl", "http://localhost/machine/%s");
        ReflectionTestUtils.setField(service, "clientSettingsDir", tempDir.toString());
    }

    @After
    public void cleanup() throws Exception {
        if (tempDir != null) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    // ---------------------------------------------------------------------
    // syncClientSettings
    // ---------------------------------------------------------------------
    @Test
    public void testSyncClientSettingsSuccess() {
        String regCenterId = "RC1";
        String keyIndex = "KEY1";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setMachineId("M1");
        machineDto.setRegCenterId("RC1");
        when(serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex)).thenReturn(machineDto);
        Map<Class<?>, CompletableFuture<?>> futureMap = new HashMap<>();
        when(clientSettingsHelper.getInitiateDataFetch(
                anyString(),
                anyString(),
                any(),
                any(),
                eq(false),
                anyBoolean(),
                isNull()
        )).thenReturn(futureMap);
        when(clientSettingsHelper.retrieveData(
                eq(futureMap),
                eq(machineDto),
                eq(false)
        )).thenReturn(Collections.emptyList());
        SyncDataResponseDto resp = service.syncClientSettings(regCenterId, keyIndex, from, to);
        assertNotNull(resp);
        assertNotNull(resp.getDataToSync());
        assertTrue(resp.getDataToSync().isEmpty());
    }

    // ---------------------------------------------------------------------
    // syncClientSettingsV2
    // ---------------------------------------------------------------------

    @Test
    public void testSyncClientSettingsV2Success() {
        String regCenterId = "RC1";
        String keyIndex = "KEY1";
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setMachineId("M1");
        machineDto.setRegCenterId("RC1");
        when(serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex)).thenReturn(machineDto);
        Map<Class<?>, CompletableFuture<?>> futureMap = new HashMap<>();
        when(clientSettingsHelper.getInitiateDataFetch(
                anyString(),
                anyString(),
                any(),
                any(),
                eq(true),
                anyBoolean(),
                any()
        )).thenReturn(futureMap);
        SyncDataBaseDto data1 = mock(SyncDataBaseDto.class);
        when(clientSettingsHelper.retrieveData(
                eq(futureMap),
                eq(machineDto),
                eq(true)
        )).thenReturn(Collections.singletonList(data1));
        SyncDataBaseDto script1 = mock(SyncDataBaseDto.class);
        when(clientSettingsHelper.getConfiguredScriptUrlDetail(
                eq(machineDto)
        )).thenReturn(Collections.singletonList(script1));
        SyncDataResponseDto resp = service.syncClientSettingsV2(
                regCenterId,
                keyIndex,
                null,
                LocalDateTime.now(),
                "1.0.0",
                null
        );
        assertNotNull(resp);
        assertNotNull(resp.getDataToSync());
        assertEquals(2, resp.getDataToSync().size());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testSyncClientSettingsV2FutureThrowsSyncDataServiceException() {
        String regCenterId = "RC1";
        String keyIndex = "KEY1";
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setMachineId("M1");
        machineDto.setRegCenterId("RC1");
        when(serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex)).thenReturn(machineDto);
        CompletableFuture<Object> failed = new CompletableFuture<>();
        failed.completeExceptionally(new SyncDataServiceException("ERR", "msg"));
        Map<Class<?>, CompletableFuture<?>> futureMap = new HashMap<>();
        futureMap.put(String.class, failed);
        when(clientSettingsHelper.getInitiateDataFetch(
                anyString(),
                anyString(),
                any(),
                any(),
                eq(true),
                anyBoolean(),
                any()
        )).thenReturn(futureMap);
        service.syncClientSettingsV2(
                regCenterId,
                keyIndex,
                null,
                LocalDateTime.now(),
                "1.0.0",
                null
        );
    }

    @Test(expected = RuntimeException.class)
    public void testSyncClientSettingsV2FutureThrowsOtherRuntimeException() {
        String regCenterId = "RC1";
        String keyIndex = "KEY1";
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setMachineId("M1");
        machineDto.setRegCenterId("RC1");
        when(serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex)).thenReturn(machineDto);
        CompletableFuture<Object> failed = new CompletableFuture<>();
        failed.completeExceptionally(new RuntimeException("boom"));
        Map<Class<?>, CompletableFuture<?>> futureMap = new HashMap<>();
        futureMap.put(String.class, failed);
        when(clientSettingsHelper.getInitiateDataFetch(
                anyString(),
                anyString(),
                any(),
                any(),
                eq(true),
                anyBoolean(),
                isNull()
        )).thenReturn(futureMap);
        service.syncClientSettingsV2(
                regCenterId,
                keyIndex,
                null,
                LocalDateTime.now(),
                "1.0.0",
                null
        );
    }

    // ---------------------------------------------------------------------
    // getLatestPublishedIdSchema
    // ---------------------------------------------------------------------

    @Test
    public void testGetLatestPublishedIdSchemaDelegatesToHelper() {
        JsonNode node = mock(JsonNode.class);
        when(identitySchemaHelper.getLatestIdentitySchema(
                any(), anyDouble(), anyString(), anyString()
        )).thenReturn(node);
        JsonNode result = service.getLatestPublishedIdSchema(
                LocalDateTime.now(),
                1.0,
                "dom",
                "schema"
        );
        assertSame(node, result);
    }

    // ---------------------------------------------------------------------
    // getCertificate
    // ---------------------------------------------------------------------

    @Test
    public void testGetCertificateDelegatesToKeymanager() {
        KeyPairGenerateResponseDto dto = new KeyPairGenerateResponseDto();
        when(keymanagerHelper.getCertificate(eq("APP"), eq(Optional.of("REF")))).thenReturn(dto);
        KeyPairGenerateResponseDto result = service.getCertificate("APP", Optional.of("REF"));
        assertSame(dto, result);
    }

    // ---------------------------------------------------------------------
    // getMachineById (private) — success, errors, exception
    // ---------------------------------------------------------------------

    @Test
    public void testGetMachineByIdSuccess() throws Exception {
        String machineId = "M1";
        String url = "http://localhost/machine/M1";
        ResponseEntity<String> en = new ResponseEntity<>("BODY", HttpStatus.OK);
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(en);
        MachineResponseDto machineResp = new MachineResponseDto();
        machineResp.setMachines(Collections.singletonList(new MachineDto()));
        ResponseWrapper<MachineResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(machineResp);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        MachineResponseDto result = (MachineResponseDto) ReflectionTestUtils.invokeMethod(
                service,
                "getMachineById",
                machineId
        );
        assertSame(machineResp, result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetMachineByIdWithErrorsThrowsSyncDataServiceException() throws Exception {
        String machineId = "M1";
        ResponseEntity<String> en = new ResponseEntity<>("BODY", HttpStatus.OK);
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(en);
        ResponseWrapper<MachineResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setErrors(Collections.singletonList(new io.mosip.kernel.core.exception.ServiceError()));
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        ReflectionTestUtils.invokeMethod(service, "getMachineById", machineId);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetMachineByIdExceptionThrowsSyncDataServiceException() throws Exception {
        String machineId = "M1";
        when(restTemplate.getForEntity(any(), eq(String.class))).thenThrow(new RuntimeException("boom"));
        ReflectionTestUtils.invokeMethod(service, "getMachineById", machineId);
    }

    // ---------------------------------------------------------------------
    // getClientPublicKey
    // ---------------------------------------------------------------------

    @Test
    public void testGetClientPublicKeySuccess() throws Exception {
        String machineId = "M1";
        ResponseEntity<String> en = new ResponseEntity<>("BODY", HttpStatus.OK);
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(en);
        MachineDto m = new MachineDto();
        m.setSignPublicKey("SIGN_KEY");
        m.setPublicKey("PUB_KEY");
        MachineResponseDto machineResp = new MachineResponseDto();
        machineResp.setMachines(Collections.singletonList(m));
        ResponseWrapper<MachineResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(machineResp);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        ClientPublicKeyResponseDto resp = service.getClientPublicKey(machineId);
        assertEquals("SIGN_KEY", resp.getSigningPublicKey());
        assertEquals("PUB_KEY", resp.getEncryptionPublicKey());
    }

    @Test(expected = RequestException.class)
    public void testGetClientPublicKeyNoMachinesThrowsRequestException() throws Exception {
        String machineId = "M1";
        ResponseEntity<String> en = new ResponseEntity<>("BODY", HttpStatus.OK);
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(en);
        MachineResponseDto machineResp = new MachineResponseDto();
        machineResp.setMachines(Collections.emptyList());
        ResponseWrapper<MachineResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(machineResp);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        service.getClientPublicKey(machineId);
    }

    // ---------------------------------------------------------------------
    // getPartnerCACertificates
    // ---------------------------------------------------------------------

    @Test
    public void testGetPartnerCACertificatesNullListReturnsEmpty() {
        when(caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(any(), any())).thenReturn(null);
        CACertificates certs = service.getPartnerCACertificates(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertNotNull(certs);
        assertNotNull(certs.getCertificateDTOList());
        assertTrue(certs.getCertificateDTOList().isEmpty());
    }

    @Test
    public void testGetPartnerCACertificatesWithDataMapsFields() {
        CACertificateStore store = mock(CACertificateStore.class);
        when(store.getCertId()).thenReturn("ID1");
        when(store.getCertIssuer()).thenReturn("ISSUER");
        when(caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(
                any(),
                any()
        )).thenReturn(Collections.singletonList(store));
        CACertificates certs = service.getPartnerCACertificates(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertNotNull(certs);
        assertEquals(1, certs.getCertificateDTOList().size());
        CACertificateDTO dto = certs.getCertificateDTOList().get(0);
        assertEquals("ID1", dto.getCertId());
        assertEquals("ISSUER", dto.getCertIssuer());
    }

    // ---------------------------------------------------------------------
    // getClientSettingsJsonFile + getEntityResource + getEncryptedData
    // ---------------------------------------------------------------------

    @Test
    public void testGetClientSettingsJsonFileNotEncryptedSuccess() throws Exception {
        String entity = "settings.json";
        String keyIndex = "KEY1";
        String content = "{\"a\":1}";
        Machine machine = new Machine();
        machine.setPublicKey("PUB");
        when(machineRepo.findByMachineKeyIndex(keyIndex)).thenReturn(Collections.singletonList(machine));
        when(environment.getProperty(
                eq("mosip.sync.entity.encrypted.SETTINGS.JSON"),
                eq(Boolean.class),
                eq(false)
        )).thenReturn(false);
        Path file = tempDir.resolve(entity);
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        when(keymanagerHelper.getFileSignature(anyString())).thenReturn("SIG");
        ResponseEntity resp = service.getClientSettingsJsonFile(entity, keyIndex);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, resp.getHeaders().getContentType());
        assertEquals("SIG", resp.getHeaders().getFirst("file-signature"));
        assertTrue(resp.getBody() instanceof String);
        assertEquals(content, resp.getBody());
    }

    @Test
    public void testGetClientSettingsJsonFileEncryptedSuccess() throws Exception {
        String entity = "settings.json";
        String keyIndex = "KEY1";
        String content = "{\"a\":1}";
        Machine machine = new Machine();
        machine.setPublicKey("PUB_KEY");
        when(machineRepo.findByMachineKeyIndex(keyIndex)).thenReturn(Collections.singletonList(machine));
        when(environment.getProperty(
                eq("mosip.sync.entity.encrypted.SETTINGS.JSON"),
                eq(Boolean.class),
                eq(false)
        )).thenReturn(true);
        Path file = tempDir.resolve(entity);
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        when(keymanagerHelper.getFileSignature(anyString())).thenReturn("SIG");
        TpmCryptoResponseDto enc = new TpmCryptoResponseDto();
        enc.setValue("ENC_VALUE");
        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class))).thenReturn(enc);
        ResponseEntity resp = service.getClientSettingsJsonFile(entity, keyIndex);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("SIG", resp.getHeaders().getFirst("file-signature"));
        assertTrue(resp.getBody() instanceof String);
        assertEquals("ENC_VALUE", resp.getBody());
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetClientSettingsJsonFileFileNotFoundThrowsFileNotFound() throws Exception {
        String entity = "missing.json";
        String keyIndex = "KEY1";
        Machine machine = new Machine();
        machine.setPublicKey("PUB");
        when(machineRepo.findByMachineKeyIndex(keyIndex)).thenReturn(Collections.singletonList(machine));
        when(environment.getProperty(
                anyString(),
                eq(Boolean.class),
                eq(false)
        )).thenReturn(false);
        service.getClientSettingsJsonFile(entity, keyIndex);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetClientSettingsJsonFile_EncryptionError_ThrowsSyncDataServiceException() throws Exception {
        String entity = "settings.json";
        String keyIndex = "KEY1";
        String content = "{\"a\":1}";
        Machine machine = new Machine();
        machine.setPublicKey("PUB_KEY");
        when(machineRepo.findByMachineKeyIndex(keyIndex)).thenReturn(Collections.singletonList(machine));
        when(environment.getProperty(
                eq("mosip.sync.entity.encrypted.SETTINGS.JSON"),
                eq(Boolean.class),
                eq(false)
        )).thenReturn(true);
        Path file = tempDir.resolve(entity);
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        when(keymanagerHelper.getFileSignature(anyString())).thenReturn("SIG");
        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class))).thenThrow(new RuntimeException("enc error"));
        service.getClientSettingsJsonFile(entity, keyIndex);
    }

    @Test(expected = RequestException.class)
    public void testGetClientSettingsJsonFileInvalidKeyIndexThrowsRequestException() throws Exception {
        String entity = "settings.json";
        String keyIndex = "   ";
        service.getClientSettingsJsonFile(entity, keyIndex);
    }

    // ---------------------------------------------------------------------
    // validateKeyMachineMapping (public)
    // ---------------------------------------------------------------------

    @Test
    public void testValidateKeyMachineMappingSuccess() {
        UploadPublicKeyRequestDto req = new UploadPublicKeyRequestDto();
        req.setMachineName("MACHINE");
        req.setPublicKey("BASE64KEY");
        Machine m = new Machine();
        m.setPublicKey("BASE64KEY");
        m.setKeyIndex("KEY-123");
        when(machineRepo.findByMachineName("MACHINE")).thenReturn(Collections.singletonList(m));
        byte[] decoded = new byte[]{1, 2, 3};
        when(cryptomanagerUtils.decodeBase64Data("BASE64KEY")).thenReturn(decoded);
        UploadPublicKeyResponseDto resp = service.validateKeyMachineMapping(req);
        assertEquals("KEY-123", resp.getKeyIndex());
    }

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingKeyMismatchThrowsRequestException() {
        UploadPublicKeyRequestDto req = new UploadPublicKeyRequestDto();
        req.setMachineName("MACHINE");
        req.setPublicKey("KEY1");
        Machine m = new Machine();
        m.setPublicKey("KEY2");
        m.setKeyIndex("K");
        when(machineRepo.findByMachineName("MACHINE")).thenReturn(Collections.singletonList(m));
        when(cryptomanagerUtils.decodeBase64Data("KEY1")).thenReturn(new byte[]{1});
        when(cryptomanagerUtils.decodeBase64Data("KEY2")).thenReturn(new byte[]{2});
        service.validateKeyMachineMapping(req);
    }

    // ---------------------------------------------------------------------
    // validateSyncClientSettingsInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsRegCenterIdNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                null,               // regCenterId
                "KEY1",             // keyIndex
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsRegCenterIdEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "   ",              // empty
                "KEY1",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsKeyIndexNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "RC1",
                null,              // keyIndex
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsKeyIndexEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "RC1",
                "   ",             // empty keyIndex
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsCurrentTimestampNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "RC1",
                "KEY1",
                LocalDateTime.now().minusDays(1),
                null               // currentTimestamp null
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateSyncClientSettingsInputsLastUpdatedAfterCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "RC1",
                "KEY1",
                now.plusSeconds(1),    // lastUpdated > currentTimestamp
                now
        );
    }

    @Test
    public void testValidateSyncClientSettingsInputsValidInputsNoException() {
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.invokeMethod(
                service,
                "validateSyncClientSettingsInputs",
                "RC1",
                "KEY1",
                now.minusDays(1),      // valid
                now
        );
    }

    // ---------------------------------------------------------------------
    // validateKeyMachineMappingInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingInputsNullDto() {
        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                (UploadPublicKeyRequestDto) null);
    }

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingInputsMachineNameNull() {
        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName(null);
        dto.setPublicKey("PUBKEY");

        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                dto);
    }

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingInputsMachineNameEmpty() {
        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("   ");   // empty
        dto.setPublicKey("PUBKEY");

        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                dto);
    }

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingInputsPublicKeyNull() {
        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("MACHINE1");
        dto.setPublicKey(null);

        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                dto);
    }

    @Test(expected = RequestException.class)
    public void testValidateKeyMachineMappingInputsPublicKeyEmpty() {
        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("MACHINE1");
        dto.setPublicKey("   ");  // empty

        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                dto);
    }

    @Test
    public void testValidateKeyMachineMappingInputsValidInputNoException() {
        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("MACHINE1");
        dto.setPublicKey("PUBKEY123");

        // Should NOT throw
        ReflectionTestUtils.invokeMethod(service,
                "validateKeyMachineMappingInputs",
                dto);
    }

    // ---------------------------------------------------------------------
    // validateIdSchemaInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateIdSchemaInputsDomainEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateIdSchemaInputs",
                "   ",    // domain empty
                "PERSON"  // valid type
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateIdSchemaInputsTypeEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateIdSchemaInputs",
                "identity",   // valid domain
                "   "         // empty type
        );
    }

    @Test
    public void testValidateIdSchemaInputsNullValuesNoException() {
        // Both NULL -> allowed
        ReflectionTestUtils.invokeMethod(
                service,
                "validateIdSchemaInputs",
                null,
                null
        );
    }

    @Test
    public void testValidateIdSchemaInputsValidInputsNoException() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateIdSchemaInputs",
                "identity",
                "PERSON"
        );
    }

    // ---------------------------------------------------------------------
    // validateCertificateInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateCertificateInputsApplicationIdNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateCertificateInputs",
                null,                       // null applicationId
                Optional.of("REF123")
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateCertificateInputsApplicationIdEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateCertificateInputs",
                "   ",                      // empty applicationId
                Optional.of("REF123")
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateCertificateInputsReferenceIdEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateCertificateInputs",
                "APP123",
                Optional.of("   ")          // empty referenceId
        );
    }

    @Test
    public void testValidateCertificateInputsReferenceIdNotPresentNoException() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateCertificateInputs",
                "APP123",
                Optional.empty()            // no referenceId → valid
        );
    }

    @Test
    public void testValidateCertificateInputsValidInputsNoException() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateCertificateInputs",
                "APP123",
                Optional.of("REF456")       // valid referenceId
        );
    }

    // ---------------------------------------------------------------------
    // validateClientPublicKeyInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateClientPublicKeyInputsMachineIdNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientPublicKeyInputs",
                (String) null          // machineId = null
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientPublicKeyInputsMachineIdEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientPublicKeyInputs",
                "   "                  // machineId empty
        );
    }

    @Test
    public void testValidateClientPublicKeyInputsValidInputNoException() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientPublicKeyInputs",
                "MACHINE123"           // valid
        );
    }

    // ---------------------------------------------------------------------
    // validateClientSettingsJsonFileInputs()
    // ---------------------------------------------------------------------

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsEntityIdentifierNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                null,              // entityIdentifier
                "KEY1"
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsEntityIdentifierEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "   ",             // empty
                "KEY1"
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsKeyIndexNull() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "ENTITY123",
                null               // keyIndex null
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsKeyIndexEmpty() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "ENTITY123",
                "   "              // empty
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsPathTraversalDoubleDot() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "folder..name",     // contains ".."
                "KEY1"
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsPathTraversalSlash() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "folder/name",      // contains "/"
                "KEY1"
        );
    }

    @Test(expected = RequestException.class)
    public void testValidateClientSettingsJsonFileInputsPathTraversalBackslash() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "folder\\name",     // contains "\"
                "KEY1"
        );
    }

    @Test
    public void testValidateClientSettingsJsonFileInputsValidNoException() {
        ReflectionTestUtils.invokeMethod(
                service,
                "validateClientSettingsJsonFileInputs",
                "ENTITY123",
                "KEY1"
        );
    }

}