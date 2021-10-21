package io.mosip.kernel.syncdata.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public class DateParsingException extends BaseUncheckedException {
	/**
	 * Generated serial version id
	 */

	private static final long serialVersionUID = 216592528649715895L;

	/**
	 * Constructor the initialize Handler exception
	 * 
	 * @param errorCode    The error code for this exception
	 * @param errorMessage The error message for this exception
	 */
	public DateParsingException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	/**
	 * Constructor the initialize Handler exception
	 * 
	 * @param errorCode    The error code for this exception
	 * @param errorMessage The error message for this exception
	 * @param rootCause    the specified cause
	 */
	public DateParsingException(String errorCode, String errorMessage, Throwable rootCause) {
		super(errorCode, errorMessage, rootCause);
	}

}
