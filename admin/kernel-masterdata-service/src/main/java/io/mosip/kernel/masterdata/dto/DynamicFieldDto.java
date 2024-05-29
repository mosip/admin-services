package io.mosip.kernel.masterdata.dto;

import java.util.List;

import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import io.mosip.kernel.masterdata.validator.AlphabeticValidator;
import io.mosip.kernel.masterdata.validator.DynamicFieldValidator;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;


/**
 * 
 * @author anusha
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model representing a dynamic field Request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicFieldDto {
	
	@ApiModelProperty(notes = "Field name", example = "any name", required = true)
	@NotBlank
	@Size(min = 2, max = 20)
	@AlphabeticValidator(message = "name cannot contain numbers and special characters")
	private String name;
	
	@ApiModelProperty(notes = "Language Code", example = "eng", required = true)
	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;
	
	@NotBlank
	@ApiModelProperty(notes = "Data Type", example = "string", required = true)
	@Size(min = 3, max = 20)
	private String dataType;
	
	@NotBlank
	private String description;

	@DynamicFieldValidator
	private JsonNode fieldVal;
}
