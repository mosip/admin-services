package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.LocationHierarchyErrorCode;
import io.mosip.kernel.masterdata.dto.LocationHierarchyLevelDto;
import io.mosip.kernel.masterdata.dto.LocationHierarchyLevelResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyResponseDto;
import io.mosip.kernel.masterdata.entity.LocationHierarchy;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.LocationHierarchyRepository;
import io.mosip.kernel.masterdata.service.LocationHierarchyService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;

/**
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Service
public class LocationHierarchyServiceImpl implements LocationHierarchyService {

	@Autowired
	private LocationHierarchyRepository locationHierarchyRepository;

	@Cacheable(value = "location-hierarchy", key = "'locationhierarchy'.concat('-').concat(#level).concat('-').concat(#langCode)",
			condition = "#langCode != null")
	@Override
	public LocationHierarchyLevelResponseDto getLocationHierarchyLevelAndLangCode(short level, String langCode) {
		LocationHierarchyLevelResponseDto locationHierarchyLevelResponseDto = new LocationHierarchyLevelResponseDto();
		List<LocationHierarchy> locationHierarchyList = null;
		List<LocationHierarchyLevelDto> locationHierarchyLevelDtos = null;
		try {
			locationHierarchyList = locationHierarchyRepository
					.findAllByLevelAndLangCodeAndIsDeletedFalseorIsDeletedIsNull(level, langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorCode(),
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (locationHierarchyList != null && !locationHierarchyList.isEmpty()) {
			locationHierarchyLevelDtos = MapperUtils.mapAll(locationHierarchyList, LocationHierarchyLevelDto.class);
		} else {

			throw new DataNotFoundException(
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_NOT_FOUND_EXCEPTION.getErrorMessage());

		}
		locationHierarchyLevelResponseDto.setLocationHierarchyLevels(locationHierarchyLevelDtos);
		return locationHierarchyLevelResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.ModuleService#getModuleLangCode(java.lang.
	 * String)
	 */
	@Cacheable(value = "location-hierarchy", key = "'locationhierarchy'.concat('-').concat(#langCode)",
			condition = "#langCode != null")
	@Override
	public LocationHierarchyLevelResponseDto getLocationHierarchyLangCode(String langCode) {
		LocationHierarchyLevelResponseDto locationHierarchyLevelResponseDto = new LocationHierarchyLevelResponseDto();
		List<LocationHierarchy> locationHierarchyList = null;
		List<LocationHierarchyLevelDto> locationHierarchyLevelDtos = null;
		try {
			locationHierarchyList = locationHierarchyRepository
					.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorCode(),
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e) + e);
		}
		if (locationHierarchyList != null && !locationHierarchyList.isEmpty()) {
			locationHierarchyLevelDtos = MapperUtils.mapAll(locationHierarchyList, LocationHierarchyLevelDto.class);

		} else {
			throw new DataNotFoundException(
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		locationHierarchyLevelResponseDto.setLocationHierarchyLevels(locationHierarchyLevelDtos);
		return locationHierarchyLevelResponseDto;
	}

	@Override
	public LocationHierarchyLevelResponseDto getLocationHierarchy(LocalDateTime lastUpdated,
			LocalDateTime currentTimestamp) {
		LocationHierarchyLevelResponseDto locationHierarchyLevelResponseDto = new LocationHierarchyLevelResponseDto();
		locationHierarchyLevelResponseDto.setLocationHierarchyLevels(new ArrayList<>());
		try {
			if (lastUpdated == null) {
				lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
			}

			List<LocationHierarchy> locationHierarchyList = locationHierarchyRepository.findByLastUpdatedAndCurrentTimeStamp(lastUpdated,
					currentTimestamp);

			if (locationHierarchyList != null && !locationHierarchyList.isEmpty()) {
				locationHierarchyLevelResponseDto.getLocationHierarchyLevels().addAll(MapperUtils.mapAll(locationHierarchyList, LocationHierarchyLevelDto.class));
			}

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorCode(),
					LocationHierarchyErrorCode.LOCATION_HIERARCHY_FETCH_EXCEPTION.getErrorMessage(), e);
		}
		return locationHierarchyLevelResponseDto;

	}
}
