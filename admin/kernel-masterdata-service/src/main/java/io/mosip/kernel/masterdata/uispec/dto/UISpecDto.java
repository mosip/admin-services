package io.mosip.kernel.masterdata.uispec.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Model representing a ui spec request")
public class UISpecDto {
	
	@ApiModelProperty(notes = "identitySchemaId",required = true)
	@NotEmpty
	private String identitySchemaId;
	
	@ApiModelProperty(notes = "domain to which spec is defining", required = true)
	@NotEmpty
	private String domain;

	@ApiModelProperty(notes = "UI Spec type", required = true)
	@NotBlank
	private String type;
	
	@ApiModelProperty(notes = "UI Spec title", required = true)
	@NotBlank
	private String title;
	
	@ApiModelProperty(notes = "UI Spec description", required = true)
	@NotBlank
	private String description;
	
	@ApiModelProperty(notes = "UISpec", required = true)
	@NotBlank
	private JsonNode jsonspec;
	
}