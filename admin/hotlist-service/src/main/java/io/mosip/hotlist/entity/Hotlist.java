package io.mosip.hotlist.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Manoj SP
 *
 */
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
	
	private LocalDateTime startTimestamp;
	
	private LocalDateTime expiryTimestamp;
	
	@Column(name = "cr_by")
	private String createdBy;

	/** The created date time. */
	@Column(name = "cr_dtimes")
	private LocalDateTime createdDateTime;

	/** The updated by. */
	@Column(name = "upd_by")
	private String updatedBy;

	/** The updated date time. */
	@Column(name = "upd_dtimes")
	private LocalDateTime updatedDateTime;

	/** The is deleted. */
	private Boolean isDeleted;

	/** The deleted date time. */
	@Column(name = "del_dtimes")
	private LocalDateTime deletedDateTime;

}