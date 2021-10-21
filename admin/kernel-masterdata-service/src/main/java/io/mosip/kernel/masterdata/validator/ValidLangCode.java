package io.mosip.kernel.masterdata.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation used to validate field for valid language code as per ISO:639-3.
 * 
 * @author Bal Vikash Sharma
 */
@Documented
@Constraint(validatedBy = LanguageCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLangCode {

	String message() default "Language code not supported";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
