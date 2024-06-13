package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class for RegistrationCenterType.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 *
 */
@Data
@Entity
@Table(name = "reg_center_type", schema = "master")
@IdClass(CodeAndLanguageCodeID.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegistrationCenterType extends BaseEntity implements Serializable {

	/**
	 * Serializable version ID.
	 */
	private static final long serialVersionUID = 7869240207930949234L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "code", column = @Column(name = "code", length = 36)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	/**
	 * The code of the registration center type.
	 */
	@Column(name = "code", length = 36)
	private String code;

	/**
	 * The language code of the registration center type.
	 */
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	/**
	 * The name of the registration center type.
	 */
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	/**
	 * The description of the registration center type.
	 */
	@Column(name = "descr", length = 128)
	private String descr;

	@OneToMany(mappedBy = "registrationCenterType", fetch = FetchType.LAZY)
	private List<RegistrationCenter> registrationCenters;
}
