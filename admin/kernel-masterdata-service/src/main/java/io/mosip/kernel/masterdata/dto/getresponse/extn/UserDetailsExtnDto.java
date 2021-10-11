package io.mosip.kernel.masterdata.dto.getresponse.extn;

import java.time.LocalDateTime;

import io.mosip.kernel.masterdata.validator.FilterType;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response Dto for Template details
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
//@ApiModel(value = "Template", description = "Template resource representation")
public class UserDetailsExtnDto extends BaseDto {

	@FilterType(types = { FilterTypeEnum.EQUALS })
	@ApiModelProperty(value = "id", required = true, dataType = "java.lang.String")
	private String id;

	@FilterType(types = { FilterTypeEnum.EQUALS, FilterTypeEnum.STARTSWITH, FilterTypeEnum.CONTAINS })
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	@FilterType(types = { FilterTypeEnum.EQUALS })
	@ApiModelProperty(value = "Language code", required = true, dataType = "java.lang.String")
	private String langCode;
	
	private String regCenterId;

	private String statusCode;
	
	private LocalDateTime lastLoginDateTime;
	
	private String lastLoginMethod;
	
	private String zoneCode;
}