package io.mosip.kernel.syncdata.test.controller;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.UploadPublicKeyRequestDto;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyncDataControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;
	
	@Value("${mosip.kernel.keymanager-service-publickey-url}")
	private String publicKeyUrl;

	private RequestWrapper<UploadPublicKeyRequestDto> uploadPublicKeyRequestDto = new RequestWrapper<>();



	@Value("${mosip.kernel.syncdata-service-idschema-url}")
	private String idSchemaUrl;
	
	@Value("${mosip.kernel.syncdata-service-machine-url}")
	private String machineUrl;

	  @Value("${mosip.kernel.keymanager.cert.url}")
	    private String certificateUrl;
	
	@Autowired
	RestTemplate restTemplate;
	
	MockRestServiceServer mockRestServiceServer;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		UploadPublicKeyRequestDto dto = new UploadPublicKeyRequestDto();
		dto.setMachineName("alm3119");
		dto.setPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		dto.setSignPublicKey(
				"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPeK0rYSEqIhX1m4X8fk78zEhO7GTdzKE3spKlRqMc2l3fCDu0QjvC55F9saq-7fM8-oz_RDcLWOvsRl-4tLST5s86mKfsTjqmjnmUZTezSz8lb3_8YDl_K9TxOhpxXbYh9hvQ3J9Is7KECTzj1VAmmqc3HCrw_F8wC2T9wsLaIwIDAQAB");
		uploadPublicKeyRequestDto.setRequest(dto);

		 mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t001syncClientSettingsTest() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")
				.param("regcenterId", "10001")).andReturn(), "KER-SNC-141");

	}
	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t001syncClientSettingsTest1() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
				.param("regcenterId", "10001")).andReturn(), "KER-SNC-149");

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t001syncClientSettingsTest3() throws Exception {
		String str="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:44:42.885Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"locationHierarchyLevels\": [\r\n" + 
				"      {\r\n" + 
				"        \"hierarchyLevel\": 0,\r\n" + 
				"        \"hierarchyLevelName\": \"country\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": true\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"pageNo\": 0,\r\n" + 
				"    \"totalPages\": 0,\r\n" + 
				"    \"totalItems\": 0,\r\n" + 
				"    \"data\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"string\",\r\n" + 
				"        \"name\": \"string\",\r\n" + 
				"        \"langCode\": \"string\",\r\n" + 
				"        \"dataType\": \"string\",\r\n" + 
				"        \"description\": \"string\",\r\n" + 
				"        \"fieldVal\": [\r\n" + 
				"          {\r\n" + 
				"            \"array\": true,\r\n" + 
				"            \"null\": true,\r\n" + 
				"            \"float\": true,\r\n" + 
				"            \"number\": true,\r\n" + 
				"            \"valueNode\": true,\r\n" + 
				"            \"containerNode\": true,\r\n" + 
				"            \"missingNode\": true,\r\n" + 
				"            \"object\": true,\r\n" + 
				"            \"nodeType\": \"ARRAY\",\r\n" + 
				"            \"pojo\": true,\r\n" + 
				"            \"integralNumber\": true,\r\n" + 
				"            \"floatingPointNumber\": true,\r\n" + 
				"            \"short\": true,\r\n" + 
				"            \"int\": true,\r\n" + 
				"            \"long\": true,\r\n" + 
				"            \"double\": true,\r\n" + 
				"            \"bigDecimal\": true,\r\n" + 
				"            \"bigInteger\": true,\r\n" + 
				"            \"textual\": true,\r\n" + 
				"            \"boolean\": true,\r\n" + 
				"            \"binary\": true\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"isActive\": true,\r\n" + 
				"        \"createdBy\": \"string\",\r\n" + 
				"        \"updatedBy\": \"string\",\r\n" + 
				"        \"createdOn\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"        \"updatedOn\": \"2021-12-10T05:59:29.437Z\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		
		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
			
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		
		UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(String.format("http://localhost:8086/v1/masterdata/dynamicfields?pageNumber=0"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder1.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
			
			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));
	
		
	/*	SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
				.param("regcenterId", "10002")).andReturn(), null);*/
		
		mockMvc.perform(MockMvcRequestBuilders.get(
				"/clientsettings").param("keyindex",
						"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49").param("regcenterId", "10001")
						).andExpect(status().is(200));

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t001syncClientSettingsTest2() throws Exception {
		String str="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:44:42.885Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"locationHierarchyLevels\": [\r\n" + 
				"      {\r\n" + 
				"        \"hierarchyLevel\": 0,\r\n" + 
				"        \"hierarchyLevelName\": \"country\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": true\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"pageNo\": 0,\r\n" + 
				"    \"totalPages\": 0,\r\n" + 
				"    \"totalItems\": 0,\r\n" + 
				"    \"data\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"string\",\r\n" + 
				"        \"name\": \"string\",\r\n" + 
				"        \"langCode\": \"string\",\r\n" + 
				"        \"dataType\": \"string\",\r\n" + 
				"        \"description\": \"string\",\r\n" + 
				"        \"fieldVal\": [\r\n" + 
				"          {\r\n" + 
				"            \"array\": true,\r\n" + 
				"            \"null\": true,\r\n" + 
				"            \"float\": true,\r\n" + 
				"            \"number\": true,\r\n" + 
				"            \"valueNode\": true,\r\n" + 
				"            \"containerNode\": true,\r\n" + 
				"            \"missingNode\": true,\r\n" + 
				"            \"object\": true,\r\n" + 
				"            \"nodeType\": \"ARRAY\",\r\n" + 
				"            \"pojo\": true,\r\n" + 
				"            \"integralNumber\": true,\r\n" + 
				"            \"floatingPointNumber\": true,\r\n" + 
				"            \"short\": true,\r\n" + 
				"            \"int\": true,\r\n" + 
				"            \"long\": true,\r\n" + 
				"            \"double\": true,\r\n" + 
				"            \"bigDecimal\": true,\r\n" + 
				"            \"bigInteger\": true,\r\n" + 
				"            \"textual\": true,\r\n" + 
				"            \"boolean\": true,\r\n" + 
				"            \"binary\": true\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"isActive\": true,\r\n" + 
				"        \"createdBy\": \"string\",\r\n" + 
				"        \"updatedBy\": \"string\",\r\n" + 
				"        \"createdOn\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"        \"updatedOn\": \"2021-12-10T05:59:29.437Z\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		
		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
			
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		
		UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(String.format("http://localhost:8086/v1/masterdata/dynamicfields?pageNumber=0"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder1.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
			
			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));
	
		
	/*	SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
				.param("regcenterId", "10002")).andReturn(), null);*/
		
		mockMvc.perform(MockMvcRequestBuilders.get(
				"/clientsettings").param("keyindex",
						"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49").param("regcenterId", "10001")
						).andExpect(status().is(200));

	}
	 
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t002syncClientSettingsTest() throws Exception {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"abcd")
				.param("regcenterId", "10001")).andReturn(), "KER-SNC-155");
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t003getPublicKeyTest() throws Exception {

		String str="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"lastSyncTime\":null,\"publicKey\":\"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48\",\"issuedAt\":null,\"expiryAt\":null,\"profile\":\"local\"},\"errors\":[]}";
		mockRestServiceServer.expect(requestTo("https://dev.mosip.io/v1/keymanager/publickey/REGISTRATION?referenceId=10001&timeStamp=2019-09-09T09%253A00%253A00.000Z"))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	
		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/publickey/REGISTRATION")
						.param("timeStamp", "2019-09-09T09%3A00%3A00.000Z").param("referenceId", "10001")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t003getPublicKeyTest1() throws Exception {

		String str="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"lastSyncTime\":null,\"publicKey\":\"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48\",\"issuedAt\":null,\"expiryAt\":null,\"profile\":\"local\"},\"errors\":[]}";
		mockRestServiceServer.expect(requestTo("https://dev.mosip.io/v1/keymanager/publickey/REGISTRATION?referenceId&timeStamp=2019-09-09T09%253A00%253A00.000Z"))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	
		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/publickey/REGISTRATION")
						.param("timeStamp", "2019-09-09T09%3A00%3A00.000Z")).andReturn(),
				"KER-SNC-003");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t004validateKeyMachineMappingTest() throws Exception {
		uploadPublicKeyRequestDto.getRequest().setMachineName("abc");
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
				.andReturn(), "KER-SNC-155");

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t004validateKeyMachineMappingTest1() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
				.andReturn(), "KER-SNC-155");

	}
	
	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t006validateKeyMachineMappingTest() throws Exception {
		uploadPublicKeyRequestDto.getRequest().setPublicKey(null);
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
				.andReturn(), "KER-SNC-158");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t007getLatestPublishedIdSchemaTest() throws Exception {
		String res="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"additionalProp1\": {},\r\n" + 
				"    \"additionalProp2\": {},\r\n" + 
				"    \"additionalProp3\": {}\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(idSchemaUrl+"?schemaVersion=0.0"))
				.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
			
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/latestidschema")).andReturn(), "KER-SNC-166");

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t007getLatestPublishedIdSchemaTest1() throws Exception {
		String res="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"additionalProp1\": {},\r\n" + 
				"    \"additionalProp2\": {},\r\n" + 
				"    \"additionalProp3\": {}\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		mockRestServiceServer.expect(requestTo(idSchemaUrl+"?schemaVersion=0.0"))
				.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));

		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/latestidschema")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t008getCertificateTest() throws Exception {
		
		String res="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"certificate\": \"certificate\",\r\n" + 
				"    \"certSignRequest\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"    \"expiryAt\": \"2021-12-07T12:51:29.957Z\",\r\n" +
				"    \"timestamp\": \"2021-12-07T12:51:29.957Z\",\r\n" +
				"    \"issuedAt\": \"2021-12-07T12:51:29.957Z\"\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(certificateUrl+"?applicationId=app&referenceId=1111"))
		.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/getCertificate")
				.param("applicationId", "app").param("referenceId", "1111")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t009getClientPublicKeyTest() throws Exception {
		String bd="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\"machines\":[\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"10\",\r\n" + 
				"        \"name\": \"alm1009\",\r\n" + 
				"        \"serialNum\": \"NM19837379\",\r\n" + 
				"        \"macAddress\": \"E8-A9-64-1F-27-E6\",\r\n" + 
				"        \"ipAddress\": \"192.168.0.120\",\r\n" + 
				"        \"machineSpecId\": \"1001\",\r\n" + 
				"        \"regCenterId\": \"10001\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": \"true\",\r\n" + 
				"        \"validityDateTime\": null ,\r\n" + 
				"        \"keyIndex\":  \"B5\" ,\r\n" +
				"        \"publicKey\": \"M10674\" ,\r\n" +
				"        \"signPublicKey\": \"M1882734\"\r\n" +
				"      }\r\n" + 
				"    ]}}";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "10"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(bd).contentType(MediaType.APPLICATION_JSON));
		
	String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/tpm/publickey/10")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t010getClientPublicKeyTest() throws Exception {
		String bd="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"1000\",\r\n" + 
				"        \"name\": \"alm1009\",\r\n" + 
				"        \"serialNum\": \"NM19837379\",\r\n" + 
				"        \"macAddress\": \"E8-A9-64-1F-27-E6\",\r\n" + 
				"        \"ipAddress\": \"192.168.0.120\",\r\n" + 
				"        \"machineSpecId\": \"1001\",\r\n" + 
				"        \"regCenterId\": \"10001\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": \"true\",\r\n" + 
				"        \"validityDateTime\": null ,\r\n" + 
				"        \"keyIndex\":  \"B5\" ,\r\n" +
				"        \"publicKey\": \"M10674\" ,\r\n" +
				"        \"signPublicKey\": \"M1882734\"\r\n" +
				"      }\r\n" + 
				"    ]}";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "1000"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(
				"{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, "
						+ "\"response\": "+bd+", \"errors\": null }"));
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/tpm/publickey/1000")).andReturn(),
				"KER-SNC-155");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t011getMachineConfigDetailsTest() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}"; 
	
			mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/registration-test.properties"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
		mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/application-test.properties"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.get(
				"/configs/B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")).andExpect(status().is(500));
	

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t011getMachineConfigDetailsTest1() throws Exception {
		String str1 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}";

		UriComponentsBuilder uribuilder1 = UriComponentsBuilder
				.fromUriString("/localhost/kernel-syncdata-service/test/0.9.0/application-test.properties");

		mockRestServiceServer.expect(requestTo(uribuilder1.toUriString()))
				.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		UriComponentsBuilder uribuilder = UriComponentsBuilder
				.fromUriString("/localhost/kernel-syncdata-service/test/0.9.0/registration-test.properties");
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(str1));

		UriComponentsBuilder uribuilder2 = UriComponentsBuilder
				.fromUriString("https://dev.mosip.net/v1/keymanager/jwtSign");
		String str2 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo(uribuilder2.toUriString())).andRespond(withSuccess().body(str2));

		mockMvc.perform(MockMvcRequestBuilders.get(
				"/configs/B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
				.andExpect(status().is(200));

	}
	
	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t012getMachineConfigDetailsTest() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/application-test.properties"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/configs/abcd")).andReturn(), "KER-SNC-155");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t013getUserDetailsBasedOnKeyIndexTest() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get(
				"/userdetails").param("keyindex","B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
				.andReturn(), "KER-SNC-141");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t014getUserDetailsBasedOnKeyIndexTest() throws Exception {
		String res="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"lastSyncTime\": \"2021-12-07T12:51:29.957Z\",\r\n" + 
				"    \"mosipUserDtoList\":[{" +
				" \"userId\": \"2\","+
				" \"mobile\": \"7898787687\","+
				" \"mail\": \"abc@gmail.com\","+
				" \"userPassword\": null ,"+
				" \"name\": \"abc\","+
				" \"role\": \"admin\","+
				" \"rId\": \"1234\","+
				" \"isDeleted\": \"false\","+
				" \"langCode\": \"eng\","+
				" \"isActive\": \"true\""+
				 "}] \r\n"+
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo("http://localhost:8091/authmanager/userdetails/registrationclient"))
		.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/userdetails").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")).andReturn(),null);

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t014getUserDetailsBasedOnKeyIndexTest1() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/userdetails").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:50")).andReturn(),"KER-SNC-303");

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t015getCACertificatesTest() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/getcacertificates")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t016getCACertificatesTest() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/getcacertificates").param("lastupdated", "2018-12-12 11:42:52.994"))
				.andReturn(), "KER_SNC-100");

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t016getCACertificatesTest1() throws Exception {

		SyncDataUtil.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/getcacertificates").param("lastupdated", "2050-12-29T13:03:44.719Z"))
				.andReturn(), "KER-SNC-135");

	}
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t016getCACertificatesTest2() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	
		SyncDataUtil.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/getcacertificates").param("lastupdated", "2021-12-09T13:03:44.719Z"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t017downloadScriptTest() throws Exception {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/abcd"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
		String res="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
		
		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/scripts/abcd").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t018downloadScriptTest() throws Exception {

		SyncDataUtil.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/scripts/testscript").param("keyindex", "abcd")).andReturn(),
				"KER-SNC-155");

	}

	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void t020downloadEntityDataTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/abcd").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
				.andExpect(status().is(500));		
	

	}

	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t022downloadEntityDataTest() throws Exception {

		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/1").param("keyindex", "abcd")).andReturn(),
				"KER-SNC-155");

	}
	
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t023syncClientSettingsV2Test() throws Exception {
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:44:42.885Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"locationHierarchyLevels\": [\r\n" + 
				"      {\r\n" + 
				"        \"hierarchyLevel\": 0,\r\n" + 
				"        \"hierarchyLevelName\": \"country\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": true\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		String str="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"pageNo\": 0,\r\n" + 
				"    \"totalPages\": 0,\r\n" + 
				"    \"totalItems\": 0,\r\n" + 
				"    \"data\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"string\",\r\n" + 
				"        \"name\": \"string\",\r\n" + 
				"        \"langCode\": \"string\",\r\n" + 
				"        \"dataType\": \"string\",\r\n" + 
				"        \"description\": \"string\",\r\n" + 
				"        \"fieldVal\": [\r\n" + 
				"          {\r\n" + 
				"            \"array\": true,\r\n" + 
				"            \"null\": true,\r\n" + 
				"            \"float\": true,\r\n" + 
				"            \"number\": true,\r\n" + 
				"            \"valueNode\": true,\r\n" + 
				"            \"containerNode\": true,\r\n" + 
				"            \"missingNode\": true,\r\n" + 
				"            \"object\": true,\r\n" + 
				"            \"nodeType\": \"ARRAY\",\r\n" + 
				"            \"pojo\": true,\r\n" + 
				"            \"integralNumber\": true,\r\n" + 
				"            \"floatingPointNumber\": true,\r\n" + 
				"            \"short\": true,\r\n" + 
				"            \"int\": true,\r\n" + 
				"            \"long\": true,\r\n" + 
				"            \"double\": true,\r\n" + 
				"            \"bigDecimal\": true,\r\n" + 
				"            \"bigInteger\": true,\r\n" + 
				"            \"textual\": true,\r\n" + 
				"            \"boolean\": true,\r\n" + 
				"            \"binary\": true\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"isActive\": true,\r\n" + 
				"        \"createdBy\": \"string\",\r\n" + 
				"        \"updatedBy\": \"string\",\r\n" + 
				"        \"createdOn\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"        \"updatedOn\": \"2021-12-10T05:59:29.437Z\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		List<DynamicFieldDto> lstd=new ArrayList<>();
		DynamicFieldDto dto=new DynamicFieldDto();
		dto.setDataType("string");
		dto.setIsDeleted(false);
		dto.setLangCode("eng");
		dto.setName("blood type");
		
		lstd.add(dto);
		
		CompletableFuture<List<DynamicFieldDto>> c=new CompletableFuture<>();
		c.completedFuture(lstd);
		
		
		SyncMasterDataServiceHelper helper=Mockito.spy(SyncMasterDataServiceHelper.class);
		
		Mockito.doReturn(c).when(helper).getAllDynamicFields(Mockito.any());
		
		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex", "41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29").param("regcenterId", "10002")).andReturn(),
				"KER-SNC-149");

	}
	
	
