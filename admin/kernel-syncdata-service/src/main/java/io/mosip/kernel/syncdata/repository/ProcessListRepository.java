package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.ProcessList;
import io.mosip.kernel.syncdata.entity.id.IdAndLanguageCodeID;

/**
 * ProcessListRepository.
 * 
 * @author Srinivasan
 * @since 1.0.0
 */
@Repository
public interface ProcessListRepository extends JpaRepository<ProcessList, IdAndLanguageCodeID> {

	/**
	 * Find by last updated time and current time stamp.
	 *
	 * @param lastUpdatedTime  the last updated time
	 * @param currentTimeStamp the current time stamp
	 * @return {@link ProcessList} list of ProcessList
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'process_list'", condition = "#a0.getYear() <= 1970")
	@Query("FROM ProcessList WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2) ")
	List<ProcessList> findByLastUpdatedTimeAndCurrentTimeStamp(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'process_list'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from ProcessList aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
