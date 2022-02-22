package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import io.mosip.kernel.masterdata.dto.DeviceDto;
import io.mosip.kernel.masterdata.dto.DevicePutReqDto;
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
public class DeviceControllerTest {
	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public DataSource dataSource;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private RequestWrapper<DeviceDto> deviceDtoReq = new RequestWrapper<DeviceDto>();
	private RequestWrapper<DevicePutReqDto> devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
	private RequestWrapper<SearchDtoWithoutLangCode> searchLangCode = new RequestWrapper<SearchDtoWithoutLangCode>();
	private RequestWrapper<FilterValueDto> filValDto = new RequestWrapper<FilterValueDto>();

	private ObjectMapper mapper;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		DeviceDto d1 = new DeviceDto();
		//d1.setId("1004");
		d1.setDeviceSpecId("327");
		d1.setIsActive(true);
		d1.setLangCode("eng");

		d1.setName("Mock Iris Scanner");
		d1.setMacAddress("85-BB-97-4B-14-05");
		d1.setRegCenterId("10001");
		d1.setSerialNum("3456789012");

		d1.setZoneCode("NTH");
		deviceDtoReq.setRequest(d1);

		devicePutReqDtoReq = new RequestWrapper<DevicePutReqDto>();
		DevicePutReqDto devicePutReqDto = new DevicePutReqDto();
		devicePutReqDto.setId("3000038");
		devicePutReqDto.setDeviceSpecId("327");
		devicePutReqDto.setIsActive(true);
		devicePutReqDto.setLangCode("eng");

		devicePutReqDto.setName("Mock Iris Scanner updted");
		devicePutReqDto.setMacAddress("85-BB-97-4B-14-05");
		devicePutReqDto.setRegCenterId("10001");
		devicePutReqDto.setSerialNum("3456789012");

		devicePutReqDto.setZoneCode("NTH");
		devicePutReqDtoReq.setRequest(devicePutReqDto);

