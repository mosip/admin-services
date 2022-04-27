package io.mosip.admin.controller;

import java.util.ArrayList;
import java.util.List;

import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.admin.service.AdminService;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;

@RestController
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	AuditUtil auditUtil;

	@PostMapping("/lostRid")
	@Operation(summary = "lostRid", description = "lostRid", tags = { "AdminController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found" ,content = @Content(schema = @Schema(hidden = true)))})
	private ResponseWrapper<LostRidExtnDto> lostRid(@RequestBody RequestWrapper<SearchInfo> searchInfo) {
		auditUtil.setAuditRequestDto(EventEnum.LOST_RID_API_CALLED,null);
		LostRidResponseDto lostRidResponseDto = adminService.lostRid(searchInfo.getRequest());
		auditUtil.setAuditRequestDto(EventEnum.LOST_RID_SUCCESS,null);
		return buildLostRidResponse(lostRidResponseDto);
	}

	@GetMapping("/applicantVerficationDetails/{rid}")
	@Operation(summary = "applicantVerficationDetails", description = "applicantVerficationDetails", tags = { "AdminController" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "401", description = "Unauthorized" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden" ,content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found" ,content = @Content(schema = @Schema(hidden = true)))})
	private ResponseWrapper<ApplicantVerficationDto> getApplicantVerficationDetails(@PathVariable("rid") String rid) {
		auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_API_CALLED,null);
		ResponseWrapper<ApplicantVerficationDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(adminService.getApplicantVerficationDetails(rid));
		auditUtil.setAuditRequestDto(EventEnum.APPLICANT_VERIFICATION_SUCCESS,null);
		return responseWrapper;
	}
	
	private ResponseWrapper<LostRidExtnDto> buildLostRidResponse(LostRidResponseDto lostRidResponseDto) {
		ResponseWrapper<LostRidExtnDto> responseWrapper = new ResponseWrapper<>();
		LostRidExtnDto lostRidExtnDto = new LostRidExtnDto();
		List<ServiceError> sr=new ArrayList<>();
		if (!lostRidResponseDto.getErrors().isEmpty()) {
			for (ErrorDTO ed : lostRidResponseDto.getErrors()) {
				ServiceError se = new ServiceError();
				se.setErrorCode(ed.getErrorCode());
				se.setMessage(ed.getErrorMessage());
				sr.add(se);
			}
		}
		lostRidExtnDto.setData(lostRidResponseDto.getResponse());
		responseWrapper.setResponse(lostRidExtnDto);
		responseWrapper.setErrors(sr);
		return responseWrapper;
	}

}
