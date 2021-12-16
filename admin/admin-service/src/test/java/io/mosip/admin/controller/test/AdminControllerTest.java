package io.mosip.admin.controller.test;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import io.mosip.admin.dto.FilterInfo;
import io.mosip.admin.dto.SearchInfo;
import io.mosip.admin.dto.SortInfo;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.RestClient;
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
	@MockBean
	private AuditUtil auditUtil;
	
	@MockBean
	private RestClient restClient;
	@Autowired
	RestTemplate restTemplate;
	MockRestServiceServer mockRestServiceServer;
	@Value("${LOST_RID_API}")
	String lstRidUrl;

	
	private RequestWrapper<SearchInfo> searchInfoReq =new RequestWrapper<>();
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());
		SearchInfo info=new SearchInfo();
		List<FilterInfo> lst=new ArrayList<>();
		FilterInfo e=new FilterInfo();
		e.setColumnName("registrationId");
		e.setType("contains");
		e.setValue("1234");
		lst.add(e);
		info.setFilters(lst);
		List<SortInfo> s=new ArrayList<>(); 
		SortInfo sf=new SortInfo();
		sf.setSortField("registrationDate");
		sf.setSortType("desc");
		s.add(sf);
		info.setSort(s);
		searchInfoReq.setRequest(info);
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		try {
			Mockito.when(restClient.getToken()).thenReturn(Mockito.anyString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
/*	@Test
	@WithUserDetails(value = "zonal-admin")
	public void t001lostRidTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": [{\"registrationId\":\"1234\",\"registrationDate\":\"2021-12-14 16:29:13,436\"}],\r\n    \"errors\": []\r\n}";
		searchInfoReq.getRequest().setSort(new ArrayList<SortInfo>());
		System.setProperty("token", "eyJhbGciOiJSUzI1NiIsImtpZCI6IkZ0eklpMUVqMURMcVVtNl8tbTIwaW45bm5iVlloSEFDcHVmNC1HTDRFdUUifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLTZoanJnIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3MWFhODY4YS1mMzAxLTRhMTgtOGNiMS02NmM3MzEyZmI0MTEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.Ku9ge1pIrnc5vDMMKSpLsO08lfMbs941lR1NMZ3MseAftwbUrSNYJ2SAlizD03N3rxH_G_e4koTm6jzqZqoSdIBGMjJjtG5H69rJDjCiLRg9B4IxuAUhdpLyJND0HuwJ1IH3rjGbjHWWoNVSXwrmB3rij6FP22OPO2TdnSpG0QL2nuFNFhaMs0R0a337s5JgRETOlIJ9MzySYW7Ge_bf-DlF7qWl_Jyy962llli7yhIO_rPFVbFEyNr5C25Nq1BTxVUjNpBfJdALsdLuHNnuduy9hxZLCVgJW8ja4EQYQ717v7fLZf6h02rqlSokXDNAemYDbOZvitUxxbSrQ62ISQ");
		
		mockRestServiceServer.expect(requestTo(lstRidUrl))
				.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
	
		AdminDataUtil.checkResponse((mockMvc.perform(MockMvcRequestBuilders.post("/lostRid").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchInfoReq))).andReturn()),null);

	}*/
	@Test
	@WithUserDetails(value = "zonal-admin")
	public void t002lostRidTest() throws Exception {
		String str = "{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": [{\"registrationId\":\"1234\",\"registrationDate\":\"2021-12-14 16:29:13,436\"}],\r\n    \"errors\": []\r\n}";
		searchInfoReq.getRequest().setSort(new ArrayList<SortInfo>());
		
		mockRestServiceServer.expect(requestTo(lstRidUrl))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		AdminDataUtil.checkResponse((mockMvc.perform(MockMvcRequestBuilders.post("/lostRid").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchInfoReq))).andReturn()),"ADMN-LRID-001");

	}
	


	
}
