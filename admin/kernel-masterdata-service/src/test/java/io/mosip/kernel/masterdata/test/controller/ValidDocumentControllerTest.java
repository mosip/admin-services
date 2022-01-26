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
import io.mosip.kernel.masterdata.dto.ValidDocumentDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidDocumentControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;
	private RequestWrapper<ValidDocumentDto> document = new RequestWrapper<ValidDocumentDto>();

	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		ValidDocumentDto dto = new ValidDocumentDto();
		dto.setDocCategoryCode("POR");
		dto.setDocTypeCode("CIN");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		document.setRequest(dto);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createValidDocumentTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(document))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createValidDocumentFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(document))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002deleteValidDocuemntTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/validdocuments/POI/CIN")).andReturn(), "KER-MSD-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003deleteValidDocuemntFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/validdocuments/POI/CIN")).andReturn(), "KER-MSD-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getValidDocumentByLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/eng")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getValidDocumentByLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/eng1")).andReturn(),
				"KER-MSD-016");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getAllValidDocumentTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/all")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime1","ASC", "docCategoryCode", "POI","equals")))).andReturn(),
				"KER-MSD-357");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","desc", "docCategoryCode", "POI","equals")))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","desc", "docCategoryCode", "POI","contains")))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentTest6() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","desc", "docCategoryCode", "testDummy","contains")))).andReturn(),
				"KER-MSD-355");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC", "docTypeCodee", "CIN","contains")))).andReturn(),
				"KER-MSD-317");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005searchValidDocumentFailTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime","ASC", "docCategoryCode", "PINI","equals")))).andReturn(),
				"KER-MSD-355");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t006categoryTypeFilterValuesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN","all")))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006categoryTypeFilterValuesTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN","")))).andReturn(),
				"KER-MSD-322");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006categoryTypeFilterValuesTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN","unique")))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006categoryTypeFilterValuesFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCodee", "CIN","all")))).andReturn(),
				"KER-MSD-317");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009mapDocCategoryAndDocTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009mapDocCategoryAndDocTypeTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/CIN")).andReturn(),
				"KER-MSD-212");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010mapDocCategoryAndDocTypeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
				"KER-MSD-360");
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t011mapDocCategoryAndDocTypeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
				"KER-MSD-360");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008unmapDocCategoryAndDocTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA/COR")).andReturn(),
				"KER-MSD-271");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008unmapDocCategoryAndDocTypeTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/P1/C1")).andReturn(),
				"KER-MSD-361");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007unmapDocCategoryAndDocTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA/COR")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008getValidDocumentByDocCategoryCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/POI/eng")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getValidDocumentByDocCategoryCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/P1/C1")).andReturn(),
				"KER-MSD-212");
	}
	
	
}
