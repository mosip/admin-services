package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseUncheckedException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class HotlistAppUncheckedException.
 *
 * @author Manoj SP
 */

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Hash code.
 *
 * @return the int
 */
@EqualsAndHashCode(callSuper = false)
public class HotlistAppUncheckedException extends BaseUncheckedException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2718788886693768207L;
	
	/** The error constant. */
	private HotlistErrorConstants errorConstant;

	/**
	 * Instantiates a new hotlist app unchecked exception.
	 */
	public HotlistAppUncheckedException() {
		super();
	}
	
	/**
	 * Instantiates a new hotlist app unchecked exception.
	 *
	 * @param errorConstant the error constant
	 */
	public HotlistAppUncheckedException(HotlistErrorConstants errorConstant) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage());
		this.errorConstant = errorConstant;
	}
	
	/**
	 * Instantiates a new hotlist app unchecked exception.
	 *
	 * @param errorConstant the error constant
	 * @param rootCause the root cause
	 */
	public HotlistAppUncheckedException(HotlistErrorConstants errorConstant, Throwable rootCause) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage(), rootCause);
		this.errorConstant = errorConstant;
	}
	
	
}
