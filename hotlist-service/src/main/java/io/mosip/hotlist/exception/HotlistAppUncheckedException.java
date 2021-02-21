package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseUncheckedException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HotlistAppUncheckedException extends BaseUncheckedException {

	private static final long serialVersionUID = 2718788886693768207L;
	
	private HotlistErrorConstants errorConstant;

	public HotlistAppUncheckedException() {
		super();
	}
	
	public HotlistAppUncheckedException(HotlistErrorConstants errorConstant) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage());
		this.errorConstant = errorConstant;
	}
	
	public HotlistAppUncheckedException(HotlistErrorConstants errorConstant, Throwable rootCause) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage(), rootCause);
		this.errorConstant = errorConstant;
	}
	
	
}
