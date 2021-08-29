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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.IdTypeDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IdTypeControllerTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	RequestWrapper<IdTypeDto> idTypeRequestDto;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		idTypeRequestDto = new RequestWrapper<IdTypeDto>();

		IdTypeDto id = new IdTypeDto();

		id.setCode("RID");
		id.setDescr("ID assigned after registration");
		id.setIsActive(true);
		id.setLangCode("eng");
		id.setName("RID");

		idTypeRequestDto.setRequest(id);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t3getIdTypesByLanguageCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idtypes/eng")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t1getIdTypesByLanguageCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/idtypes/eng1")).andReturn(),
				"KER-MSD-022");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t2createIdTypeTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/idtypes").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idTypeRequestDto))).andReturn(),
				"KER-MSD-059");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t2createIdTypeTest1() throws Exception {
		idTypeRequestDto.getRequest().setCode("RIDDD");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/idtypes").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idTypeRequestDto))).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t4createIdTypeFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/idtypes").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idTypeRequestDto))).andReturn(),
				"KER-MSD-059");

	}

}
