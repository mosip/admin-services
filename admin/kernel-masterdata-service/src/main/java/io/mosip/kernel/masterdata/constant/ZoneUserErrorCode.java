package io.mosip.kernel.masterdata.constant;

public enum ZoneUserErrorCode {
	USER_MAPPING_EXCEPTION("KER-USR-016", "Zone & User mapping  failed"),
	ZONE_NOT_FOUND_EXCEPTION("KER-USR-019", "Zone & User mapping  not found"),
	ZONE_FETCH_EXCEPTION("KER-USR-020", " Zone & User mapping fetch failed"),
	USER_MAPPING_NOT_PRSENT_IN_DB("KER-USR-017", "Zone & User mapping  not present in db"),
	DUPLICATE_REQUEST("KER-USR-018", "duplicate request"),
	USER_MAPPING_PRSENT_IN_DB("KER-USR-021", "The given user already mapped with different zone"),
	USER_MAPPING_EXIST("KER-USR-011", "User Center mapping must be deleted before remapping user to different zone"),
	INVALID_ZONE("KER-USR-014", "Admin not authorized to access for this Zone"),
	USER_MAPPING_EXIST_DELETE("KER-USR-022", "User Center mapping must be deleted before Deleting the User from the zone");

	
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
