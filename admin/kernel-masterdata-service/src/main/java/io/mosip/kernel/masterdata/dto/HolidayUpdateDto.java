package io.mosip.kernel.masterdata.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayUpdateDto {
	
	private Integer holidayId;

	private String locationCode;

	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private LocalDate holidayDate;

	private String holidayName;

	private String holidayDesc;
	
	@Deprecated
	private Boolean isActive;
	
	private String langCode;


}
