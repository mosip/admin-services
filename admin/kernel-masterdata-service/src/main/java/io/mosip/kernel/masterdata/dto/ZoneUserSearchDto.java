package io.mosip.kernel.masterdata.dto;

import lombok.Data;

@Data
public class ZoneUserSearchDto  extends ZoneUserExtnDto{
	
	private String userName;
	
	private String zoneName;

}
