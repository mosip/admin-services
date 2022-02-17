package io.mosip.kernel.masterdata.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.CharacterValidator;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

/**
 * Blocklisted word DTO.
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
public class BlocklistedWordsDto {
 
 
	@NotNull
	@StringFormatter(min = 1, max = 128)
	@CharacterValidator(message = "Blocklisted word cannot contain special characters")
	private String word;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	@Size(min = 0, max = 256)
	private String description;

	@NotNull
	private Boolean isActive;
}
