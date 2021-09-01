package io.mosip.kernel.masterdata.entity;

import lombok.Data;

@Data
public class DynamicFieldData {

    private String fieldCode;
    private String key;
    private String value;
    private String langCode;

}
