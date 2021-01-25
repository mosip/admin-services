package io.mosip.kernel.masterdata.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.UserDetails;

@Repository
public interface UserDetailsRepository extends BaseRepository<UserDetails, String> {
	
	@Query(value = "select count(*) from  master.user_detail where regcntr_id=?1 and (is_deleted is null or is_deleted=false)", nativeQuery = true)
	public Long countCenterUsers(String centerId);

	@Query(value = "select count(*) from  master.user_detail where regcntr_id=?1 and (is_deleted is null or is_deleted=false) and isActive = true", nativeQuery = true)
	public Long countCenterActiveUsers(String centerId);
	
	@Query("FROM UserDetails WHERE regcntr_id=?1 and (isDeleted is null or isDeleted =false) and isActive = true")
	public List<UserDetails> findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(String centerId);

	@Query(value = "FROM UserDetails WHERE regcntr_id=?1 and (isDeleted is null or isDeleted =false) and isActive = true", countQuery="SELECT COUNT(id) FROM master.dynamic_field WHERE (is_deleted is null OR is_deleted = false) and lang_code=?1",
	nativeQuery = true)
	public List<UserDetails> findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(String centerId, Pageable pageable);

	@Query("FROM UserDetails m where m.id = ?1 and (m.isDeleted is null or m.isDeleted = false) and m.isActive = true")
	UserDetails findByIdAndIsDeletedFalseorIsDeletedIsNull(String id);

	@Query("FROM UserDetails m where (m.isDeleted is null or m.isDeleted = false) and m.isActive = true")
	Page<UserDetails> findAllByIsDeletedFalseorIsDeletedIsNull(Pageable pageable);
}
