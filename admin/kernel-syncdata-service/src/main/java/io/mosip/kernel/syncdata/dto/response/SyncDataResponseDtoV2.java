package io.mosip.kernel.syncdata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncDataResponseDtoV2 {
	
	private String lastSyncTime;
	private List<SyncDataBaseDtoV2> dataToSync;
}
