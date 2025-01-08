package io.mosip.kernel.masterdata.dto.getresponse;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data

public class LocationHierarchyDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4552110961570300174L;

	@NotBlank
	private Short locationHierarchylevel;

	@NotBlank
	@Size(min = 1, max = 64)
	private String locationHierarchyName;

	@NotBlank
	private Boolean isActive;

}
