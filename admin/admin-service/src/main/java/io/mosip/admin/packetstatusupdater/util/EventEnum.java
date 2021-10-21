package io.mosip.admin.packetstatusupdater.util;

import io.mosip.admin.packetstatusupdater.constant.AuditConstant;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateDto;

public enum EventEnum {
	
	
    USER_NOT_AUTHORIZED("ADM-PKT-405",AuditConstant.AUDIT_USER,"Authorization request","User %s is not authorized","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	RID_INVALID("ADM-PKT-406",AuditConstant.AUDIT_USER,"Check RID validation","Registration id is invalid","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	CENTRE_NOT_EXISTS("ADM-PKT-407",AuditConstant.AUDIT_USER,"Check centre exists","Centre id extracted from registration id does not exists","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	RID_MISS("ADM-PKT-408",AuditConstant.AUDIT_USER,"Check RID exists","Registration id is missing in the input","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_STATUS_ERROR("ADM-PKT-409",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","System error has occured while fetching Packet Status for registration id %s","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_JSON_PARSE_EXCEPTION("ADM-PKT-402",AuditConstant.AUDIT_SYSTEM,"Request for get packet response API","JSON parse exception while parsing response","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PKT_STATUS_UPD_API_CALLED("ADM-PKT-102",AuditConstant.AUDIT_SYSTEM,"Request for packet status update API","Packet status update API called for registration id %s","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PKT_STATUS_UPD_SUCCESS("ADM-PKT-200",AuditConstant.AUDIT_SYSTEM,"Request for packet status update API","System successfully decommissioned for registration id %s","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTH_RID_WITH_ZONE("ADM-PKT-103",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTH_RID_WITH_ZONE_SUCCESS("ADM-PKT-201",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone is successful","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTH_RID_WITH_ZONE_FAILURE("ADM-PKT-404",AuditConstant.AUDIT_SYSTEM,"Check authorization RID with zone","Authorizing registration id %s with zone is failed","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_STATUS("ADM-PKT-104",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","Getting Packet status for %s","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_RECEIVER("ADM-PKT-105",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","Packet with registration id %s has reached packet receiver","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_RECEIVER_WITH_TRANS_CODE("ADM-PKT-106",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","Packet with registration id %s is uploaded to landing zone","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	UPLOAD_PACKET("ADM-PKT-107",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","Packet with registration id %s uploaded to packet store","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PRINT_SERVICE("ADM-PKT-108",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","PDF for registration id %s is added to queue for printing","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PRINT_POSTAL_SERVICE("ADM-PKT-109",AuditConstant.AUDIT_SYSTEM,"Request to get packet status","Printing and post completed for registration id %s","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTH_FAILED_AUTH_MANAGER("ADM-PKT-410",AuditConstant.AUDIT_SYSTEM,"Check authentication","Authentication failed from AuthManager","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	ACCESS_DENIED("ADM-PKT-405",AuditConstant.AUDIT_SYSTEM,"Check access","Access denied from AuthManager","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTHEN_ERROR_401("ADM-PKT-401",AuditConstant.AUDIT_SYSTEM,"Check authentication","The user tried to operate on a protected resource without providing the proper authorization","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	AUTHEN_ERROR_403("ADM-PKT-403",AuditConstant.AUDIT_SYSTEM,"Check authentication","The user does not have the necessary permissions for the resource","ADM-PKT","Packet service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	
	
	BULKDATA_UPLOAD_API_CALLED("ADM-BLK-101",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload API","API called for uploading bulk data","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_SUCCESS("ADM-BLK-200",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload API","Bulkdata upload is successful","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_CATEGORY("ADM-BLK-102",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload operation","Bulk data upload based on category %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD("ADM-BLK-103",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata operation","Bulk data upload based on %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_CSV("ADM-BLK-104",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload operation","Bulk data %s file","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_CSV_STATUS_ERROR("ADM-BLK-401",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload operation","Bulk data upload failed - %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_JOBDETAILS("ADM-BLK-105",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload operation","Bulk data upload job started with job id - %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_OPERATION_ERROR("ADM-BLK-402",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata operation","Error occured while operating bulk data for %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_OPERATION_CSV_EXT_VALIDATOR_ISSUE("ADM-BLK-403",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","%s is not csv file","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_OPERATION_INVALID_CSV_FILE("ADM-BLK-404",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","Invalid csv file %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_OPERATION_CSV_VALIDATOR_ISSUE("ADM-BLK-405",AuditConstant.AUDIT_SYSTEM,"Validating CSV file request","All the rows have same number of element in csv file %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME), 
	BULKDATA_UPLOAD_CSV_STATUS("ADM-BLK-106",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Status of inserting file %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_TRANSACTION_API_CALLED("ADM-BLK-107",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload transaction","Inserting data into transaction table based on %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_TRANSACTION_API_SUCCESS("ADM-BLK-108",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload transaction","Successfully inserted data into transaction table with transaction id %s","ADM-BLK","Bulk data service","%s","Transaction Id",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_INVALID_CATEGORY("ADM-BLK-406",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Invalid category","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_PACKET_STATUS_ERROR("ADM-BLK-407",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload packets","Uploading packets failed - %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_UPLOAD_PACKET_STATUS("ADM-BLK-109",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation based on category","Status of uploading packet %s","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION("ADM-BLK-110",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Getting transaction details for transaction id %s","ADM-BLK","Bulk data service","%s","Transaction Id",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION_SUCCESS("ADM-BLK-201",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Getting transaction details for transaction id %s is success","ADM-BLK","Bulk data service","%s","Transaction Id",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION_ERROR("ADM-BLK-408",AuditConstant.AUDIT_SYSTEM,"Request to get transaction details based on transaction id","Unable to retrieve transaction details for transaction id %s ","ADM-BLK","Bulk data service","%s","Transaction Id",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION_ALL("ADM-BLK-111",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Getting transaction details for all the transactions","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION_ALL_SUCCESS("ADM-BLK-202",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Getting transaction details for all the transactions is success","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_TRANSACTION_ALL_ERROR("ADM-BLK-409",AuditConstant.AUDIT_SYSTEM,"Request to get all the transaction details","Unable to retrieve transaction details","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_INVALID_ARGUMENT("ADM-BLK-410",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata insert operation","Invalid argument","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BULKDATA_PACKET_UPLOAD("ADM-BLK-112",AuditConstant.AUDIT_SYSTEM,"Request for bulkdata upload operation","Bulk data packet upload of %s file","ADM-BLK","Bulk data service","NO_ID","NO_ID_TYPE",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	VIRUS_SCAN("ADM-PKT-110",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is virus scanned","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	VALIDATE_PACKET("ADM-PKT-111",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is packet validated","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	OSI_VALIDATE("ADM-PKT-112",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is osi validated","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	EXTERNAL_INTEGRATION("ADM-PKT-113",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is external integration validated","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	DEMOGRAPHIC_VERIFICATION("ADM-PKT-114",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is demographically verified","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	MANUAL_VERIFICATION("ADM-PKT-115",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is manually verified","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BIOGRAPHIC_VERIFICATION("ADM-PKT-116",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is biometrically  verified","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	UIN_GENERATOR("ADM-PKT-117",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has uin generated","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	NOTIFICATION("ADM-PKT-118",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has notification sent","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PACKET_REPROCESS("ADM-PKT-119",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is being reprocessed","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	QUALITY_CHECK("ADM-PKT-120",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has quality check done","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	BIOMETRIC_AUTHENTICATION("ADM-PKT-121",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has Biometric Authentication done","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	SECUREZONE_NOTIFICATION("ADM-PKT-122",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s has notification received to securezone","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME),
	PRINT("ADM-PKT-123",AuditConstant.AUDIT_SYSTEM,"Request for get packet status","Packet with registration id %s is at print","ADM-PKT","Packet service","%s","RID",AuditConstant.APPLICATION_ID,AuditConstant.APPLICATION_NAME);
	
	
	private final String eventId;

	private final String type;
	
	private final String name;

	private String description;
	
	private String moduleId;
	
	private String moduleName;
	
	private String id;
	
	private String idType;
	
	private  final String applicationId;
	
	private final String applicationName;

	private EventEnum(String eventId, String type, String name, String description,String moduleId,String moduleName,String id,String idType,String applicationId,String applicationName) {
		this.eventId = eventId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.moduleId=moduleId;
		this.moduleName=moduleName;
		this.id=id;
		this.idType=idType;
		this.applicationId=applicationId;
		this.applicationName=applicationName;
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
	
	public void setId(String id) {
		this.id=id;
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
	
	

	public String getApplicationId() {
		return applicationId;
	}



	public String getApplicationName() {
		return applicationName;
	}



	public static EventEnum getEventEnumBasedOnPAcketStatus(PacketStatusUpdateDto ps)
	{
		
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PACKET_RECEIVER"))
		{
			if(null==ps.getParentTransactionId() || ps.getParentTransactionId().isBlank())
				return getEventEnumWithValue(PACKET_RECEIVER,ps.getRegistrationId());
			return getEventEnumWithValue(PACKET_RECEIVER_WITH_TRANS_CODE,ps.getRegistrationId());
		}
		if(ps.getTransactionTypeCode().equalsIgnoreCase("UPLOAD_PACKET")) 
			return getEventEnumWithValue(UPLOAD_PACKET,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PRINT_SERVICE"))
			return getEventEnumWithValue(PRINT_SERVICE,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PRINT_POSTAL_SERVICE"))
			return getEventEnumWithValue(PRINT_POSTAL_SERVICE,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("VIRUS_SCAN")) 
			return getEventEnumWithValue(VIRUS_SCAN,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("VALIDATE_PACKET")) 
			return getEventEnumWithValue(VALIDATE_PACKET,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("OSI_VALIDATE")) 
			return getEventEnumWithValue(OSI_VALIDATE,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("EXTERNAL_INTEGRATION")) 
			return getEventEnumWithValue(EXTERNAL_INTEGRATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("DEMOGRAPHIC_VERIFICATION")) 
			return getEventEnumWithValue(DEMOGRAPHIC_VERIFICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("MANUAL_VERIFICATION")) 
			return getEventEnumWithValue(MANUAL_VERIFICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("BIOGRAPHIC_VERIFICATION")) 
			return getEventEnumWithValue(BIOGRAPHIC_VERIFICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("UIN_GENERATOR")) 
			return getEventEnumWithValue(UIN_GENERATOR,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("NOTIFICATION")) 
			return getEventEnumWithValue(NOTIFICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PACKET_REPROCESS")) 
			return getEventEnumWithValue(PACKET_REPROCESS,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("QUALITY_CHECK")) 
			return getEventEnumWithValue(QUALITY_CHECK,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("BIOMETRIC_AUTHENTICATION")) 
			return getEventEnumWithValue(BIOMETRIC_AUTHENTICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("SECUREZONE_NOTIFICATION")) 
			return getEventEnumWithValue(SECUREZONE_NOTIFICATION,ps.getRegistrationId());
		if(ps.getTransactionTypeCode().equalsIgnoreCase("PRINT")) 
			return getEventEnumWithValue(PRINT,ps.getRegistrationId());
		return null;
		
	}
	
	public static EventEnum getEventEnumWithValue(EventEnum e,String s)
	{
		e.setDescription(String.format(e.getDescription(),s));
		if(e.getId().equalsIgnoreCase("%s"))
			e.setId(s);
		return e;
	}
	
	
}
