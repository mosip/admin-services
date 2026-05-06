package io.mosip.kernel.masterdata.controller;

import io.mosip.kernel.masterdata.dto.BlockListedWordStatusUpdateDto;
import jakarta.validation.Valid;

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
import io.mosip.kernel.masterdata.dto.BlockListedWordsUpdateDto;
import io.mosip.kernel.masterdata.dto.BlocklistedWordListRequestDto;
import io.mosip.kernel.masterdata.dto.BlocklistedWordsDto;
import io.mosip.kernel.masterdata.dto.getresponse.BlocklistedWordsResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.BlocklistedWordsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.MachineSearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.id.WordAndLanguageCodeID;
import io.mosip.kernel.masterdata.service.BlocklistedWordsService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller that provides with methods for operations on blocklisted words.
 * 
 * @author Abhishek Kumar
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
@RestController
@Api(tags = { "BlocklistedWords" })
@RequestMapping("/blocklistedwords")
public class BlocklistedWordsController {
	@Autowired
	private BlocklistedWordsService blocklistedWordsService;

	@Autowired
	private AuditUtil auditUtil;

	/**
	 * Fetch the list of blocklisted words based on language code.
	 * 
	 * @param langCode language code
	 * @return {@link BlocklistedWordsResponseDto}
	 */
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetblocklistedwordslangcode())")
	@GetMapping("/{langcode}")
	public ResponseWrapper<BlocklistedWordsResponseDto> getAllBlockListedWordByLangCode(
			@PathVariable("langcode") String langCode) {

		ResponseWrapper<BlocklistedWordsResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(blocklistedWordsService.getAllBlocklistedWordsBylangCode(langCode));
		return responseWrapper;
	}

	/**
	 * Takes the list of string as an argument and checks if the list contains any
	 * blocklisted words.
	 * 
	 * @param blocklistedwords list of blocklisted words
	 * @return Valid if word does not belongs to block listed word and Invalid if
	 *         word belongs to block listed word
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostblocklistedwordswords())")
	@ResponseFilter
	@PostMapping(path = "/words")
	@ApiOperation(value = "Block listed word validation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Valid Word"),
			@ApiResponse(code = 200, message = "Invalid Word") })
	public ResponseWrapper<CodeResponseDto> validateWords(
			@RequestBody RequestWrapper<BlocklistedWordListRequestDto> blocklistedwords) {

		String isValid = "Valid";
		if (!blocklistedWordsService.validateWord(blocklistedwords.getRequest().getBlocklistedwords())) {
			isValid = "Invalid";
		}
		CodeResponseDto dto = new CodeResponseDto();
		dto.setCode(isValid);

		ResponseWrapper<CodeResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dto);
		return responseWrapper;
	}

	/**
	 * Method to add blocklisted word.
	 * 
	 * @param blockListedWordsRequestDto the request dto that holds the blocklisted
	 *                                   word to be added.
	 * @return the response entity i.e. the word and language code of the word
	 *         added.
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostblocklistedwords())")
	@PostMapping
	public ResponseWrapper<WordAndLanguageCodeID> createBlockListedWord(
			@RequestBody @Valid RequestWrapper<BlocklistedWordsDto> blockListedWordsRequestDto) {
		auditUtil.auditRequest(
				MasterDataConstant.CREATE_API_IS_CALLED + BlocklistedWordListRequestDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.CREATE_API_IS_CALLED + BlocklistedWordListRequestDto.class.getCanonicalName(),
				"ADM-545");
		ResponseWrapper<WordAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper
				.setResponse(blocklistedWordsService.createBlockListedWord(blockListedWordsRequestDto.getRequest()));
		return responseWrapper;
	}

	/**
	 * Method to update the blocklisted word
	 * 
	 * @param blockListedWordsRequestDto the request dto that holds the blocklisted
	 *                                   word to be updated .
	 * @return the response entity i.e. the word and language code of the word
	 *         updated.
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPutblocklistedwords())")
	@PutMapping
	@ApiOperation(value = "update the blocklisted word")
	public ResponseWrapper<WordAndLanguageCodeID> updateBlockListedWord(
			@Valid @RequestBody RequestWrapper<BlockListedWordsUpdateDto> blockListedWordsRequestDto) {
		auditUtil.auditRequest(
				MasterDataConstant.UPDATE_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				"ADM-546");
		ResponseWrapper<WordAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper
				.setResponse(blocklistedWordsService.updateBlockListedWord(blockListedWordsRequestDto.getRequest()));
		return responseWrapper;
	}

	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPutblocklistedwordsdetails())")
	@PutMapping(path = "/details")
	@ApiOperation(value = "update the blocklisted word details except word")
	public ResponseWrapper<WordAndLanguageCodeID> updateBlockListedWordExceptWord(
			@Valid @RequestBody RequestWrapper<BlocklistedWordsDto> blockListedWordsRequestDto) {
		auditUtil.auditRequest(
				MasterDataConstant.UPDATE_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.UPDATE_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				"ADM-547");
		ResponseWrapper<WordAndLanguageCodeID> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(
				blocklistedWordsService.updateBlockListedWordExceptWord(blockListedWordsRequestDto.getRequest()));
		return responseWrapper;
	}

	/**
	 * Method to deleted blocklisted word.
	 * 
	 * @param word input blocklisted word to be deleted.
	 * @return deleted word.
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getDeleteblocklistedwordsword())")
	@DeleteMapping("/{word}")
	@ApiOperation(value = "delete the blocklisted word")
	public ResponseWrapper<CodeResponseDto> deleteBlockListedWord(@PathVariable("word") String word) {
		CodeResponseDto dto = new CodeResponseDto();//
		dto.setCode(blocklistedWordsService.deleteBlockListedWord(word));

		ResponseWrapper<CodeResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(dto);
		return responseWrapper;
	}

	/**
	 * Api to get all the blocklisted words
	 * 
	 * @return list of {@link BlocklistedWordsDto}
	 */
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	@ResponseFilter
	//@PreAuthorize("hasAnyRole(@authorizedRoles.getGetblocklistedwordsall())")
	@GetMapping("/all")
	@ApiOperation(value = "Retrieve all the blocklisted words with additional metadata", notes = "Retrieve all the blocklisted words with metadata")
	@ApiResponses({ @ApiResponse(code = 200, message = "list of blocklistedwords"),
			@ApiResponse(code = 500, message = "Error occured while retrieving blocklisted words") })
	public ResponseWrapper<PageDto<BlocklistedWordsExtnDto>> getAllBlocklistedWords(
			@RequestParam(name = "pageNumber", defaultValue = "0") @ApiParam(value = "page no for the requested data", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") @ApiParam(value = "page size for the requested data", defaultValue = "10") int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "createdDateTime") @ApiParam(value = "sort the requested data based on param value", defaultValue = "createdDateTime") String sortBy,
			@RequestParam(name = "orderBy", defaultValue = "desc") @ApiParam(value = "order the requested data based on param", defaultValue = "desc") OrderEnum orderBy) {
		ResponseWrapper<PageDto<BlocklistedWordsExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper
				.setResponse(blocklistedWordsService.getBlockListedWords(pageNumber, pageSize, sortBy, orderBy.name()));
		return responseWrapper;
	}

	/**
	 * API to search BlockListedWords.
	 * 
	 * @param request the request DTO {@link SearchDto} wrapped in
	 *                {@link RequestWrapper}.
	 * @return the response i.e. multiple entities based on the search values
	 *         required.
	 */
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostblocklistedwordssearch())")
	@PostMapping("/search")
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	public ResponseWrapper<PageResponseDto<BlocklistedWordsExtnDto>> searchBlockListedWords(
			@RequestBody @Valid RequestWrapper<SearchDto> request) {
		auditUtil.auditRequest(
				MasterDataConstant.SEARCH_API_IS_CALLED + BlocklistedWordsExtnDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SEARCH_API_IS_CALLED + BlocklistedWordsExtnDto.class.getCanonicalName(), "ADM-548");
		ResponseWrapper<PageResponseDto<BlocklistedWordsExtnDto>> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(blocklistedWordsService.searchBlockListedWords(request.getRequest()));
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_SEARCH, BlocklistedWordsExtnDto.class.getCanonicalName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_SEARCH_DESC, MachineSearchDto.class.getCanonicalName()),
				"ADM-549");
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
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPostblocklistedwordsfiltervalues())")
	@PostMapping("/filtervalues")
	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN')")
	public ResponseWrapper<FilterResponseDto> blockListedWordsFilterValues(
			@RequestBody @Valid RequestWrapper<FilterValueDto> requestWrapper) {
		auditUtil.auditRequest(
				MasterDataConstant.FILTER_API_IS_CALLED + BlocklistedWordsExtnDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.FILTER_API_IS_CALLED + BlocklistedWordsExtnDto.class.getCanonicalName(), "ADM-550");
		ResponseWrapper<FilterResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(blocklistedWordsService.blockListedWordsFilterValues(requestWrapper.getRequest()));
		auditUtil.auditRequest(MasterDataConstant.SUCCESSFUL_FILTER + BlocklistedWordsExtnDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.SUCCESSFUL_FILTER_DESC + BlocklistedWordsExtnDto.class.getCanonicalName(),
				"ADM-551");
		return responseWrapper;
	}

	//@PreAuthorize("hasAnyRole('GLOBAL_ADMIN','ZONAL_ADMIN','REGISTRATION_ADMIN')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@authorizedRoles.getPatchblocklistedwords())")
	@PatchMapping
	@ApiOperation(value = "update the blocklisted word")
	public ResponseWrapper<StatusResponseDto> updateBlockListedWordStatus(@RequestBody @Valid RequestWrapper<BlockListedWordStatusUpdateDto> requestWrapper) {
		auditUtil.auditRequest(
				MasterDataConstant.STATUS_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_API_IS_CALLED + BlockListedWordsUpdateDto.class.getCanonicalName(),
				"ADM-552");
		ResponseWrapper<StatusResponseDto> responseWrapper = new ResponseWrapper<>();
		responseWrapper.setResponse(blocklistedWordsService.updateBlockListedWordStatus(requestWrapper.getRequest()));
		auditUtil.auditRequest(
				MasterDataConstant.STATUS_UPDATED_SUCCESS + BlockListedWordsUpdateDto.class.getCanonicalName(),
				MasterDataConstant.AUDIT_SYSTEM,
				MasterDataConstant.STATUS_UPDATED_SUCCESS + BlockListedWordsUpdateDto.class.getCanonicalName(),
				"ADM-553");
		return responseWrapper;
	}
}
