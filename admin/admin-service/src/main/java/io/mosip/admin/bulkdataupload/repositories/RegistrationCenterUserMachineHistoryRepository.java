package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserMachineHistory;
import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineUserHistoryID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository class for user machine mapping
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 *
 */
@Repository
public interface RegistrationCenterUserMachineHistoryRepository
		extends BaseRepository<RegistrationCenterUserMachineHistory, RegistrationCenterMachineUserHistoryID> {
}
