package io.mosip.admin.constant;

public enum AdminErrorCode {

    UNABLE_TO_RETRIEVE_RID("ADM-AVD-001", "Unable to retrieve rid details"),
    RID_INVALID("ADM-AVD-002","RID is invalid"),
    RID_NOT_FOUND("ADM-AVD-003","RID Not Found");
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
