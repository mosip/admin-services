package io.mosip.kernel.masterdata.dto;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 */
@Data
public class UserDetailsDto  {

	@NotNull
	@StringFormatter(min = 1, max = 36)
	private String id;


	@Null
	@StringFormatter(min = 0, max = 64)
	private String name;

	@Null
	@StringFormatter(min = 0, max = 16)
	private String statusCode;
	
	@NotNull
	@StringFormatter(min = 1, max = 10)
	private String regCenterId;

	@NotNull
	private Boolean isActive;

	
	private String langCode;
	
	
	public String getId() {
		return this.id.toLowerCase();
	}

}
