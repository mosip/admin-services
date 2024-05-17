package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @author Srinivasan
 * @since 1.0.0
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reg_device_type", schema = "master")
public class DeviceTypeDPM extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6764549464759477685L;

	/** The code. */
	@Id
	@Column(name = "code")
	private String code;

	/** The name. */
	@Column(name = "name")
	private String name;

	/** The descr. */
	@Column(name = "descr")
	private String descr;

}
