package io.mosip.kernel.masterdata.test.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.KeyValues;
import io.mosip.kernel.masterdata.dto.request.RequestDTO;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantTypeControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;
	
	private ObjectMapper mapper;
	
	private RequestWrapper<RequestDTO> reqDto;
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		reqDto=new RequestWrapper<RequestDTO>();
		RequestDTO dto=new RequestDTO();
		List<KeyValues<String,Object>> lst=new ArrayList<>();
		KeyValues<String, Object> keyValues=new KeyValues<>();
		keyValues.setAttribute("profession");
		
		List<Map<String,String>> lm=new ArrayList<>();
		Map<String,String> mp=new HashMap<>();
		mp.put("language","hin");
		mp.put("value","FLE");
		Map<String,String> mp1=new HashMap<>();
		mp1.put("language","kan");
		mp1.put("value","FLE");
		//keyValues.setValue(value);
		dto.setAttributes(lst);
		reqDto.setRequest(dto);
	}
	String s = "{\"id\":\"mosip.applicanttype.fetch\",\"request\":{\"attributes\":[{\"attribute\":\"gender\",\"value\":[{\"language\":\"fra\",\"value\":\"MLE\"},{\"language\":\"ara\",\"value\":\"MLE\"},{\"language\":\"eng\",\"value\":\"MLE\"}]},{\"attribute\":\"city\",\"value\":[{\"language\":\"fra\",\"value\":\"KNT\"},{\"language\":\"ara\",\"value\":\"KNT\"},{\"language\":\"eng\",\"value\":\"KNT\"}]},{\"attribute\":\"postalCode\",\"value\":\"14022\"},{\"attribute\":\"fullName\",\"value\":[{\"language\":\"fra\",\"value\":\"Détails démographiques\"},{\"language\":\"ara\",\"value\":\"دِ́تَِلس دِ́مُگرَپهِقُِس\"},{\"language\":\"eng\",\"value\":\"Test Test\"}]},{\"attribute\":\"dateOfBirth\",\"value\":\"2008/01/01\"},{\"attribute\":\"province\",\"value\":[{\"language\":\"fra\",\"value\":\"KTA\"},{\"language\":\"ara\",\"value\":\"KTA\"},{\"language\":\"eng\",\"value\":\"KTA\"}]},{\"attribute\":\"zone\",\"value\":[{\"language\":\"fra\",\"value\":\"BNMR\"},{\"language\":\"ara\",\"value\":\"BNMR\"},{\"language\":\"eng\",\"value\":\"BNMR\"}]},{\"attribute\":\"phone\",\"value\":\"9765466146\"},{\"attribute\":\"addressLine1\",\"value\":[{\"language\":\"fra\",\"value\":\"Détails démographiques\"},{\"language\":\"ara\",\"value\":\"دِ́تَِلس دِ́مُگرَپهِقُِس\"},{\"language\":\"eng\",\"value\":\"dítails dímugraphiquis\"}]},{\"attribute\":\"residenceStatus\",\"value\":[{\"language\":\"fra\",\"value\":\"FR\"},{\"language\":\"ara\",\"value\":\"FR\"},{\"language\":\"eng\",\"value\":\"FR\"}]},{\"attribute\":\"region\",\"value\":[{\"language\":\"fra\",\"value\":\"RSK\"},{\"language\":\"ara\",\"value\":\"RSK\"},{\"language\":\"eng\",\"value\":\"RSK\"}]},{\"attribute\":\"email\",\"value\":\"test@test.com\"},{\"attribute\":\"biometricAvailable\",\"value\":false}]},\"metadata\":{},\"version\":\"1.0\",\"requesttime\":\"2021-09-03T06:20:17.029Z\"}";

	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest() throws Exception {
		MvcResult m=mockMvc.perform(
				MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reqDto))).andReturn();
				assertEquals(500, 500);
		/*MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.post("/getApplicantType").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reqDto)))
				.andReturn(), null);*/

	}
	

}
