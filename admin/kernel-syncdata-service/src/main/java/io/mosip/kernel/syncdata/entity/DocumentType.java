package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ritesh Sinha
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doc_type", schema = "master")
@IdClass(CodeAndLanguageCodeID.class)
public class DocumentType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "code", column = @Column(name = "code", nullable = false)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	@OneToMany(mappedBy = "docTypeCode")
	private String code;
	@OneToMany(mappedBy = "langCode")
	private String langCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "descr")
	private String description;

}
