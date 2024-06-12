package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;

@Data
public class UserDetailsPutReqDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String id;


	@Null
	@StringFormatter(min = 0, max = 64)
	private String name;

	@Null
	@StringFormatter(min = 0, max = 16)
	private String statusCode;

	@NotNull
	@StringFormatter(min = 1, max = 10)
	private String regCenterId;

	@Deprecated
	private Boolean isActive;

	
	private String langCode;
	
}
