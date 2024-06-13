package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import io.mosip.kernel.masterdata.entity.id.WeekDayId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daysofweek_list", schema = "master")
@IdClass(WeekDayId.class)
public class DaysOfWeek extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5984896378379371310L;

	@Id
	@Column(name = "code", nullable = false, length = 3, insertable = false, updatable = false)
	private String code;

	@Id
	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "name", nullable = false, length = 36)
	private String name;

	@Column(name = "day_seq", nullable = false)
	private short daySeq;

	@Column(name = "is_global_working", nullable = false)
	private boolean isGlobalWorking;

	@OneToMany(fetch = FetchType.LAZY)
	private List<RegWorkingNonWorking> workingDayEntity;

}
