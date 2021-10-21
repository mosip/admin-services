package io.mosip.kernel.syncdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BiometricAttribute", description = "BiometricAttribute resource representation")
public class BiometricAttributeDto extends BaseDto {
	/**
	 * Field for code
	 */
	@NotNull
	@Size(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;
	/**
	 * Field for name
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	/**
	 * Field for description
	 */
	@Size(min = 0, max = 128)
	@ApiModelProperty(value = "Biometric Attribute desc", required = false, dataType = "java.lang.String")
	private String description;
	/**
	 * Field for biometricTypecode
	 */
	@NotNull
	@Size(min = 1, max = 36)
	@ApiModelProperty(value = "Biometric Type code", required = true, dataType = "java.lang.String")
	private String biometricTypeCode;

}
