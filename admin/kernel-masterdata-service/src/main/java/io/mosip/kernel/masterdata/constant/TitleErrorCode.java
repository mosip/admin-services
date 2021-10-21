package io.mosip.kernel.masterdata.constant;

/**
 * Enum for error codes for fetching titles from master data
 * 
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
public enum TitleErrorCode {

	TITLE_FETCH_EXCEPTION("KER-MSD-047", "Error occured while fetching Titles"),
	TITLE_NOT_FOUND("KER-MSD-048", "Title not found"),
	TITLE_INSERT_EXCEPTION("KER-MSD-XXX", "Error occurred while inserting Title details	"),
	TITLE_UPDATE_EXCEPTION("KER-MSD-103", "Error occurred while updating Title details"),
	TITLE_DELETE_EXCEPTION("KER-MSD-104", "Error occurred while deleting Title details");

	private final String errorCode;
	private final String errorMessage;

	private TitleErrorCode(final String errorCode, final String errorMessage) {
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
