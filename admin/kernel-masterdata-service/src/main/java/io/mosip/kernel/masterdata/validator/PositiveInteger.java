package io.mosip.kernel.masterdata.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.Data;

@Data
public class PositiveInteger implements ConstraintValidator<PositiveValue, Integer> {

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value >= 0 && value % 1==0)
			return true;
		else
			return false;
	}

}
