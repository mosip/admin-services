package io.mosip.admin.bulkdataupload.entity.id;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidDocumentID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6394443536056430885L;

	@Column(name = "doctyp_code", nullable = false, length = 36)
	private String docTypeCode;

	@Column(name = "doccat_code", nullable = false, length = 36)
	private String docCategoryCode;

}
