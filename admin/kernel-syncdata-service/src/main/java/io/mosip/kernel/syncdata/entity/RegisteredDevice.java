package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Registered Device Service DTO
 * 
 * @author Srinivasan
 * @since 1.0.0
 *
 */
@Entity
@Table(name = "registered_device_master", schema = "master")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredDevice extends BaseEntity {

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "dtype_code")
	private String dTypeCode;

	@Column(name = "dstype_code")
	private String dsTypeCode;

	@Column(name = "status_code")
	private String statusCode;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "device_sub_id")
	private String deviceSubId;

	@Column(name = "digital_id")
	private String digitalId;

	@Column(name = "serial_number")
	private String serialNumber;

	@Column(name = "provider_id")
	private String providerId;

	@Column(name = "provider_name")
	private String providerName;

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

}
