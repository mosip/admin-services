package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.Gender;

/**
 * Repository class for fetching gender data
 * 
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Repository
public interface GenderRepository extends JpaRepository<Gender, String> {
	/**
	 * Method to find list of Gender created , updated or deleted time is greater
	 * than lastUpdated timeStamp.
	 * 
	 * @param lastUpdateTimeStamp      timeStamp - last updated timestamp
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link Gender} - list of gender repository
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'gender'", condition = "#a0.getYear() <= 1970")
	@Query("FROM Gender WHERE (createdDateTime BETWEEN ?1 AND ?2 ) OR (updatedDateTime BETWEEN ?1 AND ?2 )  OR (deletedDateTime BETWEEN ?1 AND ?2 )")
	List<Gender> findByLastUpdatedAndCurrentTimeStamp(LocalDateTime lastUpdateTimeStamp,
															LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'gender'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from Gender aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
