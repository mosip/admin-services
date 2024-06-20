package io.mosip.kernel.masterdata.dto.request;

import io.mosip.kernel.masterdata.validator.PositiveValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto to hold the request page info
 * 
 * @author Abhishek Kumar
 * @since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

	@PositiveValue
	private int pageStart;

	@PositiveValue
	private int pageFetch = 10;

}
