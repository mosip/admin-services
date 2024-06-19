package io.mosip.kernel.syncdata.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Sidhant Agarwal
 * @author Abhishek Kumar
 * @since 1.0.0
 *
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayID implements Serializable {

	private static final long serialVersionUID = -1631873932622755759L;

	@Column(name = "location_code", nullable = false, length = 36)
	private String locationCode;

	@Column(name = "holiday_date", nullable = false)
	private LocalDate holidayDate;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "holiday_name", nullable = false, length = 64)
	private String holidayName;

}
