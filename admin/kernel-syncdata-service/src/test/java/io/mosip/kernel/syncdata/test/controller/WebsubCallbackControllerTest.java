package io.mosip.kernel.syncdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.Type;
import io.mosip.kernel.partnercertservice.dto.CACertificateResponseDto;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;
import io.mosip.kernel.websub.api.filter.IntentVerificationFilter;
import io.mosip.kernel.websub.api.verifier.AuthenticatedContentVerifier;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class WebsubCallbackControllerTest {

	private static final String CERTIFICATE_DATA_SHARE_URL = "certChainDatashareUrl";
	private static final String PARTNER_DOMAIN = "partnerDomain";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	public MockMvc mockMvc;

	@Autowired
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate restTemplate;

	@MockBean
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
	public void t001handleCACertificateTest() throws Exception {

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
		Mockito.when(partnerCertificateManagerService.uploadCACertificate(Mockito.any())).thenReturn(responseDto);

		byte[] body = objectMapper.writeValueAsBytes(eventModel);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/websub/callback/cacert")
						.contentType(MediaType.APPLICATION_JSON)
						.header("x-hub-signature", getHubSignature(body))
						.content(body)).andReturn();

		Assert.assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t002handleCACertificateTest() throws Exception {

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
		Mockito.when(partnerCertificateManagerService.uploadCACertificate(Mockito.any())).thenReturn(responseDto);

		byte[] body = objectMapper.writeValueAsBytes(eventModel);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/websub/callback/cacert")
				.contentType(MediaType.APPLICATION_JSON)
				.header("x-hub-signature", getHubSignature(body))
				.content(body)).andReturn();

		//Failure with datashare URL should still return 200
		Assert.assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t003handleCACertificateTest() throws Exception {

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
		Mockito.when(partnerCertificateManagerService.uploadCACertificate(Mockito.any())).thenReturn(responseDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/websub/callback/cacert")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(eventModel))).andReturn();

		//Failure with websub authentication
		Assert.assertEquals(500, result.getResponse().getStatus());
		ResponseWrapper responseWrapper = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseWrapper.class);

		//Failure with websub authentication
		Assert.assertTrue(responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty());
		ServiceError serviceError = (ServiceError) responseWrapper.getErrors().get(0);
		Assert.assertEquals("KER-SNC-500", serviceError.getErrorCode());
		Assert.assertTrue(serviceError.getMessage().contains("KER-WSC-106"));
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
