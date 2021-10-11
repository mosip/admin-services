package io.mosip.kernel.masterdata.constant;

public enum ZoneUserErrorCode {
	USER_MAPPING_EXCEPTION("KER-USR-005", "Zone & User mapping  failed"),
	ZONE_NOT_FOUND_EXCEPTION("KER-USR-008", "Zone & User mapping  not found"),
	ZONE_FETCH_EXCEPTION("KER-USR-009", " Zone & User mapping fetch failed"),
	USER_MAPPING_NOT_PRSENT_IN_DB("KER-USR-006", "Zone & User mapping  not present in db"),
	DUPLICATE_REQUEST("KER-USR-007", "duplicate request"),
	USER_MAPPING_PRSENT_IN_DB("KER-USR-010", "The given user already mapped with different zone"),
	USER_MAPPING_EXIST("KER-USR-011", "mapping exists for usercenter"),
	INVALID_ZONE("KER-USR-014", "Admin not authorized to access for this Zone");


	
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
