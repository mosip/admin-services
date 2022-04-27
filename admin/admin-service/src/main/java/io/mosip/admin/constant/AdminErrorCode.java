package io.mosip.admin.constant;

public enum AdminErrorCode {

    UNABLE_TO_RETRIEVE_RID("ADM-AVD-001", "A technical error occurred while retrieving the data, please try again after some time."),
    RID_INVALID("ADM-AVD-002","RID is invalid"),
    RID_NOT_FOUND("ADM-AVD-003","The card for this request ID is not generated. Please check the status of the ID."),
    DATA_NOT_FOUND("ADM-AVD-004","ApplicantPhoto Not Found");
    private final String errorCode;
    private final String errorMessage;

    private AdminErrorCode(final String errorCode, final String errorMessage) {
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
