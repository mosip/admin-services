package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.PacketWorkflowActionRequestDTO;
import io.mosip.kernel.masterdata.dto.PacketWorkflowActionResponseDTO;
import io.mosip.kernel.masterdata.dto.RegProcResponseWrapper;
import io.mosip.kernel.masterdata.dto.SearchRequestDto;
import io.mosip.kernel.masterdata.dto.SearchResponseDto;

public interface PacketWorkflowActionService {

	RegProcResponseWrapper<PacketWorkflowActionResponseDTO> resumePacket(PacketWorkflowActionRequestDTO request);

	RegProcResponseWrapper<SearchResponseDto> searchPacket(SearchRequestDto request);

}
