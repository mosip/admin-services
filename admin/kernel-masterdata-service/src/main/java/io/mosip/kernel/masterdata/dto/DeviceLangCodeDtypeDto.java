/**
 * 
 *
 */
package io.mosip.kernel.masterdata.dto;

import java.time.LocalDateTime;

import io.mosip.kernel.masterdata.validator.ValidLangCode;
import lombok.Data;

/**
 * Response dto for Device Details for given Language code and device type
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Data
public class DeviceLangCodeDtypeDto {

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
	private String deviceSpecId;
	/**
	 * Field for device mac address
	 */
	private String macAddress;
	/**
	 * Field for language code
	 */
	@ValidLangCode(message = "Language Code is Invalid")
	private String langCode;
	/**
	 * Field for is active
	 */
	private Boolean isActive;

	/**
	 * Field for device type
	 */
	private String deviceTypeCode;
	/**
	 * Field to hold date and time for Validity of the Device
	 */
	private LocalDateTime validityEndDateTime;

}
