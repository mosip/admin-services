package io.mosip.admin.bulkdataupload.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author dhanendra
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkDataRequestDto {

	@NotNull
	private String tableName;
	
	@NotNull
	private String operation;
	
	@NotNull
	private String csvFile;	
	
}
