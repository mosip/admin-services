package io.mosip.admin.constant;

public enum LostRidErrorCode {

	UNABLE_TO_RETRIEVE_LOSTRID("ADMN-LRID-001", "Unable to find the lost rid.."),
	LOST_RID_DATE_RANGE_EXCEEDED("ADMN-LRID-002","searching between date should be less than %s days");
	private final String errorCode;
	private final String errorMessage;

	private LostRidErrorCode(final String errorCode, final String errorMessage) {
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
