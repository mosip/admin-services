package io.mosip.admin.packetstatusupdater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;
import io.mosip.admin.packetstatusupdater.service.PacketStatusUpdateService;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;

/**
 * The Class PacketUpdateStatusController.
 * 
 * @author Srinivasan
 */
@RestController
@RequestMapping("/packetstatusupdate")
public class PacketUpdateStatusController {

	/** The packet update status service. */
	@Autowired
	private PacketStatusUpdateService packetUpdateStatusService;

	@Autowired
	private AuditUtil auditUtil;

	/**
	 * Validate packet.
	 *
	 * @param rId the r id
	 * @return the response wrapper
	 */
	@GetMapping
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ResponseFilter
	public ResponseWrapper<PacketStatusUpdateResponseDto> validatePacket(@RequestParam(value = "rid") String rId,
			@RequestParam(value = "langCode", required = false) String langCode) {

		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.PKT_STATUS_UPD_API_CALLED, rId));
		ResponseWrapper<PacketStatusUpdateResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(packetUpdateStatusService.getStatus(rId, langCode));
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.PKT_STATUS_UPD_SUCCESS, rId));
		return responseWrapper;
	}
}
