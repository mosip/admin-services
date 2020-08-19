package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.Language;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Repository to perform CRUD operations on Language.
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 * @see Language
 * @see BaseRepository
 *
 */
@Repository
public interface LanguageRepository extends BaseRepository<Language, String> {

	/**
	 * This method provides all the languages having <b>isActive</b> is <b>true</b>
	 * and <b>isDeleted</b> is <b>false</b> present in MOSIP system.
	 * 
	 * @see Language
	 * @return list of language
	 */
	@Query("FROM Language where (isDeleted is null OR isDeleted = false) AND isActive = true")
	public List<Language> findAllByIsDeletedFalseOrIsDeletedIsNull();

	@Query("FROM Language l where l.code = ?1 and (l.isDeleted is null or l.isDeleted = false) AND isActive=true")
	public Language findLanguageByCode(String code);

}
