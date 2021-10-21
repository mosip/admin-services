package io.mosip.kernel.syncdata.constant;

public enum SyncAuthErrorCode {

    INVALID_REQUEST("KER-SYN-AUTH-001", "Invalid Request"),
    INVALID_REQUEST_TIME("KER-SYN-AUTH-002", "Invalid Request Time");

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
