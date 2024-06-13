package io.mosip.admin.dto;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RegProcRequestWrapper<T> {

	private String id;

	private String version;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private String requesttime;

	@Valid
	private T request;

}
