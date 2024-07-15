package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationDeviceSubType;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * interface for RegistrationDeviceSubType Repository
 * 
 * @author Megha Tanga
 *
 */
@Repository
public interface RegistrationDeviceSubTypeRepository extends BaseRepository<RegistrationDeviceSubType, String> {

}
