package io.mosip.kernel.masterdata.test.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.ZoneRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.ZoneUtils;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ZoneUtilTest {

	@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;

	@MockBean
	private ZoneRepository zoneRepository;

	@MockBean
	private ZoneUserRepository zoneUserRepository;

	@Autowired
	private ZoneUtils zoneUtils;

	List<Zone> zones;
	List<ZoneUser> zoneUsers;

	@Before
	public void setup() {
		zones = new ArrayList<>();
		zones.add(new Zone("AAA", "ENG", "AAA", (short) 0, "AAA", null, "AAA"));
		zones.add(new Zone("BBB", "ENG", "AAA", (short) 0, "BBB", "AAA", "AAA/BBB"));
		zones.add(new Zone("CCC", "ENG", "AAA", (short) 0, "CCC", "AAA", "AAA/CCC"));
		zones.add(new Zone("DDD", "ENG", "AAA", (short) 0, "DDD", "AAA", "AAA/DDD"));
		zones.add(new Zone("AAA1", "ENG", "AAA", (short) 0, "AAA1", "BBB", "AAA/BBB/AAA1"));
		zones.add(new Zone("AAA2", "ENG", "AAA", (short) 0, "AAA2", "CCC", "AAA/CCC/AAA2"));
		zones.add(new Zone("AAA3", "ENG", "AAA", (short) 0, "AAA3", "DDD", "AAA/DDD/AAA3"));
		zones.add(new Zone("AAA4", "ENG", "AAA", (short) 0, "AAA4", "AAA3", "AAA/DDD/AAA3/AAA4"));

		zoneUsers = new ArrayList<>();
		ZoneUser user = new ZoneUser();
		user.setUserId("zonal-admin");
		user.setZoneCode("AAA");
		user.setLangCode("ENG");
		zoneUsers.add(user);
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void testGetZones() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doReturn(zoneUsers).when(zoneUserRepository).findByUserIdNonDeleted(Mockito.anyString());
		Zone zone = new Zone();
		zone.setCode("DDD");
		List<Zone> result = zoneUtils.getZones(zone);
		assertNotNull(result);
		assertNotEquals(0, result.size());

	}

	@Test
	@WithUserDetails("zonal-admin")
	public void testGetZonesFailure() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doReturn(zoneUsers).when(zoneUserRepository).findByUserIdNonDeleted(Mockito.anyString());
		List<Zone> result = zoneUtils.getUserZones();
		assertNotNull(result);
		assertNotEquals(0, result.size());
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("zonal-admin")
	public void testUserZoneFailure() {
		doThrow(DataRetrievalFailureException.class).when(zoneRepository).findAllNonDeleted();
		doReturn(zoneUsers).when(zoneUserRepository).findByUserIdNonDeleted(Mockito.anyString());
		zoneUtils.getUserZones();
	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("zonal-admin")
	public void  testUserZoneUserFailure() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doThrow(DataRetrievalFailureException.class).when(zoneUserRepository)
				.findByUserIdNonDeleted(Mockito.anyString());
		zoneUtils.getUserZones();

	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("zonal-admin")
	public void testUserZoneNotFound() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		List<Zone> result = zoneUtils.getUserZones();
		assertNotNull(result);
		assertNotEquals(0, result.size());
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void testZonesNotFound() {
		List<Zone> result = zoneUtils.getUserZones();
		assertTrue(result.isEmpty());
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void testZoneLeafSuccess() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doReturn(zoneUsers).when(zoneUserRepository).findByUserIdNonDeleted(Mockito.anyString());
		zoneUtils.getUserLeafZones("eng");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void testZoneLeafNoUserZone() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getUserLeafZones("eng");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void testChildZoneNoZone() {
		zoneUtils.getZones(zones.get(0));
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void getChildZonesTest(){
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doReturn(zoneUsers).when(zoneUserRepository).findByUserIdNonDeleted(Mockito.anyString());
		String zoneCode="AAA";
		List<Zone> result =zoneUtils.getChildZones(zoneCode);
		assertNotNull(result);
		assertNotEquals(0, result.size());
	}

	@WithUserDetails("zonal-admin")
	@Test(expected = IllegalStateException.class)
	public void getDescedantsTest_By_Zone(){
		ReflectionTestUtils.invokeMethod(zoneUtils,"getDescedants","zones","zone");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void getLeafZonesTest_By_ZoneCode(){
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getLeafZones("ENG","AAA");
	}

	@WithUserDetails("zonal-admin")
	@Test(expected = ClassCastException.class)
	public void getLeafZonesTest_By_LangCode(){
		String userId="AAA";
		ZoneUser zu=new ZoneUser();
		zu.setZoneCode("AAA");
		Mockito.when(zoneUserRepository.findZoneByUserIdActiveAndNonDeleted(Mockito.anyString())).thenReturn(zu);
		List<Zone> zones= zoneUtils.getLeafZones("ENG");
	}

	@WithUserDetails("zonal-admin")
	@Test(expected = MasterDataServiceException.class)
	public void getUserZonesByUserIdTest_By_UserId(){
		List<Zone> zones=new ArrayList<>();
		Zone zone=new Zone();
		zone.setCode("AAA");
		zone.setName("name");
		zones.add(zone);
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getUserZonesByUserId(zones,"AAA");
	}

	@WithUserDetails("zonal-admin")
	@Test(expected = MasterDataServiceException.class)
	public void getUserZonesByUserIdTest(){
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getUserZonesByUserId("AAA");
	}
	@WithUserDetails("zonal-admin")
	@Test(expected = MasterDataServiceException.class)
	public void getUserZonesByUserIdTest_Returns_UserZone(){
		List<ZoneUser> userZone = null;
		Mockito.when(zoneUserRepository.findByUserIdNonDeleted(Mockito.any())).thenReturn(userZone);
		zoneUtils.getUserZonesByUserId(zones,"AAA");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void getChildZoneListTest_By_ZoneCode_And_LangCode(){
		List<String> zoneIds=new ArrayList<>();
		List<Zone> zones = null;
		Zone zone=new Zone();
		Mockito.when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(Mockito.anyString(),Mockito.anyString())).thenReturn(zone);
		zoneUtils.getChildZoneList(zoneIds,"AAA","ENG");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void getZoneCodesTest(){
		List<Zone> zones=new ArrayList<>();
		zoneUtils.getZoneCodes(zones);
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void getSubZonesTest_By_LangCode(){
		ZoneUser zu=new ZoneUser();
		doReturn(zu).when(zoneUserRepository).findZoneByUserIdActiveAndNonDeleted(Mockito.anyString());
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getSubZones("ENG");
	}

	@Test
	public void getSubZonesBasedOnZoneCodeTest(){
		zoneUtils.getSubZonesBasedOnZoneCode("AAA");
	}
}
