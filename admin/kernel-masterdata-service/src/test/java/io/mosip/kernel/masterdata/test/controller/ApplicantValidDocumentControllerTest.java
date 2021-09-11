package io.mosip.kernel.masterdata.test.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantValidDocumentControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;
	private List<String> lst=new ArrayList();
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		lst.add("eng");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getApplicantValidDocumentTest() throws Exception {
		
		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/applicanttype/001/"+lst)).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getApplicantValidDocumentfailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/applicanttype/app/"+lst)).andReturn(), "KER-MSD-149");
	}

}
