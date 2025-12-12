package io.mosip.kernel.masterdata.test.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.masterdata.validator.FieldValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FieldValidatorTest {

    @Mock
    private JsonNode jsonNode;

    private FieldValidator validator;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        validator = new FieldValidator();
        mapper = new ObjectMapper();
    }

    @Test
    public void testValidatorWithValidInputSuccess() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("valid_value"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("valid_code"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithEmptyValueOrCodeSuccess() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree(""));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("valid_code"));

        assertFalse(validator.isValid(jsonNode, null));

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("valid_value"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree(""));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithInvalidCharactersCodeSuccess() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("valid_value"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("invalid code!"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithInvalidCharactersValueSuccess() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("invalid value@"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("valid_code"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithNoAllowedCharactersReturnsTrue() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("éé€€"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("éé€€"));
        assertTrue(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithValueNullReturnsFalse() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        final JsonNode valueJsonNode = mock(JsonNode.class);
        when(jsonNode.get("value")).thenReturn(valueJsonNode);
        when(valueJsonNode.asText()).thenReturn(null);
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("éé€€"));
        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithValueEmptyReturnsFalse() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        final JsonNode valueJsonNode = mock(JsonNode.class);
        when(jsonNode.get("value")).thenReturn(valueJsonNode);
        when(valueJsonNode.asText()).thenReturn("");
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("éé€€"));
        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test(expected = NullPointerException.class)
    public void testValidatorMissingCodeNodeThrowsNPE() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("some_value"));
        when(jsonNode.get("code")).thenReturn(null);
        validator.isValid(jsonNode, null);
    }

    @Test
    public void testValidatorWithCodeNullReturnsFalse() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("some_value"));
        final JsonNode codeJsonNode = mock(JsonNode.class);
        when(jsonNode.get("code")).thenReturn(codeJsonNode);
        when(codeJsonNode.asText()).thenReturn(null);
        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidatorWithCodeEmptyReturnsFalse() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("some_value"));
        final JsonNode codeJsonNode = mock(JsonNode.class);
        when(jsonNode.get("code")).thenReturn(codeJsonNode);
        when(codeJsonNode.asText()).thenReturn("");
        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test(expected = NullPointerException.class)
    public void testValidatorMissingValueNodeThrowsNPE() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(null);
        validator.isValid(jsonNode, null);
    }

}
