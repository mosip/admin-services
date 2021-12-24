package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.dto.HolidayDto;
import io.mosip.kernel.masterdata.dto.HolidayIdDeleteDto;
import io.mosip.kernel.masterdata.dto.HolidayUpdateDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HolidayControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;

	private RequestWrapper<HolidayDto> holiday = new RequestWrapper<HolidayDto>();

	private RequestWrapper<HolidayUpdateDto> holidayPutReq1 = new RequestWrapper<HolidayUpdateDto>();

	private RequestWrapper<HolidayIdDeleteDto> holidayDelReq = new RequestWrapper<HolidayIdDeleteDto>();;
	private RequestWrapper<FilterValueDto> filValDto = new RequestWrapper<FilterValueDto>();
	private RequestWrapper<SearchDto> searchDtoReq = new RequestWrapper<SearchDto>();

	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		HolidayDto dto = new HolidayDto();
		dto.setHolidayDate(LocalDate.now());
		dto.setHolidayDesc("National Holiday");
		dto.setHolidayId(1);
		dto.setIsActive(true);
		dto.setHolidayName("May day");
		dto.setLangCode("eng");
		dto.setLocationCode("KTA");
		holiday.setRequest(dto);

		holidayDelReq = new RequestWrapper<HolidayIdDeleteDto>();
		HolidayIdDeleteDto deleteDto = new HolidayIdDeleteDto();
		deleteDto.setHolidayDate(LocalDate.now());
		deleteDto.setLocationCode("KTA");

		holidayDelReq.setRequest(deleteDto);

		Pagination pagination = new Pagination(0, 1);
		List<SearchSort> ss = new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		sf.setColumnName("holidayName");
		sf.setType("equals");
		sf.setValue("New Year Day");
		ls.add(sf);
		SearchSort s = new SearchSort("holidayName", "ASC");
		SearchDto sd = new SearchDto();
		sd.setFilters(ls);
		sd.setLanguageCode("eng");
		sd.setPagination(pagination);
		ss.add(s);
		sd.setSort(ss);

		searchDtoReq.setRequest(sd);

		FilterValueDto f = new FilterValueDto();
		FilterDto fdto = new FilterDto();
		fdto.setColumnName("holidayName");
		fdto.setText("New Year Day");
		fdto.setType("all");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(fdto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001saveHolidayTest() throws Exception {

		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2021-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"19\",\n" + "    \"locationCode\": \"KTA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"isActive\": \"true\",\n"
								+ "    \"holidayName\": \"May day\",\n" + "    \"langCode\": \"eng\",\n"
								+ "    \"holidayDesc\": \"National holiday\"\n" + "  }\n" + "}"))

				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001saveHolidayTest1() throws Exception {

		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2019-12-14", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"12\",\n" + "    \"locationCode\": \"KTA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"isActive\": \"true\",\n"
								+ "    \"holidayName\": \"Eid\",\n" + "    \"langCode\": \"eng\",\n"
								+ "    \"holidayDesc\": \"National holiday\"\n" + "  }\n" + "}"))

				.andReturn(), "KER-MSD-240");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001saveHolidayTest2() throws Exception {

		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2019-12-14", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.post("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"12\",\n" + "    \"locationCode\": \"KTA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"isActive\": \"true\",\n"
								+ "    \"holidayName\": \"Eidi\",\n" + "    \"langCode\": \"eng\",\n"
								+ "    \"holidayDesc\": \"National holiday\"\n" + "  }\n" + "}"))

				.andReturn(), null);
	}


	@Test
	@WithUserDetails("global-admin")
	public void t003updateHolidayTest() throws Exception {
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2021-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"1\",\n" + "    \"locationCode\": \"KTA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"holidayName\": \"May day\",\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"holidayDesc\": \"National holiday\"\n"
								+ "  }\n" + "}"))

				.andReturn(), "KER-MSD-020");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateHolidayTest2() throws Exception {
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2021-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"2000001\",\n" + "    \"locationCode\": \"KTA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"holidayName\": \"May day\",\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"holidayDesc\": \"National holiday\"\n"
								+ "  }\n" + "}"))

				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateHolidayTest3() throws Exception {
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2021-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.put("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"holidayId\": \"1\",\n" + "    \"locationCode\": \"KTAA\",\n"
								+ "     \"holidayDate\":\"" + ld + "\",\n" + "    \"holidayName\": \"May day\",\n"
								+ "    \"langCode\": \"eng\",\n" + "    \"holidayDesc\": \"National holiday\"\n"
								+ "  }\n" + "}"))

				.andReturn(), "KER-MSD-732");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006updateHolidayStatusTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/holidays").param("holidayId", "2000001").param("isActive", "true"))
				.andReturn(), "KER-MSD-731");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006updateHolidayStatusTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/holidays").param("holidayId", "001").param("isActive", "true"))
				.andReturn(), "KER-MSD-020");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007updateHolidayStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/holidays").param("holidayId", "2000002").param("isActive", "true"))
				.andReturn(), "KER-MSD-731");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008holidayFilterValuesTest() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/holidays/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009holidayFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/holidays/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), "KER-MSD-322");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009holidayFilterValuesTest2() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/holidays/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010searchMachineTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineTest1() throws Exception {
		searchDtoReq.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineTest3() throws Exception {
		searchDtoReq.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		searchDtoReq.getRequest().setLanguageCode("ara");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				"KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineTest2() throws Exception {
		searchDtoReq.getRequest().getFilters().get(0).setColumnName("name");
		searchDtoReq.getRequest().getFilters().get(0).setType(FilterTypeEnum.CONTAINS.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013searchMachineTest2() throws Exception {
		searchDtoReq.getRequest().getFilters().get(0).setType(FilterTypeEnum.STARTSWITH.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoReq))).andReturn(),
				"KER-MSD-357");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015getAllHolidaysTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t016getAllHolidaysFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays")).andReturn(),
				"KER-MSD-020");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017getAllHolidayByIdTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays/2000001")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018getAllHolidayByIdFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays/10")).andReturn(),
				"KER-MSD-020");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t019getAllHolidayByIdAndLangCodeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays/2000001/eng")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t020getAllHolidayByIdAndLangCodeFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays/10/eng")).andReturn(),
				"KER-MSD-020");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t021getHolidaysTest() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.get("/holidays").param("pageNumber", "0")
								.param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t022getMissingHolidayDetailsTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/holidays/missingids/eng").param("fieldName", "holiday_name"))
				.andReturn(), "KER-MSD-317");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t023getMissingHolidayDetailsFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/holidays/missingids/eng1").param("fieldName", "holiday_name"))
				.andReturn(), "KER-LANG-ERR");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t023getMissingHolidayDetailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.get("/holidays/missingids/eng").param("fieldName", "holidayName"))
				.andReturn(), null);
	}


	@Test
	@WithUserDetails("global-admin")
	public void t024deleteHolidayTest1() throws Exception {

		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2019-12-12", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.delete("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"locationCode\": \"KTA\",\n" + "     \"holidayDate\":\"" + ld + "\"" + "  }\n"
								+ "}"))

				.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t024deleteHolidayTest2() throws Exception {

		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse("2019-12-13", DATEFORMATTER);
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.delete("/holidays").contentType(MediaType.APPLICATION_JSON)
						.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
								+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
								+ "    \"locationCode\": \"KTA\",\n" + "     \"holidayDate\":\"" + ld + "\"" + "  }\n"
								+ "}"))

				.andReturn(), "KER-MSD-020");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t026getHolidaysFailTest() throws Exception {
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.get("/holidays").param("pageNumber", "0")
								.param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
						.andReturn(), "KER-MSD-020");

	}

}
