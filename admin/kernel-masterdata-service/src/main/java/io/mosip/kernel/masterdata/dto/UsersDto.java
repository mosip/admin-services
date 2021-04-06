package io.mosip.kernel.masterdata.dto;

import java.util.List;

public class UsersDto {
	List<UserDto> mosipUserDtoList;

	public List<UserDto> getMosipUserDtoList() {
		return mosipUserDtoList;
	}

	public void setMosipUserDtoList(List<UserDto> usersDto) {
		this.mosipUserDtoList = usersDto;
	}

}
