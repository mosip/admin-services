package io.mosip.admin.bulkdataupload.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.UserDetailHistoryPk;
import io.mosip.admin.bulkdataupload.entity.UserDetailsHistory;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Repository
public interface UserDetailsHistoryRepository extends BaseRepository<UserDetailsHistory, UserDetailHistoryPk> {

	/**
	 * @param userId    input from user
	 * @param effDTimes input from user
	 * @return list of user details for the particular input
	 */
	// select a.id,a.langCode,a.name,a.uin,a.email,a.mobile,a.statusCode
	// SELECT m.id,m.langCode,m.name,m.uin,m.email,m.mobile,m.statusCode,m.effDTimes
	@Query(value = "select * from (SELECT * FROM master.user_detail_h m WHERE id = ?1 AND eff_dtimes<= ?2 and (is_deleted is null or is_deleted =false) ORDER BY eff_dtimes DESC) a LIMIT 1", nativeQuery = true)
	List<UserDetailsHistory> getByUserIdAndTimestamp(String userId, LocalDateTime effDTimes);
	// (?2 BETWEEN effDTimes AND CURRENT_TIMESTAMP)

}
