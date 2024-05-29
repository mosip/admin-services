package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author Megha Tanga
 * @version 1.0.0
 */
@Data

public class ModuleDto {

	/**
	 * Field for code
	 */
	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "id", required = true, dataType = "java.lang.String")
	private String id;

	/**
	 * Field for module name
	 */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	/**
	 * Field for language code
	 */
	@ValidLangCode(message = "Language Code is Invalid")
	@ApiModelProperty(value = "Language Code", required = true, dataType = "java.lang.String")
	private String langCode;

	/**
	 * Field for description
	 */
	@Size(min = 0, max = 256)
	@ApiModelProperty(value = "module desc", required = false, dataType = "java.lang.String")
	private String description;

	/**
	 * Field for the status of data.
	 */
	@ApiModelProperty(value = "module isActive status", required = true, dataType = "java.lang.Boolean")
	@NotNull
	private Boolean isActive;

}
