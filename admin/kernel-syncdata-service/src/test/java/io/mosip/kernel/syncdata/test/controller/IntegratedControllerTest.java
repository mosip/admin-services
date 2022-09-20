package io.mosip.kernel.syncdata.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.syncdata.entity.Location;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import io.mosip.kernel.syncdata.test.utils.SyncDataUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegratedControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	private ObjectMapper mapper;

	@MockBean
	private ClientSettingsHelper clientSettingsHelper;

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
	@WithUserDetails(value = "reg-officer")
	public void tst001syncClientSettingsTest4() throws Exception {

		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
		when(clientSettingsHelper.getInitiateDataFetch(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(futuresMap);
		String str3 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/clientsettings").param("keyindex",
				"41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29")
				.param("regcenterId", "10001")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void tst001syncClientSettingsTest2() throws Exception {

		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
		when(clientSettingsHelper.getInitiateDataFetch(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(futuresMap);
		String str3 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

		SyncDataUtil.checkResponse(mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29")
				.param("regcenterId", "10001")).andReturn(), null);

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void tst001syncClientSettingsTest3() throws Exception {

		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
		when(clientSettingsHelper.getInitiateDataFetch(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString())).thenThrow(new RuntimeException() {
				});
		String str3 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29")
				.param("regcenterId", "10001")).andExpect(status().is(500));

	}

	@Test
	@WithUserDetails(value = "reg-officer")
	public void tst001syncClientSettingsTest5() throws Exception {

		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
		CompletableFuture c = new CompletableFuture<>();
		c.completeExceptionally(new SyncDataServiceException("", ""));
		futuresMap.put(Location.class, c);
		when(clientSettingsHelper.getInitiateDataFetch(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(futuresMap);
		String str3 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));

		mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29")
				.param("regcenterId", "10001")).andExpect(status().is(500));

	}
	@Test
	@WithUserDetails(value = "reg-officer")
	public void tst001syncClientSettingsTest6() throws Exception {

		Map<Class, CompletableFuture> futuresMap = new HashMap<>();
		CompletableFuture c=new CompletableFuture<>();
		c.completeExceptionally(new CompletionException("",new RuntimeException()));
		futuresMap.put(Location.class, c);

		when(clientSettingsHelper.getInitiateDataFetch(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyString()))
						.thenReturn(futuresMap);
		String str3 = "{\"id\":null,\"version\":null,\"responsetime\":\"2021-12-08T09:52:44.551Z\",\"metadata\":null,\"response\":{\"jwtSignedData\":\"signed\",\"timestamp\":null},\"errors\":[]}";

		mockRestServiceServer.expect(requestTo("https://dev.mosip.net/v1/keymanager/jwtSign"))
				.andRespond(withSuccess().body(str3).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(MockMvcRequestBuilders.get("/v2/clientsettings").param("keyindex",
				"41:3a:ed:6d:38:a0:28:36:72:a6:75:08:8a:41:3c:a3:4f:48:72:6f:c8:fb:29:dd:53:bd:6f:12:70:9b:e3:29")
				.param("regcenterId", "10001")).andExpect(status().is(500));
	}

}
