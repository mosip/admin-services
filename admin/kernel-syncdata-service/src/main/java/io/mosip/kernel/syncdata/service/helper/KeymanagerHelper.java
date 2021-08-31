package io.mosip.kernel.syncdata.service.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.signature.dto.JWTSignatureRequestDto;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.syncdata.constant.AdminServiceErrorCode;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.dto.response.KeyPairGenerateResponseDto;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class KeymanagerHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySchemaHelper.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mosip.kernel.keymanager.cert.url}")
    private String certificateUrl;

    @Value("${mosip.kernel.keymanager-service-sign-url}")
    private String signUrl;

    @Value("${mosip.sign.applicationid:KERNEL}")
    private String signApplicationid;

    @Value("${mosip.sign.refid:SIGN}")
    private String signRefid;

    public KeyPairGenerateResponseDto getCertificate(String applicationId, Optional<String> referenceId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(certificateUrl);
            builder.queryParam("applicationId", applicationId);
            if (referenceId.isPresent())
                builder.queryParam("referenceId", referenceId.get());
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

            objectMapper.registerModule(new JavaTimeModule());
            ResponseWrapper<KeyPairGenerateResponseDto> resp = objectMapper.readValue(responseEntity.getBody(),
                    new TypeReference<ResponseWrapper<KeyPairGenerateResponseDto>>() {});

            if(resp.getErrors() != null && !resp.getErrors().isEmpty())
                throw new SyncInvalidArgumentException(resp.getErrors());

            return resp.getResponse();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch Certificate from keymanager", e);
            throw new SyncDataServiceException(AdminServiceErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
                    AdminServiceErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage() + " : " +
                            ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
        }
    }

    public String getSignature(String responseBody) throws IOException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(signUrl);
            RequestWrapper<JWTSignatureRequestDto> requestWrapper = new RequestWrapper<>();
            JWTSignatureRequestDto jwtSignatureRequestDto = new JWTSignatureRequestDto();
            jwtSignatureRequestDto.setApplicationId(signApplicationid);
            jwtSignatureRequestDto.setReferenceId(signRefid);
            jwtSignatureRequestDto.setDataToSign(CryptoUtil.encodeBase64(responseBody.getBytes(StandardCharsets.UTF_8)));
            requestWrapper.setRequest(jwtSignatureRequestDto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RequestWrapper<?>> requestEntity = new HttpEntity<>(requestWrapper, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.build().toUri(),
                    requestEntity,String.class);

            objectMapper.registerModule(new JavaTimeModule());
            ResponseWrapper<JWTSignatureResponseDto> resp = objectMapper.readValue(responseEntity.getBody(),
                    new TypeReference<ResponseWrapper<JWTSignatureResponseDto>>() {});

            if(resp.getErrors() != null && !resp.getErrors().isEmpty()) {
                throw new SyncInvalidArgumentException(resp.getErrors());
            }

            return resp.getResponse().getJwtSignedData();
        } catch (Exception e) {
            LOGGER.error("Failed to sign the file content", e);
            throw e;
        }
    }
}
