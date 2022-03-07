package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.mosip.kernel.masterdata.constant.RequestErrorCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.utils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.RegistrationCenterErrorCode;
import io.mosip.kernel.masterdata.constant.ZoneErrorCode;
import io.mosip.kernel.masterdata.dto.FilterData;
import io.mosip.kernel.masterdata.dto.getresponse.ZoneNameResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.ZoneExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.response.ColumnCodeValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.repository.ZoneRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import io.mosip.kernel.masterdata.utils.ZoneUtils;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;

/**
 * Zone Service Implementation
 * 
 * @author Abhishek Kumar
 * @author Srinivasan
 * @since 1.0.0
 *
 */
@Service
public class ZoneServiceImpl implements ZoneService {

	private static final Logger logger = LoggerFactory.getLogger(ZoneServiceImpl.class);

	@Autowired
	private ZoneUtils zoneUtils;

	@Autowired
	ZoneUserRepository zoneUserRepository;

	@Autowired
	ZoneRepository zoneRepository;

	@Autowired
	private ZoneUserService zoneUserService;

	@Autowired
	private RegistrationCenterRepository registrationCenterRepo;
	
	@Autowired
	private FilterColumnValidator filterColumnValidator;
	
	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;

	@Value("${mosip.kernel.registrationcenterid.length}")
	private int centerIdLength;

