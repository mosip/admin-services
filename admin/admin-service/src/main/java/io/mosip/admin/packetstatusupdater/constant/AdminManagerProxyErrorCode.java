package io.mosip.admin.packetstatusupdater.constant;

public enum AdminManagerProxyErrorCode {
	ADMIN_LOG_FAILED("ADM-PKT-000","Failed to log the admin audit"),
	INVALID_ADMIN_LOG("ADM-PKT-005","Invalid admin log details");
	
	private final String errorCode;
	private final String errorMessage;

	private AdminManagerProxyErrorCode(final String errorCode, final String errorMessage) {
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
