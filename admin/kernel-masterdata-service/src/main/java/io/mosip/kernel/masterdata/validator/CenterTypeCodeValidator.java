package io.mosip.kernel.masterdata.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

/**
 * @author GOVINDARAJ VELU
 *
 */
@Data
public class CenterTypeCodeValidator implements ConstraintValidator<CenterTypeCode, String> {

	@Value("${mosip.centertypecode.validate.regex}")
	private String centerTypeCodeValidate;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Pattern CENTERTYPECODE_PATTERN = Pattern.compile(centerTypeCodeValidate);
		Matcher matcher = CENTERTYPECODE_PATTERN.matcher(value);
		return matcher.matches();
	}

}
