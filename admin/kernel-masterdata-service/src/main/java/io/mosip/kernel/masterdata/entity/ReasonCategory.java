package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reason_category", schema = "master")
@IdClass(CodeAndLanguageCodeID.class)
public class ReasonCategory extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1440279821197074364L;

	@Id
	@Column(name = "code", nullable = false)
	private String code;

	@Id
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "name")
	private String name;

	@Column(name = "descr")
	private String description;

	@OneToMany(mappedBy = "reasonCategory", cascade = CascadeType.ALL)
	private List<ReasonList> reasonList = new ArrayList<>();

}
