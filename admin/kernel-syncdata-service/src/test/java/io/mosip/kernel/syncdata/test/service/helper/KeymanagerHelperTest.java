package io.mosip.kernel.syncdata.test.service.helper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.signature.dto.JWTSignatureRequestDto;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.syncdata.dto.response.KeyPairGenerateResponseDto;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.constant.AdminServiceErrorCode;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

public class KeymanagerHelperTest {

    @InjectMocks
    private KeymanagerHelper keymanagerHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set private @Value fields using ReflectionTestUtils
        ReflectionTestUtils.setField(keymanagerHelper, "certificateUrl", "http://dummy-certificate-url");
        ReflectionTestUtils.setField(keymanagerHelper, "signUrl", "http://dummy-sign-url");
        ReflectionTestUtils.setField(keymanagerHelper, "signApplicationid", "KERNEL");
        ReflectionTestUtils.setField(keymanagerHelper, "signRefid", "SIGN");
    }

    @Test
    public void testGetCertificate_success() throws Exception {
        KeyPairGenerateResponseDto responseDto = new KeyPairGenerateResponseDto();
        ResponseWrapper<KeyPairGenerateResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(responseDto);

        String jsonResponse = "{}"; // dummy JSON
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(wrapper);

        KeyPairGenerateResponseDto result = keymanagerHelper.getCertificate("APPID", Optional.of("REFID"));
        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    public void testGetCertificate_withValidationErrors_shouldThrowSyncDataServiceException() throws Exception {
        ResponseWrapper<KeyPairGenerateResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setErrors(Collections.singletonList(new ServiceError("ERR001", "Invalid input")));

        String jsonResponse = "{}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(wrapper);

        SyncDataServiceException exception = assertThrows(SyncDataServiceException.class, () ->
                keymanagerHelper.getCertificate("APPID", Optional.empty()));

        assertEquals(AdminServiceErrorCode.VALIDATION_ERROR.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetSignature_success() throws Exception {
        String responseBody = "{\"key\":\"value\"}";

        JWTSignatureResponseDto jwtResponse = new JWTSignatureResponseDto();
        jwtResponse.setJwtSignedData("signedData");

        ResponseWrapper<JWTSignatureResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(jwtResponse);

        String jsonResponse = "{}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.postForEntity(any(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(wrapper);

        String result = keymanagerHelper.getSignature(responseBody);
        assertEquals("signedData", result);
    }

    @Test
    public void testGetFileSignature_success() throws Exception {
        KeymanagerHelper helperSpy = spy(keymanagerHelper);
        doReturn("signedHash").when(helperSpy).getSignature(any());

        String result = helperSpy.getFileSignature("hashValue");
        assertEquals("signedHash", result);

        verify(helperSpy).getSignature("{\"hash\":\"hashValue\"}");
    }

    @Test
    public void testGetCertificate_internalServerError() throws Exception {
        // Mock RestTemplate to throw an exception
        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        // Call the method and assert that SyncDataServiceException is thrown
        SyncDataServiceException exception = assertThrows(SyncDataServiceException.class, () ->
                keymanagerHelper.getCertificate("APPID", Optional.of("REFID")));

        // Verify the error code is INTERNAL_SERVER_ERROR
        assertEquals(AdminServiceErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), exception.getErrorCode());
    }
}
