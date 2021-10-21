package io.mosip.kernel.syncdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response class for Device specification save
 * 
 * @author Megha Tanga
 * @version 1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeCodeAndLanguageCodeAndId extends BaseDto {

	private String id;
	private String deviceTypeCode;

}
