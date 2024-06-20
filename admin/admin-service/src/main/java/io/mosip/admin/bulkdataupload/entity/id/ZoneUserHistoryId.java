package io.mosip.admin.bulkdataupload.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneUserHistoryId implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -4334937137252000426L;

	@Column(name = "zone_code", unique = true, nullable = false, length = 36)
	private String zoneCode;

	@Column(name = "usr_id", unique = true, nullable = false, length = 256)
	private String userId;
	
	@Column(name = "eff_dtimes", nullable = false)
	private LocalDateTime effDTimes;
}