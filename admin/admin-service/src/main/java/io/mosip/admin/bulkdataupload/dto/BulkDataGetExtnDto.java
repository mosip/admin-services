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

    private String transcationId;
	
	private String entityName;
	
	private String operation;
	
	private int count;
	
	private String category;
	
	private String status;
	
	private String statusDescription;
	
	private String uploadedBy;
	
	private String timeStamp;
	
	private String logs;
}
