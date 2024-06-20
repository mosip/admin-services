package io.mosip.kernel.masterdata.dto.getresponse;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DynamicFieldResponseDto {
	
	private String id;	
	private String name;	
	private String langCode;	
	private String dataType;
	private String description;
	private JsonNode fieldVal;
	private Boolean isActive;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;

}
