package io.mosip.kernel.syncdata.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.mosip.kernel.syncdata.entity.UserDetails;
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String>{

	@Query("FROM UserDetails mm WHERE mm.regCenterId=?1 and (mm.isDeleted=false or mm.isDeleted is null) ")
	List<UserDetails> findByUsersByRegCenterId(String regCenterId);
	@Cacheable(cacheNames = "initial-sync", key = "'user_detail'.concat('_').concat(#a0)", condition = "#a1.getYear() <= 1970")
	@Query("From UserDetails mm WHERE mm.regCenterId =?1 AND ((mm.createdDateTime BETWEEN ?2 AND ?3) OR (mm.updatedDateTime BETWEEN ?2 AND ?3) OR (mm.deletedDateTime BETWEEN ?2 AND ?3))")
	List<UserDetails> findAllLatestCreatedUpdatedDeleted(String regId, LocalDateTime lastUpdated,
														 LocalDateTime currentTimeStamp);
	@Cacheable(cacheNames = "delta-sync", key = "'user_detail'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from UserDetails aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
	@Query("FROM UserDetails mm WHERE lower(mm.id)=lower(?1) and mm.isActive=true and (mm.isDeleted=false or mm.isDeleted is null) ")
	Optional<UserDetails> findActiveUserById(String userId);
	@Query("FROM UserDetails u " +
			"WHERE u.regCenterId = ?1 AND (u.isDeleted = false OR u.isDeleted IS NULL)")
	org.springframework.data.domain.Page<UserDetails>
	findPageByRegCenterId(String regCenterId,
						  org.springframework.data.domain.Pageable pageable);
	// Optional delta-paged read (entity) if you wire lastUpdated/currentTs
	@Query("FROM UserDetails u " +
			"WHERE u.regCenterId = ?1 AND " +
			"((u.createdDateTime BETWEEN ?2 AND ?3) OR (u.updatedDateTime BETWEEN ?2 AND ?3) OR (u.deletedDateTime BETWEEN ?2 AND ?3)) " +
			"AND (u.isDeleted = false OR u.isDeleted IS NULL)")
	org.springframework.data.domain.Page<UserDetails>
	findDeltaPageByRegCenterId(String regCenterId,
							   java.time.LocalDateTime from,
							   java.time.LocalDateTime to,
							   org.springframework.data.domain.Pageable pageable);
}



