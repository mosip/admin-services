package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
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

	private RequestWrapper<HolidayDto> holidayReq = new RequestWrapper<HolidayDto>();

	private RequestWrapper<HolidayUpdateDto> holidayPutReq = new RequestWrapper<HolidayUpdateDto>();
	
	private ObjectMapper mapper;
	
	private RequestWrapper<HolidayIdDeleteDto> holidayDelReq = new RequestWrapper<HolidayIdDeleteDto>();;
	private RequestWrapper<FilterValueDto> filValDto = new RequestWrapper<FilterValueDto>();
	private RequestWrapper<SearchDto> searchDtoReq = new RequestWrapper<SearchDto>();
	

	@Before
	public void setUp() {

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		 holidayReq=new RequestWrapper<HolidayDto>();
		 HolidayDto dto=new HolidayDto();
		dto.setHolidayDate(LocalDate.now());
		dto.setHolidayDesc("National Holiday");
		dto.setHolidayId(1);
		dto.setIsActive(true);
		dto.setHolidayName("May day");
		dto.setLangCode("eng");
		dto.setLocationCode("KTA");
		holidayReq.setRequest(dto);

		 holidayPutReq=new RequestWrapper<HolidayUpdateDto>();
		HolidayUpdateDto updateDto=new HolidayUpdateDto();
		updateDto.setHolidayDate(LocalDate.now());
		updateDto.setHolidayDesc("National  Holiday updated");
		updateDto.setHolidayId(1);
		updateDto.setIsActive(true);
		updateDto.setHolidayName("May day");
		updateDto.setLangCode("eng");
		updateDto.setLocationCode("KTA");
		holidayPutReq.setRequest(updateDto);

		 holidayDelReq=new RequestWrapper<HolidayIdDeleteDto>();
		 HolidayIdDeleteDto deleteDto=new HolidayIdDeleteDto();
		deleteDto.setHolidayDate(LocalDate.now());
		deleteDto.setHolidayName("May Day");
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
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayReq))).andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001saveHolidayFailTest1() throws Exception {
		holidayReq.getRequest().setLocationCode("abc");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayReq))).andReturn(), "KER-MSD-729");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001saveHolidayFailTest2() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayReq))).andReturn(), "KER-MSD-730");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002saveHolidayFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/holidays").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayReq))).andReturn(),"KER-MSD-999");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateHolidayTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/holidays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayPutReq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateHolidayFailTest() throws Exception {
		holidayPutReq.getRequest().setHolidayId(10);
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/holidays").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayPutReq))).andReturn(),"KER-MSD-999");
	}
	@Test
	@WithUserDetails("global-admin")
	public void t006updateHolidayStatusTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/holidays").param("holidayId", "2000001").param("isActive", "true"))
				.andReturn(),"KER-MSD-731");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateHolidayStatusTest2() throws Exception {
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/holidays").param("holidayId", "2000001").param("isActive", "true"))
				.andReturn(),"KER-MSD-731");
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

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/holidays").param("holidayId", "10").param("isActive", "true"))
				.andReturn(), "KER-MSD-020");

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
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays")).andReturn(),"KER-MSD-020");
		
	}

	@Test
	@WithUserDetails("global-admin")
	public void t017getAllHolidayByIdTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/holidays/2000001")).andReturn(), null);
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
				.andReturn(), "KER-MSD-999");
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
	public void t024deleteHolidayTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/holidays")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(holidayDelReq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t025deleteHolidayFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/holidays")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(holidayDelReq))).andReturn(),
				"KER-MSD-999");

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
