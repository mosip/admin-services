package io.mosip.kernel.syncdata.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils2;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.utils.ExceptionUtils;
import jakarta.annotation.Nullable;
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
/**
 * This class provides advice for modifying the response body in the sync data service.
 * It implements Spring's ResponseBodyAdvice to intercept and modify responses before they are written.
 * The primary purpose is to add metadata like ID, version, response time, and a digital signature to the response wrapper.
 * It also handles setting errors to null if empty and processes the request to extract necessary details.
 *
 * <p>This advice is applied to controllers annotated with @RestController and methods with @ResponseFilter.</p>
 * Advice class to modify the response body for sync data endpoints.
 * This class ensures that responses are wrapped properly, signed, and include necessary metadata.
 * It optimizes performance by skipping unnecessary deserialization and serialization steps where possible.
 *
 * @author [MOSIP Team]
 * @since [1.0]
 */
@RestControllerAdvice
public class SyncResponseBodyAdviceConfig implements ResponseBodyAdvice<Object> {
	/**
	 * Logger instance for logging events in this class.
	 */
	private static final Logger logger = LoggerConfiguration.logConfig(SyncResponseBodyAdviceConfig.class);
	/**
	 * ObjectMapper for JSON serialization and deserialization.
	 */
	@Autowired
	private ObjectMapper objectMapper;
	/**
	 * Helper for key management operations, such as generating signatures.
	 */
	@Autowired
	private KeymanagerHelper keymanagerHelper;
	/**
	 * Determines if this advice supports the given return type and converter.
	 *
	 * @param returnType the return type of the controller method
	 * @param converterType the selected message converter type
	 * @return true if the return type is assignable to ResponseWrapper, false otherwise
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ResponseFilter.class);
	}
	/**
	 * Modifies the response body before it is written to the output.
	 * This method adds ID, version, response time, and a signature to the response.
	 * It also handles errors by setting them to null if empty.
	 * For optimization, it skips deserialization if metadata is already set and returns a JSON string
	 * to avoid double serialization.
	 *
	 * @param body the body to be written
	 * @param returnType the return type of the controller method
	 * @param selectedContentType the content type selected through content negotiation
	 * @param selectedConverterType the converter type selected to write to the response
	 * @param request the current request
	 * @param response the current response
	 * @return the modified body, or the original body if not applicable
	 * @throws SyncDataServiceException if there is an error in serialization or signing
	 */
	@Override
	@Nullable
	public Object beforeBodyWrite(@Nullable Object body,
								  MethodParameter returnType,
								  MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request,
								  ServerHttpResponse response) {
		if (!(body instanceof ResponseWrapper<?> responseWrapper)) {
			return body; // Not a ResponseWrapper, return as is
		}
		RequestWrapper<?> requestWrapper = null;
		String requestBody = null;
		// Only populate id/version if not already set
		if (responseWrapper.getId() == null || responseWrapper.getVersion() == null) {
			try {
				HttpServletRequest httpServletRequest =
						((ServletServerHttpRequest) request).getServletRequest();
				if (httpServletRequest instanceof ContentCachingRequestWrapper cachingRequest) {
					requestBody = new String(cachingRequest.getContentAsByteArray());
				} else if (httpServletRequest instanceof HttpServletRequestWrapper wrapper
						&& wrapper.getRequest() instanceof ContentCachingRequestWrapper cachingRequest) {
					requestBody = new String(cachingRequest.getContentAsByteArray());
				}
				if (!EmptyCheckUtils.isNullEmpty(requestBody)) {
					requestWrapper = objectMapper.readValue(requestBody, RequestWrapper.class);
					responseWrapper.setId(requestWrapper.getId());
					responseWrapper.setVersion(requestWrapper.getVersion());
				}
			} catch (Exception e) {
				logger.error("", "", "", ExceptionUtils.parseException(e));
			}
		}
		// Normalize empty errors to null
		if (responseWrapper.getErrors() != null && responseWrapper.getErrors().isEmpty()) {
			responseWrapper.setErrors(null);
		}
		// Ensure response time is always set
		if (responseWrapper.getResponsetime() == null) {
			String timestamp = DateUtils2.getUTCCurrentDateTimeString();
			responseWrapper.setResponsetime(DateUtils2.convertUTCToLocalDateTime(timestamp));
		}
		try {
			// Serialize once only to compute signature, not for returning
			String json = objectMapper.writeValueAsString(responseWrapper);
			response.getHeaders().add("response-signature", keymanagerHelper.getSignature(json));
		} catch (IOException e) {
			throw new SyncDataServiceException("KER-SIG-ERR", e.getMessage(), e);
		}
		return responseWrapper;
	}
}