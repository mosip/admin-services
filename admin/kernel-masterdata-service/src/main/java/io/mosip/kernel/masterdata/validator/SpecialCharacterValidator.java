package io.mosip.kernel.masterdata.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class SpecialCharacterValidator implements ConstraintValidator<CharacterValidator, String> {

	@Value("${mosip.kernel.masterdata.code.validate.regex}")
	private String allowedCharactersRegex;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (null != value && !value.isEmpty()) {
			Pattern p = Pattern.compile(allowedCharactersRegex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(value.trim());
			return !(m.find());
		}
		return true;
	}

}
