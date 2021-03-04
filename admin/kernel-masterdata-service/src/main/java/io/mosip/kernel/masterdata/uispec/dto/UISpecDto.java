package io.mosip.kernel.masterdata.uispec.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.mosip.kernel.masterdata.dto.SchemaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Model representing a ui spec request")
public class UISpecDto {
	
	@ApiModelProperty(notes = "identityschemaid",required = true)
	@NotEmpty
	private String identityschemaid;
	
	@ApiModelProperty(notes = "domain to which spec is defining", required = true)
	@NotEmpty
	private String domain;
	
	@ApiModelProperty(notes = "UI Spec title", required = true)
	@NotBlank
	private String title;
	
	@ApiModelProperty(notes = "UI Spec description", required = true)
	@NotBlank
	private String description;
	
	@ApiModelProperty(notes = "UISpec", required = true)
	@NotEmpty
	private List<SchemaDto> jsonspec;
}