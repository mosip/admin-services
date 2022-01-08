package io.mosip.admin.packetstatusupdater;


import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;


@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG, printOnlyOnFailure = false)
public class PacketStatusIntegrationTest {

	@Autowired
	private RestTemplate restTemplate;
	
	private MockRestServiceServer mockRestServiceServer;
	
	/** The packet update status url. */
	@Value("${mosip.kernel.packet-status-update-url}")
	private String packetUpdateStatusUrl;

	/** The zone validation url. */
	@Value("${mosip.kernel.zone-validation-url}")
	private String zoneValidationUrl;
	
	@Value("${KEYBASEDTOKENAPI}")
	private String tokenUrl;
	
	@Value("${mosip.mandatory-languages}")
	private String mandatoryLang;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AuditUtil auditUtil;
	
	private List<ServiceError> validationErrorList=null;

	@Autowired
	private MockMvc mockMvc;
	
	private String POSITIVE_RESPONSE_REG_PROC="{\r\n  \"id\": \"mosip.registration.transaction\",\r\n  \"version\": \"1.0\",\r\n  \"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n  \"response\": [\r\n    {\r\n      \"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": null,\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet has reached Packet Receiver\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n    },\r\n    {\r\n      \"id\": \"7aa593d7-8f1e-413a-abca-34b752caa795\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet is Uploaded to Landing Zone\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:08.038\"\r\n    }\r\n  ],\r\n  \"errors\": null\r\n}";
	
	private String PARSE_ERROR_RESPONSE_REG_PROC="{\r\n    \"id\": \"mosip.registration.transaction\",\r\n    \"version\": \"1.0\",\r\n    \"responsetime\": \"2019-12-03T05:21:57.024Z\",\r\n    \"response\": [\r\n        {\r\n            \"id\": \"96939b0b-982f-431c-abe0-489cf4ca9734\",\r\n            \"registrationId\": \"10001100010000120190722123617\",\r\n            \"transactionTypeCode\": \"DEMOGRAPHIC_VERIFICATION\",\r\n            \"parentTransactionId\": \"85ed38a6-3fcc-42e7-9afd-366d424c93f7\",\r\n            \"statusCode\": \"IN_PROGRESS\",\r\n            \"statusComment\": null,\r\n            \"createdDateTimes\": \"2019-08-07T15:58:17.204\"\r\n        }\r\n\t],\t\r\n\t\t\t\r\n\t\t\t\"errors\":null\r\n\t\t\t";
	
	private String POSITIVE_RESPONSE_ZONE_VALIATION="{\r\n    \"id\": null,\r\n    \"version\": null,\r\n    \"responsetime\": \"2019-12-02T09:45:24.512Z\",\r\n    \"metadata\": null,\r\n    \"response\": true,\r\n    \"errors\": []\r\n}";
	@Before
	public void setUp() {
		
		ServiceError serviceError= new ServiceError();
		serviceError.setErrorCode("KER-MSD-403");
		serviceError.setMessage("Forbidden");
		validationErrorList= new ArrayList<ServiceError>();
		validationErrorList.add(serviceError);

		//doNothing().when(auditUtil).setAuditRequestDto(EventEnum.ACCESS_DENIED);
		//mockRestServiceServer=MockRestServiceServer.bindTo(restTemplate).build();
		mockRestServiceServer=MockRestServiceServer.createServer(restTemplate);

	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate() throws Exception {
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(POSITIVE_RESPONSE_REG_PROC));
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid", "1000012232223243224234"))

				.andExpect(status().isOk());

	}
	
	/*@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate401Excption() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withUnauthorizedRequest());
		
		
		MvcResult mvcResult = mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().isOk()).andReturn();

		ResponseWrapper<PacketStatusUpdateResponseDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ResponseWrapper<PacketStatusUpdateResponseDto>>(){});
		Assert.assertNull(response.getResponse());
	}
	
	@Test
	@Ignore
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate403Excption() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withStatus(HttpStatus.FORBIDDEN));


		MvcResult mvcResult = mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().isOk()).andReturn();

		ResponseWrapper<PacketStatusUpdateResponseDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ResponseWrapper<PacketStatusUpdateResponseDto>>(){});
		Assert.assertNull(response.getResponse());
	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate401ExcptionValidationError() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withUnauthorizedRequest().body(objectMapper.writeValueAsString(validatationResponse)));


		MvcResult mvcResult = mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().isOk()).andReturn();

		ResponseWrapper<PacketStatusUpdateResponseDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ResponseWrapper<PacketStatusUpdateResponseDto>>(){});
		Assert.assertNull(response.getResponse());
	}*/
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate403ExcptionValidateionError() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(POSITIVE_RESPONSE_ZONE_VALIATION));
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withStatus(HttpStatus.FORBIDDEN).body(objectMapper.writeValueAsString(validatationResponse)));
		
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().is5xxServerError());
		
		
	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdate500ExcptionValidateionError() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(POSITIVE_RESPONSE_ZONE_VALIATION));
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl+ "/1000012232223243224234"))
		.andRespond(withBadRequest());
		
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().is5xxServerError());
		
		
	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdateValidationError() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(POSITIVE_RESPONSE_ZONE_VALIATION));
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withStatus(HttpStatus.FORBIDDEN).body(objectMapper.writeValueAsString(validatationResponse)));
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().isInternalServerError());
		
		
	}
	
	
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdateParseException() throws Exception {
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(PARSE_ERROR_RESPONSE_REG_PROC));

		MvcResult mvcResult = mockMvc.perform(get("/packetstatusupdate")
				.param("rid","1000012232223243224234")).andExpect(status().is5xxServerError()).andReturn();

		ResponseWrapper<PacketStatusUpdateResponseDto> responseWrapper = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ResponseWrapper<PacketStatusUpdateResponseDto>>() {});
		Assert.assertNotNull(responseWrapper.getErrors());
		Assert.assertEquals("ADM-PKT-090", responseWrapper.getErrors().get(0).getErrorCode());
	}
	
