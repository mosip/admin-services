package io.mosip.kernel.masterdata.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DynamicFieldDefDto {

    private String name;
    private Map<String, String> descriptionData;
    private Boolean isActive;
}
