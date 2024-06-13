package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TemplateFileFormatPutDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 256)
	@ApiModelProperty(value = "TemplateFileFormat description", required = false, dataType = "java.lang.String")
	private String description;

	@Deprecated
	private Boolean isActive;

	@ValidLangCode(message = "Language Code is Invalid")
	@ApiModelProperty(value = "Language code", required = true, dataType = "java.lang.String")
	private String langCode;
}
