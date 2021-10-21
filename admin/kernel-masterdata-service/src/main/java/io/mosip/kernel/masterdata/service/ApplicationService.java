package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.ApplicationDto;
import io.mosip.kernel.masterdata.dto.getresponse.ApplicationResponseDto;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;

/**
 * Service APIs to get Application details
 * 
 * @author Neha
 * @since 1.0.0
 * 
 */
public interface ApplicationService {

	/**
	 * Get All Applications
	 * 
	 * @return {@link ApplicationResponseDto}
	 */
	public ApplicationResponseDto getAllApplication();

	/**
	 * Get All Applications by language code
	 * 
	 * @param langCode the language code
	 * @return {@link ApplicationResponseDto}
	 */
	public ApplicationResponseDto getAllApplicationByLanguageCode(String langCode);

	/**
	 * Get An Application by code and language code
	 * 
	 * @param code     the code
	 * @param langCode the language code
	 * @return {@link ApplicationResponseDto}
	 */
	public ApplicationResponseDto getApplicationByCodeAndLanguageCode(String code, String langCode);

	/**
	 * To create an Application
	 * 
	 * @param application the application data
	 * @return {@link CodeAndLanguageCodeID}
	 */
	public CodeAndLanguageCodeID createApplication(ApplicationDto application);

}
