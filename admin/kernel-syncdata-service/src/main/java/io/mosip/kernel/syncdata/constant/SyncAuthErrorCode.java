package io.mosip.kernel.syncdata.constant;

public enum SyncAuthErrorCode {

    INVALID_REQUEST("KER-SYN-AUTH-001", "Invalid Request"),
    INVALID_REQUEST_TIME("KER-SYN-AUTH-002", "Invalid Request Time"),
    ERROR_GETTING_TOKEN("KER-SYN-AUTH-003","Error while getting token"),
    ERROR_SENDING_OTP("KER-SYN-AUTH-004","Error sending OTP"),
    ERROR_HANDLE_CA_CERTIFICATE("KER-SYN-AUTH-005","Error handling CA-Certificate");



    private final String errorCode;
    private final String errorMessage;

    private SyncAuthErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
