package io.mosip.kernel.syncdata.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenderDto extends BaseDto {

	@NotNull
	@Size(min = 1, max = 16)
	private String code;

	@NotNull
	@Size(min = 1, max = 64)
	private String genderName;

}
