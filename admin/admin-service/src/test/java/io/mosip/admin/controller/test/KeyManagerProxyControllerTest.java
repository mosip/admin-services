package io.mosip.admin.controller.test;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.admin.util.AdminDataUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KeyManagerProxyControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	@Autowired
	RestTemplate restTemplate;

	@Value("${mosip.admin.base.url}")
	private String baseUrl;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).setAuditRequestDto(Mockito.any(),Mockito.any());
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001generateCsrFailTest() throws Exception {
		String response = "{\r\n" + "  \"id\": \"string\",\r\n" + "  \"version\": \"string\",\r\n"
				+ "  \"responsetime\": \"2022-03-07T11:55:32.429Z\",\r\n" + "  \"metadata\": null,\r\n"
				+ "  \"response\": null,\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
				+ "      \"errorCode\": \"KER-KMS-002\",\r\n"
				+ "      \"message\": \"ApplicationId not found in Key Policy. Key/CSR generation not allowed.\"\r\n"
				+ "    }\r\n" + "  ]\r\n" + "}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(baseUrl+"/v1/keymanager/generatecsr"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/keymanager/generatecsr")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString("string"))).andReturn(),
				"KER-KMS-002");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001generateCsrTest() throws Exception {
		String response = "{\r\n" + "  \"id\": \"string\",\r\n" + "  \"version\": \"string\",\r\n"
				+ "  \"responsetime\": \"2022-03-07T12:08:52.656Z\",\r\n" + "  \"metadata\": null,\r\n"
				+ "  \"response\": {\r\n" + "    \"certificate\": null,\r\n"
				+ "    \"certSignRequest\": \"-----BEGIN CERTIFICATE REQUEST-----\\nMIICtTCCAZ0CAQAwcDELMAkGA1UEBhMCSU4xCzAJBgNVBAgTAktBMRIwEAYDVQQH\\nEwlCQU5HQUxPUkUxDTALBgNVBAoTBElJVEIxGjAYBgNVBAsTEU1PU0lQLVRFQ0gt\\nQ0VOVEVSMRUwEwYDVQQDEwx3d3cubW9zaXAuaW8wggEiMA0GCSqGSIb3DQEBAQUA\\nA4IBDwAwggEKAoIBAQDnIQ2bI6ZJG0JbjjpwLrxjA2aI6R+0j2xHn991iNVRkXAc\\n2xpQwht4EHOg+BUzfwTV9sSkX3oggPS+dltgdg6brunbGVtBffp1htO/0kdF0cg4\\nVHs17SE1SdNhZuS+qeajzh1ffPz67jWUmjjNMppt3TDxbdgK3gCE7yfGR7MDvb4w\\nky2+0f3k/AH/ortxQk2enxjD436B8V4v/JkQqlVVRJlNx1imarNbibRGzfBlPl0j\\nNQgpKYo92hjhJwSeQMW7EgJyRQjHnX3heeBgbiLjIX0PdyFvydLXmslmhVCxjaNY\\nEmbCvD53Isp78Fqekqo5P/NunaWkGuw6A51jNpH5AgMBAAGgADANBgkqhkiG9w0B\\nAQsFAAOCAQEA2x+hsng/rg1Cwvzy7WpV0AOjvT/wTx0K182nt6iiSqoVIbxETZ0h\\nnv1NVg7DySCu5Onsva0LRnzRNGwLcWqJQHzI6oM35OFSjEiMFw+iKvpy2OYI43w3\\nEaIoawQ5fC6OD6YJ0lTZh7Wg59M4NVZm/zg2uQ3QARkRNrn5zVe5KtkajgeTmLvi\\nOoCtXa8oMQL/3dmUdDaQTsyUZ6OoHdgh6b/eS6BkP8J5yTof000r3HFi9YdV/gVv\\nVsfDhiAA9FrEfMN6p3TqCLkdBAjQXiv7o+hdYGs/xN+kv1tdA0vWHe/HQzJt8Xu8\\njdja58gt8R0VaHzb/w/P37HuXS2BrkGPsQ==\\n-----END CERTIFICATE REQUEST-----\\n\",\r\n"
				+ "    \"issuedAt\": \"2022-02-21T07:19:50.000Z\",\r\n"
				+ "    \"expiryAt\": \"2025-02-20T07:19:50.000Z\",\r\n"
				+ "    \"timestamp\": \"2022-03-07T12:08:52.656Z\"\r\n" + "  },\r\n" + "  \"errors\": null\r\n" + "}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(baseUrl+"/v1/keymanager/generatecsr"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/keymanager/generatecsr")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString("string"))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002generateCsrCertificate() throws Exception {

		String response = "{\r\n" + "  \"id\": null,\r\n" + "  \"version\": null,\r\n"
				+ "  \"responsetime\": \"2022-03-07T11:53:27.120Z\",\r\n" + "  \"metadata\": null,\r\n"
				+ "  \"response\": {\r\n"
				+ "    \"certificate\": \"-----BEGIN CERTIFICATE-----\\nMIIDxTCCAq2gAwIBAgII6ceDW5TKkq0wDQYJKoZIhvcNAQELBQAwfzELMAkGA1UE\\nBhMCSU4xCzAJBgNVBAgMAktBMRIwEAYDVQQHDAlCQU5HQUxPUkUxDTALBgNVBAoM\\nBElJVEIxKTAnBgNVBAsMIE1PU0lQLVRFQ0gtQ0VOVEVSIChSRUdJU1RSQVRJT04p\\nMRUwEwYDVQQDDAx3d3cubW9zaXAuaW8wHhcNMjIwMzA1MDQ0MTA5WhcNMjQwMzA0\\nMDQ0MTA5WjCBhDELMAkGA1UEBhMCSU4xCzAJBgNVBAgMAktBMRIwEAYDVQQHDAlC\\nQU5HQUxPUkUxDTALBgNVBAoMBElJVEIxKTAnBgNVBAsMIE1PU0lQLVRFQ0gtQ0VO\\nVEVSIChSRUdJU1RSQVRJT04pMRowGAYDVQQDDBFSRUdJU1RSQVRJT04tYWJjZDCC\\nASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALNzoLsPX2UN1Pvlyv1ZJe64\\nejADXd60Vq7ftRiXrPkNfF5ai+yf59nZVwbncFKoudi9DZom+V8WYKrEqfh6ncbK\\nevDK6YyEPd5YvAuhu9PYmUCQzglpyxNVwvF+IAV8xdm8aNoq2DyJ3giCeTGxq4hO\\nYMcjMriOtwf2J6NyQIOvViQVhb5fZk2y9tqRZ89omyNBdrJ5pfkvepj92kbhtt/Q\\nxRSfk6GhSJ2OdCC7tDOn++MrTpWt40HJXfZgOAgThORTP4NP98+AyW+SYglbpM0r\\nghDK2JgKfdzeQrZC9qZmXaW/C04gD5SGTkUKyh/GnAMeE3Su5/6EAQEaridxwAEC\\nAwEAAaM/MD0wDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUj41GT3H30Fd2nJEM0BLv\\nx/ATpeYwDgYDVR0PAQH/BAQDAgUgMA0GCSqGSIb3DQEBCwUAA4IBAQAHysXt3S9C\\nieUK1PgDmJ0ogFaDtwW+28mQIk4V29XEAjQ2eC0PQi8A880ZToQ1Az2VHn76MsSO\\noIL4pRPcs7f6lCclKmQwpr7An8ZtG80aFuFCTmdtc8ahZG05j0ujGN6zYvvqXKeE\\nfI7sU2BJS3TMdTYw5mbFYA+lSF/cv+1GN5xlMRvDiH5MEpg19EOaKwARF/dl3snv\\nNFCoKnMzW5PR6HNFJyqGhWDCe8lp8NR/BokZ/s6eXbcd93aEUfO5lNUCFKYQTmDy\\nQvJAnZ0m00tv0+vNqfw3n1APDtUB9RUoE/ExOovCAVdPhDmphvJwWhhvtyajNVGU\\n724qC5ZyeLiY\\n-----END CERTIFICATE-----\\n\",\r\n"
				+ "    \"certSignRequest\": null,\r\n" + "    \"issuedAt\": \"2022-03-05T04:41:09.000Z\",\r\n"
				+ "    \"expiryAt\": \"2024-03-04T04:41:09.000Z\",\r\n"
				+ "    \"timestamp\": \"2022-03-07T11:53:27.121Z\"\r\n" + "  },\r\n" + "  \"errors\": null\r\n" + "}";

		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(baseUrl+"/v1/keymanager/generatecsrcertificate?applicationId=REGISTRATION&referenceId=refID"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/keymanager/generatecsrcertificate?applicationId=REGISTRATION&referenceId=refID"))
						.andReturn(),
				null);
	}


	@Test
	@WithUserDetails("global-admin")
	public void t003uploadCertificate() throws Exception {
		String response = "{\r\n" + "  \"id\": \"string\",\r\n" + "  \"version\": \"string\",\r\n"
				+ "  \"responsetime\": \"2022-03-07T12:17:35.489Z\",\r\n" + "  \"metadata\": null,\r\n"
				+ "  \"response\": null,\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
				+ "      \"errorCode\": \"KER-KMS-003\",\r\n" + "      \"message\": \"No unique alias is found\"\r\n"
				+ "    }\r\n" + "  ]\r\n" + "}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(baseUrl+"/v1/keymanager/uploadcertificate"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil
				.checkResponse(
						mockMvc.perform(MockMvcRequestBuilders.post("/keymanager/uploadcertificate")
								.accept(MediaType.APPLICATION_ATOM_XML.APPLICATION_JSON)
								.content(mapper.writeValueAsString("string"))).andReturn(),
						"KER-KMS-003");
	}



	@Test
	@WithUserDetails("global-admin")
	public void t004uploadCertificateTest() throws Exception {

		String response = "{\r\n" + "  \"id\": \"string\",\r\n" + "  \"version\": \"string\",\r\n"
				+ "  \"responsetime\": \"2022-03-07T12:21:14.274Z\",\r\n" + "  \"metadata\": null,\r\n"
				+ "  \"response\": null,\r\n" + "  \"errors\": [\r\n" + "    {\r\n"
				+ "      \"errorCode\": \"KER-KMS-003\",\r\n" + "      \"message\": \"No unique alias is found\"\r\n"
				+ "    }\r\n" + "  ]\r\n" + "}";
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(baseUrl+"/v1/keymanager/uploadotherdomaincertificate"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil
				.checkResponse(
						mockMvc.perform(MockMvcRequestBuilders.post("/keymanager/uploadotherdomaincertificate")
								.accept(MediaType.APPLICATION_ATOM_XML.APPLICATION_JSON)
								.content(mapper.writeValueAsString("temp"))).andReturn(),
						"KER-KMS-003");
	}

}
