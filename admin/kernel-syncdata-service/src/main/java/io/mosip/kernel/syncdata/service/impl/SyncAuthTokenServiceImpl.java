package io.mosip.kernel.syncdata.service.impl;


import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Header;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.clientcrypto.exception.ClientCryptoException;
import io.mosip.kernel.clientcrypto.service.impl.ClientCryptoFacade;
import io.mosip.kernel.core.authmanager.model.*;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.constant.SyncAuthErrorCode;
import io.mosip.kernel.syncdata.dto.AuthLoginUser;
import io.mosip.kernel.syncdata.dto.IdSchemaDto;
import io.mosip.kernel.syncdata.dto.MachineAuthDto;
import io.mosip.kernel.syncdata.dto.MachineOtpDto;
import io.mosip.kernel.syncdata.dto.response.TokenResponseDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @since 1.2.0-SNAPSHOT
 */
@RefreshScope
@Service
public class SyncAuthTokenServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SyncAuthTokenServiceImpl.class);

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

    @Autowired
    private ClientCryptoFacade clientCryptoFacade;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private SyncUserDetailsService syncUserDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    private static ObjectMapper objectMapper = new ObjectMapper();

    static  {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getAuthToken(String requestData) {
        String[] parts = requestData.split("\\.");
        if(parts.length == 3) {
            byte[] header = Base64.getUrlDecoder().decode(parts[0]);
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            byte[] signature = Base64.getUrlDecoder().decode(parts[2]);

            Machine machine = validateRequestData(header, payload, signature);
            try {
                MachineAuthDto machineAuthDto = objectMapper.readValue(payload, MachineAuthDto.class);
                validateRequestTimestamp(machineAuthDto.getTimestamp());
                ResponseWrapper<TokenResponseDto> responseWrapper = getTokenResponseDTO(machineAuthDto);
                String token = objectMapper.writeValueAsString(responseWrapper.getResponse());
                byte[] cipher = clientCryptoFacade.encrypt(CryptoUtil.decodeURLSafeBase64(machine.getPublicKey()),
                        token.getBytes());
                return CryptoUtil.encodeToURLSafeBase64(cipher);

            } catch (Exception ex) {
                logger.error("Failed to get auth tokens", ex);
            }
        }
        throw new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage());
    }

    public ResponseWrapper<AuthNResponse> sendOTP(String requestData) {
        String[] parts = requestData.split("\\.");
        if(parts.length == 3) {
            byte[] header = Base64.getUrlDecoder().decode(parts[0]);
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            byte[] signature = Base64.getUrlDecoder().decode(parts[2]);

            validateRequestData(header, payload, signature);
            try {
                MachineOtpDto machineOtpDto = objectMapper.readValue(payload, MachineOtpDto.class);
                validateRequestTimestamp(machineOtpDto.getTimestamp());

                OtpUser otpUser = getOtpUser(machineOtpDto);
                RequestWrapper<OtpUser> requestWrapper = new RequestWrapper<>();
                requestWrapper.setRequest(otpUser);
                requestWrapper.setRequesttime(LocalDateTime.now(ZoneId.of("UTC")));
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sendOTPUrl);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.build().toUri(),
                        new HttpEntity<>(requestWrapper), String.class);

                return objectMapper.readValue(responseEntity.getBody(),
                        new TypeReference<ResponseWrapper<AuthNResponse>>() {});
            } catch (Exception ex) {
                logger.error("Failed to send otp", ex);
            }
        }
        throw new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage());
    }

    private OtpUser getOtpUser(MachineOtpDto machineOtpDto) {
        OtpUser otpUser = new OtpUser();

        UserDetailResponseDto userDetailResponseDto = syncUserDetailsService.getUserDetailsFromAuthServer(Arrays.asList(machineOtpDto.getUserId()));
        if(userDetailResponseDto != null && userDetailResponseDto.getMosipUserDtoList() != null &&
                !userDetailResponseDto.getMosipUserDtoList().isEmpty()) {
           otpUser.setUserId(machineOtpDto.getOtpChannel().contains("email") ?
                           userDetailResponseDto.getMosipUserDtoList().get(0).getMail() :
                           userDetailResponseDto.getMosipUserDtoList().get(0).getMobile());
        }
        else
            otpUser.setUserId(machineOtpDto.getUserId());

        otpUser.setOtpChannel(machineOtpDto.getOtpChannel());
        otpUser.setAppId(machineOtpDto.getAppId());
        otpUser.setUseridtype(machineOtpDto.getUseridtype());
        otpUser.setTemplateVariables(machineOtpDto.getTemplateVariables());
        otpUser.setContext(machineOtpDto.getContext());
        return otpUser;
    }


    private Machine validateRequestData(byte[] header, byte[] payload, byte[] signature) {
        JWTParser jwtParser = new JWTParser();
        Header jwtheader = jwtParser.parseHeader(new String(header));

        if(Objects.nonNull(jwtheader.getKeyId())) {
            List<Machine> machines = machineRepository.findBySignKeyIndex(jwtheader.getKeyId());

            if(Objects.isNull(machines) || machines.isEmpty())
                throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
                        MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

            try {
                logger.info("validateRequestData for machine : {} with status : {}", machines.get(0).getId(), machines.get(0).getIsActive());
                boolean verified = clientCryptoFacade.validateSignature(CryptoUtil.decodeURLSafeBase64(machines.get(0).getSignPublicKey()),
                        signature, payload);
                logger.info("validateRequestData verified : {}", verified);
                if(verified) {  return machines.get(0); }

            } catch(ClientCryptoException ex) {
                logger.error("Failed to validate signature", ex);
            }
        }
        throw new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage());
    }

    private void validateRequestTimestamp(LocalDateTime requestTimestamp) {
        Objects.requireNonNull(requestTimestamp);
        long value = requestTimestamp.until(LocalDateTime.now(ZoneOffset.UTC), ChronoUnit.MINUTES);
        if(value <= minMinutes && value >= maxMinutes)  { return; }

        logger.error("Request timestamp validation failed : {}", requestTimestamp);
        throw new RequestException(SyncAuthErrorCode.INVALID_REQUEST_TIME.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST_TIME.getErrorMessage());
    }

    private ResponseWrapper<TokenResponseDto> getTokenResponseDTO(MachineAuthDto machineAuthDto) throws IOException {
        ResponseEntity<String> responseEntity = null;
        switch (machineAuthDto.getAuthType().toUpperCase()) {
            case "NEW" :
                LoginUserWithClientId authLoginUser = new LoginUserWithClientId();
                authLoginUser.setUserName(machineAuthDto.getUserId());
                authLoginUser.setPassword(machineAuthDto.getPassword());
                authLoginUser.setClientId(clientId);
                authLoginUser.setClientSecret(secretKey);
                authLoginUser.setAppId(authTokenInternalAppId);
                RequestWrapper<LoginUserWithClientId> requestWrapper = new RequestWrapper();
                requestWrapper.setRequest(authLoginUser);
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(newAuthTokenInternalUrl);
                responseEntity = restTemplate.postForEntity(builder.build().toUri(),
                        new HttpEntity<>(requestWrapper), String.class);
                break;

            case "OTP" :
                UserOtp userOtp = new UserOtp();
                userOtp.setUserId(machineAuthDto.getUserId());
                userOtp.setOtp(machineAuthDto.getOtp());
                userOtp.setAppId(authTokenInternalAppId);
                RequestWrapper<UserOtp> otpRequestWrapper = new RequestWrapper();
                otpRequestWrapper.setRequest(userOtp);
                UriComponentsBuilder otpRequestBuilder = UriComponentsBuilder.fromUriString(otpAuthTokenInternalUrl);
                responseEntity = restTemplate.postForEntity(otpRequestBuilder.build().toUri(),
                        new HttpEntity<>(otpRequestWrapper), String.class);
                break;

            case "REFRESH" :
                RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
                refreshTokenRequest.setClientID(clientId);
                refreshTokenRequest.setClientSecret(secretKey);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Cookie", String.format("refresh_token=%s", machineAuthDto.getRefreshToken()));
                HttpEntity<RefreshTokenRequest> httpEntity = new HttpEntity<>(refreshTokenRequest, httpHeaders);
                UriComponentsBuilder refreshRequestBuilder = UriComponentsBuilder.fromUriString(refreshAuthTokenInternalUrl);
                responseEntity = restTemplate.postForEntity(refreshRequestBuilder.build().toUri(), httpEntity, String.class);
                break;
        }

        ResponseWrapper<TokenResponseDto> responseWrapper = objectMapper.readValue(responseEntity.getBody(),
                new TypeReference<ResponseWrapper<TokenResponseDto>>() {});

        if(Objects.nonNull(responseWrapper.getErrors()) && !responseWrapper.getErrors().isEmpty())
            throw new RequestException(responseWrapper.getErrors().get(0).getErrorCode(),
                    responseWrapper.getErrors().get(0).getMessage());

        Objects.nonNull(responseWrapper.getResponse());
        responseWrapper.getResponse().setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        return responseWrapper;
    }
}
