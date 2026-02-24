package io.mosip.kernel.syncdata.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.keymanagerservice.entity.CACertificateStore;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.keymanagerservice.repository.CACertificateStoreRepository;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.service.impl.SyncMasterDataServiceImpl;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncMasterDataServiceImplTest {

    @InjectMocks
    private SyncMasterDataServiceImpl service;

    @Mock private SyncMasterDataServiceHelper serviceHelper;
    @Mock private MachineRepository machineRepo;
    @Mock private MapperUtils mapper;
    @Mock private IdentitySchemaHelper identitySchemaHelper;
    @Mock private KeymanagerHelper keymanagerHelper;
    @Mock private RestTemplate restTemplate;
    @Mock private CACertificateStoreRepository caCertificateStoreRepository;
    @Mock private ModuleDetailRepository moduleDetailRepository;
    @Mock private ClientSettingsHelper clientSettingsHelper;
    @Mock private Environment environment;
    @Mock private CryptomanagerUtils cryptomanagerUtils;
    @Mock private ClientCryptoManagerService clientCryptoManagerService;
    @Mock private ObjectMapper objectMapper;

    private Machine machine;

    @BeforeEach
    void setup() {
        machine = new Machine();
        machine.setName("machine1");
        machine.setPublicKey("cHVibGlj"); // base64
        machine.setKeyIndex("KEY1");
    }

    // ===============================
    // validateKeyMachineMapping
    // ===============================

    @Test
    void validateKeyMachineMapping_machineNotFound() {
        when(machineRepo.findByMachineName(anyString())).thenReturn(Collections.emptyList());

        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("machine1");

        assertThrows(RequestException.class,
                () -> service.validateKeyMachineMapping(dto));
    }

    @Test
    void validateKeyMachineMapping_publicKeyEmpty() {
        machine.setPublicKey("");
        when(machineRepo.findByMachineName(anyString())).thenReturn(List.of(machine));

        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("machine1");

        assertThrows(RequestException.class,
                () -> service.validateKeyMachineMapping(dto));
    }

    @Test
    void validateKeyMachineMapping_success() {
        when(machineRepo.findByMachineName(anyString())).thenReturn(List.of(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString()))
                .thenReturn("test".getBytes());

        UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
        dto.setMachineName("machine1");
        dto.setPublicKey("cHVibGlj");

        UploadPublicKeyResponseDto response =
                service.validateKeyMachineMapping(dto);

        assertEquals("KEY1", response.getKeyIndex());
    }

    // ===============================
    // getClientPublicKey
    // ===============================

    @Test
    void getClientPublicKey_machineNotFound() {
        assertThrows(SyncDataServiceException.class,
                () -> service.getClientPublicKey("123"));
    }

    // ===============================
    // getPartnerCACertificates
    // ===============================

    @Test
    void getPartnerCACertificates_nullList() {
        when(caCertificateStoreRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(null);

        CACertificates result =
                service.getPartnerCACertificates(null, LocalDateTime.now());

        assertNotNull(result);
        assertTrue(result.getCertificateDTOList().isEmpty());
    }

    @Test
    void getPartnerCACertificates_success() {
        CACertificateStore cert = new CACertificateStore();
        cert.setCertId("1");
        cert.setCertIssuer("issuer");

        when(caCertificateStoreRepository
                .findAllLatestCreatedUpdateDeleted(any(), any()))
                .thenReturn(List.of(cert));

        CACertificates result =
                service.getPartnerCACertificates(LocalDateTime.now().minusDays(1),
                        LocalDateTime.now());

        assertEquals(1, result.getCertificateDTOList().size());
    }

    // ===============================
    // syncClientSettings
    // ===============================

    @Test
    void syncClientSettings_success() {
        RegistrationCenterMachineDto dto = new RegistrationCenterMachineDto();
        dto.setMachineId("MID");
        dto.setRegCenterId("RID");

        when(serviceHelper.getRegistrationCenterMachine(any(), any()))
                .thenReturn(dto);

        Map<Class, CompletableFuture> futureMap = new HashMap<>();
        futureMap.put(String.class, CompletableFuture.completedFuture("data"));

        when(clientSettingsHelper.getInitiateDataFetch(any(), any(), any(), any(),
                anyBoolean(), anyBoolean(), any()))
                .thenReturn(futureMap);

        when(clientSettingsHelper.retrieveData(any(), any(), anyBoolean()))
                .thenReturn(new ArrayList<>());

        SyncDataResponseDto response =
                service.syncClientSettings("RID", "KEY",
                        LocalDateTime.now(), LocalDateTime.now());

        assertNotNull(response);
    }

    // ===============================
    // syncClientSettingsV2
    // ===============================

    @Test
    void syncClientSettingsV2_success() {
        RegistrationCenterMachineDto dto = new RegistrationCenterMachineDto();
        dto.setMachineId("MID");
        dto.setRegCenterId("RID");

        when(serviceHelper.getRegistrationCenterMachine(any(), any()))
                .thenReturn(dto);

        Map<Class, CompletableFuture> futureMap = new HashMap<>();
        futureMap.put(String.class, CompletableFuture.completedFuture("data"));

        when(clientSettingsHelper.getInitiateDataFetch(any(), any(), any(), any(),
                anyBoolean(), anyBoolean(), any()))
                .thenReturn(futureMap);

        when(clientSettingsHelper.retrieveData(any(), any(), anyBoolean()))
                .thenReturn(new ArrayList<>());

        when(clientSettingsHelper.getConfiguredScriptUrlDetail(any()))
                .thenReturn(new ArrayList<>());

        SyncDataResponseDto response =
                service.syncClientSettingsV2("RID", "KEY",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "1.0", "ALL");

        assertNotNull(response);
    }

    // ===============================
    // getClientSettingsJsonFile
    // ===============================

    @Test
    void getClientSettingsJsonFile_machineNotFound() {
        when(machineRepo.findByMachineKeyIndex(anyString()))
                .thenReturn(Collections.emptyList());

        assertThrows(RequestException.class,
                () -> service.getClientSettingsJsonFile("file.json", "KEY"));
    }

    @Test
    void getClientSettingsJsonFile_notEncrypted() throws Exception {

        Machine machine = new Machine();
        machine.setPublicKey("PUBKEY");

        when(machineRepo.findByMachineKeyIndex(anyString()))
                .thenReturn(List.of(machine));

        when(environment.getProperty(anyString(), eq(Boolean.class), eq(false)))
                .thenReturn(false);

        // create temp file
        Path tempFile = java.nio.file.Files.createTempFile("test", ".json");
        java.nio.file.Files.writeString(tempFile, "sample-data");

        // inject directory path
        org.springframework.test.util.ReflectionTestUtils
                .setField(service, "clientSettingsDir",
                        tempFile.getParent().toString());

        when(keymanagerHelper.getFileSignature(anyString()))
                .thenReturn("SIGNATURE");

        ResponseEntity response =
                service.getClientSettingsJsonFile(tempFile.getFileName().toString(), "KEY");

        assertEquals("sample-data", response.getBody());
    }

    @Test
    void getClientSettingsJsonFile_encrypted() throws Exception {

        Machine machine = new Machine();
        machine.setPublicKey("PUBKEY");

        when(machineRepo.findByMachineKeyIndex(anyString()))
                .thenReturn(List.of(machine));

        when(environment.getProperty(anyString(), eq(Boolean.class), eq(false)))
                .thenReturn(true);

        Path tempFile = java.nio.file.Files.createTempFile("testEnc", ".json");
        java.nio.file.Files.writeString(tempFile, "data");

        org.springframework.test.util.ReflectionTestUtils
                .setField(service, "clientSettingsDir",
                        tempFile.getParent().toString());

        when(keymanagerHelper.getFileSignature(anyString()))
                .thenReturn("SIGN");

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("ENCRYPTED_DATA");

        when(clientCryptoManagerService.csEncrypt(any()))
                .thenReturn(cryptoResponse);

        ResponseEntity response =
                service.getClientSettingsJsonFile(tempFile.getFileName().toString(), "KEY");

        assertEquals("ENCRYPTED_DATA", response.getBody());
    }

    @Test
    void getClientSettingsJsonFile_fileNotFound() {

        when(machineRepo.findByMachineKeyIndex(anyString()))
                .thenReturn(List.of(new Machine()));

        org.springframework.test.util.ReflectionTestUtils
                .setField(service, "clientSettingsDir", "/invalid/path");

        assertThrows(io.mosip.kernel.core.exception.FileNotFoundException.class,
                () -> service.getClientSettingsJsonFile("missing.json", "KEY"));
    }

    @Test
    void getEncryptedData_exception() {

        when(clientCryptoManagerService.csEncrypt(any()))
                .thenThrow(new RuntimeException("Crypto error"));

        assertThrows(SyncDataServiceException.class,
                () -> org.springframework.test.util.ReflectionTestUtils
                        .invokeMethod(service,
                                "getEncryptedData",
                                "data".getBytes(),
                                "PUBKEY"));
    }

    @Test
    void getMachineById_success() throws Exception {

        // Set machineUrl value (VERY IMPORTANT)
        org.springframework.test.util.ReflectionTestUtils
                .setField(service, "machineUrl", "http://localhost/%s");

        MachineResponseDto machineResponse = new MachineResponseDto();

        ResponseWrapper<MachineResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(machineResponse);
        wrapper.setErrors(null);

        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{dummy}"));

        when(objectMapper.readValue(
                anyString(),
                any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(wrapper);

        MachineResponseDto result =
                org.springframework.test.util.ReflectionTestUtils
                        .invokeMethod(service, "getMachineById", "MID");

        assertNotNull(result);
    }

}
