package io.mosip.kernel.masterdata.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayIdDeleteDto {
	@NotBlank
	private String locationCode;
	@NotNull
	private LocalDate holidayDate;

}
