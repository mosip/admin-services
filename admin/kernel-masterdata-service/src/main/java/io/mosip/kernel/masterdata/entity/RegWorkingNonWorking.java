package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.RegWorkingNonWorkingId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reg_working_nonworking", schema = "master")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RegWorkingNonWorkingId.class)
public class RegWorkingNonWorking extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5192880804937801240L;

	@Id
	@Column(name = "regcntr_id", nullable = false, length = 10)
	private String registrationCenterId;

	@Id
	@Column(name = "day_code", nullable = false, length = 3)
	private String dayCode;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String languagecode;

	@Column(name = "is_working", nullable = false)
	private boolean isWorking;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "day_code", referencedColumnName = "code", insertable = false, updatable = false),
			@JoinColumn(name = "lang_code", referencedColumnName = "lang_code", insertable = false, updatable = false) })
	private DaysOfWeek daysOfWeek;

//	@ManyToOne
//	@JoinColumns({
//		@JoinColumn(name="id",referencedColumnName="id",insertable=false,updatable=false),
//		@JoinColumn(name="lang_code",referencedColumnName="lang_code",insertable=false,updatable=false)
//	})
//	private RegistrationCenter registrationCenter;

}
