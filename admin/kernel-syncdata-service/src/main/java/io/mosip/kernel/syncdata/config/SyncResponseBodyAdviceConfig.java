package io.mosip.kernel.syncdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@RestControllerAdvice
public class SyncResponseBodyAdviceConfig implements ResponseBodyAdvice<ResponseWrapper<?>> {

	private static final Logger logger = LoggerConfiguration.logConfig(SyncResponseBodyAdviceConfig.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KeymanagerHelper keymanagerHelper;

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
			String timestamp = DateUtils.getUTCCurrentDateTimeString();
			body.setResponsetime(DateUtils.convertUTCToLocalDateTime(timestamp));
			try {
				response.getHeaders().add("response-signature", keymanagerHelper.getSignature(objectMapper.writeValueAsString(body)));
			} catch (IOException e) {
				throw new SyncDataServiceException("KER-SIG-ERR", e.getMessage(), e);
			}
		}

		return body;
	}


}