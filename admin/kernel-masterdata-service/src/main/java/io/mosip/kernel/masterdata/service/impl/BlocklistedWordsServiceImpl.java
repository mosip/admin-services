package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.datamapper.spi.DataMapper;
import io.mosip.kernel.masterdata.constant.BlocklistedWordsErrorCode;
import io.mosip.kernel.masterdata.constant.MachineErrorCode;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UpdateQueryConstants;
import io.mosip.kernel.masterdata.dto.BlockListedWordsUpdateDto;
import io.mosip.kernel.masterdata.dto.BlocklistedWordsDto;
import io.mosip.kernel.masterdata.dto.getresponse.BlocklistedWordsResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.BlocklistedWordsExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.dto.response.ColumnValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.BlocklistedWords;
import io.mosip.kernel.masterdata.entity.id.WordAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.BlocklistedWordsRepository;
import io.mosip.kernel.masterdata.service.BlocklistedWordsService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;

/**
 * Service implementation class for {@link BlocklistedWordsService}.
 * 
 * @author Abhishek Kumar
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
@Service
public class BlocklistedWordsServiceImpl implements BlocklistedWordsService {

	/**
	 * Autowired reference for {@link BlocklistedWordsRepository}.
	 */
	@Autowired
	private BlocklistedWordsRepository blocklistedWordsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	FilterTypeValidator filterTypeValidator;

	@Autowired
	FilterColumnValidator filterColumnValidator;

	@Autowired
	MasterdataSearchHelper masterDataSearchHelper;
	
	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	private PageUtils pageUtils;

	@Autowired
	private AuditUtil auditUtil;

	/**
	 * Autowired reference for {@link DataMapper}
	 */

	@Qualifier("blocklistedWordsToWordAndLanguageCodeIDDefaultMapper")
	@Autowired
	private DataMapper<BlocklistedWords, WordAndLanguageCodeID> blocklistedWordsToWordAndLanguageCodeIDDefaultMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * getAllBlocklistedWordsBylangCode(java.lang.String)
	 */
	@Override
	public BlocklistedWordsResponseDto getAllBlocklistedWordsBylangCode(String langCode) {
		List<BlocklistedWordsDto> wordsDto = null;
		List<BlocklistedWords> words = null;
		try {
			words = blocklistedWordsRepository.findAllByLangCode(langCode);
		} catch (DataAccessException accessException) {
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}
		if (words != null && !words.isEmpty()) {
			wordsDto = MapperUtils.mapAll(words, BlocklistedWordsDto.class);
		} else {
			throw new DataNotFoundException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
					BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
		}

		return new BlocklistedWordsResponseDto(wordsDto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.BlocklistedWordsService#validateWord(java.
	 * util.List)
	 */
	@Override
	public boolean validateWord(List<String> words) {
		List<String> wordList = new ArrayList<>();
		boolean isValid = true;
		List<BlocklistedWords> blockListedWordsList = null;
		try {
			blockListedWordsList = blocklistedWordsRepository.findAllByIsDeletedFalseOrIsDeletedNull();
		} catch (DataAccessException | DataAccessLayerException accessException) {
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}
		for (BlocklistedWords blockListedWords : blockListedWordsList) {
			wordList.add(blockListedWords.getWord());
		}
		words.replaceAll(String::toLowerCase);
		wordList.replaceAll(String::toLowerCase);

		for (String temp : wordList) {
			if (words.contains(temp)) {
				isValid = false;
			}
		}
		return isValid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.BlocklistedWordsService#addBlockListedWord
	 * (io.mosip.kernel.masterdata.dto.BlockListedWordsRequestDto)
	 */
	@Override
	public WordAndLanguageCodeID createBlockListedWord(BlocklistedWordsDto blockListedWordsRequestDto) {
		BlocklistedWords entity = MetaDataUtils.setCreateMetaData(blockListedWordsRequestDto, BlocklistedWords.class);
		BlocklistedWords blocklistedWords;
		entity.setWord(entity.getWord().toLowerCase());
		try {
			if(blocklistedWordsRepository.findByOnlyWordAndLangCode(blockListedWordsRequestDto.getWord(), blockListedWordsRequestDto.getLangCode()) !=null) {
				throw new RequestException(BlocklistedWordsErrorCode.DUPLICATE_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
						BlocklistedWordsErrorCode.DUPLICATE_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
			}
			blocklistedWords = blocklistedWordsRepository.create(entity);
		} catch (DataAccessLayerException | DataAccessException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_CREATE, BlocklistedWordsDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							MachineErrorCode.MACHINE_DELETE_EXCEPTION.getErrorCode(),
							MachineErrorCode.MACHINE_DELETE_EXCEPTION.getErrorMessage()),
					"ADM-552");
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_INSERT_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_INSERT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}

		WordAndLanguageCodeID wordAndLanguageCodeId = new WordAndLanguageCodeID();

		MapperUtils.map(entity, wordAndLanguageCodeId);

		return wordAndLanguageCodeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * updateBlockListedWord(io.mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	@Transactional
	public WordAndLanguageCodeID updateBlockListedWord(BlockListedWordsUpdateDto wordDto) {
		WordAndLanguageCodeID wordAndLanguageCodeID = null;
		int noOfRowAffected = 0;
		Map<String, Object> params = bindDtoToMap(wordDto);
		try {
			if (wordDto.getDescription() != null && !wordDto.getDescription().isEmpty()) {
				noOfRowAffected = blocklistedWordsRepository.createQueryUpdateOrDelete(
						UpdateQueryConstants.BLOCKLISTED_WORD_UPDATE_QUERY_WITH_DESCRIPTION.getQuery(), params);
			} else {
				noOfRowAffected = blocklistedWordsRepository.createQueryUpdateOrDelete(
						UpdateQueryConstants.BLOCKLISTED_WORD_UPDATE_QUERY_WITHOUT_DESCRIPTION.getQuery(), params);
			}
			if (noOfRowAffected != 0)
				wordAndLanguageCodeID = mapToWordAndLanguageCodeID(wordDto);
			else {
				auditUtil.auditRequest(
						String.format(
								MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
								BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage()),
						"ADM-553");
				throw new RequestException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
						BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorCode(),
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorMessage()),
					"ADM-554");
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorMessage());
		}
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						BlockListedWordsUpdateDto.class.getSimpleName(), wordAndLanguageCodeID.getWord()),
				"ADM-555");
		return wordAndLanguageCodeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * deleteBlockListedWord(java.lang.String)
	 */
	@Override
	public String deleteBlockListedWord(String blockListedWord) {
		int noOfRowAffected = 0;
		try {
			noOfRowAffected = blocklistedWordsRepository.deleteBlockListedWord(blockListedWord,
					LocalDateTime.now(ZoneId.of("UTC")));

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_DELETE_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_DELETE_EXCEPTION.getErrorMessage());
		}
		if (noOfRowAffected == 0) {
			throw new RequestException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
					BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
		}

		return blockListedWord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * getBlockListedWords()
	 */
	@Override
	public PageDto<BlocklistedWordsExtnDto> getBlockListedWords(int pageNumber, int pageSize, String sortBy,
			String orderBy) {
		List<BlocklistedWordsExtnDto> wordsDto = null;
		PageDto<BlocklistedWordsExtnDto> pageDto = null;
		try {
			Page<BlocklistedWords> pageData = blocklistedWordsRepository
					.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				wordsDto = MapperUtils.mapAll(pageData.getContent(), BlocklistedWordsExtnDto.class);
				pageDto = new PageDto<>(pageData.getNumber(), pageData.getTotalPages(), pageData.getTotalElements(),
						wordsDto);
			} else {
				throw new DataNotFoundException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
						BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
			}
		} catch (DataAccessException accessException) {
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}
		return pageDto;
	}

	private WordAndLanguageCodeID mapToWordAndLanguageCodeID(BlockListedWordsUpdateDto dto) {
		WordAndLanguageCodeID wordAndLanguageCodeID;
		wordAndLanguageCodeID = new WordAndLanguageCodeID();
		if (dto.getWord() != null)
			wordAndLanguageCodeID.setWord(dto.getWord());
		if (dto.getLangCode() != null)
			wordAndLanguageCodeID.setLangCode(dto.getLangCode());
		return wordAndLanguageCodeID;
	}

	private Map<String, Object> bindDtoToMap(BlockListedWordsUpdateDto dto) {
		Map<String, Object> params = new HashMap<>();
		if (dto.getWord() != null && !dto.getWord().isEmpty())
			params.put("word", dto.getWord().toLowerCase());
		if (dto.getOldWord() != null && !dto.getOldWord().isEmpty())
			params.put("oldWord", dto.getOldWord().toLowerCase());
		if (dto.getDescription() != null && !dto.getDescription().isEmpty())
			params.put("description", dto.getDescription());
		params.put("updatedBy", MetaDataUtils.getContextUser());
		params.put("updatedDateTime", LocalDateTime.now(ZoneId.of("UTC")));
		params.put("langCode", dto.getLangCode());
		return params;
	}

	@Override
	public WordAndLanguageCodeID updateBlockListedWordExceptWord(BlocklistedWordsDto blocklistedWordsDto) {
		WordAndLanguageCodeID id = null;
		BlocklistedWords wordEntity = null;
		blocklistedWordsDto.setWord(blocklistedWordsDto.getWord().toLowerCase());
		try {
			wordEntity = blocklistedWordsRepository.findByWordAndLangCode(blocklistedWordsDto.getWord(),
					blocklistedWordsDto.getLangCode());
			if (wordEntity != null) {
				MetaDataUtils.setUpdateMetaData(blocklistedWordsDto, wordEntity, false);
				wordEntity = blocklistedWordsRepository.update(wordEntity);
				id = MapperUtils.map(wordEntity, WordAndLanguageCodeID.class);
			}
		} catch (DataAccessException | DataAccessLayerException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorCode(),
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorMessage()),
					"ADM-556");
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorMessage());
		}

		if (id == null) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
							BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage()),
					"ADM-557");
			throw new RequestException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
					BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
		}
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						BlockListedWordsUpdateDto.class.getSimpleName(), id.getWord()),
				"ADM-558");
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * searchBlockListedWords(io.mosip.kernel.masterdata.dto.request.SearchDto)
	 */
	@Override
	public PageResponseDto<BlocklistedWordsExtnDto> searchBlockListedWords(SearchDto dto) {
		PageResponseDto<BlocklistedWordsExtnDto> pageDto = new PageResponseDto<>();
		List<BlocklistedWordsExtnDto> blockListedWords = null;

		if (filterTypeValidator.validate(BlocklistedWordsExtnDto.class, dto.getFilters())) {
			Pagination pagination = dto.getPagination();
			List<SearchSort> sort = dto.getSort();
			pageUtils.validateSortField(BlocklistedWordsExtnDto.class, BlocklistedWords.class, sort);
			dto.setPagination(new Pagination(0, Integer.MAX_VALUE));
			dto.setSort(Collections.emptyList());
			Page<BlocklistedWords> page = masterDataSearchHelper.searchMasterdata(BlocklistedWords.class, dto, null);
			if (page.getContent() != null && !page.getContent().isEmpty()) {
				blockListedWords = MapperUtils.mapAll(page.getContent(), BlocklistedWordsExtnDto.class);
				pageDto = pageUtils.sortPage(blockListedWords, sort, pagination);
			}
		}
		return pageDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.BlocklistedWordsService#
	 * blockListedWordsFilterValues(io.mosip.kernel.masterdata.dto.request.
	 * FilterValueDto)
	 */
	@Override
	public FilterResponseDto blockListedWordsFilterValues(FilterValueDto filterValueDto) {
		FilterResponseDto filterResponseDto = new FilterResponseDto();
		List<ColumnValue> columnValueList = new ArrayList<>();
		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), BlocklistedWords.class)) {
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				List<?> filterValues = masterDataFilterHelper.filterValues(BlocklistedWords.class, filterDto,
						filterValueDto);
				filterValues.forEach(filterValue -> {
					ColumnValue columnValue = new ColumnValue();
					columnValue.setFieldID(filterDto.getColumnName());
					columnValue.setFieldValue(filterValue.toString());
					columnValueList.add(columnValue);
				});
			}
			filterResponseDto.setFilters(columnValueList);
		}
		return filterResponseDto;
	}

	@Override
	public StatusResponseDto updateBlockListedWordStatus(String word, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();

		List<BlocklistedWords> wordEntity = null;
		try {
			wordEntity = blocklistedWordsRepository.findtoUpdateBlocklistedWordByWord(word);
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorCode(),
							BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_UPDATE_EXCEPTION.getErrorMessage()),
					"ADM-559");
			throw new MasterDataServiceException(
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
					BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}

		if (wordEntity != null && !wordEntity.isEmpty()) {
			masterdataCreationUtil.updateMasterDataStatus(BlocklistedWords.class, word, isActive, "word");
		} else {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, BlockListedWordsUpdateDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
							BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage()),
					"ADM-560");
			throw new DataNotFoundException(BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorCode(),
					BlocklistedWordsErrorCode.NO_BLOCKLISTED_WORDS_FOUND.getErrorMessage());
		}
		response.setStatus("Status updated successfully for BlocklistedWords");
		return response;
	}
}
