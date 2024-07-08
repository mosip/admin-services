package io.mosip.admin.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditManagerProxyControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@Value("${mosip.kernel.masterdata.audit-url}")
	String auditUrl;

	@Autowired
	RestTemplate restTemplate;
	MockRestServiceServer mockRestServiceServer;

	
	private RequestWrapper<AuditManagerRequestDto> requestDto = new RequestWrapper<AuditManagerRequestDto>();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
		auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
		auditManagerRequestDto.setDescription("Test description");

		auditManagerRequestDto.setApplicationName("Admin Portal");
		auditManagerRequestDto.setApplicationId("10009");
		auditManagerRequestDto.setSessionUserName("Test");
		auditManagerRequestDto.setSessionUserId("Test");
		auditManagerRequestDto.setHostIp("Test");
		auditManagerRequestDto.setHostName("Test");
		auditManagerRequestDto.setCreatedBy("Test");

		auditManagerRequestDto.setEventId("ADM-045");
		auditManagerRequestDto.setEventName("Click: Clicked on Home Page");
		auditManagerRequestDto.setEventType("Navigation: Page View Event");
		auditManagerRequestDto.setId("NO_ID");
		auditManagerRequestDto.setIdType("ADMIN");
		auditManagerRequestDto.setModuleId("ADM-NAV");
		auditManagerRequestDto.setModuleName("Navigation");
		requestDto.setRequest(auditManagerRequestDto);
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

	}

	@Test
	@WithUserDetails("zonal-admin")
	public void t001addAuditTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";

		mockRestServiceServer.expect(requestTo(auditUrl)).andRespond(withSuccess());
		AdminDataUtil
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/auditmanager/log")
								.contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(requestDto))
								.header(HttpHeaders.ORIGIN, "dev.mosip.io")
								.header(HttpHeaders.REFERER, "test"))
						.andReturn(), null);

	}

	@Test
	@WithUserDetails("zonal-admin")
	public void t002addAuditTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";

		mockRestServiceServer.expect(requestTo(auditUrl + "s"))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.post("/auditmanager/log").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestDto)).header(HttpHeaders.ORIGIN, "dev.mosip.io")
				.header(HttpHeaders.REFERER, "test")).andExpect(MockMvcResultMatchers.status().is(500));

	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void t003addAuditTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";

		mockRestServiceServer.expect(requestTo(auditUrl))
				.andRespond(withBadRequest().body(str).contentType(MediaType.APPLICATION_JSON));

	MvcResult m	=mockMvc.perform(MockMvcRequestBuilders.post("/auditmanager/log")
					.header(HttpHeaders.ORIGIN, "dev.mosip.io")
					.header(HttpHeaders.REFERER, "test").contentType(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(requestDto))).andReturn();
	assertEquals(m.getResponse().getStatus(), 500);
	Map mp = mapper.readValue(m.getResponse().getContentAsString(), Map.class);
	assertEquals("ADM-PKT-000", ((List<Map<String, String>>) mp.get("errors")).get(0).get("errorCode"));
	
	}
	
	
}
