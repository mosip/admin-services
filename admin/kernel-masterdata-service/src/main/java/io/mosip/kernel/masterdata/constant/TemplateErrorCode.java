package io.mosip.kernel.masterdata.constant;

/**
 * Error code for Template
 * 
 * @author Neha
 * @since 1.0.0
 */
public enum TemplateErrorCode {

	TEMPLATE_FETCH_EXCEPTION("KER-MSD-045", "Error ocurred while fetching Templates"),
	TEMPLATE_INSERT_EXCEPTION("KER-MSD-145", "Exception during inserting data into db"),
	TEMPLATE_NOT_FOUND("KER-MSD-046", "Template not found."),
	TEMPLATE_UPDATE_EXCEPTION("KER-MSD-095", "Error occured while updating Template"),
	TEMPLATE_DELETE_EXCEPTION("KER-MSD-096", "Error occured while deleting Template");

	private final String errorCode;
	private final String errorMessage;

	private TemplateErrorCode(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
