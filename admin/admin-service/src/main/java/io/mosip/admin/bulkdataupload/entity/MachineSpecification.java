package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.IdAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity for Machine Specifications
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
@Table(name = "machine_spec", schema = "master")
public class MachineSpecification extends BaseEntity implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false, length = 36))
	private String id;
	
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	/**
	 * Field for machine Specification name
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	/**
	 * Field for machine Specification brand
	 */
	@Column(name = "brand", nullable = false, length = 32)
	private String brand;

	/**
	 * Field for machine Specification model
	 */
	@Column(name = "model", nullable = false, length = 16)
	private String model;

	/**
	 * Field for machine Specification machineTypeCode
	 */
	@Column(name = "mtyp_code", nullable = false, length = 36)
	private String machineTypeCode;

	/**
	 * Field for machine Specification minDriverversion
	 */
	@Column(name = "min_driver_ver", nullable = false, length = 16)
	private String minDriverversion;

	/**
	 * Field for machine Specification description
	 */
	@Column(name = "descr", length = 256)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "mtyp_code", referencedColumnName = "code", insertable = false, updatable = false),
			@JoinColumn(name = "lang_code", referencedColumnName = "lang_code", insertable = false, updatable = false) })
	private MachineType machineType;

}
