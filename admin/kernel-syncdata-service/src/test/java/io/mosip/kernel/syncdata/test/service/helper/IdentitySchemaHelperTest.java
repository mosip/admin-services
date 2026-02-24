package io.mosip.kernel.syncdata.test.service.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IdentitySchemaHelperTest {

    @InjectMocks
    private IdentitySchemaHelper identitySchemaHelper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set private @Value field via ReflectionTestUtils
        ReflectionTestUtils.setField(identitySchemaHelper, "idSchemaUrl", "http://dummy-schema-url");
    }

    @Test
    public void testGetLatestIdentitySchema_internalServerError() throws Exception {
        // Mock RestTemplate to throw an exception to trigger catch block
        when(restTemplate.getForEntity(any(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        // Call the method and assert that SyncDataServiceException is thrown
        SyncDataServiceException exception = assertThrows(SyncDataServiceException.class, () ->
                identitySchemaHelper.getLatestIdentitySchema(LocalDateTime.now(), 1.0, "registration-client", "schema"));

        // Verify error code is SCHEMA_FETCH_FAILED
        assertEquals(MasterDataErrorCode.SCHEMA_FETCH_FAILED.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetLatestIdentitySchema_validationError() throws Exception {
        // Mock response with errors
        ResponseWrapper<ObjectNode> wrapper = new ResponseWrapper<>();
        ReflectionTestUtils.setField(wrapper, "errors", Collections.singletonList("Some validation error"));

        ResponseEntity<String> responseEntity = ResponseEntity.ok("{}");
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(eq("{}"), any(TypeReference.class))).thenReturn(wrapper);

        // Expect SyncInvalidArgumentException wrapped in SyncDataServiceException
        assertThrows(SyncDataServiceException.class, () ->
                identitySchemaHelper.getLatestIdentitySchema(LocalDateTime.now(), 1.0, "domain", "type"));
    }

    @Test
    public void testGetLatestIdentitySchema_backwardCompatibilityRetain() throws Exception {
        // Prepare mock ObjectNode
        ObjectNode responseNode = mock(ObjectNode.class);
        when(responseNode.retain(any(List.class))).thenReturn(responseNode);

        // Prepare ResponseWrapper with mock response
        ResponseWrapper<ObjectNode> wrapper = new ResponseWrapper<>();
        ReflectionTestUtils.setField(wrapper, "response", responseNode);

        // Mock RestTemplate and ObjectMapper
        ResponseEntity<String> responseEntity = ResponseEntity.ok("{}");
        when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(responseEntity);
        when(objectMapper.readValue(eq("{}"), any(TypeReference.class))).thenReturn(wrapper);

        // Call the method with type = "schema" and domain = "registration-client"
        ObjectNode result = (ObjectNode) identitySchemaHelper.getLatestIdentitySchema(
                LocalDateTime.now(), 1.0, "registration-client", "schema");

        // Verify retain() was called with the expected properties
        List<String> expectedProperties = Arrays.asList("schema", "schemaJson", "id", "idVersion", "effectiveFrom");
        verify(responseNode).retain(expectedProperties);

        // Assert the returned node is the same mock
        assertEquals(responseNode, result);
    }
}