package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.UserDetailsHistoryResponseDto;
import io.mosip.kernel.masterdata.entity.UserDetailsHistory;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
public interface UserDetailsHistoryService {
	/**
	 * @param userId    input from user
	 * @param effDTimes input from user
	 * @return user detail DTO for the particular input data
	 */
	UserDetailsHistoryResponseDto getByUserIdAndTimestamp(String userId, String effDTimes);

	/**
	 * @param userDetailsHistory 
	 * @return id response dto
	 */
	IdResponseDto createUserDetailsHistory(UserDetailsHistory userDetailsHistory); 
}
