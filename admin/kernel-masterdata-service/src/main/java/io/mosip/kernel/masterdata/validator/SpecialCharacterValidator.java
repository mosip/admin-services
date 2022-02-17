package io.mosip.kernel.masterdata.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class SpecialCharacterValidator implements ConstraintValidator<CharacterValidator, String> {

	@Value("${mosip.kernel.masterdata.code.validate.regex:[^a-z0-9]}")
	private String allowedCharactersRegex;
	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		Pattern p = Pattern.compile(allowedCharactersRegex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value.trim());
		return !(m.find());

	}

}
