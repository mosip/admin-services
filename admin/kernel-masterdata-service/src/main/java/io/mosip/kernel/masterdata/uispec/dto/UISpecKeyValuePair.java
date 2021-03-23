package io.mosip.kernel.masterdata.uispec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UISpecKeyValuePair {	
	private String type;
	private String spec;
}
