package io.mosip.kernel.syncdata.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.dto.MachineAuthDto;
import io.mosip.kernel.syncdata.dto.MachineOtpDto;
import io.mosip.kernel.syncdata.dto.UserDetailDto;
import io.mosip.kernel.syncdata.dto.response.TokenResponseDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.service.impl.SyncAuthTokenServiceImpl;
import io.mosip.kernel.core.authmanager.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(SpringRunner.class)
public class syncAuthServiceTest {

    @Value("${mosip.syncdata.tpm.required}")
    private boolean isTPMRequired;

    @Value("${auth.token.header}")
    private String authTokenHeaderName;

    @Value("${auth.refreshtoken.header}")
    private String authRefreshTokenHeaderName;

    @Value("${mosip.kernel.authtoken.NEW.internal.url}")
    private String newAuthTokenInternalUrl;

    @Value("${mosip.kernel.authtoken.OTP.internal.url}")
    private String otpAuthTokenInternalUrl;

    @Value("${mosip.kernel.authtoken.REFRESH.internal.url}")
    private String refreshAuthTokenInternalUrl;

    @Value("${mosip.kernel.registrationclient.app.id}")
    private String authTokenInternalAppId;

    @Value("${mosip.kernel.registrationclient.client.id}")
    private String clientId;

    @Value("${mosip.kernel.registrationclient.secret.key}")
    private String secretKey;

    @Value("${mosip.kernel.auth.sendotp.url}")
    private String sendOTPUrl;

    @Value("${mosip.kernel.syncdata.auth.reqtime.maxlimit:-5}")
    private int maxMinutes;

    @Value("${mosip.kernel.syncdata.auth.reqtime.minlimit:5}")
    private int minMinutes;

    @Value("${mosip.kernel.syncdata.auth-manager-base-uri}")
    private String authUserDetailsBaseUri;

    @Value("${mosip.kernel.syncdata.auth-user-details:/userdetails}")
    private String authUserDetailsUri;

    @MockBean
    private ClientCryptoFacade clientCryptoFacade;

    @MockBean
    private MachineRepository machineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SyncAuthTokenServiceImpl syncAuthTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SyncUserDetailsService syncUserDetailsService;


    private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
    private byte[] tpmPublicKey = Base64.getUrlDecoder().decode(encodedTPMPublicKey);
    private String keyIndex = "tetw:sdfsf:Sdfdfsd";

    private List<Machine> machines = new ArrayList<Machine>();
    private ResponseWrapper<TokenResponseDto> responseWrapper;
    private ResponseWrapper<AuthNResponse> otpResponseWrapper;

    @Before
    public void setup() throws JsonProcessingException {
        Machine machine = new Machine("1001", "Laptop", "9876427", "172.12.01.128", "21:21:21:12", "1001",
                "ENG", LocalDateTime.now(), encodedTPMPublicKey, keyIndex, "ZONE","10002", null,encodedTPMPublicKey, keyIndex);
        machines.add(machine);

        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setToken("test");
        tokenResponseDto.setRefreshToken("testst");
        tokenResponseDto.setExpiryTime(1200);
        tokenResponseDto.setRefreshExpiryTime(1200);
        responseWrapper = new ResponseWrapper<TokenResponseDto>();
        responseWrapper.setResponse(tokenResponseDto);
        responseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));

        AuthNResponse authNResponse = new AuthNResponse();
        otpResponseWrapper = new ResponseWrapper<AuthNResponse>();
        otpResponseWrapper.setResponse(authNResponse);
        otpResponseWrapper.setResponsetime(LocalDateTime.now(ZoneOffset.UTC));

        byte[] dumbCipher = new byte[0];
        when(clientCryptoFacade.encrypt(Mockito.any(), Mockito.any())).thenReturn(dumbCipher);

    }


    @Test
    public void validTokenCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String response = syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(response);
    }

    @Test
    public void validRefreshTokenCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("Refresh");
        machineAuthDto.setUserId("test");
        machineAuthDto.setRefreshToken("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(refreshAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String response = syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(response);
    }

    @Test
    public void validateOtpCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("OTP");
        machineAuthDto.setOtp("11111");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(otpAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        StringBuilder userDetailsUri = new StringBuilder();
        userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
        String userDetailsResponse = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[{\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";
        mockRestServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
                .andRespond(withSuccess().body(userDetailsResponse).contentType(MediaType.APPLICATION_JSON));

        String response = syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(response);
    }

    @Test(expected = RequestException.class)
    public void requestWithOldReqTimestamp() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).minusHours(1));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
    }

    @Test(expected = RequestException.class)
    public void requestWithFutureReqTimestamp() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusHours(1));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
    }

    @Test(expected = RequestException.class)
    public void requestWithInvalidKeyIndex() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString("{\"kid\":null}".getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
    }

    @Test(expected = RequestException.class)
    public void requestWithInvalidSignature() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(false);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, null));
    }

    @Test
    public void validOtpReqCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineOtpDto machineOtpDto = new MachineOtpDto();
        machineOtpDto.setOtpChannel(new ArrayList<>());
        machineOtpDto.setContext("test");
        machineOtpDto.setAppId("test");
        machineOtpDto.setUserId("test");
        machineOtpDto.setUseridtype("email");
        machineOtpDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        StringBuilder userDetailsUri = new StringBuilder();
        userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
        String userDetailsResponse = "{\"id\":\"SYNCDATA.REQUEST\",\"version\":\"v1.0\",\"responsetime\":\"2019-03-31T10:40:29.935Z\",\"metadata\":null,\"response\":{\"mosipUserDtoList\":[{\"userId\":\"110001\",\"mobile\":\"9663175928\",\"mail\":\"110001@mosip.io\",\"langCode\":null,\"userPassword\":\"e1NTSEE1MTJ9L25EVy9tajdSblBMZFREYjF0dXB6TzdCTmlWczhKVnY1TXJ1aXRSZlBrSCtNVmJDTXVIM2lyb2thcVhsdlR6WkNKYXAwSncrSXc5SFc3aWRYUnpnaHBTQktrNXRSVTA3\",\"name\":\"user\",\"role\":\"REGISTRATION_ADMIN,REGISTRATION_OFFICER\"}]},\"errors\":null}";
        mockRestServer.expect(requestTo(userDetailsUri.toString() + "/registrationclient"))
                .andRespond(withSuccess().body(userDetailsResponse).contentType(MediaType.APPLICATION_JSON));
        mockRestServer.expect(requestTo(sendOTPUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(otpResponseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineOtpDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        ResponseWrapper<AuthNResponse> response = syncAuthTokenService.sendOTP(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(response);
    }

    @Test
    public void validReqValidReqTimeCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));

        MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServer.expect(requestTo(newAuthTokenInternalUrl))
                .andRespond(withSuccess().body(objectMapper.writeValueAsString(responseWrapper)));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineAuthDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
    }

    @Test(expected = RequestException.class)
    public void validOtpReqInvalidReqTimeCase() throws JsonProcessingException {
        when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineOtpDto machineOtpDto = new MachineOtpDto();
        machineOtpDto.setOtpChannel(new ArrayList<>());
        machineOtpDto.setContext("test");
        machineOtpDto.setAppId("test");
        machineOtpDto.setUserId("test");
        machineOtpDto.setUseridtype("email");
        machineOtpDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(7));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(machineOtpDto).getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.sendOTP(String.format("%s.%s.%s", header, payload, signature));
    }
}
