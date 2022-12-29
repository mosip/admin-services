package io.mosip.kernel.masterdata.dto;

import java.util.List;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model representing a response of dynamic field get by name Request")
public class DynamicFieldConsolidateResponseDto {
	
	private String name;
	
	private String description;
	
	private List<DynamicFieldCodeValueDTO> values; 
	
}
