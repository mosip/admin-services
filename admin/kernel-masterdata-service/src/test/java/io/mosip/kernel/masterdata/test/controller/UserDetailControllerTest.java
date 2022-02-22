package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UserDetailsPutReqDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDetailControllerTest 
{

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	
	@Value("${zone.user.details.url}")
	private String userDetailsUri;
	@Autowired
	private RestTemplate restTemplate;

	private ObjectMapper mapper;
	private RequestWrapper<UserDetailsDto> ud = new RequestWrapper<>();
	private RequestWrapper<UserDetailsPutReqDto> udp = new RequestWrapper<UserDetailsPutReqDto>();
	private RequestWrapper<SearchDtoWithoutLangCode> sr=new RequestWrapper<SearchDtoWithoutLangCode>();
	String response =null;
	MockRestServiceServer mockRestServiceServer;
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10001");
		detailsDto.setStatusCode("Act");
		ud.setRequest(detailsDto);

		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("7");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10001");
		detailsPutReqDto.setStatusCode("Act");
		udp.setRequest(detailsPutReqDto);
		
		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("createdDateTime", "ASC");
		ss.add(s);
		sf.setColumnName("userName");
		sf.setType("equals");
		sf.setValue("abcd");
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
	/*	mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?pageStart=0&pageFetch=100"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?search=abcd"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
*/

	}

	@Test
	@WithUserDetails("reg-processor")
	public void t006getUserByIdTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/users/2")).andReturn(), null);

	}

	@Test
	@WithUserDetails("reg-processor")
	public void t005getUserByIdFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/users/20")).andReturn(), "KER-USR-007");

	}

	@Test
	@WithUserDetails("reg-processor")
	public void t003getUsersTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/0/1/cr_dtimes/DESC")).andReturn(), null);

	}

	@Test
	@WithUserDetails("reg-processor")
	public void t004getUsersFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/users/3/10/cr_dtimes/DESC")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002mapUserRegCenterFailTest() throws Exception {
		RequestWrapper<UserDetailsDto> requestWrapper = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10022");
		detailsDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-013");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001mapUserRegCenterTest() throws Exception {
		RequestWrapper<UserDetailsDto> requestWrapper = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10022");
		detailsDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002mapUserRegCenterTest() throws Exception {
		RequestWrapper<UserDetailsDto> requestWrapper = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode(null);
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10022");
		detailsDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-013");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001mapUserRegCenterTest1() throws Exception {
		RequestWrapper<UserDetailsDto> requestWrapper = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("79");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode(null);
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("10022");
		detailsDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-008");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t007mapUserRegCenterFailTest1() throws Exception {
		RequestWrapper<UserDetailsDto> requestWrapper = new RequestWrapper<>();
		UserDetailsDto detailsDto = new UserDetailsDto();
		detailsDto.setId("7");
		detailsDto.setIsActive(true);
		detailsDto.setLangCode("eng");
		detailsDto.setName("Desh");
		detailsDto.setRegCenterId("REGG");
		detailsDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-013");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("7");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10022");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest2() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("109");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10022");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-008");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest4() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("7");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode(null);
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10022");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest3() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("3");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10022");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-015");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateUserRegCenterTest5() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("3");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10003");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-012");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009updateUserRegCenterFailTest() throws Exception {
		RequestWrapper<UserDetailsPutReqDto> requestWrapper = new RequestWrapper<UserDetailsPutReqDto>();
		UserDetailsPutReqDto detailsPutReqDto = new UserDetailsPutReqDto();
		detailsPutReqDto.setId("200");
		detailsPutReqDto.setIsActive(true);
		detailsPutReqDto.setLangCode("eng");
		detailsPutReqDto.setName("Desh");
		detailsPutReqDto.setRegCenterId("10003");
		detailsPutReqDto.setStatusCode("Act");
		requestWrapper.setRequest(detailsPutReqDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/usercentermapping")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper))).andReturn(),
				"KER-USR-008");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t010updateUserRegCenterStatusTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/usercentermapping").param("isActive", "true").param("id", "41"))
				.andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010updateUserRegCenterStatusTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/usercentermapping").param("isActive", "true").param("id", "109"))
				.andReturn(), "KER-USR-025");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t011updateUserRegCenterStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/usercentermapping").param("isActive", "true").param("id", "6"))
				.andReturn(), "KER-USR-008");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t012deleteUserRegCenterMappingTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/usercentermapping/2")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t013deleteUserRegCenterMappingFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/usercentermapping/20")).andReturn(), "KER-USR-007");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest() throws Exception {
		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?pageStart=0&pageFetch=100"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest7() throws Exception {
		String rs=response = "{\r\n" +
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
				"      }"+
				"    ]\r\n" +
				"  },\r\n" +
				"  \"errors\": { "+
				"   \"errorCode\":\"KER-MSD-500\", " +
				"   \"errorMessage\" : \"ErrorMessage\"}" +
				" }\r\n" +
				"}";
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails")).andReturn());
			

	}

	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest1() throws Exception {
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.UNAUTHORIZED).body(response).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn());
				

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest2() throws Exception {
		String res = "{\r\n" +
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
				"      }"+
				"    ]\r\n" +
				"  },\r\n" +
				"  \"errors\": { "+
				"   \"errorCode\":\"KER-MSD-500\", "+
				"   \"errorMessage\" : \"ErrorMessage\"}"+
				" }\r\n" +
				"}";
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.UNAUTHORIZED).body(res).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn());
				

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest9() throws Exception {
		String res = "{\r\n" +
				"  \"id\": null,\r\n" +
				"  \"version\": null,\r\n" +
				"  \"responsetime\": \"2021-03-31T04:27:31.590Z\",\r\n" +
				"  \"metadata\": null,\r\n" +
				"  \"response\": {\r\n" +
				"    \"mosipUserDtoList\": null "+
				"  }},\r\n" +
				"  \"errors\": { "+
				"   \"errorCode\":\"KER-MSD-500\", "+
				"   \"errorMessage\" : \"ErrorMessage\"}"+
				" }\r\n" +
				"}";
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.OK).body(res).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn(),
				"KER-USR-009");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest8() throws Exception {
		String res = "{\r\n" +
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
				"      }"+
				"    ]\r\n" +
				"  },\r\n" +
				"  \"errors\": { "+
				"   \"errorCode\":\"KER-MSD-500\", "+
				"   \"errorMessage\" : \"ErrorMessage\"}"+
				" }\r\n" +
				"}";
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.OK).body(res).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest3() throws Exception {
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.FORBIDDEN).body(response).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn());
				

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest4() throws Exception {
		String res = "{\r\n" +
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
				"      }"+
				"    ]\r\n" +
				"  },\r\n" +
				"  \"errors\": { "+
				"   \"errorCode\":\"KER-MSD-500\", "+
				"   \"errorMessage\" : \"ErrorMessage\"}"+
				" }\r\n" +
				"}";
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.FORBIDDEN).body(res).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkErrorResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn());
				

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getUsersDetailsTest5() throws Exception {
		 DefaultResponseCreator responseCreator = withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body(response).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?firstName=a&lastName=a&userName=a&pageStart=0&pageFetch=100"))
		.andRespond(responseCreator);

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails").param("firstName", "a").param("lastName", "a").param("userName", "a")).andReturn(),
				"KER-USR-004");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t015getUsersDetailsFailTest() throws Exception {
		
	
			mockMvc.perform(MockMvcRequestBuilders.get("/usersdetails")).andExpect(status().is5xxServerError());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t016searchUserCenterMappingDetailsTest() throws Exception {
		
		MockRestServiceServer mockRestServiceServer1 = MockRestServiceServer.bindTo(restTemplate).build();
		
		mockRestServiceServer1.expect(requestTo(userDetailsUri + "/admin?search=abcd"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t016searchUserCenterMappingDetailsTest1() throws Exception {
		//MockRestServiceServer mockRestServiceServer1 = MockRestServiceServer.bindTo(restTemplate).build();
		
		//mockRestServiceServer1.expect(requestTo(userDetailsUri + "/admin?search=abcd"))
		//.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		sr.getRequest().getFilters().get(0).setColumnName("zoneCode");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t016searchUserCenterMappingDetailsTest2() throws Exception {
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		sr.getRequest().getFilters().get(0).setColumnName("zoneName");
		sr.getRequest().getFilters().get(0).setValue("Kenitra");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t016searchUserCenterMappingDetailsTest3() throws Exception {
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
			sr.getRequest().getFilters().get(0).setColumnName("regCenterName");
		sr.getRequest().getFilters().get(0).setValue("Center A Ben Mansour");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t016searchUserCenterMappingDetailsTest4() throws Exception {
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/userdetails/admin?search=test"))
		.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
		
		sr.getRequest().getFilters().get(0).setColumnName("userName");
		sr.getRequest().getFilters().get(0).setValue("test");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t017searchUserCenterMappingDetailsFailTest() throws Exception {
		sr.getRequest().getFilters().get(0).setColumnName("isActive");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/usercentermapping/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(), "KER-MSD-390");
	}
	
	

}
