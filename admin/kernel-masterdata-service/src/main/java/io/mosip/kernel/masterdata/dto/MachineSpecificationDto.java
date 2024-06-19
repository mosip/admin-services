package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response dto for Machine History Detail
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Data
public class MachineSpecificationDto {

	@ApiModelProperty(value = "id", required = true, dataType = "java.lang.String")
	private String id;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	@NotNull
	@StringFormatter(min = 1, max = 32)
	@ApiModelProperty(value = "brand", required = true, dataType = "java.lang.String")
	private String brand;

	@NotNull
	@StringFormatter(min = 1, max = 16)
	@ApiModelProperty(value = "model", required = true, dataType = "java.lang.String")
	private String model;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "machineTypeCode", required = true, dataType = "java.lang.String")
	private String machineTypeCode;

	@NotNull
	@StringFormatter(min = 1, max = 16)
	@ApiModelProperty(value = "minDriverversion", required = true, dataType = "java.lang.String")
	private String minDriverversion;

	@Size(min = 0, max = 256)
	@ApiModelProperty(value = "description", required = true, dataType = "java.lang.String")
	private String description;

	@Deprecated
	private String langCode;

	@NotNull
	@ApiModelProperty(value = "isActive", required = true, dataType = "java.lang.Boolean")
	private Boolean isActive;

}
