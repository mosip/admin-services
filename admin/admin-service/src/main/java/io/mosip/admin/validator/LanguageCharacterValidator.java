package io.mosip.admin.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LanguageCharacterValidator  implements ConstraintValidator<AlphabeticValidator, String>  {
	@Value("${mosip.kernel.masterdata.name.validate.regex:[^a-zA-Z]}")
	private String allowedCharactersRegex ;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(allowedCharactersRegex==null) {
			/*         Note: An additional validation was getting triggered by doInvoke() method of 
			 * 		 RepositoryListItemWriter class with allowedCharactersRegex equal to null
			 *               which is not desired. This if clause is being used to escape that additional 
			 *               validation step.
			 */
			return true;
		}
		if (null != value && !value.isEmpty()) {
			Pattern p = Pattern.compile(allowedCharactersRegex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(value.trim());
			return !(m.find());
		}
		return true;
	}
}
