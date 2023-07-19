package io.mosip.admin.controller.test;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
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
import io.mosip.admin.dto.FilterInfo;
import io.mosip.admin.dto.LostRidDto;
import io.mosip.admin.dto.LostRidResponseDto;
import io.mosip.admin.dto.RegProcRequestWrapper;
import io.mosip.admin.dto.SearchInfo;
import io.mosip.admin.dto.SortInfo;
import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.service.AdminService;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	RestTemplate restTemplate;

	MockRestServiceServer mockRestServiceServer;

	@Value("${LOST_RID_API}")
	String lstRidUrl;

	@Value("${PACKET_MANAGER_SEARCHFIELDS}")
	String searchFieldsUrl;

	@Value("${PACKET_MANAGER_BIOMETRIC}")
	String biometricUrl;

	private RequestWrapper<SearchInfo> searchInfoReq = new RequestWrapper<>();
	SearchInfo info = new SearchInfo();

	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		//doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());

		List<FilterInfo> lst = new ArrayList<>();
		FilterInfo e = new FilterInfo();
		e.setColumnName("registrationId");
		e.setType("contains");
		e.setValue("1234");
		lst.add(e);
		info.setFilters(lst);
		List<SortInfo> s = new ArrayList<>();
		SortInfo sf = new SortInfo();
		sf.setSortField("registrationDate");
		sf.setSortType("desc");
		s.add(sf);
		info.setSort(s);
		searchInfoReq.setRequest(info);
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	@WithUserDetails(value = "zonal-admin")
	public void t002lostRidTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": [{\"registrationId\":\"1234\",\"registrationDate\":\"2021-12-14 16:29:13,436\"}],\r\n    \"errors\": []\r\n}";
		searchInfoReq.getRequest().setSort(new ArrayList<SortInfo>());

		mockRestServiceServer.expect(requestTo(lstRidUrl))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil.checkResponse(
				(mockMvc.perform(MockMvcRequestBuilders.post("/lostRid").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(searchInfoReq))).andReturn()),
				null);

	}

	@Test
	@WithUserDetails(value = "zonal-admin")
	public void t003lostRidDetailsTest() throws Exception {
		String str = "{\"id\":null,\"version\":null,\"responsetime\":\"2023-07-19T05:58:54.874Z\",\"metadata\":null,\"response\":{\"fields\":{\"fullName\":\"[ {\\n  \\\"language\\\" : \\\"eng\\\",\\n  \\\"value\\\" : \\\"test new 2\\\"\\n}, {\\n  \\\"language\\\" : \\\"fra\\\",\\n  \\\"value\\\" : \\\"test new 2\\\"\\n} ]\",\"dateOfBirth\":\"1995/01/01\"}},\"errors\":[]}";
		String biometricResponse = new String(Files.readAllBytes(Paths.get(getClass().getResource("/biometricApiResponse.json").toURI())), StandardCharsets.UTF_8);

		mockRestServiceServer.expect(requestTo(searchFieldsUrl))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		mockRestServiceServer.expect(requestTo(biometricUrl))
				.andRespond(withSuccess().body(biometricResponse).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil.checkResponse(
				(mockMvc.perform(MockMvcRequestBuilders.get("/lostRid/details/"+"10002100800001020230223050340")).andReturn()),
				null);


	}

}
