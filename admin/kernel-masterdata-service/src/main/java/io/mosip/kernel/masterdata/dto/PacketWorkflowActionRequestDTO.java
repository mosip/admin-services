package io.mosip.kernel.masterdata.dto;

import java.util.List;

import lombok.Data;

@Data
public class PacketWorkflowActionRequestDTO {

	private List<String> workflowIds;

	private String workflowAction;
}
