package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.mosip.admin.bulkdataupload.entity.ZoneUser;
import io.mosip.admin.bulkdataupload.entity.id.ZoneUserId;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

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
}
