package io.mosip.admin.packetstatusupdater.constant;

public enum AuditErrorCode {

	AUDIT_PARSE_EXCEPTION("KER-MSD-196", "Parse Error exception"),
	AUDIT_EXCEPTION("KER-MSD-198","Audit Exception from client::");

	private final String errorCode;
	private final String errorMessage;

	private AuditErrorCode(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
