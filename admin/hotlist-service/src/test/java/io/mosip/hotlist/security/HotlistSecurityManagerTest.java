package io.mosip.hotlist.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.hotlist.builder.RestRequestBuilder;
import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.dto.RestRequestDTO;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.exception.RestServiceException;
import io.mosip.hotlist.helper.RestHelper;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
public class HotlistSecurityManagerTest {

	@Mock
	private RestRequestBuilder restBuilder;

	/** The rest helper. */
	@Mock
	private RestHelper restHelper;

	@InjectMocks
	private ObjectMapper mapper;

	@InjectMocks
	private HotlistSecurityManager securityManager;

	@Test
	public void testHash() {
		assertEquals("88D4266FD4E6338D13B845FCF289579D209C897823B9217DA3E161936F031589",
				HotlistSecurityManager.hash("abcd".getBytes()));
	}

	@Test
	public void testEncrypt() throws IOException, HotlistAppException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		ResponseWrapper<ObjectNode> response = new ResponseWrapper<>();
		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.put("data", "data");
		response.setResponse(responseNode);
		when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any(Class.class)))
				.thenReturn(new RestRequestDTO());
		when(restHelper.requestSync(Mockito.any()))
				.thenReturn(mapper.readValue(mapper.writeValueAsString(response), ResponseWrapper.class));
		assertEquals("data", new String(securityManager.encrypt("")));
	}

	@Test
	public void testDecrypt()
			throws IOException, HotlistAppException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		ResponseWrapper<ObjectNode> response = new ResponseWrapper<>();
		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.put("data", "ZGF0YQ==");
		response.setResponse(responseNode);
		when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any(Class.class)))
				.thenReturn(new RestRequestDTO());
		when(restHelper.requestSync(Mockito.any()))
				.thenReturn(mapper.readValue(mapper.writeValueAsString(response), ResponseWrapper.class));
		assertEquals("data", new String(securityManager.decrypt("")));
	}

	@Test
	public void testEncryptError() {
		try {
			ResponseWrapper<ObjectNode> response = new ResponseWrapper<>();
			ObjectNode responseNode = mapper.createObjectNode();
			responseNode.put("data", "data");
			response.setResponse(responseNode);
			when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any(Class.class)))
					.thenReturn(new RestRequestDTO());
			when(restHelper.requestSync(Mockito.any()))
					.thenThrow(new RestServiceException(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED));
			assertEquals("data", new String(securityManager.encrypt("")));
		} catch (HotlistAppException e) {
			assertEquals(e.getErrorCode(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode());
			assertEquals(e.getErrorText(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage());
		}
	}

	@Test
	public void testDecryptError() {
		try {
			ResponseWrapper<ObjectNode> response = new ResponseWrapper<>();
			ObjectNode responseNode = mapper.createObjectNode();
			responseNode.put("data", "data");
			response.setResponse(responseNode);
			when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any(Class.class)))
					.thenReturn(new RestRequestDTO());
			when(restHelper.requestSync(Mockito.any()))
					.thenThrow(new RestServiceException(HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED));
			assertEquals("data", new String(securityManager.decrypt("")));
		} catch (HotlistAppException e) {
			assertEquals(e.getErrorCode(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode());
			assertEquals(e.getErrorText(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage());
		}
	}

	@Test
	public void testDecryptNoResponseData() throws IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			ResponseWrapper<ObjectNode> response = new ResponseWrapper<>();
			ObjectNode responseNode = mapper.createObjectNode();
			response.setResponse(responseNode);
			when(restBuilder.buildRequest(Mockito.any(), Mockito.any(), Mockito.any(Class.class)))
					.thenReturn(new RestRequestDTO());
			when(restHelper.requestSync(Mockito.any()))
					.thenReturn(mapper.readValue(mapper.writeValueAsString(response), ResponseWrapper.class));
			assertEquals("data", new String(securityManager.decrypt("")));
		} catch (HotlistAppException e) {
			assertEquals(e.getErrorCode(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorCode());
			assertEquals(e.getErrorText(), HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED.getErrorMessage());
		}
	}
}
