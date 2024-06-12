package io.mosip.kernel.masterdata.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import io.mosip.kernel.masterdata.dto.TemplateFileFormatDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatPutDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.service.TemplateFileFormatService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class to fetch or create TemplateFileFormat.
 * 
 * @author Neha
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@RestController
@RequestMapping("/templatefileformats")
@Api(tags = { "TemplateFileFormat" })
public class TemplateFileFormatController {

	@Autowired
	private TemplateFileFormatService templateFileFormatService;
	
	@Autowired
	private AuditUtil auditUtil;

	/**
	 * API to create a templatefileformat
	 * 
	 * @param templateFileFormatRequestDto
	 *            {@link TemplateFileFormatDto} instance
	 * 
	 * @return {@link CodeAndLanguageCodeID}
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPosttemplatefileformats())")
	@PostMapping
	public ResponseWrapper<CodeAndLanguageCodeID> createTemplateFileFormat(
			@Valid @RequestBody RequestWrapper<TemplateFileFormatDto> templateFileFormatRequestDto) {

		ResponseWrapper<CodeAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(
				templateFileFormatService.createTemplateFileFormat(templateFileFormatRequestDto.getRequest()));
		return responseWrapper;
	}

	/**
	 * API to update an existing row of Templatefileformat data
	 * 
	 * @param templateFileFormatRequestDto
	 *            input parameter templateFileFormatRequestDto
	 * 
	 * @return ResponseEntity TemplateFileFormat Code and LangCode which is updated
	 *         successfully {@link ResponseEntity}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPuttemplatefileformats())")
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PutMapping
	@ApiOperation(value = "Service to update TemplateFileFormat", notes = "Update TemplateFileFormat and return TemplateFileFormat id")
	@ApiResponses({ @ApiResponse(code = 200, message = "When TemplateFileFormat updated successfully"),
			@ApiResponse(code = 400, message = "When Request body passed  is null or invalid"),
			@ApiResponse(code = 404, message = "When TemplateFileFormat is not found"),
			@ApiResponse(code = 500, message = "While updating TemplateFileFormat any error occured") })
	public ResponseWrapper<CodeAndLanguageCodeID> updateDevice(
			@Valid @RequestBody RequestWrapper<TemplateFileFormatPutDto> templateFileFormatRequestDto) {

		ResponseWrapper<CodeAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(
				templateFileFormatService.updateTemplateFileFormat(templateFileFormatRequestDto.getRequest()));
		return responseWrapper;
	}

	/**
	 * Api to delete TemplateFileFormat
	 * 
	 * @param code
	 *            the TemplateFileFormat code
	 * @return the code of templatefileformat
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeletetemplatefileformatscode())")
	@DeleteMapping("/{code}")
	@ApiOperation(value = "Service to delete TemplateFileFormat", notes = "Delete TemplateFileFormat and return code")
	@ApiResponses({ @ApiResponse(code = 200, message = "When TemplateFileFormat successfully deleted"),
			@ApiResponse(code = 400, message = "When path is invalid"),
			@ApiResponse(code = 404, message = "When No document category found"),
			@ApiResponse(code = 500, message = "While deleting document category any error occured") })
	public ResponseWrapper<CodeResponseDto> deleteDocumentCategory(@PathVariable("code") String code) {

		ResponseWrapper<CodeResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(templateFileFormatService.deleteTemplateFileFormat(code));
		return responseWrapper;
	}

	/**
	 * 
	 * Function to fetch TemplateFileFormat detail based on given TemplateFileFormat
	 * code.
	 * 
	 * @param TemplateFileFormat
	 *            code pass TemplateFileFormat code as String
	 * @param langCode
	 *            pass language code as String
	 * @return TemplateFileFormatResponseDto TemplateFileFormat detail based on
	 *         given TemplateFileFormat code and Language code
	 *         {@link TemplateFileFormatResponseDto}
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGettemplatefileformatscodelangcode())")
	@GetMapping(value = { "/code/{code}", "/{code}/{langcode}" })
	@ApiOperation(value = "Retrieve all TemplateFileFormat Details, /langCode pathparam will be deprecated soon", notes = "Retrieve all TemplateFileFormat Detail for given code")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When TemplateFileFormat Details retrieved from database for the give Code"),
			@ApiResponse(code = 404, message = "When No TemplateFileFormat Details found for the given Code"),
			@ApiResponse(code = 500, message = "While retrieving TemplateFileFormat Details any error occured") })
	public ResponseWrapper<TemplateFileFormatResponseDto> getTemplateFileFormatCodeandLangCode(
			@PathVariable("code") String templateFileFormatCode,
			@PathVariable(value = "langcode", required = false) String langCode) {

		ResponseWrapper<TemplateFileFormatResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(
				templateFileFormatService.getTemplateFileFormatCodeandLangCode(templateFileFormatCode, langCode));
		return responseWrapper;
	}

	/**
	 * 
	 * Function to fetch TemplateFileFormat detail based.
	 * 
	 * @param langCode
	 *            pass language code as String
	 * @return TemplateFileFormatResponseDto TemplateFileFormat detail based on
	 *         given TemplateFileFormat code and Language code
	 *         {@link TemplateFileFormatResponseDto}
	 */
	@Deprecated
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGettemplatefileformatslangcode())")
	@GetMapping(value = { "", "/{langcode}" })
	@ApiOperation(value = "Retrieve all TemplateFileFormat Details, /langCode pathparam will be deprecated soon", notes = "Retrieve all TemplateFileFormat Detail")
	@ApiResponses({
			@ApiResponse(code = 200, message = "When TemplateFileFormat Details retrieved from database"),
			@ApiResponse(code = 404, message = "When No TemplateFileFormat Details found"),
			@ApiResponse(code = 500, message = "While retrieving TemplateFileFormat Details any error occured") })
	public ResponseWrapper<TemplateFileFormatResponseDto> getTemplateFileFormatLangCode(
			@PathVariable(value = "langcode", required = false) String langCode) {

		ResponseWrapper<TemplateFileFormatResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(templateFileFormatService.getTemplateFileFormatLangCode(langCode));
		return responseWrapper;
	}
	
	@ResponseFilter
//	@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchtemplatefileformats())")
	@PatchMapping
	@ApiOperation(value = "Service to update TemplateFileFormat", notes = "Update TemplateFileFormat and return TemplateFileFormat code")
	public ResponseWrapper<StatusResponseDto> updateFileFormatStatus(@Valid @RequestParam boolean isActive,
			@RequestParam String code) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + TemplateFileFormatDto.class.getSimpleName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + TemplateFileFormatDto.class.getSimpleName(), "ADM-831");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(templateFileFormatService.updateTemplateFileFormat(code, isActive));
		auditUtil.auditRequest(MasterDataConstant.STATUS_UPDATED_SUCCESS + TemplateFileFormatDto.class.getSimpleName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_UPDATED_SUCCESS + TemplateFileFormatDto.class.getSimpleName(), "ADM-832");
		return responseWrapper;
	}
}
