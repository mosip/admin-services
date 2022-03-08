package io.mosip.admin.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadCertificateResponseDto {
	
	private String status;
	
	private LocalDateTime timestamp;
	
	

}
