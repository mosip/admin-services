package io.mosip.kernel.masterdata.utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Data
@Component
public class LanguageUtils implements ConstraintValidator<ValidLangCode, String>  {

	private static Predicate<String> emptyCheck = String::isBlank;
	private List<String> mandatoryLanguages;
	private List<String> optionalLanguages;
	private List<String> configuredLanguages;


	@Autowired
	public LanguageUtils(@Value("${mosip.mandatory-languages}") String mandatory,
			@Value("${mosip.optional-languages}") String optional) {
		configuredLanguages = new ArrayList<>();
		mandatoryLanguages = getLanguages(mandatory);
		optionalLanguages = getLanguages(optional);
		configuredLanguages.addAll(mandatoryLanguages);
		configuredLanguages.addAll(optionalLanguages);
	}

	public String getDefaultLanguage() {
		return configuredLanguages.isEmpty() ? "eng" : configuredLanguages.get(0);
	}

	private List<String> getLanguages(String value) {
		if(value == null || value.isBlank())
			return Collections.emptyList();

		return Arrays.asList(value.split(","))
				.stream()
				.filter(emptyCheck.negate())
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (EmptyCheckUtils.isNullEmpty(value) || value.trim().length() > 3) {
			return false;
		}
		return (value.equals("all") || configuredLanguages.contains(value));
	}
}
