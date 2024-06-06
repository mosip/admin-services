package io.mosip.kernel.masterdata.dto;

import jakarta.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegProcRequestWrapper<T> {

	private String id;

	private String version;

	@ApiModelProperty(notes = "Request Timestamp", example = "2018-12-10T06:12:52.994Z", required = true)
	private String requesttime;

	@Valid
	private T request;

}
