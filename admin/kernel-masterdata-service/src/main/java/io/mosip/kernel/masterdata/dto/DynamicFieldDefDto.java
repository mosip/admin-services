package io.mosip.kernel.masterdata.dto;

import lombok.Data;

@Data
public class DynamicFieldDefDto {

    private String name;
    private String description;
    private Boolean isActive;
}
