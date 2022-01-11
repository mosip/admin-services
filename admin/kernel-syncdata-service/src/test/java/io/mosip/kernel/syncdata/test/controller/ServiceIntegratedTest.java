package io.mosip.kernel.syncdata.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceIntegratedTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@MockBean
	private SyncMasterDataService  masterService;
	
//	@MockBean
//	private SyncUserDetailsService syncUser;

	@Autowired
	RestTemplate restTemplate;

	MockRestServiceServer mockRestServiceServer;
	private RequestWrapper<String> requestWrapper;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
		  requestWrapper = new RequestWrapper<>();
	        requestWrapper.setRequest("asdfasdfasdfads");
	        requestWrapper.setRequesttime(LocalDateTime.now(ZoneOffset.UTC));
	        requestWrapper.setId("");
	        requestWrapper.setVersion("0.1");

	}
	
	@Test
	@WithUserDetails("reg-officer")
	public void tst024downloadEntityDataTest() throws Exception {

		when(masterService.getClientSettingsJsonFile(Mockito.any(),Mockito.any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/abcd").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")).andReturn(),
				null);

	}
	
	@Test
	@WithUserDetails("reg-officer")
	public void tst024downloadEntityDataTest1() throws Exception {

		when(masterService.getClientSettingsJsonFile(Mockito.any(),Mockito.any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/abcd").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")).andReturn(),
				null);

	}
	
	
	/*@Test
    public void sendOTPFailure1() throws Exception {
		
     /*   when(syncAuthTokenService.sendOTP(Mockito.anyString())).thenThrow(new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage()));*/
    
	/*	mockRestServiceServer.expect(requestTo("https://dev.mosip.io/authmanager/v1.0/registrationclient"))
		.andRespond(withStatus(HttpStatus.FORBIDDEN).body("").contentType(MediaType.APPLICATION_JSON));

        SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/authenticate/sendotp").contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(""))).andReturn(), null);
        
        
       }

	

	@Test
    public void sendOTPFailure11() throws Exception {
		
     /*   when(syncAuthTokenService.sendOTP(Mockito.anyString())).thenThrow(new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage()));*/
    
	/*	mockRestServiceServer.expect(requestTo("https://dev.mosip.io/authmanager/v1.0/registrationclient"))
		.andRespond(withStatus(HttpStatus.BAD_REQUEST).body("").contentType(MediaType.APPLICATION_JSON));

        SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/authenticate/sendotp").contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(""))).andReturn(), null);
        
        
       }
	

	@Test
    public void sendOTPFailure() throws Exception {
		
     /*   when(syncAuthTokenService.sendOTP(Mockito.anyString())).thenThrow(new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage()));*/
    
	/*	mockRestServiceServer.expect(requestTo("https://dev.mosip.io/authmanager/v1.0/registrationclient"))
		.andRespond(withStatus(HttpStatus.FORBIDDEN).body(mapper.writeValueAsString(requestWrapper)).contentType(MediaType.APPLICATION_JSON));

        SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.post("/authenticate/sendotp").contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(requestWrapper))).andReturn(), null);
        
        
       }*/


}
