package io.mosip.kernel.syncdata.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncUserDto {

    private String lastSyncTime;
    private String userDetails;
}
