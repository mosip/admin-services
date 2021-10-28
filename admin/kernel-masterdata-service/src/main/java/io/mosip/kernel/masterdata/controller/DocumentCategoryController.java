package io.mosip.kernel.masterdata.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.OrderEnum;
import io.mosip.kernel.masterdata.dto.DocumentCategoryDto;
import io.mosip.kernel.masterdata.dto.DocumentCategoryPutDto;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.MissingDataDto;
import io.mosip.kernel.masterdata.dto.getresponse.DocumentCategoryResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DocumentCategoryExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.DocumentCategory;
import io.mosip.kernel.masterdata.entity.Template;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.service.DocumentCategoryService;
import io.mosip.kernel.masterdata.service.GenericService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class to fetch or create document categories.
 * 
 * @author Neha
 * @author Ritesh Sinha
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@CrossOrigin
@RestController
@Api(tags = { "DocumentCategory" })
public class DocumentCategoryController {

	@Autowired
	AuditUtil auditUtil;

	@Autowired
	DocumentCategoryService documentCategoryService;
	
	@Autowired
	private GenericService genericService;

	/**
	 * API to fetch all Document categories details
	 * 
	 * @return All Document categories
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdocumentcategories())")
	@GetMapping("/documentcategories")
	public ResponseWrapper<DocumentCategoryResponseDto> getAllDocumentCategory() {
		ResponseWrapper<DocumentCategoryResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.getAllDocumentCategory());
		return responseWrapper;
	}

	/**
	 * API to fetch all Document categories details based on language code
	 * 
	 * @param langCode the language code
	 * 
	 * @return {@link DocumentCategoryResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdocumentcategorieslangcode())")
	@GetMapping("/documentcategories/{langcode}")
	public ResponseWrapper<DocumentCategoryResponseDto> getAllDocumentCategoryByLaguageCode(
			@PathVariable("langcode") String langCode) {
		ResponseWrapper<DocumentCategoryResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.getAllDocumentCategoryByLaguageCode(langCode));
		return responseWrapper;
	}

	/**
	 * API to fetch all Document categories details based on code and language code
	 * 
	 * @param code     the code
	 * @param langCode the language code
	 * @return {@link DocumentCategoryResponseDto}
	 */
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','PRE_REGISTRATION','REGISTRATION_SUPERVISOR','RESIDENT','REGISTRATION_PROCESSOR','REGISTRATION_PROCESSOR','REGISTRATION_OFFICER','PARTNER','AUTH_PARTNER','PARTNER_ADMIN','DEVICE_PROVIDER','DEVICE_MANAGER')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdocumentcategoriescode())")
	@GetMapping("/documentcategories/{code}/{langcode}")
	public ResponseWrapper<DocumentCategoryResponseDto> getDocumentCategoryByCodeAndLangCode(
			@PathVariable("code") String code, @PathVariable("langcode") String langCode) {

		ResponseWrapper<DocumentCategoryResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.getDocumentCategoryByCodeAndLangCode(code, langCode));
		return responseWrapper;
	}

	/**
	 * API to create Document category
	 * 
	 * @param category is of type {@link DocumentCategoryDto}
	 * 
	 * @return {@link CodeAndLanguageCodeID}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdocumentcategories())")
	@PostMapping("/documentcategories")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to create document category", notes = "Create document category and return composite id")
	public ResponseWrapper<CodeAndLanguageCodeID> createDocumentCategory(
			@ApiParam("Document category DTO to create") @Valid @RequestBody RequestWrapper<DocumentCategoryDto> category) {

		auditUtil.auditRequest(MasterDataConstant.CREATE_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(), "ADM-694");
		ResponseWrapper<CodeAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.createDocumentCategory(category.getRequest()));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_CREATE, DocumentCategoryDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC, DocumentCategoryDto.class.getCanonicalName()),
				"ADM-695");
		return responseWrapper;
	}

	/**
	 * Api to update Document category.
	 * 
	 * @param category is of type {@link DocumentCategoryDto}
	 * @return {@link CodeAndLanguageCodeID}
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPutdocumentcategories())")
	@PutMapping("/documentcategories")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update document category", notes = "Update document category and return composite id")
	public ResponseWrapper<CodeAndLanguageCodeID> updateDocumentCategory(
			@ApiParam("Document category DTO to update") @Valid @RequestBody RequestWrapper<DocumentCategoryPutDto> category) {

		auditUtil.auditRequest(MasterDataConstant.UPDATE_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(), "ADM-696");
		ResponseWrapper<CodeAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.updateDocumentCategory(category.getRequest()));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_UPDATE, DocumentCategoryDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC, DocumentCategoryDto.class.getCanonicalName()),
				"ADM-697");
		return responseWrapper;
	}

	/**
	 * Api to delete Document Category.
	 * 
	 * @param code the document category code.
	 * @return the code.
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeletedocumentcategoriescode())")
	@DeleteMapping("/documentcategories/{code}")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to delete document category", notes = "Delete document category and return composite id")
	public ResponseWrapper<CodeResponseDto> deleteDocumentCategory(@PathVariable("code") String code) {

		ResponseWrapper<CodeResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.deleteDocumentCategory(code));
		return responseWrapper;
	}

	/**
	 * This controller method provides with all document category details.
	 * 
	 * @param pageNumber the page number
	 * @param pageSize   the size of each page
	 * @param sortBy     the attributes by which it should be ordered
	 * @param orderBy    the order to be used
	 * @return the response i.e. pages containing the document category details.
	 */
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','CENTRAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdocumentcategoriesall())")
	@GetMapping("/documentcategories/all")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Retrieve all the document category with metadata", notes = "Retrieve all the document categories")
	@ApiResponses({ @ApiResponse(code = 200, message = "list of device specifications"),
			@ApiResponse(code = 500, message = "Error occured while retrieving device specifications") })
	public ResponseWrapper<PageDto<DocumentCategoryExtnDto>> getAllDocCategories(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page no for the requested data", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size for the requested data", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "createdDateTime") @ApiParam(value = "sort the requested data based on param value", defaultValue = "createdDateTime") String sortBy,
			@RequestParam(name = "orderBy", defaultValue = "desc") @ApiParam(value = "order the requested data based on param", defaultValue = "desc") OrderEnum orderBy) {
		ResponseWrapper<PageDto<DocumentCategoryExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper
				.setResponse(documentCategoryService.getAllDocCategories(pageNumber, pageSize, sortBy, orderBy.name()));
		return responseWrapper;
	}

	/**
	 * API to search document Category.
	 * 
	 * @param request the request DTO {@link SearchDto} wrapped in
	 *                {@link RequestWrapper}.
	 * @return the response i.e. multiple entities based on the search values
	 *         required.
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdocumentcategoriessearch())")
	@PostMapping("/documentcategories/search")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	public ResponseWrapper<PageResponseDto<DocumentCategoryExtnDto>> searchDocCategories(
			@RequestBody @Valid RequestWrapper<SearchDto> request) {
		auditUtil.auditRequest(MasterDataConstant.SEARCH_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_API_IS_CALLED + DocumentTypeDto.class.getCanonicalName(), "ADM-698");
		ResponseWrapper<PageResponseDto<DocumentCategoryExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.searchDocCategories(request.getRequest()));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_FILTER, DocumentCategoryDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_FILTER_DESC, DocumentCategoryDto.class.getCanonicalName()),
				"ADM-699");
		return responseWrapper;
	}

	/**
	 * API that returns the values required for the column filter columns.
	 * 
	 * @param request the request DTO {@link FilterResponseDto} wrapper in
	 *                {@link RequestWrapper}.
	 * @return the response i.e. the list of values for the specific filter column
	 *         name and type.
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostdocumentcategoriesfiltervalues())")
	@PostMapping("/documentcategories/filtervalues")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	public ResponseWrapper<FilterResponseDto> docCategoriesFilterValues(
			@RequestBody @Valid RequestWrapper<FilterValueDto> request) {
		auditUtil.auditRequest(MasterDataConstant.FILTER_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.FILTER_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(), "ADM-800");
		ResponseWrapper<FilterResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.docCategoriesFilterValues(request.getRequest()));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_FILTER, DocumentCategoryDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_FILTER_DESC, DocumentCategoryDto.class.getCanonicalName()),
				"ADM-801");
		return responseWrapper;
	}
	
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchdocumentcategories())")
	@PatchMapping("/documentcategories")
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN')")
	@ApiOperation(value = "Service to update document category", notes = "Update document category")
	public ResponseWrapper<StatusResponseDto> updateDocumentCategoryStatus(@RequestParam boolean isActive,
			@RequestParam String code) {
		auditUtil.auditRequest(MasterDataConstant.STATUS_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + DocumentCategoryDto.class.getCanonicalName(), "ADM-802");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(documentCategoryService.updateDocumentCategory(code, isActive));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DocumentCategoryDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.STATUS_UPDATED_SUCCESS, DocumentCategoryDto.class.getCanonicalName()),
				"ADM-803");
		return responseWrapper;
	}
	
	/**
	 * Function to fetch missing ids/codes in the provided language code
	 *
	 * @return List<String> list of missing ids/ codes
	 */
	@ResponseFilter
	@GetMapping("/documentcategories/missingids/{langcode}")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetdocumentcategoriesmissingidslangcode())")
	public ResponseWrapper<List<MissingDataDto>> getMissingDocumentCategoryDetails(
			@PathVariable("langcode") String langCode, @RequestParam(required = false) String fieldName) {
		ResponseWrapper<List<MissingDataDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(genericService.getMissingData(DocumentCategory.class, langCode, "code", fieldName));
		return responseWrapper;
	}
}
