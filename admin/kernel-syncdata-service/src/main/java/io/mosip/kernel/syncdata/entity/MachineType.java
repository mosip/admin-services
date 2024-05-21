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
 * Entity for Machine Type
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "machine_type", schema = "master")
public class MachineType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	/**
	 * Field for composite primary key code and Language code
	 */
	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "lang_code")
	private String langCode;

	/**
	 * Field for Machine Type name
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	/**
	 * Field for Machine Type description
	 */
	@Column(name = "descr", length = 128)
	private String description;
}
