package io.mosip.kernel.masterdata.service.impl;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {
	@Value("${mosip.primary-language}")
	private String primaryLangCode;

	@Value("${mosip.secondary-language}")
	private String secondaryLang;
	
	@Value("${aplication.configuration.level.version}")
	private String version;
	
	@Value("${mosip.admin.ui.configs}")
	private String uiConfigs;
	
	@Override
	public ApplicationConfigResponseDto getLanguageConfigDetails() {
		ApplicationConfigResponseDto dto=new ApplicationConfigResponseDto();
		
		dto.setPrimaryLangCode(primaryLangCode);
		dto.setSecondaryLangCode(secondaryLang);
		dto.setVersion(version);
		return dto;
	}

	@Override
	public Map<String,String> getConfigValues(){
		Map<String, String> response = new HashMap<String, String>();
		String[] arrayOfKeys = uiConfigs.split(",");
		for (String key : arrayOfKeys) {
			response.put(key.split(":")[0], key.split(":")[1]);
		}
		return response;
	}
}
