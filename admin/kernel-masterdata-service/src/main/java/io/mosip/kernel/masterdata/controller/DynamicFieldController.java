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
import io.mosip.kernel.masterdata.dto.DynamicFieldConsolidateResponseDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldDefDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
import io.mosip.kernel.masterdata.dto.MissingDataDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldSearchResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DynamicFieldExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.service.GenericService;
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
	private LocalDateTimeUtil localDateTimeUtil;
	
	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private GenericService genericService;
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdynamicfields())")
	@GetMapping
	@ApiOperation(value = "Service to fetch all dynamic fields")
	public ResponseWrapper<PageDto<DynamicFieldExtnDto>> getAllDynamicFields(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page number, sorted based on name", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size", defaultValue = "10") int pageSize,
			@RequestParam(name = "langCode", required = false) @ApiParam(value = "Lang Code", required = false) String langCode,
			@RequestParam(name = "lastUpdated", required = false) @ApiParam(value = "last updated rows", required = false) String lastUpdated) {
		ResponseWrapper<PageDto<DynamicFieldExtnDto>> responseWrapper = new ResponseWrapper<>();
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		//We only sort by name as internally pagination is applied on distinct name, hence sorting is currently
		// restricted to only name field
		responseWrapper.setResponse(dynamicFieldService.getAllDynamicField(pageNumber, pageSize, "name", "asc", langCode,
				timestamp, currentTimeStamp));
		return responseWrapper;
	}


	@ResponseFilter
	@GetMapping("/{fieldName}/{langCode}")
	@ApiOperation(value = "Service to fetch  dynamic field based on langcode and field name")
	public ResponseWrapper<DynamicFieldConsolidateResponseDto> getDynamicFieldByName(@PathVariable("fieldName") String fieldName,@PathVariable("langCode") String langCode,@RequestParam(name = "withValue",defaultValue = "false",required = false) boolean withValue){
		ResponseWrapper<DynamicFieldConsolidateResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.getDynamicFieldByNameAndLangcode(fieldName,langCode,withValue));
		return responseWrapper;
	}

	@ResponseFilter
	@GetMapping("/{fieldName}")
	@ApiOperation(value = " Service to fetch one dynamic field in all the languages")
	public ResponseWrapper<List<DynamicFieldExtnDto>> getAllDynamicFieldByName(
			@PathVariable("fieldName") String fieldName){
		ResponseWrapper<List<DynamicFieldExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.getAllDynamicFieldByName(fieldName));
		return responseWrapper;
	}
	
	
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdistinct())")
	@GetMapping("/distinct")
	@ApiOperation(value = "Service to fetch distinct dynamic fields")
	public ResponseWrapper<List<String>> getDistinctDynamicFields(){
		ResponseWrapper<List<String>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.getDistinctDynamicFields());
		return responseWrapper;
	}

	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdistinct())")
	@GetMapping("/distinct/{langCode}")
	@ApiOperation(value = "Service to fetch distinct dynamic fields")
	public ResponseWrapper<List<DynamicFieldDefDto>> getDistinctDynamicFieldsBasedOnLang(@PathVariable("langCode") String langCode){
		ResponseWrapper<List<DynamicFieldDefDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.getDistinctDynamicFields(langCode));
		return responseWrapper;
	}
	

	@ResponseFilter
	@PostMapping
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdynamicfields())")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to create dynamic field")
	public ResponseWrapper<DynamicFieldResponseDto> createDynamicField (@Valid @RequestBody RequestWrapper<DynamicFieldDto> dynamicFieldDto) {
		ResponseWrapper<DynamicFieldResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.createDynamicField(dynamicFieldDto.getRequest()));
		return responseWrapper;
	}
	
	@ResponseFilter
	@PutMapping
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPutdynamicfields())")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update dynamic field")
	public ResponseWrapper<DynamicFieldResponseDto> updateDynamicField (
			@RequestParam(name = "id") @ApiParam(value = "field id") String id,
			@Valid @RequestBody RequestWrapper<DynamicFieldPutDto> dynamicFieldDto) {
		ResponseWrapper<DynamicFieldResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.updateDynamicField(id, dynamicFieldDto.getRequest()));
		return responseWrapper;
	}

	
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchdynamicfieldsall())")
	@PatchMapping("/all")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchdynamicfields())")
	@PatchMapping
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeletedynamicfields())")
	@DeleteMapping("/all/{fieldName}")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeletedynamicfieldsid())")
	@DeleteMapping("/{id}")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
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

	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdynamicfieldssearch())")
	@PostMapping(value = "/search")
	public ResponseWrapper<PageResponseDto<DynamicFieldSearchResponseDto>> searchDynamicFields(
			@RequestBody @Valid RequestWrapper<SearchDto> dto) {
		ResponseWrapper<PageResponseDto<DynamicFieldSearchResponseDto>> responseWrapper = new ResponseWrapper<>();
		auditUtil.auditRequest(
				MasterDataConstant.SEARCH_API_IS_CALLED + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_API_IS_CALLED + SearchDto.class.getCanonicalName(),"ADM-904");
		responseWrapper.setResponse(dynamicFieldService.searchDynamicFields(dto.getRequest()));
		auditUtil.auditRequest(MasterDataConstant.SUCCESSFUL_SEARCH + SearchDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SUCCESSFUL_SEARCH + SearchDto.class.getCanonicalName(),"ADM-905");
		return responseWrapper;
	}

	/**
	 * Function to fetch missing ids/codes in the provided language code
	 *
	 * @return List<String> list of missing ids/ codes
	 */
	@ResponseFilter
	@GetMapping("/missingids/{langcode}")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdynamicfieldmissingidslangcode())")
	public ResponseWrapper<List<MissingDataDto>> getMissingDynamicFields(
			@PathVariable("langcode") String langCode, @RequestParam(required = false) String fieldName) {
		ResponseWrapper<List<MissingDataDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(genericService.getMissingDynamicData(langCode, fieldName));
		return responseWrapper;
	}

	/**
	 * Api to filter dynamic field based on column and type provided.
	 *
	 * @param request the request DTO.
	 * @return the {@link FilterResponseDto}.
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdynamicfieldsfiltervalues())")
	@PostMapping("/filtervalues")
	public ResponseWrapper<FilterResponseCodeDto> dynamicFieldFilterValues(
			@RequestBody @Valid RequestWrapper<FilterValueDto> request) {
		auditUtil.auditRequest(
				String.format(MasterDataConstant.FILTER_API_IS_CALLED , DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.FILTER_API_IS_CALLED , DynamicFieldDto.class.getCanonicalName()), "ADM-671");
		ResponseWrapper<FilterResponseCodeDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dynamicFieldService.dynamicfieldFilterValues(request.getRequest()));
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_FILTER , DynamicFieldDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_FILTER_DESC , DynamicFieldDto.class.getCanonicalName()),
				"ADM-672");
		return responseWrapper;
	}

	
	

}
