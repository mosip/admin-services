package io.mosip.kernel.masterdata.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UsersDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;
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
	public ResponseWrapper<UserDetailsDto> getUserById(@PathVariable("id") String userId) {
		ResponseWrapper<UserDetailsDto> responseWrapper = new ResponseWrapper<>();
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
	 * Post API to insert a new row of Machine data
	 * 
	 * @param machineRequest input from user Machine DTO
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PostMapping("/users/{id}/{lang}/{regcenterid}")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "Maps User Detail and return User id")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to regcenter any error occured") })
	public ResponseWrapper<IdAndLanguageCodeID> mapUserRegCenter(@PathVariable("id") String userId, 
	@PathVariable("lang") String langCode,
	@PathVariable("regcenterid") String regCenterId
	 ) {
		auditUtil.auditRequest(MasterDataConstant.CREATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName());
		ResponseWrapper<IdAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		userDetailsDto.setId(userId);
		userDetailsDto.setIsActive(true);
		userDetailsDto.setLangCode(langCode);
		userDetailsDto.setRegCenterId(regCenterId);
		userDetailsDto.setName(userId);
		userDetailsDto.setStatusCode("ACT");
		responseWrapper.setResponse(userDetailsService.createUser(userDetailsDto));
		return responseWrapper;
	}
	
	/**
	 * Put API to update a  row of Machine data
	 * 
	 * @param machineRequest input from user Machine DTO
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PutMapping("/users/{id}/{lang}/{regcenterid}")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "updates User Detail and return User id")
	@ApiResponses({ @ApiResponse(code = 201, message = "When User and Registration center successfully mapped"),
			@ApiResponse(code = 400, message = "When Request is invalid"),
			@ApiResponse(code = 404, message = "When No Regcenter found"),
			@ApiResponse(code = 500, message = "While mapping user to regcenter any error occured") })
	public ResponseWrapper<UserDetailsDto> updateUserRegCenter(@PathVariable("id") String userId, 
	@PathVariable("lang") String langCode,
	@PathVariable("regcenterid") String regCenterId
	 ) {
		auditUtil.auditRequest(MasterDataConstant.UPDATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + UserDetailsController.class.getCanonicalName());
		ResponseWrapper<UserDetailsDto> responseWrapper = new ResponseWrapper<>();
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		userDetailsDto.setId(userId);
		userDetailsDto.setIsActive(true);
		userDetailsDto.setLangCode(langCode);
		userDetailsDto.setRegCenterId(regCenterId);
		userDetailsDto.setName(userId);
		userDetailsDto.setStatusCode("ACT");
		responseWrapper.setResponse(userDetailsService.updateUser(userDetailsDto));
		return responseWrapper;
	}
	
	/**
	 * dalete API to delete a  row of Machine data
	 * 
	 * @param machineRequest input from user Machine DTO
	 * 
	 * @return Responding with Machine which is inserted successfully
	 *         {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@DeleteMapping("/users/{id}/")
	@ApiOperation(value = "Service to map Users with regcenter", notes = "deletes User Detail and return User id")
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
	public ResponseWrapper<PageResponseDto<UserDetailsExtnDto>> serachUsersDetails(@RequestBody @Valid RequestWrapper<SearchDto> dto) {
		ResponseWrapper<PageResponseDto<UserDetailsExtnDto>> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_USER_DETAILS_API_IS_CALLED + SearchDto.class.getCanonicalName());
		responseWrapper.setResponse(userDetailsService.searchUserDetails(dto.getRequest()));
		return responseWrapper;
	}
}
