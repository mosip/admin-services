package io.mosip.kernel.masterdata.dto;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import lombok.Data;

@Data
public class RegCenterNonLanguageSpecificPutDto {

	@NotNull
	@NotBlank
	private String id;

	private Short numberOfKiosks;

	@Size(min = 0, max = 128)
	private String contactPerson;
	
	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String centerTypeCode;

	@NotNull
	@StringFormatter(min = 1, max = 32)
	private String latitude;

	@NotBlank
	@Size(min = 0, max = 32)
	private String longitude;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String locationCode;

	@NotBlank
	@Size(min = 0, max = 36)
	private String holidayLocationCode;

	@Size(min = 0, max = 16)
	private String contactPhone;

	@NotBlank
	@Size(min = 0, max = 32)
	private String workingHours;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime perKioskProcessTime;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime centerStartTime;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime centerEndTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime lunchStartTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime lunchEndTime;

	@Size(min = 0, max = 64)
	private String timeZone;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String zoneCode;

	private Map<String, Boolean> workingNonWorkingDays;

	private @Valid List<ExceptionalHolidayPutPostDto> exceptionalHolidayPutPostDto;

}