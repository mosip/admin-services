package io.mosip.kernel.syncdata.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LocationHierarchyDto extends BaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4552110961570300174L;

	private Short locationHierarchylevel;

	private String locationHierarchyName;

}
