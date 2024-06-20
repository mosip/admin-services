package io.mosip.kernel.masterdata.repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.ExceptionalHoliday;
import io.mosip.kernel.masterdata.entity.id.ExcptionalHolidayId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Kishan Rathore
 *
 */
@Repository
public interface ExceptionalHolidayRepository extends BaseRepository<ExceptionalHoliday, ExcptionalHolidayId> {

	@Query("FROM ExceptionalHoliday where registrationCenterId=?1 and langCode=?2 and (isDeleted = false or isDeleted is null) and isActive = true")
	List<ExceptionalHoliday> findAllNonDeletedExceptionalHoliday(String regCenterId, String langcode);

	@Query("SELECT DISTINCT holidayDate From ExceptionalHoliday where registrationCenterId=?1 and (isDeleted = false or isDeleted is null) and isActive = true")
	List<LocalDate> findDistinctByRegistrationCenterId(String regCenterId);
}
