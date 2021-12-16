package io.mosip.admin.controller.test;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDateTime;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

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
		auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now());
		auditManagerRequestDto.setApplicationId("test");
		auditManagerRequestDto.setApplicationName("test");
		auditManagerRequestDto.setCreatedBy("test");
		auditManagerRequestDto.setEventId("test");
		auditManagerRequestDto.setEventName("test");
		auditManagerRequestDto.setEventType("test");
		auditManagerRequestDto.setHostName("test");
		auditManagerRequestDto.setHostIp("test");
		auditManagerRequestDto.setId("test");
		auditManagerRequestDto.setIdType("test");
		auditManagerRequestDto.setModuleId("test");
		auditManagerRequestDto.setModuleName("test");
		auditManagerRequestDto.setSessionUserId("test");
		auditManagerRequestDto.setSessionUserName("test");
		requestDto.setRequest(auditManagerRequestDto);
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void t001addAuditTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";

		mockRestServiceServer.expect(requestTo(auditUrl))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		AdminDataUtil
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/auditmanager/log")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestDto)))
						.andReturn(), null);

	}

	@Test
	@WithUserDetails("zonal-admin")
	public void t002addAuditTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";

		mockRestServiceServer.expect(requestTo(auditUrl + "s"))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.post("/auditmanager/log").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestDto))).andExpect(MockMvcResultMatchers.status().is(500));

	}
}
