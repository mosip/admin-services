package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * Entity for language
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "language", schema = "master")
public class Language extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585825705521742941L;

	/**
	 * Field for language code
	 */
	@Id
	@Column(name = "code", unique = true, nullable = false, length = 3)
	private String code;

	/**
	 * Field for language name
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	/**
	 * Field for language family
	 */
	@Column(name = "family", length = 64)
	private String family;

	/**
	 * Field for language native name
	 */
	@Column(name = "native_name", length = 64)
	private String nativeName;

}
