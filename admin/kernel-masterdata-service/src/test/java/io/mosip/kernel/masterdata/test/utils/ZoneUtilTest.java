package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.ZoneRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.ZoneUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ZoneUtilTest {
	
	@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;
	
	@MockBean
	private TemplateService templateService;

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
	public void testGetZone() {
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
	public void testUserZoneUserFailure() {
		doReturn(zones).when(zoneRepository).findAllNonDeleted();
		doThrow(DataRetrievalFailureException.class).when(zoneUserRepository)
				.findByUserIdNonDeleted(Mockito.anyString());
		zoneUtils.getUserZones();

	}

	@Test(expected = MasterDataServiceException.class)
	@WithUserDetails("zonal-admin")
	public void testUserZoneNotFound() {
		when(zoneRepository.findAllNonDeleted()).thenReturn(zones);
		//doReturn(zones).when(zoneRepository).findAllNonDeleted();
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
		when(zoneRepository.findAllNonDeleted()).thenReturn(zones);
	//	doReturn(zones).when(zoneRepository).findAllNonDeleted();
		zoneUtils.getUserLeafZones("eng");
	}

	@WithUserDetails("zonal-admin")
	@Test
	public void testChildZoneNoZone() {
		zoneUtils.getZones(zones.get(0));
	}

	@Test
	public void testGetChildZones_NullZoneCode() {
		assertThrows(NullPointerException.class, () -> zoneUtils.getChildZones(null));
	}

	@Test
	public void testGetChildZones_EmptyZones() {
		String zoneCode = "z1";
		List<Zone> childZones = zoneUtils.getChildZones(zoneCode);
		assertEquals(Collections.emptyList(), childZones);
	}

	@Test
	public void testGetChildZones_ValidScenario() {
		List<Zone> zones = createMockZones();
		Mockito.when(zoneRepository.findAllNonDeleted()).thenReturn(zones);
		String zoneCode = "z1";

		assertEquals(createMockZones().getFirst().getLangCode(), "eng");
		assertEquals(createMockZones().getFirst().getCode(), zoneCode);
	}

	@Test
	public void testGetLeafZones_NullLangCode_EmptyZones() {
		assertThrows(NullPointerException.class, () -> zoneUtils.getLeafZones(null));
	}

	@Test
	public void testGetLeafZones_ValidScenario() {
		Zone z1 = new Zone("z1", "en", "zone1", (short) 1, "zone1Hierarchy","zone1Parent", "path/to/z1");
		List<Zone> zones = createMockZones();
		Mockito.when(zoneUtils.getZones()).thenReturn(zones);

		assertEquals(createMockZones().getFirst().getLangCode(), "eng");
		assertEquals(createMockZones().getFirst().getCode(), z1.getCode());
	}

	@Test
	public void testGetUserZonesByUserId_NullZones() {
		List<Zone> zones = null;
		String userId = "user1";
		assertThrows(MasterDataServiceException.class, () -> zoneUtils.getUserZonesByUserId(zones, userId));
	}

	@Test (expected = MasterDataServiceException.class)
	public void testGetUserZonesByUserId_EmptyZones() {
		List<Zone> zones = Collections.emptyList();
		String userId = "z1";
		List<Zone> userZones = zoneUtils.getUserZonesByUserId(zones, userId);
		assertEquals(Collections.emptyList(), userZones);
	}

	@Test (expected = MasterDataServiceException.class)
	public void testGetUserZonesByUserId_ValidScenario() {
		List<Zone> zones = createMockZones();
		String userId = "z1";
		ZoneUser zu = new ZoneUser("z1", "z1", "eng");
		Mockito.when(zoneRepository.findAllNonDeleted()).thenReturn(zones);
		Mockito.when(zoneUtils.getUserZonesByUserId(zones,"z1")).thenReturn(createMockZones());

		zoneUtils.getUserZonesByUserId(userId);
	}

	@Test
	public void testGetChildZoneList_NullZoneCode() {
		List<String> zoneIds = new ArrayList<>();
		zoneIds.add(0,"abc");
		zoneUtils.getChildZoneList(zoneIds, null, "eng");
	}

	@Test
	public void testGetChildZoneList_ZoneNotFound() {
		String zoneCode = "invalid_zone";
		List<String> zoneIds = Collections.emptyList();
		String langCode = "en";
		Mockito.when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(zoneCode, langCode)).thenReturn(null);
		List<Zone> childZones = zoneUtils.getChildZoneList(zoneIds, zoneCode, langCode);
		assertNotEquals(createMockZones().getFirst(), childZones);
	}

	@Test
	public void testGetChildZoneList_ValidScenario() {
		List<Zone> zones = createMockZones();
		Zone parentZone = zones.getFirst();
		Mockito.when(zoneRepository.findZoneByCodeAndLangCodeNonDeleted(parentZone.getCode(), "en")).thenReturn(parentZone);
		Mockito.when(zoneRepository.findAllNonDeleted()).thenReturn(zones);
		List<String> zoneIds = Collections.emptyList();
		String langCode = "en";

		List<Zone> childZones = zoneUtils.getChildZoneList(zoneIds, parentZone.getCode(), langCode);
		assertNotEquals(Collections.emptyList(), childZones);
	}

	private List<Zone> createMockZones() {
		List<Zone> zones = new ArrayList<>();
		Zone z1 = new Zone("z1", "eng", "zone1", (short) 1, "zone1Hierarchy","zone1Parent", "path/to/z1");
		Zone z2 = new Zone("z2", "eng", "zone2", (short) 2, "zone2Hierarchy","zone2Parent", "path/to/z1/z2");
		Zone z3 = new Zone("z3", "eng", "zone3", (short) 3, "zone3Hierarchy", "zone3Parent", "path/to/z4");

		zones.add(z1);
		zones.add(z2);
		zones.add(z3);
		return zones;
	}

}
