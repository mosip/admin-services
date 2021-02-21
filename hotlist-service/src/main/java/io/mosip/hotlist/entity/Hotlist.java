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
@Table(schema = "hotlist")
public class Hotlist {
	
	@Id
	private String idHash;
	
	private String idValue;
	
	@Id
	private String idType;
	
	private String status;
	
	@Column(name = "start_timestamp")
	private LocalDateTime startDTimes;
	
	@Column(name = "expiry_timestamp")
	private LocalDateTime expiryDTimes;

}