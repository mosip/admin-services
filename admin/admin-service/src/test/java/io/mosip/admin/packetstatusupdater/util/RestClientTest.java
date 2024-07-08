package io.mosip.admin.packetstatusupdater.util;

import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {RestClient.class})
@RunWith(SpringRunner.class)
public class RestClientTest {

    @MockBean
    private Environment environment;

    @Autowired
    private RestClient restClient;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void postApi_withValidInput_thenSuccess() throws Exception {

        ApiName apiName = ApiName.MACHINE_GET_API;
        List<String> pathsegments =List.of("hi","hello","welcome");
        String queryParamName = "name";
        String queryParamValue = "value";
        MediaType mediaType = mock(MediaType.class);
        Object requestType = mock(Object.class);
        Class<?> responseType = Class.class;

        restClient.postApi(apiName,pathsegments,queryParamName,queryParamValue,mediaType,requestType,responseType);
        assertNotNull(requestType);
    }

    @Test
    public void postApi_withRetrieveApi_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.RETRIEVE_IDENTITY_API, pathsegments,
                "Query Param Name", "42", null, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void postApi_withLostRIDApi_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, stringList, "Query Param Name", "42",
                null, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void postApi_withMediaType_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        MediaType mediaType = mock(MediaType.class);

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                mediaType, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void postApi_withHttpEntity_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        HttpEntity<Object> httpEntity = mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                null, httpEntity, Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

    @Test
    public void postApi_onlyWithRequestType_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, null, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test (expected = MasterDataServiceException.class)
    public void postApi_withoutPostForObject_returnErrorResponse() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenThrow(new MasterDataServiceException("An error occurred", "An error occurred"));
        restClient.postApi(ApiName.LOST_RID_API, null, "Request Type", Object.class);
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void getApi_withValidInput_thenSuccess() throws Exception {
        ApiName apiName = ApiName.MACHINE_GET_API;
        List<String> pathsegments = List.of("hi","hello","welcome");
        String queryParamName = "name";
        String queryParamValue = "value";
        Class<?> responseType = Class.class;

        restClient.getApi(apiName,pathsegments,queryParamName,queryParamValue,responseType);
        assertNotNull(pathsegments);
    }

    @Test
    public void getApi_withLostRIDAPI_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any());
    }

    @Test
    public void getApi_withHttpURL_thenSuccess() throws Exception {
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any(),
                (Object[]) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(restClient.getApi("https://dev.mosip.net", Object.class));
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any(), (Object[]) any());
    }


    @Test
    public void getApi_withMachineGetAPI_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertNull(restClient.getApi(ApiName.MACHINE_GET_API, stringList, "Query Param Name", "42", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any());
    }

    @Test (expected = Exception.class)
    public void getApi_withHttpURL_returnErrorResponse() throws Exception {
        ResponseEntity<Object> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenThrow(new MasterDataServiceException("An error occurred", "An error occurred"));
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any(),
                (Object[]) any())).thenReturn(responseEntity);
        restClient.getApi("https://dev.mosip.net/", Object.class);
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any(), (Object[]) any());
        verify(responseEntity).getBody();
    }

    @Test
    public void setRequestHeader_withValidInput_thenSuccess() {

        Object requestType = mock(Object.class);
        MediaType mediaType = mock(MediaType.class);

        ReflectionTestUtils.invokeMethod(restClient,"setRequestHeader",requestType,mediaType);
        assertNotNull(requestType);
    }

    @Test
    public void setRequestHeader_withHttpURL_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        MediaType mediaType = mock(MediaType.class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("https://dev.mosip.net/", "https://dev.mosip.net/");
        HttpEntity<Object> httpEntity = (HttpEntity<Object>) mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(httpHeaders);

        assertEquals("Post For Object", restClient.postApi(ApiName.MACHINE_GET_API, pathsegments, "Query Param Name",
                "42", mediaType, httpEntity, Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }
}