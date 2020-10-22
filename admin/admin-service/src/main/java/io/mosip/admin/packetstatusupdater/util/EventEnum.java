package io.mosip.admin.packetstatusupdater.util;

import io.mosip.admin.packetstatusupdater.constant.AuditConstant;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;

public enum EventEnum {
	
	
    ADMIN_NOT_AUTHORIZED("ADM-PKT-001",AuditConstant.AUDIT_USER,"Admin Unauthorized","Admin is not authorized"),
	RID_INVALID("ADM-PKT-002",AuditConstant.AUDIT_USER,"RID INVALID","Registration id is invalid"),
	CENTRE_NOT_EXISTS("ADM-PKT-003",AuditConstant.AUDIT_USER,"CENTRE NOT EXISTS","Centre id extracted from registration id does not exists"),
	RID_MISS("ADM-PKT-004",AuditConstant.AUDIT_USER,"RID is missing","Registration id is missing in the input"),
	PACKET_STATUS_ERROR("ADM-PKT-005",AuditConstant.AUDIT_SYSTEM,"Error occurred while fetching Packet Status","System error has occured while fetching Packet Status for registration id %s"),
	PACKET_JSON_PARSE_EXCEPTION("ADM-PKT-010",AuditConstant.AUDIT_SYSTEM,"JSON Parse issue","JSON parse exception while parsing response"),
	PKT_STATUS_UPD_API_CALLED("ADM-2000",AuditConstant.AUDIT_SYSTEM,"Packet status update API called","Packet status update API called for registration id %s"),
	PKT_STATUS_UPD_SUCCESS("ADM-2001",AuditConstant.AUDIT_SYSTEM,"Decommission success","System successfully decommissioned for registration id %s"),
	AUTH_RID_WITH_ZONE("ADM-2002",AuditConstant.AUDIT_SYSTEM,"Authorize rid","Authorizing registration id %s with zone"),
	AUTH_RID_WITH_ZONE_SUCCESS("ADM-2004",AuditConstant.AUDIT_SYSTEM,"Authorization success for rid","Authorizing registration id %s with zone is successful"),
	AUTH_RID_WITH_ZONE_FAILURE("ADM-2005",AuditConstant.AUDIT_SYSTEM,"Authorization failure for rid","Authorizing registration id %s with zone is failed"),
	PACKET_STATUS("ADM-PKT-2000",AuditConstant.AUDIT_SYSTEM,"Packet status","Getting Packet status for %s"),
	PACKET_RECEIVER("ADM-PKT-2001",AuditConstant.AUDIT_SYSTEM,"Packet received","Packet with registration id %s has reached Packet Receiver"),
	PACKET_RECEIVER_WITH_TRANS_CODE("ADM-PKT-2002",AuditConstant.AUDIT_SYSTEM,"Packet uploaded","Packet with registration id %s is Uploaded to Landing Zone"),
	UPLOAD_PACKET("ADM-PKT-2003",AuditConstant.AUDIT_SYSTEM,"Packet uploaded","Packet with registration id %s uploaded to Packet Store"),
	PRINT_SERVICE("ADM-PKT-2004",AuditConstant.AUDIT_SYSTEM,"PROCESSED","PDF for registration id %s is added to Queue for Printing"),
	PRINT_POSTAL_SERVICE("ADM-PKT-2005",AuditConstant.AUDIT_SYSTEM,"PROCESSED","Printing and Post Completed for for registration id %s"),
	AUTH_FAILED_AUTH_MANAGER("ADM-2003",AuditConstant.AUDIT_SYSTEM,"Authentication failed","Authentication failed from AuthManager"),
	ACCESS_DENIED("ADM-2006",AuditConstant.AUDIT_SYSTEM,"Access Denied","Access denied from AuthManager"),
	AUTHEN_ERROR_401("ADM-2007",AuditConstant.AUDIT_SYSTEM,"Authentication Issue","The user tried to operate on a protected resource without providing the proper authorization"),
	AUTHEN_ERROR_403("ADM-2008",AuditConstant.AUDIT_SYSTEM,"Authentication Issue","The user does not have the necessary permissions for the resource");
	
	private final String id;

	private final String type;
	
	private final String name;

	private String description;

	
	

	private EventEnum(String id, String type, String name, String description) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	public String getName() {
		return name;
	}
	
	public static EventEnum getEventEnumBasedOnPAcketStatus(PacketStatusUpdateDto ps)
	{
		
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PACKET_RECEIVER"))
		{
			if(null==ps.getParentTransactionCode() || ps.getParentTransactionCode().isBlank())
				return getEventEnumWithValue(PACKET_RECEIVER,ps.getRegistrationId());
			return getEventEnumWithValue(PACKET_RECEIVER_WITH_TRANS_CODE,ps.getRegistrationId());
		}
		if(ps.getTransactionTypeCode().equalsIgnoreCase("UPLOAD_PACKET")) 
			return getEventEnumWithValue(UPLOAD_PACKET,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PRINT_SERVICE"))
			return getEventEnumWithValue(PRINT_SERVICE,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PRINT_POSTAL_SERVICE"))
			return getEventEnumWithValue(PRINT_POSTAL_SERVICE,ps.getRegistrationId());
		return null;
		
	}
	
	public static EventEnum getEventEnumWithValue(EventEnum e,String s)
	{
		e.setDescription(String.format(e.getDescription(),s));
		return e;
	}
	
	
}
