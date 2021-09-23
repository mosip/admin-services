package io.mosip.kernel.masterdata.test.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.entity.Application;
import io.mosip.kernel.masterdata.entity.Language;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;

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
		List<?> list = masterDataFilterHelper.filterValues(Application.class, filterDto, valueDto);
		assertThat(list.isEmpty(), is(false));
	}
}
