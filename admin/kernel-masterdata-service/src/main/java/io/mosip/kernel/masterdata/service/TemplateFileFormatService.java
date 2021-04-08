package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.TemplateFileFormatDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatPutDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;

/**
 * Service API for Template File Format
 * 
 * @author Neha Sinha
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
public interface TemplateFileFormatService {

	/**
	 * Method to create a templatefileformat
	 * 
	 * @param templateFileFormatRequestDto
	 *            the template file format dto
	 * @return {@link CodeAndLanguageCodeID}
	 */
	public CodeAndLanguageCodeID createTemplateFileFormat(TemplateFileFormatDto templateFileFormatRequestDto);

	/**
	 * This method is used to update an existing TemplateFileFormat
	 * 
	 * @param templateFileFormatRequestDto
	 *            TemplateFileFormat DTO to update data
	 * @return IdResponseDto TemplateFileFormat ID which is successfully updated
	 *         {@link IdResponseDto}
	 * @throws MasterDataServiceException
	 *             if any error occurred while updating Device
	 */
	public CodeAndLanguageCodeID updateTemplateFileFormat(TemplateFileFormatPutDto templateFileFormatRequestDto);
	
	public StatusResponseDto updateTemplateFileFormat(String code, boolean isActive);

	/**
	 * Method to delete TemplateFileFormat based on code provided.
	 * 
	 * @param code
	 *            the TemplateFileFormat code.
	 * 
	 * @return {@link CodeResponseDto}
	 */
	public CodeResponseDto deleteTemplateFileFormat(String code);

	/**
	 * This abstract method to fetch TemplateFileFormat details for given
	 * TemplateFileFormat Code and language code
	 * 
	 * @param id
	 *            TemplateFileFormat Code given by user
	 * @param langCode
	 *            Language code given by user
	 * @return TemplateFileFormatResponseDto TemplateFileFormat Detail for given
	 *         TemplateFileFormat code and language code
	 * @throws MasterDataServiceException
	 *             if any error occurs while retrieving TemplateFileFormat Details
	 * @throws DataNotFoundException
	 *             if no TemplateFileFormat found
	 *
	 */
	public TemplateFileFormatResponseDto getTemplateFileFormatCodeandLangCode(String templateFileFormatCode,
			String langCode);

	/**
	 * This abstract method to fetch TemplateFileFormat details for given language
	 * code
	 * 
	 * @param langCode
	 *            Language code given by user
	 * @return TemplateFileFormatResponseDto TemplateFileFormat Detail for given
	 *         language code
	 * 
	 * @throws MasterDataServiceException
	 *             if any error occurs while retrieving TemplateFileFormat Details
	 * @throws DataNotFoundException
	 *             if no TemplateFileFormat found
	 *
	 */
	public TemplateFileFormatResponseDto getTemplateFileFormatLangCode(String langCode);
}
