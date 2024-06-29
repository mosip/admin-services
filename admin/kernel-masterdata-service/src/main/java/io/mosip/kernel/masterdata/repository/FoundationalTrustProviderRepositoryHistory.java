/**
 * 
 */
package io.mosip.kernel.masterdata.repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.FoundationalTrustProviderHistory;
import org.springframework.stereotype.Repository;

/**
 * @author Ramadurai Pandian
 *
 */
@Repository
public interface FoundationalTrustProviderRepositoryHistory
		extends BaseRepository<FoundationalTrustProviderHistory, String> {

}
