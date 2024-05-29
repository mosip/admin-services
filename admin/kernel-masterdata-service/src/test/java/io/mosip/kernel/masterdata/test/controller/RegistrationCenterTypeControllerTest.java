package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.constant.RegistrationCenterTypeErrorCode;
import io.mosip.kernel.masterdata.constant.RequestErrorCode;
import io.mosip.kernel.masterdata.constant.ValidationErrorCode;
import io.mosip.kernel.masterdata.dto.RegistrationCenterTypeDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

/**
 * @author GOVINDARAJ VELU
 * @implSpec RegistrationCenterTypecontroller Test-cases
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationCenterTypeControllerTest extends AbstractTest {
	
	private RequestWrapper<RegistrationCenterTypeDto> registrationCenterTypeWrapper;
	
	private RequestWrapper<FilterValueDto> registrationCenterTypeFilterWrapper;
	
	private RequestWrapper<SearchDto> registrationCenterSearchWrapper;
	
	private ObjectMapper mapper;

	
	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		registrationCenterTypeWrapper = new RequestWrapper<>();
		String code = "TVM", langCode = "eng", name = "tvmcn", descr="tvm center";
		boolean isActive = true;
		RegistrationCenterTypeDto registrationCenterTypeDto = getRegistrationCenterTypeDto(code, langCode,
				name, descr, isActive);

		registrationCenterTypeWrapper.setRequest(registrationCenterTypeDto);
		registrationCenterTypeFilterWrapper = new RequestWrapper<>();
		
		FilterValueDto filterValueDto = new FilterValueDto();
		String columnName = "name", filterType = FilterColumnEnum.UNIQUE.toString(), text = "CEN",
				searchType = FilterTypeEnum.EQUALS.toString();
		FilterDto filter = FilterDto.builder().columnName(columnName).type(filterType).text(text).build();
		List<FilterDto> filters = Arrays.asList(filter);
		filterValueDto.setFilters(filters);
		filterValueDto.setLanguageCode(langCode);
		String value = "test", fromValue = "e", toValue = "t";
		SearchFilter searchFilter = new SearchFilter(value, fromValue, toValue, columnName, searchType);
		List<SearchFilter> optionalFilters = Arrays.asList(searchFilter);
		filterValueDto.setOptionalFilters(optionalFilters);
		
		registrationCenterTypeFilterWrapper.setRequest(filterValueDto);
		
		registrationCenterSearchWrapper = new RequestWrapper<>();
		SearchDto searchDto = new SearchDto();
		List<SearchFilter> searchFilters = Arrays.asList(searchFilter);
		searchDto.setFilters(searchFilters);
		String sortField = "code", sortType = "DESC";
		SearchSort searchSort = new SearchSort();
		searchSort.setSortField(sortField);
		searchSort.setSortType(sortType);
		List<SearchSort> searchSorts = Arrays.asList(searchSort);
		searchDto.setSort(searchSorts);
		searchDto.setLanguageCode(langCode);
		Pagination pagination = new Pagination(0, 10);
		searchDto.setPagination(pagination);
		registrationCenterSearchWrapper.setRequest(searchDto);
		
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}

	@Test
	@WithUserDetails("global-admin")
	public void createRegistrationCenterType() throws Exception {
		//when
		String uri = "/registrationcentertypes";
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(registrationCenterTypeWrapper))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t1updateRegistrationCenterType() throws Exception {
		//given
		String code = "TVM", langCode = "eng", name = "tvmcn update", descr="tvmcenter";
		boolean isActive = true;
		RegistrationCenterTypeDto registrationCenterTypeDto = getRegistrationCenterTypeDto(code,
				langCode, name, descr, isActive);
		registrationCenterTypeWrapper.setRequest(registrationCenterTypeDto);
		//when
		String uri = "/registrationcentertypes";
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON)
						.content(mapToJson(registrationCenterTypeWrapper))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t1updateRegistrationCenterTypeFailureNotFound() throws Exception {
		//given
		String code = "TVM-01", langCode = "eng", name = "tvmcn", descr="tvm center";
		boolean isActive = true;
		RegistrationCenterTypeDto registrationCenterTypeDto = getRegistrationCenterTypeDto(code,
				langCode, name, descr, isActive);
		registrationCenterTypeWrapper.setRequest(registrationCenterTypeDto);
		//when
		String uri = "/registrationcentertypes";
		//then
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON)
						.content(mapToJson(registrationCenterTypeWrapper))).andReturn(),
				RegistrationCenterTypeErrorCode.REGISTRATION_CENTER_TYPE_NOT_FOUND_EXCEPTION.getErrorCode());
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t2deleteRegistrationCenterTypeFailureNotFound() throws Exception {
		//given
		String code = "TVM-01";
		//when
		String uri = "/registrationcentertypes/" + code;
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn(),
				RegistrationCenterTypeErrorCode.REGISTRATION_CENTER_TYPE_NOT_FOUND_EXCEPTION.getErrorCode());
	}

	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t2deleteRegistrationCenterTypeDependency() throws Exception {
		//given
		String code = "TVM";
		//when
		String uri = "/registrationcentertypes/" + code;
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t00getAllRegistrationCenterTypes() throws Exception {
		//given
		String pageNumber = "0", pageSize = "10", sortBy = "createdDateTime", orderBy = "desc";
		//when
		String uri = "/registrationcentertypes/all";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("pageNumber", pageNumber)
				.param("pageSize", pageSize)
				.param("sortBy", sortBy)
				.param("orderBy", orderBy);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t0getAllRegistrationCenterTypesFailure() throws Exception {
		//given
		String pageNumber = "0", pageSize = "10", sortBy = "createdDateTime", orderBy = "desc";
		//when
		String uri = "/registrationcentertypes/all";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("pageNumber", pageNumber)
				.param("pageSize", pageSize)
				.param("sortBy", sortBy)
				.param("orderBy", orderBy);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t000getAllRegistrationCenterTypesFailure() throws Exception {
		//given
		String pageNumber = "0", pageSize = "10", sortBy = "createdDateTimes", orderBy = "desc";
		//when
		String uri = "/registrationcentertypes/all";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("pageNumber", pageNumber)
				.param("pageSize", pageSize)
				.param("sortBy", sortBy)
				.param("orderBy", orderBy);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				RegistrationCenterTypeErrorCode.REGISTRATION_CENTER_TYPE_FETCH_EXCEPTION.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t5registrationCenterTypeFilterValuesFailureUnique() throws Exception {
		//when
		String uri = "/registrationcentertypes/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterTypeFilterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t5registrationCenterTypeFilterValuesFailureAll() throws Exception {
		//given
		setValueInFilter(FilterColumnEnum.ALL.toString());
		//when
		String uri = "/registrationcentertypes/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterTypeFilterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t5registrationCenterTypeFilterValuesFailureAll1() throws Exception {
		//given
		setValueInFilter(FilterColumnEnum.ALL.toString());
		registrationCenterTypeFilterWrapper.getRequest().getFilters().get(0).setColumnName("code");
		registrationCenterTypeFilterWrapper.getRequest().getFilters().get(0).setText("REG");
		//when
		String uri = "/registrationcentertypes/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterTypeFilterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t5registrationCenterTypeFilterValuesFailureEmpty() throws Exception {
		//given
		setValueInFilter(FilterColumnEnum.EMPTY.toString());
		//when
		String uri = "/registrationcentertypes/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterTypeFilterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureEquals() throws Exception {
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureContains() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.CONTAINS.toString());
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureStartWith() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.STARTSWITH.toString());
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureBetween() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.BETWEEN.toString());
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				ValidationErrorCode.FILTER_NOT_SUPPORTED.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureIn() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.IN.toString());
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				ValidationErrorCode.FILTER_NOT_SUPPORTED.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t6searchRegistrationCenterTypeFailureEmpty() throws Exception {
		//given
		setValueInSearch("");
		//when
		String uri = "/registrationcentertypes/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(registrationCenterSearchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				MasterdataSearchErrorCode.FILTER_TYPE_NOT_AVAILABLE.getErrorCode());
	}
	
	@Ignore
	@Test
	@WithUserDetails("global-admin")
	public void t7updateRegistrationCenterTypeStatusFailureNotFound() throws Exception {
		//given
		String code = "TVM", isActive = "true";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				RegistrationCenterTypeErrorCode.REGISTRATION_CENTER_TYPE_NOT_FOUND_EXCEPTION.getErrorCode());
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t02updateRegistrationCenterTypeStatusMapping() throws Exception {
		//given
		String code = "TVM", isActive = "false";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				null);
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t02updateRegistrationCenterTypeStatusFailure() throws Exception {
		//given
		String code = "TVM", isActive = "true";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t02updateRegistrationCenterTypeStatusFailure1() throws Exception {
		//given
		String code = "TVzzzzzzzzzzzzzzzzzzz", isActive = "true";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), "KER-MSD-120");
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t02updateRegistrationCenterTypeStatus3() throws Exception {
		//given
		String code = "REG", isActive = "true";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
		
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t02updateRegistrationCenterTypeStatusFailure2() throws Exception {
		//given
		String code = "TV?M&", isActive = "true";
		//when
		String uri = "/registrationcentertypes";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.param("code", code)
				.param("isActive", isActive);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), "KER-MSD-999");
		
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getMissingRegistrationCentersTypesDetailsFailure() throws Exception {
		//given
		String langCode = "eng", fieldName = "name";
		//when
		String uri = "/registrationcentertypes/missingids/" + langCode;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("fieldName", fieldName);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getMissingRegistrationCentersTypesDetailsInvalidLanguage() throws Exception {
		//given
		String langCode = "engs", fieldName = "name";
		//when
		String uri = "/registrationcentertypes/missingids/" + langCode;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.param("fieldName", fieldName);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				MasterdataSearchErrorCode.INVALID_LANGCODE.getErrorCode());
	}
	
	RegistrationCenterTypeDto getRegistrationCenterTypeDto(String code, String langCode, String name, 
			String descr, boolean isActive) {
		RegistrationCenterTypeDto registrationCenterTypeDto = new RegistrationCenterTypeDto();
		registrationCenterTypeDto.setCode(code);
		registrationCenterTypeDto.setLangCode(langCode);
		registrationCenterTypeDto.setName(name);
		registrationCenterTypeDto.setDescr(descr);
		registrationCenterTypeDto.setIsActive(isActive);
		return registrationCenterTypeDto;
	}
	
	void setValueInFilter(String type) {
		registrationCenterTypeFilterWrapper.getRequest().getFilters().get(0).setType(type);
	}
	
	void setValueInSearch(String type) {
		registrationCenterSearchWrapper.getRequest().getFilters().get(0).setType(type);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void registrationCenterFilterValuesTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("name", "Regular", "unique")))).andReturn(),null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void registrationCenterFilterValuesTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("name", "Regular", "all")))).andReturn(),null);
	}
	
	

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterTypeStatusTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/registrationcentertypes").param("isActive", "false").param("code","REG")).andReturn(),"KER-MSD-270");
	}
}
