package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.PacketWorkflowActionResponseDTO;
import io.mosip.kernel.masterdata.dto.PacketWorkflowResumeRequestDto;
import io.mosip.kernel.masterdata.dto.RegProcResponseWrapper;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.SearchResponseDto;

public interface PacketWorkflowActionService {

	RegProcResponseWrapper<PacketWorkflowActionResponseDTO> resumePacket(PacketWorkflowResumeRequestDto request);

	RegProcResponseWrapper<SearchResponseDto> searchPacket(SearchDtoWithoutLangCode request);

}
