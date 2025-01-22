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
import io.mosip.kernel.masterdata.dto.LocationCreateDto;
import io.mosip.kernel.masterdata.dto.LocationPutDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	private RequestWrapper<LocationCreateDto> locationCreateDtoReq;

	private RequestWrapper<LocationPutDto> locationRequestDto;

	private RequestWrapper<FilterValueDto> filValDto;

	private RequestWrapper<SearchDto> searchDtoRq;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());

		locationCreateDtoReq = new RequestWrapper<LocationCreateDto>();
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("10000");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Postal Code");
		createDto.setLangCode("eng");
		createDto.setName("10000");
		createDto.setParentLocCode("");
		locationCreateDtoReq.setRequest(createDto);

		locationRequestDto = new RequestWrapper<LocationPutDto>();
		LocationPutDto dto = new LocationPutDto();
		dto.setCode("11111");
		dto.setHierarchyLevel((short) 1);
		dto.setIsActive(true);
		dto.setHierarchyName("Postal Code");
		dto.setLangCode("eng");
		dto.setName("11111");
		dto.setParentLocCode("QRHS");
		locationRequestDto.setRequest(dto);
		Pagination pagination = new Pagination(0, 1);
		List<SearchSort> ss = new ArrayList<SearchSort>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		sf.setColumnName("code");
		sf.setType("contains");
		sf.setValue("RSK");
		ls.add(sf);
		SearchSort s = new SearchSort("name", "desc");
		SearchDto sd = new SearchDto();
		sd.setFilters(ls);
		sd.setLanguageCode("eng");
		sd.setPagination(pagination);
		ss.add(s);
		sd.setSort(ss);

		searchDtoRq = new RequestWrapper<SearchDto>();
		searchDtoRq.setRequest(sd);

		FilterValueDto f = new FilterValueDto();
		FilterDto fdto = new FilterDto();
		fdto.setColumnName("code");
		fdto.setText("RSK");
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
	public void t029getLocationHierarchyDetailsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/eng")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t001getLocationHierarchyDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/eng1")).andReturn(),
				"KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029getLocationHierarchyByLangCodeTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/14022/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t029getLocationHierarchyByLangCodeTest1() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/BNMR/ara")).andReturn(), null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t029getLocationHierarchyByLangCodeTest2() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/MOGR/eng")).andReturn(), null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t002getLocationHierarchyByLangCodeFailTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/10000/eng1")).andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getLocationHierarchyByLangCodeFailTest1() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/10600/eng")).andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t028getLocationDetailsByLangCodeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/info/KTA/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t027getLocationDetailsByLangCodeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/info/11111/eng1")).andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t026getLocationDetailsByLangCodeFailTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/info/10600/eng")).andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t025getLocationDataByHierarchyNameTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/locationhierarchy/Region")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004getLocationDataByHierarchyNameFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/locationhierarchy/aa")).andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t024updateLocationStatusTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/locations").param("code", "KNT").param("isActive", "true"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t023updateLocationHierarchyDetailsTest() throws Exception {
		locationRequestDto.getRequest().setCode("MOR");
		locationRequestDto.getRequest().setName("MyCountry");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-027");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t027updateLocationHierarchyDetailsTest() throws Exception {
		locationRequestDto.getRequest().setCode("MOR");
		locationRequestDto.getRequest().setName("MyCountry");
		locationRequestDto.getRequest().setHierarchyLevel((short)0);
		locationRequestDto.getRequest().setLangCode("eng");
		locationRequestDto.getRequest().setParentLocCode("");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-244");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023updateLocationHierarchyDetailsTest1() throws Exception {
		locationRequestDto.getRequest().setCode("MOR1");
		locationRequestDto.getRequest().setName("MyCountry1");
		locationRequestDto.getRequest().setParentLocCode("MOR");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-244");
	};

	@Test
	@WithUserDetails("global-admin")
	public void t022updateLocationHierarchyDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-027");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t022updateLocationHierarchyDetailsFailTest1() throws Exception {
		locationRequestDto.getRequest().setHierarchyLevel((short)8);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-027");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t005createLocationHierarchyDetailsFailTest1() throws Exception {
		locationCreateDtoReq.getRequest().setCode("00009");
		locationCreateDtoReq.getRequest().setHierarchyLevel((short)9);
		locationCreateDtoReq.getRequest().setHierarchyName("check");
		locationCreateDtoReq.getRequest().setName("00009");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-244");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t023updateLocationHierarchyDetailsTest2() throws Exception {
		locationRequestDto.getRequest().setCode("MOR1");
		locationRequestDto.getRequest().setName("MyCountry1");
		locationRequestDto.getRequest().setParentLocCode("MOR");
		locationRequestDto.getRequest().setHierarchyLevel((short)0);
		locationRequestDto.getRequest().setHierarchyName("Country");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				"KER-MSD-026");
	};
	
	@Test
	@WithUserDetails("global-admin")
	public void t023updateLocationHierarchyDetailsTest3() throws Exception {
		locationRequestDto.getRequest().setCode("RSK");
		locationRequestDto.getRequest().setName("Rabat Sale Kenitra");
		locationRequestDto.getRequest().setParentLocCode("MOR");
		locationRequestDto.getRequest().setHierarchyLevel((short)1);
		locationRequestDto.getRequest().setHierarchyName("Region");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationRequestDto))).andReturn(),
				null);
	};
	
	@Test
	@WithUserDetails("global-admin")
	public void t005createLocationHierarchyDetailsTest() throws Exception {
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Country");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("");
		
		locationCreateDtoReq.setRequest(createDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005createLocationHierarchyDetailsTest4() throws Exception {
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11111");
		createDto.setHierarchyLevel((short) 0);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Country");
		createDto.setLangCode("eng");
		createDto.setName("11111");
		createDto.setParentLocCode("");
		
		locationCreateDtoReq.setRequest(createDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-385");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005createLocationHierarchyDetailsTest1() throws Exception {
		LocationCreateDto createDto = new LocationCreateDto();
		createDto.setCode("11112");
		createDto.setHierarchyLevel((short) 1);
		createDto.setIsActive(true);
		createDto.setHierarchyName("Province");
		createDto.setLangCode("eng");
		createDto.setName("11112");
		createDto.setParentLocCode("11111");
		
		locationCreateDtoReq.setRequest(createDto);
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-243");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006createLocationHierarchyDetailsFailTest() throws Exception {
		locationCreateDtoReq.getRequest().setParentLocCode("qa");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(locationCreateDtoReq)))
								.andReturn(),
						"KER-MSD-243");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006createLocationHierarchyDetailsFailTest1() throws Exception {
		
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(null)))
								.andReturn(),
						"KER-MSD-242");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t007createLocationHierarchyDetailsFailTest2() throws Exception {
		locationCreateDtoReq.getRequest().setHierarchyLevel((short) 1 );
		locationCreateDtoReq.getRequest().setCode("20000");
		locationCreateDtoReq.getRequest().setName("20000");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-244");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007createLocationHierarchyDetailsTest() throws Exception {
		locationCreateDtoReq.getRequest().setHierarchyLevel((short) 0 );
		locationCreateDtoReq.getRequest().setHierarchyName("Country");
		locationCreateDtoReq.getRequest().setLangCode("eng");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007createLocationHierarchyDetailsTest1() throws Exception {
		locationCreateDtoReq.getRequest().setHierarchyLevel((short) 1 );
		locationCreateDtoReq.getRequest().setHierarchyName("Region");
		locationCreateDtoReq.getRequest().setLangCode("eng");
		locationCreateDtoReq.getRequest().setName("Rabat Sale Kenitra");
		locationCreateDtoReq.getRequest().setCode("RSK");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-385");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007createLocationHierarchyDetailsFailTest3() throws Exception {
		locationCreateDtoReq.getRequest().setHierarchyLevel((short) 1 );
		locationCreateDtoReq.getRequest().setCode("RSK");
		locationCreateDtoReq.getRequest().setName("RSK");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(locationCreateDtoReq))).andReturn(),
				"KER-MSD-244");
	}
	@Test
	@WithUserDetails("global-admin")
	public void t019getLocationsTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.get("/locations/all").param("pageNumber", "0")
								.param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
						.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t019getLocationsFailTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.get("/locations/all").param("pageNumber", "4")
								.param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
						.andReturn(), "KER-MSD-026");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t008getLocationsTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.get("/locations/all").param("pageNumber", "0")
								.param("pageSize", "10").param("sortBy", "createdDateTime").param("orderBy", "desc"))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018getLocationCodeByLangCodeTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/level/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009getLocationCodeByLangCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/level/eng1")).andReturn(), "KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t010getMissingLocationDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/locations/missingids").param("langcode", "eng1").param("fieldName", "name"))
				.andReturn(), "KER-MSD-026");
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t010getMissingLocationDetailsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(
				MockMvcRequestBuilders.get("/locations/missingids/eng").param("fieldName", "name"))
				.andReturn(),null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t011validateLocationNameTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/locations/validate/10000")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t012getImmediateChildrenByLocCodeAndLangCodeTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/RSK/eng")).andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013getImmediateChildrenByLocCodeAndLangCodeFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/1001/eng")).andReturn(),
				"KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t014searchLocationTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/locations/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t015searchLocationTest() throws Exception {
		//searchDtoRq
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/locations/search")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(searchDtoRq))).andReturn(),
				null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t017locationFilterValuesTest() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017locationFilterValuesTest3() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.ALL.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017locationFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.EMPTY.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), "KER-MSD-322");
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t017locationFilterValuesTest2() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType(FilterColumnEnum.UNIQUE.toString());
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t016locationFilterValuesTest() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/locations/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t031deleteLocationHierarchyDetailsTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/locations/MOGR")).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t031deleteLocationHierarchyDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/locations/10090")).andReturn(),
				"KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t20getMissingLocationDetailsFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/missingids/eng1")
				.param("fieldName", "1")).andReturn(), "KER-LANG-ERR");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t021updateLocationStatusFailTest() throws Exception {

		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/locations").param("code", "10099").param("isActive", "false"))
				.andReturn(), "KER-MSD-026");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t021getImmediateChildrenByLocCode() throws Exception {

		MasterDataTest
				.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/locations/immediatechildren/RSK?languageCodes=eng,tam")).andReturn(), null);
	}

}
