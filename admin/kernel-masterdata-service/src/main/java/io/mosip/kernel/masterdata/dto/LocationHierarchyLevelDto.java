package io.mosip.kernel.masterdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationHierarchyLevelDto {

	@Range(min = 0)
	private short hierarchyLevel;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String hierarchyLevelName;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	@NotNull
	private Boolean isActive;

}
