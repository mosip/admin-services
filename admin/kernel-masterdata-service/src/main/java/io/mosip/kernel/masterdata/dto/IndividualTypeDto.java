package io.mosip.kernel.masterdata.dto;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * Instantiates a new individual type dto.
 */
@Data
public class IndividualTypeDto {

	/** The code. */
	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;

	/** The lang code. */
	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	/** The name. */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	/** The is active. */
	@NotNull
	@ApiModelProperty(value = "Application isActive Status", required = true, dataType = "java.lang.Boolean")
	private Boolean isActive;

}
