package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.mosip.kernel.masterdata.validator.CharacterValidator;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DTO class for document type.
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
// @ApiModel(value = "DocumentCType", description = "DocumentType resource
// representation")
public class DocumentTypePutReqDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	@CharacterValidator(message = "Document type code cannot have special characters")
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	@Size(max = 128)
	@ApiModelProperty(value = "Application description", required = false, dataType = "java.lang.String")
	private String description;

	@Deprecated
	private Boolean isActive;

	@ValidLangCode(message = "Language Code is Invalid")
	@ApiModelProperty(value = "Language Code", required = true, dataType = "java.lang.String")
	private String langCode;


}