package io.mosip.kernel.syncdata.test.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class IdentitySchemaHelperTest {

    @InjectMocks
    private IdentitySchemaHelper identitySchemaHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(
                identitySchemaHelper,
                "idSchemaUrl",
                "http://dummy-url/idschema"
        );
    }

    @Test
    public void testGetLatestIdentitySchemaNormalNoErrors() throws Exception {
        // given
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("field", "value");
        ResponseWrapper<ObjectNode> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(node);
        wrapper.setErrors(null);
        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(wrapper);
        // when
        JsonNode result = identitySchemaHelper.getLatestIdentitySchema(
                LocalDateTime.now(),
                1.2,
                "some-domain",
                "some-type"
        );
        // then
        assertNotNull(result);
        assertEquals("value", result.get("field").asText());
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetLatestIdentitySchemaWithErrorsThrowsSyncDataServiceException() throws Exception {
        // given
        ObjectNode node = new ObjectMapper().createObjectNode();
        ResponseWrapper<ObjectNode> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(node);
        final ServiceError serviceError = new ServiceError("TEST", "TEST");
        wrapper.setErrors(Collections.singletonList(serviceError));
        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(wrapper);
        // when / then
        identitySchemaHelper.getLatestIdentitySchema(
                LocalDateTime.now(),
                1.2,
                "some-domain",
                "schema"
        );
    }

    @Test
    public void testGetLatestIdentitySchemaRegistrationClientSchemaRetainsSpecificFields() throws Exception {
        // given
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("schema", "schemaVal");
        node.put("schemaJson", "{}");
        node.put("id", "idVal");
        node.put("idVersion", "1.0");
        node.put("effectiveFrom", "2024-01-01T00:00:00");
        node.put("extra", "shouldBeRemoved");
        ResponseWrapper<ObjectNode> wrapper = new ResponseWrapper<>();
        wrapper.setResponse(node);
        wrapper.setErrors(null);
        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{}"));
        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(wrapper);
        // when
        JsonNode result = identitySchemaHelper.getLatestIdentitySchema(
                LocalDateTime.now(), 1.2, "registration-client", "schema");
        // then
        assertTrue(result.has("schema"));
        assertTrue(result.has("schemaJson"));
        assertTrue(result.has("id"));
        assertTrue(result.has("idVersion"));
        assertTrue(result.has("effectiveFrom"));
        assertFalse(result.has("extra"));
    }

    @Test(expected = SyncDataServiceException.class)
    public void testGetLatestIdentitySchemaOnExceptionThrowsSyncDataServiceException() throws Exception {
        // given
        when(restTemplate.getForEntity(any(), eq(String.class))).thenThrow(new RuntimeException("boom"));
        // when / then
        identitySchemaHelper.getLatestIdentitySchema(
                LocalDateTime.now(),
                1.2,
                "some-domain",
                "some-type"
        );
    }

}