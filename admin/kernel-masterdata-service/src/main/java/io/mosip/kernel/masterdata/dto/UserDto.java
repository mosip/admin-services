package io.mosip.kernel.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	private String userId;
	private String langCode;
	private String userPassword;
	private String name;
	private String role;
	private String token;

}
