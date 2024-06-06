package io.mosip.admin.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;



@Documented
@Constraint(validatedBy = LanguageCharacterValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphabeticValidator {
	String message() default "Numbers and Special characters not allowed";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
