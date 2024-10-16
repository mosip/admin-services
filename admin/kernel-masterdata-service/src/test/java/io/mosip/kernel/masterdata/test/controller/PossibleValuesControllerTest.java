package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PossibleValuesControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng")).andReturn(),
				null);
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType2").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest6() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType2").param("langCode", "ara")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest7() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bl").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest5() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/country1").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest9() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType1").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode","")).andReturn(),
				null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest3() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng,ara")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getAllValuesOfFieldTest4() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng,ara")).andReturn(),
				null);
	}
}
