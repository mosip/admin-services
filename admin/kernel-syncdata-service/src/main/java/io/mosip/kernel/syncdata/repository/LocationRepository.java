package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.Location;

/**
 * This interface is JPA repository class which interacts with database and does
 * the CRUD function. It is extended from {@link JpaRepository}
 * 
 * @author Srinivasan
 *
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

	/**
	 * Method to find list of Location created , updated or deleted time is greater
	 * than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated time
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link Location} - list of location
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'location'", condition = "#a0.getYear() <= 1970")
	@Query("FROM Location WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<Location> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'location'")
	@Query(value = "select max(aam.createdDateTime), max(aam.updatedDateTime) from Location aam ")
	List<Object[]> getMaxCreatedDateTimeMaxUpdatedDateTime();

}
