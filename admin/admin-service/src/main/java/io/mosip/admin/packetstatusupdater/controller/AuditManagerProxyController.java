package io.mosip.admin.packetstatusupdater.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.mosip.admin.packetstatusupdater.dto.AuditManagerRequestDto;
import io.mosip.admin.packetstatusupdater.dto.AuditManagerResponseDto;
import io.mosip.admin.packetstatusupdater.service.AuditManagerProxyService;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;

import java.util.Map;

/**
 * @author Megha Tanga
 */

@RestController
@RequestMapping(value = "/auditmanager/log")
public class AuditManagerProxyController {
	/**
	 * AuditManager Service field with functions related to auditing
	 */
	@Autowired
	AuditManagerProxyService auditManagerProxyService;
	
	
	/**
	 * Function to proxy service to log admin UI audit
	 * 
	 * @param requestDto
	 *            {@link AuditManagerRequestDto} having required fields for auditing
	 * @return The {@link AuditManagerResponseDto} having the status of audit
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostauditmanagerlog())")
	@PostMapping
	public ResponseWrapper<AuditManagerResponseDto> addAudit(@RequestBody @Valid RequestWrapper<AuditManagerRequestDto> requestDto,
															 @RequestHeader Map<String, String> headers) {
		ResponseWrapper<AuditManagerResponseDto> response = new ResponseWrapper<>();
		response.setResponse(auditManagerProxyService.logAdminAudit(requestDto.getRequest()));
		return response;
	}

}