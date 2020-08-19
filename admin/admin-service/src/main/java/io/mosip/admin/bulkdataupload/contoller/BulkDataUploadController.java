package io.mosip.admin.bulkdataupload.contoller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.admin.bulkdataupload.dto.BulkDataGetExtnDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataGetResponseDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataRequestDto;
import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.service.BulkDataService;
import io.mosip.admin.packetstatusupdater.constant.AuditConstant;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;


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
	
	@PostMapping("/bulkupload")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataResponseDto> insertData(@RequestBody @Valid RequestWrapper<BulkDataRequestDto> bulkData) {
		auditUtil.auditRequest(AuditConstant.BULKDATA_INSERT_API_CALLED,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_INSERT_API_CALLED,"ADM-2002");
		ResponseWrapper<BulkDataResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.insertData(bulkData.getRequest()));
		auditUtil.auditRequest(AuditConstant.BULKDATA_INSERT_SUCCESS,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_INSERT_API_CALLED,"ADM-2003");
		return responseWrapper;
		
	}
	@GetMapping("/bulkupload/transcation/{transcationId}")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataGetExtnDto> getTranscationDetail(@PathVariable("transcationId") UUID transcationId) throws Exception {
		ResponseWrapper<BulkDataGetExtnDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.getTrascationDetails(transcationId));
		return responseWrapper;
		
	}
	@GetMapping("/bulkupload/getAllTransactions")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataGetResponseDto> getTranscationDetail() throws Exception {
		ResponseWrapper<BulkDataGetResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.getAllTrascationDetails());
		return responseWrapper;
		
	}
	@PutMapping("/bulkupload")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataResponseDto> updateData(@RequestBody @Valid RequestWrapper<BulkDataRequestDto> bulkData)  {
		auditUtil.auditRequest(AuditConstant.BULKDATA_UPDATE_API_CALLED,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_UPDATE_API_CALLED,"ADM-2004");
		ResponseWrapper<BulkDataResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.updateData(bulkData.getRequest()));
		auditUtil.auditRequest(AuditConstant.BULKDATA_UPDATE_SUCCESS,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_UPDATE_API_CALLED,"ADM-2005");
		return responseWrapper;
		
	}
	@DeleteMapping("/bulkupload")
	//@PreAuthorize("hasRole('MASTERDATA_ADMIN')")
	public ResponseWrapper<BulkDataResponseDto> deleteData(@RequestBody@Valid RequestWrapper<BulkDataRequestDto> bulkData) {
		auditUtil.auditRequest(AuditConstant.BULKDATA_DELETE_API_CALLED,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_DELETE_API_CALLED,"ADM-2006");
		ResponseWrapper<BulkDataResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(bulkDataService.deleteData(bulkData.getRequest()));
		auditUtil.auditRequest(AuditConstant.BULKDATA_DELETE_SUCCESS,AuditConstant.AUDIT_SYSTEM,AuditConstant.BULKDATA_DELETE_API_CALLED,"ADM-2007");
		return responseWrapper;
		
	}
	
	
}
