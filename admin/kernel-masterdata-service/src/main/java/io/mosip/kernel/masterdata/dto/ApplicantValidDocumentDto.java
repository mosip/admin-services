package io.mosip.kernel.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Srinivasan
 *
 */
@Data
public class ApplicantValidDocumentDto {

	private String appTypeCode;
	private String langCode;
	private Boolean isActive;
	private Collection<DocumentCategoryAndTypeResponseDto> documentCategories;

}
