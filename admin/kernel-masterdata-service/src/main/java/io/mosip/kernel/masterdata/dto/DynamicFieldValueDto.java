package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.json.JSONObject;

@Data
public class DynamicFieldValueDto {

	@NotBlank
	private String id;

	@NotBlank
	private JSONObject fieldVal;
}
