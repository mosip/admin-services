package io.mosip.admin.packetstatusupdater;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.service.impl.AuditManagerProxyServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuditManagerProxyServiceTest {

    @Value("${mosip.kernel.audit.manager.api}")
    String auditmanagerapi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuditManagerProxyServiceImpl auditManagerProxyService;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        mockRestServiceServer=MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(requestTo(auditmanagerapi)).andRespond(withSuccess());

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        ReflectionTestUtils.setField(auditManagerProxyService, "request", request);
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("testset");
    }

    @Test
    @WithUserDetails("zonal-admin")
    public void auditLogTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.REFERER, "ttest");
        headers.put(HttpHeaders.ORIGIN, "dev.mosip.net");
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
        auditManagerRequestDto.setDescription("Test description");

        auditManagerRequestDto.setApplicationName("Admin Portal");
        auditManagerRequestDto.setApplicationId("10009");
        auditManagerRequestDto.setSessionUserName("Test");
        auditManagerRequestDto.setSessionUserId("Test");
        auditManagerRequestDto.setHostIp("Test");
        auditManagerRequestDto.setHostName("Test");

        auditManagerRequestDto.setEventId("ADM-045");
        auditManagerRequestDto.setEventName("Click: Clicked on Home Page");
        auditManagerRequestDto.setEventType("Navigation: Page View Event");
        auditManagerRequestDto.setId("NO_ID");
        auditManagerRequestDto.setIdType("ADMIN");
        auditManagerRequestDto.setModuleId("ADM-NAV");
        auditManagerRequestDto.setModuleName("Navigation");
        auditManagerProxyService.logAdminAudit(auditManagerRequestDto, headers);
    }

    @Test
    public void auditLogEmptyValuesTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.REFERER, "ttest");
        headers.put(HttpHeaders.ORIGIN, "dev.mosip.net");
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
        auditManagerRequestDto.setDescription("Test description");

        auditManagerRequestDto.setApplicationName("");
        auditManagerRequestDto.setApplicationId("");
        auditManagerRequestDto.setSessionUserName("");
        auditManagerRequestDto.setSessionUserId("");
        auditManagerRequestDto.setHostIp("");
        auditManagerRequestDto.setHostName("");

        auditManagerRequestDto.setEventId("");
        auditManagerRequestDto.setEventName("");
        auditManagerRequestDto.setEventType("");
        auditManagerRequestDto.setId("");
        auditManagerRequestDto.setIdType("");
        auditManagerRequestDto.setModuleId("");
        auditManagerRequestDto.setModuleName("");

        String errorCode = null;
        try {
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto, headers);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidEventIdTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.REFERER, "ttest");
        headers.put(HttpHeaders.ORIGIN, "dev.mosip.net");
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
        auditManagerRequestDto.setDescription("Test description");
        auditManagerRequestDto.setApplicationName("Admin Portal");
        auditManagerRequestDto.setApplicationId("10009");
        auditManagerRequestDto.setSessionUserName("Test");
        auditManagerRequestDto.setSessionUserId("Test");
        auditManagerRequestDto.setHostIp("Test");
        auditManagerRequestDto.setHostName("Test");

        auditManagerRequestDto.setEventId("ADM-0452");
        auditManagerRequestDto.setEventName("Click: Clicked on Home Page");
        auditManagerRequestDto.setEventType("Navigation: Page View Event");
        auditManagerRequestDto.setId("NO_ID");
        auditManagerRequestDto.setIdType("ADMIN");
        auditManagerRequestDto.setModuleId("ADM-NAV");
        auditManagerRequestDto.setModuleName("Navigation");

        String errorCode = null;
        try {
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto, headers);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidEventNameTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.REFERER, "ttest");
        headers.put(HttpHeaders.ORIGIN, "dev.mosip.net");
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
        auditManagerRequestDto.setDescription("Test description");
        auditManagerRequestDto.setApplicationName("Admin Portal");
        auditManagerRequestDto.setApplicationId("10009");
        auditManagerRequestDto.setSessionUserName("Test");
        auditManagerRequestDto.setSessionUserId("Test");
        auditManagerRequestDto.setHostIp("Test");
        auditManagerRequestDto.setHostName("Test");

        auditManagerRequestDto.setEventId("ADM-045");
        auditManagerRequestDto.setEventName("Clicked: Clicked on Home Page");
        auditManagerRequestDto.setEventType("Navigation: Page View Event");
        auditManagerRequestDto.setId("NO_ID");
        auditManagerRequestDto.setIdType("ADMIN");
        auditManagerRequestDto.setModuleId("ADM-NAV");
        auditManagerRequestDto.setModuleName("Navigation");

        String errorCode = null;
        try {
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto, headers);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidActionTimestampTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.REFERER, "ttest");
        headers.put(HttpHeaders.ORIGIN, "dev.mosip.net");
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(10));
        auditManagerRequestDto.setDescription("Test description");
        auditManagerRequestDto.setApplicationName("Admin Portal");
        auditManagerRequestDto.setApplicationId("10009");
        auditManagerRequestDto.setSessionUserName("Test");
        auditManagerRequestDto.setSessionUserId("Test");
        auditManagerRequestDto.setHostIp("Test");
        auditManagerRequestDto.setHostName("Test");

        auditManagerRequestDto.setEventId("ADM-045");
        auditManagerRequestDto.setEventName("Click: Clicked on Home Page");
        auditManagerRequestDto.setEventType("Navigation: Page View Event");
        auditManagerRequestDto.setId("NO_ID");
        auditManagerRequestDto.setIdType("ADMIN");
        auditManagerRequestDto.setModuleId("ADM-NAV");
        auditManagerRequestDto.setModuleName("Navigation");

        String errorCode = null;
        try {
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto, headers);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

}
