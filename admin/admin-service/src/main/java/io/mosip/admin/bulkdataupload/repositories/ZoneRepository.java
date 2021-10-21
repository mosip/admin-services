package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.mosip.admin.bulkdataupload.entity.Zone;
import io.mosip.admin.bulkdataupload.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Zone Repository
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public interface ZoneRepository extends BaseRepository<Zone, CodeAndLanguageCodeID> {

	@Query("FROM Zone z WHERE (z.isDeleted IS NULL OR z.isDeleted = false) ")
	public List<Zone> findAllNonDeleted();

	@Query("FROM Zone zu WHERE zu.code=?1 and zu.langCode=?2 AND (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public Zone findZoneByCodeAndLangCodeNonDeleted(String code, String langCode);

	@Query("FROM Zone zu WHERE zu.code in (:zoneId) AND (zu.isDeleted IS NULL OR zu.isDeleted = false) ")
	public List<Zone> findListZonesFromZone(List<String> zoneId);
}
