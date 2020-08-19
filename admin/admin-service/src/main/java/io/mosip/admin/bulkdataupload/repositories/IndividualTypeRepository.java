package io.mosip.admin.bulkdataupload.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.mosip.admin.bulkdataupload.entity.IndividualType;
import io.mosip.admin.bulkdataupload.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * Interface to do CRUD operation on Individual type
 * 
 * @author Bal Vikash Sharma
 *
 */
public interface IndividualTypeRepository extends BaseRepository<IndividualType, CodeAndLanguageCodeID> {

	@Query(value = "FROM IndividualType t where t.isActive = true and (t.isDeleted is null or t.isDeleted = false)")
	public List<IndividualType> findAll();
	
	@Query(value = "FROM IndividualType t where t.code = ?1 and t.langCode=?2 and (t.isDeleted is null or t.isDeleted = false)")
	public IndividualType findIndividualTypeByCodeAndLangCode(String code, String langCode);

}
