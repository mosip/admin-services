package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationDeviceType;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Inerface for RegistrationDeviceType Repository
 * 
 * @author Megha Tanga
 *
 */
@Repository
public interface RegistrationDeviceTypeRepository extends BaseRepository<RegistrationDeviceType, String> {

	@Query("FROM RegistrationDeviceType d where d.code=?1 AND (d.isDeleted is null OR d.isDeleted = false) AND d.isActive = true")
	RegistrationDeviceType findByCodeAndIsDeletedFalseorIsDeletedIsNullAndIsActiveTrue(String code);

}
