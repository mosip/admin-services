package io.mosip.kernel.syncdata.test.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.dto.DynamicFieldDto;
import io.mosip.kernel.syncdata.dto.PageDto;
import io.mosip.kernel.syncdata.dto.UploadPublicKeyRequestDto;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
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

	@Value("${mosip.kernel.masterdata.locationhierarchylevels.uri}")
	private String locationHirerarchyUrl;

	@Value("${mosip.kernel.syncdata-service-dynamicfield-url}")
	private String dynamicfieldUrl;

	@Value("${mosip.kernel.keymanager-service-sign-url}")
	private String signUrl;
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	private ObjectMapper objectMapper;
	
	MockRestServiceServer mockRestServiceServer;
	
	private String str1 = "{\n" + 
			"  \"id\": null,\n" + 
			"  \"version\": null,\n" + 
			"  \"responsetime\": \"2022-08-11T15:14:28.976Z\",\n" + 
			"  \"metadata\": null,\n" + 
			"  \"response\": {\n" + 
			"    \"pageNo\": 0,\n" + 
			"    \"totalPages\": 3,\n" + 
			"    \"totalItems\": 21,\n" + 
			"    \"data\": [\n" + 
			"      {\n" + 
			"        \"id\": \"1029\",\n" + 
			"        \"name\": \"bloodType\",\n" + 
			"        \"langCode\": \"ara\",\n" + 
			"        \"dataType\": \"string\",\n" + 
			"        \"description\": \"فصيلة الدم\",\n" + 
			"        \"fieldVal\": [\n" + 
			"          {\n" + 
			"            \"code\": \"101\",\n" + 
			"            \"value\": \"A\"\n" + 
			"          }\n" + 
			"        ],\n" + 
			"        \"isActive\": true,\n" + 
			"        \"createdBy\": \"110006\",\n" + 
			"        \"updatedBy\": null,\n" + 
			"        \"createdOn\": \"2022-04-25T11:18:43.776Z\",\n" + 
			"        \"updatedOn\": null\n" + 
			"      }\n" + 
			"    ]\n" + 
			"  },\n" + 
			"  \"errors\": null\n" + 
			"}";

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

		 mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void givenNoKeyIndex_whenSyncClientSettings_thenErrorResponse() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
							"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")
					.param("regcenterId", "10001")).andReturn(), "KER-SNC-141");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_successfulJwtSigning() {
		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
	.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

		assertNotNull(uploadPublicKeyRequestDto);

	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_withKeyIndex() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
							"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:49")
					.param("regcenterId", "10001")).andReturn(), "KER-SNC-149");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_successfulDataRetrieval_shouldProcessData() {
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

		String str3="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format("https://dev.mosip.net/v1/masterdata/locationHierarchyLevels"));

		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));

		UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(String.format("http://localhost:8086/v1/masterdata/dynamicfields?pageNumber=0"));

		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder1.build().toString()))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));


			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_successfulDataRetrieval_withDynamicFields_shouldProcessData() {
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
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_invalidKeyIndex_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
							"abcd")
					.param("regcenterId", "10001")).andReturn(), "KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_successfulPublicKeyRetrieval_shouldProcessData() {

		String str="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"lastSyncTime\":null,\"publicKey\":\"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48\",\"issuedAt\":null,\"expiryAt\":null,\"profile\":\"local\"},\"errors\":[]}";
		mockRestServiceServer.expect(requestTo("https://dev.mosip.io/v1/keymanager/publickey/REGISTRATION?referenceId=10001&timeStamp=2019-09-09T09%253A00%253A00.000Z"))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettings_getPublicKey_missingReferenceId_shouldHandleError() {

		String str="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"lastSyncTime\":null,\"publicKey\":\"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48\",\"issuedAt\":null,\"expiryAt\":null,\"profile\":\"local\"},\"errors\":[]}";
		mockRestServiceServer.expect(requestTo("https://dev.mosip.io/v1/keymanager/publickey/REGISTRATION?referenceId&timeStamp=2019-09-09T09%253A00%253A00.000Z"))
		.andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testValidateKeyMachineMapping_invalidMachineName_shouldReturnError() {
		try {
			uploadPublicKeyRequestDto.getRequest().setMachineName("abc");
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
							.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
					.andReturn(), "KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testValidateKeyMachineMapping_successfulJwtSigning_shouldVerifyMapping() {
		try {
			String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
					.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
							.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
					.andReturn(), null);
		} catch (Exception e){
			e.printStackTrace();
		}
	}



	@Test
	@WithUserDetails(value = "reg-officer")
	public void testValidateKeyMachineMapping_missingPublicKey_shouldReturnError() {
		try {
			uploadPublicKeyRequestDto.getRequest().setPublicKey(null);
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/tpm/publickey/verify")
							.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(uploadPublicKeyRequestDto)))
					.andReturn(), "KER-SNC-158");
		} catch (Exception e){
			e.printStackTrace();
		}


	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetLatestPublishedIdSchema_successfulRetrieval_shouldProcessData() {
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
		mockRestServiceServer.expect(requestTo(idSchemaUrl+"?schemaVersion=0.0&domain=registration-client&type=schema"))
				.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetLatestPublishedIdSchema_invalidSchemaResponse_shouldHandleError() {
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


		mockRestServiceServer.expect(requestTo(idSchemaUrl+"?schemaVersion=0.0&domain=registration-client&type=schema"))
				.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));

		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetLatestPublishedIdSchema_withoutDomainAndType_shouldUseClientVersion() {
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

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetCertificate_successfulRetrieval_shouldContainExpectedFields() {

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

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetClientPublicKey_successfulRetrieval_shouldContainPublicKey() {
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
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "10"));

			mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
					.andRespond(withSuccess().body(bd).contentType(MediaType.APPLICATION_JSON));

			String str1 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
					.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/tpm/publickey/10")).andReturn(), null);
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetClientPublicKey_invalidMachineId_shouldReturnError() {
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
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "1000"));

			mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
					.andRespond(withSuccess().body(
							"{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, "
									+ "\"response\": "+bd+", \"errors\": null }"));
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/tpm/publickey/1000")).andReturn(),
				"KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetMachineConfigDetails_shouldRetrieveRegistrationAndApplicationProperties() {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}";

			mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/registration-test.properties"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
		
		mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/application-test.properties"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetMachineConfigDetails_shouldRetrieveProperties_withoutJwtSigning() {
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
	}
	
	

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetMachineConfigDetails_invalidResponse_shouldReturnError() {
		try {
			String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}";

			mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/application-test.properties"))
					.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/configs/abcd")).andReturn(), "KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetUserDetailsBasedOnKeyIndex_invalidKeyIndex_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get(
							"/userdetails").param("keyindex","B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
					.andReturn(), "KER-SNC-141");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetUserDetailsBasedOnKeyIndex_validKeyIndex_shouldReturnUserDetails() {
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
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetUserDetailsBasedOnKeyIndex_invalidKeyIndex_thenReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/userdetails").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:50")).andReturn(),"KER-SNC-303");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetCACertificates_shouldRetrieveCertificates() {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetCACertificates_invalidLastUpdated_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(
							MockMvcRequestBuilders.get("/getcacertificates").param("lastupdated", "2018-12-12 11:42:52.994"))
					.andReturn(), "KER_SNC-100");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetCACertificates_invalidLastUpdated_futureDate_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc.perform(
							MockMvcRequestBuilders.get("/getcacertificates").param("lastupdated", "2050-12-29T13:03:44.719Z"))
					.andReturn(), "KER-SNC-135");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void getCACertificates_shouldRetrieveCertificates() {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		assertNotNull(str1);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testDownloadScript_shouldDownloadScript() {
		String str1="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"responseData\":\"signed\"},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("/localhost/kernel-syncdata-service/test/0.9.0/abcd"))
		.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));

		String res="{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
		.andRespond(withSuccess().body(res).contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testDownloadScript_invalidKeyIndex_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(mockMvc
							.perform(MockMvcRequestBuilders.get("/scripts/testscript").param("keyindex", "abcd")).andReturn(),
					"KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}



	@Test
	@WithUserDetails(value = "reg-officer")
	public void testDownloadEntityData_invalidKeyIndex_shouldReturnError() {
		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/abcd").param("keyindex",
							"B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48"))
					.andExpect(status().is(500));
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void testDownloadEntityData_invalidEntityId_shouldReturnError() {
		try {
			SyncDataUtil.checkResponse(
					mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/1").param("keyindex", "abcd")).andReturn(),
					"KER-SNC-155");
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	@Test
	@WithUserDetails(value = "reg-officer")
	public void testSyncClientSettingsV2_shouldSyncData() {
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

		lenient().doReturn(c).when(helper).getAllDynamicFields(Mockito.any());

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testV2GetUserDetails_shouldRetrieveDetails() {
		String signResponse = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(signResponse).contentType(MediaType.APPLICATION_JSON));

		assertNotNull(signResponse);
	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void testGetClientPublicKey_shouldReturnErrorForInvalidMachineId() {
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
				"    ]}," +
				"\"errors\":[{\"errorCode\":\"KER-SNC-102\",\"errorMessage\":\"error\"}]"
				+ "}";
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, "10"));

			mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(builder.build().toString()))
					.andRespond(withSuccess().body(bd).contentType(MediaType.APPLICATION_JSON));

			String str1 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

			mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
					.andRespond(withSuccess().body(str1).contentType(MediaType.APPLICATION_JSON));
			mockMvc.perform(MockMvcRequestBuilders.get("/tpm/publickey/10")).andExpect(status().is(500));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testDynamicField_shouldReturnValidData() {
		try {
			ResponseWrapper<PageDto<DynamicFieldDto>> resp = objectMapper.readValue(str1,
					new TypeReference<>() {});
			PageDto<DynamicFieldDto> dynamicFields = resp.getResponse();
			Assert.assertTrue(dynamicFields.getData().size() == 1);
			Assert.assertTrue(dynamicFields.getData().getFirst().getFieldVal().size() == 1);
			Assert.assertTrue(dynamicFields.getData().getFirst().getFieldVal().getFirst().isActive());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
