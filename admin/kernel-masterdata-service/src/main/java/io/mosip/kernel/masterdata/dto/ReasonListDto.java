package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

@Data
public class ReasonListDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String name;

	@Size(min = 1, max = 128)
	private String description;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String rsnCatCode;

	@ValidLangCode
	private String langCode;

	@NotNull
	private Boolean isActive;

}
