package io.mosip.kernel.masterdata.dto.getresponse.extn;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class WorkingDaysExtnDto {

    private String code;

    private String langCode;

    private String name;

    private short daySeq;

    private boolean isGlobalWorking;
}
