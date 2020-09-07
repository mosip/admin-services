package io.mosip.admin.bulkdataupload.contoller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetResponseDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.packetstatusupdater.constant.AuditConstant;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.kernel.core.http.RequestWrapper;
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
	
	@PostMapping(value = { "/bulkupload" }, consumes = { "multipart/form-data" })
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataResponseDto> uploadData(@RequestParam("tableName") String tableName,@RequestParam("operation") String operation,@RequestParam("category") String category,
	         @RequestParam("files") MultipartFile[] files) {
		auditUtil.auditRequest(AuditConstant.BULKDATA_INSERT_API_CALLED,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_INSERT_API_CALLED,"ADM-2002");
		ResponseWrapper<BulkDataResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.bulkDataOperation(tableName,operation,category,files));
		auditUtil.auditRequest(AuditConstant.BULKDATA_INSERT_SUCCESS,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_INSERT_API_CALLED,"ADM-2003");
		return responseWrapper;
		
	}

	@GetMapping("/bulkupload/transcation/{transcationId}")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataGetExtnDto> getTranscationDetail(@PathVariable("transcationId") String transcationId) throws Exception {
		ResponseWrapper<BulkDataGetExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.getTrascationDetails(transcationId));
		return responseWrapper;
	}
	@GetMapping("/bulkupload/getAllTransactions")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<PageDto<BulkDataGetExtnDto>> getTranscationDetail(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page no for the requested data", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size for the requested data", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "createdDateTime") @ApiParam(value = "sort the requested data based on param value", defaultValue = "createdDateTime") String sortBy,
			@RequestParam(name = "category", defaultValue = "masterdata")  String category){
		ResponseWrapper<PageDto<BulkDataGetExtnDto>> responseWrapper = new ResponseWrapper<PageDto<BulkDataGetExtnDto>>();
		responseWrapper.setResponse(bulkDataService.getAllTrascationDetails(pageNumber,pageSize,sortBy,category));
		return responseWrapper;
	}
	
	
}
