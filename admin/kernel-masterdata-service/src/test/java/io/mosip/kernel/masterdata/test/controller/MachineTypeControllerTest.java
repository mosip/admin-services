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
import io.mosip.kernel.masterdata.dto.MachineTypeDto;
import io.mosip.kernel.masterdata.dto.MachineTypePutDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MachineTypeControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	private RequestWrapper<SearchDtoWithoutLangCode> sr = new RequestWrapper<>();
	private RequestWrapper<FilterValueDto> fv = new RequestWrapper<FilterValueDto>();
	private RequestWrapper<MachineTypeDto> machineType=new RequestWrapper<MachineTypeDto>();
    private RequestWrapper<MachineTypePutDto> machineTypePut=new RequestWrapper<MachineTypePutDto>();
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		MachineTypeDto dto=new MachineTypeDto();
		dto.setCode("L2");
		dto.setDescription("L2");
		dto.setIsActive(true);
		dto.setLangCode("eng");
		dto.setName("Laptop 2");
		machineType.setRequest(dto);
		
		MachineTypePutDto dtop=new MachineTypePutDto();
		dtop.setCode("Vostro");
		dtop.setDescription("Vostro updated");
		dtop.setIsActive(true);
		dtop.setLangCode("eng");
		dtop.setName("Laptop 2");
		
		machineTypePut.setRequest(dtop);
		
		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("code");
		sf.setType("contains");
		sf.setValue("DKS");
		ls.add(sf);
		sc.setFilters(ls);
		sc.setLanguageCode("eng");
		sc.setPagination(pagination);
		sc.setSort(ss);

		sr.setRequest(sc);
		FilterValueDto filterValueDto = new FilterValueDto();
		List<FilterDto> l = new ArrayList<FilterDto>();
		FilterDto f = new FilterDto();
		f.setColumnName("code");
		f.setText("L2");
		f.setType("All");
		l.add(f);
		filterValueDto.setFilters(l);
		filterValueDto.setLanguageCode("eng");
		fv.setRequest(filterValueDto);
	}
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineTypeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineType))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002createMachineTypeFailTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineType))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateMachineTypeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machinetypes").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineTypePut))).andReturn(),
				"KER-MSD-064");
	}

	/*@Test
	@WithUserDetails("global-admin")
	public void t004updateMachineTypeFailTest() throws Exception {
		machineTypePut.getRequest().setCode("2");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machinetypes").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineTypePut))).andReturn(),
				"KER-MSD-063");
	}*/
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineTypeStatusTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinetypes").param("code","DKS").param("isActive", "true")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineTypeStatusTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinetypes").param("code","VDKS").param("isActive", "false")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineTypeStatusTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinetypes").param("code","DKS").param("isActive", "false")).andReturn(),
				"KER-MSD-065");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t006updateMachineTypeStatusFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinetypes").param("code","3").param("isActive", "false")).andReturn(),
				"KER-MSD-063");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getAllMachineTypesTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/machinetypes/all")).andReturn(),
				null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t008searchMachineTypeTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-318");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchMachineTypeTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010searchMachineTypeTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-318");
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineTypeTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-318");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t012searchMachineTypeFailTest() throws Exception {
		sr.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-317");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t013machineTypesFilterValuesTest() throws Exception {
		fv.getRequest().getFilters().get(0).setType("unique");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				"KER-MSD-317");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014machineTypesFilterValuesTest() throws Exception {
		fv.getRequest().getFilters().get(0).setType("all");
		fv.getRequest().getFilters().get(0).setColumnName("name");
		fv.getRequest().getFilters().get(0).setText("Desktop");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				null);
	}
	
		
	@Test
	@WithUserDetails("global-admin")
	public void t016machineTypesFilterValuesFailTest() throws Exception {
		fv.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinetypes/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				"KER-MSD-317");
	}

	
}
