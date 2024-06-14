package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * Entity for Device Details
 * 
 */
/**
 * @author Sidhant Agarwal
 * @author Megha Tanga
 * @since 1.0.1
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_master", schema = "master")
public class Device extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585825705521742941L;

	@Id
	@Column(name = "id", nullable = false, length = 36)
	private String id;

	@Column(name = "lang_code", nullable = true, length = 3)
	private String langCode;

	/**
	 * Field for device name
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	/**
	 * Field for device serial number
	 */
	@Column(name = "serial_num", nullable = false, length = 64)
	private String serialNum;

	/**
	 * Field for device ip address
	 */
	@Column(name = "ip_address", length = 17)
	private String ipAddress;

	/**
	 * Field for device mac address
	 */
	@Column(name = "mac_address", nullable = false, length = 64)
	private String macAddress;

	/**
	 * Field for device specific id
	 */
	@Column(name = "dspec_id", nullable = false, length = 36)
	private String deviceSpecId;

	@Column(name = "validity_end_dtimes")
	private LocalDateTime validityDateTime;
	
	@Column(name = "regcntr_id", length = 10)
	private String regCenterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dspec_id", referencedColumnName = "id", insertable = false, updatable = false)
	private DeviceSpecification deviceSpecification;

}
