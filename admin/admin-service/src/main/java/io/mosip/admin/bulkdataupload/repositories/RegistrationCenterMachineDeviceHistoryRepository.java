package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachineDeviceHistory;
import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineDeviceID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository to perform CRUD operations on
 * RegistrationCenterMachineDeviceHistory.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 * @see RegistrationCenterMachineDeviceHistory
 * @see BaseRepository
 *
 */
@Repository
public interface RegistrationCenterMachineDeviceHistoryRepository
		extends BaseRepository<RegistrationCenterMachineDeviceHistory, RegistrationCenterMachineDeviceID> {
}
