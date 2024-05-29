package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.TemplatePutDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

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
	public void t001getAllTemplateTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002getAllTemplateBylangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getAllTemplateBylangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng1")).andReturn(), "KER-MSD-045");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getAllTemplateBylangCodeAndTemplateTypeCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng/EMAIL")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getAllTemplateBylangCodeAndTemplateTypeCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng1/EMAIL")).andReturn(), "KER-MSD-045");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getAllTemplateBylangCodeAndTemplateTypeCodeFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/eng/txt")).andReturn(), "KER-MSD-046");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t004createTemplateTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004createTemplateTest1() throws Exception {
		template.getRequest().setId("");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004createTemplateFailTest() throws Exception {
		template.getRequest().setId("1");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(template))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateTemplateTest() throws Exception {
		templateUpdate.getRequest().setId("1");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), "KER-MSD-095");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateTemplateTest2() throws Exception {
		templateUpdate.getRequest().setId("4");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateTemplateTest3() throws Exception {
		templateUpdate.getRequest().setId("4");
		templateUpdate.getRequest().setModuleId("10004");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t005updateTemplateFailTest() throws Exception {
		templateUpdate.getRequest().setId("8");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templates")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateUpdate))).andReturn(), "KER-MSD-046");
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t006deleteTemplateTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templates/2")).andReturn(), null);
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t006deleteTemplateFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templates/9")).andReturn(), "KER-MSD-046");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007getAllTemplateByTemplateTypeCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EMAIL")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getAllTemplateByTemplateTypeCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EM")).andReturn(), "KER-MSD-046");
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t008getTemplatesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/all")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getAllTemplateBylangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCode","EMAIL","equals")))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getAllTemplateBylangCodeTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCode","EMAIL","contains")))).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t009getAllTemplateBylangCodeFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC","templateTypeCodee","EMAIL","contains")))).andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010filterTemplatesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCode","EMAIL","all")))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010filterTemplatesFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCode","EMAIL","unique")))).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t010filterTemplatesFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("templateTypeCodee","EMAIL","")))).andReturn(), "KER-MSD-322");
	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t011updateTemplateStatusTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templates").param("isActive", "true").param("id","3")).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t011updateTemplateStatusFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templates").param("isActive", "false").param("id","9")).andReturn(), "KER-MSD-046");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t012getMissingTemplateDetailsTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara")).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t012getMissingTemplateDetailsFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/eng1")).andReturn(), "KER-LANG-ERR");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t013getMissingTemplateDetailsTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara").param("fieldName", "name")).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t013getMissingTemplateDetailsFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/eng1").param("fieldName","name")).andReturn(), "KER-LANG-ERR");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t013getMissingTemplateDetailsFailTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templates/missingids/ara").param("fieldName", "namee")).andReturn(), "KER-MSD-317");
	}
	
	
}
