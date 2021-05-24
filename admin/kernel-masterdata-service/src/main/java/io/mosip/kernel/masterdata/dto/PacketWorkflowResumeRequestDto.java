package io.mosip.kernel.masterdata.dto;

import lombok.Data;

@Data
public class PacketWorkflowResumeRequestDto {

	private String workflowId;

	private String workflowAction;

}
