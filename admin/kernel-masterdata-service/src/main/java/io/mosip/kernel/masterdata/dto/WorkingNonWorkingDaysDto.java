package io.mosip.kernel.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkingNonWorkingDaysDto {

	// @NotNull
	boolean sun;
	// @NotNull
	boolean mon;
	// @NotNull
	boolean tue;
	// @NotNull
	boolean wed;
	// @NotNull
	boolean thu;
	// @NotNull
	boolean fri;
	// @NotNull
	boolean sat;

}
