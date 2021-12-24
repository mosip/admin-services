package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.Mockito.doNothing;

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
import io.mosip.kernel.masterdata.dto.request.WorkingDaysPutRequestDto;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkingDayControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@MockBean
	private AuditUtil auditUtil;

	private ObjectMapper mapper;

	RequestWrapper<WorkingDaysPutRequestDto> workingDayPutReq=new RequestWrapper<>(); 
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		WorkingDaysPutRequestDto putRequestDto=new WorkingDaysPutRequestDto();
		putRequestDto.setCode("101");
		putRequestDto.setDaySeq((short) 1);
		putRequestDto.setGlobalWorking(false);
		putRequestDto.setLangCode("eng");
		putRequestDto.setName("SUN");
		
		workingDayPutReq.setRequest(putRequestDto);
		

	}


	@Test
	@WithUserDetails("global-admin")
	public void t001getWeekDaysTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/weekdays/10001/eng"))
				.andReturn(),"KER-MSD-002");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getWeekDaysTest1() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/weekdays/10001/ara"))
				.andReturn(),"KER-WKDS-002");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t002getWeekDaysFailTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/weekdays/10001/eng1"))
				.andReturn(),"KER-MSD-999");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getWorkindaysTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/10001/eng"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getWorkindaysTest1() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/10004/eng"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t004getWorkindaysFailTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/10001/eng1"))
				.andReturn(),"KER-MSD-999");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t005getWorkingDaysByLangCodeTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/eng"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t005getWorkingDaysByLangCodeFailTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/ara"))
				.andReturn(),"KER-WKDS-003");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t006getWorkingDaysByLangCodeFailTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/eng1"))
				.andReturn(),"KER-MSD-999");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t007updateWorkingDaysTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/workingdays").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(workingDayPutReq)))
				.andReturn(),"KER-MSD-003");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t008updateWorkingDaysFailTest() throws Exception
	{
		workingDayPutReq.getRequest().setCode("200");
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.put("/workingdays").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(workingDayPutReq)))
				.andReturn(),"KER-WKDS-003");

	}
	
	
	@Test
	@WithUserDetails("global-admin")
	public void t009updateWorkingDaysStatusTest() throws Exception
	{
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/workingdays").param("code", "101").param("isActive","true"))
				.andReturn(),null);

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t010updateWorkingDaysStatusFailTest() throws Exception
	{
		
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.patch("/workingdays").param("code", "200").param("isActive","true"))
				.andReturn(),"KER-WKDS-003");

	}
}
