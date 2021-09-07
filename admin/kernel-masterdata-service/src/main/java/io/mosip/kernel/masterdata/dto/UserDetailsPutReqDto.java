package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;

@Data
public class UserDetailsPutReqDto {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String id;

	@Null
	@Size(min = 0, max = 28)
	private String uin;

	@Null
	@StringFormatter(min = 0, max = 64)
	private String name;

	@Null
	@StringFormatter(min = 0, max = 16)
	private String email;

	@Null
	@StringFormatter(min = 0, max = 16)
	private String mobile;

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
