package io.mosip.admin.bulkdataupload.repositories;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
/**
 * this classs BulkUploadTranscationRepository
 * 
 * @author dhanendra
 *
 */
@Repository
public interface BulkUploadTranscationRepository extends BaseRepository<BulkUploadTranscation, String> {

	@Query("FROM BulkUploadTranscation WHERE id =?1 AND (isDeleted is null OR isDeleted = false) AND isActive = true")
	BulkUploadTranscation findTransactionById(String id);
	
	//@Query(value = "FROM BulkUploadTranscation WHERE category =?1 AND (isDeleted is null OR isDeleted = false) AND isActive = true",nativeQuery = true)
	Page<BulkUploadTranscation> findByCategory(String category, Pageable pageRequest);

	@Modifying
	@Query("UPDATE BulkUploadTranscation SET statusCode=?2, recordCount=?3, uploadDescription=?4 , updatedDateTime=?5"
			+ " WHERE id=?1")
	int updateBulkUploadTransaction(String id, String statusCode, int recordCount, String uploadDescription,
									LocalDateTime updatedDateTime);
	
}
