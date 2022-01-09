package io.mosip.kernel.syncdata.test.controller;

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
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.util.HMACUtils2;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.nio.charset.StandardCharsets;

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

	@Autowired
	RestTemplate restTemplate;

	MockRestServiceServer mockRestServiceServer;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

	}
	
	@Test
	@WithUserDetails("reg-officer")
	public void tst024downloadEntityDataTest() throws Exception {

		when(masterService.getClientSettingsJsonFile(Mockito.any(),Mockito.any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		SyncDataUtil.checkResponse(
				mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings/abcd").param("keyindex", "B5:70:23:28:D4:C1:E2:C4:1C:C1:2A:E8:62:A9:18:3F:28:93:F9:3D:EB:AE:F7:56:FA:0B:9D:D0:3E:87:25:48")).andReturn(),
				null);

	}

	
}
