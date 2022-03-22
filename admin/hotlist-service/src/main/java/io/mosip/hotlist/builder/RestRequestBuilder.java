package io.mosip.hotlist.builder;

import static io.mosip.hotlist.constant.HotlistErrorConstants.INVALID_INPUT_PARAMETER;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import io.mosip.hotlist.constant.RestServicesConstants;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.StringUtils;
import lombok.NoArgsConstructor;

/**
 * A builder for creating and building RestRequest objects from properties.
 *
 * @author Manoj SP
 */
@Component

/**
 * Instantiates a new rest request builder.
 */
@NoArgsConstructor
public class RestRequestBuilder {

	/** The Constant REST_TIMEOUT. */
	private static final String REST_TIMEOUT = ".rest.timeout";

	/** The Constant REST_HTTP_METHOD. */
	private static final String REST_HTTP_METHOD = ".rest.httpMethod";

	/** The Constant REST_URI. */
	private static final String REST_URI = ".rest.uri";

	/** The Constant REST_HEADERS_MEDIA_TYPE. */
	private static final String REST_HEADERS_MEDIA_TYPE = ".rest.headers.mediaType";

	/** The Constant METHOD_BUILD_REQUEST. */
	private static final String METHOD_BUILD_REQUEST = "buildRequest";

	/** The env. */
	@Autowired
	private Environment env;

	/** The logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(RestRequestBuilder.class);

	/**
	 * Builds the rest request based on the rest service provided using
	 * {@code RestServicesConstants}.
	 *
	 * @param restService the rest service
	 * @param requestBody the request body
	 * @param returnType  the return type
	 * @return the rest request DTO
	 * @throws HotlistAppException the ID data validation exception
	 */
	public RestRequestDTO buildRequest(RestServicesConstants restService, Object requestBody, Class<?> returnType)
			throws HotlistAppException {
		RestRequestDTO request = new RestRequestDTO();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		Map<String, String> pathVariables = new HashMap<>();

		String serviceName = restService.getServiceName();

		String uri = env.getProperty(serviceName.concat(REST_URI));
		String httpMethod = env.getProperty(serviceName.concat(REST_HTTP_METHOD));
		String timeout = env.getProperty(serviceName.concat(REST_TIMEOUT));

		HttpHeaders headers = constructHttpHeaders(serviceName);

		checkUri(request, uri);

		checkHttpMethod(request, httpMethod);
		MediaType mt=headers.getContentType();
		if (requestBody != null && null!=headers && null!= mt) {
			if (!mt.includes(MediaType.MULTIPART_FORM_DATA)) {
				request.setRequestBody(requestBody);
			} else {
				if (requestBody instanceof MultiValueMap) {
					request.setRequestBody(requestBody);
				} else {
					throw new HotlistAppException(INVALID_INPUT_PARAMETER.getErrorCode(),
							String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "requestBody"));
				}
			}
		}

		checkReturnType(returnType, request);

		constructParams(paramMap, pathVariables, headers, serviceName);

		request.setHeaders(headers);

		if (!paramMap.isEmpty()) {
			request.setParams(paramMap);
		}

		if (!pathVariables.isEmpty()) {
			request.setPathVariables(pathVariables);
		}

		if (!StringUtils.isEmpty(timeout)) {
			request.setTimeout(Integer.parseInt(timeout));
		}

