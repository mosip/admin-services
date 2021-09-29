package io.mosip.kernel.masterdata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.authmanager.authadapter.model.AuthUserDetails;
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
	
	public static String getLangCode() {
		try {
			String token = ((AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getToken();
			String[] chunks = token.split("\\.");
			Base64.Decoder decoder = Base64.getDecoder();
			String payload = new String(decoder.decode(chunks[1]));
			JSONObject json = new JSONObject(payload);
			return json.get("locale").toString();
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;
	}

}
