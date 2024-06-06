package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Neha
 * @since 1.0.0
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "biometric_type", schema = "master")
public class BiometricType extends BaseEntity implements Serializable {

	/**
	 * Generated serialization id
	 */
	private static final long serialVersionUID = 4605128758645778470L;

	@Id
	@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false))
	private String code;

	@Column(name = "lang_code", nullable = true, length = 3)
	private String langCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "descr")
	private String description;

}
