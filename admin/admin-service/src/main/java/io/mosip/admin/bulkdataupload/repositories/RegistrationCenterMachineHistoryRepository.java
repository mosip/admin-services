package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachineHistory;
import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository to perform CRUD operations on RegistrationCenterMachineHistory.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 * @see RegistrationCenterMachineHistory
 * @see BaseRepository
 *
 */
@Repository
public interface RegistrationCenterMachineHistoryRepository
		extends BaseRepository<RegistrationCenterMachineHistory, RegistrationCenterMachineID> {
}
