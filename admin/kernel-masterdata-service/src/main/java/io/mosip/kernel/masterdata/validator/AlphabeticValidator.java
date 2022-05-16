package io.mosip.kernel.masterdata.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


//TODO: - rename this class with a more relevant name

@Documented
@Constraint(validatedBy = LanguageCharacterValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphabeticValidator {
	
	String message() default "Numbers and Special characters not allowed";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
