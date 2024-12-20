package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.IdAndEffectDateTimeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_master_h", schema = "master")
@IdClass(IdAndEffectDateTimeID.class)
public class DeviceHistory extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585825705521742941L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false, length = 36)),
			@AttributeOverride(name = "effectDateTime", column = @Column(name = "eff_dtimes", nullable = false)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	private String id;
	private LocalDateTime effectDateTime;
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

	@Column(name = "zone_code", length = 36)
	private String zoneCode;
	
	@Column(name = "regcntr_id", length = 10)
	private String regCenterId;

}
