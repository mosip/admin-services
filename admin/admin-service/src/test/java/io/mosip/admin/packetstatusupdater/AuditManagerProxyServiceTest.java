package io.mosip.admin.packetstatusupdater;

import io.mosip.admin.TestBootApplication;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import io.mosip.admin.packetstatusupdater.service.impl.AuditManagerProxyServiceImpl;
import io.mosip.kernel.core.util.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<String> list = new ArrayList<>();
        list.add("origin");
        list.add("referrer");
        list.add("x-forwarded-for");
        Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(list));
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("testset");
    }

    @Test
    @WithUserDetails("zonal-admin")
    public void auditLogTest() {
        AuditManagerRequestDto auditManagerRequestDto = new AuditManagerRequestDto();
        auditManagerRequestDto.setActionTimeStamp(DateUtils.getUTCCurrentDateTime());
        auditManagerRequestDto.setDescription("Test description");
        auditManagerRequestDto.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

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
        auditManagerProxyService.logAdminAudit(auditManagerRequestDto);
    }

    @Test
    public void auditLogEmptyValuesTest() {
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
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidEventIdTest() {
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
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidEventNameTest() {
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
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

    @Test
    public void auditLogInvalidActionTimestampTest() {
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
            auditManagerProxyService.logAdminAudit(auditManagerRequestDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("ADM-PKT-005", errorCode);
    }

}
