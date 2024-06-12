package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.IdAndLanguageCodeID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Neha
 * @author Uday Kumar
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template", schema = "master")
@IdClass(IdAndLanguageCodeID.class)
public class Template extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3582557516673178996L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false, length = 36)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	private String id;
	private String langCode;

	@Column(name = "name", nullable = false, length = 128)
	private String name;

	@Column(name = "descr", length = 256)
	private String description;

	@Column(name = "file_format_code", length = 36)
	private String fileFormatCode;

	@Column(name = "model", length = 128)
	private String model;

	@Column(name = "file_txt", length = 4086)
	private String fileText;

	@Column(name = "module_id", length = 36)
	private String moduleId;

	@Column(name = "module_name", length = 128)
	private String moduleName;

	@Column(name = "template_typ_code", length = 36)
	private String templateTypeCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "module_id", referencedColumnName = "id", insertable = false, updatable = false) })
	private ModuleDetail moduleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_format_code", referencedColumnName = "code", insertable = false, updatable = false)
	private TemplateFileFormat templateFileFormat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_typ_code", referencedColumnName = "code", insertable = false, updatable = false)
	private TemplateType templateType;

}
