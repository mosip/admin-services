package io.mosip.kernel.masterdata.constant;

/**
 * Constants for Language related errors.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 *
 */
public enum LanguageErrorCode {

	NO_LANGUAGE_FOUND_EXCEPTION("KER-MSD-24", "Language not found"),
	LANGUAGE_FETCH_EXCEPTION("KER-MSD-23", "Error occured while fetching Languages"),
	LANGUAGE_CREATE_EXCEPTION("KER-MSD-049", "Error occurred while inserting Language details"),
	LANGUAGE_UPDATE_EXCEPTION("KER-MSD-XXX", "Error occured while updating Language"),
	LANGUAGE_DELETE_EXCEPTION("KER-MSD-XXX", "Error occured while deleting Language");

	private final String errorCode;
	private final String errorMessage;

	private LanguageErrorCode(final String errorCode, final String errorMessage) {
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
