package io.mosip.kernel.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicFieldNameDto {

    private String name;
    private String description;
    private String langCode;
    private Boolean isActive;
}
