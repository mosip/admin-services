package io.mosip.kernel.masterdata.test.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.test.TestBootApplication;

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
	
	ZoneUser zoneUser = null;
	
	@Before
	public void setUp() {
		zoneUser = new ZoneUser();
		zoneUser.setCreatedBy("system");
		zoneUser.setIsActive(true);
		zoneUser.setLangCode("eng");
		zoneUser.setUserId("110124");
		zoneUser.setZoneCode("NTH");
	}
	
	@Test
	public void getZoneUserTest() {
		Mockito.when(zoneUserRepo.findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(zoneUser);
		ZoneUser response = zoneUserService.getZoneUser("110124", "eng", "NTH");
		assertEquals("eng", response.getLangCode());
	}
	
	@Test
	public void getZoneUserTest_01() {
		Mockito.when(zoneUserRepo.findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(null);
		ZoneUser response = zoneUserService.getZoneUser("110124", "eng", "NTH");
		assertEquals(null, response);
	}
}
