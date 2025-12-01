package io.mosip.kernel.masterdata.test.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.mosip.kernel.masterdata.validator.CustomIntegerDeserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CustomIntegerDeserializerTest {

    private ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new CustomIntegerDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    @Test
    void testValidInteger() throws Exception {
        ObjectMapper mapper = buildMapper();
        String json = "123";
        Object result = mapper.readValue(json, Object.class);
        assertTrue(result instanceof Integer);
        assertEquals(123, result);
    }

    @Test
    void testInvalidFloat() {
        ObjectMapper mapper = buildMapper();
        String json = "123.45";
        Exception ex = assertThrows(IOException.class, () -> {
            mapper.readValue(json, Object.class);
        });
        assertTrue(ex.getMessage().contains("Cannot coerce"));
    }

    @Test
    void testNullValue() throws Exception {
        ObjectMapper mapper = buildMapper();
        String json = "null";
        Object result = mapper.readValue(json, Object.class);
        assertNull(result);
    }

    @Test
    void testInvalidString() {
        ObjectMapper mapper = buildMapper();
        String json = "\"abc\"";
        assertThrows(IOException.class, () -> {
            mapper.readValue(json, Object.class);
        });
    }

}