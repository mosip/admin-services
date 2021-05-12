/**
 * 
 */
package io.mosip.kernel.masterdata.test.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.LocationDto;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.TitleDto;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;

/**
 * @author Ramadurai Pandian
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MasterdataCreationUtilTest {

	@MockBean
	private MasterdataCreationUtil masterdataCreationUtil;
	
	@MockBean
	private PublisherClient<String,TitleDto,HttpHeaders> titlePublisherClient;
	
	@MockBean
	private PublisherClient<String,TemplateDto,HttpHeaders> templatePublisherClient;
	
	@MockBean
	private TemplateService templateService;

	private String id;

	@Test
	@WithUserDetails("zonal-admin")
	public void createPrimaryMasterDataTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		LocationDto location = new LocationDto();
		short hlevel = (short) 4;
		location.setCode("");
		location.setLangCode("eng");
		location.setHierarchyLevel(hlevel);
		location.setHierarchyName("Zone");
		location.setIsActive(false);
		location.setName("Essaouira");
		location.setParentLocCode("RAB");
		location = masterdataCreationUtil.createMasterData(Location.class, location);
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void createSecondaryMasterDataTest()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		LocationDto location = new LocationDto();
		short hlevel = (short) 4;
		location.setCode("test");
		location.setLangCode("ara");
		location.setHierarchyLevel(hlevel);
		location.setHierarchyName("Zone");
		location.setIsActive(false);
		location.setName("الصويرة");
		location.setParentLocCode("RAB");
		location = masterdataCreationUtil.createMasterData(Location.class, location);
	}

}
