package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.UISpec;

/**
 * 
 * @author Nagarjuna
 *
 */
@Repository
public interface UISpecRepository extends BaseRepository<UISpec, String>{
	
	@Query("FROM UISpec WHERE (isDeleted is null OR isDeleted = false) and id = ?1")
	UISpec findUISpecById(String id);

	@Query("FROM UISpec WHERE version = (select max(b.version) from UISpec b where "
			+ "(b.isDeleted is null OR b.isDeleted = false) AND b.domain = ?1 AND b.isActive = true And b.status='PUBLISHED')")
	List<UISpec> findLatestPublishedUISpec(String domain);
	
	
	@Query("FROM UISpec WHERE version = (select max(b.version) from UISpec b where "
			+ "(b.isDeleted is null OR b.isDeleted = false) AND b.domain = ?1 AND b.type = ?2 AND b.isActive = true And b.status='PUBLISHED')")
	UISpec findLatestPublishedUISpec(String domain, String type);
	
	@Modifying
	@Query("UPDATE UISpec i SET i.jsonSpec=?2, i.isActive=?3 , i.updatedDateTime=?4, i.updatedBy=?5 WHERE i.id =?1 AND i.status='DRAFT' AND (i.isDeleted is null or i.isDeleted =false)")
	int updateUISpec(String id, String idAttributeJson, boolean isActive, LocalDateTime updatedDateTime, String updatedBy);
	
	@Query(value="select * FROM master.ui_spec WHERE is_active=?1 AND (is_deleted is null OR is_deleted = false)", 
			countQuery="select count(id) FROM master.ui_spec WHERE is_active=?1 AND (is_deleted is null OR is_deleted = false)",
			nativeQuery= true)
	Page<UISpec> findAllUISpecs(boolean isActive, Pageable pageable);
	
	@Modifying
	@Query("UPDATE UISpec g SET g.updatedBy=?3 , g.isDeleted =true , g.deletedDateTime = ?2 WHERE g.id =?1 and (g.isDeleted is null or g.isDeleted =false) and g.status='DRAFT'")
	int deleteUISpec(String id, LocalDateTime deletedDateTime, String updatedBy);
	
	@Query("FROM UISpec WHERE version=?1 AND domain = ?2 AND isActive = true AND status='PUBLISHED'")
	List<UISpec> findPublishedUISpec(double version, String domain);
	
	@Query("FROM UISpec WHERE version=?1 AND domain = ?2 AND type = ?3 AND isActive = true AND status='PUBLISHED'")
	UISpec findPublishedUISpec(double version, String domain, String type);
	
	
}
