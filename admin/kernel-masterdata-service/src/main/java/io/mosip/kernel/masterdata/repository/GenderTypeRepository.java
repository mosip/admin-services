package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.Gender;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;

/**
 * Repository class for fetching gender data
 * 
 * @author Urvil Joshi
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Repository
public interface GenderTypeRepository extends BaseRepository<Gender, CodeAndLanguageCodeID> {

	/**
	 * Get all Gender based on isActive true and isDeleted as false or null
	 * 
	 * @return list of gender
	 */
	@Query("FROM Gender WHERE isActive=true AND (isDeleted=false OR isDeleted is null)")
	List<Gender> findAllByIsActiveAndIsDeleted();

	@Query("FROM Gender WHERE langCode =?1 and (isDeleted is null or isDeleted =false) and isActive = true")
	List<Gender> findGenderByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(String langCode);

	/**
	 * Delete Gender Type by code provided.
	 * 
	 * @param code            code the gender type code.
	 * @param deletedDateTime metadata Deleted Date time
	 * @param updatedBy       updatedBy
	 * @return rows modified
	 */
	@Modifying
	@Query("UPDATE Gender g SET g.updatedBy=?3 , g.isDeleted =true , g.deletedDateTime = ?2 WHERE g.code =?1 and (g.isDeleted is null or g.isDeleted =false)")
	int deleteGenderType(String code, LocalDateTime deletedDateTime, String updatedBy);

	/**
	 * Update gender type by code and langcode
	 * 
	 * @param code            code to be updated
	 * @param langCode        lang code to be updated
	 * @param genderName      genderName to be updated
	 * @param isActive        mapping is active or not
	 * @param updatedDateTime upated timestamp
	 * @param updatedBy       updating user
	 * @return no of updated rows
	 */
	@Modifying
	@Query("UPDATE Gender g SET g.genderName=?3, g.isActive=?4 ,g.updatedDateTime=?5, g.updatedBy=?6 WHERE g.code =?1 and g.langCode=?2 and (g.isDeleted is null or g.isDeleted =false)")
	int updateGenderType(String code, String langCode, String genderName, Boolean isActive,
			LocalDateTime updatedDateTime, String updatedBy);

	/**
	 * validate gender name
	 * 
	 * @param genderName gender name eg: male or female
	 * @return boolean
	 */
	@Query(value = "select EXISTS(select * from master.gender g where LOWER(g.name)=LOWER(?1) and (g.is_deleted is null or g.is_deleted =false) and g.is_active=true)", nativeQuery = true)
	boolean isGenderNamePresent(String genderName);
}
