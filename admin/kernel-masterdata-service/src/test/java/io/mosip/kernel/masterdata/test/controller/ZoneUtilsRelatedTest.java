package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.RegCenterNonLanguageSpecificPutDto;
import io.mosip.kernel.masterdata.dto.RegCenterPostReqDto;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ZoneUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZoneUtilsRelatedTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	
	@MockBean
	private ZoneUtils zu;
	private RequestWrapper<RegCenterNonLanguageSpecificPutDto> rl=new RequestWrapper<RegCenterNonLanguageSpecificPutDto>();
	private RequestWrapper<RegCenterPostReqDto> rg = new RequestWrapper<RegCenterPostReqDto>();
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());

		 
		Zone zone=new Zone("NTH","eng", "North",(short) 1, "Direction", "MOR", "MOR");
		Zone zone1=new Zone("RBT","eng", "North",(short) 2, "region", "NTH", "MOR/NTH");
		
		List<Zone> lst=new ArrayList<>();
		lst.add(zone);
		lst.add(zone1);
		Mockito.when(zu.getLeafZones("eng")).thenReturn(lst);

		RegCenterNonLanguageSpecificPutDto rq=new RegCenterNonLanguageSpecificPutDto();
		
		rq.setCenterEndTime(LocalTime.NOON);
		rq.setCenterStartTime(LocalTime.MIDNIGHT);
		rq.setCenterTypeCode("REG");
		rq.setContactPerson("Magic");
		rq.setContactPhone("1234567891");
		rq.setZoneCode("RBT");
		rq.setWorkingHours("8");
		rq.setTimeZone("(GTM+01:00) CENTRAL EUROPEAN TIME");
		rq.setNumberOfKiosks((short)2);
		rq.setPerKioskProcessTime(LocalTime.NOON);
		
		rq.setLocationCode("14022");
		rq.setLongitude("32.3423");
		rq.setId("1");
		rq.setLatitude("23.3434");
		rl.setRequest(rq);
		
		RegCenterPostReqDto centerPostReqDto=new RegCenterPostReqDto();
		centerPostReqDto.setAddressLine1("add1");
		centerPostReqDto.setAddressLine2("add2");
		centerPostReqDto.setAddressLine3("add3");
		centerPostReqDto.setCenterEndTime(LocalTime.NOON);
		centerPostReqDto.setCenterStartTime(LocalTime.MIDNIGHT);
		centerPostReqDto.setCenterTypeCode("REG");
		centerPostReqDto.setContactPerson("Magic");
		centerPostReqDto.setContactPhone("1234567891");
		centerPostReqDto.setZoneCode("RBT1");
		centerPostReqDto.setWorkingHours("8");
		centerPostReqDto.setTimeZone("(GTM+01:00) CENTRAL EUROPEAN TIME");
		centerPostReqDto.setNumberOfKiosks((short)2);
		centerPostReqDto.setPerKioskProcessTime(LocalTime.NOON);
		centerPostReqDto.setName("Mysore road");
		centerPostReqDto.setLocationCode("14022");
		//centerPostReqDto.setLunchEndTime(new LocalTimeType().fromString("12:00"));
		//centerPostReqDto.setLunchStartTime(new LocalTimeType().fromString("12:30"));
		centerPostReqDto.setLongitude("32.3423");
	//	centerPostReqDto.setId("1");
		centerPostReqDto.setLatitude("23.3434");
		centerPostReqDto.setLangCode("eng");
		centerPostReqDto.setHolidayLocationCode("14022");
		centerPostReqDto.setIsActive(true);
		rg.setRequest(centerPostReqDto);
		
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterNonLanguageSpecificFailTest() throws Exception {
		rl.getRequest().setId("2");
		rl.getRequest().setHolidayLocationCode("14022");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/nonlanguage")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rl)))
				.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getSubZonesTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafzones/eng")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void createRegistrationCenterTest_Success() throws Exception {
		rg.getRequest().setZoneCode("RBT");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void createRegistrationCenterFailTest_WithLeafZone() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn(), "KER-MSD-346");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getLeafZonesBasedOnZoneCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/zones/leafzones/eng")).andReturn(), null);

	}
	
    @Test
    @WithUserDetails("global-admin")
    public void getAllValuesOfFieldTest_Success() throws Exception {
      
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country?langCode=eng")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    
	
}
