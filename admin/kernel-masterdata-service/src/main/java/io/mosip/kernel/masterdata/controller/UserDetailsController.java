package io.mosip.kernel.masterdata.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import io.mosip.kernel.masterdata.dto.PageDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UserDetailsGetExtnDto;
import io.mosip.kernel.masterdata.dto.UserDetailsPutDto;
import io.mosip.kernel.masterdata.dto.UsersDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for user details
 * 
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@RestController
@Api(tags = { "User Details" })
public class UserDetailsController {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private AuditUtil auditUtil;

	@PreAuthorize("hasAnyRole('ID_AUTHENTICATION','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_PROCESSOR','ZONAL_ADMIN')")
	@ResponseFilter
	@GetMapping(value = "/users/{id}")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User exists"),
			@ApiResponse(code = 400, message = "When path param id is null or invalid"),
			@ApiResponse(code = 404, message = "When No user found"),
			@ApiResponse(code = 500, message = "Error occurred when we attempted to fetch the user") })
	public ResponseWrapper<UserDetailsGetExtnDto> getUserById(@PathVariable("id") String userId) {
		ResponseWrapper<UserDetailsGetExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(userDetailsService.getUser(userId));
		return responseWrapper;
	}

	@PreAuthorize("hasAnyRole('ID_AUTHENTICATION','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_PROCESSOR','ZONAL_ADMIN')")
	@ResponseFilter
	@GetMapping(value = "/users")
	public ResponseWrapper<PageDto<UserDetailsExtnDto>>  getUsers(@RequestParam(value = "pageNumber", defaultValue = "0") @ApiParam(value = "page number for the requested data", defaultValue = "0") int page,
	@RequestParam(value = "pageSize", defaultValue = "1") @ApiParam(value = "page size for the request data", defaultValue = "1") int size,
	@RequestParam(value = "orderBy", defaultValue = "cr_dtimes") @ApiParam(value = "sort the requested data based on param value", defaultValue = "createdDateTime") String orderBy,
	@RequestParam(value = "direction", defaultValue = "DESC") @ApiParam(value = "order the requested data based on param", defaultValue = "DESC") String direction) {
		ResponseWrapper<PageDto<UserDetailsExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(userDetailsService.getUsers(page, size, orderBy, direction));
		return responseWrapper;
	}

	/**
	 * Post API to insert a new row of user
	 * 
	 * @param userDetailsDto input from user DTO
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PostMapping("usercentermapping")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "Maps User Detail and return User id")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to regcenter any error occured") })
	public ResponseWrapper<UserDetailsGetExtnDto> mapUserRegCenter(@RequestBody UserDetailsDto userDetailsDto) {
		auditUtil.auditRequest(MasterDataConstant.CREATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName());
		ResponseWrapper<UserDetailsGetExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(userDetailsService.createUser(userDetailsDto));
		return responseWrapper;
	}
	
	/**
	 * Put API to update a  row of user
	 * 
	 * @param userDetailsDto input from user DTO
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PutMapping("/usercentermapping")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "updates User Detail and return User id")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to regcenter any error occured") })
	public ResponseWrapper<UserDetailsPutDto> updateUserRegCenter(@RequestBody UserDetailsPutDto userDetailsDto) {
		auditUtil.auditRequest(MasterDataConstant.UPDATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName());
		ResponseWrapper<UserDetailsPutDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(userDetailsService.updateUser(userDetailsDto));
		return responseWrapper;
	}
	
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PatchMapping(value = "/usercentermapping")
	public ResponseWrapper<StatusResponseDto> updateUserRegCenterStatus(@Valid @RequestParam boolean isActive,
			@RequestParam String id) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + ZoneUserController.class.getCanonicalName());
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(userDetailsService.updateUserStatus(id, isActive));
		auditUtil.auditRequest(MasterDataConstant.STATUS_UPDATED_SUCCESS + ZoneUserController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_UPDATED_SUCCESS + ZoneUserController.class.getCanonicalName());
		return responseWrapper;
	}

	/**
	 * Delete API to delete a  row of user data
	 *
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@DeleteMapping("/users/{id}")
	@ApiOperation(value = "Service to delete User", notes = "delete the User Detail and return User id")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to regcenter any error occured") })
	public ResponseWrapper<IdResponseDto> deleteUserRegCenter(@PathVariable("id") String userId) {
		auditUtil.auditRequest(MasterDataConstant.DECOMMISION_API_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.DECOMMISION_API_CALLED + UserDetailsController.class.getCanonicalName());
		ResponseWrapper<IdResponseDto> responseWrapper = new ResponseWrapper<>();
		
		responseWrapper.setResponse(userDetailsService.deleteUser(userId));
		return responseWrapper;
	}
	
	/**
	 * This api will bring all users from iam by calling kernel auth service api.
	 * @param roleName
	 * @return
	 */	
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@GetMapping(value = "/usersdetails")
	public ResponseWrapper<UsersDto>  getUsersDetails(@RequestParam(required = false,name ="roleName") String roleName,
			@RequestParam(defaultValue = "0", required = false, name = "pageStart") int pageStart,
			@RequestParam(defaultValue = "0", required = false, name = "pageFetch") int pageFetch,
			@RequestParam(required = false, name = "email") String email,
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName,
			@RequestParam(required = false, name = "userName") String userName) {
		ResponseWrapper<UsersDto> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(MasterDataConstant.GET_USER_DETAILS_API_IS_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.GET_USER_DETAILS_API_IS_CALLED + UserDetailsController.class.getCanonicalName());
		responseWrapper.setResponse(userDetailsService.getUsers(roleName,pageStart, pageFetch, email,
				firstName, lastName, userName));
		return responseWrapper;
	}
	
	/**
	 * This api is for searching the user details.
	 * @param roleName
	 * @return
	 */	
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PostMapping(value = "/users/search")
	public ResponseWrapper<PageResponseDto<UserDetailsExtnDto>> serachUsersDetails(
			@RequestBody @Valid RequestWrapper<SearchDtoWithoutLangCode> dto) {
		ResponseWrapper<PageResponseDto<UserDetailsExtnDto>> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName());
		responseWrapper.setResponse(userDetailsService.searchUserDetails(dto.getRequest()));
		return responseWrapper;
	}
}
