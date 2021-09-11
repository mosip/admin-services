package io.mosip.kernel.masterdata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LanguageUtils {

	private List<String> mandatoryLang;
	private List<String> optionalLang;
	private List<String> configuredLanguages;
	private List<String> supportedLangugaes;


	@Autowired
	public LanguageUtils(@Value("${mosip.mandatory-languages:NOTSET}") String mandatory,
			@Value("${mosip.optional-languages:NOTSET}") String optional,
			@Value("${mosip.supported-languages:NOTSET}") String supported) {

		if ("NOTSET".equals(optional)) {
			this.optionalLang = Collections.emptyList();
		} else {
			this.optionalLang = Arrays.asList(optional.split(","));
		}

		if ("NOTSET".equals(optional)) {
			this.mandatoryLang = Collections.emptyList();
		} else {
			this.mandatoryLang = Arrays.asList(mandatory.split(","));
		}


		if ("NOTSET".equals(supported)) {
			this.supportedLangugaes = Collections.emptyList();
		} else {
			this.supportedLangugaes = Arrays.asList(supported.split(","));
		}
		configuredLanguages = new ArrayList<>();
		configuredLanguages.addAll(mandatoryLang);
		configuredLanguages.addAll(optionalLang);
	}

}
