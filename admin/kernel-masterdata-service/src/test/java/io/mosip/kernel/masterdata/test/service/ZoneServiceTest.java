package io.mosip.kernel.masterdata.test.service;



import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.masterdata.constant.ZoneUserErrorCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.utils.AuditUtil;

/**
 * 
 * @author Nagarjuna
 *
 */
@SpringBootTest(classes = TestBootApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ZoneServiceTest {

	@MockBean
	private ZoneUserRepository zoneUserRepo;
	
	@Autowired
	ZoneUserService zoneUserService;
	
	@MockBean
	private AuditUtil auditUtil;
	
	ZoneUser zoneUser = null;
	
	@Before
	public void setUp() {
		zoneUser = new ZoneUser();
		zoneUser.setCreatedBy("system");
		zoneUser.setIsActive(true);
		zoneUser.setLangCode("eng");
		zoneUser.setUserId("110124");
		zoneUser.setZoneCode("NTH");
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void getZoneUserTest() {
		Mockito.when(zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),Mockito.anyString())).thenReturn(zoneUser);
		ZoneUser response = zoneUserService.getZoneUser("110124",  "NTH");
		assertEquals("eng", response.getLangCode());
	}
	
	@Test
	public void getZoneUserTest_01() {
		Mockito.when(zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),Mockito.anyString())).thenReturn(null);
		ZoneUser response = zoneUserService.getZoneUser("110124", "NTH");
		assertEquals(null, response);
	}
	
	@Test
	public void zoneUserMappingTest() {
		Mockito.when(zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),Mockito.anyString())).thenReturn(null);
		Mockito.when(zoneUserRepo.findByUserId(Mockito.anyString())).thenReturn(zoneUser);
		
		ZoneUserDto request = new ZoneUserDto();
		request.setIsActive(true);
		request.setLangCode("fra");
		request.setUserId("11006");
		request.setZoneCode("KTR");
		try {
			zoneUserService.createZoneUserMapping(request);
		}catch(MasterDataServiceException ex) {
			assertEquals(ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorCode(), ex.getErrorCode());
		}
		
	}
	
	@Test
	public void getZoneUsersTest() {
		List<ZoneUser> zoneMappedUsers = new ArrayList<ZoneUser>();
		zoneMappedUsers.add(zoneUser);
		List<String> userIds = new ArrayList<String>();
		userIds.add("1234");
		userIds.add("45678");
		userIds.add("1264");
		userIds.add("45678");
		Mockito.when(zoneUserRepo.findByUserIds(userIds)).thenReturn(zoneMappedUsers);
		List<ZoneUser> users = zoneUserService.getZoneUsers(userIds);
		assertEquals("eng", users.get(0).getLangCode());
		
	}
}
