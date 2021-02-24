package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseCheckedException;
import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * The Class HotlistRetryException - Unchecked exception used to trigger retry.
 *
 * @author Manoj SP
 */
public class HotlistRetryException extends BaseUncheckedException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6748760277721155095L;

	/**
	 * Instantiates a new hotlist retry exception.
	 */
	public HotlistRetryException() {
		super();
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 */
	public HotlistRetryException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param errorCode    the error code
	 * @param errorMessage the error message
	 * @param rootCause    the root cause
	 */
	public HotlistRetryException(String errorCode, String errorMessage, Throwable rootCause) {
		super(errorCode, errorMessage, rootCause);
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param exceptionConstant the exception constant
	 */
	public HotlistRetryException(HotlistErrorConstants exceptionConstant) {
		this(exceptionConstant.getErrorCode(), exceptionConstant.getErrorMessage());
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param exceptionConstant the exception constant
	 */
	public HotlistRetryException(HotlistErrorConstants exceptionConstant, Throwable rootCause) {
		this(exceptionConstant.getErrorCode(), exceptionConstant.getErrorMessage(), rootCause);
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param BaseCheckedException the root cause
	 */
	public HotlistRetryException(BaseCheckedException rootCause) {
		this(rootCause.getErrorCode(), rootCause.getErrorText(), rootCause);
	}

	/**
	 * Instantiates a new hotlist retry exception.
	 *
	 * @param BaseUncheckedException the root cause
	 */
	public HotlistRetryException(BaseUncheckedException rootCause) {
		this(rootCause.getErrorCode(), rootCause.getErrorText(), rootCause);
	}
}