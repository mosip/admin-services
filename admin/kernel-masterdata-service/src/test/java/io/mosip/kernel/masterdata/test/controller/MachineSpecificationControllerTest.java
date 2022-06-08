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
import io.mosip.kernel.masterdata.dto.MachineSpecificationDto;
import io.mosip.kernel.masterdata.dto.MachineSpecificationPutDto;
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
public class MachineSpecificationControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	private RequestWrapper<MachineSpecificationDto> m=new RequestWrapper<MachineSpecificationDto>();
	private RequestWrapper<MachineSpecificationPutDto> machineSpecification=new RequestWrapper<MachineSpecificationPutDto>();
	private RequestWrapper<SearchDtoWithoutLangCode> sr = new RequestWrapper<>();
	private RequestWrapper<FilterValueDto> fv = new RequestWrapper<FilterValueDto>();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString());
		MachineSpecificationDto dto=new MachineSpecificationDto();
		dto.setBrand("DELL");
		dto.setDescription("Dell brand");
		dto.setId("1002");
		dto.setLangCode("eng");
		dto.setMachineTypeCode("Vostro");
		dto.setMinDriverversion("1.3");
		dto.setModel("Dell");
		dto.setName("Dell");
    	dto.setIsActive(true);

		m.setRequest(dto);
		MachineSpecificationPutDto dto1=new MachineSpecificationPutDto();
		dto1.setBrand("DELLupdate");
		dto1.setDescription("Dell brandupdate");
		dto1.setId("1001");
		dto1.setLangCode("eng");
		dto1.setMachineTypeCode("Vostro");
		dto1.setMinDriverversion("1.3");
		dto1.setModel("Dell");
		dto1.setName("Dell");
		machineSpecification.setRequest(dto1);
		
		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("name");
		sf.setType("contains");
		sf.setValue("HP");
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
		f.setText("HP");
		f.setType("All");
		
		l.add(f);
		filterValueDto.setFilters(l);
		filterValueDto.setLanguageCode("eng");
		fv.setRequest(filterValueDto);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001createMachineSpecificationTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(m))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t002createMachineSpecificationFailTest() throws Exception {
        m.getRequest().setMachineTypeCode("test");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(m))).andReturn(),
				"KER-MSD-722");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003updateMachineSpecificationTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineSpecification))).andReturn(),
				null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateMachineSpecificationFailTest() throws Exception {
		machineSpecification.getRequest().setId("2");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/machinespecifications").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(machineSpecification))).andReturn(),
				"KER-MSD-117");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineSpecificationStatusTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","1001").param("isActive", "true")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineSpecificationStatusTest1() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","1001").param("isActive", "false")).andReturn(),
				"KER-MSD-088");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005updateMachineSpecificationStatusTest2() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","100100").param("isActive", "false")).andReturn(),
				null);
	}


	@Test
	@WithUserDetails("global-admin")
	public void t006updateMachineSpecificationStatusTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","3").param("isActive", "false")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateMachineSpecificationStatusTest1() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","300").param("isActive", "false")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006updateMachineSpecificationStatusTest2() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/machinespecifications").param("id","11").param("isActive", "false")).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007getAllMachineSpecificationsTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/machinespecifications/all")).andReturn(),
				null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t008searchMachineSpecificationTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchMachineSpecificationTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchMachineSpecificationTest1() throws Exception {
		sr.getRequest().getFilters().get(0).setType("equals");
		sr.getRequest().getSort().get(0).setSortField("machineTypeName");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchMachineSpecificationTest2() throws Exception {
		sr.getRequest().getFilters().get(0).setType("Contains");
		sr.getRequest().getFilters().get(0).setValue("HP");
		sr.getRequest().getFilters().get(0).setColumnName("machineTypeName");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t009searchMachineSpecificationTest4() throws Exception {
		sr.getRequest().getFilters().get(0).setType("Contains");
		sr.getRequest().getFilters().get(0).setValue("Desktop");
		sr.getRequest().getFilters().get(0).setColumnName("machineTypeName");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t010searchMachineSpecificationTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("startsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				null);
	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t011searchMachineSpecificationTest() throws Exception {
		sr.getRequest().getFilters().get(0).setType("endsWith");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-318");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t012searchMachineSpecificationFailTest() throws Exception {
		sr.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/search").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sr))).andReturn(),
				"KER-MSD-317");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t013machineSpecificationFilterValuesTest() throws Exception {
		fv.getRequest().getFilters().get(0).setType("unique");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				null);
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t014machineSpecificationFilterValuesTest() throws Exception {
		fv.getRequest().getFilters().get(0).setType("empty");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				"KER-MSD-324");
	}
	

		
	@Test
	@WithUserDetails("global-admin")
	public void t016machineSpecificationFilterValuesFailTest() throws Exception {
		fv.getRequest().getFilters().get(0).setColumnName("namee");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.post("/machinespecifications/filtervalues").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(fv))).andReturn(),
				"KER-MSD-317");
	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t017deleteMachineSpecificationTest() throws Exception {

		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/machinespecifications/1001")).andReturn(),
				"KER-MSD-122");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t018deleteMachineSpecificationFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.delete("/machinespecifications/3")).andReturn(),
				"KER-MSD-117");
	}
	
}
