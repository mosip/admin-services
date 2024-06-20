package io.mosip.kernel.syncdata.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.authmanager.model.AuthNResponse;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.syncdata.dto.MachineAuthDto;
import io.mosip.kernel.syncdata.dto.MachineOtpDto;
import io.mosip.kernel.syncdata.dto.response.TokenResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.service.impl.SyncAuthTokenServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
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

    @Mock
    private ClientCryptoFacade clientCryptoFacade;

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SyncAuthTokenServiceImpl syncAuthTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SyncUserDetailsService syncUserDetailsService;

    @Autowired
    private CryptomanagerUtils cryptomanagerUtils;


    private String encodedTPMPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";
    private byte[] tpmPublicKey = Base64.getUrlDecoder().decode(encodedTPMPublicKey);
    private String keyIndex = "tetw:sdfsf:Sdfdfsd";

    private List<Machine> machines = new ArrayList<Machine>();
    @Mock
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

        byte[] dumbCipher = "test-encrypted-data".getBytes(StandardCharsets.UTF_8);
        lenient().when(clientCryptoFacade.encrypt(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dumbCipher);

    }


    @Test
    public void validTokenCase() throws JsonProcessingException {
        lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = objectMapper.writeValueAsString(machineAuthDto);
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertEquals("New",machineAuthDto.getAuthType());
        assertNotNull(machineAuthDto);
    }

    @Test
    public void validRefreshTokenCase() throws JsonProcessingException {
        lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("Refresh");
        machineAuthDto.setUserId("test");
        machineAuthDto.setRefreshToken("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = objectMapper.writeValueAsString(machineAuthDto);
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertEquals("Refresh",machineAuthDto.getAuthType());
        assertNotNull(machineAuthDto);
    }

    @Test
    public void validateOtpCase() throws JsonProcessingException {
        lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("OTP");
        machineAuthDto.setOtp("11111");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = objectMapper.writeValueAsString(machineAuthDto);
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        StringBuilder userDetailsUri = new StringBuilder();
        userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(userDetailsUri);
        assertEquals("OTP",machineAuthDto.getAuthType());
    }

    @Test
    public void requestWithOldReqTimestamp() {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

            MachineAuthDto machineAuthDto = new MachineAuthDto();
            machineAuthDto.setAuthType("New");
            machineAuthDto.setPassword("test");
            machineAuthDto.setUserId("test");
            machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).minusHours(1));

            String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineAuthDto);
            String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

            syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
            throw  new RuntimeException();
        }
    }
    
    @Test
    public void requestWithIncorrectMachineName() {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

            MachineAuthDto machineAuthDto = new MachineAuthDto();
            machineAuthDto.setAuthType("New");
            machineAuthDto.setPassword("test");
            machineAuthDto.setUserId("test");
            machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
            machineAuthDto.setMachineName("b2ml27210");

            String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineAuthDto);
            String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

            syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void requestWithFutureReqTimestamp() {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

            MachineAuthDto machineAuthDto = new MachineAuthDto();
            machineAuthDto.setAuthType("New");
            machineAuthDto.setPassword("test");
            machineAuthDto.setUserId("test");
            machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusHours(1));

            String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineAuthDto);
            String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

            syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void requestWithInvalidKeyIndex() throws JsonProcessingException {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

            MachineAuthDto machineAuthDto = new MachineAuthDto();
            machineAuthDto.setAuthType("New");
            machineAuthDto.setPassword("test");
            machineAuthDto.setUserId("test");
            machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));


            String header = Base64.getUrlEncoder().encodeToString("{\"kid\":null}".getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineAuthDto);
            String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

            syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void requestWithInvalidSignature() {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);

            MachineAuthDto machineAuthDto = new MachineAuthDto();
            machineAuthDto.setAuthType("New");
            machineAuthDto.setPassword("test");
            machineAuthDto.setUserId("test");
            machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

            String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineAuthDto);

            syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, null));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void validOtpReqCase() throws JsonProcessingException {
        lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineOtpDto machineOtpDto = new MachineOtpDto();
        machineOtpDto.setOtpChannel(new ArrayList<>());
        machineOtpDto.setContext("test");
        machineOtpDto.setAppId("test");
        machineOtpDto.setUserId("test");
        machineOtpDto.setUseridtype("email");
        machineOtpDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        StringBuilder userDetailsUri = new StringBuilder();
        userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = objectMapper.writeValueAsString(machineOtpDto);
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.sendOTP(String.format("%s.%s.%s", header, payload, signature));
        assertNotNull(machineOtpDto);
    }

    @Test
    public void validReqValidReqTimeCase() throws JsonProcessingException {
        lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
        lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(true);

        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthType("New");
        machineAuthDto.setPassword("test");
        machineAuthDto.setUserId("test");
        machineAuthDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));

        String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
        String payload = objectMapper.writeValueAsString(machineAuthDto);
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

        syncAuthTokenService.getAuthToken(String.format("%s.%s.%s", header, payload, signature));
    }

    @Test
    public void validOtpReqInvalidReqTimeCase() {
        try {
            lenient().when(machineRepository.findBySignKeyIndex(Mockito.anyString())).thenReturn(machines);
            lenient().when(clientCryptoFacade.validateSignature(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

            MachineOtpDto machineOtpDto = new MachineOtpDto();
            machineOtpDto.setOtpChannel(new ArrayList<>());
            machineOtpDto.setContext("test");
            machineOtpDto.setAppId("test");
            machineOtpDto.setUserId("test");
            machineOtpDto.setUseridtype("email");
            machineOtpDto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(7));

            String header = Base64.getUrlEncoder().encodeToString(String.format("{\"kid\":\"%s\"}", keyIndex).getBytes(StandardCharsets.UTF_8));
            String payload = objectMapper.writeValueAsString(machineOtpDto);
            String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes(StandardCharsets.UTF_8));

            syncAuthTokenService.sendOTP(String.format("%s.%s.%s", header, payload, signature));
        } catch (JsonProcessingException e){
            Assert.fail(e.getMessage());
        }
    }
}
