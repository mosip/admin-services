package io.mosip.kernel.masterdata.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
@Service
public interface ApplicationConfigService {

	public ApplicationConfigResponseDto getLanguageConfigDetails();
	
	public Map<String,String> getConfigValues();

}
