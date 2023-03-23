package io.mosip.admin.packetstatusupdater.util;

import io.mosip.admin.packetstatusupdater.constant.ApiName;
import io.mosip.admin.packetstatusupdater.exception.MasterDataServiceException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RestClient.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class RestClientTest {

    @MockBean
    private Environment environment;

    @Autowired
    private RestClient restClient;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testPostApi01() throws Exception {

        ApiName apiName = ApiName.MACHINE_GET_API;
        List<String> pathsegments =List.of("hi","hello","welcome");
        String queryParamName = "name";
        String queryParamValue = "value";
        MediaType mediaType = mock(MediaType.class);
        Object requestType = mock(Object.class);
        Class<?> responseType = Class.class;

        restClient.postApi(apiName,pathsegments,queryParamName,queryParamValue,mediaType,requestType,responseType);
    }

    @Test
    public void testPostApi02() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi03() throws Exception {
        when(environment.getProperty((String) any())).thenReturn(null);
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42", null, "Request Type",
                Object.class));
        verify(environment).getProperty((String) any());
    }

    @Test
    public void testPostApi04() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.CRYPTOMANAGERDECRYPT_API, pathsegments,
                "Query Param Name", "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi05() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.MACHINE_GET_API, pathsegments, "Query Param Name",
                "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi06() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.RETRIEVE_IDENTITY_API, pathsegments,
                "Query Param Name", "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi07() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, stringList, "Query Param Name", "42",
                null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi08() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object",
                restClient.postApi(ApiName.LOST_RID_API, pathsegments, "", "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi09() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object",
                restClient.postApi(ApiName.LOST_RID_API, pathsegments, null, "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi10() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        MediaType mediaType = mock(MediaType.class);

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                mediaType, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi11() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        HttpEntity<Object> requestType = new HttpEntity<>((MultiValueMap<String, String>) new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                null, requestType, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi12() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object",
                restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42", null, null, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi13() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        HttpEntity<Object> httpEntity = (HttpEntity<Object>) mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42",
                null, httpEntity, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

    @Test
    public void testPostApi14() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        assertEquals("Post For Object", restClient.postApi(ApiName.LOST_RID_API, null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi15() throws Exception {
        when(environment.getProperty((String) any())).thenReturn(null);
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        assertNull(restClient.postApi(ApiName.LOST_RID_API, null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
    }

    @Test (expected = MasterDataServiceException.class)
    public void testPostApi16() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenThrow(new MasterDataServiceException("An error occurred", "An error occurred"));
        restClient.postApi(ApiName.LOST_RID_API, null, "Request Type", Object.class);
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testGetApi01() throws Exception {
        ApiName apiName = ApiName.MACHINE_GET_API;
        List<String> pathsegments = List.of("hi","hello","welcome");
        String queryParamName = "name";
        String queryParamValue = "value";
        Class<?> responseType = Class.class;

        restClient.getApi(apiName,pathsegments,queryParamName,queryParamValue,responseType);
    }

    @Test
    public void testGetApi02() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.LOST_RID_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi03() throws Exception {
        when(restTemplate.exchange((String) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any(),
                (Object[]) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(restClient.getApi("https://dev.mosip.net", Object.class));
        verify(restTemplate).exchange((String) any(), (HttpMethod) any(), (HttpEntity<Object>) any(),
                (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testGetApi04() throws Exception {
        when(environment.getProperty((String) any())).thenReturn(null);
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.CRYPTOMANAGERDECRYPT_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
    }

    @Test
    public void testGetApi05() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.RETRIEVE_IDENTITY_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi06() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertNull(restClient.getApi(ApiName.MACHINE_GET_API, stringList, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test (expected = Exception.class)
    public void testGetApi07() throws Exception {
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenThrow(new MasterDataServiceException("An error occurred", "An error occurred"));
        when(restTemplate.exchange((String) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any(),
                (Object[]) any())).thenReturn(responseEntity);
        restClient.getApi("https://dev.mosip.net/", Object.class);
        verify(restTemplate).exchange((String) any(), (HttpMethod) any(), (HttpEntity<Object>) any(),
                (Class<Object>) any(), (Object[]) any());
        verify(responseEntity).getBody();
    }

    @Test
    public void testSetRequestHeader01() {

        Object requestType = mock(Object.class);
        MediaType mediaType = mock(MediaType.class);

        ReflectionTestUtils.invokeMethod(restClient,"setRequestHeader",requestType,mediaType);
    }

    @Test
    public void testSetRequestHeader02() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
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
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }
}