package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserMachine;
import io.mosip.admin.bulkdataupload.entity.id.RegistrationCenterMachineUserID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository class for user machine mapping
 * 
 * @author Dharmesh Khandelwal
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Repository
public interface RegistrationCenterMachineUserRepository
		extends BaseRepository<RegistrationCenterUserMachine, RegistrationCenterMachineUserID> {

	/**
	 * To find all data which are not previously deleted
	 * 
	 * @param cntrId    input from user
	 * @param machineId input from user
	 * @param usrId     input from user
	 * @return {@link RegistrationCenterUserMachine}
	 */
	@Query("FROM RegistrationCenterUserMachine a WHERE a.registrationCenterMachineUserID.cntrId=?1 AND a.registrationCenterMachineUserID.machineId=?2 AND a.registrationCenterMachineUserID.usrId=?3 and (a.isDeleted is null or a.isDeleted =false) and a.isActive = true")
	Optional<RegistrationCenterUserMachine> findAllNondeletedMappings(String cntrId, String machineId, String usrId);

	@Query("FROM RegistrationCenterUserMachine rum where rum.registrationCenterMachineUserID.machineId = ?1 AND (rum.isDeleted is null or rum.isDeleted=false) and rum.isActive = true")
	List<RegistrationCenterUserMachine> findByMachineIdAndIsDeletedFalseOrIsDeletedIsNull(String machineId);

	@Query("FROM RegistrationCenterUserMachine rum where rum.registrationCenterMachineUserID.usrId = ?1 AND (rum.isDeleted is null or rum.isDeleted=false) and rum.isActive = true")
	List<RegistrationCenterUserMachine> findByUsrIdAndIsDeletedFalseOrIsDeletedIsNull(String usrId);

	@Query("FROM RegistrationCenterUserMachine rum where rum.registrationCenterMachineUserID.cntrId = ?1 AND (rum.isDeleted is null or rum.isDeleted=false) and rum.isActive = true")
	List<RegistrationCenterUserMachine> findByCntrIdAndIsDeletedFalseOrIsDeletedIsNull(String cntrId);

}
