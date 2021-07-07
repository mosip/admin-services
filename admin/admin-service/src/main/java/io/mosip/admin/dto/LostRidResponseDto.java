package io.mosip.admin.dto;

import java.util.List;

import lombok.Data;

/**
 * The Class LostRidResponseDto.
 * 
 * @author Dhanendra
 *
 */
@Data
public class LostRidResponseDto {

	private static final long serialVersionUID = 4422198670538094471L;

	private List<LostRidDto> response;

	private List<ErrorDTO> errors;

}
