package io.mosip.kernel.masterdata.dto;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data

public class PostReasonCategoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -845601642085487726L;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String code;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String name;

	@NotNull
	@StringFormatter(min = 1, max = 128)
	private String description;

	@ValidLangCode
	private String langCode;

	@NotNull
	private Boolean isActive;

}
