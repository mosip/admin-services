package io.mosip.admin.constant;

public enum LostRidErrorCode {

	UNABLE_TO_RETRIEVE_LOSTRID("ADMN-LRID-001", "Unable to find the lost rid.."),
	UNABLE_TO_RETRIEVE_LOSTRID_DATA("ADMN-LRID-002", "Unable to find the lost rid data"),

	UNABLE_TO_RETRIEVE_APPLICANT_PHOTO("ADMN-LRID-003", "Unable to retrieve applicantPhoto");

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
