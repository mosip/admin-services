package io.mosip.hotlist.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.mosip.hotlist.entity.Hotlist;

// TODO: Auto-generated Javadoc
/**
 * The Interface HotlistRepository.
 *
 * @author Manoj SP
 */
@Repository
public interface HotlistRepository extends JpaRepository<Hotlist, String>{

	/**
	 * Find by id hash and id type.
	 *
	 * @param idHash the id hash
	 * @param idType the id type
	 * @return the optional
	 */
	Optional<Hotlist> findByIdHashAndIdType(String idHash, String idType);
	
	/**
	 * Exists by id hash and id type.
	 *
	 * @param idHash the id hash
	 * @param idType the id type
	 * @return the boolean
	 */
	Boolean existsByIdHashAndIdType(String idHash, String idType);

	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @return the list
	 */
	List<Hotlist> findByStatus(String status);

	/**
	 * Find by expiry timestamp less than current timestamp.
	 *
	 * @param currentTimestamp the current timestamp
	 * @return the list
	 */
	List<Hotlist> findByExpiryTimestampLessThan(LocalDateTime currentTimestamp);
}
