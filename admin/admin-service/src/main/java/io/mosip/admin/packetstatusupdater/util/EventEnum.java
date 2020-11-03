package io.mosip.admin.packetstatusupdater.util;

import io.mosip.admin.packetstatusupdater.constant.AuditConstant;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;

public enum EventEnum {
	
	
    ADMIN_NOT_AUTHORIZED("ADM-PKT-001",AuditConstant.AUDIT_USER,"Authorization request","User %s is not authorized","KER-MSD","Kernel masterdata","User","Admin"),
	RID_INVALID("ADM-PKT-002",AuditConstant.AUDIT_USER,"Check RID validation","Registration id is invalid","KER-MSD","Kernel masterdata","User","Admin"),
	CENTRE_NOT_EXISTS("ADM-PKT-003",AuditConstant.AUDIT_USER,"Check centre exists","Centre id extracted from registration id does not exists","KER-MSD","Kernel masterdata","User","Admin"),
	RID_MISS("ADM-PKT-004",AuditConstant.AUDIT_USER,"Check RID exists","Registration id is missing in the input","KER-MSD","Kernel masterdata","User","Admin"),
	PACKET_STATUS_ERROR("ADM-PKT-005",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","System error has occured while fetching Packet Status for registration id %s","KER-MSD","Kernel masterdata","User","Admin"),
	PACKET_JSON_PARSE_EXCEPTION("ADM-PKT-412",AuditConstant.AUDIT_SYSTEM,"Request for get packet response API","JSON parse exception while parsing response","KER-MSD","Kernel masterdata","User","Admin"),
	PKT_STATUS_UPD_API_CALLED("ADM-PKT-102",AuditConstant.AUDIT_SYSTEM,"Request for packet status update API","Packet status update API called for registration id %s","KER-MSD","Kernel masterdata","User","Admin"),
	PKT_STATUS_UPD_SUCCESS("ADM-PKT-200",AuditConstant.AUDIT_SYSTEM,"Request for packet status update API","System successfully decommissioned for registration id %s","KER-MSD","Kernel masterdata","User","Admin"),
	AUTH_RID_WITH_ZONE("ADM-PKT-103",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone","KER-MSD","Kernel masterdata","User","Admin"),
	AUTH_RID_WITH_ZONE_SUCCESS("ADM-PKT-201",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone is successful","KER-MSD","Kernel masterdata","User","Admin"),
	AUTH_RID_WITH_ZONE_FAILURE("ADM-PKT-406",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone is failed","KER-MSD","Kernel masterdata","User","Admin"),
	PACKET_STATUS("ADM-PKT-103",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Getting Packet status for %s","KER-MSD","Kernel masterdata","User","Admin"),
	PACKET_RECEIVER("ADM-PKT-104",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has reached Packet Receiver","KER-MSD","Kernel masterdata","User","Admin"),
	PACKET_RECEIVER_WITH_TRANS_CODE("ADM-PKT-105",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is Uploaded to Landing Zone","KER-MSD","Kernel masterdata","User","Admin"),
	UPLOAD_PACKET("ADM-PKT-106",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s uploaded to Packet Store","KER-MSD","Kernel masterdata","User","Admin"),
	PRINT_SERVICE("ADM-PKT-107",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","PDF for registration id %s is added to Queue for Printing","KER-MSD","Kernel masterdata","User","Admin"),
	PRINT_POSTAL_SERVICE("ADM-PKT-108",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Printing and Post Completed for for registration id %s","KER-MSD","Kernel masterdata","User","Admin"),
	AUTH_FAILED_AUTH_MANAGER("ADM-PKT-405",AuditConstant.AUDIT_SYSTEM,"Check authentication","Authentication failed from AuthManager","KER-MSD","Kernel masterdata","User","Admin"),
	ACCESS_DENIED("ADM-PKT-422",AuditConstant.AUDIT_SYSTEM,"Check access","Access denied from AuthManager","KER-MSD","Kernel masterdata","User","Admin"),
	AUTHEN_ERROR_401("ADM-PKT-401",AuditConstant.AUDIT_SYSTEM,"Check authentication","The user tried to operate on a protected resource without providing the proper authorization","KER-MSD","Kernel masterdata","User","Admin"),
	AUTHEN_ERROR_403("ADM-PKT-403",AuditConstant.AUDIT_SYSTEM,"Check authentication","The user does not have the necessary permissions for the resource","KER-MSD","Kernel masterdata","User","Admin"),
	
	
	BULKDATA_INSERT_API_CALLED("ADM-BLK-102",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert API","API called to insert bulk data","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_INSERT_SUCCESS("ADM-BLK-200",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert API","Bulkdata insertion is successful","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_INSERT_CATEGORY("ADM-BLK-101",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Inserting bulk data based on category %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_INSERT_CSV("ADM-BLK-102",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation to CSV","Inserting bulk data from %s file to csv file","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_INSERT_CSV_STATUS_ERROR("ADM-BLK-403",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation to CSV","Inserting bulk data to csv failed - %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_OPERATION_ERROR("ADM-BLK-404",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation","Error occured while inserting data for category  %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_OPERATION_CSV_EXT_VALIDATOR_ISSUE("ADM-BLK-405",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","%s is not csv file","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_OPERATION_INVALID_CSV_FILE("ADM-BLK-406",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","Invalid csv file %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE("ADM-BLK-407",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","All the rows have same number of element in csv file %s","KER-MSD","Kernel masterdata","User","Admin"), 
	BULKDATA_INSERT_CSV_STATUS("ADM-BLK-102",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Status of inserting file %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_UPLOAD_TRANSACTION_API_CALLED("ADM-BLK-103",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload transaction","Inserting data into bulk data upload transaction","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_UPLOAD_TRANSACTION_API_SUCCESS("ADM-BLK-104",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload transaction","Successfully inserted data into bulk data upload transaction","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_INVALID_CATEGORY("ADM-BLK-408",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Invalid category","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_UPLOAD_PACKET_STATUS_ERROR("ADM-BLK-408",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload packets","Uploading packets failed - %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_UPLOAD_PACKET_STATUS("ADM-BLK-105",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Status of uploading packet %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION("ADM-BLK-106",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Getting transaction details for transaction id %s","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION_SUCCESS("ADM-BLK-201",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Getting transaction details for transaction id %s is success","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION_ERROR("ADM-BLK-409",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Unable to retrieve transaction details for transaction id %s ","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION_ALL("ADM-BLK-107",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Getting transaction details for all the transactions","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION_ALL_SUCCESS("ADM-BLK-202",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Getting transaction details for all the transactions is success","KER-MSD","Kernel masterdata","User","Admin"),
	BULKDATA_TRANSACTION_ALL_ERROR("ADM-BLK-410",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Unable to retrieve transaction details","KER-MSD","Kernel masterdata","User","Admin");
	
	
	private final String eventId;

	private final String type;
	
	private final String name;

	private String description;
	
	private String moduleId;
	
	private String moduleName;
	
	private String id;
	
	private String idType;

	private EventEnum(String eventId, String type, String name, String description,String moduleId,String moduleName,String id,String idType) {
		this.eventId = eventId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.moduleId=moduleId;
		this.moduleName=moduleName;
		this.id=id;
		this.idType=idType;
	}

	
	
	public String getId() {
		return id;
	}

	public String getIdType() {
		return idType;
	}


	public String getEventId() {
		return eventId;
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
	
	public String getModuleId() {
		return moduleId;
	}

	public String getModuleName() {
		return moduleName;
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
