package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import io.mosip.kernel.syncdata.dto.EntityDtimes;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.syncdata.entity.AppAuthenticationMethod;
import io.mosip.kernel.syncdata.entity.id.AppAuthenticationMethodID;

/**
 * The Interface AppAuthenticationMethodRepository.
 * 
 * @author Srinivasan
 * @since 1.0.0
 */
@Repository
public interface AppAuthenticationMethodRepository
		extends JpaRepository<AppAuthenticationMethod, AppAuthenticationMethodID> {

	/**
	 * Find by last updated and current time stamp.
	 *
	 * @param lastUpdatedTimeStamp the last updated time stamp
	 * @param currentTimeStamp     the current time stamp
	 * @return list of app authenticationMethod
	 */
	@Cacheable(cacheNames = "initial-sync", key = "'app_authentication_method'", condition = "#a0.getYear() <= 1970")
	@Query("FROM AppAuthenticationMethod WHERE (createdDateTime BETWEEN ?1 AND ?2 ) OR (updatedDateTime BETWEEN ?1 AND ?2 )  OR (deletedDateTime BETWEEN ?1 AND ?2 ) ")
	List<AppAuthenticationMethod> findByLastUpdatedAndCurrentTimeStamp(LocalDateTime lastUpdatedTimeStamp,
			LocalDateTime currentTimeStamp);

	@Cacheable(cacheNames = "delta-sync", key = "'app_authentication_method'")
	@Query(value = "select new io.mosip.kernel.syncdata.dto.EntityDtimes(max(aam.createdDateTime), max(aam.updatedDateTime), max(aam.deletedDateTime)) from AppAuthenticationMethod aam ")
	EntityDtimes getMaxCreatedDateTimeMaxUpdatedDateTime();
}
