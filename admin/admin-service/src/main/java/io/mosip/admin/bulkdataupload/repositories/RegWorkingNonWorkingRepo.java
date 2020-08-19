package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegWorkingNonWorking;
import io.mosip.admin.bulkdataupload.entity.id.RegWorkingNonWorkingId;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

@Repository("workingDaysRepo")
public interface RegWorkingNonWorkingRepo extends BaseRepository<RegWorkingNonWorking, RegWorkingNonWorkingId> {

	@Query("From RegWorkingNonWorking where registrationCenterId=?1 and languagecode=?2 and (isDeleted is null or isDeleted = false) and isActive = true")
	List<RegWorkingNonWorking> findByRegCenterIdAndlanguagecode(String registrationCenterId, String languagecode);

	@Query("From RegWorkingNonWorking where languagecode=?1 and (isDeleted is null or isDeleted = false) and isActive = true")
	List<RegWorkingNonWorking> findByLanguagecode(String languageCode);

}
