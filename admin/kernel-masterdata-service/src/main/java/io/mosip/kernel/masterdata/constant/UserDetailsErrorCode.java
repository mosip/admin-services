package io.mosip.kernel.masterdata.constant;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
public enum UserDetailsErrorCode {
	INVALID_EFFECTIVE_DATE_TIME_FORMATE_EXCEPTION("KER-USR-002", "Invalid Date Format"),
	USER_FETCH_EXCEPTION("KER-USR-004", "Error occurred while retrieving User details"),
	USER_CREATION_EXCEPTION("KER-USR-005", "User creation failed"),
	USER_UNMAP_EXCEPTION("KER-USR-006", "User unmap failed"),
	USER_NOT_FOUND("KER-USR-007", "User Center mapping not found"),
	ZONE_USER_MAPPING_NOT_FOUND("KER-USR-008", "Zone user mapping not exists or not active"),
	USER_DETAILS_PARSE_ERROR("KER-USR-009","Error occured while parsing auth service userdetails response"),
	ZONE_USER_MAPPING_ERROR("KER-USR-010","Error occured while zone user mapping"),
	ZONE_USER_MAPPING_NOT_ACTIVE("KER-USR-025","Kindly activate user-zone mapping before activating user-center"),
	CENTER_LANG_MAPPING_NOT_EXISTS("KER-USR-012","Reg center is not exists for given language"),
	USER_ALREADY_EXISTS("KER-USR-013", "User already exists"),
	INVALID_ZONE("KER-USR-014", "Admin not authorized to access for this Zone"),
	INVALID_ZONE_MAPPING("KER-USR-015", "Provided center zone is not part of user zone");

	private final String errorCode;
	private final String errorMessage;

	private UserDetailsErrorCode(final String errorCode, final String errorMessage) {
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
