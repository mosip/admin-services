package io.mosip.kernel.masterdata.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

public class TemplateTypeDto {

	/**
	 * Field for code
	 */
	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;

	/**
	 * Field for language code
	 */
	@Deprecated
	private String langCode;
	/**
	 * Field for description
	 */
	@Size(min = 0, max = 256)
	@ApiModelProperty(value = "Template Type desc", required = false, dataType = "java.lang.String")
	private String description;

	/**
	 * Field for the status of data.
	 */
	@ApiModelProperty(value = "Template Type  isActive status", required = true, dataType = "java.lang.Boolean")
	@NotNull
	private Boolean isActive;

}
