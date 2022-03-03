package io.mosip.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.admin.dto.ErrorDTO;
import io.mosip.admin.dto.LostRidExtnDto;
import io.mosip.admin.dto.LostRidResponseDto;
import io.mosip.admin.dto.SearchInfo;
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
	private ResponseWrapper<LostRidExtnDto> lostRid(@RequestBody RequestWrapper<SearchInfo> searchInfo) {
		auditUtil.setAuditRequestDto(EventEnum.LOST_RID_API_CALLED,null);
		LostRidResponseDto lostRidResponseDto = adminService.lostRid(searchInfo.getRequest());
		auditUtil.setAuditRequestDto(EventEnum.LOST_RID_SUCCESS,null);
		return buildLostRidResponse(lostRidResponseDto);
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
