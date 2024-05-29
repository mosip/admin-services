package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineDeviceHistoryID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity class to track history of mapped Registration center id, Machine id
 * and Device id.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reg_center_machine_device_h", schema = "master")
public class RegistrationCenterMachineDeviceHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "regCenterId", column = @Column(name = "regcntr_id")),
			@AttributeOverride(name = "machineId", column = @Column(name = "machine_id")),
			@AttributeOverride(name = "deviceId", column = @Column(name = "device_id")),
			@AttributeOverride(name = "effectivetimes", column = @Column(name = "eff_dtimes")) })
	private RegistrationCenterMachineDeviceHistoryID registrationCenterMachineDeviceHistoryPk;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

}
