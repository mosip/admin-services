package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
