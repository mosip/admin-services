package io.mosip.admin.bulkdataupload.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.swagger.annotations.ApiParam;


/**
 * The class BulkDataUploadController
 * 
 * @author dhanendra
 *
 */


@RestController
public class BulkDataUploadController {

	@Autowired
	private AuditUtil auditUtil;
	
	@Autowired
	private BulkDataService bulkDataService;
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostbulkupload())")
	@PostMapping(value = { "/bulkupload" }, consumes = { "multipart/form-data" })
	//@PreAuthorize("hasRole('GLOBAL_ADMIN')")
	public ResponseWrapper<BulkDataResponseDto> uploadData(@RequestParam(value = "tableName", required = false, defaultValue = "") String tableName,
														   @RequestParam("operation") String operation,
														   @RequestParam("category") String category,
														   @RequestParam("files") MultipartFile[] files,
														   @RequestParam(value = "centerId", required = false) String centerId,
														   @RequestParam(value = "source", required = false, defaultValue = "REGISTRATION_CLIENT") String source,
														   @RequestParam(value = "process", required = false, defaultValue = "NEW") String process,
														   @RequestParam(value = "supervisorStatus", required = false, defaultValue = "APPROVED") String supervisorStatus) {
		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_UPLOAD_API_CALLED,null);
		ResponseWrapper<BulkDataResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.bulkDataOperation(tableName, operation, category, files, centerId, source,
				process, supervisorStatus));
		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_UPLOAD_SUCCESS,null);
		return responseWrapper;
		
	}
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetbulkuploadtranscationtranscationid())")
	@GetMapping("/bulkupload/transcation/{transcationId}")
	//@PreAuthorize("hasRole('GLOBAL_ADMIN')")
	public ResponseWrapper<BulkDataGetExtnDto> getTranscationDetail(@PathVariable("transcationId") String transcationId) throws Exception {
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_TRANSACTION,transcationId),null);
		ResponseWrapper<BulkDataGetExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.getTrascationDetails(transcationId));
		auditUtil.setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_TRANSACTION_SUCCESS,transcationId),null);
		return responseWrapper;
	}
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetbulkuploadgetalltransactions())")
	@GetMapping("/bulkupload/getAllTransactions")
	//@PreAuthorize("hasRole('GLOBAL_ADMIN')")
	public ResponseWrapper<PageDto<BulkDataGetExtnDto>> getTranscationDetail(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page no for the requested data", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size for the requested data", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "createdDateTime") @ApiParam(value = "sort the requested data based on param value", defaultValue = "createdDateTime") String sortBy,
			@RequestParam(name = "orderBy", defaultValue = "desc") @ApiParam(value = "order the requested data based on param", defaultValue = "desc") String orderBy,
			@RequestParam(name = "category", defaultValue = "masterdata")  String category){
		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_TRANSACTION_ALL,null);
		ResponseWrapper<PageDto<BulkDataGetExtnDto>> responseWrapper = new ResponseWrapper<PageDto<BulkDataGetExtnDto>>();
		responseWrapper.setResponse(bulkDataService.getAllTrascationDetails(pageNumber,pageSize,sortBy,orderBy,category));
		auditUtil.setAuditRequestDto(EventEnum.BULKDATA_TRANSACTION_ALL_SUCCESS,null);
		return responseWrapper;
	}
	
	
}
