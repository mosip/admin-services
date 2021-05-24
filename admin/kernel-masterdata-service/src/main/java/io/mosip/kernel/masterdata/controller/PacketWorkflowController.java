package io.mosip.kernel.masterdata.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.masterdata.dto.PacketWorkflowActionResponseDTO;
import io.mosip.kernel.masterdata.dto.PacketWorkflowResumeRequestDto;
import io.mosip.kernel.masterdata.dto.RegProcResponseWrapper;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.SearchResponseDto;
import io.mosip.kernel.masterdata.service.PacketWorkflowActionService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;

@RestController
@Api(tags = { "PacketWorkflow" })
@RequestMapping("/packet")
public class PacketWorkflowController {

	@Autowired
	AuditUtil auditUtil;

	@Autowired
	PacketWorkflowActionService packetWorkflowActionService;

	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PostMapping("/resume")
	public RegProcResponseWrapper<PacketWorkflowActionResponseDTO> resumePacket(
			@Valid @RequestBody RequestWrapper<PacketWorkflowResumeRequestDto> requestDto) {
		RegProcResponseWrapper<PacketWorkflowActionResponseDTO> responseWrapper = packetWorkflowActionService
				.resumePacket(requestDto.getRequest());
		return responseWrapper;
	}

	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PostMapping("/search")
	public RegProcResponseWrapper<SearchResponseDto> searchPacket(
			@Valid @RequestBody RequestWrapper<SearchDtoWithoutLangCode> requestDto) {
		RegProcResponseWrapper<SearchResponseDto> responseWrapper = packetWorkflowActionService
				.searchPacket(requestDto.getRequest());
		return responseWrapper;
	}

}
