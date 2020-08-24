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
	
	private UUID transcationId;
	
	private String tableName;
	
	private String category;
	
	private String operation;
	
	private int successCount;
	
	private String status;
	
	private String uploadedBy;
	
	private String timeStamp;
	
	private String logs;
	
}
