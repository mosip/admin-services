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
 * @author Srinivasan
 * @since 1.0.0
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationCreateDto {

	@NotNull
	@StringFormatter(min = 1, max = 32)
	private String code;

	@NotNull
	@StringFormatter(min = 0, max = 128)
	private String name;

	@Range(min = 0)
	private short hierarchyLevel;

	@NotNull
	@StringFormatter(min = 1, max = 64)
	private String hierarchyName;

	private String parentLocCode;

	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;

	@NotNull
	private Boolean isActive;

}
