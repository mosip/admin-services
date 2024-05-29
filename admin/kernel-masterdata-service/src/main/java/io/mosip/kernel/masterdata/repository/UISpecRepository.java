package io.mosip.kernel.masterdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.UISpec;

/**
 * 
 * @author Nagarjuna
 *
 */
@Repository
public interface UISpecRepository extends BaseRepository<UISpec, String> {

	@Query("FROM UISpec WHERE (isDeleted is null OR isDeleted = false) and id = ?1")
	UISpec findUISpecById(String id);

	@Query("FROM UISpec WHERE domain = ?1 and version = (select max(b.version) from UISpec b where "
			+ "(b.isDeleted is null OR b.isDeleted = false) AND b.domain = ?1 AND b.isActive = true And b.status='PUBLISHED')")
	List<UISpec> findLatestPublishedUISpec(String domain);

	@Query(value = "with maxversiongroupbydomain AS(select MAX(us.version) as maxVersion, us.type from master.ui_spec us where us.domain =?1 group by us.type)"
			+ "select * from master.ui_spec u inner join maxversiongroupbydomain mvg on mvg.maxVersion = u.version	and mvg.type = u.type and u.domain = ?1", nativeQuery = true)
	List<UISpec> findTypesByDomain(String domain);

	@Query(value = "with maxversiongroupbydomain AS(select MAX(us.version) as maxVersion, us.type from master.ui_spec us where us.domain =:domain and us.type IN :types group by us.type)"
			+ "select 	* from 	master.ui_spec u inner join maxversiongroupbydomain mvg on 	mvg.maxVersion = u.version	and mvg.type = u.type and u.domain =:domain", nativeQuery = true)
	List<UISpec> findLatestPublishedUISpec(@Param("domain") String domain, @Param("types") List<String> types);

	@Query(value = "with maxversiongroupbydomain AS(select MAX(us.version) as maxVersion, us.type from master.ui_spec us where us.domain =?1 and us.identity_schema_id =?2 group by us.type)"
			+ "select * from master.ui_spec u inner join maxversiongroupbydomain mvg on mvg.maxVersion = u.version	and mvg.type = u.type and u.domain = ?1 and u.identity_schema_id =?2", nativeQuery = true)
	List<UISpec> findTypesByDomainAndSchema(String domain,String identitySchemaId);
	
	@Query(value = "with maxversiongroupbydomain AS(select MAX(us.version) as maxVersion, us.type from master.ui_spec us where us.domain =:domain and us.type IN :types and us.identity_schema_id =:identitySchemaId group by us.type)"
			+ "select * from master.ui_spec u inner join maxversiongroupbydomain mvg on mvg.maxVersion = u.version	and mvg.type = u.type and u.domain =:domain and u.identity_schema_id =:identitySchemaId", nativeQuery = true)
	List<UISpec> findLatestPublishedUISpecByIdentitySchema(@Param("identitySchemaId") String identitySchemaId,
			@Param("domain") String domain, @Param("types") List<String> types);

	@Query("FROM UISpec WHERE domain = ?1 AND type = ?2 AND isActive = true And status='PUBLISHED' and version = (select max(b.version) from UISpec b where "
			+ "(b.isDeleted is null OR b.isDeleted = false) AND b.domain = ?1 AND b.type =?2 AND b.isActive = true And b.status='PUBLISHED')")
	UISpec findLatestVersion(String domain, String type);
	
	
	@Query("FROM UISpec WHERE domain = ?1 AND type = ?2 and identitySchemaId = ?3 And status='DRAFT'")
	List<UISpec> findUISpecByDomainTypeandIdentitySchemaId(String domain, String type, String IdentitySchemaId);

	@Modifying
	@Query("UPDATE UISpec i SET i.jsonSpec=?2, i.isActive=?3 , i.updatedDateTime=?4, i.updatedBy=?5 WHERE i.id =?1 AND i.status='DRAFT' AND (i.isDeleted is null or i.isDeleted =false)")
	int updateUISpec(String id, String idAttributeJson, boolean isActive, LocalDateTime updatedDateTime,
			String updatedBy);

	@Query(value = "select * FROM master.ui_spec WHERE is_active=?1 AND (is_deleted is null OR is_deleted = false)", countQuery = "select count(id) FROM master.ui_spec WHERE is_active=?1 AND (is_deleted is null OR is_deleted = false)", nativeQuery = true)
	Page<UISpec> findAllUISpecs(boolean isActive, Pageable pageable);

	@Modifying
	@Query("UPDATE UISpec g SET g.updatedBy=?3 , g.isDeleted =true , g.deletedDateTime = ?2 WHERE g.id =?1 and (g.isDeleted is null or g.isDeleted =false) and g.status='DRAFT'")
	int deleteUISpec(String id, LocalDateTime deletedDateTime, String updatedBy);

	@Query("FROM UISpec WHERE version=?1 AND domain = ?2 AND (isDeleted is null OR isDeleted = false) AND isActive = true AND status='PUBLISHED'")
	List<UISpec> findPublishedUISpec(double version, String domain);

	@Query("FROM UISpec WHERE version=:version AND domain =:domain AND type IN :types AND isActive = true AND status='PUBLISHED'")
	List<UISpec> findPublishedUISpec(@Param("version") double version, @Param("domain") String domain,
			@Param("types") List<String> types);
}