/*	@Test
	@WithUserDetails(value = "reg-officer")
	public void t023syncClientSettingsV2Test1() throws Exception {
		String str="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:44:42.885Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"locationHierarchyLevels\": [\r\n" + 
				"      {\r\n" + 
				"        \"hierarchyLevel\": 0,\r\n" + 
				"        \"hierarchyLevelName\": \"country\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": true\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"pageNo\": 0,\r\n" + 
				"    \"totalPages\": 0,\r\n" + 
				"    \"totalItems\": 0,\r\n" + 
				"    \"data\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"string\",\r\n" + 
				"        \"name\": \"string\",\r\n" + 
				"        \"langCode\": \"string\",\r\n" + 
				"        \"dataType\": \"string\",\r\n" + 
				"        \"description\": \"string\",\r\n" + 
				"        \"fieldVal\": [\r\n" + 
				"          {\r\n" + 
				"            \"array\": true,\r\n" + 
				"            \"null\": true,\r\n" + 
				"            \"float\": true,\r\n" + 
				"            \"number\": true,\r\n" + 
				"            \"valueNode\": true,\r\n" + 
				"            \"containerNode\": true,\r\n" + 
				"            \"missingNode\": true,\r\n" + 
				"            \"object\": true,\r\n" + 
				"            \"nodeType\": \"ARRAY\",\r\n" + 
				"            \"pojo\": true,\r\n" + 
				"            \"integralNumber\": true,\r\n" + 
				"            \"floatingPointNumber\": true,\r\n" + 
				"            \"short\": true,\r\n" + 
				"            \"int\": true,\r\n" + 
				"            \"long\": true,\r\n" + 
				"            \"double\": true,\r\n" + 
				"            \"bigDecimal\": true,\r\n" + 
				"            \"bigInteger\": true,\r\n" + 
				"            \"textual\": true,\r\n" + 
				"            \"boolean\": true,\r\n" + 
				"            \"binary\": true\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"isActive\": true,\r\n" + 
				"        \"createdBy\": \"string\",\r\n" + 
				"        \"updatedBy\": \"string\",\r\n" + 
				"        \"createdOn\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"        \"updatedOn\": \"2021-12-10T05:59:29.437Z\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		
		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
	
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		
		UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(String.format("http://localhost:8086/v1/masterdata/dynamicfields?pageNumber=0"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder1.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
			
			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));
	
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
				.param("regcenterId", "10002")).andExpect(status().is(200));

	
	}*/
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t023syncClientSettingsV2Test2() throws Exception {
		String str="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:44:42.885Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"locationHierarchyLevels\": [\r\n" + 
				"      {\r\n" + 
				"        \"hierarchyLevel\": 0,\r\n" + 
				"        \"hierarchyLevelName\": \"country\",\r\n" + 
				"        \"langCode\": \"eng\",\r\n" + 
				"        \"isActive\": true\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 

				"  ]\r\n" + 
				"}";
		
		
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"pageNo\": 0,\r\n" + 
				"    \"totalPages\": 0,\r\n" + 
				"    \"totalItems\": 0,\r\n" + 
				"    \"data\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"string\",\r\n" + 
				"        \"name\": \"string\",\r\n" + 
				"        \"langCode\": \"string\",\r\n" + 
				"        \"dataType\": \"string\",\r\n" + 
				"        \"description\": \"string\",\r\n" + 
				"        \"fieldVal\": [\r\n" + 
				"          {\r\n" + 
				"            \"array\": true,\r\n" + 
				"            \"null\": true,\r\n" + 
				"            \"float\": true,\r\n" + 
				"            \"number\": true,\r\n" + 
				"            \"valueNode\": true,\r\n" + 
				"            \"containerNode\": true,\r\n" + 
				"            \"missingNode\": true,\r\n" + 
				"            \"object\": true,\r\n" + 
				"            \"nodeType\": \"ARRAY\",\r\n" + 
				"            \"pojo\": true,\r\n" + 
				"            \"integralNumber\": true,\r\n" + 
				"            \"floatingPointNumber\": true,\r\n" + 
				"            \"short\": true,\r\n" + 
				"            \"int\": true,\r\n" + 
				"            \"long\": true,\r\n" + 
				"            \"double\": true,\r\n" + 
				"            \"bigDecimal\": true,\r\n" + 
				"            \"bigInteger\": true,\r\n" + 
				"            \"textual\": true,\r\n" + 
				"            \"boolean\": true,\r\n" + 
				"            \"binary\": true\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"isActive\": true,\r\n" + 
				"        \"createdBy\": \"string\",\r\n" + 
				"        \"updatedBy\": \"string\",\r\n" + 
				"        \"createdOn\": \"2021-12-10T05:59:29.437Z\",\r\n" + 
				"        \"updatedOn\": \"2021-12-10T05:59:29.437Z\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		
		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}"; 
		
	
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		
		UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(String.format("http://localhost:8086/v1/masterdata/dynamicfields?pageNumber=0"));
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder1.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
			
			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));
	
		
	/*	SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
				.param("regcenterId", "10002")).andReturn(), null);*/

			mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
					"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
					.param("regcenterId", "10002")).andExpect(status().is(200));

	}
	
/*	@Test
	@WithUserDetails(value = "reg-officer")
	public void t024downloadEntityDataTest() throws Exception {

		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/1").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")).andReturn(),
				null);

	}
	*/

}
