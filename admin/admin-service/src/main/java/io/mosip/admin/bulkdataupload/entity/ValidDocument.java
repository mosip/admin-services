package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.ValidDocumentID;
import io.mosip.admin.validator.DocCatCode;
import io.mosip.admin.validator.DocTypeCode;
import io.mosip.admin.bulkdataupload.constant.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @Column(name = "doctyp_code", nullable = false, length = 36)
	@DocTypeCode(message = ErrorConstants.INVALID_DOC_TYPE_CODE)
	private String docTypeCode;

	@Id
	@Column(name = "doccat_code", nullable = false, length = 36)
	@DocCatCode(message = ErrorConstants.INVALID_DOC_CAT_CODE)
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
