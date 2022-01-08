package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

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
import io.mosip.kernel.masterdata.dto.DeviceSpecificationDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceSpecificationControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private RequestWrapper<DeviceSpecificationDto> deviceSpecification;

	private DeviceSpecificationDto devicespecitionDto;

	private RequestWrapper<SearchDtoWithoutLangCode> searchLangCode;

	private RequestWrapper<FilterValueDto> filDto;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		devicespecitionDto = new DeviceSpecificationDto();
		devicespecitionDto.setBrand("Safran Morpho");
		devicespecitionDto.setDescription("To scan fingerprint");
		devicespecitionDto.setDeviceTypeCode("FRS");
		devicespecitionDto.setId("165");
		devicespecitionDto.setIsActive(true);
		devicespecitionDto.setLangCode("eng");
		devicespecitionDto.setMinDriverversion("1.12");
		devicespecitionDto.setModel("1300 E2");
		devicespecitionDto.setName("Fingerprint Scanner");
		deviceSpecification = new RequestWrapper<DeviceSpecificationDto>();
		deviceSpecification.setRequest(devicespecitionDto);

		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("name");
		sf.setType("equals");
		sf.setValue("scanner");
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		searchLangCode = new RequestWrapper<>();
		searchLangCode.setRequest(sc);

		FilterValueDto f = new FilterValueDto();
		FilterDto dto = new FilterDto();
		dto.setColumnName("name");
		dto.setText("scanner");
		dto.setType("equals");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(dto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filDto = new RequestWrapper<>();
		filDto.setRequest(f);

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	@WithUserDetails("global-admin")
	public void t000getDeviceHistoryIdLangEff() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicespecifications/devicetypecode/FRS"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceSpecificationFailTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),"KER-MSD-054");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceSpecificationTest() throws Exception {
		deviceSpecification.getRequest().setId("166");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceSpecificationTest1() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceSpecificationTest2() throws Exception {
		deviceSpecification.getRequest().setId(null);
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),null);

	}


	@Test
	@WithUserDetails("global-admin")
	public void t002updateDeviceSpecificationTest1() throws Exception {
		deviceSpecification.getRequest().setDescription("updated");
		deviceSpecification.getRequest().setId("166");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002updateDeviceSpecificationTest2() throws Exception {
		deviceSpecification.getRequest().setId("000");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicespecifications").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),"KER-MSD-012");

	}



	@Test
	@WithUserDetails("global-admin")
	public void t003getAllDeviceSpecificationsTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicespecifications/all"))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004deviceSpecificationSearchTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchLangCode)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005deviceSpecificationSearchTest1() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchLangCode)))
				.andReturn(),null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006deviceSpecificationSearchTest2() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("startswith");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchLangCode)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008updateDeviceSpecificationStatusTest() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/devicespecifications").param("isActive", "true").param("id", "165"))
				.andReturn(),null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t008updateDeviceSpecificationStatusTest1() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/devicespecifications").param("isActive", "false").param("id", "165"))
				.andReturn(),"KER-MSD-217");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009deviceSpecificationFilterValuesTest() throws Exception {
		filDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filDto)))
				.andReturn(),null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010deviceSpecificationFilterValuesTest1() throws Exception {
		filDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filDto)))
				.andReturn(),null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t011deviceSpecificationFilterValuesTest2() throws Exception {
		filDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/filtervalues")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filDto)))
				.andReturn(),"KER-MSD-322");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013updateDeviceSpecificationStatusFailTest() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/devicespecifications").param("isActive", "true").param("id", "170"))
				.andReturn(),"KER-MSD-012");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t013updateDeviceSpecificationStatusFailTest1() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.patch("/devicespecifications").param("isActive", "true").param("id", "327"))
				.andReturn(),"KER-MSD-217");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t014deviceSpecificationSearchFailTest() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchLangCode)))
				.andReturn(),null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t015deleteDeviceSpecificationTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devicespecifications/165"))
				.andReturn(), "KER-MSD-121");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t016getAllDeviceSpecificationsFailTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devicespecifications/all")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),"KER-MSD-012");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t017deleteDeviceSpecificationFailTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devicespecifications/167"))
				.andReturn(),"KER-MSD-012");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018updateDeviceSpecificationFailTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicespecifications")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),"KER-MSD-081");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t019createDeviceSpecificationFailTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicespecifications")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceSpecification)))
				.andReturn(),"KER-MSD-054");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t020getDeviceHistoryIdLangEffTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicespecifications/devicetypecode/CMR11"))
				.andReturn(),"KER-MSD-012");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t021deleteDeviceSpecificationTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devicespecifications/1"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t022getDeviceSpecificationByLanguageCodeTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicespecifications"))
				.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023getDeviceSpecificationByLanguageCodeTest() throws Exception {
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicespecifications/eng"))
				.andReturn(), null);
	}
	
}
