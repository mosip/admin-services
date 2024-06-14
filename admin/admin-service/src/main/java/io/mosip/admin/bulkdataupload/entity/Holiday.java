package io.mosip.admin.bulkdataupload.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;

//github.com/mosip/mosip.git

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import io.mosip.admin.bulkdataupload.entity.id.HolidayID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Abhishek Kumar
 * @author Uday Kumar
 * @version 1.0.0
 * @since 24-10-2018
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loc_holiday", schema = "master")
@IdClass(HolidayID.class)
public class Holiday extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1329042436883315822L;

	@Id
	@AttributeOverrides({
			@AttributeOverride(name = "holidayDate", column = @Column(name = "holiday_date", nullable = false)),
			@AttributeOverride(name = "locationCode", column = @Column(name = "location_code", nullable = false, length = 36)),
			@AttributeOverride(name = "langCode", column = @Column(name = "lang_code", nullable = false, length = 3)) })
	private LocalDate holidayDate;
	private String locationCode;
	private String langCode;
	
	@Column(name = "holiday_name", nullable = false, length = 64)
	private String holidayName;

	@Column(name = "id", unique = true, nullable = false)
	private int holidayId;

	@Column(name = "holiday_desc", length = 128)
	private String holidayDesc;

}
