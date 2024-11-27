package io.mosip.kernel.masterdata.constant;

/**
 * ENUM constants for blocklisted words.
 * 
 * @author Abhishek Kumar
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
public enum BlocklistedWordsErrorCode {
	NO_BLOCKLISTED_WORDS_FOUND("KER-MSD-008", "Blocklisted word not found"),
	DUPLICATE_BLOCKLISTED_WORDS_FOUND("KER-MSD-071", "Duplicate Blocklisted word request"),
	BLOCKLISTED_WORDS_FETCH_EXCEPTION("KER-MSD-007", "Error occurred while fetching Blocklisted words"),
	BLOCKLISTED_WORDS_INSERT_EXCEPTION("KER-MSD-070", "Error occurred while inserting Blocklisted words"),
	BLOCKLISTED_WORDS_UPDATE_EXCEPTION("KER-MSD-105", "Error occurred while updating Blocklisted Word"),
	BLOCKLISTED_WORDS_DELETE_EXCEPTION("KER-MSD-213", "Error occurred while deleting Blocklisted Word");

	/**
	 * The error code.
	 */
	private final String errorCode;
	/**
	 * The error message.
	 */
	private final String errorMessage;

	/**
	 * Constructor for BlocklistedWordsErrorCode.
	 * 
	 * @param errorCode    the error code.
	 * @param errorMessage the error message.
	 */
	private BlocklistedWordsErrorCode(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Getter for error code.
	 * 
	 * @return the error code.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Getter for error message.
	 * 
	 * @return the error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
