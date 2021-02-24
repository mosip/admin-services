package io.mosip.hotlist.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.mosip.hotlist.entity.Hotlist;

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
	 * @param isDeleted the is deleted
	 * @return the optional
	 */
	Optional<Hotlist> findByIdHashAndIdTypeAndIsDeleted(String idHash, String idType, Boolean isDeleted);
	
	/**
	 * Exists by id hash and id type.
	 *
	 * @param idHash the id hash
	 * @param idType the id type
	 * @param isDeleted the is deleted
	 * @return the boolean
	 */
	Boolean existsByIdHashAndIdTypeAndIsDeleted(String idHash, String idType, Boolean isDeleted);

	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @param isDeleted the is deleted
	 * @return the list
	 */
	List<Hotlist> findByStatusAndIsDeleted(String status, Boolean isDeleted);

	/**
	 * Find by expiry timestamp less than current timestamp.
	 *
	 * @param currentTimestamp the current timestamp
	 * @param isDeleted the is deleted
	 * @return the list
	 */
	List<Hotlist> findByExpiryTimestampLessThanAndIsDeleted(LocalDateTime currentTimestamp, Boolean isDeleted);
	
	/**
	 * Find by is deleted.
	 *
	 * @param isDeleted the is deleted
	 * @return the list
	 */
	List<Hotlist> findByIsDeleted(Boolean isDeleted);
}
