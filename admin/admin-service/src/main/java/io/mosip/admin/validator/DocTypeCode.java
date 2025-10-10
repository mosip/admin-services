package io.mosip.admin.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import io.mosip.admin.bulkdataupload.constant.ErrorConstants;




@Documented
@Constraint(validatedBy = DocTypeCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocTypeCode {

	String message() default ErrorConstants.INVALID_DOC_TYPE_CODE;
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}
