package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity class to track history of mapped Registration center id and Device id.
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
@Table(name = "reg_center_device_h", schema = "master")
public class RegistrationCenterDeviceHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "regCenterId", column = @Column(name = "regcntr_id")),
			@AttributeOverride(name = "effectivetimes", column = @Column(name = "eff_dtimes")),
			@AttributeOverride(name = "deviceId", column = @Column(name = "device_id")) })
	private RegistrationCenterDeviceHistoryPk registrationCenterDeviceHistoryPk;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

}
