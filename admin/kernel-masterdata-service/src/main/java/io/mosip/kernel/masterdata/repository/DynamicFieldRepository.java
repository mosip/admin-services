package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.mosip.kernel.masterdata.dto.DynamicFieldNameDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.DynamicField;


/**
 * 
 * @author anusha
 *
 */
@Repository
public interface DynamicFieldRepository extends BaseRepository<DynamicField, String> {

	/**
	 * Get All dynamic fields based on pagination
	 * 
	 * @param langCode
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT * FROM master.dynamic_field WHERE (is_deleted is null OR is_deleted = false) and lang_code=?1",
			countQuery="SELECT COUNT(id) FROM master.dynamic_field WHERE (is_deleted is null OR is_deleted = false) and lang_code=?1",
			nativeQuery = true)
	Page<DynamicField> findAllDynamicFieldsByLangCode(String langCode, Pageable pageable);
	
	/**
	 * Get All dynamic fields based on pagination
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT * FROM master.dynamic_field WHERE (is_deleted is null OR is_deleted = false)", 
			countQuery="SELECT COUNT(id) FROM master.dynamic_field WHERE (is_deleted is null OR is_deleted = false)",
			nativeQuery= true)
	Page<DynamicField> findAllDynamicFields(Pageable pageable);
	
	/**
	 * Get dynamic field based on id
	 * @param id
	 * @return
	 */
	@Query("FROM DynamicField WHERE (isDeleted is null OR isDeleted = false) and id=?1")
	DynamicField findDynamicFieldById(String id);
	
	/**
	 * 
	 * @param fieldName
	 * @param langCode
	 * @return
	 */
	@Query("FROM DynamicField WHERE lower(name)=lower(?1) and langCode=?2")
	List<DynamicField> findAllDynamicFieldByNameAndLangCode(String fieldName, String langCode);

	
	/**
	 * 
	 * @param fieldName
	 * @param langCode
	 * @return
	 */
	@Query("FROM DynamicField WHERE lower(name)=lower(?1) and langCode=?2 and (isDeleted is null OR isDeleted = false)")
	List<DynamicField> findAllDynamicFieldByNameLangCodeAndisDeleted(String fieldName, String langCode);

	
	/**
	 *
	 * @param fieldName
	 * @return
	 */
	@Query("FROM DynamicField WHERE lower(name)=lower(?1)")
	List<DynamicField> findAllDynamicFieldByName(String fieldName);

	/**
	 * Update all the fields of dynamic field except name
	 * @param id
	 * @param description
	 * @param langCode
	 * @param dataType
	 * @param updatedDateTime
	 * @param updatedBy
	 * @param fieldVal
	 * @return
	 */
	@Modifying
	@Query("UPDATE DynamicField SET description=?2, langCode=?3, dataType=?4 , updatedDateTime=?5, updatedBy=?6, valueJson=?7"
			+ " WHERE (isDeleted is null OR isDeleted = false) and id=?1")
	int updateDynamicField(String id, String description, String langCode, String dataType,
			LocalDateTime updatedDateTime, String updatedBy, String fieldVal);

	/**
	 * Update status of dynamic field of particular name
	 *
	 * @param name
	 * @param isActive
	 * @param updatedDateTime
	 * @param updatedBy
	 * @return
	 */
	@Modifying
	@Query("UPDATE DynamicField SET isActive=?2 , updatedDateTime=?3, updatedBy=?4"
			+ " WHERE (isDeleted is null OR isDeleted = false) and name=?1")
	int updateAllDynamicFieldIsActive(String name, boolean isActive, LocalDateTime updatedDateTime, String updatedBy);

	@Modifying
	@Query("UPDATE DynamicField SET isActive=?3 , updatedDateTime=?4, updatedBy=?5"
			+ " WHERE (isDeleted is null OR isDeleted = false) and lower(name)=lower(?1) and valueJson like ?2")
	int updateDynamicFieldIsActive(String name, String code, boolean isActive, LocalDateTime updatedDateTime, String updatedBy);
	
	@Query("SELECT DISTINCT name FROM DynamicField WHERE (isDeleted is null or isDeleted = false)")
	List<String> getDistinctDynamicFields();

	@Query("SELECT new io.mosip.kernel.masterdata.dto.DynamicFieldNameDto(name, description, langCode, isActive) FROM DynamicField " +
			"WHERE (isDeleted is null or isDeleted = false) " +
			"group by name, description, langCode, isActive")
	List<DynamicFieldNameDto> getDistinctDynamicFieldsWithDescription();

	/**
	 * Update dynamic field value specific to a language code
	 * 
	 * @param id
	 * @param valueJson
	 * @param langCode
	 * @param updatedDateTime
	 * @param updatedBy
	 * @return
	 */
	@Modifying
	@Query("UPDATE DynamicField SET valueJson=?2, updatedDateTime=?4, updatedBy=?5"
			+ " WHERE (isDeleted is null OR isDeleted = false) and id=?1 and langCode=?3")
	int updateDynamicFieldValue(String id, String valueJson, String langCode, LocalDateTime updatedDateTime, String updatedBy);
	
