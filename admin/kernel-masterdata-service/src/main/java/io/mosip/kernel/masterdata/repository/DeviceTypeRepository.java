package io.mosip.kernel.masterdata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.DeviceType;

/**
 * Repository function to fetching Device Type details
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Repository
public interface DeviceTypeRepository extends BaseRepository<DeviceType, String> {

	@Query("FROM DeviceType d where (isDeleted is null OR isDeleted = false) AND isActive = true")
	List<DeviceType> findAllDeviceTypeByIsActiveAndIsDeletedFalseOrNull();
	
	@Query(value = "select A.code, A.lang_code from (select distinct * from master.device_type) A left join (select distinct code, lang_code from master.device_type where lang_code=?1) B on A.code=B.code where B.code is null", nativeQuery = true)
	List<DeviceType> findAllDeviceTypeForMissingData(String langCode);

	@Query(value="FROM DeviceType d where d.code = ?1 and d.langCode =?2")
	DeviceType findDeviceTypeByCodeAndByLangCode(String code, String langCode);
	


}