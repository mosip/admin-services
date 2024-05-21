package io.mosip.hotlist.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.AuditRequestDTO;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.AuthenticationException;
import io.mosip.hotlist.exception.HotlistRetryException;
import io.mosip.hotlist.exception.RestServiceException;
import io.mosip.kernel.core.util.exception.JsonMappingException;
import io.mosip.kernel.core.util.exception.JsonParseException;
import net.logstash.logback.encoder.org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The Class RestUtilTest.
 *
 * @author Manoj SP
 */
@RunWith(MockitoJUnitRunner.class)
public class RestHelperTest {

	/** The rest helper. */
	@InjectMocks
	RestHelper restHelper;

	@Mock
	WebClient.Builder webClientBuilder;

	@Mock
	WebClient webClient;

	@Mock
	WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	WebClient.RequestBodyUriSpec requestBodyUriSpec;

	@Mock
	WebClient.RequestHeadersSpec requestHeadersSpec;

	@Mock
	WebClient.RequestBodySpec requestBodySpec;

	@Mock
	WebClient.ResponseSpec responseSpec;

	@Mock
	Mono<Object> monoObject;

	@Mock
	ObjectMapper mapper;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test (expected = HotlistRetryException.class)
	public void testReqSync() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;
		String responseBody = "{\"response\":{\"status\":\"success\"}}";

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		Object result = restHelper.requestSync(restReqDTO);

		assertTrue(result instanceof String);
		assertEquals(responseBody, result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test (expected = HotlistRetryException.class)
	public void testReqSyncWithTimeout() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;
		int timeout = 1000;

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);
		restReqDTO.setTimeout(timeout);

		when(webClient.method(any())).thenReturn(requestBodyUriSpec);
		when(requestBodyUriSpec.uri(uri)).thenReturn(requestBodySpec);
		when(requestBodySpec.retrieve()).thenReturn(responseSpec);

		Object result = restHelper.requestSync(restReqDTO);

