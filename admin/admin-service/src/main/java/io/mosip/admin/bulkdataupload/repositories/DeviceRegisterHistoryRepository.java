package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.admin.bulkdataupload.entity.DeviceRegisterHistory;
import io.mosip.admin.bulkdataupload.entity.id.DeviceRegisterHistoryId;

@Repository
public interface DeviceRegisterHistoryRepository
		extends BaseRepository<DeviceRegisterHistory, DeviceRegisterHistoryId> {

}
