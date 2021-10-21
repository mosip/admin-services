package io.mosip.kernel.syncdata.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
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
	private int hierarchyLevel;

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
