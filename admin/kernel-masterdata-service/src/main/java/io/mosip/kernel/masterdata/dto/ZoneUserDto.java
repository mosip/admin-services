package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;
@Data
public class ZoneUserDto {
	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String zoneCode;

	@NotNull
	@StringFormatter(min = 1, max = 256)
	private String userId;

	@Deprecated
	private String langCode;
	
	@NotNull
	private Boolean isActive;
}
