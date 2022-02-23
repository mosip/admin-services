package io.mosip.kernel.masterdata.service.impl;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
import io.mosip.kernel.masterdata.utils.LanguageUtils;
@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigServiceImpl.class);

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
		
		dto.setMandatoryLanguages(String.join(",", languageUtils.getMandatoryLanguages()));
		dto.setOptionalLanguages(String.join(",", languageUtils.getOptionalLanguages()));
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
		logger.info("admin.ui.config : {} ",uiConfigs);
		String[] arrayOfKeys = uiConfigs.split(";");
		for (String key : arrayOfKeys) {
			if (key.split(":").length > 1) {
				response.put(key.split(":")[0], key.split(":")[1]);
			} else {
				response.put(key.split(":")[0], null);
			}
		}
		return response;
	}
}
