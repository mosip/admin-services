package io.mosip.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.util.AdminDataUtil;
import io.mosip.admin.util.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class ApplicantControllerTest {

    @Autowired
    public MockMvc mockMvc;

    private ObjectMapper mapper;

    @MockBean
    private AuditUtil auditUtil;

    @MockBean
    RestClient restClient;

    MockRestServiceServer mockRestServiceServer;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${RETRIEVE_IDENTITY_API}")
    String retrieveIdentityUrl;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/identity.json").toURI())), StandardCharsets.UTF_8);
        mapper.registerModule(new JavaTimeModule());
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        Mockito.when(restClient.getApi(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(),
                Mockito.any(Class.class))).thenReturn(str);
    }

    @Test
    @WithUserDetails(value = "global-admin")
    public void getApplicantDetailsTest() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/identity.json").toURI())), StandardCharsets.UTF_8);
        mockRestServiceServer.expect(requestTo(retrieveIdentityUrl+"/10001101910003320220425050433?type=bio"))
                .andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                null);
    }

    @Test
    @WithUserDetails(value = "global-admin")
    public void getApplicantDetailsWithEmptyIdentityJsonTest() throws Exception {
        String str = new String(Files.readAllBytes(Paths.get(getClass().getResource("/emptyIdentity.json").toURI())), StandardCharsets.UTF_8);
        mockRestServiceServer.expect(requestTo(retrieveIdentityUrl+"/10001101910003320220425050433?type=bio"))
                .andRespond(withSuccess().body(str).contentType(MediaType.APPLICATION_JSON));
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                "ADM-AVD-003");
    }
    @Test
    @WithUserDetails(value = "global-admin")
    public void getApplicantDetailsFailTest() throws Exception {
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/10001101910003320220425050433")).andReturn()),
                "KER-MSD-500");
    }
    @Test
    @WithUserDetails(value = "global-admin")
    public void getApplicantDetailsWithInvalidRidTest() throws Exception {
        AdminDataUtil.checkResponse(
                (mockMvc.perform(MockMvcRequestBuilders.get("/applicantVerficationDetails"+"/100011019100033202")).andReturn()),
                "KER-IDV-304");
    }
}

