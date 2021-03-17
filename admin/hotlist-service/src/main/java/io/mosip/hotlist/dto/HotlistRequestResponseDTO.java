package io.mosip.hotlist.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author Manoj SP
 *
 */
@Data
public class HotlistRequestResponseDTO {

	private String id;
	
	private String idType;
	
	private String status;
	
	private LocalDateTime expiryTimestamp;
}
