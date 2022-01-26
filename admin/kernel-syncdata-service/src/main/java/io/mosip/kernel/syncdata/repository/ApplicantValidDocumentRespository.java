package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.ApplicantValidDocument;
import io.mosip.kernel.syncdata.entity.id.ApplicantValidDocumentID;

/**
 * Repository to handle CRUD operations for {@link ApplicantValidDocument}
 * 
 * @author Srinivasan
 *
 */
@Repository
public interface ApplicantValidDocumentRespository
		extends JpaRepository<ApplicantValidDocument, ApplicantValidDocumentID> {

	@Cacheable(cacheNames = "initial-sync", key = "'applicant_valid_document'", condition = "#a0.getYear() <= 1970")
	@Query("FROM ApplicantValidDocument avd WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2) ")
	public List<ApplicantValidDocument> findAllByTimeStamp(LocalDateTime lastUpdatedTime,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'applicant_valid_document'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from ApplicantValidDocument aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
