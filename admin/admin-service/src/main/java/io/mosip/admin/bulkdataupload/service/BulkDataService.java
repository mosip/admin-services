package io.mosip.admin.bulkdataupload.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetResponseDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;

/**
 * The Interface BulkDataService
 * @author dhanendra
 *
 */

public interface BulkDataService {

	/**
	 * perfrom crud operation in the masterdata
	 * 
	 * @param bulkDataRequestDto
	 * @return
	 */
	public BulkDataResponseDto insertDataToCSVFile(String tableName,String operation,String category,MultipartFile[] files);
	
	/**
	 * perfrom the bulk data operation
	 * 
	 * @param bulkDataRequestDto
	 * @return

	 */
	public BulkDataResponseDto bulkDataOperation(String tableName,String operation,String category,MultipartFile[] files);
	
	
	/**
	 * Get the transcation Details bassed on id
	 * @param transcationId
	 * @return
	 */
	public BulkDataGetExtnDto getTrascationDetails(UUID transcationId);
	
	/**
	 * Get the all transcation Details
	 * @return
	 */
	public PageDto<BulkDataGetExtnDto> getAllTrascationDetails(int pageNumber, int pageSize, String sortBy, String category);
	
	/**
	 *  upload the packet
	 * @param files
	 * @param category
	 * @return
	 */

	public BulkDataResponseDto uploadPackets(MultipartFile[] files, String operation, String category);
}
