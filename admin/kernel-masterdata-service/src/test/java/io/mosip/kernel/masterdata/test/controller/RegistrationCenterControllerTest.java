package io.mosip.kernel.masterdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.ExceptionalHolidayPutPostDto;
import io.mosip.kernel.masterdata.dto.RegCenterLanguageSpecificPutDto;
import io.mosip.kernel.masterdata.dto.RegCenterNonLanguageSpecificPutDto;
import io.mosip.kernel.masterdata.dto.RegCenterPostReqDto;
import io.mosip.kernel.masterdata.dto.request.*;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationCenterControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	
	private RequestWrapper<SearchDto> sr = new RequestWrapper<>();
	private RequestWrapper<FilterValueDto> fv = new RequestWrapper<FilterValueDto>();
	private RequestWrapper<RegCenterPostReqDto> rg = new RequestWrapper<RegCenterPostReqDto>();
	private RequestWrapper<RegCenterLanguageSpecificPutDto> rp=new RequestWrapper<RegCenterLanguageSpecificPutDto>();
	private RequestWrapper<RegCenterNonLanguageSpecificPutDto> rl=new RequestWrapper<RegCenterNonLanguageSpecificPutDto>();
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
	
	
		SearchDto sc = new SearchDto();
		List<SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("name");
		sf.setType("contains");
		sf.setValue("Center A Ben");
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		sr.setRequest(sc);
		FilterValueDto filterValueDto = new FilterValueDto();
		List<FilterDto> l = new ArrayList<FilterDto>();
		FilterDto f = new FilterDto();
		f.setColumnName("name");
		f.setText("Center A Ben");
		f.setType("All");
		l.add(f);
		filterValueDto.setFilters(l);
		filterValueDto.setLanguageCode("eng");
		fv.setRequest(filterValueDto);
		
		RegCenterPostReqDto centerPostReqDto=new RegCenterPostReqDto();
		centerPostReqDto.setAddressLine1("add1");
		centerPostReqDto.setAddressLine2("add2");
		centerPostReqDto.setAddressLine3("add3");
		centerPostReqDto.setCenterEndTime(LocalTime.NOON);
		centerPostReqDto.setCenterStartTime(LocalTime.MIDNIGHT);
		centerPostReqDto.setCenterTypeCode("REG");
		centerPostReqDto.setContactPerson("Magic");
		centerPostReqDto.setContactPhone("1234567891");
		centerPostReqDto.setZoneCode("RBT");
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
		
		RegCenterLanguageSpecificPutDto rcput=new RegCenterLanguageSpecificPutDto();
		rcput.setAddressLine1("address1 update");
		rcput.setAddressLine2("address2 update");
		rcput.setAddressLine3("address3 update");
		rcput.setContactPerson("Magic man");
		rcput.setLangCode("eng");
		rcput.setName("Mysoreroad");
		rcput.setId("10003");
		rp.setRequest(rcput);
		
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
		//centerPostReqDto.setLunchEndTime(new LocalTimeType().fromString("12:00"));
		//centerPostReqDto.setLunchStartTime(new LocalTimeType().fromString("12:30"));
		rq.setLongitude("32.3423");
		rq.setId("1");
		rq.setLatitude("23.3434");
		rl.setRequest(rq);
		
	
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getRegistrationCenterDetailsByLocationCodeTest() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getlocspecificregistrationcenters/eng/14022")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002getRegistrationCenterDetailsByLocationCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getlocspecificregistrationcenters/eng1/KBT")).andReturn(),
				"KER-MSD-215");

	}

	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getgetRegistrationCenterHolidaysTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10001/2019")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getgetRegistrationCenterHolidaysTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10002/2000")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004getgetRegistrationCenterHolidaysTest2() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/all/10002/2000")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004getRegistrationCenterHolidaysFailTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10002/00000")).andReturn(),
				"KER-MSD-443");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005getCoordinateSpecificRegistrationCentersTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getcoordinatespecificregistrationcenters/eng/23/34/50"))
				.andReturn(), "KER-MSD-041");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006getCoordinateSpecificRegistrationCentersFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getcoordinatespecificregistrationcenters/eng/23.3454/4.5434/6"))
				.andReturn(), "KER-MSD-041");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t008getSpecificRegistrationCenterByIdTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/10001/eng")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009getSpecificRegistrationCenterByIdFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/100/eng")).andReturn(), "KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t010getAllRegistrationCentersDetailsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t011getRegistrationCenterByHierarchyLevelAndTextAndlangCodeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/6/14022")).andReturn(),
				"KER-MSD-026");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011getRegistrationCenterByHierarchyLevelAndTextAndlangCodeTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/5/33")).andReturn(),
				"KER-MSD-215");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011getRegistrationCenterByHierarchyLevelAndTextAndlangCodeTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/0/14022")).andReturn(),
				"KER-MSD-215");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011getRegistrationCenterByHierarchyLevelAndTextAndlangCodeTest5() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/ara/4/abc")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t012getRegistrationCenterByHierarchyLevelAndTextAndlangCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/5/abc")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t013getRegistrationCenterByHierarchyLevelAndTextAndlangCodePaginatedTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/page/eng/5/14022")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t014getRegistrationCenterByHierarchyLevelAndTextAndlangCodePaginatedTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/page/eng/3/14022")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t015validateTimestampTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/10001/eng/2021-11-23T13:09:19.695Z")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t015validateTimestampTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/10001/eng/2019-12-10T13:09:19.695Z")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t016validateTimestampFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/100/eng/2021-11-23T13:09:19.695Z")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017deleteRegistrationCenterTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/10003")).andReturn(),
				"KER-MSD-192");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017deleteRegistrationCenterTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/11113")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t018deleteRegistrationCenterFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/11113")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t019getRegistrationCenterByHierarchyLevelAndListTextAndlangCodeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng1/5/names?name=MyCountry&name=kenitra")).andReturn(),
				"KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t019getRegistrationCenterByHierarchyLevelAndListTextAndlangCodeTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/2/names?name=MyCountry")).andReturn(),
				"KER-MSD-215");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t019getRegistrationCenterByHierarchyLevelAndListTextAndlangCodeTestFail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng1/6/names?name=MyCountry")).andReturn(),
				"KER-MSD-026");

	}
	
	

	@Test
	@WithUserDetails("global-admin")
	public void t021getAllExistingRegistrationCentersDetailsTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/all")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterFailTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10002")).andReturn(),
				"KER-MSD-352");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10004")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10103")).andReturn(),
				"KER-MSD-441");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterTest3() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10002")).andReturn(),
				"KER-MSD-352");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterTest4() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/100")).andReturn(),
				"KER-MSD-216");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t022decommissionRegCenterTest5() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/100031")).andReturn(),
				"KER-MSD-353");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023decommissionRegCenterFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/1009")).andReturn(),
				"KER-MSD-216");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023decommissionRegCenterFailTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10003")).andReturn(),
				"KER-MSD-351");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023decommissionRegCenterFailTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10077")).andReturn(),
				"KER-MSD-350");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023decommissionRegCenterFailTest3() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10103")).andReturn(),
				"KER-MSD-441");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t024updateRegistrationCenterAdminStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/registrationcenters").param("isActive", "false").param("id", "10002"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t025updateRegistrationCenterAdminStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/registrationcenters").param("isActive", "true").param("id", "100"))
				.andReturn(), "KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t026getMissingRegistrationCentersDetailsTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/missingids/ara")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t027getMissingRegistrationCentersDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/missingids/eng1")).andReturn(),
				"KER-LANG-ERR");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t028getCenterSpecificToZoneTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getzonespecificregistrationcenters/eng/NTH")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t028getCenterSpecificToZoneTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getzonespecificregistrationcenters/eng/KT")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t029searchRegistrationCenterTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029createRegistrationCenterTest4() throws Exception {

		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn());
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029createRegistrationCenterTest() throws Exception {
		List<ExceptionalHolidayPutPostDto> elst=new ArrayList<>();
		ExceptionalHolidayPutPostDto e=new ExceptionalHolidayPutPostDto();
		e.setExceptionHolidayDate(LocalDate.now().toString());
		e.setExceptionHolidayName("Test");
		e.setExceptionHolidayReson("test");
		elst.add(e);
		rg.getRequest().setExceptionalHolidayPutPostDto(elst);
		
		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn());
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029createRegistrationCenterTest1() throws Exception {
		List<ExceptionalHolidayPutPostDto> elst=new ArrayList<>();
		ExceptionalHolidayPutPostDto e=new ExceptionalHolidayPutPostDto();
		e.setExceptionHolidayDate(LocalDate.now().toString());
		e.setExceptionHolidayName("Test");
		e.setExceptionHolidayReson("test");
		elst.add(e);
		
		rg.getRequest().setExceptionalHolidayPutPostDto(elst);
		Map<String,Boolean> m=new HashMap<>();
		m.put("101", true);
		rg.getRequest().setWorkingNonWorkingDays(m);
		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn());
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t029searchRegistrationCenterTest5() throws Exception {
		sr.getRequest().getFilters().get(0).setType("");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t030searchRegistrationCenterFailTest() throws Exception {
		sr.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029searchRegistrationCenterTest1() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029searchRegistrationCenterTest2() throws Exception {
		sr.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029searchRegistrationCenterFailTest3() throws Exception {
		sr.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), "KER-MSD-318");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t031registrationCenterFilterValuesTest1() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t031registrationCenterFilterValuesTest2() throws Exception {
		fv.getRequest().getFilters().get(0).setType("unique");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t031registrationCenterFilterValuesFailTest() throws Exception {
		fv.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), "KER-MSD-317");
	}
	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t034updateRegistrationCenterAdminTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/language")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rp)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t035updateRegistrationCenterAdminFailTest() throws Exception {
		rp.getRequest().setId("2");
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/language")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rp)))
				.andReturn(), "KER-MSD-215");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t036updateRegistrationCenterNonLanguageSpecifiTest1() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/nonlanguage")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rl)))
				.andReturn(), "KER-MSD-999");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t036updateRegistrationCenterNonLanguageSpecifiTestFail1() throws Exception {
		rl.getRequest().setCenterTypeCode("REG1");
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/nonlanguage")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rl)))
				.andReturn(), "KER-MSD-999");
	}
	
	
}
