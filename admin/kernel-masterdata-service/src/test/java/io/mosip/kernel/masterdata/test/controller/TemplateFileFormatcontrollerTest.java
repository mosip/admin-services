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

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatPutDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateFileFormatcontrollerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	private RequestWrapper<TemplateFileFormatDto> templateFileFormatRequestDto=new RequestWrapper<TemplateFileFormatDto>();
	private RequestWrapper<TemplateFileFormatPutDto> putDto=new RequestWrapper<TemplateFileFormatPutDto>();
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		TemplateFileFormatDto dto=new TemplateFileFormatDto();
		dto.setCode("html");
		dto.setDescription("htmldesc");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		templateFileFormatRequestDto.setRequest(dto);
		TemplateFileFormatPutDto fileFormatDto=new TemplateFileFormatPutDto();
		fileFormatDto.setCode("html");
		fileFormatDto.setDescription("jsondesc update");
		fileFormatDto.setIsActive(true);
		fileFormatDto.setLangCode("eng");
		putDto.setRequest(fileFormatDto);
		
	}
	@Test
	@WithUserDetails("global-admin")
	public void t001createTemplateFileFormatTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/templatefileformats").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(templateFileFormatRequestDto))).andReturn(), null);

	}
		
	@Test
	@WithUserDetails("global-admin")
	public void t002updateTemplateFileFormatTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templatefileformats").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(putDto))).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateTemplateFileFormatFailTest1() throws Exception {
		putDto.getRequest().setCode("abc");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/templatefileformats").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(putDto))).andReturn(), "KER-MSD-046");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004deleteTemplateFileFormatFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templatefileformats/txt")).andReturn(), "KER-MSD-125");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004deleteTemplateFileFormatTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templatefileformats/html")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005deleteTemplateFileFormatFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/templatefileformats/txt1")).andReturn(), "KER-MSD-046");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t006getTemplateFileFormatCodeandLangCodeTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/code/json")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006getTemplateFileFormatCodeandLangCodeFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/code/json1")).andReturn(), "KER-MSD-046");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getTemplateFileFormatCodeandLangCodeTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/json/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getTemplateFileFormatCodeandLangCodeFailTest1() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/json1/eng")).andReturn(), "KER-MSD-046");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getTemplateFileFormatCodeandLangCodeFailTest2() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/json/eng1")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateFileFormatStatusTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templatefileformats").param("isActive","true").param("code","json")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateFileFormatStatusTest1() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templatefileformats").param("isActive","false").param("code","json")).andReturn(), "KER-MSD-237");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009updateFileFormatStatusTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/templatefileformats").param("isActive","true").param("code","json1")).andReturn(), "KER-MSD-046");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010getTemplateFileFormatLangCodeTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011getTemplateFileFormatLangCodeTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/templatefileformats")).andReturn(), null);

	}
}
