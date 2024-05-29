package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.admin.bulkdataupload.entity.DeviceProvider;

/**
 * The Interface DeviceProviderRepository.
 * 
 * @author Srinivasan
 * @author Megha Tanga
 * @since 1.0.0
 */
@Repository
public interface DeviceProviderRepository extends BaseRepository<DeviceProvider, String> {

	/**
	 * Find by id and is active is true.
	 *
	 * @param id the id
	 * @return the device provider
	 */
	DeviceProvider findByIdAndIsActiveIsTrue(String id);

	@Query("FROM DeviceProvider  where id=?1 AND (isDeleted is null OR isDeleted = false) AND isActive = true")
	DeviceProvider findByIdAndIsDeletedFalseorIsDeletedIsNullAndIsActiveTrue(String id);

	@Query("FROM DeviceProvider  where id=?1 AND vendorName=?2 AND (isDeleted is null OR isDeleted = false) AND isActive = true")
	DeviceProvider findByIdAndNameAndIsDeletedFalseorIsDeletedIsNullAndIsActiveTrue(String id, String vendorName);
	
	@Query("FROM DeviceProvider  where vendorName=?1 AND address=?2 AND (isDeleted is null OR isDeleted = false) AND isActive = true")
	DeviceProvider findByNameAndAddressAndIsDeletedFalseorIsDeletedIsNullAndIsActiveTrue(String vendorName, String address);

	/*
	 * @Query("FROM DeviceProvider d where d.id=?1 AND d.venderName AND (d.isDeleted is null OR d.isDeleted = false) AND d.isActive = true"
	 * ) DeviceProvider
	 * findByIdAndNameIsDeletedFalseorIsDeletedIsNullAndIsActiveTrue(String id,
	 * String vendorName);
	 */
}
