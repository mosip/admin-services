package io.mosip.kernel.masterdata.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;

/**
 * Repository class for RegistrationCenter to trigger queries.
 * 
 * @author Dharmesh Khandelwal
 * @author Abhishek Kumar
 * @author Sidhant Agarwal
 * @author Sagar Mahapatra
 * @author Uday Kumar
 * @author Ravi Kant
 * @since 1.0.0
 *
 */
@Repository
public interface RegistrationCenterRepository extends BaseRepository<RegistrationCenter, String> {

	/**
	 * This method trigger query to fetch registration centers based on
	 * latitude,longitude,proximity distance and language code
	 * 
	 * @param latitude          latitude provided by user
	 * @param longitude         longitude provided by user
	 * @param proximityDistance proximityDistance provided by user as a radius
	 * @param langCode          langCode provided by user
	 * @return list of {@link RegistrationCenter} fetched from database
	 */
	@Query
	List<RegistrationCenter> findRegistrationCentersByLat(@Param("latitude") double latitude,
			@Param("longitude") double longitude, @Param("proximitydistance") double proximityDistance,
			@Param("langcode") String langCode);

	/**
	 * This method trigger query to fetch registration centers based on id and
	 * language code.
	 * 
	 * @param id       the centerId
	 * @param langCode the languageCode
	 * @return the RegistrationCenter
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and  langCode =?2 and (isDeleted is null or isDeleted =false) and isActive = true")
	RegistrationCenter findByIdAndLangCode(String id, String langCode);

	/**
	 * This method trigger query to fetch registration centers based on id and
	 * language code.
	 * 
	 * @param id       the centerId
	 * @param langCode the languageCode
	 * @return the RegistrationCenter
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and  langCode =?2 and (isDeleted is null or isDeleted =false)")
	RegistrationCenter findByIdAndLangCodeAndIsDeletedTrue(String id, String langCode);

	/**
	 * This method triggers query to find registration center holiday location code
	 * based on id and language code.
	 * 
	 * @param id       the id against which the holiday location code needs to be
	 *                 found.
	 * @param langCode the language code against which the holiday location code
	 *                 needs to be found.
	 * @return the holiday location code fetched.
	 */
	String findRegistrationCenterHolidayLocationCodeByIdAndLangCode(String id, String langCode);

	/**
	 * This method trigger query to fetch registration centers based on locationCode
	 * and language code.
	 * 
	 * @param locationCode locationCode provided by user
	 * @param langCode     languageCode provided by user
	 * @return list of {@link RegistrationCenter} fetched from database
	 */
	@Query("FROM RegistrationCenter WHERE locationCode= ?1 and  langCode =?2 and (isDeleted is null or isDeleted =false) and isActive = true")
	List<RegistrationCenter> findByLocationCodeAndLangCode(String locationCode, String langCode);

	/**
	 * This method trigger query to fetch all registration centers based on deletion
	 * condition.
	 * 
	 * @return the list of list of {@link RegistrationCenter}.
	 */
	@Query("FROM RegistrationCenter WHERE (isDeleted is null or isDeleted =false) and isActive = true and langCode = ?1")
	List<RegistrationCenter> findAllByIsDeletedFalseOrIsDeletedIsNullAndLangCode(String langCode);

	/**
	 * This method triggers query to find registration centers based on center type
	 * code.
	 * 
	 * @param code the code against which registration centers need to be found.
	 * @return the list of registration centers.
	 */
	@Query("FROM RegistrationCenter WHERE centerTypeCode= ?1 and (isDeleted is null or isDeleted =false) and isActive = true")
	List<RegistrationCenter> findByCenterTypeCode(String code);

	@Query(value = "select EXISTS(select * from  master.loc_holiday hol where hol.is_active=true and (hol.is_deleted is null or hol.is_deleted=false) and hol.holiday_date=?1 and hol.location_code=?2)", nativeQuery = true)
	boolean validateDateWithHoliday(LocalDate date, String holidayLocationCode);

	/**
	 * This method triggers query to find registration centers based on id.
	 * 
	 * @param id - id of the registration center.
	 * @return - the fetched registration center entity.
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and (isDeleted is null or isDeleted =false) and isActive = true")
	List<RegistrationCenter> findByIdAndIsDeletedFalseOrNull(String id);

	/**
	 * This method triggers query to set the isDeleted to true for a registration
	 * center based on id given.
	 * 
	 * @param deletedDateTime the time at which the center is set to be deleted.
	 * @param id              the id of the registration center which is to be
	 *                        deleted.
	 * @param updatedBy       updated by
	 * @return the number of id deleted.
	 */
	@Modifying
	@Query("UPDATE RegistrationCenter r SET r.isDeleted =true , r.deletedDateTime = ?1, r.updatedBy = ?3 WHERE r.id =?2 and (r.isDeleted is null or r.isDeleted =false)")
	int deleteRegistrationCenter(LocalDateTime deletedDateTime, String id, String updatedBy);

	/**
	 * This method trigger query to fetch registration centers based on hierarchy
	 * List of location_code
	 * 
	 * @param codes    provided by user
	 * @param langCode language code
	 * @return list of {@link RegistrationCenter} fetched from database
	 */

