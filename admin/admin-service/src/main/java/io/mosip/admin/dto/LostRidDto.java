package io.mosip.admin.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LostRidDto {

	private String registrationId;

	private String registartionDate;

	Map<String, String> additionalInfo= new HashMap<>();

	private String syncDateTime;

}
