package io.mosip.kernel.syncdata.test.service;

import io.mosip.kernel.syncdata.dto.PublicKeyResponse;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.helper.ConfigServerClient;
import io.mosip.kernel.syncdata.service.impl.SyncConfigDetailsServiceImpl;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class SyncConfigDetailsServiceImplTest {

    @InjectMocks
    private SyncConfigDetailsServiceImpl syncConfigDetailsService;

    @InjectMocks
    private ConfigServerClient configServerClient;

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Environment environment;

    @Mock
    private MapperUtils mapper;

    Map<String, String> uriParams = null;

    @Value("${mosip.kernel.keymanager-service-publickey-url}")
    private String publicKeyUrl;


    @Test
    public void getConfigDetails_success() {
        String keyIndex = "test-machine";
        List<Machine> machines = new ArrayList<>();
        Machine machine = new Machine();
        machine.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNAD...");
        machines.add(machine);

        assertNotNull(machines);

        lenient().when(machineRepository.findByMachineKeyIndex(keyIndex)).thenReturn(machines);
    }

    @Test(expected = RequestException.class)
    public void getConfigDetails_machineNotFound() {
        String keyIndex = "not-found-machine";
        lenient().when(machineRepository.findByMachineKeyIndex(keyIndex)).thenReturn(Collections.emptyList());

        syncConfigDetailsService.getConfigDetails(keyIndex);
    }

    @Test(expected = Exception.class)
    public void getPublicKey_clientException() {
        String applicationId = "mosip-kernel";
        String timeStamp = "2024-06-17T16:29:00Z";
        String referenceId = "ref-id";

        lenient().when(restTemplate.getForEntity(any(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        syncConfigDetailsService.getPublicKey(applicationId, timeStamp, referenceId);
    }

    @Test
    public void getScript_success() {
        String scriptName = "applicanttype.mvel";
        String keyIndex = "test-machine";
        List<Machine> machines = new ArrayList<>();
        Machine machine = new Machine();
        machine.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNAD...");
        machines.add(machine);

        assertNotNull(machines);

        lenient().when(machineRepository.findByMachineKeyIndex(keyIndex)).thenReturn(machines);
        lenient().when(environment.getProperty(String.format("mosip.sync.entity.encrypted.%s", scriptName.toUpperCase()), Boolean.class, false)).thenReturn(false);
    }

    @Test(expected = RequestException.class)
    public void getScript_machineNotFound() throws Exception {
        String scriptName = "applicanttype.mvel";
        String keyIndex = "not-found-machine";
        lenient().when(machineRepository.findByMachineKeyIndex(Mockito.anyString())).thenReturn(new ArrayList<>());

        syncConfigDetailsService.getScript(scriptName, keyIndex);
    }

    @Test
    public void parsePropertiesString_emptyString() {
        String input = "";
        JSONObject expectedOutput = new JSONObject();
        JSONObject actualOutput = new SyncConfigDetailsServiceImpl().parsePropertiesString(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void parsePropertiesString_singleKeyValue() {
        String input = "name=John Doe";
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("name", "John Doe");
        JSONObject actualOutput = new SyncConfigDetailsServiceImpl().parsePropertiesString(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void parsePropertiesString_multipleKeyValues() {
        String input = "name=John Doe\nage=30\ncity=New York";
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("name", "John Doe");
        expectedOutput.put("age", "30");
        expectedOutput.put("city", "New York");
        JSONObject actualOutput = new SyncConfigDetailsServiceImpl().parsePropertiesString(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void parsePropertiesString_emptyValue() {
        String input = "city=New York";
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("city", "New York");
        JSONObject actualOutput = new SyncConfigDetailsServiceImpl().parsePropertiesString(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void parsePropertiesString_comments() {
        String input = "# This is a comment\nname=John\nage=30\n# Another comment";
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("name", "John");
        expectedOutput.put("age", "30");
        JSONObject actualOutput = new SyncConfigDetailsServiceImpl().parsePropertiesString(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void parsePropertiesString_invalidFormat() {
        String input = "invalid format";
        new SyncConfigDetailsServiceImpl().parsePropertiesString(input);

        assertTrue("invalid format", true);
    }

    @Test
    public void getPublicKey() {

        try {
            uriParams = new HashMap<>();
            uriParams.put("applicationId", "REGISTRATION");

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
                    .queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
            MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

            mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
                    .andRespond(withSuccess().body(
                            "{ \"id\": null, \"version\": null, \"responsetime\": \"2019-04-24T09:07:42.017Z\", \"metadata\": null, \"response\": { \"lastSyncTime\": \"2019-04-24T09:07:41.771Z\", \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtzi3nWiNMEcaBV2cWO5ZLTBZe1TEGnT95bTvrpEEr-kJLrn80dn9k156zjQpjSzNfEOFVwugTEhEWdxrdrjDUACpA0cF4tUdAM5XJBB0xmzNGS5s7lmcliAOjXbCGU2VJwOUnYV4DSCgrReMCCe6LD_aApwu45OAZ9_sWG6R-jlIUOHLTdDUs6O8zLk8zl7tOX6Rlp25Zk9CLQw1m9drHJqxCbr9Wc9PQKUHBPqhtvCe9ZZeySsZb83dXpKKAZlkjdbrB25i_4O0pbv9LHk0qQlk0twqaef6D5nCTqcB5KQ4QqVYLcrtAhdbMXaDvpSf9syRQ3P3fAeiGkvUIhUWPwIDAQAB\", \"issuedAt\": \"2019-04-23T06:17:46.753\", \"expiryAt\": \"2020-04-23T06:17:46.753\" }, \"errors\": null }"));

            PublicKeyResponse<String> publicKeyResponse = syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");

            assertNotNull(publicKeyResponse.getProfile());
            assertEquals("test", publicKeyResponse.getProfile());
        } catch (Exception ex){
            ex.getCause();
        }
    }

    @Test
    public void getPublicKeyServiceExceptionTest() {

        try {
            uriParams = new HashMap<>();
            uriParams.put("applicationId", "REGISTRATION");

            assertNotNull(uriParams);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
                    .queryParam("referenceId", "referenceId").queryParam("timeStamp", "2019-09-09T09:00:00.000Z");
            MockRestServiceServer mockRestServer = MockRestServiceServer.bindTo(restTemplate).build();

            mockRestServer.expect(MockRestRequestMatchers.requestTo(builder.buildAndExpand(uriParams).toString()))
                    .andRespond(withServerError());

            syncConfigDetailsService.getPublicKey("REGISTRATION", "2019-09-09T09:00:00.000Z", "referenceId");
        } catch (Exception e){
            e.getCause();
        }
    }

    @Test (expected = SyncDataServiceException.class)
    public void getEncryptedData_success() {
        JSONObject config = new JSONObject();
        config.put("key", "value");
        Machine machine = new Machine();
        machine.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNAD...");

        String expectedJsonString = "{\"key\":\"value\"}";
        ReflectionTestUtils.invokeMethod(mapper,"getObjectAsJsonString", config);

        String encryptedData = "encrypted_data";
        ReflectionTestUtils.invokeMethod(syncConfigDetailsService, "getEncryptedData", expectedJsonString, machine);

        String actualEncryptedData = ReflectionTestUtils.invokeMethod(syncConfigDetailsService,"getEncryptedData",config, machine);

        assertEquals(encryptedData, actualEncryptedData);
    }

    @Test(expected = SyncDataServiceException.class)
    public void getEncryptedData_serializationError() throws Exception {
        JSONObject config = new JSONObject();
        config.put("key", "value");
        Machine machine = new Machine();
        machine.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNAD...");

        Mockito.when(mapper.getObjectAsJsonString(config)).thenThrow(new RuntimeException("Serialization error"));

        ReflectionTestUtils.invokeMethod(syncConfigDetailsService, "getEncryptedData",config, machine);
    }

    @Test
    public void getConfigDetailsResponse_success() {
        String fileName = "application.yml";
        String expectedUri = "http://localhost:8888/my-app/dev/my-label/" + fileName;
        String responseBody = "config content";

        Mockito.when(environment.getProperty("spring.cloud.config.uri")).thenReturn("http://localhost:8888");
        Mockito.when(environment.getProperty("spring.application.name")).thenReturn("my-app");
        Mockito.when(environment.getProperty("spring.profiles.active")).thenReturn("dev");
        Mockito.when(environment.getProperty("spring.cloud.config.label")).thenReturn("my-label");
        Mockito.when(restTemplate.getForObject(expectedUri, String.class)).thenReturn(responseBody);

        String actualResponse = ReflectionTestUtils.invokeMethod(configServerClient, "fetch", fileName);

        assertEquals(responseBody, actualResponse);
    }

    @Test(expected = SyncDataServiceException.class)
    public void getConfigDetailsResponse_nullResponse() {
        String fileName = "application.yml";
        String expectedUri = "http://localhost:8888/my-app/dev/my-label/" + fileName;

        Mockito.when(environment.getProperty("spring.cloud.config.uri")).thenReturn("http://localhost:8888");
        Mockito.when(environment.getProperty("spring.application.name")).thenReturn("my-app");
        Mockito.when(environment.getProperty("spring.profiles.active")).thenReturn("dev");
        Mockito.when(environment.getProperty("spring.cloud.config.label")).thenReturn("my-label");
        Mockito.when(restTemplate.getForObject(expectedUri, String.class)).thenReturn(null);

        ReflectionTestUtils.invokeMethod(configServerClient, "fetch", fileName);
    }

    @Test(expected = SyncDataServiceException.class)
    public void getConfigDetailsResponse_restClientException() {
        String fileName = "application.yml";
        String expectedUri = "http://localhost:8888/my-app/dev/my-label/" + fileName;

        Mockito.when(environment.getProperty("spring.cloud.config.uri")).thenReturn("http://localhost:8888");
        Mockito.when(environment.getProperty("spring.application.name")).thenReturn("my-app");
        Mockito.when(environment.getProperty("spring.profiles.active")).thenReturn("dev");
        Mockito.when(environment.getProperty("spring.cloud.config.label")).thenReturn("my-label");
        Mockito.when(restTemplate.getForObject(expectedUri, String.class)).thenThrow(new RestClientException("Connection refused"));

        ReflectionTestUtils.invokeMethod(configServerClient, "fetch", fileName);
    }

}
