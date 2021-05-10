package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.masterdata.constant.ApiName;
import io.mosip.kernel.masterdata.constant.PacketWorkflowErrorCode;
import io.mosip.kernel.masterdata.dto.PacketWorkflowActionRequestDTO;
import io.mosip.kernel.masterdata.dto.PacketWorkflowActionResponseDTO;
import io.mosip.kernel.masterdata.dto.RegProcRequestWrapper;
import io.mosip.kernel.masterdata.dto.RegProcResponseWrapper;
import io.mosip.kernel.masterdata.dto.SearchRequestDto;
import io.mosip.kernel.masterdata.dto.SearchResponseDto;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.service.PacketWorkflowActionService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.RestClient;

@Service
public class PacketWorkflowActionServiceImpl implements PacketWorkflowActionService {

	@Value("${mosip.regproc.workflow.action.api-id:mosip.registration.processor.workflow.action}")
	private String requestId;

	@Value("${mosip.regproc.workflow.action.version:1.0}")
	private String reqVersion;

	@Value("${mosip.regproc.workflow.search.api-id:mosip.registration.processor.workflow.search}")
	private String searchRequestId;

	@Value("${mosip.regproc.workflow.search.version:v1}")
	private String searchReqVersion;

	@Autowired
	RestClient restClient;

	@Autowired
	ObjectMapper objectMapper;

	@SuppressWarnings("unchecked")
	@Override
	public RegProcResponseWrapper<PacketWorkflowActionResponseDTO> resumePacket(
			PacketWorkflowActionRequestDTO request) {
		RegProcRequestWrapper<PacketWorkflowActionRequestDTO> procRequestWrapper = new RegProcRequestWrapper<>();
		RegProcResponseWrapper<PacketWorkflowActionResponseDTO> procResponseWrapper = null;
		procRequestWrapper.setId(requestId);
		procRequestWrapper.setVersion(reqVersion);
		procRequestWrapper.setRequest(request);
		procRequestWrapper.setRequesttime(LocalDateTime.now().toString());
		
		try {
			String response = restClient.postApi(ApiName.PACKET_RESUME_API, null, "", "", MediaType.APPLICATION_JSON,
					procRequestWrapper, String.class);
			procResponseWrapper = objectMapper.readValue(response, RegProcResponseWrapper.class);
		} catch (Exception e) {
			throw new MasterDataServiceException(
					PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_RESUMING_PACKET.getErrorCode(),
					PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_RESUMING_PACKET.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		return procResponseWrapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RegProcResponseWrapper<SearchResponseDto> searchPacket(SearchRequestDto request) {
		RegProcRequestWrapper<SearchRequestDto> searchDtoResponseWrapp = new RegProcRequestWrapper<>();
		RegProcResponseWrapper<SearchResponseDto> procResponseWrapper = null;
		searchDtoResponseWrapp.setId(searchRequestId);
		searchDtoResponseWrapp.setVersion(searchReqVersion);
		searchDtoResponseWrapp.setRequest(request);
		searchDtoResponseWrapp.setRequesttime(LocalDateTime.now().toString());

		try {
			String response = restClient.postApi(ApiName.PACKET_PAUSE_API, null, "", "", MediaType.APPLICATION_JSON,
					searchDtoResponseWrapp, String.class);
			procResponseWrapper = objectMapper.readValue(response, RegProcResponseWrapper.class);
		} catch (Exception e) {
			throw new MasterDataServiceException(PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorCode(),
					PacketWorkflowErrorCode.ERROR_OCCURED_WHILE_SEARCHING.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		return procResponseWrapper;
	}

}
