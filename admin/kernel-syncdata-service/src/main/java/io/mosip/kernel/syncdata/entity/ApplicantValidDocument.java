package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "applicant_valid_document", schema = "master")
public class ApplicantValidDocument extends BaseEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -6722875314198649346L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "appTypeCode", column = @Column(name = "apptyp_code", nullable = false)),
			@AttributeOverride(name = "docCatcode", column = @Column(name = "doccat_code", nullable = false)),
			@AttributeOverride(name = "docTypeCode", column = @Column(name = "doctyp_code", nullable = false)) })
	private ApplicantValidDocumentID applicantValidDocumentId;

	@Column(name = "lang_code")
	private String langCode;

}
