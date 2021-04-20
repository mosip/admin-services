package io.mosip.kernel.masterdata.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDtoWithoutLangCode {

	@NotNull
	@Valid
	private List<SearchFilter> filters;

	@NotNull
	private List<SearchSort> sort;

	// @NotNull
	private Pagination pagination;

	/*
	 * @ValidLangCode(message = "Language Code is Invalid") private String
	 * languageCode;
	 */
}
