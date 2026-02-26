package io.mosip.kernel.syncdata.test.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.authmanager.model.AuthNResponse;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.constant.SyncAuthErrorCode;
import io.mosip.kernel.syncdata.dto.MachineAuthDto;
import io.mosip.kernel.syncdata.dto.MachineOtpDto;
import io.mosip.kernel.syncdata.dto.response.TokenResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;

import io.mosip.kernel.syncdata.service.impl.SyncAuthTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncAuthTokenServiceImplTest {

    @InjectMocks
    @Spy
    private SyncAuthTokenServiceImpl service;

    @Mock private ClientCryptoFacade clientCryptoFacade;
    @Mock private MachineRepository machineRepository;
    @Mock private SyncUserDetailsService syncUserDetailsService;
    @Mock private RestTemplate restTemplate;
    @Mock private CryptomanagerUtils cryptomanagerUtils;
    @Mock private ObjectMapper objectMapper;

    private Machine machine;

    @BeforeEach
    void setup() {
        machine = new Machine();
        machine.setId("1");
        machine.setName("machine1");
        machine.setPublicKey("cHVibGlj");
        machine.setSignPublicKey("c2lnbg==");
        machine.setSignKeyIndex("KEY1");
        machine.setIsActive(true);

        ReflectionTestUtils.setField(service, "minMinutes", 5);
        ReflectionTestUtils.setField(service, "maxMinutes", -5);
        ReflectionTestUtils.setField(service, "clientId", "cid");
        ReflectionTestUtils.setField(service, "secretKey", "secret");
        ReflectionTestUtils.setField(service, "authTokenInternalAppId", "appId");
        ReflectionTestUtils.setField(service, "newAuthTokenInternalUrl", "http://new");
        ReflectionTestUtils.setField(service, "otpAuthTokenInternalUrl", "http://otp");
        ReflectionTestUtils.setField(service, "refreshAuthTokenInternalUrl", "http://refresh");
    }


    @Test
    void getAuthToken_invalidRequest_throwsRequestException() {
        // Invalid token with less than 3 parts
        String invalidToken = "invalid.token";

        RequestException ex = assertThrows(RequestException.class,
                () -> service.getAuthToken(invalidToken));

        // Verify exception code and message
        assertEquals(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(), ex.getErrorCode());

    }

    // ======================= SUCCESS =======================
    @Test
    void getAuthToken_invalidTimestamp_throwsRequestException() throws Exception {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Create JWT-like token
        String headerJson = "{\"kid\":\"KEY1\"}";
        String payloadJson = "{\"machineName\":\"machine1\",\"userId\":\"user1\",\"password\":\"pass123\",\"timestamp\":\"" + now + "\",\"authType\":\"NEW\"}";
        String token = Base64.getUrlEncoder().encodeToString(headerJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString(payloadJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString("sig".getBytes());

        // 1️⃣ Mock machineRepository to return the machine (used in validateRequestData)
        when(machineRepository.findBySignKeyIndex("KEY1")).thenReturn(List.of(machine));

        // 2️⃣ Force validateSignature = true
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);

        // 3️⃣ Mock payload to MachineAuthDto
        when(objectMapper.readValue(any(byte[].class), eq(MachineAuthDto.class)))
                .thenReturn(new MachineAuthDto() {{
                    setMachineName("machine1");
                    setUserId("user1");
                    setPassword("pass123");
                    setAuthType("NEW");
                    setTimestamp(now);
                }});

        // 4️⃣ Mock REST call inside getTokenResponseDTO
        String fakeResponse = "{\"response\":{}}"; // minimal valid JSON
        when(restTemplate.postForEntity(any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(fakeResponse));

        // 5️⃣ Mock ObjectMapper.readValue to convert fake response
        ResponseWrapper<TokenResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new TokenResponseDto());
        when(objectMapper.readValue(eq(fakeResponse), any(TypeReference.class))).thenReturn(wrapper);

        // 6️⃣ Mock encryption
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());

        // Call public method
        assertThrows(RequestException.class, () -> {
            service.getAuthToken(token);
        });

    }

    // ======================= SUCCESS =======================
    @Test
    void getAuthToken_emptyTokenResponse() throws Exception {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Create JWT-like token
        String headerJson = "{\"kid\":\"KEY1\"}";
        String payloadJson = "{\"machineName\":\"machine1\",\"userId\":\"user1\",\"password\":\"pass123\",\"timestamp\":\"" + now + "\",\"authType\":\"OTP\"}";
        String token = Base64.getUrlEncoder().encodeToString(headerJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString(payloadJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString("sig".getBytes());

        // 1️⃣ Mock machineRepository to return the machine (used in validateRequestData)
        when(machineRepository.findBySignKeyIndex("KEY1")).thenReturn(List.of(machine));

        // 2️⃣ Force validateSignature = true
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);

        // 3️⃣ Mock payload to MachineAuthDto
        when(objectMapper.readValue(any(byte[].class), eq(MachineAuthDto.class)))
                .thenReturn(new MachineAuthDto() {{
                    setMachineName("machine1");
                    setUserId("user1");
                    setPassword("pass123");
                    setAuthType("OTP");
                    setTimestamp(now);
                }});

        // 4️⃣ Mock REST call inside getTokenResponseDTO
        String fakeResponse = "{\"response\":{}}"; // minimal valid JSON
        when(restTemplate.postForEntity(any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(fakeResponse));

        // 5️⃣ Mock ObjectMapper.readValue to convert fake response
        ResponseWrapper<TokenResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new TokenResponseDto());
        when(objectMapper.readValue(eq(fakeResponse), any(TypeReference.class))).thenReturn(wrapper);

        // 6️⃣ Mock encryption
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());

        // Call public method
        assertThrows(RequestException.class, () -> {
            service.getAuthToken(token);
        });

    }

    @Test
    void getAuthToken_whenTokenResponseIsEmpty_thenThrowsRequestException() throws Exception {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Create JWT-like token
        String headerJson = "{\"kid\":\"KEY1\"}";
        String payloadJson = "{\"machineName\":\"machine1\",\"userId\":\"user1\",\"password\":\"pass123\",\"timestamp\":\"" + now + "\",\"authType\":\"OTP\"}";
        String token = Base64.getUrlEncoder().encodeToString(headerJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString(payloadJson.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString("sig".getBytes());

        // 1️⃣ Mock machineRepository to return the machine (used in validateRequestData)
        when(machineRepository.findBySignKeyIndex("KEY1")).thenReturn(List.of(machine));

        // 2️⃣ Force validateSignature = true
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any())).thenReturn(true);

        // 3️⃣ Mock payload to MachineAuthDto
        when(objectMapper.readValue(any(byte[].class), eq(MachineAuthDto.class)))
                .thenReturn(new MachineAuthDto() {{
                    setMachineName("machine1");
                    setUserId("user1");
                    setPassword("pass123");
                    setAuthType("REFRESH");
                    setTimestamp(now);
                }});

        // 4️⃣ Mock REST call inside getTokenResponseDTO
        String fakeResponse = "{\"response\":{}}"; // minimal valid JSON
        when(restTemplate.postForEntity(any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(fakeResponse));

        // 5️⃣ Mock ObjectMapper.readValue to convert fake response
        ResponseWrapper<TokenResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new TokenResponseDto());
        when(objectMapper.readValue(eq(fakeResponse), any(TypeReference.class))).thenReturn(wrapper);

        // 6️⃣ Mock encryption
        when(cryptomanagerUtils.decodeBase64Data(anyString())).thenReturn("decoded".getBytes());

        // Call public method
        assertThrows(RequestException.class, () -> {
            service.getAuthToken(token);
        });

    }

    // =====================================================
    // TIMESTAMP VALIDATION
    // =====================================================

    @Test
    void validateRequestTimestamp_invalid() {

        LocalDateTime oldTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(2);

        assertThrows(RequestException.class,
                () -> ReflectionTestUtils.invokeMethod(service,
                        "validateRequestTimestamp", oldTime));
    }

    // =====================================================
    // sendOTP SUCCESS
    // =====================================================

    @Test
    void sendOTP_success() throws Exception {

        ReflectionTestUtils.setField(service, "sendOTPUrl", "http://otp");

        String headerJson = "{\"kid\":\"KEY1\"}";
        String payloadJson = "{\"userId\":\"user1\",\"timestamp\":\""
                + LocalDateTime.now(ZoneOffset.UTC) + "\",\"otpChannel\":\"email\"}";

        String token = Base64.getUrlEncoder().encodeToString(headerJson.getBytes()) + "."
                + Base64.getUrlEncoder().encodeToString(payloadJson.getBytes()) + "."
                + Base64.getUrlEncoder().encodeToString("sig".getBytes());

        // ✅ Machine mock
        when(machineRepository.findBySignKeyIndex("KEY1"))
                .thenReturn(List.of(machine));

        // ✅ decode base64 mock
        when(cryptomanagerUtils.decodeBase64Data(anyString()))
                .thenReturn("decoded".getBytes());

        // ✅ signature validation
        when(clientCryptoFacade.validateSignature(any(), any(), any(), any()))
                .thenReturn(true);

        // ✅ MachineOtpDto
        MachineOtpDto otpDto = new MachineOtpDto();
        otpDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        otpDto.setUserId("user1");
        otpDto.setOtpChannel(Collections.singletonList("email"));

        when(objectMapper.readValue(any(byte[].class), eq(MachineOtpDto.class)))
                .thenReturn(otpDto);

        // ✅ REST response
        when(restTemplate.postForEntity(any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));

        // ✅ final wrapper parse
        when(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .thenReturn(new ResponseWrapper<>());

        ResponseWrapper<AuthNResponse> response =
                service.sendOTP(token);

        assertNotNull(response);
    }

    // =====================================================
    // getTokenResponseDTO NULL RESPONSE
    // =====================================================

    @Test
    void getTokenResponseDTO_nullBranch() {

        MachineAuthDto dto = new MachineAuthDto();
        dto.setAuthType("UNKNOWN");

        assertThrows(RequestException.class,
                () -> ReflectionTestUtils.invokeMethod(service,
                        "getTokenResponseDTO", dto));
    }

    // =====================================================
    // SIGNATURE INVALID
    // =====================================================

    @Test
    void validateRequestData_invalidSignature() {

        assertThrows(RequestException.class,
                () -> ReflectionTestUtils.invokeMethod(service,
                        "validateRequestData",
                        "{}".getBytes(),
                        "{}".getBytes(),
                        "sig".getBytes()));
    }

}
