package io.mosip.kernel.masterdata.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class SpecialCharacterValidator implements ConstraintValidator<CharacterValidator, String> {

	@Value("${mosip.kernel.masterdata.code.validate.regex:a-z0-9}")
	private String allowedCharacters;

	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
        
		 allowedCharacters="[^"+allowedCharacters+"]";
		 Pattern p = Pattern.compile(""+allowedCharacters+"", Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(value.trim());
		return !(m.find());

	}

}
