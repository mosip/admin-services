package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Model representing a ID schema request")
public class IdentitySchemaDto {
	
	@ApiModelProperty(notes = "Schema title", required = false)
	@NotBlank
	private String title;
	
	@ApiModelProperty(notes = "Schema description", required = false)
	@NotBlank
	private String description;
	
	@ApiModelProperty(notes = "schema", required = true)
	@NotEmpty
	private String schema;
}