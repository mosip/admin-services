package io.mosip.kernel.masterdata.test.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.constant.PacketWorkflowErrorCode;
import io.mosip.kernel.masterdata.dto.PacketWorkflowResumeRequestDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

public class PacketWorkflowControllerTest extends AbstractTest {

	private PacketWorkflowResumeRequestDto packetWorkflowResumeRequestDto;
	
	private RequestWrapper<SearchDtoWithoutLangCode> searchPacketWorkflowRequest;
	
	@Before
	public void setUp() {
		packetWorkflowResumeRequestDto = new PacketWorkflowResumeRequestDto();
		String workflowId = "123", workflowAction = "add";
		packetWorkflowResumeRequestDto.setWorkflowId(workflowId);
		packetWorkflowResumeRequestDto.setWorkflowAction(workflowAction);
		
		searchPacketWorkflowRequest = new RequestWrapper<>();
		SearchDtoWithoutLangCode searchDto = new SearchDtoWithoutLangCode();
		String value = "test", fromValue = "e", toValue = "t", 
				columnName = "name", searchType = FilterTypeEnum.EQUALS.toString(), langCode = "eng";
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
		searchPacketWorkflowRequest.setRequest(searchDto);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void resumePacketWorkflowFailure() throws Exception {
		//when
		String uri = "/packet/resume";
		//then
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(packetWorkflowResumeRequestDto))).andReturn(),
				"KER-MSD-999");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureEquals() throws Exception {
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureContains() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.CONTAINS.toString());
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureStartWith() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.STARTSWITH.toString());
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureBetween() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.BETWEEN.toString());
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
//				ValidationErrorCode.FILTER_NOT_SUPPORTED.getErrorCode());
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureIn() throws Exception {
		//given
		setValueInSearch(FilterTypeEnum.IN.toString());
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void searchPacketWorkflowFailureEmpty() throws Exception {
		//given
		setValueInSearch("");
		//when
		String uri = "/packet/search";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(searchPacketWorkflowRequest));
		//then
		MasterDataTest.checkResponse(mockMvc.perform(requestBuilder).andReturn(),
				PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode());
	}
	
	void setValueInSearch(String type) {
		searchPacketWorkflowRequest.getRequest().getFilters().get(0).setType(type);
	}
}
