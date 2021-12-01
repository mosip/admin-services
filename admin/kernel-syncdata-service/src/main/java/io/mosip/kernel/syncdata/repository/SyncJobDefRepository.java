package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.SyncJobDef;

/**
 * The SyncJobDefRepository which handles CRUD operation for {@link SyncJobDef}.
 * 
 * @author Bal Vikash Sharma
 */
@Repository
public interface SyncJobDefRepository extends JpaRepository<SyncJobDef, String> {

	/**
	 * Find latest by last updated time and current time stamp.
	 *
	 * @param lastUpdatedTime  -the last updated time
	 * @param currentTimeStamp -the current time stamp
	 * @return the list of {@link SyncJobDef}
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'sync_job_def'", condition = "#a0.getYear() <= 1970")
	@Query("FROM SyncJobDef WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<SyncJobDef> findLatestByLastUpdatedTimeAndCurrentTimeStamp(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'sync_job_def'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from SyncJobDef aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
