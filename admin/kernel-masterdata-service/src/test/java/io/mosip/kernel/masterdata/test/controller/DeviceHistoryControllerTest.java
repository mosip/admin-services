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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.test.utils.MasterDataTest;
import io.mosip.kernel.masterdata.utils.AuditUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceHistoryControllerTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;
	
	@MockBean
	private AuditUtil auditUtil;
	private ObjectMapper mapper;
	
	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(auditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t001getDeviceHistoryIdLangEff() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/deviceshistories/3000021/eng/2021-08-27T07:26:36.491Z"))
				.andReturn(),"KER-MSD-128");

	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t002getDeviceHistoryIdLangEffTest() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/deviceshistories/1/eng1/2018-12-17T07:22:22.233Z")).andReturn(),"KER-MSD-129");
	}
	
	@Test
	@WithUserDetails("global-admin")
	public void t003getDeviceHistoryIdLangEffTest1() throws Exception {
		MasterDataTest.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/deviceshistories/1/eng1/17-12-2018T07:22:22.233Z")).andReturn(),"KER-MSD-033");
	}
	
	
}
