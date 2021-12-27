package io.mosip.kernel.syncdata.test.controller;

import java.util.HashMap;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class WebsubCallbackControllerTest {
	
	
	
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;
	EventModel em=new EventModel();
	
	
	Event e=new Event();
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
	//	mapper.registerModule(new JavaTimeModule());
		em.setTopic("CA_CERTIFICATE_UPLOADED");
		
		 Map<String, Object> m=new HashMap<>();
		m.put("PARTNER_DOMAIN", "partnerDomain");
		m.put("CERTIFICATE_DATA_SHARE_URL", "certChainDatashareUrl");
		e.setData(m);
		em.setEvent(e);
	}
	
	
	@Test
	@WithUserDetails(value = "reg-officer")
	public void t001handleCACertificateTest() throws Exception {

		//SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/websub/callback/cacert").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(em))).andReturn(), null);

	}

}