		SearchDtoWithoutLangCode sc = new SearchDtoWithoutLangCode();
		List<io.mosip.kernel.masterdata.dto.request.SearchFilter> ls = new ArrayList<>();
		io.mosip.kernel.masterdata.dto.request.SearchFilter sf = new io.mosip.kernel.masterdata.dto.request.SearchFilter();
		List<SearchSort> ss = new ArrayList<SearchSort>();
		Pagination pagination = new Pagination(0, 1);
		SearchSort s = new SearchSort("name", "ASC");
		ss.add(s);
		sf.setColumnName("name");
		sf.setType("equals");
		sf.setValue("Dummy Web Camera 20");
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
		dto.setText("Dummy Web Camera 20");
		dto.setType("UNIQUE");
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
	public void t001getDeviceLangTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng")).andReturn(),
				"KER-MSD-010");

	}

	@Test
	 @WithUserDetails("global-admin")
	public void t002createDeviceTest() throws Exception {
		//deviceDtoReq.getRequest().setZoneCode("RBT");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devices")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceDtoReq))).andReturn(),
				null);

	}
	
	@Test
	 @WithUserDetails("global-admin")
	public void t002createDeviceTest1() throws Exception {
		deviceDtoReq.getRequest().setRegCenterId("1113");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devices")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceDtoReq))).andReturn(),
				"KER-MSD-222");

	}

	@Test
	 @WithUserDetails("global-admin")
	public void t002createDeviceTest3() throws Exception {
		deviceDtoReq.getRequest().setZoneCode("NTH");
		deviceDtoReq.getRequest().setRegCenterId("10003");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devices")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceDtoReq))).andReturn(),
				"KER-MSD-219");

	}

	
	@Test
	@WithUserDetails("global-admin")
	public void t002createDeviceFailTest1() throws Exception {
		
		deviceDtoReq.getRequest().setZoneCode("JRD");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devices")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceDtoReq))).andReturn(),
				"KER-MSD-439");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getDeviceLangTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getDeviceLangCodeAndDeviceTypeTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng/FRS")).andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t003getDeviceLangCodeAndDeviceTypeFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng/IRS")).andReturn(),
				"KER-MSD-010");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateDeviceTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				null);

	}

	@Test
 	@WithUserDetails("global-admin")
	public void t004updateDeviceFailTest() throws Exception {
		
		devicePutReqDtoReq.getRequest().setName("updated");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-339");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t004updateDeviceFailTest1() throws Exception {
		
		devicePutReqDtoReq.getRequest().setName("updated");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-339");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t005getDevicesByRegistrationCenterTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/devices/mappeddevices/10001")).andReturn(), "KER-MSD-441");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006searchDeviceTest() throws Exception {
		
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/devices/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(searchLangCode)))
								.andReturn(),
						"KER-MSD-344");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t006searchDeviceTest1() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		searchLangCode.getRequest().getFilters().get(0).setValue("Finger");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/devices/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(searchLangCode)))
								.andReturn(),
						null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006searchDeviceTest3() throws Exception {
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		searchLangCode.getRequest().getFilters().get(0).setValue("Fingerprint");
		searchLangCode.getRequest().getFilters().get(0).setColumnName("deviceTypeName");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/devices/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(searchLangCode)))
								.andReturn(),
						null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007deviceFilterValuesTest() throws Exception {
		
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/devices/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t007deviceFilterValuesTest2() throws Exception {
		filValDto.getRequest().getFilters().get(0).setText("Dummy IRIS Scanner 18");
		filValDto.getRequest().getFilters().get(0).setColumnName("name");
		filValDto.getRequest().getFilters().get(0).setType("all");
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/devices/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);

	}
	

	@Test
	@WithUserDetails("global-admin")
	public void t007deviceFilterValuesTest1() throws Exception {
		filValDto.getRequest().getFilters().get(0).setType("");
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/devices/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), null);

	}

	@Test
	@WithUserDetails("global-admin")
	public void t008updateDeviceStatusTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc
				.perform(MockMvcRequestBuilders.patch("/devices").param("isActive", "true").param("id", "3000038"))
				.andReturn(), null);
	}

	@Test
	@WithUserDetails("global-admin")
	public void t009updateDeviceStatusFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.patch("/devices").param("isActive", "true").param("id", "1"))
						.andReturn(),
				"KER-MSD-042");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t017decommissionDeviceFailTest1() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices/decommission/3000040")).andReturn(), "KER-MSD-438");

	}
	@Test
	@WithUserDetails("global-admin")
	public void t017decommissionDeviceTest1() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices/decommission/3000058")).andReturn(), null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t017decommissionDeviceTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices/decommission/3000077")).andReturn(), "KER-MSD-439");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t018decommissionDeviceFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices/decommission/1004")).andReturn(), "KER-MSD-042");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t010deviceFilterValuesTest1() throws Exception {
		
		filValDto.getRequest().getFilters().get(0).setType("contains");
		MasterDataTest
				.checkResponse(mockMvc
						.perform(MockMvcRequestBuilders.post("/devices/filtervalues")
								.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(filValDto)))
						.andReturn(), "KER-MSD-324");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t011createDeviceFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/devices")
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(deviceDtoReq))).andReturn(),
				"KER-MSD-339");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t012updateDeviceFailTest() throws Exception {
		
		devicePutReqDtoReq.setId("7");
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.put("/devices").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(devicePutReqDtoReq))).andReturn(),
				"KER-MSD-339");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t013getDeviceLangCodeAndDeviceTypeFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng1/abcd")).andReturn(),
				"KER-MSD-010");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t014getDeviceLangFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/devices/eng1")).andReturn(),
				"KER-MSD-010");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t015searchDeviceFailTest() throws Exception {
		
		searchLangCode.getRequest().getFilters().get(0).setType("contains");
		searchLangCode.getRequest().getFilters().get(0).setValue("abcd");
		MasterDataTest
				.checkResponse(
						mockMvc.perform(
								MockMvcRequestBuilders.post("/devices/search").contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(searchLangCode)))
								.andReturn(),
						"KER-MSD-339");

	}

	@Test
	@WithUserDetails("global-admin")
	public void t016getDevicesByRegistrationCenterFailTest() throws Exception {
		
		MasterDataTest.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/devices/mappeddevices/101")).andReturn(), "KER-MSD-441");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t020deleteDeviceFailTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devices/100100")).andReturn(),
				"KER-MSD-010");
	}

	@Test
	@WithUserDetails("global-admin")
	public void t019deleteDeviceTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devices/3000058")).andReturn(), "KER-MSD-010");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t020deleteDeviceTest() throws Exception {
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.delete("/devices/3000077")).andReturn(), null);

	}

}
