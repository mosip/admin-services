package io.mosip.kernel.masterdata.test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.masterdata.dto.KeyValues;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.request.RequestDTO;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getApplicantTypeTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reqDto))).andExpect(MockMvcResultMatchers.status().isOk());

	}

}
