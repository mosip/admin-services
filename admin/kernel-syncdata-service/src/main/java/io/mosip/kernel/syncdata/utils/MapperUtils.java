package io.mosip.kernel.syncdata.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.mosip.kernel.syncdata.dto.RegistrationCenterUserDto;
import io.mosip.kernel.syncdata.dto.UserDetailDto;
import io.mosip.kernel.syncdata.dto.UserDetailMapDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Utility methods for mapping and JSON serialization.
 * Improves performance by using O(N+M) lookups and cached ObjectWriter.
 *
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Component
public class MapperUtils {

	private final ObjectWriter writer;

	@Autowired
	public MapperUtils(ObjectMapper objectMapper) {
		// Cache an ObjectWriter to reuse compiled serializers for faster serialization
		this.writer = objectMapper.writer();
	}

	/**
	 * Maps a list of UserDetailDto to UserDetailMapDto by matching with users from DB.
	 * Uses a HashMap for O(1) lookups instead of repeated linear scans.
	 */
	public static List<UserDetailMapDto> mapUserDetailsToUserDetailMap(
			List<UserDetailDto> userDetails,
			List<RegistrationCenterUserDto> usersFromDB) {

		if (userDetails == null || usersFromDB == null) {
			return Collections.emptyList();
		}

		// Pre-index usersFromDB by lowercase userId for quick lookup
		Map<String, RegistrationCenterUserDto> userMap = new HashMap<>(usersFromDB.size());
		for (RegistrationCenterUserDto dbUser : usersFromDB) {
			String id = dbUser.getUserId();
			if (id != null) {
				userMap.put(id.toLowerCase(Locale.ROOT), dbUser);
			}
		}

		List<UserDetailMapDto> result = new ArrayList<>(userDetails.size());
		for (UserDetailDto detail : userDetails) {
			if (detail.getUserId() == null) continue;

			RegistrationCenterUserDto dbUser =
					userMap.get(detail.getUserId().toLowerCase(Locale.ROOT));

			if (dbUser != null) {
				UserDetailMapDto dto = new UserDetailMapDto();
				dto.setUserName(detail.getUserId());
				dto.setMail(detail.getMail());
				dto.setMobile(detail.getMobile());
				dto.setLangCode(dbUser.getLangCode());
				dto.setName(detail.getName());
				dto.setUserPassword(null);
				dto.setIsActive(dbUser.getIsActive());
				dto.setIsDeleted(dbUser.getIsDeleted());
				dto.setRegCenterId(dbUser.getRegCenterId());
				// Split roles once into an immutable list
				dto.setRoles(List.of(detail.getRole().split(",")));
				result.add(dto);
			}
		}
		return result;
	}

	/**
	 * Convert an object to its JSON string representation.
	 * Uses a cached ObjectWriter for better performance.
	 */
	public String getObjectAsJsonString(Object object) throws JsonProcessingException {
		return object == null ? null : writer.writeValueAsString(object);
	}
}
