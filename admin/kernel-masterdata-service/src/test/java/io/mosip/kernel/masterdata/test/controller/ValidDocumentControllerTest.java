package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.ValidDocumentDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
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

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidDocumentControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private PublisherClient<String, EventModel, HttpHeaders> publisher;

    @MockBean
    private AuditUtil auditUtil;

    private ObjectMapper mapper;
    private RequestWrapper<ValidDocumentDto> document = new RequestWrapper<ValidDocumentDto>();

    @Before
    public void setUp() {

        doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ValidDocumentDto dto = new ValidDocumentDto();
        dto.setDocCategoryCode("POR");
        dto.setDocTypeCode("CIN");
        dto.setIsActive(true);
        dto.setLangCode("eng");
        document.setRequest(dto);
    }

    @Test
    @WithUserDetails("global-admin")
    public void createDocument_ValidData_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(document))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void createDocument_InvalidData_Fail() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(document))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void deleteValidDocumentTest_Fail() throws Exception {
        MasterDataTest.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.delete("/validdocuments/POI/CIN")).andReturn(), null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void deleteValidDocumentTest_Success() throws Exception {
        MasterDataTest.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.delete("/validdocuments/POI/CIN")).andReturn(), "KER-MSD-016");
    }

    @Test
    @WithUserDetails("global-admin")
    public void getValidDocumentByLangCodeTest_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/eng")).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void getValidDocumentByLangCodeFailTest_WithInvalidLangCode() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/eng1")).andReturn(),
                "KER-MSD-016");
    }

    @Test
    @WithUserDetails("global-admin")
    public void getAllValidDocumentTest_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/all")).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocumentTest_FailWithInvalidSort() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime1", "ASC", "docCategoryCode", "POI", "equals")))).andReturn(),
                "KER-MSD-357");
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocumentTest_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime", "desc", "docCategoryCode", "POI", "equals")))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocumentTest_SuccessWithContainsType() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime", "desc", "docCategoryCode", "POI", "contains")))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocumentTest_FailWithInvalidDoc() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime", "desc", "docCategoryCode", "testDummy", "contains")))).andReturn(),
                "KER-MSD-355");
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocumentFailTest_WithInvalidColumn() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime", "ASC", "docTypeCodee", "CIN", "contains")))).andReturn(),
                "KER-MSD-317");
    }

    @Test
    @WithUserDetails("global-admin")
    public void searchValidDocument_FailWithInvalidDoc() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonSearchDto("createdDateTime", "ASC", "docCategoryCode", "PINI", "equals")))).andReturn(),
                "KER-MSD-355");
    }


    @Test
    @WithUserDetails("global-admin")
    public void categoryTypeFilterValuesTest_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN", "all")))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void categoryTypeFilterValuesTest_WithInvalidFilterType() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN", "")))).andReturn(),
                "KER-MSD-322");
    }

    @Test
    @WithUserDetails("global-admin")
    public void categoryTypeFilterValuesTest_SuccessWithUniqueFilter() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCode", "CIN", "unique")))).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void categoryTypeFilterValuesFailTest_WithInvalidColumn() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/validdocuments/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(MasterDataTest.commonFilterValueDto("docTypeCodee", "CIN", "all")))).andReturn(),
                "KER-MSD-317");
    }

    @Test
    @WithUserDetails("global-admin")
    public void mapDocCategoryAndDocTypeTest_Fail() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
                "KER-MSD-360");
    }

    @Test
    @WithUserDetails("global-admin")
    public void mapDocCategoryAndDocTypeTest_withDBError() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/CIN")).andReturn(),
                "KER-MSD-212");
    }

    @Test
    @WithUserDetails("global-admin")
    public void mapDocCategoryAndDocTypeFailTest_WithAlreadyMappedDoc() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
                "KER-MSD-360");
    }


    @Test
    @WithUserDetails("global-admin")
    public void mapDocCategoryAndDocType_FailWithAlreadyMappedDoc() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/POA/COR")).andReturn(),
                "KER-MSD-360");
    }

    @Test
    @WithUserDetails("global-admin")
    public void unmapDocCategoryAndDocTypeTest_SuccessWithMapping() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA/COR")).andReturn(),
                "KER-MSD-271");
    }

    @Test
    @WithUserDetails("global-admin")
    public void unmapDocCategoryAndDocTypeTest_FailWithMappingNotFound() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/P1/C1")).andReturn(),
                "KER-MSD-361");
    }

    @Test
    @WithUserDetails("global-admin")
    public void unmapDocCategoryAndDocTypeTest_Fail() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/unmap/POA/COR")).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void getValidDocumentByDocCategoryCodeTest_Success() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/validdocuments/POI/eng")).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void getValidDocumentByDocCategoryCodeTest_WithDBError() throws Exception {
        MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/validdocuments/map/P1/C1")).andReturn(),
                "KER-MSD-212");
    }


}
