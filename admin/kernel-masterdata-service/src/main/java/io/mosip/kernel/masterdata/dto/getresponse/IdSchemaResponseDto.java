package io.mosip.kernel.masterdata.dto.getresponse;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class IdSchemaResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Map<String, Object> jsonData = new HashMap<>();
	
	private String id;	
	private double idVersion;
	private String title;
	private String description;
	private String schemaJson;	
	private String status;
	private LocalDateTime effectiveFrom;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	
}
