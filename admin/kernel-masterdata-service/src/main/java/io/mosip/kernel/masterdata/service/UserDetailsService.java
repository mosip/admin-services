package io.mosip.kernel.masterdata.service;

import java.util.List;

import io.mosip.kernel.core.authmanager.model.UserDetailsResponseDto;
import io.mosip.kernel.masterdata.dto.PageDto;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.UserDetailsHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
public interface UserDetailsService {
	/**
	 * This abstract method to fetch users details for given User Id. Note: the user id should be the username of the IAM system.
	 * 
	 * @param id       User Id given by user
	 * @return UserResponseDto User Detail for given user id and language
	 *         code
	 * @throws MasterDataServiceException if any error occurs while retrieving
	 *                                    User Details
	 * @throws DataNotFoundException      if no User found
	 *
	 */
	public UserDetailsDto getUser(String id);

	/**
	 * This method provides with all templates.
	 * 
	 * @param pageNumber the page number
	 * @param pageSize   the size of each page
	 * @param sortBy     the attributes by which it should be ordered
	 * @param direction    the order to be used
	 * 
	 * @return the response i.e. pages containing the templates.
	 */
	public PageDto<UserDetailsExtnDto> getUsers(int pageNumber, int pageSize, String sortBy, String direction);


	/**
	 * Abstract method to delete User Details to the Database
	 * 
	 * @param id user id
	 * 
	 * @return IdResponseDto returning user id which is updated successfully
	 *         {@link IdResponseDto}
	 * @throws RequestException           if User not Found
	 * 
	 * @throws MasterDataServiceException if any error occurred while updating
	 *                                    User
	 * 
	 */
	public IdResponseDto deleteUser(String id);

	/**
	 * Fetch all Users which are mapped with the given registration center
	 * 
	 * @throws MasterDataServiceException if any error occurred while updating
	 *                                    User
	 * 
	 * @param regCenterId Registration center id as String
	 * @return UserDetailsResponseDto response object which contain the list
	 *         of users those are mapped with the given registration center
	 *         {@link RegistrationCenterUserID}
	 */
	public List<UserDetailsExtnDto> getUsersByRegistrationCenter(String regCenterId, int pageNumber, int pageSize, String sortBy, String orderBy);

	
	/**
	 * Abstract method to create a user in the User Details database.
	 * 
	 * @param userDetailsDto user details
	 * 
	 * @return IdAndLanguageCodeID updated user id and language code.
	 * 
	 */
	public IdAndLanguageCodeID createUser(UserDetailsDto userDetailsDto);

	/**
	 * Abstract method to update User Details to the database
	 * 
	 * @param user User Put Request DTO
	 * 
	 * @return UserDetailsDto returning updated user {@link UserDetailsDto}
	 * 
	 * @throws MasterDataServiceException if any error occurred while updating
	 *                                    User
	 */
	public UserDetailsDto updateUser(UserDetailsDto user);


}
