package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseCheckedException;

/**
 * The Class HotlistAppException.
 *
 * @author Manoj SP
 */
public class HotlistAppException extends BaseCheckedException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2718788886693768207L;
	
	/**
	 * Instantiates a new hotlist app exception.
	 */
	public HotlistAppException() {
		super();
	}
	
	/**
	 * Instantiates a new hotlist app exception.
	 *
	 * @param errorConstant the error constant
	 */
	public HotlistAppException(HotlistErrorConstants errorConstant) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage());
	}
	
	/**
	 * Instantiates a new hotlist app exception.
	 *
	 * @param errorCode the error code
	 * @param errorMessage the error message
	 */
	public HotlistAppException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	/**
	 * Instantiates a new hotlist app exception.
	 *
	 * @param errorConstant the error constant
	 * @param rootCause the root cause
	 */
	public HotlistAppException(HotlistErrorConstants errorConstant, Throwable rootCause) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage(), rootCause);
	}
	
	
}