/*	@Test
	@WithUserDetails("zonal-admin")
	public void t003lostRidTest() throws Exception {
		
		AdminDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/validatePacket").param("rid","1234").param("langCode", "")).andReturn(),
				null);

	}
	*/
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdateParseException1() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		ServiceError serviceError= new ServiceError();
		serviceError.setErrorCode("ADM-PKT-411");
		serviceError.setMessage("Registration id is not found");
		validationErrorList.clear();
		validationErrorList.add(serviceError);
		ResponseWrapper<?> validatationResponse= new ResponseWrapper<>();
		validatationResponse.setErrors(validationErrorList);
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": true,\r\n" + 
				"  \"errors\": [{\"errorCode\":\"ADM-PKT-411\",\"message\":\"Registration id is not found\"}]\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(str1));
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(validatationResponse)));
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234").param("lang", "eng")).andExpect(status().is(500));
			
		
	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdateParseException2() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\":[ {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}],\r\n" + 
				"	\"errors\": []\r\n" + 
				"}";
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": true,\r\n" + 
				"  \"errors\": [{\"errorCode\":\"ADM-PKT-407\",\"message\":\"Centre id extracted from registration id does not exists\"}]\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(str1));
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str)));
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234").param("lang", "eng")).andExpect(status().is(500));
			
	}
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatusUpdateParseException3() throws Exception {

		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\":[ {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}],\r\n" + 
				"	\"errors\": []\r\n" + 
				"}";
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": true,\r\n" + 
				"  \"errors\": [{\"errorCode\":\"ADM-PKT-001\",\"message\":\"Admin is not authorized\"}]\r\n" + 
				"}";
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str)));

		MvcResult mvcResult = mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andReturn();
		ResponseWrapper<PacketStatusUpdateResponseDto> responseWrapper = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ResponseWrapper<PacketStatusUpdateResponseDto>>() {});
		Assert.assertNotNull(responseWrapper.getErrors());
		Assert.assertEquals("ADM-PKT-090", responseWrapper.getErrors().get(0).getErrorCode());
		
	}
	
	/*@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatus4() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\":[ {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}],\r\n" + 
				"	\"errors\": []\r\n" + 
				"}";
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": true,\r\n" + 
				"  \"errors\": null\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(str1));
		mockRestServiceServer
				.expect(requestTo("<https://dev.mosip.io/registrationprocessor/v1/registrationtransaction/search/eng/1000012232223243224234>"))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str)));
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234").param("lang", "eng")).andExpect(status().isOk());
		
		
	}*/
	
	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatus6() throws Exception {

		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\":[ {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" +
				"\"subStatusCode\": \"RPR-PKR-SUCCESS-001\",\r\n" +
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709Z\"\r\n" +
				"		}],\r\n" + 
				"	\"errors\": []\r\n" + 
				"}";
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": false,\r\n" + 
				"  \"errors\": null\r\n" + 
				"}";
		mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl + "/1000012232223243224234"))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str)));
		
		mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234")).andExpect(status().is5xxServerError());
		
		
	}
	
