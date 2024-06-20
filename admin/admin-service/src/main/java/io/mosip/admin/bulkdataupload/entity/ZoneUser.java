package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zone_user", schema = "master")
public class ZoneUser extends BaseEntity implements Serializable {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8194849518681293756L;

	@Column(name = "zone_code", nullable = false, length = 36)
	private String zoneCode;

	@Id
	@Column(name = "usr_id", nullable = false, length = 256)
	private String userId;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

}
