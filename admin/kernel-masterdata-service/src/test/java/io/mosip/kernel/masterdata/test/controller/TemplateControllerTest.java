package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.TemplatePutDto;
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

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	
	private ObjectMapper mapper;
	
	private RequestWrapper<TemplateDto> template=new RequestWrapper<TemplateDto>();
	private RequestWrapper<TemplatePutDto> templateUpdate=new RequestWrapper<TemplatePutDto>();
	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		TemplateDto dto=new TemplateDto();
		dto.setDescription("description");
		dto.setFileFormatCode("txt");
		dto.setFileText("hi this is sample");
		dto.setId("5");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setModel("velocity");
		dto.setModuleId("10003");
		dto.setModuleName("testing");
		dto.setName("Sample");
		dto.setTemplateTypeCode("SMS");
		template.setRequest(dto);
		
		TemplatePutDto  dto2=new TemplatePutDto();
		dto2.setDescription("description");
		dto2.setFileFormatCode("txt");
		dto2.setFileText("hi this is sample update");
		dto2.setId("5");
		dto2.setIsActive(true);
		dto2.setLangCode("eng");
		dto2.setModel("velocity");
		dto2.setModuleId("10003");
		dto2.setModuleName("testing");
		dto2.setName("Sample update");
		dto2.setTemplateTypeCode("SMS");
		templateUpdate.setRequest(dto2);

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng1")).andReturn(), "KER-MSD-045");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeAndTemplateTypeCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng/EMAIL")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeAndTemplateTypeCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng1/EMAIL")).andReturn(), "KER-MSD-045");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeAndTemplateTypeCodeFailTest_WithInvalidTemplate() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng/txt")).andReturn(), "KER-MSD-046");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void createTemplateTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void createTemplateTest_SuccessWithId() throws Exception {
		template.getRequest().setId("");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void createTemplateFailTest_WithInvalidId() throws Exception {
		template.getRequest().setId("1");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateTest_Fail() throws Exception {
		templateUpdate.getRequest().setId("1");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), "KER-MSD-095");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateTest_Success() throws Exception {
		templateUpdate.getRequest().setId("4");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateTest_WithModuleId_Success() throws Exception {
		templateUpdate.getRequest().setId("4");
		templateUpdate.getRequest().setModuleId("10004");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateFailTest_WithInvalidTemplate() throws Exception {
		templateUpdate.getRequest().setId("8");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), "KER-MSD-046");
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteTemplateTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templates/2")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteTemplateFailTest_WithInvalidTemplate() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templates/9")).andReturn(), "KER-MSD-046");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByTemplateTypeCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EMAIL")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByTemplateTypeCodeFailTest_WithInvalidTemplate() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EM")).andReturn(), "KER-MSD-046");
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void getTemplatesTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/all")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchTemplateByLangCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCode","EMAIL","equals")))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchAllTemplateByLangCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCode","EMAIL","contains")))).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByLangCodeFailTest_WithInvalidColumn() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCodee","EMAIL","contains")))).andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesTest_Success_WithAllFilterTypes() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCode","EMAIL","all")))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesTest_PassWithUniqueFilterType() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCode","EMAIL","unique")))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesFail_WithInvalidFilterType() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCodee","EMAIL","")))).andReturn(), "KER-MSD-322");
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateStatusTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templates").param("isActive", "true").param("id","3")).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void updateTemplateStatusFail_WithInvalidTemplate() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templates").param("isActive", "false").param("id","9")).andReturn(), "KER-MSD-046");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getMissingTemplateDetailsTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara")).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void getMissingTemplateDetailsFail_WithInvalidLangCode() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/eng1")).andReturn(), "KER-LANG-ERR");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getMissingTemplateDetailsTest_PassWithValidLangCode() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara").param("fieldName", "name")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getMissingTemplateDetailsFail_WithInvalidLang() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/eng1").param("fieldName","name")).andReturn(), "KER-LANG-ERR");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getMissingTemplateDetailsFail_WithInvalidFieldName() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara").param("fieldName", "namee")).andReturn(), "KER-MSD-317");
	}
	
	
}
