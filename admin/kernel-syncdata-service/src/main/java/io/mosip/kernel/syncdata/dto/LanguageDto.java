package io.mosip.kernel.syncdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data transfer object class for Language.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Language", description = "Language resource representation")
@JsonIgnoreProperties("langCode")
public class LanguageDto extends BaseDto {

	/**
	 * Field for language code
	 */
	@NotNull
	@Size(min = 1, max = 3)
	@ApiModelProperty(value = "Language Code", required = true, dataType = "java.lang.String")
	private String code;

	/**
	 * Field for language name
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@ApiModelProperty(value = "Language Name", required = true, dataType = "java.lang.String")
	private String name;

	/**
	 * Field for language family
	 */
	@Size(min = 0, max = 64)
	@ApiModelProperty(value = "Language Family", dataType = "java.lang.String")
	private String family;

	/**
	 * Field for language native name
	 */
	@Size(min = 0, max = 64)
	@ApiModelProperty(value = "Language Native Name", dataType = "java.lang.String")
	private String nativeName;

}
