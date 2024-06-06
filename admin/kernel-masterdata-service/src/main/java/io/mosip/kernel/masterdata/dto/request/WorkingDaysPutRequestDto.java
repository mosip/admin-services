package io.mosip.kernel.masterdata.dto.request;

import lombok.Data;


import jakarta.validation.constraints.NotNull;

/**
 * DTO class for workingdays.
 *
 * @author Dhanendra Sahu
 * @since 1.2.0
 */
@Data
public class WorkingDaysPutRequestDto {

    @NotNull
    private String code;

    @NotNull
    private String langCode;

    private String name;

    private short daySeq;

    @NotNull
    private boolean isGlobalWorking;
}
