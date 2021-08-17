package io.mosip.kernel.masterdata.dto;

import lombok.Data;

import java.util.List;


@Data
public class DynamicFieldDataDto {

    private String fieldCode;
    private List<FieldKeyValues> fieldKeyValues;

}




