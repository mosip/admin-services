package io.mosip.kernel.syncdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicFieldValueDto {

	
	private String code;	
	private String value;	
	private boolean isActive;
}
