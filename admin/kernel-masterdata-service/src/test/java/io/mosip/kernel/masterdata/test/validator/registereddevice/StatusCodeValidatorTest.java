package io.mosip.kernel.masterdata.test.validator.registereddevice;

import io.mosip.kernel.masterdata.validator.registereddevice.StatusCodeValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatusCodeValidatorTest {

    private final StatusCodeValidator validator = new StatusCodeValidator();

    @Test
    void testNullValue() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void testEmptyValue() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    void testBlankValue() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    void testValidRegistered() {
        assertTrue(validator.isValid("registered", null));
    }

    @Test
    void testValidRegisteredDifferentCase() {
        assertTrue(validator.isValid("REGISTERED", null));
    }

    @Test
    void testValidRetired() {
        assertTrue(validator.isValid("retired", null));
    }

    @Test
    void testValidRevoked() {
        assertTrue(validator.isValid("revoked", null));
    }

    @Test
    void testInvalidStatusNotMatching() {
        assertFalse(validator.isValid("UNKNOWN", null));
    }

}