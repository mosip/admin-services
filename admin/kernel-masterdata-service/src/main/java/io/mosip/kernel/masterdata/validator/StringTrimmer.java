package io.mosip.kernel.masterdata.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.Data;

/**
 * The Class StringTrimmer.
 */

/**
 * Instantiates a new string trimmer.
 */
@Data
public class StringTrimmer implements ConstraintValidator<StringFormatter, String> {

	/** The min. */
	private int min = 0;

	/** The max. */
	private int max = Integer.MAX_VALUE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.
	 * Annotation)
	 */
	@Override
	public void initialize(StringFormatter constraintAnnotation) {
		max = constraintAnnotation.max();
		min = constraintAnnotation.min();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
	 * jakarta.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {

		return !(arg0 != null && (arg0.trim().length() == 0 || arg0.trim().isEmpty() || arg0.trim().length() < min
				|| arg0.trim().length() > max));
	}

}
