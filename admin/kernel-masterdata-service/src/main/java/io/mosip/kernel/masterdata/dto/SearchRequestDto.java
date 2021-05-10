package io.mosip.kernel.masterdata.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {

	@NotNull
	@Valid
	private List<SearchRequestFilter> filters;

	@NotNull
	private SearchSort sort;

	private Pagination pagination;

}
