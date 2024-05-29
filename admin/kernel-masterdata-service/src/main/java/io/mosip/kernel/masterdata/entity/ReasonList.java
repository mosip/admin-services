package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.CodeLangCodeAndRsnCatCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reason_list", schema = "master")
@IdClass(CodeLangCodeAndRsnCatCodeID.class)
public class ReasonList extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -572990183711593868L;

	@Id
	@Column(name = "rsncat_code", nullable = false, length = 36)
	private String rsnCatCode;
	@Id
	@Column(name = "code", nullable = false, length = 36)
	private String code;
	@Id
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "name", nullable = false, length = 64)
	private String name;

	@Column(name = "descr", length = 256)
	private String description;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "rsncat_code", referencedColumnName = "code", insertable = false, updatable = false),
			@JoinColumn(name = "lang_code", referencedColumnName = "lang_code", insertable = false, updatable = false) })
	private ReasonCategory reasonCategory;

}
