package io.mosip.kernel.masterdata.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response dto for Device Detail
 * 
 * @author Megha Tanga
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDto {

	/**
	 * Field for device name
	 */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;
	/**
	 * Field for device serial number
	 */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "serialNum", required = true, dataType = "java.lang.String")
	private String serialNum;
	/**
	 * Field for device device specification Id
	 */
	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "deviceSpecId", required = true, dataType = "java.lang.String")
	private String deviceSpecId;
	/**
	 * Field for device mac address
	 */
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "macAddress", required = true, dataType = "java.lang.String")
	private String macAddress;
	/**
	 * Field for device ip address
	 */

	@Size(min = 0, max = 17)
	@ApiModelProperty(value = "ipAddress", required = true, dataType = "java.lang.String")
	private String ipAddress;
	/**
	 * Field for language code
	 */
	@Deprecated
	private String langCode;
	/**
	 * Field for is active
	 */
	@NotNull
	private Boolean isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime validityDateTime;

	@StringFormatter(min = 0, max = 36)
	private String zoneCode;
	
	@StringFormatter(min = 0, max = 10)
	private String regCenterId;

}