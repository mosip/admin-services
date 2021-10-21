package io.mosip.admin.bulkdataupload.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
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

	@Query("FROM RegistrationCenterUserMachineHistory WHERE cntrId =?1 and usrId=?2 and machineId=?3 and effectivetimes <=?4 and (isDeleted is null or isDeleted =false)")
	List<RegistrationCenterUserMachineHistory> findByCntrIdAndUsrIdAndMachineIdAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(
			String cntrId, String usrId, String machineId, LocalDateTime effectivetimes);
}
