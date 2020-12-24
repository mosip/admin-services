package io.mosip.kernel.masterdata.constant;

public enum ZoneUserErrorCode {
	USER_MAPPING_EXCEPTION("KER-USR-005", "Zone & User mapping  failed"),
	USER_MAPPING_NOT_PRSENT_IN_DB("KER-USR-006", "Zone & User mapping  not present in db"),
	DUPLICATE_REQUEST("KER-USR-007", "duplicate request");
	private final String errorCode;
	private final String errorMessage;

	private ZoneUserErrorCode(final String errorCode, final String errorMessage) {
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
