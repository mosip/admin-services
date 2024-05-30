package io.mosip.kernel.masterdata.uispec.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UISpecPublishDto {
	
	@NotNull
	private String id;
	
	@NotNull
	private LocalDateTime effectiveFrom;

}
