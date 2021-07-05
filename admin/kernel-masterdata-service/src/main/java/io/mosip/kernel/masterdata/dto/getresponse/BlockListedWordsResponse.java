package io.mosip.kernel.masterdata.dto.getresponse;

import lombok.Data;

/**
 * DTO class for blocklisted words response.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
@Data
public class BlockListedWordsResponse {
	/**
	 * The blocklisted word added.
	 */
	private String word;
	/**
	 * The language code of the word added.
	 */
	private String langCode;

}
