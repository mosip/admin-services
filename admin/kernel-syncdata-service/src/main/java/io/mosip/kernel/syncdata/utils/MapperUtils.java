package io.mosip.kernel.syncdata.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.syncdata.dto.RegistrationCenterUserDto;
import io.mosip.kernel.syncdata.dto.UserDetailDto;
import io.mosip.kernel.syncdata.dto.UserDetailMapDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Component
public class MapperUtils {

	@Autowired
	private ObjectMapper objectMapper;

	public static List<UserDetailMapDto> mapUserDetailsToUserDetailMap(List<UserDetailDto> userDetails,
																	   List<RegistrationCenterUserDto> usersFromDB) {
		List<UserDetailMapDto> userDetailMapDtoList = new ArrayList<>();

		for (UserDetailDto userDetail : userDetails) {
			Optional<RegistrationCenterUserDto> userDto = usersFromDB.stream()
					.filter(user -> userDetail.getUserId().equalsIgnoreCase(user.getUserId())).findFirst();

			if(userDto.isPresent()) {
				UserDetailMapDto userDetailMapDto = new UserDetailMapDto();
				userDetailMapDto.setUserName(userDetail.getUserId());
				userDetailMapDto.setMail(userDetail.getMail());
				userDetailMapDto.setMobile(userDetail.getMobile());
				userDetailMapDto.setLangCode(userDto.get().getLangCode());
				userDetailMapDto.setName(userDetail.getName());
				userDetailMapDto.setUserPassword(null);
				userDetailMapDto.setIsActive(userDto.get().getIsActive());
				userDetailMapDto.setIsDeleted(userDto.get().getIsDeleted());
				userDetailMapDto.setRegCenterId(userDto.get().getRegCenterId());
				List<String> roles = Arrays.asList(userDetail.getRole().split(","));
				userDetailMapDto.setRoles(roles);
				userDetailMapDtoList.add(userDetailMapDto);
			}
		}
		return userDetailMapDtoList;
	}
	
	public String getObjectAsJsonString(Object object) throws Exception {
		return (null != object) ? objectMapper.writeValueAsString(object) : null;
	}
}
