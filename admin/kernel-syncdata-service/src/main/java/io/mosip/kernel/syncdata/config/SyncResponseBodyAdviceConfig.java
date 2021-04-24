package io.mosip.kernel.syncdata.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.signature.dto.JWTSignatureRequestDto;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.exception.SyncServiceException;
import io.mosip.kernel.syncdata.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.signatureutil.exception.ParseResponseException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.cryptosignature.constant.SigningDataErrorCode;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class SyncResponseBodyAdviceConfig implements ResponseBodyAdvice<ResponseWrapper<?>> {

	private static final Logger logger = LoggerConfiguration.logConfig(SyncResponseBodyAdviceConfig.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${mosip.kernel.keymanager-service-sign-url}")
	private String signUrl;

	@Value("${mosip.sign.applicationid:KERNEL}")
	private String signApplicationid;

	@Value("${mosip.sign.refid:SIGN}")
	private String signRefid;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ResponseFilter.class);
	}

	@Override
	public ResponseWrapper<?> beforeBodyWrite(ResponseWrapper<?> body, MethodParameter returnType,
											  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
											  ServerHttpRequest request, ServerHttpResponse response) {

		RequestWrapper<?> requestWrapper = null;
		String requestBody = null;

		try {
			HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();

			if (httpServletRequest instanceof ContentCachingRequestWrapper) {
				requestBody = new String(((ContentCachingRequestWrapper) httpServletRequest).getContentAsByteArray());
			} else if (httpServletRequest instanceof HttpServletRequestWrapper
					&& ((HttpServletRequestWrapper) httpServletRequest)
					.getRequest() instanceof ContentCachingRequestWrapper) {
				requestBody = new String(
						((ContentCachingRequestWrapper) ((HttpServletRequestWrapper) httpServletRequest).getRequest())
								.getContentAsByteArray());
			}

			objectMapper.registerModule(new JavaTimeModule());
			if (!EmptyCheckUtils.isNullEmpty(requestBody)) {
				requestWrapper = objectMapper.readValue(requestBody, RequestWrapper.class);
				body.setId(requestWrapper.getId());
				body.setVersion(requestWrapper.getVersion());
			}
			body.setErrors(null);

		} catch (Exception e) {
			logger.error("", "", "", ExceptionUtils.parseException(e));
		}
		if (body != null) {
			try {
				String timestamp = DateUtils.getUTCCurrentDateTimeString();
				body.setResponsetime(DateUtils.convertUTCToLocalDateTime(timestamp));
				response.getHeaders().add("response-signature", getResponseSignature(objectMapper.writeValueAsString(body)));
			} catch (JsonProcessingException e) {
				throw new ParseResponseException(SigningDataErrorCode.RESPONSE_PARSE_EXCEPTION.getErrorCode(),
						SigningDataErrorCode.RESPONSE_PARSE_EXCEPTION.getErrorCode());
			} catch (IOException e) {
				throw new SyncDataServiceException(SigningDataErrorCode.REST_CRYPTO_CLIENT_EXCEPTION.getErrorCode(),
						SigningDataErrorCode.REST_CRYPTO_CLIENT_EXCEPTION.getErrorCode());
			}
		}

		return body;
	}


	private String getResponseSignature(String responseBody) throws IOException {
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
			logger.error("", "", "", ExceptionUtils.parseException(e));
			throw e;
		}
	}

}