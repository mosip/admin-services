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

import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineUserHistoryID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for User and Registration mappings
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@IdClass(RegistrationCenterMachineUserHistoryID.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reg_center_user_machine_h", schema = "master")
public class RegistrationCenterUserMachineHistory extends BaseEntity implements Serializable {

	/**
	 * Generated Serial Id
	 */
	private static final long serialVersionUID = -4167453471874926985L;

	/**
	 * Composite key for this table
	 */
	@Id
	@AttributeOverrides({
			@AttributeOverride(name = "cntrId", column = @Column(name = "regcntr_id", nullable = false, length = 36)),
			@AttributeOverride(name = "usrId", column = @Column(name = "usr_id", nullable = false, length = 36)),
			@AttributeOverride(name = "machineId", column = @Column(name = "machine_id", nullable = false, length = 36)),
			@AttributeOverride(name = "effectivetimes", column = @Column(name = "eff_dtimes", nullable = false)), })

	/**
	 * Center Id
	 */
	private String cntrId;

	/**
	 * User Id
	 */
	private String usrId;

	/**
	 * Machine Id
	 */
	private String machineId;

	/**
	 * Effective TimeStamp
	 */
	private LocalDateTime effectivetimes;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;
}
