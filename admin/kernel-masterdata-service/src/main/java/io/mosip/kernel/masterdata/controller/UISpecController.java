package io.mosip.kernel.masterdata.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import io.mosip.kernel.masterdata.constant.OrderEnum;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecPublishDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author Nagarjuna
 *
 */

@RestController
@RequestMapping(value = "/uispec")
@Api(tags = "UI Specification")
public class UISpecController {

	@Autowired
	UISpecService uiSpecService;

	@ResponseFilter
	@PostMapping
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to define ui specification")
	public ResponseWrapper<UISpecResponseDto> defineUISpec(@Valid @RequestBody RequestWrapper<UISpecDto> request) {
		ResponseWrapper<UISpecResponseDto> response = new ResponseWrapper<UISpecResponseDto>();
		response.setResponse(uiSpecService.defineUISpec(request.getRequest()));
		return response;
	}

	@ResponseFilter
	@PutMapping
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to update ui specification")
	public ResponseWrapper<UISpecResponseDto> updateUISpec(
			@RequestParam(name = "id", required = true) @ApiParam(value = "uispec id") String id,
			@Valid @RequestBody RequestWrapper<UISpecDto> request) {
		ResponseWrapper<UISpecResponseDto> response = new ResponseWrapper<UISpecResponseDto>();
		response.setResponse(uiSpecService.updateUISpec(id, request.getRequest()));
		return response;
	}

	@ResponseFilter
	@PutMapping("/publish")
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to publish draftted ui specification")
	public ResponseWrapper<String> publishUISpec(@Valid @RequestBody RequestWrapper<UISpecPublishDto> request) {
		ResponseWrapper<String> response = new ResponseWrapper<String>();
		response.setResponse(uiSpecService.publishUISpec(request.getRequest()));
		return response;
	}

	@ResponseFilter
	@DeleteMapping
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to delete draftted ui specification")
	public ResponseWrapper<String> deleteUISpec(
			@RequestParam(name = "id", required = true) @ApiParam(value = "uispec id") String id) {
		ResponseWrapper<String> response = new ResponseWrapper<String>();
		response.setResponse(uiSpecService.deleteUISpec(id));
		return response;
	}

	@ResponseFilter
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to fetch all ui specifications")
	public ResponseWrapper<PageDto<UISpecResponseDto>> getAllUISpecs(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page number", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "cr_dtimes") @ApiParam(value = "sort on field name", defaultValue = "cr_dtimes") String sortBy,
			@RequestParam(name = "orderBy", defaultValue = "desc") @ApiParam(value = "sort order", defaultValue = "desc") OrderEnum orderBy) {
		ResponseWrapper<PageDto<UISpecResponseDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(uiSpecService.getAllUISpecs(pageNumber, pageSize, sortBy, orderBy.name()));
		return responseWrapper;
	}

	@ResponseFilter
	@GetMapping("/{domain}/latest")
	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','REGISTRATION_CLIENT','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_PROCESSOR','RESIDENT','PRE_REGISTRATION_ADMIN')")
	@ApiOperation(value = "Service to fetch latest published ui specification of a domain")
	public ResponseWrapper<List<UISpecResponseDto>> getLatestPublishedSchema(@PathVariable String domain,
			@RequestParam(name = "version", defaultValue = "0", required = false) @ApiParam(value = "version", defaultValue = "0") double version,
			@RequestParam(name = "type", required = false) @ApiParam(value = "typeof the ui spec. Supported comma separted values") String type,
			@RequestParam(name = "identitySchemaVersion", defaultValue = "0", required = false) @ApiParam(value = "version", defaultValue = "0") double identitySchemaVersion) {
		ResponseWrapper<List<UISpecResponseDto>> response = new ResponseWrapper<List<UISpecResponseDto>>();
		response.setResponse(uiSpecService.getLatestPublishedUISpec(domain, version, type, identitySchemaVersion));
		return response;
	}
}