package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

/**
 * 
 * @author Megha Tanga
 *
 */
@Data
public class RegistrationCenterUserDto {

	@NotNull
	@StringFormatter(min = 1, max = 10)
	private String regCenterId;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String userId;

	@Deprecated
	private String langCode;

	@NotNull
	private Boolean isActive;

}
