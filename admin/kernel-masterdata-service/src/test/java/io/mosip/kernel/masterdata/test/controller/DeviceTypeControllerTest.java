package io.mosip.kernel.masterdata.test.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import io.mosip.kernel.masterdata.dto.DeviceTypeDto;
import io.mosip.kernel.masterdata.dto.DeviceTypePutDto;
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
public class DeviceTypeControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private RequestWrapper<DeviceTypeDto> filDto;

	private RequestWrapper<DeviceTypePutDto> filPutDto;

	private RequestWrapper<SearchDtoWithoutLangCode> searchLangCode;
	private RequestWrapper<FilterValueDto> filValDto;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		DeviceTypeDto deviceType = new DeviceTypeDto();
		deviceType.setCode("CMR1");
		deviceType.setDescription("For capturing photo");
		deviceType.setIsActive(true);
		deviceType.setLangCode("eng");
		deviceType.setName("Camera");

		filDto = new RequestWrapper<DeviceTypeDto>();
		filDto.setRequest(deviceType);

		DeviceTypePutDto dp = new DeviceTypePutDto();
		dp.setCode("CMR1");
		dp.setDescription("For capturing photo");
		dp.setIsActive(true);
		dp.setLangCode("eng");
		dp.setName("Camera");
		filPutDto = new RequestWrapper<DeviceTypePutDto>();
		filPutDto.setRequest(dp);

		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("name");
		sf.setType("equals");
		sf.setValue("Camera");
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
		dto.setText("Camera");
		dto.setType("equals");
		List<FilterDto> lf = new ArrayList<>();
		lf.add(dto);
		f.setLanguageCode("eng");
		f.setOptionalFilters(null);
		f.setFilters(lf);
		filValDto = new RequestWrapper<>();
		filValDto.setRequest(f);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filDto))).andReturn(),"KER-MSD-053");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t001createDeviceTypeTest1() throws Exception {
		filDto.getRequest().setCode("CMR11");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filDto))).andReturn(),null);

	}
	@Test
	@WithUserDetails("global-admin")
	public void t002createDeviceTypeFailTest() throws Exception {
		filDto.getRequest().setCode(null);
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filDto))).andReturn(),"KER-MSD-999");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003updateDeviceTypeTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicetypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filPutDto))).andReturn(),null);;
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateDeviceTypeFailTest() throws Exception {
		filPutDto.getRequest().setCode("CMRK");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicetypes").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filPutDto))).andReturn(),"KER-MSD-209");;
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getAllDeviceTypesTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicetypes/all"))
				.andReturn(),null);;
	}

	@Test
	@WithUserDetails("global-admin")
	public void t005deviceTypeSearchTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes/search").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(searchLangCode))).andReturn(),null);;

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006updateDeviceTypeStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/devicetypes").param("isActive", "true").param("code", "CMR"))
				.andReturn(),null);;
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007deviceTypeFilterValuesTest() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);;

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008deviceTypeFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),null);;

	}

	@Test
	@WithUserDetails("global-admin")
	public void t009deviceTypeFilterValuesTest2() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes/filtervalues").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(filValDto))).andReturn(),"KER-MSD-322");;

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t011updateDeviceTypeStatusFailTest() throws Exception {

		 MasterDataTest.checkResponse( mockMvc
				.perform(MockMvcRequestBuilders.patch("/devicetypes").param("isActive", "true").param("code", "CMRR"))
				.andReturn(), "KER-MSD-209");
	
	}

	@Test
	@WithUserDetails("global-admin")
	public void t012deviceTypeSearchFailTest() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("");
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devicetypes/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchLangCode)))
				.andReturn(),"KER-MSD-312");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013getAllDeviceTypesFailTest() throws Exception {

		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devicetypes/all")).andReturn(),"KER-MSD-003");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t014updateDeviceTypeFailTest() throws Exception {
		filPutDto.getRequest().setCode("aa");
		 MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/devicetypes")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filPutDto))).andReturn(),"KER-MSD-209");
	
	}

}
