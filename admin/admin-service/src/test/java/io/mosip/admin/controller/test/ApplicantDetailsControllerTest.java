package io.mosip.admin.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.admin.util.AdminDataUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantDetailsControllerTest {

    @Autowired
    public MockMvc mockMvc;

    private ObjectMapper mapper;

    @MockBean
    private AuditUtil auditUtil;

    @MockBean
    RestClient restClient;

    MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${RETRIEVE_IDENTITY_API}")
    String retrieveIdentityUrl;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

    }

    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void getApplicantDetailsTest() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/identity.json").toURI())), StandardCharsets.UTF_8);
        mockRestServiceServer.expect(requestTo(retrieveIdentityUrl+"/10001101910003320220425050433?type=bio"))
                .andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                null);
    }

    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void getApplicantDetailsWithEmptyIdentityJsonTest() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/emptyIdentity.json").toURI())), StandardCharsets.UTF_8);
        mockRestServiceServer.expect(requestTo(retrieveIdentityUrl+"/10001101910003320220425050433?type=bio"))
                .andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                "ADM-AVD-003");
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void getApplicantDetailsFailTest() throws Exception {
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                "KER-MSD-500");
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void getApplicantDetailsWithInvalidRidTest() throws Exception {
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/100011019100033202")).andReturn()),
                "KER-IDV-304");
    }

    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void getApplicantLoginDetails() throws Exception {
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantDetails/getLoginDetails")).andReturn()),
                null);
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void testGetRIDDigitalCardSuccess() throws Exception {
        String data="dHN0bWFzIGRzYWttZ2FzIGRma3M=";
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/digitalCardStatusResponseJson.json").toURI())), StandardCharsets.UTF_8);
        Mockito.when(restClient.getApi(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(),
                                Mockito.any(Class.class))).thenReturn(str);
        Mockito.when(restClient.getApi(Mockito.any(),
                Mockito.any(Class.class))).thenReturn(data.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.get("/rid-digital-card/11234567897").param("isAcknowledged","true")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_PDF_VALUE));
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void testGetRIDDigitalCardFailure() throws Exception {
        AdminDataUtil.checkErrorResponse(
                mockMvc.perform(MockMvcRequestBuilders.get("/rid-digital-card/11234567897").param("isAcknowledged","false"))
                        .andReturn(),
                "ADM-AVD-006");
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void testGetRIDDigitalCardNotFoundFailure() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/digitalCardStatusUnavailableResponseJson.json").toURI())), StandardCharsets.UTF_8);
        Mockito.when(restClient.getApi(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(),
                Mockito.any(Class.class))).thenReturn(str);
        AdminDataUtil.checkErrorResponse(
                mockMvc.perform(MockMvcRequestBuilders.get("/rid-digital-card/11234567897").param("isAcknowledged","true"))
                        .andReturn(),
                "ADM-AVD-005");
    }
    @Test
    @WithUserDetails(value = "digitalcard-admin")
    public void testGetRIDDigitalCardFailureEmptyResponse() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/emptyResponse.json").toURI())), StandardCharsets.UTF_8);
        Mockito.when(restClient.getApi(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(),
                Mockito.any(Class.class))).thenReturn(str);
        AdminDataUtil.checkErrorResponse(
                mockMvc.perform(MockMvcRequestBuilders.get("/rid-digital-card/11234567897").param("isAcknowledged","true"))
                        .andReturn(),
                "ADM-AVD-007");
    }
}

