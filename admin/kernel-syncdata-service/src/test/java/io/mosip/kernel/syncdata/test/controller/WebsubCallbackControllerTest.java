package io.mosip.kernel.syncdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.model.Type;
import io.mosip.kernel.partnercertservice.dto.CACertificateRequestDto;
import io.mosip.kernel.partnercertservice.dto.CACertificateResponseDto;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.syncdata.controller.WebsubCallbackController;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.websub.api.verifier.AuthenticatedContentVerifier;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import jakarta.xml.bind.DatatypeConverter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class WebsubCallbackControllerTest {

	private static final String CERTIFICATE_DATA_SHARE_URL = "certChainDatashareUrl";
	private static final String PARTNER_DOMAIN = "partnerDomain";

	@InjectMocks
	private WebsubCallbackController controller;

	@Mock
	private ObjectMapper objectMapper;
	
	@Autowired
	public MockMvc mockMvc;

	@Mock
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate restTemplate;

	@Mock
	private PartnerCertificateManagerService partnerCertificateManagerService;

	@Autowired
	private AuthenticatedContentVerifier authenticatedContentVerifier;

	@Value("${syncdata.websub.callback.secret.ca-cert}")
	private String secret;

	@Value("${syncdata.websub.topic.ca-cert}")
	private String topic;

	private String callback = "/v1/syncdata/websub/callback/cacert";

	private MockRestServiceServer mockRestServiceServer;

	@Before
	public void setUp() throws Exception {
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate)
				.ignoreExpectOrder(true)
				.build();
		setField("partnerAllowedDomains", Arrays.asList("FTM", "DEVICE"));
	}
	
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void testHandleCACertificate_shouldUploadCertificate() {

		Map<String, Object> data = new HashMap<>();
		data.put(PARTNER_DOMAIN, "DEVICE");
		data.put(CERTIFICATE_DATA_SHARE_URL, "https://localhost:8080/datashares/testts");

		EventModel eventModel = new EventModel();
		eventModel.setTopic(topic);
		eventModel.setPublishedOn(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		eventModel.setPublisher("");
		Event event = new Event();
		event.setData(data);
		event.setId("test");
		event.setTimestamp(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		event.setType(new Type());
		eventModel.setEvent(event);

		mockRestServiceServer.expect(requestTo("https://localhost:8080/datashares/testts"))
				.andRespond(withSuccess()
				.body("etstsetstsetsetettstststsetsetset"));

		CACertificateResponseDto responseDto = new CACertificateResponseDto();
		responseDto.setStatus("success");
		lenient().doReturn(responseDto)
				.when(partnerCertificateManagerService).uploadCACertificate(Mockito.any());
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testHandleCACertificate_shouldHandleError() throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put(PARTNER_DOMAIN, "DEVICE");
		data.put(CERTIFICATE_DATA_SHARE_URL, "https://localhost:8080/datashares/testts");

		EventModel eventModel = new EventModel();
		eventModel.setTopic(topic);
		eventModel.setPublishedOn(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		eventModel.setPublisher("");
		Event event = new Event();
		event.setData(data);
		event.setId("test");
		event.setTimestamp(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		event.setType(new Type());
		eventModel.setEvent(event);

		ResponseWrapper<JsonNode> errorRes = new ResponseWrapper<>();
		ServiceError serviceError = new ServiceError();
		serviceError.setErrorCode("Failed");
		serviceError.setMessage("Failed");
		errorRes.setErrors(Collections.singletonList(serviceError));
		mockRestServiceServer.expect(requestTo("https://localhost:8080/datashares/testts"))
				.andRespond(withSuccess().body(objectMapper.writeValueAsBytes(errorRes)));

		CACertificateResponseDto responseDto = new CACertificateResponseDto();
		responseDto.setStatus("success");
		lenient().when(partnerCertificateManagerService.uploadCACertificate(Mockito.any())).thenReturn(responseDto);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testHandleCACertificate_shouldUploadCACertificate() {

		Map<String, Object> data = new HashMap<>();
		data.put(PARTNER_DOMAIN, "DEVICE");
		data.put(CERTIFICATE_DATA_SHARE_URL, "https://localhost:8080/datashares/testts");

		EventModel eventModel = new EventModel();
		eventModel.setTopic(topic);
		eventModel.setPublishedOn(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		eventModel.setPublisher("");
		Event event = new Event();
		event.setData(data);
		event.setId("test");
		event.setTimestamp(DateUtils.formatToISOString(LocalDateTime.now(ZoneOffset.UTC)));
		event.setType(new Type());
		eventModel.setEvent(event);

		CACertificateResponseDto responseDto = new CACertificateResponseDto();
		responseDto.setStatus("success");
		lenient().when(partnerCertificateManagerService.uploadCACertificate(Mockito.any())).thenReturn(responseDto);
	}

	private String getHubSignature(byte[] body) {
		KeyParameter params = new KeyParameter(secret.getBytes());
		HMac hMac = new HMac(new SHA1Digest());
		hMac.init(params);
		hMac.update(body, 0, body.length);
		byte[] result = new byte[hMac.getMacSize()];
		hMac.doFinal(result, 0);
		return "SHA1="+DatatypeConverter.printHexBinary(result).toLowerCase();
	}

	private void setField(String name, Object value) throws Exception {
		Field field = WebsubCallbackController.class.getDeclaredField(name);
		field.setAccessible(true);
		field.set(controller, value);
	}

	private EventModel buildEvent(Map<String, Object> dataMap) {

		EventModel eventModel = new EventModel();   // DO NOT mock this
		Event event = mock(Event.class);            // mock real Event class

		when(event.getData()).thenReturn(dataMap);

		eventModel.setEvent(event);

		return eventModel;
	}

	/**
	 * ✅ Success Flow
	 */
	@Test
	public void handleCACertificate_success() throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "FTM");
		data.put("certChainDatashareUrl", "http://test-url");

		EventModel eventModel = buildEvent(data);

		String certificateResponse = "{\"response\":{}}";

		when(restTemplate.getForObject("http://test-url", String.class))
				.thenReturn(certificateResponse);

		ResponseWrapper<JsonNode> wrapper = new ResponseWrapper<>();
		wrapper.setErrors(null);

		controller.handleCACertificate(eventModel);

		ArgumentCaptor<CACertificateRequestDto> captor =
				ArgumentCaptor.forClass(CACertificateRequestDto.class);

		verify(partnerCertificateManagerService, times(1))
				.uploadCACertificate(captor.capture());

		CACertificateRequestDto dto = captor.getValue();
		assertEquals("FTM", dto.getPartnerDomain());
		assertEquals(certificateResponse, dto.getCertificateData());
	}

	/**
	 * ✅ Error In Response → Should Not Upload
	 */
	@Test
	public void handleCACertificate_whenServiceError_shouldStillUpload() throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "FTM");
		data.put("certChainDatashareUrl", "http://test-url");

		EventModel eventModel = buildEvent(data);

		String response = "{\"errors\":[{\"errorCode\":\"ERR-001\"}]}";

		when(restTemplate.getForObject(anyString(), eq(String.class)))
				.thenReturn(response);

		ResponseWrapper<JsonNode> wrapper = new ResponseWrapper<>();
		wrapper.setErrors(Collections.singletonList(new ServiceError("ERR-001", "Error")));

		controller.handleCACertificate(eventModel);

		verify(partnerCertificateManagerService, times(1))
				.uploadCACertificate(any());
	}

	/**
	 * ✅ Domain Not Allowed
	 */
	@Test
	public void handleCACertificate_domainNotAllowed() {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "NOT_ALLOWED");
		data.put("certChainDatashareUrl", "http://test-url");

		EventModel eventModel = buildEvent(data);

		controller.handleCACertificate(eventModel);

		verifyNoInteractions(partnerCertificateManagerService);
	}

	/**
	 * ✅ Missing Keys
	 */
	@Test
	public void handleCACertificate_missingKeys() {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "FTM");

		EventModel eventModel = buildEvent(data);

		controller.handleCACertificate(eventModel);

		verifyNoInteractions(partnerCertificateManagerService);
	}

	/**
	 * ✅ Exception From RestTemplate
	 */
	@Test
	public void handleCACertificate_restTemplateThrowsException() {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "FTM");
		data.put("certChainDatashareUrl", "http://test-url");

		EventModel eventModel = buildEvent(data);

		when(restTemplate.getForObject(anyString(), eq(String.class)))
				.thenThrow(new RuntimeException("Network error"));

		assertDoesNotThrow(() -> controller.handleCACertificate(eventModel));

		verify(partnerCertificateManagerService, never())
				.uploadCACertificate(any());
	}

	/**
	 * ✅ ObjectMapper Parsing Exception
	 */
	@Test
	public void handleCACertificate_whenInvalidJsonResponse_shouldStillUpload() throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put("partnerDomain", "FTM");
		data.put("certChainDatashareUrl", "http://test-url");

		EventModel eventModel = buildEvent(data);

		String response = "invalid-json";

		when(restTemplate.getForObject(anyString(), eq(String.class)))
				.thenReturn(response);

		controller.handleCACertificate(eventModel);

		verify(partnerCertificateManagerService, times(1))
				.uploadCACertificate(any());
	}

}
