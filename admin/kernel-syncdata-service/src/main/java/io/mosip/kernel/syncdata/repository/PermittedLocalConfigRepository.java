package io.mosip.kernel.syncdata.repository;

import io.mosip.kernel.syncdata.entity.PermittedLocalConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PermittedLocalConfigRepository extends JpaRepository<PermittedLocalConfig, String> {

    @Query("FROM PermittedLocalConfig WHERE (createdDateTime BETWEEN ?1 AND ?2) OR (updatedDateTime BETWEEN ?1 AND ?2)  OR (deletedDateTime BETWEEN ?1 AND ?2)")
    List<PermittedLocalConfig> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp);

}
