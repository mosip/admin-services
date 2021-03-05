package io.mosip.kernel.masterdata.constant;

public enum UISpecErrorCode {

	UI_SPEC_FETCH_EXCEPTION("KER-UIS-001", "Error occurred while fetching UI spec"),
	UI_SPEC_INSERT_EXCEPTION("KER-UIS-002", "Error occurred while inserting UI spec"),
	UI_SPEC_UPDATE_EXCEPTION("KER-UIS-003", "Error occurred while updating UI spec"),
	UI_SPEC_NOT_FOUND_EXCEPTION("KER-UIS-004", "UI spec not found"),
	NO_PUBLISHED_UI_SPEC_FOUND_EXCEPTION("KER-UIS-005", "No published UI spec"),
	UI_SPEC_ALREADY_PUBLISHED("KER-UIS-006", "UI spec already published"),
	UI_SPEC_EFFECTIVE_FROM_IS_OLDER("UIS-UIS-007", "UI spec effective from date cannot be older"),	
	UI_SPEC_VALUE_PARSE_ERROR("KER-UIS-008", "Error while parsing json string")	;
	

	private final String errorCode;
	private final String errorMessage;

	private UISpecErrorCode(String errorCode, String errorMessage) {
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
