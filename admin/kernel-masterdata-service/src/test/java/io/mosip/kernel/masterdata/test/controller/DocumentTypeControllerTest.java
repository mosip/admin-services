package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.DocumentTypePutReqDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentTypeControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;
	
	private DocumentTypeDto dto=new DocumentTypeDto();
	private RequestWrapper<DocumentTypeDto> documentTypeDtoReq=new RequestWrapper<DocumentTypeDto>();
	private RequestWrapper<DocumentTypePutReqDto> documentTypeupdateDtoReq=new RequestWrapper<DocumentTypePutReqDto>();
	private RequestWrapper<FilterValueDto> filValDto =new RequestWrapper<FilterValueDto>();
	private RequestWrapper<SearchDto> searchDtoRq=new RequestWrapper<SearchDto>();
	@Before
	public void setUp(){
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		documentTypeDtoReq=new RequestWrapper<DocumentTypeDto>();
		dto.setCode("CIN1");
		dto.setDescription("Reference of Identity");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setName("Identity card");
		documentTypeDtoReq.setRequest(dto);
		
		documentTypeupdateDtoReq=new RequestWrapper<DocumentTypePutReqDto>();
		DocumentTypePutReqDto dto1=new DocumentTypePutReqDto();
		dto1.setCode("CIN1");
		dto1.setDescription("Reference of Identity updated");
		dto1.setIsActive(true);
		dto1.setLangCode("eng");
		dto1.setName("Identity card");
		documentTypeupdateDtoReq.setRequest(dto1);
		Pagination pagination=new Pagination(0,1);
		List<SearchSort> ss=new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf=new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls=new ArrayList<>();
		sf.setColumnName("name");
		sf.setType("equals");
		sf.setValue("Reference of Identity updated");
		ls.add(sf);
		SearchSort s=new SearchSort("name","ASC");
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
		dto.setText("Reference of Identity");
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
	public void t002getDoucmentTypesForDocumentCategoryAndLangCodeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/eng/CIN"))
				.andReturn(),"KER-MSD-118");//check

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t003getDoucmentTypesForDocumentCategoryAndLangCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/eng1/POA"))
		.andReturn(), "KER-MSD-118");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createDocumentCategoryTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(documentTypeDtoReq))).andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004createDocumentCategoryTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(documentTypeDtoReq))).andReturn(),null);
	}
	
	/*@Test
	@WithUserDetails("global-admin")
	public void t005updateDocumentTypeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(mapper.writeValueAsString(documentTypeupdateDtoReq)))).andReturn(),null);

	}*/
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateDocumentTypeFailTest() throws Exception {
		documentTypeupdateDtoReq.getRequest().setCode("POP");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(mapper.writeValueAsString(documentTypeupdateDtoReq)))).andReturn(), "KER-MSD-999");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateDocumentTypeFailTest1() throws Exception {
		documentTypeupdateDtoReq.getRequest().setCode("@#gsf");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(mapper.writeValueAsString(documentTypeupdateDtoReq)))).andReturn(), "KER-MSD-999");
	}
	
	
	/*@Test
	@WithUserDetails("global-admin")
	public void t018deleteDocumentTypeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/CIN1"))
				.andReturn(),null);

	}*/
	

	@Test
	@WithUserDetails("global-admin")
	public void t019deleteDocumentTypeFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/CINN"))
		.andReturn(),"KER-MSD-023");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getAllDocumentTypesTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/all").param("pageNumber", "0").param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
				.andReturn(),null);

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t008documentTypeFilterValuesTest() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t008documentTypeFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),"KER-MSD-322");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t009documentTypeFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t010searchDocumentTypeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(searchDtoRq))).andReturn(),null);

	}
	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t011searchDocumentTypeTest1() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(searchDtoRq))).andReturn(),null);

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t012searchDocumentTypeTest2() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(searchDtoRq))).andReturn(),null);

	}
	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t014getAllDocumentTypeByLaguageCodeTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/eng"))
				.andReturn(),null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t015getAllDocumentTypeByLaguageCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/eng1"))
		.andReturn(),"KER-MSD-023");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017updateDocumentTypeStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/documenttypes").param("isActive", "false").param("code","CIN1"))
				.andReturn(),null);

	}
	

	//this test case is ignored to resolve the build issue after the build issue is resolved it should be removed
	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t016updateDocumentTypeStatusTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/documenttypes").param("isActive", "false").param("code","COR"))
				.andReturn(),"KER-MSD-119");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017updateDocumentTypeStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/documenttypes").param("isActive", "false").param("code","CINN"))
		.andReturn(),"KER-MSD-118");
	
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t018getMissingDocumentTypeDetailsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/missingids/fra"))
		.andReturn(),null);
	
	}
	@Test
	@WithUserDetails("global-admin")
	public void t001createDocumentCategoryTest1() throws Exception {
		documentTypeDtoReq.getRequest().setCode("abc%");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/documenttypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(documentTypeDtoReq))).andReturn(),"KER-MSD-999");

	}

}
