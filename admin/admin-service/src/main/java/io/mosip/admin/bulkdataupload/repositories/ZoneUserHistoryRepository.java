package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.ZoneUserHistory;
import io.mosip.admin.bulkdataupload.entity.id.ZoneUserHistoryId;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
@Repository
public interface ZoneUserHistoryRepository extends BaseRepository<ZoneUserHistory, ZoneUserHistoryId>{

}
