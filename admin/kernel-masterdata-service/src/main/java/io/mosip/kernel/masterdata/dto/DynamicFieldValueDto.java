package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.json.JSONObject;

@Data
public class DynamicFieldValueDto {

	@NotBlank
	private String id;

	@NotBlank
	private JSONObject fieldVal;
}
