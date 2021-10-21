/**
 * 
 */
package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.FoundationalTrustProvider;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * @author Ramadurai Pandian
 *
 */
@Repository
public interface FoundationalTrustProviderRepository extends BaseRepository<FoundationalTrustProvider, String> {

	@Query("from FoundationalTrustProvider WHERE name = ?1 and email=?2 and address=?3 and certAlias=?4 and (isDeleted is null or isDeleted =false) AND isActive = ?5")
	FoundationalTrustProvider findByDetails(String name, String email, String address, String certAlias,
			boolean isActive);

}
