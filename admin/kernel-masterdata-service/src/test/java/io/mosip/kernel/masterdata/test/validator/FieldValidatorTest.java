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

}
