package io.mosip.kernel.masterdata.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReasonCategoryDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String name;

	@Size(min = 1, max = 128)
	private String description;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	private Boolean isActive;

	private List<ReasonListDto> reasonList = new ArrayList<>();
}
