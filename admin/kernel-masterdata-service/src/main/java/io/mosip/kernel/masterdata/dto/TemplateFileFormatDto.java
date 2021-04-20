package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TemplateFileFormatDto {
	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 256)
	@ApiModelProperty(value = "TemplateFileFormat description", required = false, dataType = "java.lang.String")
	private String description;


	@Deprecated
	private String langCode;

	@NotNull
	@ApiModelProperty(value = "TemplateFileFormat isActive status", required = true, dataType = "java.lang.Boolean")
	private Boolean isActive;
}
