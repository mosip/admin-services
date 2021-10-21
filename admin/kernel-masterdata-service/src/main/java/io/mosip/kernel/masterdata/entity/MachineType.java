package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@IdClass(CodeAndLanguageCodeID.class)
public class MachineType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541947587557590379L;

	/**
	 * Field for composite primary key code and Language code
	 */
	@Id
	@Column(name = "code")
	private String code;

	@Id
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
