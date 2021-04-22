package io.mosip.kernel.masterdata.dto.getresponse;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.mosip.kernel.masterdata.dto.DynamicFieldValueDto;
import lombok.Data;
import org.json.JSONObject;

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
