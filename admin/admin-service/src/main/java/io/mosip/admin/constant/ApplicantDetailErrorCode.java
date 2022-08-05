package io.mosip.admin.constant;

public enum ApplicantDetailErrorCode {

    UNABLE_TO_RETRIEVE_RID_DETAILS("ADM-AVD-001", "A technical error occurred while retrieving the data, please try again after some time."),
    RID_INVALID("ADM-AVD-002","RID is invalid"),
    RID_NOT_FOUND("ADM-AVD-003","The card for this request ID is not generated. Please check the status of the ID."),
    DATA_NOT_FOUND("ADM-AVD-004","Applicant Photo Not Found"),
    DIGITAL_CARD_RID_NOT_FOUND("ADM-AVD-005", "Digital card not found for the RID, please try after few days"),
    DIGITAL_CARD_NOT_ACKNOWLEDGED("ADM-AVD-006", "Please acknowledge the details before downloading digital card"),
    REQ_ID_NOT_FOUND("ADM-AVD-007","Request id not found"),

    LIMIT_EXCEEDED("ADM-AVD-008","Your daily search limit has exceeded. Please try searching again tomorrow."),

    DATA_SHARE_EXPIRED_EXCEPTION("ADM-AVD-008", "Data share usuage expired");

    private final String errorCode;
    private final String errorMessage;

    private ApplicantDetailErrorCode(final String errorCode, final String errorMessage) {
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