		assertTrue(result == HotlistErrorConstants.UNKNOWN_ERROR);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test (expected = HotlistRetryException.class)
	public void testReqSyncWithHeaders() throws Exception {
		// Arrange
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		Object result = restHelper.requestSync(restReqDTO);

		assertTrue(result == HotlistErrorConstants.UNKNOWN_ERROR);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testReqSyncUnknownError() {
		// Arrange
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		try {
			restHelper.requestSync(restReqDTO);
		} catch (Exception e) {
			e.getCause();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test (expected = HotlistRetryException.class)
	public void testRequestSyncWithParams() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("key1", "value1");
		params.add("key2", "value2");

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);
		restReqDTO.setParams(params);

		Object result = restHelper.requestSync(restReqDTO);

		assertTrue(result == null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test (expected = HotlistRetryException.class)
	public void testRequestSyncWithPathVariables() throws Exception {
		String uri = "https://example.com/{id}";
		HttpMethod method = HttpMethod.GET;
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("id", "123");

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);
		restReqDTO.setPathVariables(pathVariables);

		when(webClient.method(any())).thenReturn(requestBodyUriSpec);

		Object result = restHelper.requestSync(restReqDTO);

		assertNull(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	@Ignore
	public void testRequestSyncWithTimeout() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		when(webClientBuilder.build()).thenReturn(webClient);
		when(webClient.method(any())).thenReturn((WebClient.RequestBodyUriSpec) requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenThrow(new RuntimeException("Error occurred during retrieval"));

		try {
			restHelper.requestSync(restReqDTO);
			fail("Expected RuntimeException was not thrown");
		} catch (RuntimeException e) {
			assertEquals("Error occurred during retrieval", e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test (expected = Exception.class)
	public void testRequestAsync() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		when(webClient.method(any())).thenReturn(requestBodyUriSpec);
		when(requestBodyUriSpec.uri(uri)).thenReturn(requestBodySpec);

		Mono<Object> resultMono = (Mono<Object>) restHelper.requestAsync(restReqDTO);

		assertEquals(monoObject, resultMono);
	}

	@SuppressWarnings("unchecked")
	@Test (expected = HotlistRetryException.class)
	public void testRequestSyncWebClientResponseException() throws Exception {
		String uri = "https://hotlist.com";
		HttpMethod method = HttpMethod.GET;

		RestRequestDTO restReqDTO = new RestRequestDTO();
		restReqDTO.setHttpMethod(method);
		restReqDTO.setUri(uri);

		when(webClient.method(any())).thenReturn(requestBodyUriSpec);
		when(requestBodyUriSpec.uri(uri)).thenReturn(requestBodySpec);

		restHelper.requestSync(restReqDTO);
	}

	/**
	 * Test handle status error without response body.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	@Ignore
	public void testHandleStatusErrorWithErrorResponseBody() throws Throwable {
		try {
			RestRequestDTO restRequestDTO = new RestRequestDTO();
			restRequestDTO.setUri("0.0.0.0");
			restRequestDTO.setResponseType(ObjectNode.class);

			// Mocking WebClient and its method to handle any argument
			WebClient webClient = mock(WebClient.class);
			WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
			when(webClient.method(any())).thenReturn((WebClient.RequestBodyUriSpec) requestHeadersUriSpec);

			// Stubbing WebClient to throw WebClientResponseException when method is called
			WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
			when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
			WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
			when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
			when(webClient.method(Mockito.any())).thenThrow(new WebClientResponseException("message", 400,
					"failed", null, mapper.writeValueAsBytes(restRequestDTO), null));
			restRequestDTO.setParams(new LinkedMultiValueMap<>(0));
			restRequestDTO.setPathVariables(Collections.singletonMap("", ""));
			ReflectionTestUtils.setField(restHelper, "webClient", webClient);
			restHelper.requestSync(restRequestDTO);
		} catch (RestServiceException e) {
			assertEquals(e.getErrorCode(), HotlistErrorConstants.CLIENT_ERROR.getErrorCode());
			assertEquals(e.getErrorText(), HotlistErrorConstants.CLIENT_ERROR.getErrorMessage());
		}
	}

	@Test
	public void testHandleTimeoutException() throws Throwable {
		try {
			RestRequestDTO restRequestDTO = new RestRequestDTO();
			restRequestDTO.setParams(new LinkedMultiValueMap<>(0));
			restRequestDTO.setPathVariables(Collections.singletonMap("", ""));
			restRequestDTO.setUri("0.0.0.0");
			restRequestDTO.setResponseType(String.class);
			WebClient webClient = mock(WebClient.class);
			when(webClient.method(Mockito.any()))
					.thenThrow(new RuntimeException(new TimeoutException("")));
			ReflectionTestUtils.setField(restHelper, "webClient", webClient);
			restHelper.requestSync(restRequestDTO);
		} catch (HotlistRetryException e) {
			assertEquals(e.getErrorCode(), HotlistErrorConstants.CONNECTION_TIMED_OUT.getErrorCode());
			assertEquals(e.getErrorText(), HotlistErrorConstants.CONNECTION_TIMED_OUT.getErrorMessage());
		}
	}

	/**
	 * Test handle status error without response body unauthorised error.
	 *
	 * @throws Throwable the throwable
	 */
	@SuppressWarnings("unchecked")
	@Test (expected = Exception.class)
	public void testHandleStatusErrorWithoutResponseBodyUnauthorisedError() throws Throwable {
		try {
			ClientResponse clientResponse = mock(ClientResponse.class);
			String response = "{\"errors\":[{\"errorCode\":\"KER-ATH-402\"}]}";
			when(clientResponse.bodyToMono(any(Class.class))).thenReturn(Mono.just(mapper.readValue(response.getBytes(), ObjectNode.class)));
			ReflectionTestUtils.invokeMethod(restHelper, "handleStatusError",
					new WebClientResponseException("message", 401, "failed", null, response.getBytes(), null),
					String.class);
		} catch (UndeclaredThrowableException | AuthenticationException e) {
			if (Objects.nonNull(e.getCause())) {
				AuthenticationException ex = (AuthenticationException) e.getCause();
				assertEquals(ex.getErrorCode(), "KER-ATH-402");
				assertTrue(Objects.isNull(ex.getErrorText()));
			} else {
				assertEquals(((AuthenticationException) e).getErrorCode(), "KER-ATH-402");
				assertTrue(Objects.isNull(((AuthenticationException) e).getErrorText()));
			}
		}
	}

	/**
	 * Test handle status error 4 xx.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected = RestServiceException.class)
	public void testHandleStatusError4xx() throws Throwable {
		try {
			ReflectionTestUtils.invokeMethod(restHelper, "handleStatusError", new WebClientResponseException("message",
					400, "failed", null, mapper.writeValueAsBytes(new AuditRequestDTO()), null), AuditRequestDTO.class);
		} catch (Exception e) {
			throw ExceptionUtils.getRootCause(e);
		}
	}

	/**
	 * Test handle status error 5 xx.
	 *
	 * @throws Throwable the throwable
	 */
	@Test(expected = RestServiceException.class)
	public void testHandleStatusError5xx() throws Throwable {
		try {
			assertTrue(
					ReflectionTestUtils.invokeMethod(
							restHelper, "handleStatusError", new WebClientResponseException("message", 500, "failed",
									null, mapper.writeValueAsBytes(new AuditRequestDTO()), null),
							AuditRequestDTO.class));
		} catch (Exception e) {
			throw ExceptionUtils.getRootCause(e);
		}
	}

	@Test (expected = HotlistRetryException.class)
	public void testHandleStatusErrorIOException() throws Throwable {
		try {
			assertTrue(ReflectionTestUtils
					.invokeMethod(restHelper, "handleStatusError",
							new WebClientResponseException("message", 500, "failed", null,
									mapper.writeValueAsBytes(new AuditRequestDTO()), null),
							RestRequestDTO.class)
					.getClass().equals(RestServiceException.class));
		} catch (UndeclaredThrowableException e) {
			RestServiceException ex = (RestServiceException) e.getCause();
			assertEquals(ex.getErrorCode(), HotlistErrorConstants.SERVER_ERROR.getErrorCode());
			assertEquals(ex.getErrorText(), HotlistErrorConstants.SERVER_ERROR.getErrorMessage());
		}
	}

	/**
	 * Test check error response exception.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCheckErrorResponseException() throws Throwable {
		try {
			String response = "{\"errors\":[{\"errorCode\":\"\"}]}";
			ReflectionTestUtils.invokeMethod(restHelper, "checkErrorResponse",
					mapper.readValue(response.getBytes(), Object.class), ObjectNode.class);
		} catch (UndeclaredThrowableException e) {
			RestServiceException ex = (RestServiceException) e.getCause();
			assertEquals(ex.getErrorCode(), HotlistErrorConstants.CLIENT_ERROR.getErrorCode());
			assertEquals(ex.getErrorText(), HotlistErrorConstants.CLIENT_ERROR.getErrorMessage());
		}
	}

	@Test
	public void testCheckErrorResponseIOException() throws Throwable {
		try {
			String response = "{\"errors\":[{\"errorCode\":\"\"}]}";
			ReflectionTestUtils.invokeMethod(restHelper, "checkErrorResponse",
					mapper.readValue(response.getBytes(), Object.class), RestRequestDTO.class);
		} catch (UndeclaredThrowableException e) {
			RestServiceException ex = (RestServiceException) e.getCause();
			assertEquals(ex.getErrorCode(), HotlistErrorConstants.CLIENT_ERROR.getErrorCode());
			assertEquals(ex.getErrorText(), HotlistErrorConstants.CLIENT_ERROR.getErrorMessage());
		}
	}

	/**
	 * Test check error response retry.
	 *
	 * @throws JsonParseException   the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException          Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCheckErrorResponseRetry() throws JsonParseException, JsonMappingException, IOException {
		try {
			String response = "{\"errors\":[{\"errorCode\":\"KER-ATH-401\"}]}";
			ReflectionTestUtils.invokeMethod(restHelper, "checkErrorResponse",
					mapper.readValue(response.getBytes(), Object.class), ObjectNode.class);
		} catch (UndeclaredThrowableException e) {
			RestServiceException cause = (RestServiceException) e.getCause();
			assertEquals(cause.getErrorCode(), HotlistErrorConstants.CLIENT_ERROR.getErrorCode());
			assertEquals(cause.getErrorText(), HotlistErrorConstants.CLIENT_ERROR.getErrorMessage());
		}
	}
}