package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.DocumentCategoryDto;
import io.mosip.kernel.masterdata.dto.DocumentCategoryPutDto;
import io.mosip.kernel.masterdata.dto.request.*;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentCategoryControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private DocumentCategoryDto docCatDto;
	private RequestWrapper<DocumentCategoryDto> doCatDto;
	private RequestWrapper<SearchDto> searchDtoRq;
	private DocumentCategoryPutDto docCatputDto;
	private RequestWrapper<DocumentCategoryPutDto> doPutCatDto;
	private RequestWrapper<FilterValueDto> filValDto;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		docCatDto = new DocumentCategoryDto();

		docCatDto.setCode("POA");
		docCatDto.setDescription("Address proof");
		docCatDto.setIsActive(true);
		docCatDto.setLangCode("eng");
		docCatDto.setName("Proof of Address");
		doCatDto = new RequestWrapper<DocumentCategoryDto>();
		doCatDto.setRequest(docCatDto);
		doPutCatDto = new RequestWrapper<DocumentCategoryPutDto>();
		docCatputDto = new DocumentCategoryPutDto();
		
		doPutCatDto.setRequest(docCatputDto);
		Pagination pagination = new Pagination(0, 1);
		List<SearchSort> ss = new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		sf.setColumnName("code");
		sf.setType("equals");
		sf.setValue("POA");
		ls.add(sf);
		SearchSort s = new SearchSort("name", "ASC");
		SearchDto sd = new SearchDto();
		sd.setFilters(ls);
		sd.setLanguageCode("eng");
		sd.setPagination(pagination);
		ss.add(s);
		sd.setSort(ss);

		searchDtoRq = new RequestWrapper<SearchDto>();
		searchDtoRq.setRequest(sd);

		FilterValueDto f = new FilterValueDto();
		FilterDto dto = new FilterDto();
		dto.setColumnName("name");
		dto.setText("Proof of Address");
		dto.setType("equals");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(dto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);

	}

	@Test
	@WithUserDetails("global-admin")
	public void createDocumentCategory_ValidData_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doCatDto))).andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void createDocumentCategory_InvalidCode_Error() throws Exception {
		doCatDto.getRequest().setCode("a$%bc%");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doCatDto))).andReturn(),"KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void createDocumentCategory_EmptyCode_Error() throws Exception {
		doCatDto.getRequest().setCode("");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doCatDto))).andReturn(),"KER-MSD-999");

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void updateDocumentCategory_withValidCode() throws Exception {
		doPutCatDto.getRequest().setCode("POA");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doPutCatDto))).andReturn(),"KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void updateDocumentCategory_InvalidCode_Error() throws Exception {
		doPutCatDto.getRequest().setCode("#$fdgf");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doPutCatDto))).andReturn(),"KER-MSD-999");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategory_InvalidData_Error() throws Exception {
		
		doPutCatDto.getRequest().setCode("POA");
		doPutCatDto.getRequest().setDescription("Address Proof updated");
		doPutCatDto.getRequest().setIsActive(true);
		doPutCatDto.getRequest().setLangCode("eng");
		doPutCatDto.getRequest().setName("Proof of Address");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doPutCatDto))).andReturn(),"KER-MSD-089");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategory_Success() throws Exception {
		
		doPutCatDto.getRequest().setCode("POI");
		doPutCatDto.getRequest().setDescription("Address Proof updated");
		doPutCatDto.getRequest().setIsActive(true);
		doPutCatDto.getRequest().setLangCode("eng");
		doPutCatDto.getRequest().setName("Proof of Address");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doPutCatDto))).andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testCreateDocumentCategory_ValidData_Success () throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(doCatDto))).andReturn(),null);
		}

	
	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategory_InvalidCode_Error() throws Exception {
		doPutCatDto.getRequest().setCode("POAZ");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documentcategories").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(doPutCatDto))).andReturn(),"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetAllDocumentCategories_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/all"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDocumentCategories_ValidCriteria_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDocumentCategories_InvalidFilterType_Error() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq)))
				.andReturn(),"KER-MSD-318");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDocumentCategories_InvalidFilterType_Fail() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType(FilterTypeEnum.STARTSWITH.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq)))
				.andReturn(),"KER-MSD-318");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDocumentCategoryFilterValues_Success() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDocumentCategoryFilterValues_InvalidColumn_Error() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDocumentCategoryFilterValues_UnsupportedColumn_Error() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documentcategories/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDocumentCategories_EmptyFilterType_Error() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("");
		MasterDataTest.checkResponse( mockMvc
				.perform(MockMvcRequestBuilders.post("/documentcategories/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq)))
				.andReturn(), "KER-MSD-312");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetAllDocumentCategoriesByLanguageCode_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDocumentCategoryByCodeAndLanguageCode_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/POI/eng"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDocumentCategoryByCodeAndLanguageCode_NotFound_Error() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/POAz/eng")).andReturn(), "KER-MSD-014");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllDocumentCategories_Success() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/all")).andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetAllDocumentCategoriesByLanguageCode_InvalidLanguage_Error() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/eng1")).andReturn(),"KER-MSD-014");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategoryStatus_NonexistentCategory_Error() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/documentcategories").param("isActive", "false").param("code", "POAZ"))
				.andReturn(),"KER-MSD-014");
	
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategoryStatus_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/documentcategories").param("isActive", "true").param("code", "POI"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDocumentCategoryStatus_Inactive_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/documentcategories").param("isActive", "false").param("code", "POI"))
				.andReturn(),"KER-MSD-055");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteDocumentCategory_NonexistentCategory_Error() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/documentcategories/POAZ")).andReturn(),"KER-MSD-014");
	
	}

	@Test()
	@WithUserDetails("global-admin")
	public void deleteDocumentCategory_NonexistentCategory_Error() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/documentcategories/POE"))
				.andReturn(),"KER-MSD-014");

	}

	@Test()
	@WithUserDetails("global-admin")
	public void deleteDocumentCategory_withNonexistentCategory_Error() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/documentcategories/POI"))
				.andReturn(),"KER-MSD-123");

	}
	
	@Test()
	@WithUserDetails("global-admin")
	public void testDeleteDocumentCategory_withNonexistentCategory_Error() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/documentcategories/POA"))
				.andReturn(),"KER-MSD-123");

	}
	
	@Test()
	@WithUserDetails("global-admin")
	public void getMissingDocumentCategoryDetails_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/missingids/fra"))
				.andReturn(),null);

	}

}
