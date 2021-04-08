package io.mosip.kernel.masterdata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.entity.id.ZoneUserId;

/**
 * Zone-User mapping repository
 * 
 * @author Abhishek Kumar
 * @author Megha Tanga
 * @since 1.0.0
 */
public interface ZoneUserRepository extends BaseRepository<ZoneUser, ZoneUserId> {

	@Query("FROM ZoneUser zu WHERE zu.userId=?1 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<ZoneUser> findByUserIdNonDeleted(String userId);

	@Query("FROM ZoneUser zu WHERE zu.userId=?1 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public ZoneUser findZoneByUserIdNonDeleted(String userId);

	@Query("FROM ZoneUser zu WHERE zu.userId=?1")
	public ZoneUser findByIdAndLangCode(String userId);
	
	@Query("FROM ZoneUser zu WHERE zu.userId=?1 and zu.zoneCode=?2 ")
	public List<ZoneUser> findByUserIdAndZoneCode(String userId, String zoneCode);

	@Query("FROM ZoneUser zu WHERE zu.userId=?1 and zu.langCode=?2 and zu.zoneCode=?3 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public ZoneUser findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(String userId, String langCode, String zoneCode);

	@Query("FROM ZoneUser zu WHERE zu.zoneCode=?1 and (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<ZoneUser> findtoUpdateZoneUserByCode(String zoneCode);
	
	@Query("FROM ZoneUser zu WHERE zu.userId=?1 and zu.langCode=?2 and zu.zoneCode=?3 and (zu.isDeleted IS NULL OR zu.isDeleted = false) and zu.isActive=true")
	public ZoneUser findZoneUserByUserIdZoneCodeLangCodeIsActive(String userId, String langCode, String zoneCode);
}
