package io.mosip.kernel.masterdata.dto.getresponse.extn;

import java.time.LocalDateTime;

import io.mosip.kernel.masterdata.validator.FilterType;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCenterMappingExtnDto extends BaseDto{
	
	@ApiModelProperty(value = "id", required = true, dataType = "java.lang.String")
	private String userId;

	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String userName;

	@ApiModelProperty(value = "Language code", required = true, dataType = "java.lang.String")
	private String langCode;
	
	private String zoneName;
	
	private String regCenterName;
	
	private String regCenterId;

	private String statusCode;
	
	private LocalDateTime lastLoginDateTime;
	
	private String lastLoginMethod;
	
	private String zoneCode;

}
