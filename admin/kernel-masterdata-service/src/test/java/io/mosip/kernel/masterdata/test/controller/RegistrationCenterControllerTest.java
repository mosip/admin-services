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
	public void getRegistrationCenterDetails_ByLocationCodeTest_Success() throws Exception {
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getlocspecificregistrationcenters/eng/14022")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenterDetails_ByInvalidLangCode_Test() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getlocspecificregistrationcenters/eng1/KBT")).andReturn(),
				"KER-MSD-215");

	}

	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenterHolidaysTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10001/2019")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenterHolidaysTest_Pass() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10002/2000")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getAllRegistrationCenterHolidaysTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/all/10002/2000")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenterHolidays_WithInValidId_Fail() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getregistrationcenterholidays/eng/10002/00000")).andReturn(),
				"KER-MSD-443");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getCoordinateSpecificRegistrationCentersTest_Fail() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getcoordinatespecificregistrationcenters/eng/23/34/50"))
				.andReturn(), "KER-MSD-041");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getCoordinateSpecificRegistrationCentersFail_WithInvalidRequest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/getcoordinatespecificregistrationcenters/eng/23.3454/4.5434/6"))
				.andReturn(), "KER-MSD-041");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void getSpecificRegistrationCenter_ByValidId_Test() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/10001/eng")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void getSpecificRegistrationCenter_ByInValidId_Test() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/100/eng")).andReturn(), "KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getAllRegistrationCentersDetailsTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_WithInvalidLocation_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/6/14022")).andReturn(),
				"KER-MSD-026");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_WithInvalidRegCenter_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/5/33")).andReturn(),
				"KER-MSD-215");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndTextAndLangCode_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/0/14022")).andReturn(),
				"KER-MSD-215");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndTextAndLangCodeAra_withInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/ara/4/abc")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndTextAndLangCodeEng_withInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/5/abc")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndTextAndLangCodePaginated_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/page/eng/5/14022")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndTextAndLangCodePaginated_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/page/eng/3/14022")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void validateTimestampTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/10001/eng/2021-11-23T13:09:19.695Z")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void validateTimestampTest_Pass() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/10001/eng/2019-12-10T13:09:19.695Z")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void validateTimestampTest_Fail() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/registrationcenters/validate/100/eng/2021-11-23T13:09:19.695Z")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteRegistrationCenterTest_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/10003")).andReturn(),
				"KER-MSD-192");

	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteRegistrationCenterTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/11113")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void deleteRegistrationCenter_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/registrationcenters/11113")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndListTextAndLangCode_WithInvalidLocation() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng1/5/names?name=MyCountry&name=kenitra")).andReturn(),
				"KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_ByHierarchyLevelAndListTextAndLangCode_WithInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng/2/names?name=MyCountry")).andReturn(),
				"KER-MSD-215");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void getRegistrationCenter_FailWithInvalidLocation() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/eng1/6/names?name=MyCountry")).andReturn(),
				"KER-MSD-026");

	}
	
	

	@Test
	@WithUserDetails("global-admin")
	public void getAllExistingRegistrationCentersDetailsTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/all")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenterTest_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10002")).andReturn(),
				"KER-MSD-352");

	}

	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenterTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10004")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenter_WithNotAuthorizedZone_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10103")).andReturn(),
				"KER-MSD-441");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenter_WithMappedCenter_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10002")).andReturn(),
				"KER-MSD-352");

	}

	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenter_WithInvalidId_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/100")).andReturn(),
				"KER-MSD-216");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenter_WithInvalidLengthId_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/100031")).andReturn(),
				"KER-MSD-353");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenter_WithDuplicateId_Fail() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/1009")).andReturn(),
				"KER-MSD-216");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenterFail_WithMappedRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10003")).andReturn(),
				"KER-MSD-351");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenterFail_WithMappedToDevice() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10077")).andReturn(),
				"KER-MSD-350");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void decommissionRegCenterFail_WithInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/registrationcenters/decommission/10103")).andReturn(),
				"KER-MSD-441");

	}

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterAdminStatusTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/registrationcenters").param("isActive", "false").param("id", "10002"))
				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterAdminStatusFail_WithInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/registrationcenters").param("isActive", "true").param("id", "100"))
				.andReturn(), "KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void getMissingRegistrationCentersDetailsTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/missingids/ara")).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t027getMissingRegistrationCentersDetailsFail_WithInvalidLangCode() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/registrationcenters/missingids/eng1")).andReturn(),
				"KER-LANG-ERR");
	}

	@Test
	@WithUserDetails("global-admin")
	public void getCenterSpecificToZoneTest_Success() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getzonespecificregistrationcenters/eng/NTH")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void getCenterSpecificToZone_WithInvalidRegCenter() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/getzonespecificregistrationcenters/eng/KT")).andReturn(),
				"KER-MSD-215");

	}

	@Test
	@WithUserDetails("global-admin")
	public void searchRegistrationCenterTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void createRegistrationCenterTest_Success() throws Exception {

		MasterDataTest.checkErrorResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rg)))
				.andReturn());
	}

	@Test
	@WithUserDetails("global-admin")
	public void createRegistrationCenter_WithExceptionalHoliday_Test() throws Exception {
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
	public void createRegistrationCenter_WithWorkingDays_Test() throws Exception {
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
	public void searchRegistrationCenter_Success() throws Exception {
		sr.getRequest().getFilters().get(0).setType("");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void searchRegistrationCenter_Fail() throws Exception {
		sr.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void searchRegistrationCenter_WithEqualType_Success() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void searchRegistrationCenter_WithStartsWithType_Success() throws Exception {
		sr.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void searchRegistrationCenter_WithEndsWithType_Fail() throws Exception {
		sr.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/search")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr)))
				.andReturn(), "KER-MSD-318");
	}

	@Test
	@WithUserDetails("global-admin")
	public void registrationCenterFilterValuesTest_Success() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void registrationCenterFilterValues_WithUniqueType_Success() throws Exception {
		fv.getRequest().getFilters().get(0).setType("unique");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void registrationCenterFilterValues_WithInvalidColumnName_Fail() throws Exception {
		fv.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/registrationcenters/filtervalues")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv)))
				.andReturn(), "KER-MSD-317");
	}
	
	
	
	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterAdminTest_Success() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/language")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rp)))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterAdminFail_WithInvalidId() throws Exception {
		rp.getRequest().setId("2");
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/language")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rp)))
				.andReturn(), "KER-MSD-215");
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterNonLanguageSpecificTest_FailWithInvalidInput() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/nonlanguage")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rl)))
				.andReturn(), "KER-MSD-999");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void updateRegistrationCenterNonLanguageSpecificTest_FailWithInvalidCenter() throws Exception {
		rl.getRequest().setCenterTypeCode("REG1");
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/registrationcenters/nonlanguage")
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(rl)))
				.andReturn(), "KER-MSD-999");
	}
	
	
}
