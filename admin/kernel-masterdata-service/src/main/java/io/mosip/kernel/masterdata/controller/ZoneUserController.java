package io.mosip.kernel.masterdata.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.ZoneUserPutDto;
import io.mosip.kernel.masterdata.dto.ZoneUserSearchDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = { "ZoneUser Details" })
public class ZoneUserController {
	@Autowired
	private AuditUtil auditUtil;
	
	@Autowired
	ZoneUserService zoneUserService;
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPutzoneuser())")
	@PutMapping(value="/zoneuser")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "Maps zone and User  and returns ZoneUserDto")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and zone successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No zoneuser found"),
			@ApiResponse(code = 500, message = "While mapping user to zone any error occured") })
	public ResponseWrapper<ZoneUserExtnDto> updateapUserZone(
			@Valid @RequestBody RequestWrapper<ZoneUserPutDto> zoneUserDto) {
		auditUtil.auditRequest(MasterDataConstant.UPDATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),"ADM-925");
		ResponseWrapper<ZoneUserExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneUserService.updateZoneUserMapping(zoneUserDto.getRequest()));
		return responseWrapper;
	}
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostzoneuser())")
	@PostMapping(value="/zoneuser")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "Maps zone and User  and returns ZoneUserDto")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No zoneuser found"),
			@ApiResponse(code = 500, message = "While mapping user to zone any error occured") })
	public ResponseWrapper<ZoneUserExtnDto> mapUserZone(@Valid @RequestBody RequestWrapper<ZoneUserDto> zoneUserDto) {
		auditUtil.auditRequest(MasterDataConstant.CREATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),"ADM-926");
		ResponseWrapper<ZoneUserExtnDto> responseWrapper = new ResponseWrapper<>();
		
		responseWrapper.setResponse(zoneUserService.createZoneUserMapping(zoneUserDto.getRequest()));
		return responseWrapper;
	}
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeletezoneuseruseridzonecode())")
	@DeleteMapping("/zoneuser/{userid}/{zonecode}")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "deletes zone and user mapping")
	@ApiResponses({ @ApiResponse(code = 201, message = "When successfully deletes zone and user mapping"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to zone any error occured") })
	public ResponseWrapper<IdResponseDto> deleteMapUserZone(@Valid @NotEmpty @StringFormatter(min = 1, max = 256) @PathVariable("userid") String userId,
			@Valid @NotEmpty @StringFormatter(min = 1, max = 256) @PathVariable("zonecode") String zoneCode) {
		auditUtil.auditRequest(MasterDataConstant.CREATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),"ADM-927");
		ResponseWrapper<IdResponseDto> responseWrapper = new ResponseWrapper<>();
		
		responseWrapper.setResponse(zoneUserService.deleteZoneUserMapping(userId,zoneCode));
		return responseWrapper;
	}
	


	//@PreAuthorize("hasAnyRole('INDIVIDUAL','ID_AUTHENTICATION','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_PROCESSOR','ZONAL_ADMIN','PRE_REGISTRATION','RESIDENT')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetzoneuserhistoryuserid())")
	@GetMapping(value = "/zoneuser/history/{userid}/{eff_dtimes}")
	public ResponseWrapper<ZoneUserHistoryResponseDto> getHistoryByUserIdAndTimestamp(@Valid @NotEmpty @StringFormatter(min = 1, max = 256)@PathVariable("userid") String userId,
			@PathVariable("eff_dtimes") String effDtimes) {
		ResponseWrapper<ZoneUserHistoryResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneUserService.getHistoryByUserIdAndTimestamp(userId, effDtimes));
		return responseWrapper;
	}
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchzoneuser())")
	@PatchMapping(value = "/zoneuser")
	public ResponseWrapper<StatusResponseDto> updateapUserZoneStatus(@Valid @RequestParam boolean isActive,
			@RequestParam String userId) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),"ADM-928");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(zoneUserService.updateZoneUserMapping(userId, isActive));
		auditUtil.auditRequest(MasterDataConstant.STATUS_UPDATED_SUCCESS + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_UPDATED_SUCCESS + ZoneUserController.class.getCanonicalName(),"ADM-929");
		return responseWrapper;
	}

	/**
	 * This api is for searching the users list mapped to zone.
	 * @param dto
	 * @return
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostzoneusersearch())")
	@ResponseFilter
	@PostMapping(value = "/zoneuser/search")
	public ResponseWrapper<PageResponseDto<ZoneUserSearchDto>> searchZoneUserMapping(
			@RequestBody @Valid RequestWrapper<SearchDtoWithoutLangCode> dto) {
		ResponseWrapper<PageResponseDto<ZoneUserSearchDto>> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(
				MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName(),"ADM-930");
		responseWrapper.setResponse(zoneUserService.searchZoneUserMapping(dto.getRequest()));
		return responseWrapper;
	}

}
