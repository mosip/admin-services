package io.mosip.kernel.masterdata.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.mosip.kernel.masterdata.validator.ValidLangCode;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Filter request dto
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterValueDto {
	@NotNull
	@Valid
	private List<FilterDto> filters;
	// @NotBlank
	@ValidLangCode(message = "Language Code is Invalid")
	private String languageCode;

	private List<SearchFilter> optionalFilters;

	@ApiModelProperty(value = "Number of records to be fetched", required = false)
	private int pageFetch;

	@ApiModelProperty(value = "Should be true to get total count based on the provided filters",
			required = false)
	private boolean totalCountRequired = false;
}
