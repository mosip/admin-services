package io.mosip.kernel.masterdata.test.controller;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantValidDocumentControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;



	@Test
	@WithUserDetails("global-admin")
	public void t001getApplicantValidDocumentTest() throws Exception {
		
		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/applicanttype/001/languages?languages=eng&languages=fra&languages=ara")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getApplicantValidDocumentfailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/applicanttype/app/languages?languages=eng")).andReturn(), "KER-MSD-150");
	}

}
