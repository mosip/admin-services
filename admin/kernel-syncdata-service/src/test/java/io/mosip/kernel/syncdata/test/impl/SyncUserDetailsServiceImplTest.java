package io.mosip.kernel.syncdata.test.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.dto.RegistrationCenterMachineDto;
import io.mosip.kernel.syncdata.dto.RegistrationCenterUserDto;
import io.mosip.kernel.syncdata.dto.SyncUserDto;
import io.mosip.kernel.syncdata.dto.response.RegistrationCenterUserResponseDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.ParseResponseException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.impl.SyncUserDetailsServiceImpl;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class SyncUserDetailsServiceImplTest {

    private SyncUserDetailsServiceImpl service;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private ClientCryptoManagerService clientCryptoManagerService;

    @Mock
    private MapperUtils mapper;

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SyncUserDetailsServiceImpl();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ReflectionTestUtils.setField(service, "userDetailsRepository", userDetailsRepository);
        ReflectionTestUtils.setField(service, "serviceHelper", serviceHelper);
        ReflectionTestUtils.setField(service, "clientCryptoManagerService", clientCryptoManagerService);
        ReflectionTestUtils.setField(service, "mapper", mapper);
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(service, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(service, "authUserDetailsBaseUri", "http://auth");
        ReflectionTestUtils.setField(service, "authUserDetailsUri", "/userdetails");
        ReflectionTestUtils.setField(service, "syncDataRequestId", "SYNCDATA.REQUEST");
        ReflectionTestUtils.setField(service, "syncDataVersionId", "v1.0");
        ReflectionTestUtils.setField(service, "pageSize", 2);
    }

    // -------------------------------------------------------------------------
    // getUserDetailsFromAuthServer – invalid params
    // -------------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetUserDetailsFromAuthServerNullIds() {
        service.getUserDetailsFromAuthServer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserDetailsFromAuthServerEmptyIds() {
        service.getUserDetailsFromAuthServer(Arrays.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserDetailsFromAuthServerNullResponse() throws Exception {
        List<String> ids = Arrays.asList("u1", "u2");
        ResponseEntity<String> entity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(entity);
        service.getUserDetailsFromAuthServer(ids);
    }

    @Test(expected = ParseResponseException.class)
    public void testGetUserDetailsFromAuthServerEmptyResponse() throws Exception {
        List<String> ids = Arrays.asList("u1", "u2");
        ResponseEntity<String> entity = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(entity);
        service.getUserDetailsFromAuthServer(ids);
    }

    // -------------------------------------------------------------------------
    // getUserDetailsFromAuthServer – success
    // -------------------------------------------------------------------------

    @Test
    public void testGetUserDetailsFromAuthServerSuccess() throws Exception {
        List<String> ids = Arrays.asList("u1", "u2");
        UserDetailResponseDto dto = new UserDetailResponseDto();
        ResponseWrapper<UserDetailResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(dto);
        String body = objectMapper.writeValueAsString(wrapper);
        ResponseEntity<String> entity = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(entity);
        UserDetailResponseDto result = service.getUserDetailsFromAuthServer(ids);
        assertEquals(dto, result);
    }

    // -------------------------------------------------------------------------
    // getUserDetailsFromAuthServer – 401/403/500 branches
    // -------------------------------------------------------------------------

    @Test(expected = AuthNException.class)
    public void testGetUserDetailsFromAuthServer401WithErrorsThrowsAuthN() throws Exception {
        List<String> ids = Collections.singletonList("u1");
        ServiceError err = new ServiceError("KER-TEST-001", "auth error");
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setErrors(Collections.singletonList(err));
        String body = objectMapper.writeValueAsString(wrapper);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNAUTHORIZED,
                "401",
                HttpHeaders.EMPTY,
                body.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(ex);
        service.getUserDetailsFromAuthServer(ids);
    }

    @Test(expected = BadCredentialsException.class)
    public void testGetUserDetailsFromAuthServer401NoErrorsThrowsBadCredentials() throws Exception {
        List<String> ids = Collections.singletonList("u1");
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        String body = objectMapper.writeValueAsString(wrapper);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNAUTHORIZED,
                "401",
                HttpHeaders.EMPTY,
                body.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(ex);
        service.getUserDetailsFromAuthServer(ids);
    }

    @Test(expected = AuthZException.class)
    public void testGetUserDetailsFromAuthServer403WithErrorsThrowsAuthZ() throws Exception {
        List<String> ids = Collections.singletonList("u1");
        ServiceError err = new ServiceError("KER-TEST-002", "authz error");
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setErrors(Collections.singletonList(err));
        String body = objectMapper.writeValueAsString(wrapper);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.FORBIDDEN,
                "403",
                HttpHeaders.EMPTY,
                body.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(ex);
        service.getUserDetailsFromAuthServer(ids);
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetUserDetailsFromAuthServer403NoErrorsThrowsAccessDenied() throws Exception {
        List<String> ids = Collections.singletonList("u1");
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        String body = objectMapper.writeValueAsString(wrapper);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.FORBIDDEN,
                "403",
                HttpHeaders.EMPTY,
                body.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(ex);
        service.getUserDetailsFromAuthServer(ids);
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetUserDetailsFromAuthServer500ThrowsSyncDataServiceException() throws Exception {
        List<String> ids = Collections.singletonList("u1");
        HttpServerErrorException ex = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "500",
                HttpHeaders.EMPTY,
                "error".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(ex);
        service.getUserDetailsFromAuthServer(ids);
    }

    // -------------------------------------------------------------------------
    // getUserDetailFromResponse (private) via ReflectionTestUtils
    // -------------------------------------------------------------------------

    @Test(expected = SyncServiceException.class)
    public void testGetUserDetailFromResponseWithErrorsThrowsSyncServiceException() throws Exception {
        ServiceError err = new ServiceError("KER-TEST-003", "validation");
        ResponseWrapper<UserDetailResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setErrors(Collections.singletonList(err));
        String json = objectMapper.writeValueAsString(wrapper);
        ReflectionTestUtils.invokeMethod(service, "getUserDetailFromResponse", json);
    }

    @Test(expected = ParseResponseException.class)
    public void testGetUserDetailFromResponseNullResponseThrowsParseResponseException() throws Exception {
        ResponseWrapper<UserDetailResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(null);
        String json = objectMapper.writeValueAsString(wrapper);
        ReflectionTestUtils.invokeMethod(service, "getUserDetailFromResponse", json);
    }

    @Test(expected = ParseResponseException.class)
    public void testGetUserDetailFromResponse_InvalidJson_ThrowsParseResponseException() {
        ReflectionTestUtils.invokeMethod(service, "getUserDetailFromResponse", "not-json");
    }

    // -------------------------------------------------------------------------
    // getUsersBasedOnRegistrationCenterId
    // -------------------------------------------------------------------------

    @Test
    public void testGetUsersBasedOnRegistrationCenterIdSuccess() {
        String regCenterId = "RC1";
        UserDetails u1 = mock(UserDetails.class);
        when(u1.getIsActive()).thenReturn(true);
        when(u1.getIsDeleted()).thenReturn(false);
        when(u1.getLangCode()).thenReturn("en");
        when(u1.getRegCenterId()).thenReturn(regCenterId);
        when(u1.getId()).thenReturn("user1");
        when(userDetailsRepository.findByUsersByRegCenterId(regCenterId)).thenReturn(Collections.singletonList(u1));
        RegistrationCenterUserResponseDto resp = service.getUsersBasedOnRegistrationCenterId(regCenterId);
        assertNotNull(resp);
        assertEquals(1, resp.getRegistrationCenterUsers().size());
        assertEquals("user1", resp.getRegistrationCenterUsers().get(0).getUserId());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetUsersBasedOnRegistrationCenterIdDataAccessException() {
        when(userDetailsRepository.findByUsersByRegCenterId("RC1")).thenThrow(
                new DataAccessException("db") {
                }
        );
        service.getUsersBasedOnRegistrationCenterId("RC1");
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetUsersBasedOnRegistrationCenterIdNoUsers() {
        when(userDetailsRepository.findByUsersByRegCenterId("RC1")).thenReturn(Collections.emptyList());
        service.getUsersBasedOnRegistrationCenterId("RC1");
    }

    // -------------------------------------------------------------------------
    // fetchUsersPaged – plusieurs pages
    // -------------------------------------------------------------------------

    @Test
    public void testFetchUsersPagedReturnsAllPages() {
        String regCenterId = "RC1";
        UserDetails u1 = mock(UserDetails.class);
        when(u1.getIsActive()).thenReturn(true);
        when(u1.getIsDeleted()).thenReturn(false);
        when(u1.getLangCode()).thenReturn("en");
        when(u1.getRegCenterId()).thenReturn(regCenterId);
        when(u1.getId()).thenReturn("user1");
        UserDetails u2 = mock(UserDetails.class);
        when(u2.getIsActive()).thenReturn(true);
        when(u2.getIsDeleted()).thenReturn(false);
        when(u2.getLangCode()).thenReturn("en");
        when(u2.getRegCenterId()).thenReturn(regCenterId);
        when(u2.getId()).thenReturn("user2");
        UserDetails u3 = mock(UserDetails.class);
        when(u3.getIsActive()).thenReturn(true);
        when(u3.getIsDeleted()).thenReturn(false);
        when(u3.getLangCode()).thenReturn("en");
        when(u3.getRegCenterId()).thenReturn(regCenterId);
        when(u3.getId()).thenReturn("user3");
        Page<UserDetails> page1 = new PageImpl<>(Arrays.asList(u1, u2), PageRequest.of(0, 2), 3);
        Page<UserDetails> page2 = new PageImpl<>(Collections.singletonList(u3), PageRequest.of(1, 2), 3);
        when(userDetailsRepository.findPageByRegCenterId(
                eq(regCenterId),
                any(PageRequest.class)
        )).thenReturn(page1, page2);
        @SuppressWarnings("unchecked")
        List<RegistrationCenterUserDto> result = (List<RegistrationCenterUserDto>) ReflectionTestUtils.invokeMethod(
                service,
                "fetchUsersPaged",
                regCenterId
        );
        assertEquals(3, result.size());
        assertEquals("user1", result.get(0).getUserId());
        assertEquals("user3", result.get(2).getUserId());
    }

    // -------------------------------------------------------------------------
    // getAllUserDetailsBasedOnKeyIndexV2
    // -------------------------------------------------------------------------

    @Test
    public void testGetAllUserDetailsBasedOnKeyIndexV2SuccessEncrypts() throws Exception {
        String keyIndex = "KEY1";
        String regCenterId = "RC1";
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setRegCenterId(regCenterId);
        machineDto.setPublicKey("PUB_KEY");
        machineDto.setClientType(ClientType.LOCAL);
        when(serviceHelper.getRegistrationCenterMachine(null, keyIndex)).thenReturn(machineDto);
        UserDetails u1 = mock(UserDetails.class);
        when(u1.getIsActive()).thenReturn(true);
        when(u1.getIsDeleted()).thenReturn(false);
        when(u1.getLangCode()).thenReturn("en");
        when(u1.getRegCenterId()).thenReturn(regCenterId);
        when(u1.getId()).thenReturn("user1");
        Page<UserDetails> page = new PageImpl<>(
                Collections.singletonList(u1),
                PageRequest.of(0, 2),
                1
        );
        when(userDetailsRepository.findPageByRegCenterId(eq(regCenterId), any(PageRequest.class))).thenReturn(page);
        when(mapper.getObjectAsJsonString(any())).thenReturn("[{\"userId\":\"user1\"}]");
        TpmCryptoResponseDto enc = new TpmCryptoResponseDto();
        enc.setValue("encrypted-value");
        when(clientCryptoManagerService.csEncrypt(any(TpmCryptoRequestDto.class))).thenReturn(enc);
        SyncUserDto dto = service.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
        assertNotNull(dto);
        assertEquals("encrypted-value", dto.getUserDetails());
        verify(clientCryptoManagerService, times(1)).csEncrypt(any(TpmCryptoRequestDto.class));
    }

    @Test
    public void testGetAllUserDetailsBasedOnKeyIndexV2NoUsersNoEncryption() {
        String keyIndex = "KEY1";
        String regCenterId = "RC1";
        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setRegCenterId(regCenterId);
        machineDto.setPublicKey("PUB_KEY");
        machineDto.setClientType(ClientType.LOCAL);
        when(serviceHelper.getRegistrationCenterMachine(null, keyIndex)).thenReturn(machineDto);
        Page<UserDetails> emptyPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 2),
                0
        );
        when(userDetailsRepository.findPageByRegCenterId(eq(regCenterId), any(PageRequest.class))).thenReturn(emptyPage);
        SyncUserDto dto = service.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
        assertNotNull(dto);
        assertNull(dto.getUserDetails());
        verify(clientCryptoManagerService, never()).csEncrypt(any(TpmCryptoRequestDto.class));
    }

}