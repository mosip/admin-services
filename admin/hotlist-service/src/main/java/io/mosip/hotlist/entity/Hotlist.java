package io.mosip.hotlist.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

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