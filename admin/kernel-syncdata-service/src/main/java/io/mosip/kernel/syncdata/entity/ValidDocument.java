package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.ValidDocumentID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity class for valid document.
 * 
 * @author Urvil Joshi
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
@Entity
@Table(name = "valid_document", schema = "master")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ValidDocumentID.class)
public class ValidDocument extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3111581667845281498L;

	@Id
	@AttributeOverrides({
			@AttributeOverride(name = "docTypeCode", column = @Column(name = "doctyp_code", nullable = false, length = 36)),
			@AttributeOverride(name = "docCategoryCode", column = @Column(name = "doccat_code", nullable = false, length = 36)) })

	private String docTypeCode;

	private String docCategoryCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "doccat_code", referencedColumnName = "code", insertable = false, updatable = false),
			@JoinColumn(name = "lang_code", referencedColumnName = "lang_code", insertable = false, updatable = false), })
	DocumentCategory documentCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "doctyp_code", referencedColumnName = "code", insertable = false, updatable = false),
			@JoinColumn(name = "lang_code", referencedColumnName = "lang_code", insertable = false, updatable = false) })
	DocumentType documentType;

	@Column(name = "lang_code", length = 3)
	private String langCode;

}
