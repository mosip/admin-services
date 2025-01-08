package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineUserID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for User and Registration mappings
 * 
 * @author Dharmesh Khandelwal
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reg_center_user_machine", schema = "master")
public class RegistrationCenterUserMachine extends BaseEntity implements Serializable {

	/**
	 * Generated Serial Id
	 */
	private static final long serialVersionUID = -4167453471874926985L;

	/**
	 * Composite key for this table
	 */
	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "cntrId", column = @Column(name = "regcntr_id")),
			@AttributeOverride(name = "usrId", column = @Column(name = "usr_id")),
			@AttributeOverride(name = "machineId", column = @Column(name = "machine_id")) })
	private RegistrationCenterMachineUserID registrationCenterMachineUserID;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

}
