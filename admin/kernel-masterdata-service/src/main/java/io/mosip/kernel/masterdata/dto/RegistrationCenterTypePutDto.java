package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.mosip.kernel.masterdata.validator.CenterTypeCode;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationCenterTypePutDto {

	/**
	 * code of the registration center type.
	 */
	@NotNull
	@CenterTypeCode(min =2, max = 36)
	private String code;
	/**
	 * language code of the registration center type.
	 */
	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;
	/**
	 * name of the registration center type.
	 */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String name;

	@Deprecated
	private Boolean isActive;

	/**
	 * 
	 * description of the registration center type.
	 */

	@Size(min = 0, max = 128)
	private String descr;

}
