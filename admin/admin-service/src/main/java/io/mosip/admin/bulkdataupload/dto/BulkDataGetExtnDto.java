package io.mosip.admin.bulkdataupload.dto;

import java.util.UUID;

import lombok.Data;
/**
 * 
 * @author dhanendra
 *
 */
@Data
public class BulkDataGetExtnDto {

    private UUID transcationId;
	
	private String tableName;
	
	private String operation;
	
	private String category;
	
	private int count;
	
	private String status;
	
	private String statusDescription;
	
	private String uploadedBy;
	
	private String timeStamp;
	
	private String logs;
}
