package io.mosip.kernel.masterdata.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mosip.kernel.masterdata.dto.*;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import org.junit.Assert;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserZoneCenterMappingTest {

    @Autowired
    private ZoneUserService zoneUserService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${zone.user.details.url}")
    private String userDetailsUri;

    @MockBean
    private AuditUtil auditUtil;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    ZoneUserRepository zoneUserRepository;
 
    public void setup() {
      String response = "{\r\n" +
                "  \"id\": null,\r\n" +
                "  \"version\": null,\r\n" +
                "  \"responsetime\": \"2021-03-31T04:27:31.590Z\",\r\n" +
                "  \"metadata\": null,\r\n" +
                "  \"response\": {\r\n" +
                "    \"mosipUserDtoList\": [\r\n" +
                "      {\r\n" +
                "        \"userId\": \"110005\",\r\n" +
                "        \"mobile\": null,\r\n" +
                "        \"mail\": \"110005@xyz.com\",\r\n" +
                "        \"langCode\": null,\r\n" +
                "        \"userPassword\": null,\r\n" +
                "        \"name\": \"Test110005 Auto110005\",\r\n" +
                "        \"role\": \"ZONAL_ADMIN,GLOBAL_ADMIN\",\r\n" +
                "        \"token\": null,\r\n" +
                "        \"rid\": null\r\n" +
                "      },\r\n" +
                "      {\r\n" +
                "        \"userId\": \"110006\",\r\n" +
                "        \"mobile\": null,\r\n" +
                "        \"mail\": \"110006@xyz.com\",\r\n" +
                "        \"langCode\": null,\r\n" +
                "        \"userPassword\": null,\r\n" +
                "        \"name\": \"Test110006 Auto110006\",\r\n" +
                "        \"role\": \"REGISTRATION_OPERATOR\",\r\n" +
                "        \"token\": null,\r\n" +
                "        \"rid\": null\r\n" +
                "      }\r\n" +
                "    ]\r\n" +
                "  },\r\n" +
                "  \"errors\": null\r\n" +
                "}";
        MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin"))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(requestTo(userDetailsUri + "/admin?search=110006"))
                .andRespond(withSuccess().body(response).contentType(MediaType.APPLICATION_JSON));

        Mockito.doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test01CreateUserZoneMappingTest() {
        ZoneUserDto zoneUserDto = new ZoneUserDto();
        zoneUserDto.setUserId("110005");
        zoneUserDto.setZoneCode("RBT");
        zoneUserDto.setIsActive(true);
        zoneUserDto.setLangCode("eng");
        ZoneUserExtnDto result = zoneUserService.createZoneUserMapping(zoneUserDto);
        Assert.assertEquals(zoneUserDto.getZoneCode(), result.getZoneCode());
        Assert.assertEquals(zoneUserDto.getUserId(), result.getUserId());
        Assert.assertEquals(false, result.getIsActive());
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test02CreateUserZoneMappingWithInvalidZoneTest() {
        ZoneUserDto zoneUserDto = new ZoneUserDto();
        zoneUserDto.setUserId("110005");
        zoneUserDto.setZoneCode("MOR");
        zoneUserDto.setIsActive(false);
        zoneUserDto.setLangCode("eng");
        String errorCode = null;
        try {
            zoneUserService.createZoneUserMapping(zoneUserDto);
        } catch (RequestException requestException) {
            errorCode = requestException.getErrorCode();
        }
        Assert.assertEquals("KER-USR-014", errorCode);  //Admin not authorized to access for this Zone
    }

    @Test
    @WithUserDetails("global-admin")
    public void test03CreateDuplicateUserZoneMappingTest() {
        ZoneUserDto zoneUserDto = new ZoneUserDto();
        zoneUserDto.setUserId("110005");
        zoneUserDto.setZoneCode("RBT");
        zoneUserDto.setLangCode("eng");
        String errorCode = null;

        try {
            zoneUserService.createZoneUserMapping(zoneUserDto);
        } catch (MasterDataServiceException exception) {
            exception.printStackTrace();
            errorCode = exception.getErrorCode();
        }
        Assert.assertEquals("KER-USR-018", errorCode); //Duplicate request

        zoneUserDto.setZoneCode("CST");
        try {
            zoneUserService.createZoneUserMapping(zoneUserDto);
        } catch (MasterDataServiceException exception) {
            errorCode = exception.getErrorCode();
        }
        Assert.assertEquals("KER-USR-021", errorCode); //The given user already mapped with different zone
    }

    @Test
    @WithUserDetails("global-admin")
    public void test04UpdateUserZoneMappingTest() {
        ZoneUserPutDto zoneUserPutDto = new ZoneUserPutDto();
        zoneUserPutDto.setUserId("global-admin");
        zoneUserPutDto.setZoneCode("NTH");
        zoneUserPutDto.setLangCode("eng");
        ZoneUserExtnDto result = zoneUserService.updateZoneUserMapping(zoneUserPutDto);
        Assert.assertEquals(zoneUserPutDto.getUserId(), result.getUserId());
        Assert.assertEquals(zoneUserPutDto.getZoneCode(), result.getZoneCode());
        Assert.assertEquals(true, result.getIsActive());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test05UpdateUserZoneMappingTestWithInvalidUserId() {
        ZoneUserPutDto zoneUserPutDto = new ZoneUserPutDto();
        zoneUserPutDto.setUserId("110006");
        zoneUserPutDto.setZoneCode("RBT");
        String errorCode = null;
        try {
            zoneUserService.updateZoneUserMapping(zoneUserPutDto);
        } catch (MasterDataServiceException masterDataServiceException) {
            errorCode = masterDataServiceException.getErrorCode();
        }
        Assert.assertEquals("KER-USR-017", errorCode);

    }

    @Test
    @WithUserDetails("reg-admin")
    public void test06ActivateUserZoneMappingTest() {
        StatusResponseDto statusResponseDto = zoneUserService.updateZoneUserMapping("110005", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        ZoneUser zoneUser = zoneUserService.getZoneUser("zonal-admin", "RSK");
        Assert.assertEquals(true, zoneUser.getIsActive());
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test07ActivateUserZoneMappingTestWithInvalidUser() {
        String errorCode = null;
        try {
            zoneUserService.updateZoneUserMapping("11000", true);
        } catch (DataNotFoundException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-019", errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test08DeactivateUserZoneMappingTest() {
        StatusResponseDto statusResponseDto = zoneUserService.updateZoneUserMapping("zonal-admin", false);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        ZoneUser zoneUser = zoneUserService.getZoneUser("zonal-admin", "RSK");
        Assert.assertEquals(false, zoneUser.getIsActive());
        Assert.assertEquals(false, zoneUser.getIsDeleted());
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test09CreateUserCenterMappingTestWithInactiveZoneUser() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("110005");
        userDetailsDto.setRegCenterId("10001");
        userDetailsDto.setLangCode("eng");
        String errorCode = null;
        try {
            userDetailsService.createUser(userDetailsDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-008", errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test10CreateUserCenterMappingTestWithInvalidCenter() {
        StatusResponseDto statusResponseDto = zoneUserService.updateZoneUserMapping("110005", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("110005");
        userDetailsDto.setRegCenterId("10003");
        userDetailsDto.setLangCode("eng");
        String errorCode = null;
        try {
            userDetailsService.createUser(userDetailsDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-008", errorCode);
    }

    @Test
    @WithUserDetails("global-admin")
    public void test11CreateUserCenterMappingTest() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("global-admin");
        userDetailsDto.setRegCenterId("10002");
        userDetailsDto.setLangCode("eng");
        UserDetailsCenterMapping userDetailsCenterMapping = userDetailsService.createUser(userDetailsDto);
        Assert.assertEquals(userDetailsDto.getRegCenterId(), userDetailsCenterMapping.getRegCenterId());
        Assert.assertEquals(userDetailsDto.getId(), userDetailsCenterMapping.getId());
        Assert.assertEquals(false, userDetailsCenterMapping.getIsActive());
    }
    
    @Test
    @WithUserDetails("global-admin")
    public void test11CreateUserCenterMappingTest1() throws JsonProcessingException, Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("29");
        userDetailsDto.setRegCenterId("11");
        userDetailsDto.setLangCode("eng");
        String errorCode = null;
        try {
            userDetailsService.createUser(userDetailsDto);
        } catch (DataNotFoundException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-MSD-215", errorCode);
          
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test12CreateUserCenterMappingTestWithoutZoneUser() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("110006");
        userDetailsDto.setRegCenterId("10001");
        userDetailsDto.setLangCode("eng");
        String errorCode = null;
        try {
            userDetailsService.createUser(userDetailsDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-008", errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test13CreateUserCenterMappingTestDuplicate() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("global-admin");
        userDetailsDto.setRegCenterId("10001");
        userDetailsDto.setLangCode("eng");
        String errorCode = null;
        try {
            userDetailsService.createUser(userDetailsDto);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-013", errorCode);
    }

    @Test
    @WithUserDetails("global-admin")
    public void test14UpdateUserCenterMappingTest() {
        UserDetailsPutReqDto userDetailsDto = new UserDetailsPutReqDto();
        userDetailsDto.setId("global-admin");
        userDetailsDto.setRegCenterId("10002");
        userDetailsDto.setLangCode("eng");
        UserDetailsPutDto userDetailsPutDto = userDetailsService.updateUser(userDetailsDto);
        Assert.assertEquals(userDetailsDto.getRegCenterId(), userDetailsPutDto.getRegCenterId());
        Assert.assertEquals(userDetailsDto.getId(), userDetailsPutDto.getId());

        UserDetailsGetExtnDto userDetailsGetExtnDto = userDetailsService.getUser("global-admin");
        Assert.assertEquals(false, userDetailsGetExtnDto.getIsActive());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test15ActivateUserCenterMappingTest() {
        StatusResponseDto statusResponseDto = userDetailsService.updateUserStatus("global-admin", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        UserDetailsGetExtnDto userDetailsGetExtnDto = userDetailsService.getUser("global-admin");
        Assert.assertEquals(true, userDetailsGetExtnDto.getIsActive());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test16DeactivateUserCenterMappingTest() {
        StatusResponseDto statusResponseDto = userDetailsService.updateUserStatus("global-admin", false);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        UserDetailsGetExtnDto userDetailsGetExtnDto = userDetailsService.getUser("global-admin");
        Assert.assertEquals(false, userDetailsGetExtnDto.getIsActive());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test17UpdateZoneUserWithActiveUserCenterMapping() {
        StatusResponseDto statusResponseDto = userDetailsService.updateUserStatus("global-admin", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        ZoneUserPutDto zoneUserPutDto = new ZoneUserPutDto();
        zoneUserPutDto.setUserId("110005");
        zoneUserPutDto.setZoneCode("RBT");
        String errorCode = null;
        try {
            zoneUserService.updateZoneUserMapping(zoneUserPutDto);
        }catch (MasterDataServiceException exception) {
            errorCode = exception.getErrorCode();
        }
        Assert.assertEquals(null, errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test18UpdateZoneUserStatusWithActiveUserCenterMapping() {
        String errorCode = null;
        try {
            zoneUserService.updateZoneUserMapping("110005", true);
        }catch (MasterDataServiceException exception) {
            errorCode = exception.getErrorCode();
        }
        Assert.assertEquals(null, errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test19UpdateZoneUserStatusWithActiveUserCenterMapping() {
        String errorCode = null;
        try {
            zoneUserService.updateZoneUserMapping("110005", false);
        }catch (MasterDataServiceException exception) {
            errorCode = exception.getErrorCode();
        }
        Assert.assertEquals(null, errorCode);
    }

    @Test
    @WithUserDetails("global-admin")
    public void test20UpdateZoneUserStatusWithInActiveUserCenterMapping() {
        StatusResponseDto statusResponseDto = userDetailsService.updateUserStatus("global-admin", false);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        statusResponseDto = zoneUserService.updateZoneUserMapping("global-admin", false);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        statusResponseDto = zoneUserService.updateZoneUserMapping("global-admin", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));
    }

    @Test
    @WithUserDetails("global-admin")
    public void test21DeleteZoneUserWithActiveUserCenterMapping() {
        StatusResponseDto statusResponseDto = userDetailsService.updateUserStatus("global-admin", true);
        Assert.assertTrue(statusResponseDto.getStatus().contains("Status updated successfully"));

        String errorCode = null;
        try {
            zoneUserService.deleteZoneUserMapping("110005", "RSK");
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-017", errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test22DeleteZoneUserWithInvalidUserId() {
        String errorCode = null;
        try {
            zoneUserService.deleteZoneUserMapping("110007", "RSK");
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-017", errorCode);
    }

    @Test
    @WithUserDetails("reg-admin")
    public void test23DeleteUserCenterMappingWithInvalidUserId() {
        String errorCode = null;
        try {
            userDetailsService.deleteUser("110007");
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-007", errorCode);
    }

    @Test
    @WithUserDetails("global-admin")
    public void test24DeleteUserCenterMapping() {
        IdResponseDto responseDto = userDetailsService.deleteUser("4");
        Assert.assertEquals("4", responseDto.getId());

        UserDetails userDetails = userDetailsRepository.findUserDetailsById("4");
        Assert.assertEquals("4", userDetails.getId());
        Assert.assertEquals(false, userDetails.getIsActive());
        Assert.assertEquals(true, userDetails.getIsDeleted());
    }

    @Test
    @WithUserDetails("global-admin")
    public void test25DeleteUserZoneMapping() {
        IdResponseDto responseDto = zoneUserService.deleteZoneUserMapping("4", "RSK");
        Assert.assertEquals("4", responseDto.getId());

        ZoneUser zoneUser = zoneUserRepository.findByUserId("4");
        Assert.assertEquals("4", zoneUser.getUserId());
        Assert.assertEquals(false, zoneUser.getIsActive());
        Assert.assertEquals(true, zoneUser.getIsDeleted());
    }


    //inactive user-center, update user-zone to different zone, re-activate user-center
   /* @Test
    @WithUserDetails("global-admin")
    public void test26() {
        ZoneUserDto zoneUserDto = new ZoneUserDto();
        zoneUserDto.setUserId("110006");
        zoneUserDto.setZoneCode("RBT");
        zoneUserDto.setLangCode("eng");
        zoneUserService.createZoneUserMapping(zoneUserDto);
        zoneUserService.updateZoneUserMapping("110006", true);

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("110006");
        userDetailsDto.setRegCenterId("10001");
        userDetailsDto.setLangCode("eng");
        userDetailsService.createUser(userDetailsDto);
        userDetailsService.updateUserStatus("110006", true);

        userDetailsService.updateUserStatus("110006", false);
        ZoneUserPutDto zoneUserPutDto = new ZoneUserPutDto();
        zoneUserPutDto.setUserId("110006");
        zoneUserPutDto.setZoneCode("CST");
        zoneUserService.updateZoneUserMapping(zoneUserPutDto);

        String errorCode = null;
        try {
            userDetailsService.updateUserStatus("110006", true);
        } catch (MasterDataServiceException e) {
            errorCode = e.getErrorCode();
        }
        Assert.assertEquals("KER-USR-015", errorCode);
    }

    @Test
    @WithUserDetails("global-admin")
    public void test27() {
        ZoneUserDto zoneUserDto = new ZoneUserDto();
        zoneUserDto.setUserId("4");
        zoneUserDto.setZoneCode("RSK");
        zoneUserDto.setLangCode("eng");
        zoneUserService.createZoneUserMapping(zoneUserDto);
        zoneUserService.updateZoneUserMapping("4", true);

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId("4");
        userDetailsDto.setRegCenterId("10002");
        userDetailsDto.setLangCode("eng");
        userDetailsService.createUser(userDetailsDto);
        userDetailsService.updateUserStatus("4", true);

        userDetailsService.updateUserStatus("4", false);
        ZoneUserPutDto zoneUserPutDto = new ZoneUserPutDto();
        zoneUserPutDto.setUserId("4");
        zoneUserPutDto.setZoneCode("CST");
        zoneUserService.updateZoneUserMapping(zoneUserPutDto);
    }*/
}
