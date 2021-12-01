package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

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

	@Query("From UserDetails mm WHERE mm.regCenterId =?1 AND ((mm.createdDateTime BETWEEN ?2 AND ?3) OR (mm.updatedDateTime BETWEEN ?2 AND ?3) OR (mm.deletedDateTime BETWEEN ?2 AND ?3))")
	List<UserDetails> findAllLatestCreatedUpdatedDeleted(String regId, LocalDateTime lastUpdated,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'user_detail'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from UserDetails aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
