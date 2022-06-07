package io.mosip.kernel.masterdata.test.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.ApplicationDto;
import io.mosip.kernel.masterdata.entity.Application;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;
	
	private RequestWrapper<ApplicationDto> applicationReq=new RequestWrapper<>();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
	
	}

	@Test
	@WithUserDetails("global-admin")
	public void t2createApplicationTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/applicationtypes").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:15:06.724Z\",\n" + "  \"request\": {\n"
								+ "    \"code\": \"101\",\n"
								+ "    \"description\": \"Pre-registration Application Form\",\n"
								+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\",\n"
								+ "    \"name\": \"pre-registeration\"\n" + "  }\n" + "}"))

				.andReturn(), null);
	}


	
	@Test
	@WithUserDetails("global-admin")
	public void t3getAllApplicationTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t4getAllApplicationByLanguageCodeTest() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/eng")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-Admin")
	public void t5getApplicationByCodeAndLanguageCodeTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/101/engk")).andReturn(), "KER-MSD-002");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t6createApplicationFailTest() throws Exception {
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/applicationtypes").contentType(MediaType.APPLICATION_JSON)
										.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
												+ "  \"requesttime\": \"2018-12-17T07:15:06.724Z\",\n"
												+ "  \"request\": {\n" + "    \"code\": \"\",\n"
												+ "    \"description\": \"Pre-registration Application Form\",\n"
												+ "    \"isActive\": true,\n" + "    \"langCode\": \"\",\n"
												+ "    \"name\": \"pre-registeration\"\n" + "  }\n" + "}"))
								.andReturn(),
						"KER-MSD-999");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t1getAllApplicationFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t6getAllApplicationByLanguageCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/eng1")).andReturn(), "KER-MSD-002");
	}

	@Test
	@WithUserDetails("global-Admin")
	public void t7getApplicationByCodeAndLanguageCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/10111/eng1")).andReturn(), "KER-MSD-002");
	}

}
