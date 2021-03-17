package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.syncdata.entity.AppAuthenticationMethod;
import io.mosip.kernel.syncdata.entity.LocationHierarchy;

/**
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Repository
public interface LocationHierarchyRepository extends JpaRepository<LocationHierarchy, String> {

	@Query("FROM LocationHierarchy l WHERE (l.createdDateTime BETWEEN ?1 AND ?2 ) OR (l.updatedDateTime BETWEEN ?1 AND ?2 )  OR (l.deletedDateTime BETWEEN ?1 AND ?2 ) ")
	List<LocationHierarchy> findByLastUpdatedAndCurrentTimeStamp(LocalDateTime lastUpdatedTimeStamp,
			LocalDateTime currentTimeStamp);
	}
