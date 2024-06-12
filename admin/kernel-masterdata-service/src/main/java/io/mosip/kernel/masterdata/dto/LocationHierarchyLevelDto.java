package io.mosip.kernel.masterdata.dto;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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
