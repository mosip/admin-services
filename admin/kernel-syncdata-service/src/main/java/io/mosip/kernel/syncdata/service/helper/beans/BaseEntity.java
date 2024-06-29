package io.mosip.kernel.syncdata.service.helper.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

	
	private Boolean isActive;

	
	private String createdBy;

	
	private LocalDateTime createdDateTime;

	
	private String updatedBy;

	
	private LocalDateTime updatedDateTime;

	
	private Boolean isDeleted;

	
	private LocalDateTime deletedDateTime;

}
