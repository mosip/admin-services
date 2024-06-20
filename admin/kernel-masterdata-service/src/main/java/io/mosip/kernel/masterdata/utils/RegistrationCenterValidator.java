package io.mosip.kernel.masterdata.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import io.mosip.kernel.masterdata.entity.*;
import io.mosip.kernel.masterdata.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.idgenerator.spi.MachineIdGenerator;
import io.mosip.kernel.masterdata.constant.RegistrationCenterErrorCode;
import io.mosip.kernel.masterdata.dto.RegCenterPostReqDto;
import io.mosip.kernel.masterdata.dto.RegCenterPutReqDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.RegistrationCenterExtnDto;

@Component
public class RegistrationCenterValidator {

	@Autowired
	private ZoneUtils zoneUtils;

	private String negRegex;
	private String posRegex;

	/**
	 * minimum digits after decimal point in Longitude and latitude
	 */
	@Value("${mosip.min-digit-longitude-latitude:4}")
	private int minDegits;

	@Value("${mosip.recommended.centers.locCode}")
	private String locationHierarchy;


	@Autowired
	private LanguageUtils languageUtils;

	/**
	 * Constructing regex for matching the Latitude and Longitude format
	 */

	@PostConstruct
	public void constructRegEx() {
		negRegex = "^(\\-\\d{1,2}\\.\\d{" + minDegits + ",})$";
		posRegex = "^(\\d{1,2}\\.\\d{" + minDegits + ",})$";
	}

	@Autowired
	RegistrationCenterRepository registrationCenterRepository;

	@Autowired
	RegistrationCenterHistoryRepository registrationCenterHistoryRepository;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	MachineIdGenerator<String> machineIdGenerator;

	@Autowired
	MachineRepository machineRepository;

	public void validateRegCenterCreate(RegCenterPostReqDto registrationCenterDto, List<ServiceError> errors) {

		String latitude = registrationCenterDto.getLatitude();
		String longitude = registrationCenterDto.getLongitude();
		validateLocation(registrationCenterDto.getLocationCode(), errors);
		zoneUserMapValidation(registrationCenterDto, errors, getZoneIdsForUser(registrationCenterDto.getLangCode()));
		zoneStartEndTimeGtrValidation(registrationCenterDto, errors);
		lunchStartEndTimeGrtValidation(registrationCenterDto, errors);
		formatValidationLongitudeLatitude(errors, latitude, longitude);
		// checkWorkingNonworking(errors, registrationCenterDto);
		// holidayVlidation(registrationCenterDto, errors);

	}

	// list zone Id mapped with the called user
	private List<String> getZoneIdsForUser(String langCode) {
		List<String> zoneIds;
		List<Zone> zones = zoneUtils.getLeafZones(langCode);
		zoneIds = zones.parallelStream().map(Zone::getCode).collect(Collectors.toList());
		return zoneIds;
	}

	public List<String> getSubZoneIdsForUser(String langCode) {
		List<String> zoneIds;
		List<Zone> zones = zoneUtils.getSubZones(langCode);
		zoneIds = zones.parallelStream().map(Zone::getCode).collect(Collectors.toList());
		return zoneIds;
	}

	// validation to check entered zoneCode is mapped with eligible user or not
	// and
	// is valid zoneCode
	private void zoneUserMapValidation(RegCenterPostReqDto registrationCenterDto, List<ServiceError> errors,
			List<String> zoneIds) {

		if (!zoneIds.isEmpty()) {
			if (!zoneIds.contains(registrationCenterDto.getZoneCode())) {
				errors.add(new ServiceError(RegistrationCenterErrorCode.INVALIDE_ZONE.getErrorCode(),
						String.format(RegistrationCenterErrorCode.INVALIDE_ZONE.getErrorMessage(),
								registrationCenterDto.getZoneCode())));
			}
		}
	}

	// validate to check the format of latitude and longitude
	// Latitude or Longitude must have minimum 4 digits after decimal
	private void formatValidationLongitudeLatitude(List<ServiceError> errors, String latitude, String longitude) {

		if (!((Pattern.matches(negRegex, latitude) || Pattern.matches(posRegex, latitude))
				&& (Pattern.matches(negRegex, longitude) || Pattern.matches(posRegex, longitude)))) {
			errors.add(
					new ServiceError(RegistrationCenterErrorCode.REGISTRATION_CENTER_FORMATE_EXCEPTION.getErrorCode(),
							RegistrationCenterErrorCode.REGISTRATION_CENTER_FORMATE_EXCEPTION.getErrorMessage()));
		}
	}

