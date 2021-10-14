package io.mosip.kernel.masterdata.constant;

/**
 * Error code for TemplateFileFormat
 * 
 * @author Neha Sinha
 * @author Megha Tanga
 * @since 1.0.0
 */
public enum TemplateFileFormatErrorCode {

	TEMPLATE_FILE_FORMAT_INSERT_EXCEPTION("KER-MSD-232", "Error occurred while inserting Template File Format details"),
	TEMPLATE_FILE_FORMAT_NOT_FOUND("KER-MSD-046", "Template not found."),
	TEMPLATE_FILE_FORMAT_UPDATE_EXCEPTION("KER-MSD-093", "Error occurred while updating Template"),
	TEMPLATE_FILE_FORMAT_DELETE_EXCEPTION("KER-MSD-236", "Error occurred while deleting Template"),
	TEMPLATE_FILE_FORMAT_DELETE_DEPENDENCY_EXCEPTION("KER-MSD-125", "Cannot delete dependency found."),
	TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION("KER-MSD-246", "Error occurred while fetching Template File Format"),
	TEMPLATE_FILE_FORMAT_UPDATE_MAPPING_EXCEPTION("KER-MSD-237",
			"Mapping exist before deactivating remove the mapping");

	private final String errorCode;
	private final String errorMessage;

	private TemplateFileFormatErrorCode(final String errorCode, final String errorMessage) {
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
