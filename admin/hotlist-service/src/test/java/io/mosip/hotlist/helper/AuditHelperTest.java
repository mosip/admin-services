package io.mosip.hotlist.helper;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.hotlist.builder.AuditRequestBuilder;
import io.mosip.hotlist.builder.RestRequestBuilder;
import io.mosip.hotlist.constant.AuditEvents;
import io.mosip.hotlist.constant.AuditModules;
import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.exception.HotlistAppException;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(SpringRunner.class)
@WebMvcTest
public class AuditHelperTest {

	@Mock
	RestHelper restHelper;

	@InjectMocks
	AuditHelper auditHelper;

	@Autowired
	MockMvc mockMvc;

	@Mock
	AuditRequestBuilder auditBuilder;

	@Mock
	RestRequestBuilder restBuilder;

	@Autowired
	Environment env;

	@Before
	public void before() {
		ReflectionTestUtils.setField(auditBuilder, "env", env);
		ReflectionTestUtils.setField(restBuilder, "env", env);
	}

	@Test
	public void testAudit() {
		auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id", "rid", "desc");
	}

	@Test
	public void testAuditFailure() throws HotlistAppException {
		when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new HotlistAppException(HotlistErrorConstants.AUTHORIZATION_FAILED));
		auditHelper.audit(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id", "RID", "desc");
	}

	@Test
	public void testAuditError() {
		auditHelper.auditError(AuditModules.HOTLIST_SERVICE, AuditEvents.BLOCK_REQUEST, "id", "RID",
				new HotlistAppException(HotlistErrorConstants.AUTHORIZATION_FAILED));
	}

}
