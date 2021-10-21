package io.mosip.kernel.masterdata.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Registered Device Service DTO
 * 
 * @author Ramadurai Pandian
 * @since 1.0.0
 *
 */
@Entity
@Table(name = "registered_device_master_h", schema = "master")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredDeviceHistory extends BaseEntity {

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "dtype_code")
	private String deviceTypeCode;

	@Column(name = "dstype_code")
	private String deviceSTypeCode;

	@Column(name = "status_code")
	private String statusCode;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "device_sub_id")
	private String deviceSubId;

	@Column(name = "serial_number")
	private String serialNo;

	@Column(name = "provider_id")
	private String dpId;

	@Column(name = "provider_name")
	private String dp;

	@Column(name = "purpose")
	private String purpose;

	@Column(name = "firmware")
	private String firmware;

	@Column(name = "make")
	private String make;

	@Column(name = "model")
	private String model;

	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;

	@Column(name = "certification_level")
	private String certificationLevel;

	@Column(name = "foundational_trust_provider_id")
	private String foundationalTPId;

	@Column(name = "eff_dtimes")
	private LocalDateTime effectivetimes;

	// json inner class
	@Column(name = "digital_id", length = 1024)
	private String digitalId;

}
