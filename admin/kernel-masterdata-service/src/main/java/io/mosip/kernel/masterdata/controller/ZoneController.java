package io.mosip.kernel.masterdata.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.getresponse.ZoneNameResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.ZoneExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.Api;

/**
 * Controller to handle api request for the zones
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@RestController
@RequestMapping("/zones")
@Validated
@Api(tags = { "Zone" })
public class ZoneController {

	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private AuditUtil auditUtil;

	/**
	 * api to fetch the logged-in user zone hierarchy
	 * 
	 * @param langCode input language code
	 * @return {@link List} of {@link ZoneExtnDto}
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzoneshierarchylangCode())")
	@GetMapping("/hierarchy/{langCode}")
	public ResponseWrapper<List<ZoneExtnDto>> getZoneHierarchy(
			@PathVariable("langCode") @ValidLangCode(message = "Language Code is Invalid") String langCode) {
		ResponseWrapper<List<ZoneExtnDto>> response = new ResponseWrapper<>();
		response.setResponse(zoneService.getUserZoneHierarchy(langCode));
		return response;
	}

	/**
	 * api to fetch the logged-in user zone hierarchy leaf zones
	 * 
	 * @param langCode input language code
	 * @return {@link List} of {@link ZoneExtnDto}
	 */
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzonesleafslangCode())")
	@GetMapping("/leafs/{langCode}")
	public ResponseWrapper<List<ZoneExtnDto>> getLeafZones(
			@PathVariable("langCode") @ValidLangCode(message = "Language Code is Invalid") String langCode) {
		ResponseWrapper<List<ZoneExtnDto>> response = new ResponseWrapper<>();
		response.setResponse(zoneService.getUserLeafZone(langCode));
		return response;
	}
	
	/**
	 * api to fetch the logged-in user zone hierarchy leaf zones
	 * 
	 * @param langCode input language code
	 * @return {@link List} of {@link ZoneExtnDto}
	 */
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzonesleafslangCode())")
/*	@GetMapping("/leafs/{zoneCode}/{langCode}")
	public ResponseWrapper<List<ZoneExtnDto>> getLeafZones(
			@PathVariable("langCode") @ValidLangCode(message = "Language Code is Invalid") String langCode) {
		ResponseWrapper<List<ZoneExtnDto>> response = new ResponseWrapper<>();
		response.setResponse(zoneService.getUserLeafZone(langCode));
		return response;
	}*/
	/**
	 * api to fetch the logged-in user zone hierarchy leaf zones
	 * 
	 * @param langCode input language code
	 * @return {@link List} of {@link ZoneExtnDto}
	 */
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzonesleafslangCode())")
	@GetMapping("/subzone/{langCode}")
	public ResponseWrapper<List<ZoneExtnDto>> getSubZones(
			@PathVariable("langCode") @ValidLangCode(message = "Language Code is Invalid") String langCode) {
		ResponseWrapper<List<ZoneExtnDto>> response = new ResponseWrapper<>();
		response.setResponse(zoneService.getSubZones(langCode));
		return response;
	}
	
	@GetMapping("/leafzones/{langCode}")
	public ResponseWrapper<List<ZoneExtnDto>> getLeafZonesBasedOnZoneCode(
			@PathVariable("langCode") @ValidLangCode(message = "Language Code is Invalid") String langCode) {
		ResponseWrapper<List<ZoneExtnDto>> response = new ResponseWrapper<>();
		response.setResponse(zoneService.getLeafZonesBasedOnLangCode(langCode));
		return response;
	}
	

	//@PreAuthorize("hasAnyRole('INDIVIDUAL','ID_AUTHENTICATION','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_PROCESSOR','ZONAL_ADMIN','PRE_REGISTRATION','RESIDENT')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzoneszonename())")
	@GetMapping("/zonename")
	public ResponseWrapper<ZoneNameResponseDto> getZoneNameBasedOnUserIDAndLangCode(
			@RequestParam("userID") String userID,
			@ValidLangCode(message = "Language Code is Invalid") @RequestParam("langCode") String langCode) {
		ResponseWrapper<ZoneNameResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneService.getZoneNameBasedOnLangCodeAndUserID(userID, langCode));
		return responseWrapper;
	}

	/*@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzonesauthorize())")
	@GetMapping("/authorize")
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	public ResponseWrapper<Boolean> authorizeZone(@NotBlank @RequestParam("rid") String rId) {
		ResponseWrapper<Boolean> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneService.authorizeZone(rId));
		return responseWrapper;
	}*/
	
	/**
	 * Api to filter Zone based on column and type provided.
	 * 
	 * @param request the request DTO.
	 * @return the {@link FilterResponseDto}.
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostzonesfiltervalues())")
	@PostMapping("/filtervalues")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	public ResponseWrapper<FilterResponseCodeDto> zoneFilterValues(
			@RequestBody @Valid RequestWrapper<FilterValueDto> request) {
		auditUtil.auditRequest(MasterDataConstant.FILTER_API_IS_CALLED + Zone.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.FILTER_API_IS_CALLED + Zone.class.getCanonicalName(),"ADM-923");
		ResponseWrapper<FilterResponseCodeDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneService.zoneFilterValues(request.getRequest()));
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_FILTER, Zone.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_SEARCH_DESC, Zone.class.getCanonicalName()),"ADM-924");
		return responseWrapper;
	}

}
