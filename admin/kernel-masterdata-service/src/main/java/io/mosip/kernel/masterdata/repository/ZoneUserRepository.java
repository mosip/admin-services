package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.entity.id.ZoneUserId;
import org.springframework.transaction.annotation.Transactional;

/**
 * Zone-User mapping repository
 * 
 * @author Abhishek Kumar
 * @author Megha Tanga
 * @since 1.0.0
 */
public interface ZoneUserRepository extends BaseRepository<ZoneUser, String> {

	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<ZoneUser> findByUserIdNonDeleted(String userId);

	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public ZoneUser findZoneByUserIdNonDeleted(String userId);

	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) ")
	public ZoneUser findByUserId(String userId);
	
	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and zu.zoneCode=?2 ")
	public List<ZoneUser> findByUserIdAndZoneCode(String userId, String zoneCode);

	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and zu.zoneCode=?2 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public ZoneUser findByIdAndIsDeletedFalseOrIsDeletedIsNull(String userId, String zoneCode);

	@Query("FROM ZoneUser zu WHERE zu.zoneCode=?1 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<ZoneUser> findtoUpdateZoneUserByCode(String zoneCode);
	
	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and zu.langCode=?2 and zu.zoneCode=?3 and (zu.isDeleted IS NULL OR zu.isDeleted = false) and zu.isActive=true")
	public ZoneUser findZoneUserByUserIdZoneCodeLangCodeIsActive(String userId, String langCode, String zoneCode);
	
	@Query("FROM ZoneUser zu WHERE zu.userId IN :userids")
	public List<ZoneUser> findByUserIds(@Param("userids") List<String> userIds);

	@Query("FROM ZoneUser zu WHERE LOWER(zu.userId)=LOWER(?1) and zu.isActive=true and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public ZoneUser findZoneByUserIdActiveAndNonDeleted(String userId);
	
	/*
	 * This is a query used by a deprecated end point /users/search, this should be removed in next release.
	 * The end point using this query was added in version 1.2.0-rc1 and marked as deprecated in version 1.2.0 
	 * For security reason, we replaced the 'LIKE' query with equals in where clause.
	 */
	
	@Query("FROM ZoneUser zu WHERE LOWER(zu.zoneCode)=?1  and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<ZoneUser> findZoneByZoneCodeActiveAndNonDeleted(String zoneCode);
	
	@Modifying
	@Transactional
	@Query("UPDATE ZoneUser z SET z.updatedBy=?3, z.isDeleted =true, z.isActive = false, z.updatedDateTime=?2 ,z.deletedDateTime= ?2 WHERE z.userId =?1 and (z.isDeleted is null or z.isDeleted =false)")
	int deleteZoneUser(String id, LocalDateTime deletedDateTime, String updatedBy);

	ZoneUser findOneByUserIdIgnoreCase(String userId);

}
