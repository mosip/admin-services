package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;
import java.util.List;

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
 * 
 * @author Srinivasan
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location", schema = "master")
@IdClass(CodeAndLanguageCodeID.class)
public class Location extends BaseEntity implements Serializable {

	/**
	 * generated serial Id
	 */

	private static final long serialVersionUID = -5585825705521742941L;

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "name", nullable = false, length = 128)
	private String name;

	@Column(name = "hierarchy_level", nullable = false)
	private short hierarchyLevel;

	@Column(name = "hierarchy_level_name", nullable = false, length = 64)
	private String hierarchyName;

	@Column(name = "parent_loc_code", nullable = false, length = 32)
	private String parentLocCode;

	@Id
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
	private List<RegistrationCenter> registrationCenters;

}
