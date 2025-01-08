package io.mosip.kernel.masterdata.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response dto for Device Detail
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Data
public class DevicePutReqDto {

	/**
	 * Field for device id
	 */

	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "id", required = false, dataType = "java.lang.String")
	private String id;
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

	@Deprecated
	private Boolean isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime validityDateTime;

	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "zoneCode", required = true, dataType = "java.lang.String")
	private String zoneCode;
	
	@ApiModelProperty(value = "regCenterId", required = true, dataType = "java.lang.String")
	private String regCenterId;

}
