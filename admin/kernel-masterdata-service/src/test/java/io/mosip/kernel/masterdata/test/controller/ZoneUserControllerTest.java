package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import java.util.ArrayList;
import java.util.List;
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

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserPutDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZoneUserControllerTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;
	
	@Value("${zone.user.details.url}")
	private String userDetailsUri;
	@Autowired
	private RestTemplate restTemplate;


	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	private RequestWrapper<SearchDtoWithoutLangCode> sr = new RequestWrapper<>();
	private RequestWrapper<ZoneUserPutDto> zoneUserPutDto =new RequestWrapper<ZoneUserPutDto>();
	private  RequestWrapper<ZoneUserDto> zoneUserDto=new  RequestWrapper<ZoneUserDto>();
	String response=null;
	MockRestServiceServer mockRestServiceServer;
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		ZoneUserDto dto=new ZoneUserDto();
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setUserId("3");
		dto.setZoneCode("NTH");
		zoneUserDto.setRequest(dto);
		
		ZoneUserPutDto putDto=new ZoneUserPutDto();
		putDto.setIsActive(true);
		putDto.setLangCode("eng");
		putDto.setUserId("3");
		putDto.setZoneCode("NTH");
		zoneUserPutDto.setRequest(putDto);
		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("zoneCode", "ASC");
		ss.add(s);
		sf.setColumnName("zoneCode");
		sf.setType("contains");
		sf.setValue("NTH");
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		sr.setRequest(sc);
		
		 response = "{\r\n" +
				"  \"id\": null,\r\n" +
				"  \"version\": null,\r\n" +
				"  \"responsetime\": \"2021-03-31T04:27:31.590Z\",\r\n" +
				"  \"metadata\": null,\r\n" +
				"  \"response\": {\r\n" +
				"    \"mosipUserDtoList\": [\r\n" +
				"      {\r\n" +
				"        \"userId\": \"110005\",\r\n" +
				"        \"mobile\": null,\r\n" +
				"        \"mail\": \"110005@xyz.com\",\r\n" +
				"        \"langCode\": null,\r\n" +
				"        \"userPassword\": null,\r\n" +
				"        \"name\": \"Test110005 Auto110005\",\r\n" +
				"        \"role\": \"ZONAL_ADMIN,GLOBAL_ADMIN\",\r\n" +
				"        \"token\": null,\r\n" +
				"        \"rid\": null\r\n" +
				"      },\r\n" +
				"      {\r\n" +
				"        \"userId\": \"110006\",\r\n" +
				"        \"mobile\": null,\r\n" +
				"        \"mail\": \"110006@xyz.com\",\r\n" +
				"        \"langCode\": null,\r\n" +
				"        \"userPassword\": null,\r\n" +
				"        \"name\": \"Test110006 Auto110006\",\r\n" +
				"        \"role\": \"REGISTRATION_OPERATOR\",\r\n" +
				"        \"token\": null,\r\n" +
				"        \"rid\": null\r\n" +
				"      }\r\n" +
				"    ]\r\n" +
				"  },\r\n" +
				"  \"errors\": null\r\n" +
				"}";
		
		 mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?search=110006"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

	}
	@Test
	@WithUserDetails("global-admin")
	public void t001mapUserZoneTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserDto))).andReturn(),
				"KER-USR-021");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002mapUserZoneTest() throws Exception {
		zoneUserDto.getRequest().setUserId("5");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserDto))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002mapUserZoneFailTest() throws Exception {
		zoneUserDto.getRequest().setUserId("100");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserDto))).andReturn(),
				"KER-USR-014");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateapUserZoneTest2() throws Exception {
		zoneUserPutDto.getRequest().setUserId("7");
		//zoneUserPutDto.getRequest().setIsActive(true);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserPutDto))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateapUserZoneTest1() throws Exception {
		zoneUserPutDto.getRequest().setUserId("5");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserPutDto))).andReturn(),
				"KER-USR-011");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateapUserZoneTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserPutDto))).andReturn(),
				"KER-USR-011");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateapUserZoneFailTest() throws Exception {
		zoneUserPutDto.getRequest().setZoneCode("a");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/zoneuser").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(zoneUserPutDto))).andReturn(),
				"KER-USR-014");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005deleteMapUserZoneTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/zoneuser/3/CST")).andReturn(),
				"KER-USR-011");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005deleteMapUserZoneFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/zoneuser/10/NTH")).andReturn(),
				"KER-USR-017");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateapUserZoneStatusTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/zoneuser").param("isActive","true").param("userId", "7")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateapUserZoneStatusTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/zoneuser").param("isActive","true").param("userId", "3")).andReturn(),
				"KER-USR-011");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateapUserZoneStatusFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/zoneuser").param("isActive","true").param("userId", "10")).andReturn(),
				"KER-USR-019");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t008searchZoneUserMappingTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchZoneUserMappingTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchZoneUserMappingTest1() throws Exception {
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		sr.getRequest().getFilters().get(0).setType("contains");
		sr.getRequest().getFilters().get(0).setColumnName("userName");
		sr.getRequest().getFilters().get(0).setValue("dummy");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchZoneUserMappingTest3() throws Exception {
		sr.getRequest().getFilters().get(0).setType("contains");
		sr.getRequest().getFilters().get(0).setColumnName("zoneName");
		sr.getRequest().getFilters().get(0).setValue("North");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010searchZoneUserMappingTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t011searchZoneUserMappingTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/zoneuser/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	

	
	@Test
	@WithUserDetails("global-admin")
	public void t013getHistoryByUserIdAndTimestampTest1() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/zoneuser/history/5/2021-11-1605:12:27.164Z")).andReturn(),
				"KER-USR-002");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t013getHistoryByUserIdAndTimestampTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/zoneuser/history/5/2021-11-16T05:12:27.164Z")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getHistoryByUserIdAndTimestampFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/zoneuser/history/20/2021-11-16T05:12:27.164Z")).andReturn(),
				"KER-USR-003");
	}
	
	
}
