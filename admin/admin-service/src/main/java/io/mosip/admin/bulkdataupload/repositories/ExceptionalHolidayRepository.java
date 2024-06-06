package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.ExceptionalHoliday;
import io.mosip.admin.bulkdataupload.entity.id.ExcptionalHolidayId;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * @author Kishan Rathore
 *
 */
@Repository
public interface ExceptionalHolidayRepository extends BaseRepository<ExceptionalHoliday, ExcptionalHolidayId> {

	@Query("From ExceptionalHoliday where registrationCenterId=?1 and langCode=?2 and (isDeleted = false or isDeleted is null) and isActive = true")
	List<ExceptionalHoliday> findAllNonDeletedExceptionalHoliday(String regCenterId, String langcode);
}
