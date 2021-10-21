package io.mosip.kernel.syncdata.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response dto for Device Details for given Language code and device type
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLangCodeDtypeDto extends BaseDto {

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
	 * Field for Ip Address
	 */
	private String ipAddress;
	/**
	 * Field for device specification Id
	 */
	private String dspecId;
	/**
	 * Field for device mac address
	 */
	private String macAddress;

	/**
	 * Field for device type
	 */
	private String deviceTypeCode;
	/**
	 * Field to hold date and time for Validity of the Device
	 */
	private LocalDateTime validityEndDateTime;

}
