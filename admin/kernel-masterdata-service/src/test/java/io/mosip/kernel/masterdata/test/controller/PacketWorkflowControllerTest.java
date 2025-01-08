package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.PacketWorkflowResumeRequestDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.RestClient;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PacketWorkflowControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	
	@MockBean
	private RestClient  restClient;
	
	private ObjectMapper mapper;
	private RequestWrapper<PacketWorkflowResumeRequestDto> requestDto=new RequestWrapper<>();
	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		PacketWorkflowResumeRequestDto dto=new PacketWorkflowResumeRequestDto();
		dto.setWorkflowAction("pause");
		dto.setWorkflowId("1");
		requestDto.setRequest(dto);
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001resumePacketTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/packet/resume")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestDto))).andReturn(), "KER-MSD-364");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002searchPacketTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/packet/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDtoWithoutLangCode("createdDateTime","ASC", "process", "NEW", "equals")))).andReturn(), "KER-MSD-263");
	} 
	
	@Test
	@WithUserDetails("global-admin")
	public void t003searchPacketTest() throws Exception {
		 String res="{\r\n  \"id\": \"mosip.registration.transaction\",\r\n  \"version\": \"1.0\",\r\n  \"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n  \"response\": [\r\n    {\r\n      \"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": null,\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet has reached Packet Receiver\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n    },\r\n    {\r\n      \"id\": \"7aa593d7-8f1e-413a-abca-34b752caa795\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet is Uploaded to Landing Zone\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:08.038\"\r\n    }\r\n  ],\r\n  \"errors\": null\r\n}";

		try {
			when(restClient.postApi(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/packet/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDtoWithoutLangCode("createdDateTime","ASC", "process", "UPDATE", "contains")))).andReturn(), null);
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004resumePacketTest() throws Exception {
		 String res="{\r\n  \"id\": \"mosip.registration.transaction\",\r\n  \"version\": \"1.0\",\r\n  \"responsetime\": \"2019-12-11T09:45:45.544Z\",\r\n  \"response\": [\r\n    {\r\n      \"id\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": null,\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet has reached Packet Receiver\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:06.709\"\r\n    },\r\n    {\r\n      \"id\": \"7aa593d7-8f1e-413a-abca-34b752caa795\",\r\n      \"registrationId\": \"10002100320002420191210085947\",\r\n      \"transactionTypeCode\": \"PACKET_RECEIVER\",\r\n      \"parentTransactionId\": \"60c5f55d-8f22-48d0-8b55-edcd724417bc\",\r\n      \"statusCode\": \"SUCCESS\",\r\n      \"statusComment\": \"Packet is Uploaded to Landing Zone\",\r\n      \"createdDateTimes\": \"2019-12-10T09:05:08.038\"\r\n    }\r\n  ],\r\n  \"errors\": null\r\n}";

			try {
				when(restClient.postApi(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(res);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/packet/resume")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestDto))).andReturn(), null);
	}
}
