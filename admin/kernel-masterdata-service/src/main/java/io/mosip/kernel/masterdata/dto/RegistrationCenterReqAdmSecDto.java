package io.mosip.kernel.masterdata.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegistrationCenterReqAdmSecDto {

	@NotBlank
	List<RegCenterPostReqDto> registrationCenterDtos;

}