	/**
	 * update isDeleted as true
	 * @param updatedDateTime
	 * @param updatedBy
	 * @return
	 */
	@Modifying
	@Query("UPDATE DynamicField SET isDeleted=true, updatedDateTime=?3, updatedBy=?4 WHERE lower(name)=lower(?1) AND valueJson like ?2 AND (isDeleted is null OR isDeleted = false)")
	int deleteDynamicField(String name, String code, LocalDateTime updatedDateTime, String updatedBy);

	@Modifying
	@Query("UPDATE DynamicField SET isDeleted=true, updatedDateTime=?2, updatedBy=?3 WHERE lower(name)=lower(?1) AND (isDeleted is null OR isDeleted = false)")
	int deleteAllDynamicField(String fieldName, LocalDateTime updatedDateTime, String updatedBy);


	/**
	 * Get All dynamic fields based on pagination
	 *
	 * @param langCode
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT * FROM master.dynamic_field WHERE lang_code=?1 and ((cr_dtimes BETWEEN ?2 AND ?3) or (upd_dtimes BETWEEN ?2 AND ?3) or (del_dtimes BETWEEN ?2 AND ?3))",
			countQuery="SELECT COUNT(id) FROM master.dynamic_field WHERE lang_code=?1 and ((cr_dtimes BETWEEN ?2 AND ?3) or (upd_dtimes BETWEEN ?2 AND ?3) or (del_dtimes BETWEEN ?2 AND ?3))",
			nativeQuery = true)
	Page<DynamicField> findAllLatestDynamicFieldsByLangCode(String langCode, LocalDateTime lastUpdated,
															LocalDateTime currentTimeStamp, Pageable pageable);

	/**
	 * Get All dynamic fields based on pagination
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT * FROM master.dynamic_field WHERE ((cr_dtimes BETWEEN ?1 AND ?2) or (upd_dtimes BETWEEN ?1 AND ?2) or (del_dtimes BETWEEN ?1 AND ?2))",
			countQuery="SELECT COUNT(id) FROM master.dynamic_field WHERE ((cr_dtimes BETWEEN ?1 AND ?2) or (upd_dtimes BETWEEN ?1 AND ?2) or (del_dtimes BETWEEN ?1 AND ?2))",
			nativeQuery= true)
	Page<DynamicField> findAllLatestDynamicFields(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp, Pageable pageable);


	/**
	 * Get All dynamic fields based on pagination
	 *
	 * @param langCode
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT DISTINCT name, lang_code FROM master.dynamic_field WHERE lang_code=?1 and ((cr_dtimes BETWEEN ?2 AND ?3) or (upd_dtimes BETWEEN ?2 AND ?3) or (del_dtimes BETWEEN ?2 AND ?3))",
			countQuery="SELECT count(id) FROM master.dynamic_field WHERE lang_code=?1 and ((cr_dtimes BETWEEN ?2 AND ?3) or (upd_dtimes BETWEEN ?2 AND ?3) or (del_dtimes BETWEEN ?2 AND ?3)) GROUP BY name, lang_code",
			nativeQuery = true)
	Page<Object[]> findAllLatestDynamicFieldNamesByLangCode(String langCode, LocalDateTime lastUpdated,
															LocalDateTime currentTimeStamp, Pageable pageable);

	/**
	 * Get All dynamic fields based on pagination
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT DISTINCT name, lang_code FROM master.dynamic_field WHERE ((cr_dtimes BETWEEN ?1 AND ?2) or (upd_dtimes BETWEEN ?1 AND ?2) or (del_dtimes BETWEEN ?1 AND ?2)) ORDER BY name, lang_code",
			countQuery="SELECT count(id) FROM master.dynamic_field WHERE ((cr_dtimes BETWEEN ?1 AND ?2) or (upd_dtimes BETWEEN ?1 AND ?2) or (del_dtimes BETWEEN ?1 AND ?2)) GROUP BY name, lang_code",
			nativeQuery= true)
	Page<Object[]> findAllLatestDynamicFieldNames(LocalDateTime lastUpdated, LocalDateTime currentTimeStamp, Pageable pageable);

	@Query("FROM DynamicField WHERE lower(name)=lower(?1) and langCode=?2")
	List<DynamicField> findAllDynamicFieldValuesByNameAndLangCode(String fieldName, String langCode);

	@Query("FROM DynamicField WHERE (isDeleted is null OR isDeleted = false) AND lower(name)=lower(?1)")
	List<DynamicField> findAllDynamicFieldValuesByName(String fieldName);

	@Query("FROM DynamicField WHERE (isDeleted is null OR isDeleted = false) and lower(name)=lower(?1) and valueJson like ?2")
	List<DynamicField> findAllByFieldNameAndCode(String name, String code);
}
