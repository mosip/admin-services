package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
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
	public void t001createDynamicFieldTest() throws Exception {
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
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createDynamicFieldTest1() throws Exception {
		JsonNode fieldVal = mapper.readTree("{\"code\":\"10001\",\"value\":\"bloodType1\"}");
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
	public void t001createDynamicFieldTest3() throws Exception {
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
	public void t002createDynamicFieldFailTest() throws Exception {

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
	public void t003updateDynamicFieldFailTest() throws Exception {
		dynamicFieldPutDtoReq.getRequest().setName("bld");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "10001")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-DYN-001");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateDynamicFieldTest() throws Exception {
		String val="{\"code\":\"8888\",\"value\":\"99999\"}";
		JsonNode jsonNode=mapper.readTree(val); 
		dynamicFieldPutDtoReq.getRequest().setFieldVal(jsonNode);
				MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "10001")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateDynamicFieldFailTest() throws Exception {
		dynamicFieldPutDtoReq.getRequest().setName("");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/dynamicfields").param("id", "2")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dynamicFieldPutDtoReq)))
				.andReturn(), "KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t005searchDynamicFieldsTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006searchDynamicFieldsTest2() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006searchDynamicFieldsTest3() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007searchDynamicFieldsTest4() throws Exception {
		searchDtoRq.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t008getAllDynamicFieldsTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.get("/dynamicfields").param("pageNumber", "0").param("pageSize", "10")
								.param("sortBy", "name").param("orderBy", "desc").param("langCode", "eng"))
						.andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t019getAllDynamicFieldsTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(
						MockMvcRequestBuilders.get("/dynamicfields").param("pageNumber", "0").param("pageSize", "10")
								.param("sortBy", "name").param("orderBy", "desc").param("langCode", "eng"))
						.andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009getDistinctDynamicFieldsBasedOnLangTest01() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct/eng")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getDistinctDynamicFieldsBasedOnLangTest02() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct/eng1")).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009getDistinctDynamicFieldsTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/distinct")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t011updateDynamicFieldStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "false").param("id", "10001"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t012updateDynamicFieldStatusFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/dynamicfields").param("isActive", "true").param("id", "110"))
				.andReturn(), "KER-SCH-003");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t013updateAllDynamicFieldStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/dynamicfields/all")
				.param("isActive", "true").param("fieldName", "bloodType1")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t014updateAllDynamicFieldStatusFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/dynamicfields/all").param("isActive", "true").param("fieldName", "110"))
				.andReturn(), "KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015deleteDynamicFieldTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/10001")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t016deleteDynamicFieldFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/1111")).andReturn(),
				"KER-SCH-003");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017deleteAllDynamicFieldTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/all/bloodtype")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018deleteAllDynamicFieldFailTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/dynamicfields/all/bloodtype")).andReturn(),
				"KER-SCH-003");

	}
	@Test
	@WithUserDetails("global-admin")
	public void t019dynamicFiledFilterValuesTest() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/dynamicfields/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t020getMissingDynamicFieldsFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara1")).andReturn(), "KER-LANG-ERR");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t020getMissingDynamicFieldsTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t022getMissingDynamicFieldsTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/missingids/ara").param("fieldName", "name")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t000getDynamicFieldByNameTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType1/eng")).andReturn(),
				null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t000getDynamicFieldByNameTest3() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType1/eng").param("withValue", "true")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t000getDynamicFieldByNameTest1() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/blod/eng")).andReturn(),
				"KER-SCH-003");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t022getDynamicFieldByNameTest2() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/bloodType/eng1")).andReturn(),
				"KER-SCH-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t000getAllDynamicFieldByNameTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/dynamicfields/gender")).andReturn(),
				null);
	}

}
