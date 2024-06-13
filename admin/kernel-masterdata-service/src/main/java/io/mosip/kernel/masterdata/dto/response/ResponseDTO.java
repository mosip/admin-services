package io.mosip.kernel.masterdata.dto.response;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * 
 * @author Bal Vikash Sharma
 *
 */
@Data
public class ResponseDTO {

	@NotNull
	private ApplicantTypeCodeDTO applicantType;

}