package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.ZoneUserHistory;
import io.mosip.kernel.masterdata.entity.id.ZoneUserHistoryId;
@Repository
public interface ZoneUserHistoryRepository extends BaseRepository<ZoneUserHistory, ZoneUserHistoryId>{
	@Query(value = "SELECT * FROM master.zone_user_h m WHERE usr_id = ?1 AND eff_dtimes>= ?2 and (is_deleted is null or is_deleted =false) ORDER BY eff_dtimes DESC ", nativeQuery = true)
	List<ZoneUserHistory> getByUserIdAndTimestamp(String userId, LocalDateTime localDateTime);

}