		return request;
	}

	/**
	 * Construct http headers.
	 *
	 * @param serviceName the service name
	 * @return the http headers
	 * @throws HotlistAppException the id repo data validation exception
	 */
	private HttpHeaders constructHttpHeaders(String serviceName) throws HotlistAppException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.valueOf(env.getProperty(serviceName.concat(REST_HEADERS_MEDIA_TYPE))));
			return headers;
		} catch (InvalidMediaTypeException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), METHOD_BUILD_REQUEST, "returnType",
					"throwing HotlistAppException - INVALID_INPUT_PARAMETER"
							+ env.getProperty(serviceName.concat(REST_HEADERS_MEDIA_TYPE)));
			throw new HotlistAppException(INVALID_INPUT_PARAMETER.getErrorCode(), String
					.format(INVALID_INPUT_PARAMETER.getErrorMessage(), serviceName.concat(REST_HEADERS_MEDIA_TYPE)));
		}
	}

	/**
	 * Construct uri params and path variables from properties.
	 *
	 * @param paramMap      the param map
	 * @param pathVariables the path variables
	 * @param headers       the headers
	 * @param serviceName   the service name
	 */
	private void constructParams(MultiValueMap<String, String> paramMap, Map<String, String> pathVariables,
			HttpHeaders headers, String serviceName) {
		((AbstractEnvironment) env).getPropertySources().forEach((PropertySource<?> source) -> {
			if (source instanceof MapPropertySource) {
				Map<String, Object> systemProperties = ((MapPropertySource) source).getSource();

				systemProperties.keySet().forEach((String property) -> {
					if (property.startsWith(serviceName.concat(".rest.headers"))) {
						headers.add(property.replace(serviceName.concat(".rest.headers."), ""),
								env.getProperty(property));
					}
					if (property.startsWith(serviceName.concat(".rest.uri.queryparam."))) {
						paramMap.put(property.replace(serviceName.concat(".rest.uri.queryparam."), ""),
								Collections.singletonList(env.getProperty(property)));
					}
					if (property.startsWith(serviceName.concat(".rest.uri.pathparam."))) {
						pathVariables.put(property.replace(serviceName.concat(".rest.uri.pathparam."), ""),
								env.getProperty(property));
					}
				});
			}
		});
	}

	/**
	 * Check return type is null or not. If null, exception is thrown.
	 *
	 * @param returnType the return type
	 * @param request    the request
	 * @throws HotlistAppException the ID data validation exception
	 */
	private void checkReturnType(Class<?> returnType, RestRequestDTO request) throws HotlistAppException {
		if (returnType != null) {
			request.setResponseType(returnType);
		} else {
			mosipLogger.error(HotlistSecurityManager.getUser(), METHOD_BUILD_REQUEST, "returnType",
					"throwing IDDataValidationException - INVALID_RETURN_TYPE");
			throw new HotlistAppException(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "returnType"));
		}
	}

	/**
	 * Check http method is null or empty. If so, exception is thrown.
	 *
	 * @param request    the request
	 * @param httpMethod the http method
	 * @throws HotlistAppException the ID data validation exception
	 */
	private void checkHttpMethod(RestRequestDTO request, String httpMethod) throws HotlistAppException {
		if (!StringUtils.isEmpty(httpMethod)) {
			request.setHttpMethod(HttpMethod.valueOf(httpMethod));
		} else {
//			mosipLogger.error(HotlistSecurityManager.getUser(), METHOD_BUILD_REQUEST, "httpMethod",
//					"throwing IDDataValidationException - INVALID_HTTP_METHOD" + httpMethod);
			throw new HotlistAppException(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "httpMethod"));
		}
	}

	/**
	 * Check uri is null or empty. If so, exception is thrown.
	 *
	 * @param request the request
	 * @param uri     the uri
	 * @throws HotlistAppException the ID data validation exception
	 */
	private void checkUri(RestRequestDTO request, String uri) throws HotlistAppException {
		if (!StringUtils.isEmpty(uri)) {
			request.setUri(uri);
		} else {
//			mosipLogger.error(HotlistSecurityManager.getUser(), METHOD_BUILD_REQUEST, "uri",
//					"throwing IDDataValidationException - uri is empty or whitespace" + uri);
			throw new HotlistAppException(INVALID_INPUT_PARAMETER.getErrorCode(),
					String.format(INVALID_INPUT_PARAMETER.getErrorMessage(), "uri"));
		}
	}

}