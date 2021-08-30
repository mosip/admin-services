package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.mosip.kernel.syncdata.entity.ReasonCategory;

public interface ReasonCategoryRepository extends JpaRepository<ReasonCategory, String> {
	/**
	 * Method to find list of ReasonCategory created , updated or deleted time is
	 * greater than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated time stamp
	 * @param currentTimeStamp - currentTimestamp
	 * @return list of {@link ReasonCategory} -list of reason category
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'reason_category'", condition = "#a0.getYear() <= 1970")
	@Query(value = "select rc.code,rc.lang_code,rc.cr_by,rc.cr_dtimes,rc.del_dtimes,rc.is_active,rc.is_deleted,rc.upd_by,rc.upd_dtimes,rc.descr,rc.name from master.reason_category rc where (rc.cr_dtimes BETWEEN ?1 AND ?2) or (rc.upd_dtimes BETWEEN ?1 AND ?2) or (rc.del_dtimes BETWEEN ?1 AND ?2)", nativeQuery = true)
	List<ReasonCategory> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

	/**
	 * Method to fetch all the Reason categories.
	 * 
	 * @return list of {@link ReasonCategory} -list of reason category
	 */
	@Query(value = "select rc.code,rc.lang_code,rc.cr_by,rc.cr_dtimes,rc.del_dtimes,rc.is_active,rc.is_deleted,rc.upd_by,rc.upd_dtimes,rc.descr,rc.name from master.reason_category rc", nativeQuery = true)
	List<ReasonCategory> findAllReasons();

	@Cacheable(cacheNames = "delta-sync", key = "'reason_category'")
	@Query(value = "select max(aam.createdDateTime), max(aam.updatedDateTime) from ReasonCategory aam ")
	List<Object[]> getMaxCreatedDateTimeMaxUpdatedDateTime();
}
