package io.mosip.kernel.masterdata.test.validator;

import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.validator.registereddevice.TypeValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TypeValidatorTest {

    @Test
    public void testIsValid_EmptyType() {
        TypeValidator validator = new TypeValidator();
        String emptyType = "";

        boolean isValid = validator.isValid(emptyType, null);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_NullType() {
        TypeValidator validator = new TypeValidator();

        boolean isValid = validator.isValid(null, null);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_InvalidType() {
        TypeValidator validator = new TypeValidator();
        String invalidType = "INVALID_TYPE";

        boolean isValid = validator.isValid(invalidType, null);

        assertFalse(isValid);
    }

    @Test(expected = RestClientException.class)
    public void testIsValid_RestClientException() throws RequestException {
        TypeValidator validator = mock(TypeValidator.class);
        String type = "anyType";
        when(validator.isValid(type, null)).thenThrow(new RestClientException("RestClientException"));

        validator.isValid(type, null);
    }

}
