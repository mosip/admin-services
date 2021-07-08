package io.mosip.kernel.masterdata.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.dto.getresponse.ApplicationConfigResponseDto;
import io.mosip.kernel.masterdata.service.ApplicationConfigService;
import io.swagger.annotations.Api;

@Api(tags = { "Application Configs" })
@RestController
public class ApplicationConfigController {

	@Autowired
	private ApplicationConfigService applicationService;
	
	@Deprecated
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetapplicationconfigs())")
	@GetMapping("/applicationconfigs")
	public ResponseWrapper<ApplicationConfigResponseDto> getAllApplication() {
		ResponseWrapper<ApplicationConfigResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(applicationService.getLanguageConfigDetails());
		return responseWrapper;
	}
	
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetconfigs())")
	@GetMapping("/configs")
	public ResponseWrapper<Map<String,String>> getConfigValues() {
		ResponseWrapper<Map<String,String>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(applicationService.getConfigValues());
		return responseWrapper;
	}
}