	@Autowired
	private LanguageUtils languageUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.ZoneService#getUserZoneHierarchy(java.lang
	 * .String)
	 */
	@Override
	public List<ZoneExtnDto> getUserZoneHierarchy(String langCode) {
		List<Zone> zones = zoneUtils.getUserZones();
		if (zones != null && !zones.isEmpty()) {
			List<Zone> zoneList = zones.parallelStream().filter(z -> z.getLangCode().equals(langCode))
					.collect(Collectors.toList());
			return MapperUtils.mapAll(zoneList, ZoneExtnDto.class);
		}
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.ZoneService#getUserLeafZone(java.lang.
	 * String)
	 */
	@Cacheable(value = "zones", key = "'zone'.concat('-').concat(#langCode)", condition = "#langCode != null")
	@Override
	public List<ZoneExtnDto> getUserLeafZone(String langCode) {
		List<Zone> zones = zoneUtils.getUserLeafZones(langCode);
		if (zones != null && !zones.isEmpty()) {
			List<Zone> zoneList = zones.parallelStream().filter(z -> z.getLangCode().equals(langCode))
					.collect(Collectors.toList());
			return MapperUtils.mapAll(zoneList, ZoneExtnDto.class);
		}
		return Collections.emptyList();	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.ZoneService#getSubZone(java.lang.
	 * String, java.lang.
	 * String)
	 */
	@Override
	public List<ZoneExtnDto> getSubZones(String langCode) {
		List<Zone> zones = zoneUtils.getSubZones(langCode);
		if (zones != null && !zones.isEmpty()) {
			List<Zone> zoneList = zones.parallelStream().filter(z -> z.getLangCode().equals(langCode))
					.collect(Collectors.toList());
			return MapperUtils.mapAll(zoneList, ZoneExtnDto.class);
		}
		return Collections.emptyList();	
	}

	
	@Override
	public List<ZoneExtnDto> getLeafZonesBasedOnLangCode(String langCode) {
		List<Zone> zones = zoneUtils.getLeafZones(langCode);
		if (zones != null && !zones.isEmpty()) {
			return MapperUtils.mapAll(zones, ZoneExtnDto.class);
		}
		return Collections.emptyList();	
	}

	@Override
	public ZoneNameResponseDto getZoneNameBasedOnLangCodeAndUserID(String userID, String langCode) {
		ZoneNameResponseDto zoneNameResponseDto = new ZoneNameResponseDto();
		ZoneUser zoneUser = null;
		Zone zone = null;
		try {
			UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user == null || !user.getUsername().equals(userID)) {
				throw new MasterDataServiceException(RequestErrorCode.REQUEST_DATA_NOT_VALID.getErrorCode(),
						RequestErrorCode.REQUEST_DATA_NOT_VALID.getErrorMessage());
			}

			if(zoneUserRepository.count() <= 0) {
				setFirstUserZone(userID, langCode);
			}

			zoneUser = zoneUserRepository.findZoneByUserIdNonDeleted(userID);
			if (zoneUser == null) {
				throw new DataNotFoundException(ZoneErrorCode.ZONEUSER_ENTITY_NOT_FOUND.getErrorCode(),
						ZoneErrorCode.ZONEUSER_ENTITY_NOT_FOUND.getErrorMessage());
			}
			zone = zoneRepository.findZoneByCodeAndLangCodeNonDeleted(zoneUser.getZoneCode(), langCode);
			if (zone == null) {
				throw new DataNotFoundException(ZoneErrorCode.ZONE_ENTITY_NOT_FOUND.getErrorCode(),
						ZoneErrorCode.ZONE_ENTITY_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(ZoneErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
					ZoneErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage());
		}
		zoneNameResponseDto.setZoneName(zone.getName());
		zoneNameResponseDto.setZoneCode(zone.getCode());
		return zoneNameResponseDto;
	}

	/**
	 * This feature is allowed only for GLOBAL_ADMIN's
	 * @param userId
	 * @param langCode
	 */
	private void setFirstUserZone(String userId, String langCode) {
		boolean isGlobalAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().anyMatch(a -> a.getAuthority().equals("ROLE_GLOBAL_ADMIN"));
		if(!isGlobalAdmin)
			return;

		logger.info("Global_admin User {} logged without zone mapping, and current zoneuser mapping is zero", userId);
		ZoneUser zoneUser = new ZoneUser();
		zoneUser.setUserId(userId);
		zoneUser.setZoneCode(zoneRepository.getRootZone(langCode).getCode());
		zoneUser.setLangCode(langCode);
		zoneUser.setIsActive(true);
		zoneUser.setCreatedBy("Default Insert From Server");
		zoneUser.setCreatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
		zoneUser.setIsDeleted(false);
		zoneUserRepository.save(zoneUser);
	}

	@Override
	public boolean getUserValidityZoneHierarchy(String langCode, String zoneCode) {
		List<Zone> zones = zoneUtils.getUserZones();
		boolean zoneValid = false;
		List<ZoneExtnDto> zoneExtnList = new ArrayList<>();
		if (zones != null && !zones.isEmpty()) {
			List<Zone> zoneList = zones.parallelStream().filter(z -> z.getLangCode().equals(langCode))
					.collect(Collectors.toList());
			zoneExtnList = MapperUtils.mapAll(zoneList, ZoneExtnDto.class);
		}
		for (ZoneExtnDto zoneExtnDto : zoneExtnList) {
			if (zoneCode.equals(zoneExtnDto.getCode())) {
				zoneValid = true;
			}
		}
		return zoneValid;
	}

	/*@Override
	public boolean authorizeZone(String rId) {
		String centerId = rId.substring(0, centerIdLength);
		RegistrationCenter registrationCenter = getZoneBasedOnTheRId(centerId);
		return isPresentInTheHierarchy(registrationCenter);
	}

	private RegistrationCenter getZoneBasedOnTheRId(String centerId) {
		List<RegistrationCenter> registrationCenter = null;
		try {
			registrationCenter = registrationCenterRepo.findByIdAndIsDeletedFalseOrNull(centerId);
		} catch (DataAccessException | DataAccessLayerException ex) {
			throw new MasterDataServiceException("ADM-PKT-500", "Error occured while fetching packet");
		}
		if (registrationCenter.isEmpty()) {
			throw new DataNotFoundException(RegistrationCenterErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(), RegistrationCenterErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
		}

		return registrationCenter.get(0);
	}*/

	/*private boolean isPresentInTheHierarchy(RegistrationCenter registrationCenter) {
		List<Zone> zones = zoneUtils.getUserLeafZones(registrationCenter.getLangCode());
		boolean isAuthorized = zones.stream().anyMatch(zone -> zone.getCode().equals(registrationCenter.getZoneCode()));
		if (!isAuthorized) {
			throw new RequestException(ZoneErrorCode.ADMIN_UNAUTHORIZED.getErrorCode(),
					ZoneErrorCode.ADMIN_UNAUTHORIZED.getErrorMessage());
		}

		return isAuthorized;
	}*/

	@Override
	public ZoneNameResponseDto getZone(String zoneCode, String langCode) {
		ZoneNameResponseDto zoneNameResponseDto = new ZoneNameResponseDto();
		Zone zone = null;
		try {
		zone = zoneRepository.findZoneByCodeAndLangCodeNonDeletedAndIsActive(zoneCode,
				langCode == null? languageUtils.getDefaultLanguage() : langCode);

		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(ZoneErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
					ZoneErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage());
		}

		if (zone == null) {
			throw new DataNotFoundException(ZoneErrorCode.ZONE_ENTITY_NOT_FOUND.getErrorCode(),
					ZoneErrorCode.ZONE_ENTITY_NOT_FOUND.getErrorMessage());
		}

		zoneNameResponseDto.setZoneName(zone.getName());
		return zoneNameResponseDto;
	}

	@Override
	public FilterResponseCodeDto zoneFilterValues(FilterValueDto filterValueDto) {
		// TODO Auto-generated method stub
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();

		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), Zone.class)) {
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				FilterResult<FilterData> filterResult = masterDataFilterHelper.filterValuesWithCode(Zone.class, filterDto,
						filterValueDto, "code");
				filterResult.getFilterData().forEach(filterValue -> {
					ColumnCodeValue columnValue = new ColumnCodeValue();
					columnValue.setFieldCode(filterValue.getFieldCode());
					columnValue.setFieldID(filterDto.getColumnName());
					columnValue.setFieldValue(filterValue.getFieldValue());
					columnValueList.add(columnValue);
				});
				filterResponseDto.setTotalCount(filterResult.getTotalCount());
			}
			filterResponseDto.setFilters(columnValueList);
		}
		return filterResponseDto;
	}
	
	@Override
	public List<Zone> getZoneListBasedonZoneName(String zoneName) {

		return zoneRepository.findListZonesFromZoneName(zoneName.toLowerCase());
	}


}
