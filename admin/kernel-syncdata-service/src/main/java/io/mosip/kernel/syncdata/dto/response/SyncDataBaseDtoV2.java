package io.mosip.kernel.syncdata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncDataBaseDtoV2 extends SyncDataBaseDto {

	private String url;

}
