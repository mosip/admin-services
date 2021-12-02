package io.mosip.kernel.syncdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EntityDtimes {

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private LocalDateTime deletedDateTime;

}