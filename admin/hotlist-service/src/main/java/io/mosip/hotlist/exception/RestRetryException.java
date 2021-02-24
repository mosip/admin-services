package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseCheckedException;
import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * The Class IdRepoRetryException - Unchecked exception used to trigger retry
 * in RestHelper.
 *
 * @author Manoj SP
 */
public class RestRetryException extends BaseUncheckedException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6748760277721155095L;

	/**
	 * Instantiates a new id repo retry exception.
	 */
	public RestRetryException() {
		super();
	}

	/**
	 * Instantiates a new id repo retry exception.
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 */
	public RestRetryException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	/**
	 * Instantiates a new id repo retry exception.
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 * @param rootCause    the root cause
	 */
	public RestRetryException(String errorCode, String errorMessage, Throwable rootCause) {
		super(errorCode, errorMessage, rootCause);
	}

	/**
	 * Instantiates a new id repo retry exception.
	 *
	 * @param exceptionConstant the exception constant
	 */
	public RestRetryException(HotlistErrorConstants exceptionConstant) {
		this(exceptionConstant.getErrorCode(), exceptionConstant.getErrorMessage());
	}

	/**
	 * Instantiates a new id repo retry exception.
	 *
	 * @param BaseCheckedException the root cause
	 */
	public RestRetryException(BaseCheckedException rootCause) {
		this(rootCause.getErrorCode(), rootCause.getErrorText(), rootCause);
	}

	/**
	 * Instantiates a new id repo retry exception.
	 *
	 * @param BaseUncheckedException the root cause
	 */
	public RestRetryException(BaseUncheckedException rootCause) {
		this(rootCause.getErrorCode(), rootCause.getErrorText(), rootCause);
	}
}