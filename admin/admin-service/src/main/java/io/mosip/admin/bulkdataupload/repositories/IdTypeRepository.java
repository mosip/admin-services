
package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.mosip.admin.bulkdataupload.entity.IdType;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Interface for idtype repository.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 *
 */
public interface IdTypeRepository extends BaseRepository<IdType, String> {
	/**
	 * Method that returns the list of id types for the specific language code.
	 * 
	 * @param languageCode the language code.
	 * @return the list of id types.
	 */
	@Query("FROM IdType WHERE lang_code = ?1 and (isDeleted is null or isDeleted =false) AND isActive = true")
	List<IdType> findByLangCode(String languageCode);
}
