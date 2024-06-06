package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.CodeAndLanguageCodeID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Neha
 * @since 1.0.0
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template_file_format", schema = "master")
@EqualsAndHashCode(callSuper = true)
@IdClass(CodeAndLanguageCodeID.class)
public class TemplateFileFormat extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1051422672381211978L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "code", column = @Column(name = "code", nullable = false)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	private String code;
	private String langCode;

	@Column(name = "descr")
	private String description;

}
