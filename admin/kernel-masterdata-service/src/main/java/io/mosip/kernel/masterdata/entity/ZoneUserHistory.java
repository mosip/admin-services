package io.mosip.kernel.masterdata.entity;



import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.ZoneUserHistoryId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZoneUserHistoryId.class)
@Table(name = "zone_user_h", schema = "master")
public class ZoneUserHistory extends BaseEntity implements Serializable {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8194849518681293756L;

	@Id
	@Column(name = "zone_code", nullable = false, length = 36)
	private String zoneCode;

	@Id
	@Column(name = "usr_id", nullable = false, length = 256)
	private String userId;
	
	@Id
	@Column(name = "eff_dtimes", nullable = false)
	private LocalDateTime effDTimes;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;
}
