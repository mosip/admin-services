package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;


@Data
public class RegCenterLanguageSpecificPutDto {

	@NotNull
	private String id;

	@NotNull
	@StringFormatter(min = 1, max = 128)
	private String name;

	@NotNull
	@StringFormatter(min = 1, max = 256)
	private String addressLine1;

	@Size(min = 0, max = 256)
	private String addressLine2;

	@Size(min = 0, max = 256)
	private String addressLine3;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	@Size(min = 0, max = 128)
	private String contactPerson;
}