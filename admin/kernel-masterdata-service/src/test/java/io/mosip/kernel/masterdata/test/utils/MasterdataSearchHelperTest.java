package io.mosip.kernel.masterdata.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.OptionalFilter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MasterdataSearchHelperTest {

	@Autowired
	MasterdataSearchHelper searchHelper;
	@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;

	@MockBean
	private TemplateService templateService;

	private SearchFilter noColumnFilter;
	private SearchFilter noBetweenValueFilter;
	private SearchFilter noValuefilter;
	private SearchFilter filter;
	private SearchFilter betweenfilter;
	private SearchSort noColumnSort;
	private SearchSort sort;
	private Pagination page;
	private Pagination invalidPage;
	private SearchFilter startWithFilter;
	private SearchFilter wildCardFilter1;
	private SearchFilter wildCardFilter2;
	private SearchFilter wildCardFilter3;
	private SearchFilter wildCardFilter4;
	private SearchFilter wildCardFilter5;
	private SearchFilter wildCardFilter6;
	private SearchFilter wildCardFilter7;
	private SearchFilter noFilterType;
	private SearchFilter searchwithDateFilter;
	private OptionalFilter[] optionalFilterArray;

	@Before
	public void setup() {
		noColumnFilter = new SearchFilter();
		noColumnFilter.setType("equals");
		noColumnFilter.setValue("REG");

		noBetweenValueFilter = new SearchFilter();
		noBetweenValueFilter.setColumnName("createdDateTime");
		noBetweenValueFilter.setType("between");

		noValuefilter = new SearchFilter();
		noValuefilter.setColumnName("name");
		noValuefilter.setType("equals");

		betweenfilter = new SearchFilter();
		betweenfilter.setColumnName("createdDateTime");
		betweenfilter.setType("between");
		betweenfilter.setFromValue("2019-01-01T01:01:01.000Z");
		betweenfilter.setToValue("2019-01-07T01:01:01.000Z");

		filter = new SearchFilter();
		filter.setColumnName("name");
		filter.setType("contains");
		filter.setValue("*mosip*");

		startWithFilter = new SearchFilter();
		startWithFilter.setColumnName("name");
		startWithFilter.setType("startswith");
		startWithFilter.setValue("mosip*");

		wildCardFilter1 = new SearchFilter();
		wildCardFilter1.setColumnName("name");
		wildCardFilter1.setType("contains");
		wildCardFilter1.setValue("mosip");

		wildCardFilter2 = new SearchFilter();
		wildCardFilter2.setColumnName("name");
		wildCardFilter2.setType("contains");
		wildCardFilter2.setValue("mosip*");

		wildCardFilter3 = new SearchFilter();
		wildCardFilter3.setColumnName("name");
		wildCardFilter3.setType("contains");
		wildCardFilter3.setValue("*mosip");

		wildCardFilter4 = new SearchFilter();
		wildCardFilter4.setColumnName("name");
		wildCardFilter4.setType("contains");
		wildCardFilter4.setValue("*");

		wildCardFilter5 = new SearchFilter();
		wildCardFilter5.setColumnName("name");
		wildCardFilter5.setType("contains");
		wildCardFilter5.setValue("**");

		wildCardFilter6 = new SearchFilter();
		wildCardFilter6.setColumnName("name");
		wildCardFilter6.setType("contains");
		wildCardFilter6.setValue("* *");

		wildCardFilter7 = new SearchFilter();
		wildCardFilter7.setColumnName("name");
		wildCardFilter7.setType("contains");
		wildCardFilter7.setValue("* s");

		noColumnSort = new SearchSort();
		noColumnSort.setSortType("desc");

		sort = new SearchSort();
		sort.setSortField("updatedDateTime");
		sort.setSortType("asc");

		page = new Pagination();
		page.setPageStart(1);
		page.setPageFetch(100);

		invalidPage = new Pagination();
		invalidPage.setPageFetch(0);
		invalidPage.setPageStart(-1);

		noFilterType = new SearchFilter();
		noFilterType.setColumnName("name");

		searchwithDateFilter = new SearchFilter();
		searchwithDateFilter.setType("equals");
		searchwithDateFilter.setValue("2019-01-01T01:01:01.000Z");
		searchwithDateFilter.setColumnName("createdDateTime");

		optionalFilterArray = new OptionalFilter[1];
		optionalFilterArray[0] = new OptionalFilter(Arrays.asList(betweenfilter));
	}
	
	@Test
	public void searchMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchWithOptionalFilterMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test
	public void searchConstainsFilter1Masterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter1), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test
	public void searchConstainsFilter2Masterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter2), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test
	public void searchStartsWithFilterMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter2), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}
	@Test
	public void searchSortDescFilterMasterdata() {
		sort.setSortType("desc");
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(sort), page, "eng");

		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}
	@Test(expected = RequestException.class)
	public void searchInvalidPaginationMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(sort), invalidPage, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}
	@Test(expected = RequestException.class)
	public void searchInvalidBetweenFilterMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(noBetweenValueFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}
	@Test(expected = RequestException.class)
	public void searchNoColumnFilterMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(noColumnFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test(expected = RequestException.class)
	public void searchNoValueFilterMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(noValuefilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test(expected = RequestException.class)
	public void searchNoSortColumnMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test(expected = RequestException.class)
	public void searchNoColumnMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(filter), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test(expected = RequestException.class)
	public void searchNoFilterTypeMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(noFilterType), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, optionalFilterArray);
	}

	@Test
	public void searchWithDateMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(searchwithDateFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchStartsWithMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(startWithFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchwildcardSearchMasterdata() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter3), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchStartswithWildcardMasterdata01() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter4), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchStartswithWildcardMasterdata02() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter5), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchStartswithWildcardMasterdata03() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter6), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}

	@Test
	public void searchStartswithWildcardMasterdata04() {
		SearchDto searchDto = new SearchDto(Arrays.asList(wildCardFilter7), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdata(RegistrationCenter.class, searchDto, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void searchMasterdataWithoutLangCode01(){
		ArrayList<Entity> entity = new ArrayList<Entity>();
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		OptionalFilter optionalFilter = new OptionalFilter();
		Mockito.when(searchHelper.searchMasterdataWithoutLangCode(null,searchDtoWithoutLangCode, new OptionalFilter[]{optionalFilter}))
				.thenReturn(null);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode02(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter),Arrays.asList(sort),page,"eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class,searchDtoWithoutLangCode,null);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode03(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter),Arrays.asList(sort),page,"eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class,searchDtoWithoutLangCode,optionalFilterArray);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode04(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(wildCardFilter1), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode05(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(wildCardFilter2), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode06() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter), Arrays.asList(sort), invalidPage, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode07() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(noBetweenValueFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode08() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(noColumnFilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode09() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(noValuefilter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode10() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode11() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test(expected = RequestException.class)
	public void searchMasterdataWithoutLangCode12() {
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(noFilterType), Arrays.asList(noColumnSort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode13() {
		sort.setSortType("desc");
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test
	public void searchMasterdataWithoutLangCode14() {
		sort.setSortType("asc");
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode(Arrays.asList(filter), Arrays.asList(sort), page, "eng");
		searchHelper.searchMasterdataWithoutLangCode(RegistrationCenter.class, searchDtoWithoutLangCode, optionalFilterArray);
	}
	
	@Test
	public void getMissingDataTest(){
		String tableName = null;
		String langCode = null;
		searchHelper.getMissingData(tableName,langCode);
	}
	
	@Test(expected = Exception.class)
	public void WithoutFilterMasterdata() {
		searchHelper.searchMasterdata(RegistrationCenter.class, null, optionalFilterArray);
	}
	
	@Test(expected = NullPointerException.class)
	public void sortQuery01(){
		List<SearchSort> searchSorts=new ArrayList<>();
		SearchSort ss=new SearchSort("abc","bgd");
		searchSorts.add(ss);
		ReflectionTestUtils.invokeMethod(searchHelper,"sortQuery",null,null,null,searchSorts);
	}
	
	@Test(expected = RequestException.class)
	public void sortQuery02(){
		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		Root root = mock(Root.class);
		CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
		List<SearchSort> searchSorts=new ArrayList<>();
		SearchSort ss=new SearchSort(null,"ASC");
		searchSorts.add(ss);
		ReflectionTestUtils.invokeMethod(searchHelper,"sortQuery",builder,root,criteriaQuery,searchSorts);
	}
	
	@Test
	public void sortQuery03(){
		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		Root root = mock(Root.class);
		CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
		List<SearchSort> searchSorts=new ArrayList<>();
		SearchSort sort1 = new SearchSort();
		sort1.setSortField("abc");
		sort1.setSortType("ASC");
		searchSorts.add(sort1);
		ReflectionTestUtils.invokeMethod(searchHelper,"sortQuery",builder,root,criteriaQuery,searchSorts);
	}
	
	@Test
	public void setLangCodeTest(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setLangCode",null,null,null);
	}
	
	@Test (expected = NullPointerException.class)
	public void setBetweenValue01(){
		SearchFilter searchFilter = null;
		ReflectionTestUtils.invokeMethod(searchHelper,"setBetweenValue",null,null,searchFilter);
	}
	
	@Test (expected = IllegalStateException.class)
	public void setBetweenValue02(){
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setValue("123");
		searchFilter.setType("filter");
		ReflectionTestUtils.invokeMethod(searchHelper,"setBetweenValue",null,RegistrationCenter.class,searchFilter);
	}
	
	@Test (expected = IllegalStateException.class)
	public void setBetweenValue03(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setBetweenValue");
	}
	
	@Test (expected = NullPointerException.class)
	public void setBetweenValue04(){
		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		Root root = mock(Root.class);
		SearchFilter filter = new SearchFilter("1","2000-01-01","2010-01-01","deviceName","equals");
		javax.persistence.criteria.Path<Object> path = root.get(filter.getColumnName());
		Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
		ReflectionTestUtils.invokeMethod(searchHelper,"setBetweenValue",builder,root,filter);
	}
	
	@SneakyThrows
	@Test (expected = MissingMethodInvocationException.class)
	public void parseDataType01(){
		Root root = mock(Root.class);
		Path<Object> path = mock(Path.class);
		Class<? extends Object> type = Class.forName(LocalDateTime.class.getName());
		Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
		String fieldType = type.getTypeName();
		Mockito.when(type.getTypeName()).thenReturn(fieldType);
		String column = "first";
		String value = "one";
		ReflectionTestUtils.invokeMethod(searchHelper,"parseDataType",root,column,value);
	}
	
	@Test (expected = NullPointerException.class)
	public void parseDataType02(){
		Root root = mock(Root.class);
		root.getModel();
		Path<Object> path = mock(Path.class);
		path = root.get(filter.getColumnName());
		Class<? extends Object> type = path.getJavaType();
		Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
		String fieldType = type.getTypeName();
		Mockito.when(type.getTypeName()).thenReturn(fieldType);
		String column = "first";
		String value = "one";
		ReflectionTestUtils.invokeMethod(searchHelper,"parseDataType",root,column,value);
	}
	
	@Test(expected = NullPointerException.class)
	public void filterTypesTest01(){
		SearchFilter filter = null;
		ReflectionTestUtils.invokeMethod(searchHelper,"FilterTypes",filter);
	}
	
	@Test
	public void filterTypesTest02(){
		SearchFilter filter = new SearchFilter();
		filter.setValue("123");
		filter.setType("filter");
		ReflectionTestUtils.invokeMethod(searchHelper,"FilterTypes",filter);
	}
	
	@Test
	public void validateSort01(){
		SearchSort searchSort = new SearchSort("id","asc");
		ReflectionTestUtils.invokeMethod(searchHelper,"validateSort",searchSort);
	}
	
	@Test (expected = RequestException.class)
	public void validateSort02(){
		SearchSort searchSort = new SearchSort(null,null);
		ReflectionTestUtils.invokeMethod(searchHelper,"validateSort",searchSort);
		assertEquals(null,searchSort);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeMachineQuerySearch01(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>(2);
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"filter",zones,false);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeMachineQuerySearch02(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		StringBuilder nativeQuery = null;
		List<Zone> zones = new ArrayList<>(2);
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"filter",zones,true);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeMachineQuerySearch03(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>();
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"filter",zones,true);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeMachineQuerySearch04(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>();
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"filter",zones,false);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeMachineQuerySearch05(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>();
		Iterator<SearchFilter> searchIterator = searchDtoWithoutLangCode.getFilters().iterator();
		SearchFilter searchFilter = searchIterator.next();
		SearchFilter searchFilter1 = searchFilter;
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"searchFilter",zones,true);
	}
	
	@Test (expected = InvalidDataAccessApiUsageException.class)
	public void nativeMachineQuerySearchIterator(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<SearchFilter> filters = new ArrayList<>();
		SearchFilter filter1 = new SearchFilter();
		filter1.setColumnName("name");
		filter1.setValue("value");
		filter1.setType("equals");
		filters.add(filter1);
		searchDtoWithoutLangCode.setFilters(filters);
		Pagination pagination = new Pagination();
		pagination.setPageStart(0);
		pagination.setPageFetch(10);
		searchDtoWithoutLangCode.setPagination(pagination);
		String typeName = "typeName";
		List<Zone> zones= new ArrayList<>();
		Zone zone1 = new Zone();
		zone1.setCode("java");
		zone1.setLangCode("eng");
		zone1.setHierarchyPath("hierarchy");
		zone1.setHierarchyLevel((short)1);
		zone1.setParentZoneCode("Parent");
		zone1.setName("name");
		zone1.setHierarchyName("name");
		zone1.setIsActive(true);
		zones.add(zone1);
		boolean isAssigned = true;
		searchHelper.nativeMachineQuerySearch(searchDtoWithoutLangCode,"filter",zones,isAssigned);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeDeviceQuerySearch01(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>(2);
		searchHelper.nativeDeviceQuerySearch(searchDtoWithoutLangCode,"filter",zones,true);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeDeviceQuerySearch02(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>(2);
		searchHelper.nativeDeviceQuerySearch(searchDtoWithoutLangCode,"filter",zones,false);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeDeviceQuerySearch03(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>();
		searchHelper.nativeDeviceQuerySearch(searchDtoWithoutLangCode,"filter",zones,false);
	}
	
	@Test (expected = NullPointerException.class)
	public void nativeDeviceQuerySearch04(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<Zone> zones = new ArrayList<>();
		searchHelper.nativeDeviceQuerySearch(searchDtoWithoutLangCode,"filter",zones,true);
	}
	
	@Test (expected = InvalidDataAccessApiUsageException.class)
	public void nativeDeviceQuerySearchIterator(){
		SearchDtoWithoutLangCode searchDtoWithoutLangCode = new SearchDtoWithoutLangCode();
		List<SearchFilter> filters = new ArrayList<>();
		SearchFilter filter1 = new SearchFilter();
		filter1.setColumnName("name");
		filter1.setValue("value");
		filter1.setType("equals");
		filters.add(filter1);
		searchDtoWithoutLangCode.setFilters(filters);
		Pagination pagination = new Pagination();
		pagination.setPageStart(0);
		pagination.setPageFetch(10);
		searchDtoWithoutLangCode.setPagination(pagination);
		String typeName = "typeName";
		List<Zone> zones= new ArrayList<>();
		Zone zone1 = new Zone();
		zone1.setCode("java");
		zone1.setLangCode("eng");
		zone1.setHierarchyPath("hierarchy");
		zone1.setHierarchyLevel((short)1);
		zone1.setParentZoneCode("Parent");
		zone1.setName("name");
		zone1.setHierarchyName("name");
		zone1.setIsActive(true);
		zones.add(zone1);
		boolean isAssigned = true;
		searchHelper.nativeDeviceQuerySearch(searchDtoWithoutLangCode,null,zones,isAssigned);
	}
	
	@Test (expected = NullPointerException.class)
	public void getColumnName01(){
		ReflectionTestUtils.invokeMethod(searchHelper,"getColumnName",null,null);
	}
	
	@Test (expected = IllegalStateException.class)
	public void getColumnName02(){
		String fieldName="abc";
		ReflectionTestUtils.invokeMethod(searchHelper,"getColumnName",fieldName);
	}
	
	@Test
	public void getColumnName03(){
		ReflectionTestUtils.invokeMethod(searchHelper,"getColumnName",RegistrationCenter.class,"0");
	}
	
	@Test (expected = IllegalStateException.class)
	public void getColumnName04(){
		Entity entity = mock(Entity.class);
		String fieldName = "dynamic";
		ReflectionTestUtils.invokeMethod(searchHelper,"getColumnName",entity,fieldName);
	}
	
	@Test (expected = NullPointerException.class)
	public void fetchMissingValues01(){
		Class<Entity> entity = Entity.class;
		String langCode="eng";
		String idFieldName="123field";
		String fieldName="dynamic";
		Mockito.when(searchHelper.fetchMissingValues(entity,langCode,idFieldName,fieldName)).thenReturn(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void fetchMissingValues02(){
		Class<Entity> entity = Entity.class;
		String langCode="eng";
		String idFieldName="123field";
		String colName = filter.getColumnName();
		String fieldName = null;
		fieldName = colName;
		Mockito.when(searchHelper.fetchMissingValues(entity,langCode,idFieldName,fieldName)).thenReturn(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void fetchMissingValues03(){
		Class<Entity> entity = Entity.class;
		String langCode="eng";
		String idFieldName="1";
		String fieldName = "dynamic";
		Mockito.when(searchHelper.fetchMissingValues(entity,langCode,idFieldName,fieldName)).thenReturn(null);
	}
	
	@Test (expected = IllegalStateException.class)
	public void setDeviceQueryParams01(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setDeviceQueryParams",Query.class, null);
	}
	
	@Test (expected = NullPointerException.class)
	public void setDeviceQueryParams02(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setDeviceQueryParams",null, null);
	}
	
	@Test
	public void setDeviceQueryParams03(){
		SearchFilter searchFilter1 = new SearchFilter("1","","","deviceName","equals");
		SearchFilter searchFilter2 = new SearchFilter("2","","","isActive","equals");
		SearchFilter searchFilter3 = new SearchFilter("3","","","macAddress","equals");
		SearchFilter searchFilter4 = new SearchFilter("4","","","serialNum","equals");
		SearchFilter searchFilter5 = new SearchFilter("5","","","deviceSpecId","equals");
		List<SearchFilter> searchFilterList = new ArrayList<>();
		searchFilterList.add(searchFilter1);
		searchFilterList.add(searchFilter2);
		searchFilterList.add(searchFilter3);
		searchFilterList.add(searchFilter4);
		searchFilterList.add(searchFilter5);
		Query query = mock(Query.class);
		ReflectionTestUtils.invokeMethod(searchHelper,"setDeviceQueryParams",query,searchFilterList);
	}
	
	@Test (expected = IllegalStateException.class)
	public void setMachineQueryParams01(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setMachineQueryParams",Query.class, null);
	}
	
	@Test (expected = NullPointerException.class)
	public void setMachineQueryParams02(){
		ReflectionTestUtils.invokeMethod(searchHelper,"setMachineQueryParams",null, null);
	}
	
	@Test
	public void setMachineQueryParamsTest(){
		SearchFilter searchFilter1 = new SearchFilter("1","","","name","equals");
		SearchFilter searchFilter2 = new SearchFilter("2","","","isActive","equals");
		SearchFilter searchFilter3 = new SearchFilter("3","","","macAddress","equals");
		SearchFilter searchFilter4 = new SearchFilter("4","","","serialNum","equals");
		SearchFilter searchFilter5 = new SearchFilter("5","","","machineSpecId","equals");
		List<SearchFilter> searchFilterList = new ArrayList<>();
		searchFilterList.add(searchFilter1);
		searchFilterList.add(searchFilter2);
		searchFilterList.add(searchFilter3);
		searchFilterList.add(searchFilter4);
		searchFilterList.add(searchFilter5);
		Query query = mock(Query.class);
		ReflectionTestUtils.invokeMethod(searchHelper,"setMachineQueryParams",query,searchFilterList);
	}
	
	@Test (expected = RequestException.class)
	public void filterTypeNull(){
		SearchFilter searchFilter = new SearchFilter("1","","","name","equals");
		searchFilter.setValue("2");
		searchFilter.setType(null);
		ReflectionTestUtils.invokeMethod(searchHelper,"FilterTypes",searchFilter);
	}
	
	@Test (expected = RequestException.class)
	public void validateSortNull(){
		SearchSort sort = new SearchSort();
		sort.setSortType("hg");
		sort.setSortField(null);
		ReflectionTestUtils.invokeMethod(searchHelper,"validateSort",sort);
	}
	
	@Test (expected = NullPointerException.class)
	public void buildFiltersNull(){
		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		Root root = mock(Root.class);
		SearchFilter filter = null;
		ReflectionTestUtils.invokeMethod(searchHelper,"buildFilters",builder,root,null);
	}
	
	@Test (expected = IllegalStateException.class)
	public void validateFiltersNull(){
		ReflectionTestUtils.invokeMethod(searchHelper,"validateFilters",null);
	}
	
	@SneakyThrows
	@Test (expected = NoSuchFieldException.class)
	public void getColumnName001(){
		String fieldName = "dynamic";
		Entity entity = mock(Entity.class);
		Field field = entity.getClass().getDeclaredField(fieldName);
		ReflectionTestUtils.invokeMethod(searchHelper,"getColumnName",field,entity);
	}
	
	@Test (expected = RequestException.class)
	public void validateFilter01(){
		SearchFilter filter = new SearchFilter();
		filter.setValue(filter.getValue());
		filter.setType(filter.getType());
		filter.setColumnName(filter.getColumnName());
		ReflectionTestUtils.invokeMethod(searchHelper,"validateFilter",filter);
	}
	
	@Test
	public void validateFilter02(){
		SearchFilter filter = new SearchFilter();
		filter.setValue("123");
		filter.setType("equals");
		filter.setColumnName("device");
		ReflectionTestUtils.invokeMethod(searchHelper,"validateFilter",filter);
	}
	
	@Test
	public void sortQuery001(){
		CriteriaBuilder builder = mock(CriteriaBuilder.class);
		Root root = mock(Root.class);
		CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
		List<SearchSort> sortFilter = new ArrayList<>();
		SearchSort sort1 = new SearchSort();
		sort1.setSortField("dynamic");
		sort1.setSortType("equals");
		sortFilter.add(sort1);
		ReflectionTestUtils.invokeMethod(searchHelper,"sortQuery",builder,root,criteriaQuery,sortFilter);
	}
}
