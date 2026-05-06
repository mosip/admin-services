package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.BlockListedWordStatusUpdateDto;
import io.mosip.kernel.masterdata.dto.BlockListedWordsUpdateDto;
import io.mosip.kernel.masterdata.dto.BlocklistedWordsDto;
import io.mosip.kernel.masterdata.dto.getresponse.BlocklistedWordsResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.BlocklistedWordsExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.id.WordAndLanguageCodeID;

import java.util.List;

/**
 * blocklisted words service
 * 
 * @author Abhishek Kumar
 * @version 1.0.0
 * @since 06-11-2018
 */
public interface BlocklistedWordsService {

	/**
	 * method to fetch all blocklisted words by language code
	 * 
	 * @param langCode language code
	 * @return {@link BlocklistedWordsResponseDto}
	 */
	BlocklistedWordsResponseDto getAllBlocklistedWordsBylangCode(String langCode);

	/**
	 * @param blocklistedwords the words to validate
	 * @return if the words are valid or invaid
	 */
	public boolean validateWord(List<String> blocklistedwords);

	/**
	 * Method to add blocklisted words.
	 * 
	 * @param blockListedWordsRequestDto the request dto of blocklisted word to be
	 *                                   added.
	 * @return the response.
	 */
	public WordAndLanguageCodeID createBlockListedWord(BlocklistedWordsDto blockListedWordsRequestDto);

	/**
	 * Method to update blocklisted words.
	 * 
	 * @param blockListedWordsRequestDto the request dto of blocklisted word to be
	 *                                   updated.
	 * @return the response.
	 */
	public WordAndLanguageCodeID updateBlockListedWord(BlockListedWordsUpdateDto blockListedWordsRequestDto);

	/**
	 * Method to delete blocklisted words.
	 * 
	 * @param blockListedWord the word to be deleted.
	 * @return the response.
	 */

	public String deleteBlockListedWord(String blockListedWord);

	/**
	 * Method to fetch all the blocklisted words
	 * 
	 * @param pageNumber next page number to get the requested data
	 * @param pageSize   number of data in the list
	 * @param sortBy     sorting data based the column name
	 * @param orderBy    order the list based on desc or asc
	 * 
	 * @return list of {@link BlocklistedWordsDto}
	 */
	public PageDto<BlocklistedWordsExtnDto> getBlockListedWords(int pageNumber, int pageSize, String sortBy,
			String orderBy);

	WordAndLanguageCodeID updateBlockListedWordExceptWord(BlocklistedWordsDto blocklistedWordsDto);

	/**
	 * Method to search blocklisted words.
	 * 
	 * @param dto the searchDTO
	 * @return {@link PageResponseDto} containing pages of the searched values.
	 */
	public PageResponseDto<BlocklistedWordsExtnDto> searchBlockListedWords(SearchDto dto);

	/**
	 * Method that returns the column values of specific filter column name.
	 * 
	 * @param filterValueDto the request DTO that provides the column name.
	 * @return the response containing the filter values.
	 */
	public FilterResponseDto blockListedWordsFilterValues(FilterValueDto filterValueDto);
	
	public StatusResponseDto updateBlockListedWordStatus(BlockListedWordStatusUpdateDto requestDto);

}
