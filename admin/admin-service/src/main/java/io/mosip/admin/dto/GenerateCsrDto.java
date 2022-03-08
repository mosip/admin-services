package io.mosip.admin.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GenerateCsrDto {
	
	@NotNull
	@ApiModelProperty(value = "Application id", required = true, dataType = "java.lang.String")
	private String applicationId;
	
	@ApiModelProperty(value = "Reference id", required = false, dataType = "java.lang.String")
	private String referenceId;
	
	@ApiModelProperty(value = "Common name", required = false, dataType = "java.lang.String")
	private String commonName;
	
	@ApiModelProperty(value = "Organization", required = false, dataType = "java.lang.String")
	private String organization;
	
	@ApiModelProperty(value = "Organization unit", required = false, dataType = "java.lang.String")
	private String orgaizationUnit;
	
	@ApiModelProperty(value = "Location", required = false, dataType = "java.lang.String")
	private String location;
	
	@ApiModelProperty(value = "State", required = false, dataType = "java.lang.String")
	private String state;
	
	@ApiModelProperty(value = "Country", required = false, dataType = "java.lang.String")
	private String country;
	
	
}
