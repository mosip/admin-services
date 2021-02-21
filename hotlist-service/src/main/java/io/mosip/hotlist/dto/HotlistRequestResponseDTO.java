package io.mosip.hotlist.dto;

import java.time.LocalDateTime;

import io.mosip.kernel.core.exception.ServiceError;
import lombok.Data;

@Data
public class HotlistRequestResponseDTO {

	private String id;
	
	private String idType;
	
	private String status;
	
	private LocalDateTime expiryTimestamp;
	
	private ServiceError error;
}
