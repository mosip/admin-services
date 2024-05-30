package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DeRegisterDevicePostDto {
	@NotBlank
	private String device;
}