	@Query(value = "SELECT r.id, r.name, r.cntrtyp_code, r.addr_line1, r.addr_line2, r.addr_line3,r.number_of_kiosks,r.per_kiosk_process_time,r.center_end_time,r.center_start_time,r.time_zone,r.contact_person,r.lunch_start_time, r.lunch_end_time,r.latitude, r.longitude, r.location_code,r.holiday_loc_code,r.contact_phone, r.working_hours, r.lang_code,r.is_active, r.cr_by,r.cr_dtimes, r.upd_by,r.upd_dtimes, r.is_deleted, r.del_dtimes, r.zone_code FROM master.registration_center r  WHERE  r.lang_code=:langcode AND r.location_code in :codes AND (r.is_deleted is null or r.is_deleted = false) AND r.is_active = true", nativeQuery = true)
	List<RegistrationCenter> findRegistrationCenterByListOfLocationCode(@Param("codes") Set<String> codes,
			@Param("langcode") String langCode);

	/**
	 * This method triggers query to find registration centers based on id.
	 * 
	 * @param id - id of the registration center.
	 * @return - the fetched registration center entity.
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and (isDeleted is null or isDeleted =false) ")
	List<RegistrationCenter> findByRegIdAndIsDeletedFalseOrNull(String id);

	/**
	 * This method triggers query to find registration centers based on id.
	 * 
	 * @param id - id of the registration center.
	 * @return - the fetched registration center entity.
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and (isDeleted is null or isDeleted =false)")
	List<RegistrationCenter> findByRegCenterIdAndIsDeletedFalseOrNull(String id);

	@Query("FROM RegistrationCenter WHERE id= ?1 and lang_code=?2 ")
	List<RegistrationCenter> findByRegIdAndLangCode(String id, String langCode);

	@Query("FROM RegistrationCenter WHERE id= ?1")
	List<RegistrationCenter> findByRegId(String id);

	/**
	 * Method to decommission the reg-center.
	 * 
	 * @param regCenterID            the center ID of the reg-center which needs to
	 *                               be decommissioned.
	 * @param deCommissionedBy       the user name retrieved from the context who
	 *                               performs this operation.
	 * @param deCommissionedDateTime date and time at which the center was
	 *                               decommissioned.
	 * @return the number of registration centers decommissioned.
	 */
	@Query("UPDATE RegistrationCenter rc SET rc.isDeleted = true, rc.isActive = false, rc.updatedBy = ?2, rc.updatedDateTime = ?3, rc.deletedDateTime = ?3 WHERE rc.id = ?1 and (rc.isDeleted is null or rc.isDeleted =false)")
	@Modifying
	int decommissionRegCenter(String regCenterID, String deCommissionedBy, LocalDateTime deCommissionedDateTime);

	/**
	 * This method trigger query to fetch registration centers based on id and
	 * language code with is_Deleted true or false or null and is_Active true or
	 * false
	 * 
	 * @param id       the centerId
	 * @param langCode the languageCode
	 * @return the RegistrationCenter
	 */
	@Query("FROM RegistrationCenter WHERE id= ?1 and  langCode =?2  and (isDeleted is null or isDeleted = false)")
	RegistrationCenter findByLangCodeAndId(String id, String langCode);
	
	@Query("FROM RegistrationCenter WHERE id= ?1 and  zoneCode =?2  and (isDeleted is null or isDeleted = false) and isActive = true")
	List<RegistrationCenter> findByRegIdAndZone(String regCenterId, String zoneCode);

	
	@Query(value = "FROM RegistrationCenter r  WHERE  r.langCode=:langcode AND r.locationCode in :codes AND (r.isDeleted is null or r.isDeleted = false) AND r.isActive = true",
			countQuery = "SELECT count(*) FROM RegistrationCenter r  WHERE  r.langCode=:langcode AND r.locationCode in :codes AND (r.isDeleted is null or r.isDeleted = false) AND r.isActive = true")
	Page<RegistrationCenter> findRegistrationCenterByListOfLocationCodePaginated(@Param("codes") Set<String> codes,
			@Param("langcode") String langCode,Pageable pageable);

	@Query("UPDATE RegistrationCenter rc SET rc.centerTypeCode=?1,rc.latitude=?2,rc.longitude=?3,rc.locationCode=?4,rc.contactPhone=?5,rc.numberOfKiosks=?6,rc.holidayLocationCode=?7,rc.workingHours=?8,rc.perKioskProcessTime=?9,rc.centerStartTime=?10,rc.centerEndTime=?11,rc.timeZone=?12,rc.lunchStartTime=?13,rc.lunchEndTime=?14,rc.zoneCode=?15,rc.updatedBy = ?17, rc.updatedDateTime =?18 where rc.id=?16")
	@Modifying
	int updateRegCenter(String centerTypeCode,String latitude ,String longitude,String locationCode,
			String contactPhone,Short numberOfKiosks,String holidayLocationCode,String workingHours,
			LocalTime perKioskProcessTime,LocalTime centerStartTime,LocalTime centerEndTime,String timeZone,
			LocalTime lunchStartTime,LocalTime lunchEndTime,String zoneCode,String id,String updatedBy, LocalDateTime updatedDateTime);


	@Query("FROM RegistrationCenter WHERE zoneCode =?1 and langCode =?2 and (isDeleted is null or isDeleted = false) and isActive = true")
	List<RegistrationCenter> findAllActiveByZoneCodeAndLangCode(String zoneCode, String langCode);

	@Query(value="select distinct(ud.id) from master.user_detail ud inner join master.registration_center rc on rc.id=ud.regcntr_id where lower(rc.name) like %?1%",nativeQuery=true)
	List<String> findUserIdBasedOnRegistrationCenterName(String regCenterName);
	
}
