/**
 * 
 */
package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.FoundationalTrustProviderHistory;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * @author Ramadurai Pandian
 *
 */
@Repository
public interface FoundationalTrustProviderRepositoryHistory
		extends BaseRepository<FoundationalTrustProviderHistory, String> {

}
