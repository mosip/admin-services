package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.AppRolePriorityID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @author Srinivasan
 * @since 1.0.0
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "app_role_priority", schema = "master")
@IdClass(AppRolePriorityID.class)
public class AppRolePriority extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8100187831794974147L;

	/** The app id. */
	@Id
	private String appId;

	/** The process id. */
	@Id
	private String processId;

	/** The role code. */
	@Id
	private String roleCode;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "lang_code")
	private String langCode;

}
