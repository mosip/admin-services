package io.mosip.kernel.syncdata.test.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.dto.response.RolesResponseDto;
import io.mosip.kernel.syncdata.exception.ParseResponseException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.service.impl.SyncRolesServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SyncRolesServiceImplTest {

    private SyncRolesServiceImpl service;

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        service = new SyncRolesServiceImpl();
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(service, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(service, "authBaseUrl", "http://auth");
        ReflectionTestUtils.setField(service, "authServiceName", "/roles");
        ReflectionTestUtils.setField(service, "syncDataRequestId", "SYNCDATA.REQUEST");
        ReflectionTestUtils.setField(service, "syncDataVersionId", "v1.0");
    }

    private String buildSuccessBody() throws Exception {
        ResponseWrapper<RolesResponseDto> wrapper = new ResponseWrapper<>();
        RolesResponseDto dto = new RolesResponseDto();
        wrapper.setResponse(dto);
        return objectMapper.writeValueAsString(wrapper);
    }

    private String buildBodyWithErrors() throws Exception {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        ServiceError error = new ServiceError("TEST", "TEST MESSAGE");
        wrapper.setErrors(Collections.singletonList(error));
        return objectMapper.writeValueAsString(wrapper);
    }

    /* ======================= SUCCESS PATH ======================= */

    @Test
    public void testGetAllRolesSuccess() throws Exception {
        String body = buildSuccessBody();
        ResponseEntity<String> httpResponse = ResponseEntity.ok(body);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(httpResponse);
        RolesResponseDto result = service.getAllRoles();
        assertNotNull(result);
    }

    /* ======================= HTTP ERROR 401 ======================= */

    @Test(expected = AuthNException.class)
    public void testGetAllRolesHttp401WithErrorsThrowsAuthNException() throws Exception {
        String errorBody = buildBodyWithErrors();
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNAUTHORIZED,
                "401",
                HttpHeaders.EMPTY,
                errorBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(ex);
        service.getAllRoles();
    }

    @Test(expected = BadCredentialsException.class)
    public void testGetAllRolesHttp401NoErrorsThrowsBadCredentialsException() throws Exception {
        String errorBody = buildSuccessBody();
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNAUTHORIZED,
                "401",
                HttpHeaders.EMPTY,
                errorBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(ex);
        service.getAllRoles();
    }

    /* ======================= HTTP ERROR 403 ======================= */

    @Test(expected = AuthZException.class)
    public void testGetAllRolesHttp403WithErrorsThrowsAuthZException() throws Exception {
        String errorBody = buildBodyWithErrors();
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.FORBIDDEN,
                "403",
                HttpHeaders.EMPTY,
                errorBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(ex);
        service.getAllRoles();
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetAllRolesHttp403NoErrorsThrowsAccessDeniedException() throws Exception {
        String errorBody = buildSuccessBody();
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.FORBIDDEN,
                "403",
                HttpHeaders.EMPTY,
                errorBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(ex);
        service.getAllRoles();
    }

    /* ======================= AUTRE HTTP ERROR (500) ======================= */

    @Test(expected = SyncDataServiceException.class)
    public void testGetAllRolesHttp500ThrowsSyncDataServiceException() throws Exception {
        String errorBody = buildSuccessBody();
        HttpServerErrorException ex = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "500",
                HttpHeaders.EMPTY,
                errorBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenThrow(ex);
        service.getAllRoles();
    }

    /* ======================= RESPONSE AVEC ERREURS ======================= */

    @Test(expected = SyncServiceException.class)
    public void testGetAllRolesResponseHasValidationErrorsThrowsSyncServiceException() throws Exception {
        String body = buildBodyWithErrors();
        ResponseEntity<String> httpResponse = ResponseEntity.ok(body);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(httpResponse);
        service.getAllRoles();
    }

    /* ======================= PARSE ERROR ======================= */

    @Test(expected = ParseResponseException.class)
    public void testGetAllRolesParseResponseIOExceptionThrowsParseResponseException() {
        String body = "NOT_JSON";
        ResponseEntity<String> httpResponse = ResponseEntity.ok(body);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(httpResponse);
        service.getAllRoles();
    }

}