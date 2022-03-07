package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadCertificateDto {

	@NotNull
	@ApiModelProperty(value = "Application id", required = true, dataType = "java.lang.String")
	private String applicationId;
	
	private String referenceId;
	
	private String certificateData;
	
	
}
