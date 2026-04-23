package io.mosip.admin.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import io.mosip.admin.bulkdataupload.constant.ErrorConstants;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DocCatCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocCatCode {

	String message() default ErrorConstants.INVALID_DOC_CAT_CODE;
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
