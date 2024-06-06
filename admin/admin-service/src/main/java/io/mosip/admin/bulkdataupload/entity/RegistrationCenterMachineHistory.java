package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineHistoryID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity class to track history of mapped Registration center id and Machine
 * id.
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
@Table(name = "reg_center_machine_h", schema = "master")
public class RegistrationCenterMachineHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "regCenterId", column = @Column(name = "regcntr_id")),
			@AttributeOverride(name = "effectivetimes", column = @Column(name = "eff_dtimes")),
			@AttributeOverride(name = "machineId", column = @Column(name = "machine_id")) })
	private RegistrationCenterMachineHistoryID registrationCenterMachineHistoryPk;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

}
