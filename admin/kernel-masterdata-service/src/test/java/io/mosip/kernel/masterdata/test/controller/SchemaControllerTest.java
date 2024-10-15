package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.IdSchemaPublishDto;
import io.mosip.kernel.masterdata.dto.IdentitySchemaDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchemaControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private RequestWrapper<IdentitySchemaDto> schema = new RequestWrapper<IdentitySchemaDto>();
	private RequestWrapper<IdSchemaPublishDto> idSchemaPublishDto = new RequestWrapper<IdSchemaPublishDto>();

	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		IdentitySchemaDto request = new IdentitySchemaDto();
		request.setDescription("mp test");
		request.setSchema("{\"schema\":\"schema\"}");
		request.setTitle("mp test");
		schema.setRequest(request);
		IdSchemaPublishDto dto = new IdSchemaPublishDto();
		dto.setId("2");
		dto.setEffectiveFrom(LocalDateTime.of(2019, 12, 10, 10, 20));
		idSchemaPublishDto.setRequest(dto);
	}

	@Test
	@WithUserDetails("global-admin")
	public void createSchemaTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/idschema")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateSchemaTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/idschema").param("id", "1")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema)))
				.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateSchemaTest_FailWithInvalidSchema() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/idschema").param("id", "3")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema)))
				.andReturn(), "KER-SCH-009");
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateSchema_FailWithInvalidSchema() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/idschema").param("id", "7")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(schema)))
				.andReturn(), "KER-SCH-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishSchemaTest_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/idschema/publish").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idSchemaPublishDto))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void publishSchemaTest_WithNonEffectiveSchema() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/idschema/publish").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idSchemaPublishDto))).andReturn(),
				"KER-SCH-010");
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishSchemaTest_FailWithAlreadyPublishedSchema() throws Exception {
		idSchemaPublishDto.getRequest().setId("10");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/idschema/publish").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idSchemaPublishDto))).andReturn(),
				"KER-SCH-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void publishSchemaTest_SuccessWithValidSchema() throws Exception {
		idSchemaPublishDto.getRequest().setId("10");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/idschema/publish").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(idSchemaPublishDto))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteSchemaTest_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/idschema").param("id", "1")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteSchemaTest_FailWithInvalidSchema() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/idschema").param("id", "10")).andReturn(), "KER-SCH-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllSchemaTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/idschema/all")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchemaTest_FailWithInvalidSchema() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion","0.1")).andReturn(), "KER-SCH-007");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getLatestPublishedSchemaTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/idschema/latest").param("schemaVersion","1.1")).andReturn(), null);
	}
	
}
