package io.mosip.admin.bulkdataupload.dto;

import java.util.List;

import lombok.Data;
/**
 * 
 * @author dhanendra
 *
 */
@Data
public class BulkDataGetResponseDto {

	private List<BulkDataGetExtnDto> records;
}
