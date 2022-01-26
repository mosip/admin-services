package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

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
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationCenterUserMachineHistoryControllerTest {

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
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getRegistrationCentersMachineUserMappingTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10001/10001/1"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getRegistrationCentersMachineUserMappingTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10001/10001/4"))
				.andReturn(),"KER-MSD-038");

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t002getRegistrationCentersMachineUserMappingFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/17-12-2018T07:22:22.233Z/10001/1000/1"))
				.andReturn(), "KER-MSD-043");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getRegistrationCentersMachineUserMappingFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders
						.get("/getregistrationmachineusermappinghistory/2024-11-08T16:17:14.811Z/10001/1001/1"))
				.andReturn(),"KER-MSD-038");

	}

}
