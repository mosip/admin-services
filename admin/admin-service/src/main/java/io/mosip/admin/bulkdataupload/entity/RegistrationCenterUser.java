package io.mosip.admin.bulkdataupload.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterUserID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reg_center_user", schema = "master")
public class RegistrationCenterUser extends BaseEntity {

	/**
	 * Composite key for this table
	 */
	@EmbeddedId
	@AttributeOverride(name = "regCenterId", column = @Column(name = "regcntr_id"))
	@AttributeOverride(name = "userId", column = @Column(name = "usr_id"))
	private RegistrationCenterUserID registrationCenterUserID;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;
}
