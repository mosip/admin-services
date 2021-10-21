package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.LanguageDto;
import io.mosip.kernel.masterdata.dto.getresponse.LanguageResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;

/**
 * This interface provides methods to do CRUD operations on Language.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
public interface LanguageService {

	/**
	 * This method provides all the languages present in database.
	 * 
	 * @return LanguageResponseDto
	 * 
	 * @throws MasterDataServiceException if any error occurs while retrieving
	 *                                    languages
	 * 
	 * @throws DataNotFoundException      if no language found
	 */
	LanguageResponseDto getAllLaguages();

	/**
	 * This method save {@link LanguageDto} provide by the user in database.
	 * 
	 * @param requestDto request {@link LanguageDto} data provided by the user which
	 *                   is going to be persisted
	 * 
	 * @return language code which is created of type {@link CodeResponseDto}
	 * 
	 * 
	 * @throws MasterDataServiceException if any error occurred while saving
	 *                                    languages
	 */
	CodeResponseDto saveLanguage(LanguageDto requestDto);

	/**
	 * This method update {@link LanguageDto} provide by the user in database.
	 * 
	 * @param requestDto request {@link LanguageDto} data provided by the user which
	 *                   is going to be persisted
	 * 
	 * @return language code which is created of type {@link CodeResponseDto}
	 * 
	 * 
	 * @throws MasterDataServiceException if any error occurred while saving
	 *                                    languages
	 */
	CodeResponseDto updateLanguage(LanguageDto requestDto);

	/**
	 * This method is used to delete a language present in database.
	 * 
	 * @param code is the language code present in the database.
	 * @return the language code deleted.
	 */
	CodeResponseDto deleteLanguage(String code);

}
