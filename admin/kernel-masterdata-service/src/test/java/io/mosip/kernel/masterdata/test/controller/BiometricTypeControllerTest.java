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
import org.springframework.http.MediaType;
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
public class BiometricTypeControllerTest {

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
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());

	}

	@Test
	@WithUserDetails("global-admin")
	public void t1createBiometricTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/biometrictypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"IR\",\n" + "    \"description\": \"Iris of applicant\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\",\n" + "    \"name\": \"Iris\"\n"
						+ "  }\n" + "}"))

				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t9createBiometricTypeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/biometrictypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"IRS1\",\n" + "    \"description\": \"Iris of applicant\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\",\n" + "    \"name\": \"Iris\"\n"
						+ "  }\n" + "}"))

				.andReturn(),"KER-MSD-105");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t2getAllBiometricTypesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes")).andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t3getAllBiometricTypesByCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/code/PHT"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t3getAllBiometricTypesByLangcodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t5getBiometricTypeByCodeAndLangCodeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/PHT/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t6createBiometricTypeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/biometrictypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"\",\n" + "    \"description\": \"Iris of applicant\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\",\n" + "    \"name\": \"Iris\"\n"
						+ "  }\n" + "}"))
				.andReturn(),"KER-MSD-999");
	

	}

	@Test
	@WithUserDetails("global-admin")
	public void t7getAllBiometricTypesFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes")).andReturn(),"KER-MSD-006");
		
	}

	@Test
	@WithUserDetails("global-admin")
	public void t8getAllBiometricTypesByLanguageCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/eng1"))
				.andReturn(),"KER-MSD-006");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t9getBiometricTypeByCodeAndLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/PHT/eng1"))
				.andReturn(),"KER-MSD-006");
	

	}

}
