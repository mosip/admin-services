package io.mosip.kernel.masterdata.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author GovindarajV
 *
 * This annotation used for center-type code validation
 */
@Documented
@Constraint(validatedBy = CenterTypeCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CenterTypeCode {

	/*
	 * or
	 */
	String OR_MSG = " OR ";
	/*
	 * Code should be start and end with alphanumeric
	 */
	String ALPHANUMERIC_MSG = "Code should start and end with an alphanumeric character";
	/*
	 * Hypen or underscore should accept between the alphanumeric
	 */
	String HYPER_UNDERSCORE_MSG = "Code should not start or end with a hyphen or an underscore";
	/*
	 * Hypen or underscore should not accept consecutively
	 */
	String HYPER_UNDERSCORE_NOT_MSG = "Code should not contain consecutive hyphen or underscore";
	/*
	 * code length message
	 */
	String SIZE_MSG = "{javax.validation.constraints.Size.message}";
	
	/*
	 * Registration center type code having below rules of message
	 */
	String message() default ALPHANUMERIC_MSG + OR_MSG + HYPER_UNDERSCORE_MSG + OR_MSG + HYPER_UNDERSCORE_NOT_MSG + OR_MSG + SIZE_MSG;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	/**
	 * Min.
	 *
	 * @return the int
	 */
	int min() default 0;

	/**
	 * Max.
	 *
	 * @return the int
	 */
	int max() default Integer.MAX_VALUE;
}
