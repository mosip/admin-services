package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Neha
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
@Entity
@Table(name = "doc_category", schema = "master")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CodeAndLanguageCodeID.class)
public class DocumentCategory extends BaseEntity implements Serializable {

	/**
	 * Generated serialization id
	 */
	private static final long serialVersionUID = 1582360946027855765L;
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "code", column = @Column(name = "code", nullable = false)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	@OneToMany(mappedBy = "docCategoryCode")
	private String code;
	@OneToMany(mappedBy = "langCode")
	private String langCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "descr")
	private String description;

}
