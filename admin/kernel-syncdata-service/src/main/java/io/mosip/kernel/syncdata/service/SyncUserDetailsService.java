package io.mosip.kernel.syncdata.service;

import io.mosip.kernel.syncdata.dto.SyncUserDto;
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
	 * get all the userDetails belonging to respective registration center based on keyindex provided
	 * @param keyIndex
	 * @return
	 */
	SyncUserDto getAllUserDetailsBasedOnKeyIndex(String keyIndex);

	/**
	 *
	 * @param userIds
	 * @return
	 */
	UserDetailResponseDto getUserDetailsFromAuthServer(List<String> userIds);

	/**
	 * get all the userids with their status belonging to respective registration center based on keyindex provided
	 * @param keyIndex
	 * @return
	 */
	SyncUserDto getAllUserDetailsBasedOnKeyIndexV2(String keyIndex);
}
