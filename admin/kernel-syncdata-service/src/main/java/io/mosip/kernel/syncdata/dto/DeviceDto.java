package io.mosip.kernel.syncdata.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response dto for Device Detail
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto extends BaseDto {

	/**
	 * Field for device id
	 */
	private String id;
	/**
	 * Field for device name
	 */
	private String name;
	/**
	 * Field for device serial number
	 */
	private String serialNum;
	/**
	 * Field for device device specification Id
	 */
	private String deviceSpecId;
	/**
	 * Field for device mac address
	 */
	private String macAddress;
	/**
	 * Field for device ip address
	 */
	private String ipAddress;

	/**
	 * Valdity timestamp
	 */
	private LocalDateTime validityDateTime;

}