	// validation to check the RegCenter Lunch Start Time is greater
	// than RegCenter
	// Lunch End Time
	private void lunchStartEndTimeGrtValidation(RegCenterPostReqDto registrationCenterDto, List<ServiceError> errors) {
		// validation to check the RegCenter Lunch Start Time is greater than
		// RegCenter
		// Lunch End Time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime lunchStartTime = LocalTime.parse("00:00:00", formatter);
		if ((registrationCenterDto.getLunchStartTime() != null
				&& !registrationCenterDto.getLunchStartTime().equals(lunchStartTime))
				&& registrationCenterDto.getLunchStartTime().isAfter(registrationCenterDto.getLunchEndTime())) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_END_EXCEPTION.getErrorCode(),
					String.format(
							RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_END_EXCEPTION.getErrorMessage(),
							registrationCenterDto.getLunchEndTime())));

		}
		if ((registrationCenterDto.getLunchEndTime() != null
				&& !registrationCenterDto.getLunchEndTime().equals(lunchStartTime))
				&& registrationCenterDto.getLunchEndTime().isAfter(registrationCenterDto.getCenterEndTime())) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_END_CENTER_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_END_CENTER_END_EXCEPTION
							.getErrorMessage(), registrationCenterDto.getLunchEndTime())));

		}
		if ((registrationCenterDto.getLunchStartTime() != null
				&& !registrationCenterDto.getLunchStartTime().equals(lunchStartTime))
				&& registrationCenterDto.getLunchStartTime().isBefore(registrationCenterDto.getCenterStartTime())) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_CENTER_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_CENTER_END_EXCEPTION
							.getErrorMessage(), registrationCenterDto.getLunchEndTime())));
		}
	}

	// validation to check the RegCenter Start Time is greater than
	// RegCenter End Time
	private void zoneStartEndTimeGtrValidation(RegCenterPostReqDto registrationCenterDto, List<ServiceError> errors) {
		if (registrationCenterDto.getCenterStartTime().isAfter(registrationCenterDto.getCenterEndTime())) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_START_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_START_END_EXCEPTION.getErrorMessage(),
							registrationCenterDto.getCenterEndTime())));
		}
	}


	// call a method while updating to created new recored for the ID and
	// Language
	// which is not
	// there in DB
	public List<RegistrationCenterExtnDto> createRegCenterPut(List<RegistrationCenter> newregistrationCenterList,
			RegistrationCenter registrationCenterEntity, RegCenterPutReqDto registrationCenterDto) {
		List<RegistrationCenterExtnDto> newrRegistrationCenterDtoList;
		RegistrationCenterHistory registrationCenterHistoryEntity;
		RegistrationCenter registrationCenter;
		registrationCenterEntity.setId(registrationCenterDto.getId());

		registrationCenter = registrationCenterRepository.create(registrationCenterEntity);

		newregistrationCenterList.add(registrationCenter);

		// creating registration center history
		registrationCenterHistoryEntity = MetaDataUtils.setCreateMetaData(registrationCenterEntity,
				RegistrationCenterHistory.class);
		registrationCenterHistoryEntity.setEffectivetimes(registrationCenterEntity.getCreatedDateTime());
		registrationCenterHistoryEntity.setCreatedDateTime(registrationCenterEntity.getCreatedDateTime());
		registrationCenterHistoryRepository.create(registrationCenterHistoryEntity);

		newrRegistrationCenterDtoList = MapperUtils.mapAll(newregistrationCenterList, RegistrationCenterExtnDto.class);
		return newrRegistrationCenterDtoList;
	}


	// call method generate ID or validate with DB
	public String generateMachineIdOrvalidateWithDB() {
		String uniqueId = machineIdGenerator.generateMachineId();
		List<Machine> renMachine = machineRepository
				.findMachineByIdAndIsDeletedFalseorIsDeletedIsNullNoIsActive(uniqueId);

		return renMachine.isEmpty() ? uniqueId : generateMachineIdOrvalidateWithDB();
	}

	public void validateRegCenterUpdate(String zoneCode, LocalTime centerStartTime, LocalTime centerEndTime,
			LocalTime lunchStartingTime, LocalTime lunchEndingTime, String latitude, String longitude,
			Map<String, Boolean> workingNonWorkingDaysDto, String langCode, String locationCode,
			List<ServiceError> errors) {
		validateLocation(locationCode, errors);
		zoneUserMapValidation(zoneCode, errors, getZoneIdsForUser(langCode));
		zoneStartEndTimeGtrValidation(centerStartTime, centerEndTime, errors);
		lunchStartEndTimeGrtValidation(lunchStartingTime, lunchEndingTime, centerStartTime, centerEndTime, errors);
		formatValidationLongitudeLatitude(errors, latitude, longitude);
		// checkWorkingNonworking(errors, workingNonWorkingDaysDto);
		// holidayVlidation(registrationCenterDto, errors);

	}


	private void validateLocation(String locationCode, List<ServiceError> errors) {
		Location location = locationRepository.findLocationByCodeAndLanguageCode(locationCode,
				languageUtils.getDefaultLanguage());
		if (null == location) {
			errors.add(new ServiceError(RegistrationCenterErrorCode.LOCATION_INVALID.getErrorCode(), String
					.format(RegistrationCenterErrorCode.LOCATION_INVALID.getErrorMessage(), locationCode)));
			return;

		}

		if (Integer.parseInt(locationHierarchy) != location.getHierarchyLevel()) {
			errors.add(new ServiceError(RegistrationCenterErrorCode.LOCATION_HIERARCHY_INVALID.getErrorCode(), String
					.format(RegistrationCenterErrorCode.LOCATION_HIERARCHY_INVALID.getErrorMessage(), locationCode)));
		}

	}


	// validation to check entered zoneCode is mapped with eligible user or not
	// and
	// is valid zoneCode
	private void zoneUserMapValidation(String zoneCode, List<ServiceError> errors, List<String> zoneIds) {

		if (!zoneIds.isEmpty()) {
			if (!zoneIds.contains(zoneCode)) {
				errors.add(new ServiceError(RegistrationCenterErrorCode.INVALIDE_ZONE.getErrorCode(),
						String.format(RegistrationCenterErrorCode.INVALIDE_ZONE.getErrorMessage(), zoneCode)));
			}
		}
	}

	// validation to check the RegCenter Lunch Start Time is greater
	// than RegCenter
	// Lunch End Time
	private void lunchStartEndTimeGrtValidation(LocalTime lunchStartingTime, LocalTime lunchEndingTime,
			LocalTime centerStartTime, LocalTime centerEndTime, List<ServiceError> errors) {
		// validation to check the RegCenter Lunch Start Time is greater than
		// RegCenter
		// Lunch End Time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime lunchStartTime = LocalTime.parse("00:00:00", formatter);
		if ((lunchStartingTime != null && !lunchStartingTime.equals(lunchStartTime))
				&& lunchStartingTime.isAfter(lunchEndingTime)) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_END_EXCEPTION.getErrorCode(),
					String.format(
							RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_END_EXCEPTION.getErrorMessage(),
							lunchEndingTime)));

		}
		if ((lunchEndingTime != null && !lunchEndingTime.equals(lunchStartTime))
				&& lunchEndingTime.isAfter(centerEndTime)) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_END_CENTER_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_END_CENTER_END_EXCEPTION
							.getErrorMessage(), lunchEndingTime)));

		}
		if ((lunchStartingTime != null && !lunchStartingTime.equals(lunchStartTime))
				&& lunchStartingTime.isBefore(centerStartTime)) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_CENTER_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_LUNCH_START_CENTER_END_EXCEPTION
							.getErrorMessage(), lunchEndingTime)));
		}
	}

	// validation to check the RegCenter Start Time is greater than
	// RegCenter End Time
	private void zoneStartEndTimeGtrValidation(LocalTime centerStartTime, LocalTime centerEndTime,
			List<ServiceError> errors) {
		if (centerStartTime.isAfter(centerEndTime)) {
			errors.add(new ServiceError(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_START_END_EXCEPTION.getErrorCode(),
					String.format(RegistrationCenterErrorCode.REGISTRATION_CENTER_START_END_EXCEPTION.getErrorMessage(),
							centerEndTime)));
		}
	}

}
