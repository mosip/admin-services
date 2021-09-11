package io.mosip.kernel.masterdata.test.controller;

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
public class LocationHierarchyControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	
	//@Autowired
	//public LocationHierarchyRepository  locationRepo;

	private ObjectMapper mapper;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		/*LocationHierarchy lh=new LocationHierarchy();
		lh.setCreatedBy("superAdmin");
		lh.setHierarchyLevel((short)0);
		lh.setHierarchyLevelName("Country");
		lh.setIsActive(true);
		lh.setLangCode("eng");
		lh.setCreatedDateTime(LocalDateTime.now(ZoneId.of("UTC")));		
		locationRepo.save(lh);*/
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getModuleLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getModuleLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels/eng11"))
				.andReturn(),"KER-MSD-398");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getLocationHierarchyLevelTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getLocationHierarchyLevelAndLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels/1/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005getLocationHierarchyLevelAndLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels/1/eng1")).andReturn(),"KER-MSD-398");
		
	}

}
