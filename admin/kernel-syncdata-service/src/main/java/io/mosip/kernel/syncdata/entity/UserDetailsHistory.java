package io.mosip.kernel.syncdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "name", nullable = false, length = 64)
	private String name;

	@Column(name = "status_code", nullable = false, length = 36)
	private String statusCode;

	@Column(name = "last_login_dtimes")
	private LocalDateTime lastLoginDateTime;

	@Column(name = "last_login_method", length = 64)
	private String lastLoginMethod;
	
	@Column(name = "regcntr_id", length = 10)
	private String regCenterId;

}
