package io.mosip.kernel.syncdata.test.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.authmanager.model.AuthNResponse;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.dto.response.TokenResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.service.impl.SyncAuthTokenServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class SyncAuthTokenServiceImplTest {

    @InjectMocks
    private SyncAuthTokenServiceImpl service;

    @Mock
    private ClientCryptoFacade clientCryptoFacade;

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private SyncUserDetailsService syncUserDetailsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CryptomanagerUtils cryptomanagerUtils;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ReflectionTestUtils.setField(service, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(service, "newAuthTokenInternalUrl", "http://auth/new");
        ReflectionTestUtils.setField(service, "otpAuthTokenInternalUrl", "http://auth/otp");
        ReflectionTestUtils.setField(service, "refreshAuthTokenInternalUrl", "http://auth/refresh");
        ReflectionTestUtils.setField(service, "authTokenInternalAppId", "REG_CLIENT");
        ReflectionTestUtils.setField(service, "clientId", "client-id");
        ReflectionTestUtils.setField(service, "secretKey", "secret");
        ReflectionTestUtils.setField(service, "sendOTPUrl", "http://auth/send-otp");
        ReflectionTestUtils.setField(service, "maxMinutes", -5);
        ReflectionTestUtils.setField(service, "minMinutes", 5);
        service.init();
    }

    // ---------- Helpers ----------

    private String buildJwt(String kid, Map<String, Object> payloadMap) throws Exception {
        String headerJson;
        if (kid == null) {
            headerJson = "{\"alg\":\"RS256\"}";
        } else {
            headerJson = "{\"alg\":\"RS256\",\"kid\":\"" + kid + "\"}";
        }
        String payloadJson = objectMapper.writeValueAsString(payloadMap);
        String header = CryptoUtil.encodeToURLSafeBase64(headerJson.getBytes());
        String payload = CryptoUtil.encodeToURLSafeBase64(payloadJson.getBytes());
        String signature = CryptoUtil.encodeToURLSafeBase64("dummy-signature".getBytes());
        return header + "." + payload + "." + signature;
    }

    private Machine buildMachine(String name) {
        Machine m = new Machine();
        m.setId("M1");
        m.setIsActive(true);
        m.setName(name);
        m.setSignPublicKey("SIGN_PUB_BASE64");
        m.setPublicKey("PUB_BASE64");
        return m;
    }

    private ResponseEntity<String> buildTokenResponseEntity() throws Exception {
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setToken("ACCESS_TOKEN");
        ResponseWrapper<TokenResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(tokenResponseDto);
        String body = objectMapper.writeValueAsString(wrapper);
        return ResponseEntity.ok(body);
    }

    // ---------- Tests ----------

    @Test
    public void initShouldConfigureObjectMapper() throws Exception {
        String json = "{\"timestamp\":\"2024-01-01T10:00:00\"}";
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        assertEquals("2024-01-01T10:00:00", map.get("timestamp"));
    }

    @Test
    public void getAuthTokenWithAuthTypeNewShouldReturnEncryptedToken() throws Exception {
        Machine machine = buildMachine("MACHINE-1");
        when(machineRepository.findBySignKeyIndex("key1")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        when(clientCryptoFacade.encrypt(any(), any(), any())).thenReturn("cipher-bytes".getBytes());
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class))).thenReturn(
                buildTokenResponseEntity()
        );
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", "admin");
        payload.put("password", "pwd");
        payload.put("authType", "NEW");
        payload.put("machineName", "MACHINE-1");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key1", payload);
        String result = service.getAuthToken(jwt);
        assertNotNull(result);
        byte[] decoded = CryptoUtil.decodeURLSafeBase64(result);
        assertEquals("cipher-bytes", new String(decoded));
        verify(clientCryptoFacade, times(1)).encrypt(any(), any(), any());
    }

    @Test
    public void getAuthTokenWithAuthTypeOtpShouldCallOtpUrl() throws Exception {
        Machine machine = buildMachine("MACHINE-OTP");
        when(machineRepository.findBySignKeyIndex("key2")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        when(clientCryptoFacade.encrypt(any(), any(), any())).thenReturn("cipher-otp".getBytes());
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class))).thenReturn(
                buildTokenResponseEntity()
        );
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", "user-otp");
        payload.put("otp", "123456");
        payload.put("authType", "OTP");
        payload.put("machineName", "MACHINE-OTP");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key2", payload);
        String result = service.getAuthToken(jwt);
        assertNotNull(result);
        verify(restTemplate, atLeastOnce()).postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    public void getAuthTokenWithAuthTypeRefreshShouldCallRefreshUrl() throws Exception {
        Machine machine = buildMachine("MACHINE-REFRESH");
        when(machineRepository.findBySignKeyIndex("key3")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        when(clientCryptoFacade.encrypt(any(), any(), any())).thenReturn("cipher-refresh".getBytes());
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class))).thenReturn(
                buildTokenResponseEntity()
        );
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", "user-refresh");
        payload.put("refreshToken", "REFRESH-TOKEN");
        payload.put("authType", "REFRESH");
        payload.put("machineName", "MACHINE-REFRESH");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key3", payload);
        String result = service.getAuthToken(jwt);
        assertNotNull(result);
        verify(restTemplate, atLeastOnce()).postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class));
    }

    @Test(expected = RequestException.class)
    public void getAuthTokenInvalidPartsShouldThrowRequestException() {
        String invalid = "no-dot-here";
        service.getAuthToken(invalid);
    }

    @Test(expected = RequestException.class)
    public void getAuthTokenHeaderWithoutKidShouldThrowRequestException() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("authType", "NEW");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt(null, payload);
        service.getAuthToken(jwt);
    }

    @Test(expected = RequestException.class)
    public void getAuthTokenWithOldTimestampShouldThrowRequestException() throws Exception {
        Machine machine = buildMachine("MACHINE-TIME");
        when(machineRepository.findBySignKeyIndex("key-time")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        Map<String, Object> payload = new HashMap<>();
        payload.put("authType", "NEW");
        payload.put("machineName", "MACHINE-TIME");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(30).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key-time", payload);
        service.getAuthToken(jwt);
    }

    @Test(expected = RequestException.class)
    public void getAuthTokenUnknownAuthTypeShouldThrowErrorGettingToken() throws Exception {
        Machine machine = buildMachine("MACHINE-UNKNOWN");
        when(machineRepository.findBySignKeyIndex("key-unknown")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        Map<String, Object> payload = new HashMap<>();
        payload.put("authType", "somethingElse");
        payload.put("machineName", "MACHINE-UNKNOWN");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key-unknown", payload);
        service.getAuthToken(jwt);
    }

    @Test
    public void sendOtpValidRequestShouldReturnResponse() throws Exception {
        Machine machine = buildMachine("MACH-OTP");
        when(machineRepository.findBySignKeyIndex("key-otp")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        when(syncUserDetailsService.getUserDetailsFromAuthServer(anyList())).thenReturn(null);
        ResponseWrapper<AuthNResponse> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new AuthNResponse());
        String body = objectMapper.writeValueAsString(wrapper);
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class))).thenReturn(
                ResponseEntity.ok(body)
        );
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", "user1");
        payload.put("otpChannel", Arrays.asList("email"));
        payload.put("appId", "REG_CLIENT");
        payload.put("useridtype", "USER");
        payload.put("context", "CTX");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedTimestamp = LocalDateTime.now(ZoneOffset.UTC).format(timestampFormatter);
        payload.put("timestamp", formattedTimestamp);
        String jwt = buildJwt("key-otp", payload);
        ResponseWrapper<AuthNResponse> resp = service.sendOTP(jwt);
        assertNotNull(resp);
        assertNotNull(resp.getResponse());
        verify(restTemplate, times(1)).postForEntity(
                any(URI.class),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test(expected = RequestException.class)
    public void sendOtpInvalidPartsShouldThrowRequestException() {
        service.sendOTP("invalid");
    }

    @Test(expected = RequestException.class)
    public void getAuthTokenWhenAuthServerReturnsErrorShouldThrowRequestException() throws Exception {
        Machine machine = buildMachine("MACH-ERROR");
        when(machineRepository.findBySignKeyIndex("key-err")).thenReturn(Collections.singletonList(machine));
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        ResponseWrapper<TokenResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(tokenResponseDto);
        List<ServiceError> errors = new ArrayList<>();
        ServiceError error = new ServiceError();
        error.setErrorCode("ERR_CODE");
        error.setMessage("ERR_MESSAGE");
        errors.add(error);
        wrapper.setErrors(errors);
        String body = objectMapper.writeValueAsString(wrapper);
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", "admin");
        payload.put("password", "pwd");
        payload.put("authType", "NEW");
        payload.put("machineName", "MACH-ERROR");
        payload.put("timestamp", LocalDateTime.now(ZoneOffset.UTC));
        String jwt = buildJwt("key-err", payload);
        service.getAuthToken(jwt);
    }

}