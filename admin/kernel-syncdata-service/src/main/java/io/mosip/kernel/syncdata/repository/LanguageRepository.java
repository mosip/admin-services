package io.mosip.kernel.syncdata.repository;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import io.mosip.kernel.syncdata.entity.Language;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository to perform CRUD operations on Language.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 * @see Language
 * @see JpaRepository
 *
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

	/**
	 * Method to find list of Language created , updated or deleted time is greater
	 * than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      - last updated time
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link Language} - list of language
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'language'", condition = "#a0.getYear() <= 1970")
	@Query("FROM Language WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<Language> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'language'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from Language aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
