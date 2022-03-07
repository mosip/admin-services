package io.mosip.kernel.masterdata.test.integration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.ValidationErrorCode;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MasterdataFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MasterDataFilterHelper masterDataFilterHelper;

    @Autowired
    private RegistrationCenterRepository registrationCenterRepository;

    @MockBean
    private AuditUtil auditUtil;

    @Before
    public void init() {
        doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void test1() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("unique");
        filterDto.setText(null);
        filterDto.setColumnName("isActive");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode(null);
        filterValueDto.setPageFetch(3);
        filterValueDto.setOptionalFilters(null);
        filterValueDto.setTotalCountRequired(true);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(2, filterResult.getFilterData().size());
        Assert.assertEquals(2, filterResult.getTotalCount());
    }

    @Test
    public void test2() {
        FilterDto filterDto = new FilterDto();
        filterDto.setColumnName("isDeleted");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setPageFetch(3);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(2, filterResult.getFilterData().size());
        Assert.assertEquals(2, filterResult.getTotalCount());
    }

    @Test
    public void test3() {
        FilterDto filterDto = new FilterDto();
        filterDto.setColumnName("lunchStartTime");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setPageFetch(3);
        try {
            masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
            Assert.fail();
        } catch (MasterDataServiceException ex) {
            Assert.assertEquals(ex.getErrorCode(), ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorCode());
        }
    }

    @Test
    public void nullFilterTextReturnsEmptyResult() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("unique");
        filterDto.setText(null);
        filterDto.setColumnName("name");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode(null);
        filterValueDto.setPageFetch(3);
        filterValueDto.setTotalCountRequired(true);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
        Assert.assertTrue(registrationCenterRepository.count() > 0);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(0, filterResult.getFilterData().size());
        Assert.assertEquals(0, filterResult.getTotalCount());
    }

    @Test
    public void emptyFilterTextTest() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("unique");
        filterDto.setText("");
        filterDto.setColumnName("name");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode(null);
        filterValueDto.setPageFetch(3);
        filterValueDto.setTotalCountRequired(false);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);

        long totalCount = registrationCenterRepository.count();
        Assert.assertTrue(totalCount > 0);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(filterValueDto.getPageFetch(), filterResult.getFilterData().size());
        Assert.assertEquals(0, filterResult.getTotalCount());
    }

    @Test
    public void emptyFilterTypeTest() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("");
        filterDto.setText("");
        filterDto.setColumnName("name");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode(null);
        filterValueDto.setPageFetch(3);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
        long totalCount = registrationCenterRepository.count();
        Assert.assertTrue(totalCount > 0);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(filterValueDto.getPageFetch(), filterResult.getFilterData().size());
        Assert.assertEquals(0, filterResult.getTotalCount());
    }

    @Test
    public void allFilterTypeTest() {
        int total = (int) registrationCenterRepository.count();
        FilterDto filterDto = new FilterDto();
        filterDto.setType("all");
        filterDto.setText("");
        filterDto.setColumnName("name");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode(null);
        filterValueDto.setPageFetch(total+1);
        filterValueDto.setTotalCountRequired(true);
        FilterResult filterResult = masterDataFilterHelper.filterValues(RegistrationCenter.class, filterDto, filterValueDto);
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(total, filterResult.getFilterData().size());
        Assert.assertEquals(total, filterResult.getTotalCount());
    }

    @Test
    @WithUserDetails("global-admin")
    public void centerFilterTest() throws Exception {
        FilterDto filterDto = new FilterDto();
        filterDto.setColumnName("id");
        filterDto.setType("all");
        filterDto.setText("");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));
        filterValueDto.setLanguageCode("eng");
        RequestWrapper<FilterValueDto> requestDto = new RequestWrapper<>();
        requestDto.setRequest(filterValueDto);
        String json = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(post("/registrationcenters/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        ResponseWrapper<FilterResponseCodeDto> wrapper = objectMapper.readValue(response, new TypeReference<ResponseWrapper<FilterResponseCodeDto>>() {});
        Assert.assertNotNull(wrapper.getResponse());
        Assert.assertEquals(0, wrapper.getResponse().getTotalCount());
        long totalCount = registrationCenterRepository.count();
        Assert.assertTrue(wrapper.getResponse().getFilters().size() <= totalCount);
    }

    @Test
    @WithUserDetails("global-admin")
    public void centerFilterTestWithTotalCount() throws Exception {
        FilterDto filterDto = new FilterDto();
        filterDto.setColumnName("id");
        filterDto.setType("all");
        filterDto.setText("");
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilters(Arrays.asList(filterDto));

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setType("equals");
        searchFilter.setValue("true");
        searchFilter.setColumnName("isActive");
        filterValueDto.setOptionalFilters(Arrays.asList(searchFilter));

        filterValueDto.setLanguageCode("eng");
        filterValueDto.setTotalCountRequired(true);
        RequestWrapper<FilterValueDto> requestDto = new RequestWrapper<>();
        requestDto.setRequest(filterValueDto);
        String json = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(post("/registrationcenters/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        ResponseWrapper<FilterResponseCodeDto> wrapper = objectMapper.readValue(response, new TypeReference<ResponseWrapper<FilterResponseCodeDto>>() {});
        Assert.assertNotNull(wrapper.getResponse());
        long totalCount = registrationCenterRepository.count();
        Assert.assertTrue(wrapper.getResponse().getTotalCount() <= totalCount);
        Assert.assertTrue(wrapper.getResponse().getFilters().size() <= totalCount);

        mockMvc.perform(post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/registrationcentertypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/individualtypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/machinespecifications/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/holidays/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/locations/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/templates/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/title/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/machines/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        filterDto.setColumnName("code");
        json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/machinetypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/devicetypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/devicespecifications/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/documentcategories/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/documenttypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gendertypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/devices/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());


        filterDto.setColumnName("word");
        json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/blocklistedwords/filtervalues").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

}
