package io.mosip.kernel.masterdata.test.controller;

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

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZoneControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	
	@Value("${zone.user.details.url}")
	private String userDetailsUri;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		String response = "{\r\n" +
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
		
		MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

		mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?search=110006"))
				.andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

	
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getZoneHierarchyTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/hierarchy/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getZoneHierarchyTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/hierarchy/ara")).andReturn(), null);

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t002getZoneHierarchyFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/hierarchy/eng1")).andReturn(), "KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getLeafZonesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafs/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004getLeafZonesFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafs/ara")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005getSubZonesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/subzone/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005getSubZonesTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/subzone/ara")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006getSubZonesFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/subzone/ara1")).andReturn(), "KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006getSubZonesFailTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/subzone/ara")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getZoneNameBasedOnUserIDAndLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID", "global-admin").param("langCode","eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010getZoneNameBasedOnUserIDAndLangCodeFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng1")).andReturn(), "KER-MSD-999");

	}
	@Test
	@WithUserDetails("global-admin")
	public void t010getZoneNameBasedOnUserIDAndLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng")).andReturn(), "KER-MSD-391");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010getZoneNameBasedOnUserIDAndLangCodeFailTest4() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng")).andReturn(), "KER-MSD-392");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011zoneFilterValuesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/zones/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("code", "NTH", "")))).andReturn(), "KER-MSD-322");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t012zoneFilterValuesFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/zones/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("codee", "NTH", "all")))).andReturn(), "KER-MSD-317");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getLeafZonesBasedOnZoneCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafzones/eng1")).andReturn(), "KER-MSD-999");

	}
	
	/*@Test
	@WithUserDetails("global-admin")
	public void t014getLeafZonesBasedOnZoneCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafzones/ara")).andReturn(),null);

	}*/
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t015getZoneNameBasedOnUserIDAndLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng")).andReturn(), "KER-MSD-391");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t016getZoneNameBasedOnUserIDAndLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng")).andReturn(), "KER-MSD-391");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017getZoneNameBasedOnUserIDAndLangCodeFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/zonename").param("userID","global-admin").param("langCode","eng1")).andReturn(), "KER-MSD-999");

	}
	
	/*@Test
	@WithUserDetails("global-admin")
	public void t018authorizeZoneTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/authorize").param("rid","10002")).andReturn(), null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t018authorizeZoneTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/authorize").param("rid","10103")).andReturn(), "ADM-PKT-001");

	}*/
	
	
}
