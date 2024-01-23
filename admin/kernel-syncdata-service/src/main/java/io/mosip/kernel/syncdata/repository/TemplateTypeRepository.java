package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.TemplateType;

@Repository
public interface TemplateTypeRepository extends JpaRepository<TemplateType, String> {
	/**
	 * Method to find list of TemplateType created , updated or deleted time is
	 * greater than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated      timeStamp - last updated time stamp
	 * @param currentTimeStamp - current time stamp
	 * @return list of {@link TemplateType} - list of template type
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'template_type'", condition = "#a0.getYear() <= 1970")
	@Query("FROM TemplateType WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
	List<TemplateType> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);
	
	@Cacheable(cacheNames = "initial-sync", key = "'template_type'", condition = "#a0.getYear() <= 1970")
	@Query(value = "select * from template_type WHERE (code IN (select template_typ_code from template where module_id=?3)) AND ((cr_dtimes between ?1 AND ?2) OR (upd_dtimes between ?1 AND ?2)  OR (del_dtimes between ?1 AND ?2))", nativeQuery = true)
	List<TemplateType> findAllLatestCreatedUpdateDeletedTemplateTypeCode(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp, String regclientModuleId);

	@Cacheable(cacheNames = "delta-sync", key = "'template_type'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from TemplateType aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
