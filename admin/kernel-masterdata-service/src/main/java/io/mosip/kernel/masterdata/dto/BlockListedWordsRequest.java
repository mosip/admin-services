package io.mosip.kernel.masterdata.dto;

import lombok.Data;

/**
 * Class for blocklisted words request.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 *
 */
@Data
public class BlockListedWordsRequest {
	/**
	 * The data for blocklisted word.
	 */
	private BlocklistedWordsDto blocklistedword;
}