/*	@Test
	@WithUserDetails("zonal-admin")
	public void testPacketStatus5() throws Exception {
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(zoneValidationUrl).queryParam("rid",
				"1000012232223243224234");
		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\": {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}"
				+"}";
		String str1="{\r\n" + 
				"  \"id\": null,\r\n" + 
				"  \"version\": null,\r\n" + 
				"  \"responsetime\": \"2020-07-08T06:08:26.654Z\",\r\n" + 
				"  \"metadata\": null,\r\n" + 
				"  \"response\": true,\r\n" + 
				"  \"errors\": null\r\n" + 
				"}";
		mockRestServiceServer.expect(requestTo(uribuilder.toUriString())).andRespond(withSuccess().body(str1));
		mockRestServiceServer
				.expect(requestTo("<https://dev.mosip.io/registrationprocessor/v1/registrationsaction/search/eng/1000012232223243224234>"))
		.andRespond(withBadRequest().body(objectMapper.writeValueAsString(str)));
		AdminDataUtil.checkResponse(mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234").param("langCode", "eng")).andReturn(),null);
		
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testPacketStatus7() throws Exception {
		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\": {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}"
				+"}";
		
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-16T12:13:19.302Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"status\": \"string\",\r\n" + 
				"    \"message\": \"string\"\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
		
				"  ]\r\n" + 
				"}";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Cookie", "");
		//Mockito.when(restTemplate.exchange("https://dev.mosip.io/registrationprocessor/v1/registrationsaction/search/1111", HttpMethod.GET,new  HttpEntity<Object>(headers), String.class)).thenReturn(new ResponseEntity<String>(str,HttpStatus.OK));
		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/authmanager/authenticate/clientidsecretkey")).andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).andExpect(MockRestRequestMatchers.header("Cookie", ""))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str1)));
	
			mockRestServiceServer.expect(requestTo("https://dev.mosip.io/registrationprocessor/v1/registrationsaction/search/1111")).andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).andExpect(MockRestRequestMatchers.header("Cookie", ""))
		.andRespond(withSuccess().body(objectMapper.writeValueAsString(str)));
		AdminDataUtil.checkResponse(mockMvc.perform(
				get("/packetstatusupdate").param("rid","1111").param("langCode", "eng")).andReturn(),"ADM-PKT-090");
		
		
	}*/
	
/*	@Test
	@WithUserDetails("global-admin")
	public void testPacketStatus8() throws Exception {
		String str="{\r\n" + 
				"	\"id\": \"mosip.registration.transaction\",\r\n" + 
				"	\"version\": \"1.0\",\r\n" + 
				"	\"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n" + 
				"	\"response\": {\r\n" + 
				"			\"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n" + 
				"			\"registrationId\": \"10002100320002420191210085947\",\r\n" + 
				"			\"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n" + 
				"			\"parentTransactionId\": null,\r\n" + 
				"			\"statusCode\": \"SUCCESS\",\r\n" + 
				"			\"statusComment\": \"Packet has reached Packet Receiver\",\r\n" + 
				"			\"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n" + 
				"		}"
				+"}";
		String str1="{\r\n" + 
				"  \"id\": \"string\",\r\n" + 
				"  \"version\": \"string\",\r\n" + 
				"  \"responsetime\": \"2021-12-16T11:58:04.766Z\",\r\n" + 
				"  \"metadata\": {},\r\n" + 
				"  \"response\": {\r\n" + 
				"    \"status\": \"success\",\r\n" + 
				"    \"message\": \"token\"\r\n" + 
				"  },\r\n" + 
				"  \"errors\": [\r\n" + 
				"  ]\r\n" + 
				"}";
		//when(defaultHttpClient.execute(any())).thenReturn(str1);
	//	mockRestServiceServer
	//	.expect(requestTo("POST https://dev.mosip.net/v1/authmanager/authenticate/clientidsecretkey HTTP/1.1")).andRespond(withSuccess().body(str1));


mockRestServiceServer
				.expect(requestTo(packetUpdateStatusUrl+"/eng/1000012232223243224234>"))
		.andRespond(withBadRequest().body(objectMapper.writeValueAsString(str)));
		AdminDataUtil.checkResponse(mockMvc.perform(
				get("/packetstatusupdate").param("rid","1000012232223243224234").param("langCode", "eng")).andReturn(),null);
		
		
	}*/
	
	
}
