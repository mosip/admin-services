package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.DocumentCategory;
import io.mosip.kernel.syncdata.entity.id.CodeAndLanguageCodeID;

/**
 * @author Neha
 * @since 1.0.0
 *
 */
@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, CodeAndLanguageCodeID> {
	/**
	 * Method to find list of DocumentCategory created , updated or deleted time is
	 * greater than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated timestamp
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link DocumentCategory} - list of document category
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'doc_category'", condition = "#a0.getYear() <= 1970")
	@Query("FROM DocumentCategory WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<DocumentCategory> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);


	@Cacheable(cacheNames = "delta-sync", key = "'doc_category'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from DocumentCategory aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
