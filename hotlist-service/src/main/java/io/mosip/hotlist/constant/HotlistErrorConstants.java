package io.mosip.hotlist.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum HotlistErrorConstants {

	MISSING_INPUT_PARAMETER("ADM-HTL-001", "Missing Input Parameter - %s"),

	INVALID_INPUT_PARAMETER("ADM-HTL-002", "Invalid Input Parameter - %s"),

	INVALID_REQUEST("ADM-HTL-003", "Invalid Request"),

	DATA_VALIDATION_FAILED("ADM-HTL-004", "Input Data Validation Failed"),

	DATABASE_ACCESS_ERROR("ADM-HTL-005", "Error occured while performing DB operations"),

	NO_RECORD_FOUND("ADM-HTL-006", "No Record(s) found"),

	RECORD_EXISTS("ADM-HTL-007", "Failed to hotlist as existing hotlist is still active"),

	CLIENT_ERROR("ADM-HTL-008", "4XX - Client Error occured"),

	SERVER_ERROR("ADM-HTL-009", "5XX - Server Error occured"),

	CONNECTION_TIMED_OUT("ADM-HTL-010", "Connection timed out"),

	AUTHORIZATION_FAILED("ADM-HTL-011", "Authorization Failed"),

	AUTHENTICATION_FAILED("ADM-HTL-012", "Authentication Failed"),

	ENCRYPTION_DECRYPTION_FAILED("ADM-HTL-013", "Failed to either encrypt/decrypt message using Kernel Crypto Manager"),

	UNKNOWN_ERROR("ADM-HTL-014", "Unknown error occurred");

	private final String errorCode;

	private final String errorMessage;

	private HotlistErrorConstants(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public static List<String> getAllErrorCodes() {
		return Collections.unmodifiableList(Arrays.asList(HotlistErrorConstants.values()).parallelStream()
				.map(HotlistErrorConstants::getErrorCode).collect(Collectors.toList()));
	}
}
