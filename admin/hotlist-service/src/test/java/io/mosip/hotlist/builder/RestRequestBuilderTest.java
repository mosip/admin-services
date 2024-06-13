package io.mosip.hotlist.builder;

import io.mosip.hotlist.constant.AuditEvents;
import io.mosip.hotlist.constant.AuditModules;
import io.mosip.hotlist.constant.RestServicesConstants;
import io.mosip.hotlist.dto.AuditRequestDTO;
import io.mosip.hotlist.dto.AuditResponseDTO;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.kernel.core.http.RequestWrapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
public class RestRequestBuilderTest {

	@InjectMocks
	RestRequestBuilder restBuilder;

	@Mock
	ConfigurableEnvironment env;

	@Autowired
	MockMvc mockMvc;

	@InjectMocks
	AuditRequestBuilder auditBuilder;

	@Before
	public void before() {
		ReflectionTestUtils.setField(auditBuilder, "env", env);
		ReflectionTestUtils.setField(restBuilder, "env", env);
	}

	@Test (expected = HotlistAppException.class)
	public void testBuildRequest() throws HotlistAppException {
		RequestWrapper<AuditRequestDTO> auditRequest = auditBuilder.buildRequest(AuditModules.HOTLIST_SERVICE,
				AuditEvents.BLOCK_REQUEST, "id","RID", "desc");
		auditRequest.getRequest().setActionTimeStamp(null);

		RestRequestDTO request = restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditRequest,
				AuditResponseDTO.class);

		RestRequestDTO testRequest = new RestRequestDTO();
		String serviceName = RestServicesConstants.AUDIT_MANAGER_SERVICE.getServiceName();
		String uri = env.getProperty(serviceName.concat(".rest.uri"));
		String httpMethod = env.getProperty(serviceName.concat(".rest.httpMethod"));
		String mediaType = env.getProperty(serviceName.concat(".rest.headers.mediaType"));
		String timeout = env.getProperty(serviceName.concat(".rest.timeout"));

		testRequest.setUri(uri);
		testRequest.setHttpMethod(HttpMethod.valueOf(httpMethod));
		testRequest.setRequestBody(auditRequest);
		testRequest.setResponseType(AuditResponseDTO.class);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(mediaType));
		testRequest.setHeaders(headers);
		testRequest.setTimeout(Integer.parseInt(timeout));

		request.setHeaders(null);
		testRequest.setHeaders(null);
	}

	@Ignore
	@Test(expected = HotlistAppException.class)
	public void testBuildRequestWithMultiValueMap() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.headers.mediaType", "multipart/form-data");
		environment.setProperty("mosip.hotlist.audit.rest.uri.queryparam.test", "yes");
		environment.setProperty("mosip.hotlist.audit.rest.uri.pathparam.test", "yes");

		ReflectionTestUtils.setField(restBuilder, "env", environment);
		RequestWrapper<AuditRequestDTO> auditRequest = auditBuilder.buildRequest(AuditModules.HOTLIST_SERVICE,
				AuditEvents.BLOCK_REQUEST, "id","RID", "desc");
		auditRequest.getRequest().setActionTimeStamp(null);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditRequest,
				AuditResponseDTO.class);

	}

	@Test(expected = Exception.class)
	public void testBuildRequestEmptyUri() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.uri", "");

		ReflectionTestUtils.setField(restBuilder, "env", environment);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder
				.buildRequest(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id","RID", "desc"),
				AuditResponseDTO.class);
	}

	@Test(expected = HotlistAppException.class)
	@DirtiesContext
	public void testBuildRequestNullProperties() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();

		ReflectionTestUtils.setField(restBuilder, "env", environment);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder
				.buildRequest(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id", "RID","desc"),
				AuditResponseDTO.class);
	}

	@Test(expected = Exception.class)
	public void testBuildRequestEmptyHttpMethod() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.httpMethod", "");

		ReflectionTestUtils.setField(restBuilder, "env", environment);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder
				.buildRequest(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id","RID", "desc"),
				AuditResponseDTO.class);
	}

	@Test(expected = HotlistAppException.class)
	public void testBuildRequestEmptyResponseType() throws HotlistAppException {

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder.buildRequest(
				AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id", "RID","desc"), null);
	}
	
	@Test (expected = Exception.class)
	public void testBuildRequestMultiValueMap() throws HotlistAppException {
		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.headers.mediaType", "multipart/form-data");
		environment.setProperty("mosip.hotlist.audit.rest.uri.queryparam.test", "yes");
		environment.setProperty("mosip.hotlist.audit.rest.uri.pathparam.test", "yes");

		ReflectionTestUtils.setField(restBuilder, "env", environment);
		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, new LinkedMultiValueMap<String, String>(),
				Object.class);
	}

	@Test (expected = Exception.class)
	public void testBuildRequestEmptyTimeout() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.timeout", "");

		ReflectionTestUtils.setField(restBuilder, "env", environment);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder
				.buildRequest(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id","RID", "desc"),
				AuditResponseDTO.class);
	}

	@Test (expected = Exception.class)
	public void testBuildRequestHeaders() throws HotlistAppException {

		MockEnvironment environment = new MockEnvironment();
		environment.merge(env);
		environment.setProperty("mosip.hotlist.audit.rest.headers.accept", "application/json");

		ReflectionTestUtils.setField(restBuilder, "env", environment);

		restBuilder.buildRequest(RestServicesConstants.AUDIT_MANAGER_SERVICE, auditBuilder
				.buildRequest(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id","RID", "desc"),
				AuditResponseDTO.class);
	}

}