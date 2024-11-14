package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.masterdata.constant.ApiName;
import io.mosip.kernel.masterdata.utils.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
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

        ApiName apiName = ApiName.PACKET_PAUSE_API;
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
    public void postApi_withProperty_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }


    @Test
    public void postApi_withPacketResumeApi_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, stringList, "Query Param Name",
                "42", null, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void getApi_withValidInput_thenSuccess() throws Exception {
        ApiName apiName = ApiName.PACKET_PAUSE_API;
        List<String> pathsegments = List.of("hi","hello","welcome");
        String queryParamName = "name";
        String queryParamValue = "value";
        Class<?> responseType = Class.class;

        restClient.getApi(apiName,pathsegments,queryParamName,queryParamValue,responseType);
        assertNotNull(pathsegments);
    }

    @Test
    public void getApi_withProperty_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name", "42", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any());
    }

    @Test
    public void getApi_withoutPathsegments_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("RestClientTest");

        assertNull(restClient.getApi(ApiName.PACKET_RESUME_API, stringList, "Query Param Name", "42", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).exchange(any(), any(), any(), (Class<Object>) any());
    }

    @Test (expected = Exception.class)
    public void getApi_withInvalidProperty_returnErrorResponse() throws Exception {
        when(environment.getProperty(any())).thenReturn("dev@1:2:3{Mosip}");
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ArrayList<String> pathsegments = new ArrayList<>();
        restClient.getApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name", "42", Object.class);
        verify(environment).getProperty(any());
    }

    @Test
    public void setRequestHeader_withProperty_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        MediaType mediaType = mock(MediaType.class);

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", mediaType, "Request Type", Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void setRequestHeader_withRequestType_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();
        HttpEntity<Object> requestType = new HttpEntity<>(new HttpHeaders());

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, requestType, Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }

    @Test
    public void setRequestHeader_withPathsegments_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, null, Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
    }


    @Test
    public void setRequestHeader_withHttpURL_thenSuccess() throws Exception {
        when(environment.getProperty(any())).thenReturn("Property");
        when(restTemplate.postForObject(any(), any(), any(), (Object[]) any()))
                .thenReturn("Post For Object");
        ArrayList<String> pathsegments = new ArrayList<>();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("https://dev.mosip.net/", "https://dev.mosip.net/");
        HttpEntity<Object> httpEntity = mock(HttpEntity.class);
        when(httpEntity.getBody()).thenThrow(new ClassCastException());
        when(httpEntity.getHeaders()).thenReturn(httpHeaders);

        assertEquals("Post For Object", restClient.postApi(ApiName.PACKET_RESUME_API, pathsegments, "Query Param Name",
                "42", null, httpEntity, Object.class));
        verify(environment).getProperty(any());
        verify(restTemplate).postForObject(any(), any(), any(), (Object[]) any());
        verify(httpEntity).getBody();
        verify(httpEntity).getHeaders();
    }

}