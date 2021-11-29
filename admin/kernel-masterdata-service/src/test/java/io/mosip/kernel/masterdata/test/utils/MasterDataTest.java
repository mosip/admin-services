package io.mosip.kernel.masterdata.test.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;

public class MasterDataTest {

	public static void checkResponse(MvcResult rst, String expectedCode) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (rst.getResponse().getContentAsString().isEmpty() && rst.getResponse().getStatus() == 404) {
				assertEquals(rst.getResponse().getStatus(), 404);

			} else {
				Map m = mapper.readValue(rst.getResponse().getContentAsString(), Map.class);
				assertEquals(rst.getResponse().getStatus(), 200);
				if (m.containsKey("errors") && null != m.get("errors")) {
//					assertEquals(((List<Map<String, String>>) m.get("errors")).get(0).get("errorCode"), actualCode);
					assertEquals(expectedCode, ((List<Map<String, String>>) m.get("errors")).get(0).get("errorCode"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public static RequestWrapper<SearchDtoWithoutLangCode> commonSearchDtoWithoutLangCode(String sortField,String seachSort,String columnName,String value,String type)
	{
		RequestWrapper<SearchDtoWithoutLangCode> sr = new RequestWrapper<>();
		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort(sortField, seachSort);
		ss.add(s);
		sf.setColumnName(columnName);
		sf.setType(type);
		sf.setValue(value);
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		sr.setRequest(sc);
		return sr;
	}

	public static RequestWrapper<SearchDto> commonSearchDto(String seachSortField,String seachSortFiled,String columnName,String value,String type)
	{
		RequestWrapper<SearchDto> sr = new RequestWrapper<>();
		SearchDto sc = new SearchDto();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort(seachSortField, seachSortFiled);
		ss.add(s);
		sf.setColumnName(columnName);
		sf.setType(type);
		sf.setValue(value);
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		sr.setRequest(sc);
		return sr;
	}
	
	public static RequestWrapper<FilterValueDto> commonFilterValueDto(String columnName,String txt,String type)
	{
	    RequestWrapper<FilterValueDto> filValDto;
		FilterValueDto f = new FilterValueDto();
		FilterDto fdto = new FilterDto();
		fdto.setColumnName(columnName);
		fdto.setText(txt);
		fdto.setType(type);
		List<FilterDto> lf = new ArrayList<>();
		lf.add(fdto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);
		return filValDto;
	}
	

}
