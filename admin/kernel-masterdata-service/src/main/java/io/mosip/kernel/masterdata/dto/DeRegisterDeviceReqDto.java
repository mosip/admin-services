package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class DeRegisterDeviceReqDto {
	@NotNull
	@Size(min=1,max=36)
	private String deviceCode;
	@NotNull
	private String env;
}
