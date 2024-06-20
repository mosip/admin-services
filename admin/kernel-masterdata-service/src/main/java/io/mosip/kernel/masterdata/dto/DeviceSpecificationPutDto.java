package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceSpecificationPutDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
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
	@ApiModelProperty(value = "deviceTypeCode", required = true, dataType = "java.lang.String")
	private String deviceTypeCode;


	@NotNull
	@StringFormatter(min = 1, max = 16)
	@ApiModelProperty(value = "minDriverversion", required = true, dataType = "java.lang.String")
	private String minDriverversion;

	@Size(min = 0, max = 256)
	@ApiModelProperty(value = "description", required = true, dataType = "java.lang.String")
	private String description;

	@Deprecated
	private String langCode;

	@Deprecated
	private Boolean isActive;


}
