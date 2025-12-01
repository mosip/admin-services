package io.mosip.kernel.masterdata.test.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
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
    public void testValidator_withValidInput_Success() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("valid_value"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("valid_code"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidator_withEmptyValueOrCode_Success() {
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
    public void testValidator_withInvalidCharactersCode_Success() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("valid_value"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("invalid code!"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidator_withInvalidCharactersValue_Success() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);

        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("invalid value@"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("valid_code"));

        assertFalse(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidator_withNoAllowedCharacters_ReturnsTrue() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("éé€€"));
        when(jsonNode.get("code")).thenReturn(mapper.valueToTree("éé€€"));
        assertTrue(validator.isValid(jsonNode, null));
    }

    @Test
    public void testValidator_withValueNull_ReturnsFalse() {
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
    public void testValidator_withValueEmpty_ReturnsFalse() {
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
    public void testValidator_missingCodeNode_ThrowsNPE() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(mapper.valueToTree("some_value"));
        when(jsonNode.get("code")).thenReturn(null);
        validator.isValid(jsonNode, null);
    }

    @Test
    public void testValidator_withCodeNull_ReturnsFalse() {
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
    public void testValidator_withCodeEmpty_ReturnsFalse() {
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
    public void testValidator_missingValueNode_ThrowsNPE() {
        validator.setAllowedCodeCharactersRegex("[a-zA-Z0-9_]");
        validator.setAllowedValueCharactersRegex("[a-zA-Z0-9.,!@#$%^&*()]");
        validator.initialize(null);
        when(jsonNode.get("value")).thenReturn(null);
        validator.isValid(jsonNode, null);
    }

}
