package io.mosip.admin.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MasterDataProxyControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private PublisherClient<String, EventModel, HttpHeaders> publisher;

    @MockBean
    private AuditUtil auditUtil;

    @Autowired
    RestTemplate restTemplate;

    private ObjectMapper mapper;

    private String request=null;

    @Value("${mosip.admin.base.url}")
    private String baseUrl;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        request="{\"id\":null,\"version\":null,\"requesttime\":null,\"metadata\":null,\"request\":{\"id\":\"1002\",\"name\":\"Dell\",\"brand\":\"DELL\",\"model\":\"Dell\",\"machineTypeCode\":\"Vostro\",\"minDriverversion\":\"1.3\",\"description\":\"Dell brand\",\"langCode\":\"eng\",\"isActive\":true}}";
        doNothing().when(auditUtil).setAuditRequestDto(Mockito.any(),Mockito.any());
    }

    @Test
    @WithUserDetails("global-admin")
    public void t001postMethodMasterdataProxyTest() throws Exception {
        String reqUrl=baseUrl+"/v1/masterdata/machinespecifications";
        String response="{\"id\":null,\"version\":null,\"responsetime\":\"2022-03-10T09:00:19.686Z\",\"metadata\":null,\"response\":{\"id\":\"59331a62-cf13-45c0-a564-d5c555d8de75\",\"langCode\":\"eng\"},\"errors\":null}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(reqUrl))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.post("/masterdata/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                null);
    }
    @Test
    @WithUserDetails("global-admin")
    public void t002putMethodMasterdataProxyTest() throws Exception {
        String reqUrl=baseUrl+"/v1/masterdata/machinespecifications";
        String response="{\"id\":null,\"version\":null,\"responsetime\":\"2022-03-10T09:00:19.686Z\",\"metadata\":null,\"response\":{\"id\":\"59331a62-cf13-45c0-a564-d5c555d8de75\",\"langCode\":\"eng\"},\"errors\":null}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(reqUrl))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.put("/masterdata/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                null);
    }
    @Test
    @WithUserDetails("global-admin")
    public void t003patchMethodMasterdataProxyTest() throws Exception {
        String reqUrl=baseUrl+"/v1/masterdata/machinespecifications?id=59331a62-cf13-45c0-a564-d5c555d8de75&isActive=true";
        String response="{\"id\":null,\"version\":null,\"responsetime\":\"2022-03-10T18:02:47.984Z\",\"metadata\":null,\"response\":{\"status\":\"Status updated successfully for MachineSpecification\"},\"errors\":null}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(reqUrl))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.patch("/masterdata/machinespecifications?id=59331a62-cf13-45c0-a564-d5c555d8de75&isActive=true").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                null);
    }

    @Test
    @WithUserDetails("global-admin")
    public void t004deleteMethodMasterdataProxyTest() throws Exception {
        String reqUrl=baseUrl+"/v1/masterdata/machinespecifications/59331a62-cf13-45c0-a564-d5c555d8de75";
        String response="{\"id\":null,\"version\":null,\"responsetime\":\"2022-03-10T18:06:48.617Z\",\"metadata\":null,\"response\":{\"id\":\"59331a62-cf13-45c0-a564-d5c555d8de75\"},\"errors\":null}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(reqUrl))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.delete("/masterdata/machinespecifications/59331a62-cf13-45c0-a564-d5c555d8de75").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                null);
    }
    @Test
    @WithUserDetails("global-admin")
    public void t005getMethodMasterdataProxyTest() throws Exception {
        String reqUrl=baseUrl+"/v1/masterdata/machinespecifications/all?pageNumber=0&pageSize=10&sortBy=createdDateTime&orderBy=desc";
        String response="{\"id\":null,\"version\":null,\"responsetime\":\"2022-03-10T18:09:25.777Z\",\"metadata\":null,\"response\":{\"pageNo\":0,\"totalPages\":1,\"totalItems\":3,\"data\":[{\"isActive\":false,\"createdBy\":\"110006\",\"createdDateTime\":\"2022-03-10T09:00:19.689Z\",\"updatedBy\":\"110006\",\"updatedDateTime\":\"2022-03-10T18:02:47.998Z\",\"isDeleted\":true,\"deletedDateTime\":\"2022-03-10T18:06:48.633Z\",\"id\":\"59331a62-cf13-45c0-a564-d5c555d8de75\",\"name\":\"Dell\",\"brand\":\"DELL\",\"model\":\"Dell\",\"machineTypeCode\":\"MCH\",\"minDriverversion\":\"1.3\",\"description\":\"Dell brand\",\"langCode\":\"eng\",\"machineTypeName\":null},{\"isActive\":true,\"createdBy\":\"sysadmin\",\"createdDateTime\":\"2022-02-21T06:55:34.533Z\",\"updatedBy\":null,\"updatedDateTime\":null,\"isDeleted\":false,\"deletedDateTime\":null,\"id\":\" RESIDENT-1\",\"name\":\"Resident Virtual Machine\",\"brand\":\"Unkown\",\"model\":\"Unknown\",\"machineTypeCode\":\"RESIDENT-REG\",\"minDriverversion\":\"0\",\"description\":\"Resident Virtual Machine\",\"langCode\":\"eng\",\"machineTypeName\":null},{\"isActive\":false,\"createdBy\":\"sysadmin\",\"createdDateTime\":\"2022-02-21T06:55:34.533Z\",\"updatedBy\":\"110006\",\"updatedDateTime\":null,\"isDeleted\":true,\"deletedDateTime\":\"2022-03-10T07:18:10.711Z\",\"id\":\" RESIDENT-2\",\"name\":\"Resident Virtual Machine\",\"brand\":\"Unkown\",\"model\":\"Unknown\",\"machineTypeCode\":\"RESIDENT-REG\",\"minDriverversion\":\"0\",\"description\":\"Resident Virtual Machine\",\"langCode\":\"eng\",\"machineTypeName\":null}]},\"errors\":null}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(reqUrl))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                mockMvc.perform(MockMvcRequestBuilders.get("/masterdata/machinespecifications/all?pageNumber=0&pageSize=10&sortBy=createdDateTime&orderBy=desc").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                null);
    }
    @Test
    @WithUserDetails("global-admin")
    public void t006getMethodMasterdataProxyFailTest() throws Exception {
        AdminDataUtil.checkErrorResponse(
                mockMvc.perform(MockMvcRequestBuilders.get("/masterdata/machinespecifications/all?pageNumber=0&pageSize=10&sortBy=createdDateTime&orderBy=desc").contentType(MediaType.APPLICATION_JSON).content(request)).andReturn(),
                "KER-MSD-500");
    }

}
