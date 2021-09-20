package io.mosip.kernel.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private String userId;
	/*private String mobile;
	private String mail;*/
	private String langCode;
	private String userPassword;
	private String name;
	private String role;
	private String rId;
	private String token;

}
