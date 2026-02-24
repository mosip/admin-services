package io.mosip.kernel.syncdata.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.constant.ClientType;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.dto.RegistrationCenterMachineDto;
import io.mosip.kernel.syncdata.dto.SyncUserDto;
import io.mosip.kernel.syncdata.dto.response.RegistrationCenterUserResponseDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;
import io.mosip.kernel.syncdata.entity.UserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import io.mosip.kernel.syncdata.exception.DataNotFoundException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.impl.SyncUserDetailsServiceImpl;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import org.springframework.security.access.AccessDeniedException;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncUserDetailsServiceImplTest {

    @InjectMocks
    private SyncUserDetailsServiceImpl service;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private SyncMasterDataServiceHelper serviceHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ClientCryptoManagerService clientCryptoManagerService;

    @Mock
    private MapperUtils mapper;

    private final String REG_CENTER_ID = "RC01";
    private final String KEY_INDEX = "KEY01";

    // ---------------------------------------------------------
    // 1️⃣ getUsersBasedOnRegistrationCenterId SUCCESS
    // ---------------------------------------------------------
    @Test
    public void testGetUsersBasedOnRegistrationCenterId_Success() {

        UserDetails user = new UserDetails();
        user.setId("user1");
        user.setRegCenterId(REG_CENTER_ID);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setLangCode("ENG");

        when(userDetailsRepository.findByUsersByRegCenterId(REG_CENTER_ID))
                .thenReturn(List.of(user));

        RegistrationCenterUserResponseDto response =
                service.getUsersBasedOnRegistrationCenterId(REG_CENTER_ID);

        assertNotNull(response);
        assertEquals(1, response.getRegistrationCenterUsers().size());
        assertEquals("user1", response.getRegistrationCenterUsers().get(0).getUserId());
    }

    // ---------------------------------------------------------
    // 2️⃣ DataAccessException
    // ---------------------------------------------------------
    @Test
    public void testGetUsersBasedOnRegistrationCenterId_DataAccessException() {

        when(userDetailsRepository.findByUsersByRegCenterId(REG_CENTER_ID))
                .thenThrow(new DataAccessException("DB error") {});

        assertThrows(SyncDataServiceException.class,
                () -> service.getUsersBasedOnRegistrationCenterId(REG_CENTER_ID));
    }

    // ---------------------------------------------------------
    // 3️⃣ No Users Found
    // ---------------------------------------------------------
    @Test
    public void testGetUsersBasedOnRegistrationCenterId_NoUsers() {

        when(userDetailsRepository.findByUsersByRegCenterId(REG_CENTER_ID))
                .thenReturn(List.of());

        assertThrows(DataNotFoundException.class,
                () -> service.getUsersBasedOnRegistrationCenterId(REG_CENTER_ID));
    }

    // ---------------------------------------------------------
    // 4️⃣ getUserDetailsFromAuthServer SUCCESS
    // ---------------------------------------------------------
    @Test
    public void testGetUserDetailsFromAuthServer_Success() throws Exception {

        String responseBody = "{\"response\":{}}";

        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(responseEntity);

        ResponseWrapper<UserDetailResponseDto> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(new UserDetailResponseDto());

        when(objectMapper.readValue(eq(responseBody), any(TypeReference.class)))
                .thenReturn(wrapper);

        UserDetailResponseDto result =
                service.getUserDetailsFromAuthServer(List.of("user1"));

        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 5️⃣ 401 Unauthorized
    // ---------------------------------------------------------
    @Test
    public void testGetUserDetailsFromAuthServer_401() {

        HttpClientErrorException ex =
                HttpClientErrorException.create(HttpStatus.UNAUTHORIZED,
                        "Unauthorized", HttpHeaders.EMPTY, null, null);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(ex);

        assertThrows(BadCredentialsException.class,
                () -> service.getUserDetailsFromAuthServer(List.of("user1")));
    }

    // ---------------------------------------------------------
    // 6️⃣ 403 Forbidden
    // ---------------------------------------------------------
    @Test
    public void testGetUserDetailsFromAuthServer_403() {

        HttpClientErrorException ex =
                HttpClientErrorException.create(HttpStatus.FORBIDDEN,
                        "Forbidden", HttpHeaders.EMPTY, null, null);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(ex);

        assertThrows(AccessDeniedException.class,
                () -> service.getUserDetailsFromAuthServer(List.of("user1")));
    }

    // ---------------------------------------------------------
    // 7️⃣ Other HTTP Error
    // ---------------------------------------------------------
    @Test
   public void testGetUserDetailsFromAuthServer_OtherError() {

        HttpServerErrorException ex =
                HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error", HttpHeaders.EMPTY, null, null);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(ex);

        assertThrows(SyncDataServiceException.class,
                () -> service.getUserDetailsFromAuthServer(List.of("user1")));
    }

    // ---------------------------------------------------------
    // 8️⃣ getAllUserDetailsBasedOnKeyIndex SUCCESS
    // ---------------------------------------------------------
    @Test
    public void testGetAllUserDetailsBasedOnKeyIndex_Success() throws Exception {

        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setRegCenterId(REG_CENTER_ID);
        machineDto.setPublicKey("PUBKEY");

        when(serviceHelper.getRegistrationCenterMachine(null, KEY_INDEX))
                .thenReturn(machineDto);

        // mock users
        UserDetails user = new UserDetails();
        user.setId("user1");
        user.setRegCenterId(REG_CENTER_ID);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setLangCode("ENG");

        when(userDetailsRepository.findByUsersByRegCenterId(REG_CENTER_ID))
                .thenReturn(List.of(user));

        // mock auth response
        UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto();
        userDetailResponseDto.setMosipUserDtoList(new ArrayList<>());

        SyncUserDetailsServiceImpl spy = Mockito.spy(service);
        doReturn(userDetailResponseDto)
                .when(spy).getUserDetailsFromAuthServer(any());

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("encryptedData");

        SyncUserDto result =
                spy.getAllUserDetailsBasedOnKeyIndex(KEY_INDEX);

        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // 9️⃣ getAllUserDetailsBasedOnKeyIndexV2 SUCCESS
    // ---------------------------------------------------------
    @Test
    public void testGetAllUserDetailsBasedOnKeyIndexV2_Success() throws Exception {

        RegistrationCenterMachineDto machineDto = new RegistrationCenterMachineDto();
        machineDto.setRegCenterId(REG_CENTER_ID);
        machineDto.setPublicKey("PUBKEY");
        machineDto.setClientType(ClientType.TPM);

        when(serviceHelper.getRegistrationCenterMachine(null, KEY_INDEX))
                .thenReturn(machineDto);

        UserDetails user = new UserDetails();
        user.setId("user1");
        user.setRegCenterId(REG_CENTER_ID);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setLangCode("ENG");

        when(userDetailsRepository.findByUsersByRegCenterId(REG_CENTER_ID))
                .thenReturn(List.of(user));

        when(mapper.getObjectAsJsonString(any())).thenReturn("json");

        TpmCryptoResponseDto cryptoResponse = new TpmCryptoResponseDto();
        cryptoResponse.setValue("encryptedData");

        when(clientCryptoManagerService.csEncrypt(any()))
                .thenReturn(cryptoResponse);

        SyncUserDto result =
                service.getAllUserDetailsBasedOnKeyIndexV2(KEY_INDEX);

        assertNotNull(result);
        assertEquals("encryptedData", result.getUserDetails());
    }

}