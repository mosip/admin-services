package io.mosip.kernel.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayDto {
	private String locationCode;

	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private LocalDate holidayDate;

	private String holidayName;

	private String holidayDesc;
	
	private String langCode;

	private Boolean isActive;
	
	private int holidayId;

}
