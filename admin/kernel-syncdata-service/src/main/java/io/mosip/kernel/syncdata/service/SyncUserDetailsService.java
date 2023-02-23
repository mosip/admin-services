package io.mosip.kernel.syncdata.service;

import io.mosip.kernel.syncdata.dto.SyncUserDetailDto;
import io.mosip.kernel.syncdata.dto.SyncUserDto;
import io.mosip.kernel.syncdata.dto.SyncUserSaltDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;

import java.util.List;

/**
 * This service class handles CRUD opertaion method signature
 * 
 * @author Srinivasan
 * @author Megha Tanga
 *
 */
public interface SyncUserDetailsService {

	/**
	 * This method would fetch all user details for that registration center id
	 * 
	 * @param regId - registration center id
	 * @return {@link SyncUserDetailDto}
	 */
	@Deprecated
	SyncUserDetailDto getAllUserDetail(String regId);

	/**
	 * Gets the user salts.
	 *
	 * @param regId the reg id
	 * @return the user salts
	 */
	@Deprecated
	SyncUserSaltDto getUserSalts(String regId);

	/**
	 * get all the userDetails belonging to respective registration center based on keyindex provided
	 * @param keyIndex
	 * @return
	 */
	SyncUserDto getAllUserDetailsBasedOnKeyIndex(String keyIndex);

	UserDetailResponseDto getUserDetailsFromAuthServer(List<String> userIds);
}
