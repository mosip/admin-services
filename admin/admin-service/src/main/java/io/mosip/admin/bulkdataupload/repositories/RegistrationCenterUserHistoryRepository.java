package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserHistoryPk;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository to perform CRUD operations on RegistrationCenterUserHistory.
 * 
 * @author Megha Tanga
 * @since 1.0.0
 * @see RegistrationCenterUserHistory
 * @see BaseRepository
 *
 */
@Repository
public interface RegistrationCenterUserHistoryRepository
		extends BaseRepository<RegistrationCenterUserHistory, RegistrationCenterUserHistoryPk> {

}
