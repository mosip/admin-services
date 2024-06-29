package io.mosip.kernel.masterdata.dto;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
