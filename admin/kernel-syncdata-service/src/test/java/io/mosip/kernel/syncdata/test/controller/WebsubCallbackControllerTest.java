package io.mosip.kernel.syncdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.model.Type;
import io.mosip.kernel.partnercertservice.dto.CACertificateResponseDto;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.websub.api.verifier.AuthenticatedContentVerifier;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class WebsubCallbackControllerTest {

	private static final String CERTIFICATE_DATA_SHARE_URL = "certChainDatashareUrl";
	private static final String PARTNER_DOMAIN = "partnerDomain";
	
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
	public void setUp() {
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate)
				.ignoreExpectOrder(true)
				.build();
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

}
