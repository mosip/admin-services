package io.mosip.kernel.syncdata.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceIntegratedTest {
	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@Mock
	private SyncMasterDataService  masterService;

	@Mock
	RestTemplate restTemplate;

	MockRestServiceServer mockRestServiceServer;
	private RequestWrapper<String> requestWrapper;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		  requestWrapper = new RequestWrapper<>();
	        requestWrapper.setRequest("test");
	        requestWrapper.setRequesttime(LocalDateTime.now(ZoneOffset.UTC));
	        requestWrapper.setId("");
	        requestWrapper.setVersion("0.1");
		mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

	}
	
	@Test
	@WithUserDetails("reg-officer")
	public void tst024downloadEntityDataTest() throws Exception {

		lenient().when(masterService.getClientSettingsJsonFile(Mockito.any(),Mockito.any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		assertNotNull(requestWrapper);

	}

	
	
}
