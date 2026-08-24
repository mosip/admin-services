package io.mosip.kernel.masterdata.test.validator.registereddevice;

import io.mosip.kernel.masterdata.validator.registereddevice.StatusCodeValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusCodeValueTest {

    @Test
    void testToStringRegistered() {
        assertEquals("registered", StatusCodeValue.REGISTERED.toString());
    }

    @Test
    void testToStringRetired() {
        assertEquals("retired", StatusCodeValue.RETIRED.toString());
    }

    @Test
    void testToStringRevoked() {
        assertEquals("revoked", StatusCodeValue.REVOKED.toString());
    }

    @Test
    void testValueOfRegistered() {
        StatusCodeValue value = StatusCodeValue.valueOf("REGISTERED");
        assertSame(StatusCodeValue.REGISTERED, value);
    }

    @Test
    void testValueOfRetired() {
        StatusCodeValue value = StatusCodeValue.valueOf("RETIRED");
        assertSame(StatusCodeValue.RETIRED, value);
    }

    @Test
    void testValueOfRevoked() {
        StatusCodeValue value = StatusCodeValue.valueOf("REVOKED");
        assertSame(StatusCodeValue.REVOKED, value);
    }

    @Test
    void testValuesContainsAll() {
        StatusCodeValue[] values = StatusCodeValue.values();
        assertEquals(3, values.length);
        assertArrayEquals(
                new StatusCodeValue[] {
                        StatusCodeValue.REGISTERED,
                        StatusCodeValue.RETIRED,
                        StatusCodeValue.REVOKED
                },
                values
        );
    }

}