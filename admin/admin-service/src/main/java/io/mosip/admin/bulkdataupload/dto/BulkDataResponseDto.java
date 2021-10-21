package io.mosip.admin.bulkdataupload.dto;

import java.util.UUID;

import lombok.Data;
/**
 * 
 * @author dhanendra
 *
 */
@Data
public class BulkDataResponseDto {
	
	private String transcationId;
	
	private String tableName;
	
	private String operation;
	
	private int successCount;
	
	private String category;
	
	private String status;
	
	private String statusDescription;
	
	private String uploadedBy;
	
	private String timeStamp;
	
	private String logs;
	
}
