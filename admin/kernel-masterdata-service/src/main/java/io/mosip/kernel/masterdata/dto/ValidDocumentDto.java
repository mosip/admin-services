package io.mosip.kernel.masterdata.dto;


import jakarta.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;

/**
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
@Data

public class ValidDocumentDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String docTypeCode;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String docCategoryCode;

	@Deprecated
	private String langCode;

	@NotNull
	private Boolean isActive;
}
