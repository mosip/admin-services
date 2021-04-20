package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;

@Data
public class ZoneUserPutDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String zoneCode;

	@NotNull
	@StringFormatter(min = 1, max = 256)
	private String userId;

	@Deprecated
	private boolean isActive;

	@NotNull
	@StringFormatter(min = 1, max = 3)
	private String langCode;

}
