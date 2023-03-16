package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.constant.ApiName;
import io.mosip.kernel.masterdata.utils.RestClient;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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

        ApiName apiName = ApiName.PACKET_PAUSE_API;
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

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi03() throws Exception {
        when(environment.getProperty((String) any())).thenReturn(null);
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name", "42", null,
                "Request Type", Object.class));
        verify(environment).getProperty((String) any());
    }

    @Test
    public void testPostApi04() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_PAUSE_API, pathsegments, "Query Param Name",
                "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi05() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, stringList, "Query Param Name",
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

        assertEquals("Post For Object",
                restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "", "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testPostApi07() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object",
                restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, null, "42", null, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testGetApi01() throws Exception {
        ApiName apiName = ApiName.PACKET_PAUSE_API;
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

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi03() throws Exception {
        when(environment.getProperty((String) any())).thenReturn(null);
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
    }

    @Test
    public void testGetApi04() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.PACKET_PAUSE_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi05() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, stringList, "Query Param Name", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi06() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, "", "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
    }

    @Test
    public void testGetApi07() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, null, "42", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).exchange((URI) any(), (HttpMethod) any(), (HttpEntity<Object>) any(), (Class<Object>) any());
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

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", mediaType, "Request Type", Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testSetRequestHeader03() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        HttpEntity<Object> requestType = new HttpEntity<>((MultiValueMap<String, String>) new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, requestType, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testSetRequestHeader04() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, null, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
    }

    @Test
    public void testSetRequestHeader05() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        HttpEntity<Object> httpEntity = (HttpEntity<Object>) mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, httpEntity, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

    @Test
    public void testSetRequestHeader06() throws Exception {
        when(environment.getProperty((String) any())).thenReturn("Property");
        when(restTemplate.postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("https://dev.mosip.net/", "https://dev.mosip.net/");
        HttpEntity<Object> httpEntity = (HttpEntity<Object>) mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(httpHeaders);

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, httpEntity, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

    @Test
    public void testSetRequestHeader07() throws Exception {
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

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", mediaType, httpEntity, Object.class));
        verify(environment).getProperty((String) any());
        verify(restTemplate).postForObject((String) any(), (Object) any(), (Class<Object>) any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

}

