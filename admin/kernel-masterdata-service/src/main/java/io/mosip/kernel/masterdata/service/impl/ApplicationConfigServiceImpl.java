package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
import io.mosip.kernel.masterdata.utils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigServiceImpl.class);
	private static String SEMICOLON = ";";
	private static String COLON = ":";
	private static String COMMA = ",";

	@Value("${aplication.configuration.level.version}")
	private String version;
	
	@Value("${mosip.recommended.centers.locCode}")
	private String locationHierarchyLevel;
		
	@Value("${mosip.admin.ui.configs}")
	private String uiConfigs;

	@Autowired
	LanguageUtils languageUtils;

	@Override
	public ApplicationConfigResponseDto getLanguageConfigDetails() {
		ApplicationConfigResponseDto dto=new ApplicationConfigResponseDto();
		
		dto.setMandatoryLanguages(String.join(COMMA, languageUtils.getMandatoryLanguages()));
		dto.setOptionalLanguages(String.join(COMMA, languageUtils.getOptionalLanguages()));
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
		Map<String, String> response = new HashMap<>();
		logger.debug("admin.ui.config : {} ",uiConfigs);
		for (String key : uiConfigs.split(SEMICOLON)) {
			String[] parts = key.split(COLON, 2);
			if (parts.length > 1) {
				response.put(parts[0], parts[1]);
			} else {
				response.put(parts[0], null);
			}
		}
		return response;
	}
}
