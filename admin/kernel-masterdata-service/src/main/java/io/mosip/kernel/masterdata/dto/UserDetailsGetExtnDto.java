package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

@Data
public class UserDetailsGetExtnDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String id;

	@Deprecated
	@Null
	private String langCode;


	@Null
	@StringFormatter(min = 0, max = 64)
	private String name;


	@Null
	@StringFormatter(min = 0, max = 16)
	private String statusCode;

	@NotNull
	@StringFormatter(min = 1, max = 10)
	private String regCenterId;

	@NotNull
	private Boolean isActive;

	private String zoneCode;
}
