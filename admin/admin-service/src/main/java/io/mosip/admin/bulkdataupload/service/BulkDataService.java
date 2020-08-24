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
import io.mosip.admin.bulkdataupload.dto.BulkDataRequestDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PacketResponseDto;
import io.mosip.admin.packetstatusupdater.dto.PacketStatusUpdateResponseDto;

/**
 * The Interface BulkDataService
 * @author dhanendra
 *
 */

public interface BulkDataService {

	/**
	 * insert the bulk data
	 * 
	 * @param bulkDataRequestDto
	 * @return
	 */
	public BulkDataResponseDto insertDataToCSVFile(String tableName,String operation,String category,MultipartFile[] files);
	
	/**
	 * update the bulk data
	 * 
	 * @param bulkDataRequestDto
	 * @return

	 */
	public BulkDataResponseDto bulkDataOperation(String tableName,String operation,String category,MultipartFile[] files);
	
	/**
	 * Delete the bulk data
	 * @param bulkDataRequestDto
	 * @return
	 *
	 */
	public BulkDataResponseDto deleteData(BulkDataRequestDto bulkDataRequestDto);
	
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
	public BulkDataGetResponseDto getAllTrascationDetails();
	
	public BulkDataResponseDto uploadPackets(MultipartFile[] files);
}
