package io.mosip.kernel.syncdata.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @author Srinivasan
 *
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantValidDocumentID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "apptyp_code")
	private String appTypeCode;

	@Column(name = "doccat_code")
	private String docCatCode;

	@Column(name = "doctyp_code")
	private String docTypeCode;

}
