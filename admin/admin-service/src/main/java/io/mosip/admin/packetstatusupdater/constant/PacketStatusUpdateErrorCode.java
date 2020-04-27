package io.mosip.admin.packetstatusupdater.constant;

public enum PacketStatusUpdateErrorCode {

	ADMIN_UNAUTHORIZED("ADM-PKT-001","Admin is not authorized"),
	RID_INVALID("ADM-PKT-002","RID is invalid"),
	CENTER_ID_NOT_PRESENT("ADM_PKT-003","Center does not exist"),
	PACKET_JSON_PARSE_EXCEPTION("ADM-PKT-010","JSON parse exception while parsing response"),
	PACKET_FETCH_EXCEPTION("ADM-PKT-090","Error occured while fetching packet status update");
	
	
	private final String errorCode;
	private final String errorMessage;

	private PacketStatusUpdateErrorCode(final String errorCode, final String errorMessage) {
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
