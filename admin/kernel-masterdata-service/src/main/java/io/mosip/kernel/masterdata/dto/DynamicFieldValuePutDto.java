package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "code", "langCode" })

public class DynamicFieldValuePutDto {

	@NotBlank
	private String code;

	@NotBlank
	private String value;

	@Deprecated
	private Boolean isActive;

	@NotBlank
	private String langCode;
}
