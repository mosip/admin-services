package io.mosip.kernel.syncdata.test.helper;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.syncdata.dto.response.KeyPairGenerateResponseDto;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeymanagerHelperTest {

    @InjectMocks
    private KeymanagerHelper keymanagerHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(
                keymanagerHelper,
                "certificateUrl",
                "http://localhost/keymanager/cert"
        );
        ReflectionTestUtils.setField(
                keymanagerHelper,
                "signUrl",
                "http://localhost/keymanager/sign"
        );
        ReflectionTestUtils.setField(
                keymanagerHelper,
                "signApplicationid",
                "KERNEL"
        );
        ReflectionTestUtils.setField(
                keymanagerHelper,
                "signRefid",
                "SIGN"
        );
    }

    /* ====================== getCertificate ====================== */

    @Test
    public void testGetCertificateSuccessNoErrorsWithReferenceId() throws Exception {
        KeyPairGenerateResponseDto expected = new KeyPairGenerateResponseDto();
        ResponseWrapper<KeyPairGenerateResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(expected);
        wrapper.setErrors(null); // errors == null
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        KeyPairGenerateResponseDto result = keymanagerHelper.getCertificate(
                "APP_ID",
                Optional.of("REF_ID")
        );
        assertSame(expected, result);
    }

    @Test
    public void testGetCertificateSuccessEmptyErrorsNoReferenceId() throws Exception {
        KeyPairGenerateResponseDto expected = new KeyPairGenerateResponseDto();
        ResponseWrapper<KeyPairGenerateResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(expected);
        wrapper.setErrors(Collections.emptyList()); // errors non null mais vide
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        KeyPairGenerateResponseDto result = keymanagerHelper.getCertificate(
                "APP_ID",
                Optional.empty()
        );
        assertSame(expected, result);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetCertificateWithErrorsThrowsSyncDataServiceException() throws Exception {
        ResponseWrapper<KeyPairGenerateResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new KeyPairGenerateResponseDto());
        final ServiceError serviceError = new ServiceError("TEST", "TEST");
        wrapper.setErrors(Collections.singletonList(serviceError));
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        keymanagerHelper.getCertificate("APP_ID", Optional.of("REF_ID"));
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetCertificateRestTemplateThrowsThrowsSyncDataServiceException() throws Exception {
        when(restTemplate.getForEntity(any(), eq(String.class))).thenThrow(new RuntimeException("REST error"));
        keymanagerHelper.getCertificate("APP_ID", Optional.empty());
    }

    /* ====================== getSignature ====================== */

    @Test
    public void testGetSignatureSuccessReturnsJwtSignedData() throws Exception {
        JWTSignatureResponseDto responseDto = new JWTSignatureResponseDto();
        responseDto.setJwtSignedData("SIGNED_DATA");
        ResponseWrapper<JWTSignatureResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(responseDto);
        wrapper.setErrors(null);
        when(restTemplate.postForEntity(any(), any(HttpEntity.class), eq(String.class))).thenReturn(
                ResponseEntity.ok("{}")
        );
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        String result = keymanagerHelper.getSignature("{\"data\":\"test\"}");
        assertEquals("SIGNED_DATA", result);
    }

    @Test(expected = SyncInvalidArgumentException.class)
    public void testGetSignatureWithErrorsThrowsSyncInvalidArgumentException() throws Exception {
        JWTSignatureResponseDto responseDto = new JWTSignatureResponseDto();
        ResponseWrapper<JWTSignatureResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(responseDto);
        final ServiceError serviceError = new ServiceError("TEST", "TEST");
        wrapper.setErrors(Collections.singletonList(serviceError)); // errors non vide
        when(restTemplate.postForEntity(any(), any(HttpEntity.class), eq(String.class))).thenReturn(
                ResponseEntity.ok("{}")
        );
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(wrapper);
        keymanagerHelper.getSignature("{\"data\":\"test\"}");
    }

    @Test(expected = RuntimeException.class)
    public void testGetSignatureRestTemplateThrowsPropagatesException() throws Exception {
        when(restTemplate.postForEntity(any(), any(HttpEntity.class), eq(String.class))).thenThrow(
                new RuntimeException("REST error")
        );
        keymanagerHelper.getSignature("{\"data\":\"test\"}");
    }

    /* ====================== getFileSignature ====================== */

    @Test
    public void testGetFileSignatureDelegatesToGetSignature() throws Exception {
        // on utilise un spy pour vérifier l'argument passé à getSignature
        KeymanagerHelper spyHelper = Mockito.spy(keymanagerHelper);
        doReturn("SIGNED_HASH").when(spyHelper).getSignature("{\"hash\":\"abc123\"}");
        String result = spyHelper.getFileSignature("abc123");
        assertEquals("SIGNED_HASH", result);
        verify(spyHelper, times(1)).getSignature("{\"hash\":\"abc123\"}");
    }

}