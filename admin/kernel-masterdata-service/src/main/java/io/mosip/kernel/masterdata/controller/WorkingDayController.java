package io.mosip.kernel.masterdata.controller;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.WorkingDaysDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.WorkingDaysExtnDto;
import io.mosip.kernel.masterdata.dto.request.WorkingDaysPutRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.dto.WeekDaysResponseDto;
import io.mosip.kernel.masterdata.dto.WorkingDaysResponseDto;
import io.mosip.kernel.masterdata.service.RegWorkingNonWorkingService;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Kishan Rathore
 *
 */
@RestController
@Validated
@Api(tags = { "Working Non Working Days" })
public class WorkingDayController {

	@Autowired
	private RegWorkingNonWorkingService service;

	/**
	 * 
	 * Function to fetch week days detail based on given Registration center ID and
	 * Language code.
	 * 
	 * @param weekDaysRequest
	 * @return WeekDaysResponseDto week days based on given Registration center ID
	 *         and Language code {@link WeekDaysResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetweekdaysregistrationcenteridlangcode())")
	@GetMapping(value = "/weekdays/{registrationCenterId}/{langCode}")
	@ApiOperation(value = "Retrieve all Week Days for given Registration center ID and Languge Code", notes = "Retrieve all Week Days for given Registration center ID and Languge Code")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When Week Days retrieved from database for the given Registration center ID Languge Code"),
			@ApiResponse(code = 404, message = "When No Week Days found for the given Registration center ID and Languge Code"),
			@ApiResponse(code = 500, message = "While retrieving Week Days any error occured") })
	public ResponseWrapper<WeekDaysResponseDto> getWeekDays(@PathVariable("registrationCenterId") String regCenterId,
			@ValidLangCode(message = "Language Code is Invalid") @PathVariable("langCode") String langCode) {

		ResponseWrapper<WeekDaysResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(service.getWeekDaysList(regCenterId, langCode));
		return responseWrapper;
	}

	/**
	 * 
	 * Function to fetch working days detail based on given Registration center ID
	 * and day code.
	 * 
	 * @param WorkingDaysRequest
	 * @return WorkingDaysResponseDto working days based on given Registration
	 *         center ID and Day code {@link WorkingDaysResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetworkingdaysregistrationcenteridlangcode())")
	@GetMapping(value = "/workingdays/{registrationCenterID}/{langCode}")
	@ApiOperation(value = "Retrieve all working Days for given Registration center ID and Lang Code", notes = "Retrieve all working Days for given Registration center ID and Languge Code")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When working days retrieved from database for the given Registration center ID lang Code"),
			@ApiResponse(code = 404, message = "When No working days found for the given Registration center ID and lang Code"),
			@ApiResponse(code = 500, message = "While retrieving working days any error occured") })
	public ResponseWrapper<WorkingDaysResponseDto> getWorkingays(
			@PathVariable("registrationCenterID") String regCenterId,
			@ValidLangCode(message = "Language Code is Invalid") @PathVariable("langCode") String langCode) {

		ResponseWrapper<WorkingDaysResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(service.getWorkingDays(regCenterId, langCode));
		return responseWrapper;
	}
	
	/**
	 * 
	 * Function to fetch working days detail based on given Language code.
	 * 
	 * @param WorkingDaysRequest
	 * @return WorkingDaysResponseDto working days based on given lang code {@link WorkingDaysResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetworkingdayslangcode())")
	@GetMapping(value = "/workingdays/{langCode}")
	@ApiOperation(value = "Retrieve all working Days for given Lang Code", notes = "Retrieve all working Days for given Languge Code")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When working days retrieved from database for the given lang Code"),
			@ApiResponse(code = 404, message = "When No working days found for the given lang Code"),
			@ApiResponse(code = 500, message = "While retrieving working days any error occured") })
	public ResponseWrapper<WorkingDaysResponseDto> getWorkingDaysByLangCode(
			@ValidLangCode(message = "Language Code is Invalid") @PathVariable("langCode") String langCode) {

		ResponseWrapper<WorkingDaysResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(service.getWorkingDays(langCode));
		return responseWrapper;
	}
	/**
	 *
	 * Function to update working days detail.
	 *
	 * @param WorkingDaysRequest
	 * @return WorkingDaysResponseDto working days based on given lang code {@link WorkingDaysResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetworkingdayslangcode())")
	@PutMapping(value = "/workingdays")
	@ApiOperation(value = "update the working Days for given Lang Code", notes = "update the working Days")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When working days updated"),
			@ApiResponse(code = 404, message = "When No working days found for the given lang Code"),
			@ApiResponse(code = 500, message = "While retrieving working days any error occured") })
	public ResponseWrapper<WorkingDaysExtnDto> updateWorkingDays(
			@RequestBody RequestWrapper<WorkingDaysPutRequestDto> workingDaysPutRequestDtoRequestWrapper) throws NoSuchFieldException, IllegalAccessException {

		ResponseWrapper<WorkingDaysExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(service.updateWorkingDays(workingDaysPutRequestDtoRequestWrapper.getRequest()));
		return responseWrapper;
	}
	/**
	 *
	 * Function to update working days status.
	 *
	 * @param code
	 *
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetworkingdayslangcode())")
	@PatchMapping(value = "/workingdays")
	@ApiOperation(value = "update the workingdays status", notes = "update the workingdays status")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When working days status is updated"),
			@ApiResponse(code = 404, message = "When No working days found for the given code"),
			@ApiResponse(code = 500, message = "While retrieving working days any error occured") })
	public ResponseWrapper<StatusResponseDto> updateWorkingDaysStatus(
			@RequestParam String code, @RequestParam boolean isActive) {

		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(service.updateWorkingDaysStatus(code,isActive));
		return responseWrapper;
	}

}
