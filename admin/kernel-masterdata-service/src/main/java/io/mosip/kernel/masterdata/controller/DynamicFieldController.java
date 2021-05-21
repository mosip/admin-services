package io.mosip.kernel.masterdata.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.OrderEnum;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldSearchResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DynamicFieldExtnDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.LocalDateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/dynamicfields")
@Api(tags = { "Dynamic Field" })
public class DynamicFieldController {
	
	@Autowired
	private DynamicFieldService dynamicFieldService;

	@Autowired
	LocalDateTimeUtil localDateTimeUtil;
	
	@Autowired
	AuditUtil auditUtil;
	
	@ResponseFilter
	@GetMapping
	@ApiOperation(value = "Service to fetch all dynamic fields")
	public ResponseWrapper<PageDto<DynamicFieldExtnDto>> getAllDynamicFields(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page number", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "name") @ApiParam(value = "sort on field name", defaultValue = "name") String sortBy,
			@RequestParam(name = "orderBy", defaultValue = "desc") @ApiParam(value = "sort order", defaultValue = "desc") OrderEnum orderBy,
			@RequestParam(name = "langCode", required = false) @ApiParam(value = "Lang Code", required = false) String langCode,
			@RequestParam(name = "lastUpdated", required = false) @ApiParam(value = "last updated rows", required = false) String lastUpdated) {
		ResponseWrapper<PageDto<DynamicFieldExtnDto>> responseWrapper = new ResponseWrapper<>();
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		responseWrapper.setResponse(dynamicFieldService.getAllDynamicField(pageNumber, pageSize, sortBy, orderBy.name(), langCode,
				timestamp, currentTimeStamp));
		return responseWrapper;
	}

	@ResponseFilter
	@GetMapping("/distinct")
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','Default')")
	@ApiOperation(value = "Service to fetch distinct dynamic fields")
	public ResponseWrapper<List<String>> getDistinctDynamicFields(){
		ResponseWrapper<List<String>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.getDistinctDynamicFields());
		return responseWrapper;
	}
	

	@ResponseFilter
	@PostMapping
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to create dynamic field")
	public ResponseWrapper<DynamicFieldResponseDto> createDynamicField (@Valid @RequestBody RequestWrapper<DynamicFieldDto> dynamicFieldDto) {
		ResponseWrapper<DynamicFieldResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.createDynamicField(dynamicFieldDto.getRequest()));
		return responseWrapper;
	}
	
	@ResponseFilter
	@PutMapping
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update dynamic field")
	public ResponseWrapper<DynamicFieldResponseDto> updateDynamicField (
			@RequestParam(name = "id") @ApiParam(value = "field id") String id,
			@Valid @RequestBody RequestWrapper<DynamicFieldPutDto> dynamicFieldDto) {
		ResponseWrapper<DynamicFieldResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.updateDynamicField(id, dynamicFieldDto.getRequest()));
		return responseWrapper;
	}

	
	@ResponseFilter
	@PatchMapping("/all")
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update dynamic field")
	public ResponseWrapper<StatusResponseDto> updateAllDynamicFieldStatus(@RequestParam boolean isActive,
			@RequestParam String fieldName) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(), "ADM-681");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.updateDynamicFieldStatus(fieldName, isActive));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				"ADM-682");
		return responseWrapper;
	}

	@ResponseFilter
	@PatchMapping
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update dynamic field")
	public ResponseWrapper<StatusResponseDto> updateDynamicFieldStatus(@RequestParam boolean isActive,
																	   @RequestParam String id) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(), "ADM-681");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.updateDynamicFieldValueStatus(id, isActive));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				"ADM-682");
		return responseWrapper;
	}

	@ResponseFilter
	@DeleteMapping("/all/{fieldName}")
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to delete dynamic field")
	public ResponseWrapper<StatusResponseDto> deleteAllDynamicField(@PathVariable("fieldName") String fieldName) {
		auditUtil.auditRequest(MasterDataConstant.DELETE_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.DELETE_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(), "ADM-681");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.deleteDynamicField(fieldName));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.DELETE_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.DELETE_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				"ADM-682");
		return responseWrapper;
	}

	@ResponseFilter
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to delete dynamic field")
	public ResponseWrapper<StatusResponseDto> deleteDynamicField(@PathVariable("id") String id)
	{
		auditUtil.auditRequest(MasterDataConstant.DELETE_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.DELETE_API_IS_CALLED + DynamicFieldDto.class.getCanonicalName(), "ADM-681");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.deleteDynamicFieldValue(id));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.DELETE_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.DELETE_SUCCESS, DynamicFieldDto.class.getCanonicalName()),
				"ADM-682");
		return responseWrapper;
	}

	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PostMapping(value = "/search")
	public ResponseWrapper<PageResponseDto<DynamicFieldSearchResponseDto>> searchDynamicFields(
			@RequestBody @Valid RequestWrapper<SearchDto> dto) {
		ResponseWrapper<PageResponseDto<DynamicFieldSearchResponseDto>> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(
				MasterDataConstant.SEARCH_API_IS_CALLED + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_API_IS_CALLED + SearchDto.class.getCanonicalName());
		responseWrapper.setResponse(dynamicFieldService.searchDynamicFields(dto.getRequest()));
		auditUtil.auditRequest(MasterDataConstant.SUCCESSFUL_SEARCH + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SUCCESSFUL_SEARCH + SearchDto.class.getCanonicalName());
		return responseWrapper;
	}

}
