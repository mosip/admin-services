package io.mosip.admin.bulkdataupload.service;

import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;

/**
 * The Interface BulkDataService
 * @author dhanendra
 *
 */

public interface BulkDataService {

	
	/**
	 * perfrom the bulk data operation
	 * 
	 * @param bulkDataRequestDto
	 * @return

	 */
	public BulkDataResponseDto bulkDataOperation(String tableName, String operation, String category,
            MultipartFile[] files, String centerId, String source, String process,
			 String supervisorStatus);
	
	
	/**
	 * Get the transcation Details bassed on id
	 * @param transcationId
	 * @return
	 */
	public BulkDataGetExtnDto getTrascationDetails(String transcationId);
	
	/**
	 * Get the all transcation Details
	 * @return
	 */
	public PageDto<BulkDataGetExtnDto> getAllTrascationDetails(int pageNumber, int pageSize, String sortBy,String orderBy, String category);
	
}
