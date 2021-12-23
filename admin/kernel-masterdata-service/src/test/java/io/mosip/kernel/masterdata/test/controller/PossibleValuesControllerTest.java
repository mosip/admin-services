package io.mosip.kernel.masterdata.test.controller;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.repository.LocationHierarchyRepository;
import io.mosip.kernel.masterdata.repository.LocationRepository;
import io.mosip.kernel.masterdata.service.PossibleValuesService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;

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
	public void t001createMachineSpecificationTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng")).andReturn(),
				null);
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType2").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest6() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType2").param("langCode", "ara")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest7() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bl").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest5() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/country1").param("langCode", "eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "")).andReturn(),
				null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest3() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng,ara")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest4() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country").param("langCode", "eng,ara")).andReturn(),
				null);
	}
}
