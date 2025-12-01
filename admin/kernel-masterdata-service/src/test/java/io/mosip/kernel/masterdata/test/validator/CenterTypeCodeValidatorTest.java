package io.mosip.kernel.masterdata.test.validator;

import io.mosip.kernel.masterdata.validator.CenterTypeCodeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CenterTypeCodeValidatorTest {

    private CenterTypeCodeValidator validator;

    @BeforeEach
    void setup() {
        validator = new CenterTypeCodeValidator();
        // Simulation de l’injection @Value
        validator.setCenterTypeCodeValidate("^[A-Z]{3}[0-9]{2}$");
        // Exemple de regex : 3 lettres + 2 chiffres
    }

    @Test
    void testValidValue() {
        boolean result = validator.isValid("ABC12", null);
        assertTrue(result);
    }

    @Test
    void testInvalidValue() {
        boolean result = validator.isValid("A12", null);
        assertFalse(result);
    }

    @Test
    void testLowercaseInvalid() {
        boolean result = validator.isValid("abc12", null);
        assertFalse(result);
    }

    @Test
    void testNullValueThrowsException() {
        assertThrows(NullPointerException.class, () -> validator.isValid(null, null));
    }

    @Test
    void testEmptyValue() {
        boolean result = validator.isValid("", null);
        assertFalse(result);
    }

    @Test
    void testRegexChange() {
        validator.setCenterTypeCodeValidate("^[0-9]{4}$");
        assertTrue(validator.isValid("1234", null));
        assertFalse(validator.isValid("ABC1", null));
    }

}