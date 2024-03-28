package io.mosip.kernel.masterdata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AddressDto {
    @ApiModelProperty(
            notes = "Request Timestamp",
            example = "2023-07-26T06:12:52.994Z",
            required = true
    )
    private LocalDateTime effectiveDate;
    private Map<String, Object> address;
}
