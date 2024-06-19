package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserDetailHistoryPk.class)
@Table(name = "user_detail_h", schema = "master")
public class UserDetailsHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8541941111557590379L;

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false, length = 36)),
			@AttributeOverride(name = "effDTimes", column = @Column(name = "eff_dtimes", nullable = false)) })
	private String id;
	private LocalDateTime effDTimes;

	@Column(name = "lang_code", length = 3)
	private String langCode;

	@Column(name = "name", length = 64)
	private String name;

	@Column(name = "status_code", length = 36)
	private String statusCode;

	@Column(name = "last_login_dtimes")
	private LocalDateTime lastLoginDateTime;

	@Column(name = "last_login_method", length = 64)
	private String lastLoginMethod;
	
	@Column(name = "regcntr_id", length = 10)
	private String regCenterId;

}
