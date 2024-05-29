package io.mosip.kernel.masterdata.entity;

import jakarta.persistence.Id;

public class DynamicFieldDefinition {

    @Id
    private String code;
    //Map<String, String> as jsonString
    private String descriptionData;
    private Boolean isActive;
}
