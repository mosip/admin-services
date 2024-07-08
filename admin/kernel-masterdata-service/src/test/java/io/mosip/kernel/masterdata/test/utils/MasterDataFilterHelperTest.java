package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.entity.Application;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MasterDataFilterHelperTest {

	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;
	
	@MockBean
	private TemplateService templateService;

	@Test
	public void filterValuesTest() {
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("f");
		filterDto.setType("all");
		List<FilterDto> filterDtoList = new ArrayList<>();
		FilterValueDto valueDto = new FilterValueDto();
		valueDto.setFilters(filterDtoList);
		valueDto.setLanguageCode("eng");
		FilterResult<?> filterResult = masterDataFilterHelper.filterValues(Application.class, filterDto, valueDto);
		assertThat(filterResult.getFilterData().isEmpty(), is(false));
	}
	
	@Test
	public void filterValuesTest01() {
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("f");
		filterDto.setType("all");
		List<FilterDto> filterDtoList = new ArrayList<>();
		FilterValueDto valueDto = new FilterValueDto();
		List<SearchFilter> optionalFilters =  new ArrayList<>();
		SearchFilter optionalFilter1 =  new SearchFilter();
		optionalFilter1.setValue("true");
		optionalFilter1.setType("equals");
		optionalFilter1.setColumnName("isActive");
		SearchFilter optionalFilter2 =  new SearchFilter();
		optionalFilter2.setColumnName("name");
		optionalFilter2.setType("contains");
		optionalFilter2.setValue("f*");
		optionalFilters.add(optionalFilter1);
		optionalFilters.add(optionalFilter2);
		valueDto.setFilters(filterDtoList);
		valueDto.setOptionalFilters(optionalFilters);
		valueDto.setLanguageCode("eng");
		FilterResult<?> filterResult = masterDataFilterHelper.filterValues(Application.class, filterDto, valueDto);
		assertThat(filterResult.getFilterData().isEmpty(), is(false));
	}
	@Test
	public void filterValuesTest02() {
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("f");
		filterDto.setType("all");
		List<FilterDto> filterDtoList = new ArrayList<>();
		FilterValueDto valueDto = new FilterValueDto();
		List<SearchFilter> optionalFilters =  new ArrayList<>();
		SearchFilter optionalFilter1 =  new SearchFilter();
		optionalFilter1.setValue("true");
		optionalFilter1.setType("equals");
		optionalFilter1.setColumnName("isActive");
		SearchFilter optionalFilter2 =  new SearchFilter();
		optionalFilter2.setColumnName("name");
		optionalFilter2.setType("contains");
		optionalFilter2.setValue("*f*");
		optionalFilters.add(optionalFilter1);
		optionalFilters.add(optionalFilter2);
		valueDto.setFilters(filterDtoList);
		valueDto.setOptionalFilters(optionalFilters);
		valueDto.setLanguageCode("eng");
		FilterResult<?> filterResult = masterDataFilterHelper.filterValues(Application.class, filterDto, valueDto);
		assertThat(filterResult.getFilterData().isEmpty(), is(false));
	}
	@Test
	public void filterValuesTest03() {
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("f");
		filterDto.setType("all");
		List<FilterDto> filterDtoList = new ArrayList<>();
		FilterValueDto valueDto = new FilterValueDto();
		List<SearchFilter> optionalFilters =  new ArrayList<>();
		SearchFilter optionalFilter1 =  new SearchFilter();
		optionalFilter1.setValue("true");
		optionalFilter1.setType("equals");
		optionalFilter1.setColumnName("isActive");
		SearchFilter optionalFilter2 =  new SearchFilter();
		optionalFilter2.setColumnName("name");
		optionalFilter2.setType("contains");
		optionalFilter2.setValue("*f");
		optionalFilters.add(optionalFilter1);
		optionalFilters.add(optionalFilter2);
		valueDto.setFilters(filterDtoList);
		valueDto.setOptionalFilters(optionalFilters);
		valueDto.setLanguageCode("eng");
		FilterResult<?> filterResult = masterDataFilterHelper.filterValues(Application.class, filterDto, valueDto);
		assertThat(filterResult.getFilterData().isEmpty(), is(true));
	}
}
