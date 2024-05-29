package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.constant.RegistrationCenterTypeErrorCode;
import io.mosip.kernel.masterdata.constant.RequestErrorCode;
import io.mosip.kernel.masterdata.constant.TitleErrorCode;
import io.mosip.kernel.masterdata.constant.ValidationErrorCode;
import io.mosip.kernel.masterdata.dto.TitleDto;
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
 * @implSpec TitleController Test-cases
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TitleControllerTest extends AbstractTest {

	private RequestWrapper<TitleDto> requestWrapper;
	private RequestWrapper<SearchDto> searchWrapper;
	private RequestWrapper<FilterValueDto> filterWrapper;
	
	
	@Before
	public void setUp() {
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		
		requestWrapper = new RequestWrapper<>();
		String langCode = "eng", code = "TVM", titleName = "tvm-center", titleDescription = "tit-descrip";
		boolean isActive = true;
		TitleDto titleDto = titleDto(langCode, code, titleName, titleDescription, isActive);
		requestWrapper.setRequest(titleDto);
		
		searchWrapper = new RequestWrapper<>();
		SearchDto searchDto = new SearchDto();
		String columnName = "titleName", searchType = FilterTypeEnum.EQUALS.toString();
		String value = "tvm-center", fromValue = "c", toValue = "r";
		SearchFilter searchFilter = new SearchFilter(value, fromValue, toValue, columnName, searchType);
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
		searchWrapper.setRequest(searchDto);
		
		filterWrapper = new RequestWrapper<>();
		FilterValueDto filterValueDto = new FilterValueDto();
		String text = "MRS", filterType = FilterColumnEnum.UNIQUE.toString();
		FilterDto filter = FilterDto.builder().columnName(columnName).type(filterType).text(text).build();
		List<FilterDto> filters = Arrays.asList(filter);
		filterValueDto.setFilters(filters);
		filterValueDto.setLanguageCode(langCode);
		List<SearchFilter> optionalFilters = Arrays.asList(searchFilter);
		filterValueDto.setOptionalFilters(optionalFilters);
		filterWrapper.setRequest(filterValueDto);
	}
	
	@Test
	public void getAllTitlesFailure() throws Exception {
		//when
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}

	@Ignore
	@Test
	public void t0getAllTitlesFailureNotFound() throws Exception {
		//when
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	public void t0getTitlesBylangCodeNotFound() throws Exception {
		//given
		String langCode = "tam";
		//when
		String uri = "/title/" + langCode;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	public void getTitlesBylangCodeFailure() throws Exception {
		//given
		String langCode = "tam";
		//when
		String uri = "/title/" + langCode;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t1saveTitleInsert() throws Exception {
		//when
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(requestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t1saveTitleFailure() throws Exception {
		//when
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(requestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t01updateTitleFailureNotFound() throws Exception {
		//given
		//when
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(requestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t01updateTitleFailureNotFound1() throws Exception {
		//given
		//when
		requestWrapper.getRequest().setIsActive(false);
		String uri = "/title";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(requestWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t0deleteTitleFailure() throws Exception {
		//given
		String code = "tam";
		//when
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		String uri = "/title/" + code;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON);
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllTitlesWithPagination() throws Exception {
		//given
		String pageNumber = "0", pageSize = "10", sortBy = "createdDateTime", orderBy = "desc";
		//when
		String uri = "/title/all";
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
	public void t2searchTitlesFailure() throws Exception {
		//given
		//when
		String uri = "/title/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t2searchTitlesFailureContains() throws Exception {
		//given
		searchWrapper.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		//when
		String uri = "/title/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t2searchTitlesFailureStartswith() throws Exception {
		//given
		searchWrapper.getRequest().getFilters().get(0).setType(FilterTypeEnum.STARTSWITH.toString());
		//when
		String uri = "/title/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				TitleErrorCode.TITLE_NOT_FOUND.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesFailureUnique() throws Exception {
		//given
		//when
		String uri = "/title/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(filterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesFailureAll() throws Exception {
		//given
		setValueInFilter(FilterColumnEnum.ALL.toString());
		//when
		String uri = "/title/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(filterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), null);
	}
	@Test
	@WithUserDetails("global-admin")
	public void filterTemplatesFailureEmpty() throws Exception {
		//given
		setValueInFilter(FilterColumnEnum.EMPTY.toString());
		//when
		String uri = "/title/filtervalues";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(filterWrapper));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(), 
				ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorCode());
	}
	
	private TitleDto titleDto(String langCode, String code, String titleName, 
			String titleDescription, boolean isActive) {
		TitleDto titleDto = new TitleDto();
		titleDto.setCode(code);
		titleDto.setLangCode(langCode);
		titleDto.setTitleName(titleName);
		titleDto.setTitleDescription(titleDescription);
		titleDto.setIsActive(isActive);
		return titleDto;
	}
	
	void setValueInFilter(String type) {
		filterWrapper.getRequest().getFilters().get(0).setType(type);
	}
}
