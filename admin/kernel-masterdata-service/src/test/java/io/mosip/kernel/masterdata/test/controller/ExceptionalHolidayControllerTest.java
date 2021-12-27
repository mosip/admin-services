package io.mosip.kernel.masterdata.test.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExceptionalHolidayControllerTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;


	@Test
	@WithUserDetails("global-admin")
	public void t1getExceptionalHolidaysFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10005/eng"))
				.andReturn(),"KER-MSD-802");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t2getExceptionalHolidaysTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001/eng"))
				.andReturn(),null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t3getExceptionalHolidaysFailTest() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001/eng1")).andReturn(),"KER-MSD-999");
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t3getExceptionalHolidaysFailTest1() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10004/eng")).andReturn(),"KER-EHD-002");
		
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t4getExceptionalHolidaysTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001"))
				.andReturn(),null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t5getExceptionalHolidaysTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10078"))
				.andReturn(),"KER-MSD-802");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t5getExceptionalHolidaysTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001"))
				.andReturn(),null);
	}

}
