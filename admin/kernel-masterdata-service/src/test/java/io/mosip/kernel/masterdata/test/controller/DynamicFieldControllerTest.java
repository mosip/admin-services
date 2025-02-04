package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
import io.mosip.kernel.masterdata.dto.request.*;
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
public class DynamicFieldControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private RequestWrapper<DynamicFieldDto> dynamicFieldDtoReq;

	private RequestWrapper<SearchDto> searchDtoRq;
	private DynamicFieldDto dynamicFieldDto = new DynamicFieldDto();
	private RequestWrapper<FilterValueDto> filValDto =new RequestWrapper<FilterValueDto>();
	private RequestWrapper<DynamicFieldPutDto> dynamicFieldPutDtoReq = new RequestWrapper<DynamicFieldPutDto>();

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		dynamicFieldDtoReq = new RequestWrapper<DynamicFieldDto>();
		// DynamicFieldDto dynamicFieldDto = new DynamicFieldDto();
		dynamicFieldDto.setDataType("string");
		dynamicFieldDto.setDescription("Blood Type");
		dynamicFieldDto.setLangCode("eng");
		dynamicFieldDto.setName("blood type");

		JsonNode jsonNode = null;
		dynamicFieldDto.setFieldVal(jsonNode);

		dynamicFieldDtoReq.setRequest(dynamicFieldDto);

		Pagination pagination = new Pagination(0, 1);
		List<SearchSort> ss = new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		sf.setColumnName("name");
		sf.setType("equals");
		sf.setValue("print");
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

		DynamicFieldPutDto dynamicFieldPutDto = new DynamicFieldPutDto();
		dynamicFieldPutDto.setDataType("string");
		dynamicFieldPutDto.setDescription("Blood Type");
		dynamicFieldPutDto.setLangCode("eng");
		dynamicFieldPutDto.setName("bloodtype");

		dynamicFieldPutDtoReq.setRequest(dynamicFieldPutDto);
		FilterValueDto f = new FilterValueDto();
		FilterDto dto = new FilterDto();
		dto.setColumnName("isActive");
		dto.setText("");
		dto.setType("unique");
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
	public void createDynamicFieldTest_Success() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"oo\",\"value\":\"ooo\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		dynamicFieldDtoReq.getRequest().setName("bloodtype");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						null);
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void createDynamicField_withInValidCode_thenFail() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"%^%$\",\"value\":\"ooo\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		dynamicFieldDtoReq.getRequest().setName("bloodtype");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
										MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
												.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						"KER-MSD-999");
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void createDynamicField_withInValidValue_thenFail() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"avj\",\"value\":\"%^%$\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		dynamicFieldDtoReq.getRequest().setName("bloodtype");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
										MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
												.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void createDynamicFieldRequest_Success() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"10001\",\"value\":\"bloodType\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		dynamicFieldDtoReq.getRequest().setName("bloodtype");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
										MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
												.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testCreateDynamicField_InvalidName_Error() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"10004\",\"value\":\"bloodType1\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		dynamicFieldDtoReq.getRequest().setName("blooddtype1");
		//dynamicFieldDtoReq.getRequest().setLangCode("eng");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testCreateDynamicField_MissingName_Error() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"10004\",\"value\":\"bloodType1\"}");
		dynamicFieldDtoReq.getRequest().setFieldVal(fieldVal);
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
										MockMvcRequestBuilders.post("/dynamicfields").contentType(MediaType.APPLICATION_JSON)
												.content(mapper.writeValueAsString(dynamicFieldDtoReq)))
								.andReturn(),
						"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateDynamicFieldRequest_Fail() throws Exception {
		dynamicFieldPutDtoReq.getRequest().setName("bld");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "10001")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-DYN-001");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDynamicField_Fail() throws Exception {
		String val="{\"code\":\"8888\",\"value\":\"99999\"}";
		JsonNode jsonNode=mapper.readTree(val); 
		dynamicFieldPutDtoReq.getRequest().setFieldVal(jsonNode);
				MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "10001")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDynamicFieldRequest_Fail() throws Exception {
		dynamicFieldPutDtoReq.getRequest().setName("");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "2")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDynamicFields_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDynamicFields_StartsWithFilter_Success() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDynamicFields_EndsWithFilter_Success() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testSearchDynamicFields_ContainsFilter_Success() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetAllDynamicFields_WithPaginationAndSorting_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.get("/dynamicfields").param("pageNumber", "0").param("pageSize", "10")
								.param("sortBy", "name").param("orderBy", "desc").param("langCode", "eng"))
						.andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllDynamicFields_WithPaginationAndSorting_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.get("/dynamicfields").param("pageNumber", "0").param("pageSize", "10")
								.param("sortBy", "name").param("orderBy", "desc").param("langCode", "eng"))
						.andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDistinctDynamicFieldsByLanguageCode_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct/eng")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testGetDistinctDynamicFieldsByLanguageCode_InvalidLanguage_Error() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct/eng1")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testGetDistinctDynamicFields_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDynamicFieldStatus_Inactive_Fail() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "false").param("id", "10001"))
				.andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateDynamicFieldStatus_NonexistentField_Error() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "true").param("id", "110"))
				.andReturn(), "KER-SCH-003");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateAllDynamicFieldsStatusByName_Fail() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/dynamicfields/all")
				.param("isActive", "true").param("fieldName", "bloodType1")).andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateAllDynamicFieldsStatus_InvalidFieldName_Error() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/dynamicfields/all").param("isActive", "true").param("fieldName", "110"))
				.andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteDynamicField_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/10001")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteDynamicField_NonexistentField_Fail() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/1111")).andReturn(),
				"KER-SCH-003");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteAllDynamicFieldsByName_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/all/bloodtype")).andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testDeleteAllDynamicFieldsByName_Fail() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/all/bloodtype")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDynamicFieldFilterValues_UniqueValues_Success() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetMissingDynamicFields_InvalidLanguage_Error() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara1")).andReturn(), "KER-LANG-ERR");

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetMissingDynamicFields_withValidLanguage_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetMissingDynamicFieldsByFieldNameAndLanguage_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara").param("fieldName", "name")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testGetDynamicFieldByNameAndLanguage_Fail() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType1/eng")).andReturn(),
				"KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetDynamicFieldByNameAndLanguageWithInvalidValues_Fail() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType1/eng").param("withValue", "true")).andReturn(),
				"KER-SCH-003");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testGetDynamicFieldByNameAndLanguage_NotFound_Error() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/blod/eng")).andReturn(),
				"KER-SCH-003");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void testGetDynamicFieldByNameAndLanguage_InvalidLanguage_Error() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType/eng1")).andReturn(),
				"KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void testGetAllDynamicFieldsByName_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/gender")).andReturn(),
				null);
	}

}
