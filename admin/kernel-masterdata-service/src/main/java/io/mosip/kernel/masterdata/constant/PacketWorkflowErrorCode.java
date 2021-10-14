package io.mosip.kernel.masterdata.constant;

public enum PacketWorkflowErrorCode {

	ERROR_OCCURED_WHILE_SEARCHING("KER-MSD-263", "error occured while searching the record : invalid rquest"),
	ERROR_OCCURED_WHILE_RESUMING_PACKET("KER-MSD-364", "failed to resume packet");

	private String errorCode;
	private String errorMessage;

	private PacketWorkflowErrorCode(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
