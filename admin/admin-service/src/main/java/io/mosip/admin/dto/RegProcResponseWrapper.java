package io.mosip.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegProcResponseWrapper<T> {

	private String id;

	private String version;

	@ApiModelProperty(notes = "Request Timestamp", example = "2018-12-10T06:12:52.994Z", required = true)
	private LocalDateTime responsetime;

	@Valid
	private T response;

	private List<ErrorDTO> errors;

}
