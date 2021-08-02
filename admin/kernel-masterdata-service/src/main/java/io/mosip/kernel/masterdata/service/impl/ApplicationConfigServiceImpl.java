package io.mosip.kernel.masterdata.service.impl;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

	@Value("${mosip.mandatory-languages}")
	private String mandatoryLang;

	@Value("${mosip.optional-languages}")
	private String optionalLang;
	
	@Value("${aplication.configuration.level.version}")
	private String version;
	
	@Value("${mosip.recommended.centers.locCode}")
	private String locationHierarchyLevel;
	
	@Value("${mosip.supported-languages}")
	private String supportedLanguages;
	
	@Value("${mosip.admin.ui.configs}")
	private String uiConfigs;
	
	@Override
	public ApplicationConfigResponseDto getLanguageConfigDetails() {
		ApplicationConfigResponseDto dto=new ApplicationConfigResponseDto();
		
		dto.setMandatoryLanguages(mandatoryLang);
		dto.setOptionalLanguages(optionalLang);
		dto.setVersion(version);
		dto.setLocationHierarchyLevel(locationHierarchyLevel);
		return dto;
	}

	/**
	 * This method will spilt the configuration value and sends as a response in desired format.
	 * uiConfigs will look like primaryLangCode:eng,secondaryLang:ara,version:1.1.2
	 * 
	 */
	@Override
	public Map<String,String> getConfigValues(){
		Map<String, String> response = new HashMap<String, String>();
		String[] arrayOfKeys = uiConfigs.split(";");
		for (String key : arrayOfKeys) {
			if (key.split(":").length > 1) {
				response.put(key.split(":")[0], key.split(":")[1]);
			}
			response.put(key.split(":")[0], null);
		}
		return response;
	}
}
