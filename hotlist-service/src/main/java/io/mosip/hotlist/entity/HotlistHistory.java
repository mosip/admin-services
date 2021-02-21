package io.mosip.hotlist.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(HotlistPK.class)
@Data
@NoArgsConstructor
@Table(schema = "hotlist", name = "hotlist_h")
public class HotlistHistory {

	@Id
	public String idHash;

	public String idValue;

	@Id
	public String idType;

	public String status;

	@Column(name = "start_timestamp")
	public LocalDateTime startDTimes;

	@Column(name = "expiry_timestamp")
	public LocalDateTime expiryDTimes;

}
