package io.mosip.kernel.masterdata.dto.getresponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;
/**
 * 
 * @author dhanendra
 *
 */
@Data
public class ValidDocumentMapDto {
	
	@NotBlank
	@Size(min = 1, max = 36)
	private String docTypeCode;

	@NotBlank
	@Size(min = 1, max = 36)
	private String docCategoryCode;
	
	@NotBlank
	@Size(min = 1, max = 36)
	private String docTypeName;

	@ValidLangCode(message = "Language Code is Invalid")
	// @NotBlank
	// @Size(min = 1, max = 3)
	private String langCode;

	@NotNull
	private Boolean isActive;

}
