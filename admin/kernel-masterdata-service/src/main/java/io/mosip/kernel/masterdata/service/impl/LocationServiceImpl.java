package io.mosip.kernel.masterdata.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.mosip.kernel.masterdata.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.LocationErrorCode;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.MasterdataSearchErrorCode;
import io.mosip.kernel.masterdata.dto.FilterData;
import io.mosip.kernel.masterdata.dto.LocationCreateDto;
import io.mosip.kernel.masterdata.dto.LocationDto;
import io.mosip.kernel.masterdata.dto.LocationLevelDto;
import io.mosip.kernel.masterdata.dto.LocationLevelResponseDto;
import io.mosip.kernel.masterdata.dto.LocationPutDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.LocationExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.LocationHierarchyRepository;
import io.mosip.kernel.masterdata.repository.LocationRepository;
import io.mosip.kernel.masterdata.service.LocationService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;

/**
 * Class will fetch Location details based on various parameters this class is
 * implemented from {@link LocationService}}
 * 
 * @author Srinivasan
 * @author Tapaswini
 * @author Sidhant Agarwal
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
@Service
public class LocationServiceImpl implements LocationService {

	/**
	 * creates an instance of repository class {@link LocationRepository}}
	 */
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LocationHierarchyRepository locationHierarchyRepository;

	@Autowired
	FilterColumnValidator filterColumnValidator;

	@Autowired
	MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	private MasterdataCreationUtil masterDataCreateUtil;

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Autowired
	private PageUtils pageUtils;
	//private List<Location> childHierarchyList = null;
	//private List<Location> parentHierarchyList = null;
	//private List<String> childList = null;

	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	@Value("${mosip.level:4}")
	private short level;

	@Autowired
	private AuditUtil auditUtil;
	
	@Autowired
	private FilterTypeValidator filterTypeValidator;

	/**
	 * This method will all location details from the Database. Refers to
	 * {@link LocationRepository} for fetching location hierarchy
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#getLocationDetails(
	 * java. lang.String)
	 */
	@Cacheable(value = "locations", key = "'location'.concat('-').concat(#langCode)", condition = "#langCode != null")
	@Override
	public LocationHierarchyResponseDto getLocationDetails(String langCode) {
		List<LocationHierarchyDto> responseList = null;
		LocationHierarchyResponseDto locationHierarchyResponseDto = new LocationHierarchyResponseDto();
		List<Object[]> locations = null;
		try {

			locations = locationRepository.findDistinctLocationHierarchyByIsDeletedFalse(langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		if (!locations.isEmpty()) {

			responseList = MapperUtils.objectToDtoConverter(locations);

		} else {
			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		locationHierarchyResponseDto.setLocations(responseList);
		return locationHierarchyResponseDto;
	}

	/**
	 * This method will fetch location hierarchy based on location code and language
	 * code Refers to {@link LocationRepository} for fetching location hierarchy
	 * 
	 * @param locCode  - location code
	 * @param langCode - language code
	 * @return LocationHierarchyResponseDto-
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#
	 * getLocationHierarchyByLangCode(java.lang.String, java.lang.String)
	 */

	@Cacheable(value = "locations", key = "'location'.concat('-').concat('ext').concat('-').concat(#locCode).concat('-').concat(#langCode)", 
			condition = "#locCode != null && #langCode != null")
	@Override
	public LocationResponseDto getLocationHierarchyByLangCode(String locCode, String langCode) {
		List<Location> childList = null;
		List<Location> parentList = null;
		List<Location> childHierarchyList = new ArrayList<>();
		List<Location> parentHierarchyList = new ArrayList<>();
		LocationResponseDto locationHierarchyResponseDto = new LocationResponseDto();
		try {

			List<Location> locHierList = getLocationHierarchyList(locCode, langCode);
			if (locHierList != null && !locHierList.isEmpty()) {
				for (Location locationHierarchy : locHierList) {
					String currentParentLocCode = locationHierarchy.getParentLocCode();
					childList = getChildList(locCode, langCode,childHierarchyList);
					parentList = getParentList(currentParentLocCode, langCode,parentHierarchyList);

				}
				locHierList.addAll(childList);
				locHierList.addAll(parentList);

				List<LocationDto> locationHierarchies = MapperUtils.mapAll(locHierList, LocationDto.class);
				locationHierarchyResponseDto.setLocations(locationHierarchies);

			} else {
				throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		}

		catch (DataAccessException | DataAccessLayerException e) {

			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));

		}
		return locationHierarchyResponseDto;
	}

	@CacheEvict(value = "locations", allEntries = true)
	@Override
	@Transactional
	public LocationPostResponseDto createLocation(LocationCreateDto dto) {
		Location locationEntity = null;
		LocationPostResponseDto locationPostResponseDto = new LocationPostResponseDto();

		try {
			if (dto != null) {
				dto = masterdataCreationUtil.createMasterData(Location.class, dto);
				if (!EmptyCheckUtils.isNullEmpty(dto.getParentLocCode())) {
					List<Location> parentLocList = locationRepository
							.findLocationHierarchyByCodeAndLanguageCode(dto.getParentLocCode(), dto.getLangCode());
					if (CollectionUtils.isEmpty(parentLocList)) {
						auditUtil.auditRequest(
								String.format(MasterDataConstant.FAILURE_CREATE, LocationDto.class.getSimpleName()),
								MasterDataConstant.AUDIT_SYSTEM,
								String.format(MasterDataConstant.FAILURE_DESC,
										LocationErrorCode.PARENT_LOC_NOT_FOUND.getErrorCode(),
										LocationErrorCode.PARENT_LOC_NOT_FOUND.getErrorMessage()),
								"ADM-574");
						throw new MasterDataServiceException(LocationErrorCode.PARENT_LOC_NOT_FOUND.getErrorCode(),
								LocationErrorCode.PARENT_LOC_NOT_FOUND.getErrorMessage());
					}
				}
				if(locationHierarchyRepository.findByLangCodeAndLevelAndName(dto.getLangCode(), dto.getHierarchyLevel(), dto.getHierarchyName())==null) {
					throw new RequestException(LocationErrorCode.INVALID_HIERARCY_LEVEL.getErrorCode(),
							LocationErrorCode.INVALID_HIERARCY_LEVEL.getErrorMessage());
				}
				List<Location> list = locationRepository.findByNameAndLevelLangCode(dto.getName(),
						dto.getHierarchyLevel(), dto.getLangCode());
				if (list != null && !list.isEmpty()) {
					auditUtil.auditRequest(
							String.format(MasterDataConstant.FAILURE_CREATE, LocationDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorCode(),
									String.format(
											LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorMessage(),
											dto.getName())),
							"ADM-575");
					throw new RequestException(LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorCode(),
							String.format(LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorMessage(),
									dto.getName()));
				}

				locationEntity = MetaDataUtils.setCreateMetaData(dto, Location.class);
				locationEntity = locationRepository.create(locationEntity);
				MapperUtils.map(locationEntity, locationPostResponseDto);
			}
		} catch (DataAccessLayerException | DataAccessException ex) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_CREATE, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorCode(),
							LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(ex)),
					"ADM-576");
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(ex));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_CREATE, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorCode(),
							LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorMessage()),
					"ADM-577");
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_INSERT_EXCEPTION.getErrorMessage());
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, LocationDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						LocationDto.class.getSimpleName(), locationPostResponseDto.getCode()),
				"ADM-578");
		return locationPostResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.LocationService#updateLocationDetails( io.
	 * mosip.kernel.masterdata.dto.RequestDto)
	 */
	@CacheEvict(value = "locations", allEntries = true)
	@Override
	@Transactional
	public LocationPutResponseDto updateLocationDetails(LocationPutDto locationDto) {
		LocationPutResponseDto postLocationCodeResponseDto = new LocationPutResponseDto();
		try {
			if (!EmptyCheckUtils.isNullEmpty(locationDto.getParentLocCode())) {
				List<Location> parentLocList = locationRepository.findLocationHierarchyByCodeAndLanguageCode(
						locationDto.getParentLocCode(), locationDto.getLangCode());
				if (CollectionUtils.isEmpty(parentLocList)) {
					throw new RequestException(LocationErrorCode.PARENT_LOC_NOT_EXIST.getErrorCode(), String.format(
							LocationErrorCode.PARENT_LOC_NOT_EXIST.getErrorMessage(), locationDto.getParentLocCode()));
				}
			}
			if(locationHierarchyRepository.findByLangCodeAndLevelAndName(locationDto.getLangCode(), locationDto.getHierarchyLevel(), locationDto.getHierarchyName())==null) {
				throw new RequestException(LocationErrorCode.INVALID_HIERARCY_LEVEL.getErrorCode(),
						LocationErrorCode.INVALID_HIERARCY_LEVEL.getErrorMessage());
			}
			List<Location> list = locationRepository.findByNameAndLevelLangCodeNotCode(locationDto.getName(),
					locationDto.getHierarchyLevel(), locationDto.getLangCode(), locationDto.getCode());
			if (list != null && !list.isEmpty()) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.FAILURE_CREATE, LocationDto.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorCode(),
								String.format(
										LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorMessage(),
										locationDto.getName())),
						"ADM-575");
				throw new RequestException(LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorCode(),
						String.format(LocationErrorCode.LOCATION_ALREDAY_EXIST_UNDER_HIERARCHY.getErrorMessage(),
								locationDto.getName()));
			}
			/*
			 * if (!CollectionUtils.isEmpty(locationRepository.
			 * findLocationHierarchyByParentLocCodeAndLanguageCode( locationDto.getCode(),
			 * locationDto.getLangCode()))) { if (!locationDto.getIsActive()) { throw new
			 * MasterDataServiceException(
			 * LocationErrorCode.LOCATION_CHILD_STATUS_EXCEPTION.getErrorCode(),
			 * LocationErrorCode.LOCATION_CHILD_STATUS_EXCEPTION.getErrorMessage()); } }
			 */
			locationDto = masterDataCreateUtil.updateMasterData(Location.class, locationDto);
			Location location = locationRepository.findLocationByCodeAndLanguageCode(locationDto.getCode(),
					locationDto.getLangCode());
			if (location == null) {
				throw new MasterDataServiceException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			} else {
				location = MetaDataUtils.setUpdateMetaData(locationDto, location, false);
				locationRepository.update(location);
				MapperUtils.map(location, postLocationCodeResponseDto);
				if (!location.getIsActive()) {
					masterdataCreationUtil.updateMasterDataDeactivate(Location.class, location.getCode());
				}
			}

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorCode(),
							LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(e)),
					"ADM-579");
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, LocationDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						LocationDto.class.getSimpleName(), postLocationCodeResponseDto.getCode()),
				"ADM-580");
		return postLocationCodeResponseDto;
	}

	@CacheEvict(value = "locations", allEntries = true)
	@Override
	@Transactional
	public StatusResponseDto updateLocationStatus(String code, boolean isActive) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();
		try {
			List<Location> locations = locationRepository.findLocationByCode(code);
			if (!EmptyCheckUtils.isNullEmpty(locations)) {
				masterDataCreateUtil.updateMasterDataStatus(Location.class, code, isActive, "code");
			}
			else {
				throw new MasterDataServiceException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
			statusResponseDto.setStatus("Status updated successfully for location");
		} catch (IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorCode(),
							LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(e)),
					"ADM-580");
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, LocationDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						LocationDto.class.getSimpleName()),
				"ADM-581");
		return statusResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.LocationService#deleteLocationDetials(
	 * java .lang.String)
	 */
	@CacheEvict(value = "locations", allEntries = true)
	@Override
	@Transactional
	public CodeResponseDto deleteLocationDetials(String locationCode) {
		List<Location> locations = null;
		CodeResponseDto codeResponseDto = new CodeResponseDto();
		try {
			locations = locationRepository.findByCode(locationCode);
			if (!locations.isEmpty()) {

				locations.stream().map(MetaDataUtils::setDeleteMetaData)
						.forEach(location -> locationRepository.update(location));

			} else {
				throw new RequestException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}

		} catch (DataAccessException | DataAccessLayerException ex) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_UPDATE_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(ex));
		}
		codeResponseDto.setCode(locationCode);
		return codeResponseDto;
	}

	/**
	 * Method creates location hierarchy data into the table based on the request
	 * parameter sent {@inheritDoc}
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#
	 * getLocationDataByHierarchyName(java.lang.String)
	 */
	@Cacheable(value = "locations", key = "'location'.concat('-').concat('hierarchy').concat('-').concat(#hierarchyName)", condition = "#hierarchyName != null")
	@Override
	public LocationResponseDto getLocationDataByHierarchyName(String hierarchyName) {
		List<Location> locationlist = null;
		LocationResponseDto locationHierarchyResponseDto = new LocationResponseDto();
		try {
			locationlist = locationRepository.findAllByHierarchyNameIgnoreCase(hierarchyName);

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		if (!(locationlist.isEmpty())) {
			List<LocationDto> hierarchyList = MapperUtils.mapAll(locationlist, LocationDto.class);
			locationHierarchyResponseDto.setLocations(hierarchyList);

		} else {

			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return locationHierarchyResponseDto;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#
	 * getImmediateChildrenByLocCodeAndLangCode(java.lang.String, java.lang.String)
	 */
	@Cacheable(value = "locations", key = "'location'.concat('-').concat('immediate').concat('-').concat(#locCode).concat('-').concat(#langCode)",
			condition = "#locCode != null && #langCode != null")
	@Override
	public LocationResponseDto getImmediateChildrenByLocCodeAndLangCode(String locCode, String langCode) {
		List<Location> locationlist = null;
		LocationResponseDto locationHierarchyResponseDto = new LocationResponseDto();
		try {
			locationlist = locationRepository.findLocationHierarchyByParentLocCodeAndLanguageCode(locCode, langCode);

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		if (locationlist.isEmpty()) {
			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		List<LocationDto> locationDtoList = MapperUtils.mapAll(locationlist, LocationDto.class);
		locationHierarchyResponseDto.setLocations(locationDtoList);
		return locationHierarchyResponseDto;
	}

	/**
	 * fetches location hierarchy details from database based on location code and
	 * language code
	 * 
	 * @param locCode  - location code
	 * @param langCode - language code
	 * @return List<LocationHierarchy>
	 */
	private List<Location> getLocationHierarchyList(String locCode, String langCode) {

		return locationRepository.findLocationHierarchyByCodeAndLanguageCode(locCode, langCode);
	}

	/**
	 * fetches location hierarchy details from database based on parent location
	 * code and language code
	 * 
	 * @param locCode  - location code
	 * @param langCode - language code
	 * @return List<LocationHierarchy>
	 */
	private List<Location> getLocationChildHierarchyList(String locCode, String langCode) {

		return locationRepository.findLocationHierarchyByParentLocCodeAndLanguageCode(locCode, langCode);

	}

	/**
	 * This method fetches child hierarchy details of the location based on location
	 * code
	 * 
	 * @param locCode  - location code
	 * @param langCode - language code
	 * @return List<Location>
	 */
	private List<Location> getChildList(String locCode, String langCode,List<Location> childHierarchyList) {

		if (locCode != null && !locCode.isEmpty()) {
			List<Location> childLocHierList = getLocationChildHierarchyList(locCode, langCode);
			childHierarchyList.addAll(childLocHierList);
			for (int i = 0; i < childLocHierList.size(); i++) {
				if (null != childLocHierList.get(i).getCode() && !childLocHierList.get(i).getCode().isEmpty()) {
					getChildList(childLocHierList.get(i).getCode(), langCode, childHierarchyList);
				}
			}
			
		//childLocHierList.parallelStream().filter(entity -> entity.getCode() != null && !entity.getCode().isEmpty())
		//			.map(entity -> getChildList(entity.getCode(), langCode,childHierarchyList)).collect(Collectors.toList());

		}
		

		return childHierarchyList;
	}

	/**
	 * This method fetches parent hierarchy details of the location based on parent
	 * Location code
	 * 
	 * @param locCode  - location code
	 * @param langCode - language code
	 * @return List<LocationHierarcy>
	 */
	private List<Location> getParentList(String locCode, String langCode,List<Location> parentHierarchyList) {

		if (locCode != null && !locCode.isEmpty()) {
			List<Location> parentLocHierList = getLocationHierarchyList(locCode, langCode);
			parentHierarchyList.addAll(parentLocHierList);
			for (int i = 0; i < parentLocHierList.size(); i++) {
				if (parentLocHierList.get(i).getParentLocCode() != null
						&& !parentLocHierList.get(i).getParentLocCode().isEmpty()) {
					getParentList(parentLocHierList.get(i).getParentLocCode(), langCode, parentHierarchyList);
				}
			}
		/*	parentLocHierList.parallelStream()
					.filter(entity -> entity.getParentLocCode() != null && !entity.getParentLocCode().isEmpty())
					.map(entity -> getParentList(entity.getParentLocCode(), langCode,parentHierarchyList)).collect(Collectors.toList());
		*/
		}

		return parentHierarchyList;
	}

	@Override
	public Map<Short, List<Location>> getLocationByLangCodeAndHierarchyLevel(String langCode, Short hierarchyLevel) {
		Map<Short, List<Location>> map = new TreeMap<>();
		List<Location> locations = locationRepository.getAllLocationsByLangCodeAndLevel(langCode, hierarchyLevel);
		if (!EmptyCheckUtils.isNullEmpty(locations)) {
			for (Location location : locations) {
				if (map.containsKey(location.getHierarchyLevel())) {
					map.get(location.getHierarchyLevel()).add(location);
				} else {
					List<Location> list = new ArrayList<>();
					list.add(location);
					map.put(location.getHierarchyLevel(), list);
				}
			}
			return map;
		} else {
			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#validateLocationName(
	 * java. lang.String)
	 */

	@Cacheable(value = "locations", key = "'location'.concat('-').concat(#locationName)", condition = "#locationName != null")
	@Override
	public StatusResponseDto validateLocationName(String locationName) {
		StatusResponseDto statusResponseDto = null;
		boolean isPresent = false;
		try {
			statusResponseDto = new StatusResponseDto();
			statusResponseDto.setStatus(MasterDataConstant.INVALID);
			isPresent = locationRepository.isLocationNamePresent(locationName);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage());
		}
		if (isPresent) {
			statusResponseDto.setStatus(MasterDataConstant.VALID);
		}
		return statusResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#getLocations(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public PageDto<LocationExtnDto> getLocations(int pageNumber, int pageSize, String sortBy, String orderBy) {
		List<LocationExtnDto> locations = null;
		PageDto<LocationExtnDto> pageDto = null;
		try {
			Page<Location> pageData = locationRepository
					.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				locations = MapperUtils.mapAll(pageData.getContent(), LocationExtnDto.class);
				pageDto = new PageDto<>(pageData.getNumber(), pageData.getTotalPages(), pageData.getTotalElements(),
						locations);
			} else {
				throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage());
		}
		return pageDto;
	}

	/**
	 * This method fetches child hierarchy details of the location based on location
	 * code, here child isActive can true or false
	 * 
	 * @param locCode - location code
	 * @return List<Location>
	 */
	@Override
	public List<String> getChildList(String locCode,List<String> childList) {
		
		List<String> resultList = getChildByLocCode(locCode, childList);
		if (!resultList.isEmpty())
			return resultList;
		return Arrays.asList(locCode);
	}

	private List<String> getChildByLocCode(String locCode,List<String> childList) {
		if (locCode != null && !locCode.isEmpty()) {
			List<String> childLocHierList = getLocationChildHierarchyList(locCode);
			childList.addAll(childLocHierList);
			for (int i = 0; i < childLocHierList.size(); i++) {
				if (null != childLocHierList.get(i)) {
					getChildByLocCode(locCode, childList);
				}
			}
			//childLocHierList.parallelStream().filter(Objects::nonNull).map(entity->getChildByLocCode(locCode,childList)).collect(Collectors.toList());
		}
		return childList;
	}

	/**
	 * fetches location hierarchy details from database based on parent location
	 * code and language code, children's isActive is either true or false
	 * 
	 * @param locCode - location code
	 * @return List<LocationHierarchy>
	 */
	private List<String> getLocationChildHierarchyList(String locCode) {

		return locationRepository.findDistinctByparentLocCode(locCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#locationFilterValues(
	 * io. mosip.kernel.masterdata.dto.request.FilterValueDto)
	 *//*
	@Override
	public FilterResponseCodeDto locationFilterValues(FilterValueDto filterValueDto) {
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();
		List<String> hierarchyNames = null;
		try {
			hierarchyNames = locationRepository.findLocationAllHierarchyNames();
			String langCode = filterValueDto.getLanguageCode();
			for (FilterDto filter : filterValueDto.getFilters()) {
				String columnName = filter.getColumnName();
				String type = filter.getType();
				if (EmptyCheckUtils.isNullEmpty(type)) {
					auditUtil.auditRequest(
							String.format(MasterDataConstant.FILTER_FAILED, LocationDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorCode(),
									ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorMessage()),
							"ADM-584");

					throw new RequestException(ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorCode(),
							ValidationErrorCode.NO_FILTER_COLUMN_FOUND.getErrorMessage());
				}
				if (!type.equals(FilterColumnEnum.UNIQUE.toString()) && !type.equals(FilterColumnEnum.ALL.toString())) {
					auditUtil.auditRequest(
							String.format(MasterDataConstant.FILTER_FAILED, LocationDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorCode(),
									ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorMessage()),
							"ADM-585");
					throw new RequestException(ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorCode(),
							ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorMessage());
				}
				if (!hierarchyNames.contains(columnName) && !columnName.equals(MasterDataConstant.IS_ACTIVE)) {
					auditUtil.auditRequest(
							String.format(MasterDataConstant.FILTER_FAILED, LocationDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									ValidationErrorCode.COLUMN_DOESNT_EXIST.getErrorCode(),
									String.format(ValidationErrorCode.COLUMN_DOESNT_EXIST.getErrorMessage(),
											filter.getColumnName())),
							"ADM-586");
					throw new RequestException(ValidationErrorCode.COLUMN_DOESNT_EXIST.getErrorCode(), String
							.format(ValidationErrorCode.COLUMN_DOESNT_EXIST.getErrorMessage(), filter.getColumnName()));
				}
				if (filter.getType().equals(FilterColumnEnum.UNIQUE.toString())) {
					if (filter.getColumnName().equals(MasterDataConstant.IS_ACTIVE)) {
						FilterResult<FilterData> filterResult = masterDataFilterHelper.filterValuesWithCode(Location.class,
								filter, filterValueDto, "code");
						filterResult.getFilterData().forEach(filterValue -> {
							ColumnCodeValue columnValue = new ColumnCodeValue();
							columnValue.setFieldCode(filterValue.getFieldCode());
							columnValue.setFieldID(filter.getColumnName());
							columnValue.setFieldValue(filterValue.getFieldValue());
							columnValueList.add(columnValue);
						});
					}
					if (filter.getText() == null || filter.getText().isEmpty()) {
						List<Location> locationNames = locationRepository
								.findDistinctHierarchyNameAndNameValueForEmptyTextFilter(filter.getColumnName(),
										langCode);
						locationNames.forEach(locationName -> {
							ColumnCodeValue columnValue = new ColumnCodeValue();
							columnValue.setFieldID(filter.getColumnName());
							columnValue.setFieldValue(locationName.getName());
							columnValue.setFieldCode(locationName.getCode());
							columnValueList.add(columnValue);
						});

					} else {
						List<Location> locationNames = locationRepository
								.findDistinctHierarchyNameAndNameValueForTextFilter(filter.getColumnName(),
										"%" + filter.getText().toLowerCase() + "%", langCode);
						locationNames.forEach(locationName -> {
							ColumnCodeValue columnValue = new ColumnCodeValue();
							columnValue.setFieldID(filter.getColumnName());
							columnValue.setFieldValue(locationName.getName());
							columnValue.setFieldCode(locationName.getCode());
							columnValueList.add(columnValue);
						});
					}

				} else {
					if (filter.getText().isEmpty() || filter.getText() == null) {
						List<Location> locations = locationRepository
								.findAllHierarchyNameAndNameValueForEmptyTextFilter(filter.getColumnName(), langCode);
						locations.forEach(loc -> {
							ColumnCodeValue columnValue = new ColumnCodeValue();
							columnValue.setFieldID(filter.getColumnName());
							columnValue.setFieldValue(loc.getName());
							columnValue.setFieldCode(loc.getCode());
							columnValueList.add(columnValue);
						});

					} else {
						List<Location> locations = locationRepository.findAllHierarchyNameAndNameValueForTextFilter(
								filter.getColumnName(), "%" + filter.getText().toLowerCase() + "%", langCode);
						locations.forEach(loc -> {
							ColumnCodeValue columnValue = new ColumnCodeValue();
							columnValue.setFieldID(filter.getColumnName());
							columnValue.setFieldValue(loc.getName());
							columnValue.setFieldCode(loc.getCode());
							columnValueList.add(columnValue);
						});
					}

				}

			}
			filterResponseDto.setFilters(columnValueList);
		} catch (DataAccessLayerException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FILTER_FAILED, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
							LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-587");
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage());
		}

		return filterResponseDto;
	}*/

	/**
	 * Method to find out the hierrachy level from the column name
	 * 
	 * @param columnName input column name
	 * @return hierarchy level
	 */
	public String getHierarchyLevel(String columnName, String languageCode) {
		Integer level = locationHierarchyRepository.findByheirarchyLevalNameAndLangCode(columnName, languageCode);

		if (level == null) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, LocationDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							MasterdataSearchErrorCode.MISSING_FILTER_COLUMN.getErrorCode(),
							MasterdataSearchErrorCode.MISSING_FILTER_COLUMN.getErrorMessage()),
					"ADM-588");

			throw new RequestException(MasterdataSearchErrorCode.MISSING_FILTER_COLUMN.getErrorCode(),
					MasterdataSearchErrorCode.MISSING_FILTER_COLUMN.getErrorMessage());
		}
		return level.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.LocationService#getLocationCodeByLangCode(
	 * java.lang.String)
	 */
	@Override
	public LocationLevelResponseDto getLocationCodeByLangCode(String langCode) {
		Set<Location> locationList = null;
		List<LocationLevelDto> locationLevelDtoList = null;
		LocationLevelResponseDto locationLevelResponseDto = new LocationLevelResponseDto();
		try {
			locationList = locationRepository.findLocationByLangCodeLevel(langCode, level);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage());
		}
		if (locationList != null && !locationList.isEmpty()) {
			locationLevelDtoList = MapperUtils.mapAll(locationList, LocationLevelDto.class);

		} else {
			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		locationLevelResponseDto.setLocations(locationLevelDtoList);
		return locationLevelResponseDto;
	}

	@Cacheable(value = "locations", key = "'location'.concat('-').concat('info').concat('-').concat(#locationCode).concat('-').concat(#langCode)",
			condition = "#locationCode != null && #langCode != null")
	@Override
	public LocationExtnDto getLocationDetailsByLangCode(String locationCode, String langCode) {
		LocationExtnDto location = null;
		
		try {
			Location loc = locationRepository
					.findLocationByCodeAndLanguageCodeAndIsActiveTrue(locationCode,langCode);
			if (loc!=null) {
				location = MapperUtils.map(loc, LocationExtnDto.class);
				
			} else {
				throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage());
		}
		return location;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.LocationService#searchLocation(io.
	 * mosip. kernel.masterdata.dto.request.SearchDto)
	 */
	@Override
	public PageResponseDto<LocationSearchDto> searchLocation(SearchDto dto) {
		PageResponseDto<LocationSearchDto> pageDto = new PageResponseDto<>();
		List<LocationSearchDto> responseDto = null;
		pageUtils.validateSortField(Location.class, dto.getSort());
		Boolean isFilter = filterTypeValidator.validate(LocationSearchDto.class, dto.getFilters());
		if (isFilter) {
			Pagination pagination = dto.getPagination();
			List<SearchSort> sort = dto.getSort();
			pageUtils.validateSortFieldLocation(LocationSearchDto.class, Location.class, dto.getSort());
			dto.setPagination(new Pagination(0, Integer.MAX_VALUE));
			dto.setSort(Collections.emptyList());
			Page<Location> page = masterdataSearchHelper.searchMasterdata(Location.class, dto, null);
			
			if (page.getContent() != null && !page.getContent().isEmpty()) {
				responseDto = MapperUtils.mapAll(page.getContent(), LocationSearchDto.class);
				pageDto = pageUtils.sortPage(responseDto, sort, pagination);
			}
		}
		return pageDto;
	}

	@Override
	public FilterResponseCodeDto locFilterValues(FilterValueDto filterValueDto) {
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();
		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), Location.class)) {
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				FilterResult<FilterData> filterResult = masterDataFilterHelper.filterValuesWithCode(Location.class,
						filterDto, filterValueDto, "code");
				filterResult.getFilterData().forEach(filterValue -> {
					ColumnCodeValue columnCodeValue = new ColumnCodeValue();
					columnCodeValue.setFieldID(filterDto.getColumnName());
					columnCodeValue.setFieldValue(filterValue.getFieldValue());
					columnCodeValue.setFieldCode(filterValue.getFieldCode());
					columnValueList.add(columnCodeValue);
				});
				filterResponseDto.setTotalCount(filterResult.getTotalCount());
			}
			filterResponseDto.setFilters(columnValueList);
		}
		return filterResponseDto;
	}


	@Override
	public LocationResponseDto getImmediateChildrenByLocCode(String locationCode, List<String> languageCodes) {
		List<Location> locationlist = null;
		LocationResponseDto locationHierarchyResponseDto = new LocationResponseDto();
		try {
			locationlist = locationRepository.findLocationHierarchyByParentLocCode(locationCode, languageCodes);

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		if (locationlist.isEmpty()) {
			throw new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		List<LocationDto> locationDtoList = MapperUtils.mapAll(locationlist, LocationDto.class);
		locationHierarchyResponseDto.setLocations(locationDtoList);
		return locationHierarchyResponseDto;
	}
}
