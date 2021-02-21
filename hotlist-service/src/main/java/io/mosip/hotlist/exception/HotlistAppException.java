package io.mosip.hotlist.exception;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.kernel.core.exception.BaseCheckedException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HotlistAppException extends BaseCheckedException {

	private static final long serialVersionUID = 2718788886693768207L;
	
	public HotlistAppException() {
		super();
	}
	
	public HotlistAppException(HotlistErrorConstants errorConstant) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage());
	}
	
	public HotlistAppException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
	
	public HotlistAppException(HotlistErrorConstants errorConstant, Throwable rootCause) {
		super(errorConstant.getErrorCode(), errorConstant.getErrorMessage(), rootCause);
	}
	
	
}
