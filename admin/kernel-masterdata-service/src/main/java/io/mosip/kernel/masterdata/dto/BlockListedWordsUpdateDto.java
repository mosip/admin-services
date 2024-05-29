package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.AlphabeticValidator;
import io.mosip.kernel.masterdata.validator.CharacterValidator;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

@Data
public class BlockListedWordsUpdateDto {


	@NotNull
	@StringFormatter(min = 1, max = 128)
	@AlphabeticValidator(message = "Blocklisted word cannot contain numbers and special characters")
	private String word;


	@NotNull
	@StringFormatter(min = 1, max = 128)
	private String oldWord;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	@Deprecated
	private Boolean isActive;

	@Size(min = 0, max = 256)
	private String description;

}