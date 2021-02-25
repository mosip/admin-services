package io.mosip.hotlist.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Enum HotlistErrorConstants.
 *
 * @author Manoj SP
 */
public enum HotlistErrorConstants {

	/** The missing input parameter. */
	MISSING_INPUT_PARAMETER("ADM-HTL-001", "Missing Input Parameter - %s"),

	/** The invalid input parameter. */
	INVALID_INPUT_PARAMETER("ADM-HTL-002", "Invalid Input Parameter - %s"),

	/** The id type not allowed. */
	ID_TYPE_NOT_ALLOWED("ADM-HTL-003", "Input IdType not allowed to hotlist"),

	/** The invalid request. */
	INVALID_REQUEST("ADM-HTL-004", "Invalid Request"),

	/** The data validation failed. */
	DATA_VALIDATION_FAILED("ADM-HTL-005", "Input Data Validation Failed"),

	/** The database access error. */
	DATABASE_ACCESS_ERROR("ADM-HTL-006", "Error occured while performing DB operations"),

	/** The no record found. */
	NO_RECORD_FOUND("ADM-HTL-007", "No Record(s) found"),

	/** The record exists. */
	RECORD_EXISTS("ADM-HTL-008", "Failed to hotlist as existing hotlist is still active"),

	/** The client error. */
	CLIENT_ERROR("ADM-HTL-009", "4XX - Client Error occured"),

	/** The server error. */
	SERVER_ERROR("ADM-HTL-010", "5XX - Server Error occured"),

	/** The connection timed out. */
	CONNECTION_TIMED_OUT("ADM-HTL-011", "Connection timed out"),

	/** The authorization failed. */
	AUTHORIZATION_FAILED("ADM-HTL-012", "Authorization Failed"),

	/** The authentication failed. */
	AUTHENTICATION_FAILED("ADM-HTL-013", "Authentication Failed"),

	/** The encryption decryption failed. */
	ENCRYPTION_DECRYPTION_FAILED("ADM-HTL-014", "Failed to either encrypt/decrypt message using Kernel Crypto Manager"),

	/** The unknown error. */
	UNKNOWN_ERROR("ADM-HTL-015", "Unknown error occurred");

	/** The error code. */
	private final String errorCode;

	/** The error message. */
	private final String errorMessage;

	/**
	 * Instantiates a new hotlist error constants.
	 *
	 * @param errorCode the error code
	 * @param errorMessage the error message
	 */
	private HotlistErrorConstants(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Gets the all error codes.
	 *
	 * @return the all error codes
	 */
	public static List<String> getAllErrorCodes() {
		return Collections.unmodifiableList(Arrays.asList(HotlistErrorConstants.values()).parallelStream()
				.map(HotlistErrorConstants::getErrorCode).collect(Collectors.toList()));
	}
}
