package io.mosip.admin.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

public class LanguageCharacterValidatorTest {

    private LanguageCharacterValidator validator;

    @Before
    public void setUp() {
        validator = new LanguageCharacterValidator();
    }

    @Test
    public void isValid_shouldReturnTrue_whenAllowedCharactersRegexIsNull() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", null);
        // when
        boolean result = validator.isValid("AnyValue123", (ConstraintValidatorContext) null);
        // then
        assertTrue(result);
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsNull() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", "[^a-zA-Z]");
        // when
        boolean result = validator.isValid(null, (ConstraintValidatorContext) null);
        // then
        assertTrue(result);
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueIsEmpty() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", "[^a-zA-Z]");
        // when
        boolean result = validator.isValid("", (ConstraintValidatorContext) null);
        // then
        assertTrue(result);
    }

    @Test
    public void isValid_shouldReturnTrue_whenValueContainsOnlyAllowedCharacters() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", "[^a-zA-Z]");
        // when
        boolean result = validator.isValid("AlphaBeta", (ConstraintValidatorContext) null);
        // then
        assertTrue(result);
    }

    @Test
    public void isValid_shouldReturnFalse_whenValueContainsNotAllowedCharacters() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", "[^a-zA-Z]");
        // when
        boolean result = validator.isValid("Alpha1", (ConstraintValidatorContext) null);
        // then
        assertFalse(result);
    }

    @Test
    public void isValid_shouldTrimValueBeforeValidation() {
        // given
        ReflectionTestUtils.setField(validator, "allowedCharactersRegex", "[^a-zA-Z]");
        // when
        boolean result = validator.isValid("  Alpha  ", (ConstraintValidatorContext) null);
        // then
        assertTrue(result);
    }

}