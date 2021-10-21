package io.mosip.kernel.syncdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO class for IdType fetch response.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdTypeDto extends BaseDto {
	/**
	 * The id code.
	 */
	private String code;

	private String name;

	/**
	 * The id description.
	 */
	private String descr;

}
