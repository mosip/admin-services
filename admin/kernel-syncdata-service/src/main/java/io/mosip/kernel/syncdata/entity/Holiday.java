package io.mosip.kernel.syncdata.entity;

import io.mosip.kernel.syncdata.entity.id.HolidayID;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @author Abhishek Kumar
 * @version 1.0.0
 * @since 24-10-2018
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loc_holiday", schema = "master")
public class Holiday extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1329042436883315822L;

	@EmbeddedId
	private HolidayID holidayId;

	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "holiday_desc", length = 128)
	private String holidayDesc;

}
