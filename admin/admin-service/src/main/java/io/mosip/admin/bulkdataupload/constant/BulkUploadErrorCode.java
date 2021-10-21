package io.mosip.admin.bulkdataupload.constant;
/**
 * 
 * @author dhanendra
 *
 */
public enum BulkUploadErrorCode {

	UNABLE_TO_RETRIEVE_TRANSCATION("ADMN-BLK-TRNSCTNS-001","Unable ro retrieve the transactions"),
	BULK_OPERATION_ERROR("ADMN-BLK-001",
			"Any error occured during the bulk operation. If the category is masterdata, list operations are listed. If the category is packet, list of failed packet informations are listed"),
	BULK_UPDATE_OPERATION_ERROR("ADM-BLK-001", "Data is not exist in db"),
	INVALID_ARGUMENT("ADM-BLK-002", "plz enter valid input"),
	DUPLICATE_RECORD("ADM-BLK-003", "Duplicate Record");
	private final String errorCode;
	private final String errorMessage;


	private BulkUploadErrorCode(final String errorCode, final String errorMessage) {
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
