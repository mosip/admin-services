package io.mosip.hotlist.builder;

import io.mosip.hotlist.constant.AuditEvents;
import io.mosip.hotlist.constant.AuditModules;
import io.mosip.hotlist.dto.AuditRequestDTO;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
public class AuditRequestBuilderTest {

	@InjectMocks
	AuditRequestBuilder auditBuilder;
	
	@Mock
	Environment env;

	@Before
	public void before() {
		ReflectionTestUtils.setField(auditBuilder, "env", env);
		ReflectionTestUtils.setField(auditBuilder, "appId", "HOTLIST");
	}

	@Test
	public void testBuildRequest() {
		RequestWrapper<AuditRequestDTO> actualRequest = auditBuilder.buildRequest(AuditModules.HOTLIST_SERVICE,
				AuditEvents.BLOCK_REQUEST, "id", "RID", "desc");
		actualRequest.getRequest().setActionTimeStamp(DateUtils.getUTCCurrentDateTime());
		AuditRequestDTO expectedRequest = new AuditRequestDTO();
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			expectedRequest.setEventId(AuditEvents.BLOCK_REQUEST.getEventId());
			expectedRequest.setEventName(AuditEvents.BLOCK_REQUEST.getEventName());
			expectedRequest.setEventType(AuditEvents.BLOCK_REQUEST.getEventType());
			expectedRequest.setActionTimeStamp(null);
			expectedRequest.setHostName(inetAddress.getHostName());
			expectedRequest.setHostIp(inetAddress.getHostAddress());
			expectedRequest.setApplicationId("HOTLIST");
			expectedRequest.setApplicationName("HOTLIST");
			expectedRequest.setSessionUserId("sessionUserId");
			expectedRequest.setSessionUserName("sessionUserName");
			expectedRequest.setId("id");
			expectedRequest.setIdType("RID");
			expectedRequest.setCreatedBy(env.getProperty("user.name"));
			expectedRequest.setModuleName(AuditModules.HOTLIST_SERVICE.getModuleName());
			expectedRequest.setModuleId(AuditModules.HOTLIST_SERVICE.getModuleId());
			expectedRequest.setDescription("desc");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		assertEquals(expectedRequest, actualRequest.getRequest());
	}

}