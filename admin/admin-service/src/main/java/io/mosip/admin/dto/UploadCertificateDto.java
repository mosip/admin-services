package io.mosip.admin.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadCertificateDto {

	@NotNull
	@ApiModelProperty(value = "Application id", required = true, dataType = "java.lang.String")
	private String applicationId;
	
	private String referenceId;
	
	private String certificateData;
	
	
}
