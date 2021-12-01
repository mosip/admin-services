package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.AppRolePriority;
import io.mosip.kernel.syncdata.entity.id.IdAndLanguageCodeID;

/**
 * AppRolePriorityRepository.
 *
 * @author Srinivasan
 * @since 1.0.0
 */
@Repository
public interface AppRolePriorityRepository extends JpaRepository<AppRolePriority, IdAndLanguageCodeID> {

	/**
	 * Find by last updated and current time stamp.
	 *
	 * @param lastUpdatedTime  the last updated time
	 * @param currentTimeStamp the current time stamp
	 * @return {@link AppRolePriority}
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'app_role_priority'", condition = "#a0.getYear() <= 1970")
	@Query("FROM AppRolePriority WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<AppRolePriority> findByLastUpdatedAndCurrentTimeStamp(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'app_role_priority'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from AppRolePriority aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
