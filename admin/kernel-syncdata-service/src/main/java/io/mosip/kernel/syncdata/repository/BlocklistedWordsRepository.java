package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.mosip.kernel.syncdata.entity.BlocklistedWords;

/**
 * repository for blacklisted words
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public interface BlocklistedWordsRepository extends JpaRepository<BlocklistedWords, String> {

	/**
	 * Method to find list of BlocklistedWords created , updated or deleted time is
	 * greater than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated time stamp
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link BlocklistedWords} - list of blocklisted words
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'blocklisted_words'", condition = "#a0.getYear() <= 1970")
	@Query("FROM BlocklistedWords WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<BlocklistedWords> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'blocklisted_words'")
	@Query(value = "select max(aam.createdDateTime), max(aam.updatedDateTime) from BlocklistedWords aam ")
	List<Object[]> getMaxCreatedDateTimeMaxUpdatedDateTime();
}
