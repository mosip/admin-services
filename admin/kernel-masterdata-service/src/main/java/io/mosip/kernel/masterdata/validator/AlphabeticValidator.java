package io.mosip.kernel.masterdata.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LanguageCharacterValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphabeticValidator {
	
	String message() default "Numbers and Special characters not allowed";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
