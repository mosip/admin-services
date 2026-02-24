package io.mosip.kernel.syncdata.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.dto.response.RolesResponseDto;
import io.mosip.kernel.syncdata.exception.ParseResponseException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.service.impl.SyncRolesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncRolesServiceImplTest {

    @InjectMocks
    private SyncRolesServiceImpl service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private ResponseEntity<String> mockResponse;

    @BeforeEach
    void setup() {
        mockResponse = new ResponseEntity<>("{\"response\":{}}", HttpStatus.OK);
    }

    @Test
    void getAllRoles_success() throws Exception {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        ResponseWrapper<RolesResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new RolesResponseDto());

        when(objectMapper.readValue(anyString(), eq(ResponseWrapper.class))).thenReturn(wrapper);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(objectMapper.readValue(anyString(), eq(RolesResponseDto.class))).thenReturn(new RolesResponseDto());

        RolesResponseDto result = service.getAllRoles();
        assertNotNull(result);

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
        verify(objectMapper, atLeastOnce()).readValue(anyString(), eq(ResponseWrapper.class));
    }

    @Test
    void getAllRoles_httpUnauthorized_throwsAuthNException() {
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.UNAUTHORIZED,
                "Unauthorized", HttpHeaders.EMPTY, "{}".getBytes(), null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(ex);

        assertThrows(BadCredentialsException.class, () -> service.getAllRoles());
    }

    @Test
    void getAllRoles_httpForbidden_throwsAuthZException() {
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.FORBIDDEN,
                "Forbidden", HttpHeaders.EMPTY, "{}".getBytes(), null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(ex);

        assertThrows(AccessDeniedException.class, () -> service.getAllRoles());
    }

    @Test
    void getAllRoles_httpOtherError_throwsSyncDataServiceException() {
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                "Bad Request", HttpHeaders.EMPTY, "{}".getBytes(), null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(ex);

        assertThrows(SyncDataServiceException.class, () -> service.getAllRoles());
    }

    @Test
    void getAllRoles_responseValidationError_throwsSyncServiceException() throws Exception {
        // Simulate response body with errors
        ResponseEntity<String> responseWithError = new ResponseEntity<>("{\"errors\":[{\"errorCode\":\"ERR1\",\"message\":\"msg\"}]}", HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseWithError);

        assertThrows(SyncServiceException.class, () -> service.getAllRoles());
    }
}