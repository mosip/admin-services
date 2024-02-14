package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.mosip.kernel.masterdata.dto.*;
import io.mosip.kernel.masterdata.dto.response.*;
import io.mosip.kernel.masterdata.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.DeviceErrorCode;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.getresponse.DeviceLangCodeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DeviceResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DeviceExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.entity.Device;
import io.mosip.kernel.masterdata.entity.DeviceHistory;
import io.mosip.kernel.masterdata.entity.DeviceSpecification;
import io.mosip.kernel.masterdata.entity.DeviceType;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.DeviceRepository;
import io.mosip.kernel.masterdata.repository.DeviceSpecificationRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.service.DeviceHistoryService;
import io.mosip.kernel.masterdata.service.DeviceService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.validator.FilterColumnEnum;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;

/**
 * This class have methods to fetch and save Device Details
 * 
 * @author Megha Tanga
 * @author Sidhant Agarwal
 * @author Ravi Kant
 * @since 1.0.0
 *
 */
@Service
public class DeviceServiceImpl implements DeviceService {

	/**
	 * Field to hold Device Repository object
	 */
	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	LanguageUtils languageUtils;

	@Autowired
	RegistrationCenterRepository regCenterRepository;
	
	@Autowired
	DeviceSpecificationRepository deviceSpecificationRepository;
	/**
	 * Field to hold Device Service object
	 */
	@Autowired
	DeviceHistoryService deviceHistoryService;

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Autowired
	private FilterTypeValidator filterValidator;

	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	private FilterColumnValidator filterColumnValidator;

	@Autowired
	private ZoneUtils zoneUtils;

	@Autowired
	private DeviceUtils deviceUtil;

	@Autowired
	private PageUtils pageUtils;

	@Autowired
	private ZoneService zoneService;

	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private RegistrationCenterServiceHelper regCenterServiceHelper;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceService#getDeviceLangCode(java.lang.
	 * String)
	 */
	@Override
	public DeviceResponseDto getDeviceLangCode(String langCode) {
		List<Device> deviceList = null;
		List<DeviceDto> deviceDtoList = null;
		DeviceResponseDto deviceResponseDto = new DeviceResponseDto();
		try {
			deviceList = deviceRepository.findByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(langCode);
		} catch (DataAccessException e) {
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorMessage() + "  " + ExceptionUtils.parseException(e));
		}
		if (deviceList != null && !deviceList.isEmpty()) {
			deviceDtoList = MapperUtils.mapAll(deviceList, DeviceDto.class);

		} else {
			throw new DataNotFoundException(DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		deviceResponseDto.setDevices(deviceDtoList);
		return deviceResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceService#
	 * getDeviceLangCodeAndDeviceType(java.lang.String, java.lang.String)
	 */
	@Override
	public DeviceLangCodeResponseDto getDeviceLangCodeAndDeviceType(String langCode, String dtypeCode) {

		List<Object[]> objectList = null;
		List<DeviceLangCodeDtypeDto> deviceLangCodeDtypeDtoList = null;
		DeviceLangCodeResponseDto deviceLangCodeResponseDto = new DeviceLangCodeResponseDto();
		try {
			objectList = deviceRepository.findByLangCodeAndDtypeCode(langCode, dtypeCode);
		} catch (DataAccessException e) {
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorMessage() + "  " + ExceptionUtils.parseException(e));
		}
		if (objectList != null && !objectList.isEmpty()) {
			deviceLangCodeDtypeDtoList = MapperUtils.mapDeviceDto(objectList);
		} else {
			throw new DataNotFoundException(DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		deviceLangCodeResponseDto.setDevices(deviceLangCodeDtypeDtoList);
		return deviceLangCodeResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceService#saveDevice(io.mosip.kernel.
	 * masterdata.dto.RequestDto)
	 */
	@Override
	@Transactional
	public DeviceExtnDto createDevice(DeviceDto deviceDto) {
		Device device = null;
		Device entity = null;
		String uniqueId = null;
		DeviceHistory entityHistory = null;
		DeviceExtnDto deviceExtnDto = new DeviceExtnDto();
		try {
			validateZone(deviceDto.getZoneCode(),deviceDto.getLangCode());
			if (deviceDto.getRegCenterId() != null && !deviceDto.getRegCenterId().isEmpty()) {
				validateRegistrationCenter(deviceDto.getRegCenterId());
				regCenterServiceHelper.validateRegistrationCenterZone(deviceDto.getZoneCode(), deviceDto.getRegCenterId());
			}
			if (deviceDto != null) {
				entity = MetaDataUtils.setCreateMetaData(deviceDto, Device.class);
				entity.setId(generateId());
				entityHistory = MetaDataUtils.setCreateMetaData(entity, DeviceHistory.class);
				entityHistory.setEffectDateTime(entity.getCreatedDateTime());
				entityHistory.setCreatedDateTime(entity.getCreatedDateTime());

				device = deviceRepository.create(entity);
				deviceHistoryService.createDeviceHistory(entityHistory);
				MapperUtils.map(device, deviceExtnDto);
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_CREATE, DeviceDto.class.getCanonicalName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorCode(),
							DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorMessage() + " "
									+ ExceptionUtils.parseException(e)),
					"ADM-507");
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		} catch (IllegalArgumentException | SecurityException e1) {
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_INSERT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e1));
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, DeviceExtnDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC, device != null ? device.getId() : null),
				"ADM-508");
		return deviceExtnDto;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceService#deleteDevice(java.lang.
	 * String)
	 */
	@Override
	@Transactional
	public IdResponseDto deleteDevice(String id) {
		List<Device> foundDeviceList = null;
		Device deletedDevice = null;
		try {
			foundDeviceList = deviceRepository.findByIdAndIsDeletedFalseOrIsDeletedIsNull(id);

			if (foundDeviceList != null && !foundDeviceList.isEmpty()) {
				for (Device foundDevice : foundDeviceList) {

					MetaDataUtils.setDeleteMetaData(foundDevice);
					deletedDevice = deviceRepository.update(foundDevice);

					DeviceHistory deviceHistory = new DeviceHistory();
					MapperUtils.map(deletedDevice, deviceHistory);
					MapperUtils.setBaseFieldValue(deletedDevice, deviceHistory);

					deviceHistory.setEffectDateTime(deletedDevice.getDeletedDateTime());
					deviceHistory.setDeletedDateTime(deletedDevice.getDeletedDateTime());
					deviceHistoryService.createDeviceHistory(deviceHistory);

				}
			} else {
				throw new RequestException(DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorCode(),
						DeviceErrorCode.DEVICE_NOT_FOUND_EXCEPTION.getErrorMessage());
			}

		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}
		IdResponseDto idResponseDto = new IdResponseDto();
		idResponseDto.setId(id);
		return idResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.MachineService#
	 * getRegistrationCenterMachineMapping1(java.lang.String)
	 */
	@Override
	public PageDto<DeviceRegistrationCenterDto> getDevicesByRegistrationCenter(String regCenterId, int page, int size,
			String orderBy, String direction) {
		PageDto<DeviceRegistrationCenterDto> pageDto = new PageDto<>();
		List<DeviceRegistrationCenterDto> deviceRegistrationCenterDtoList = null;
		Page<Device> pageEntity = null;

		try {
			pageEntity = deviceRepository.findDeviceByRegCenterId(regCenterId,
					PageRequest.of(page, size, Sort.by(Direction.fromString(direction), orderBy)));
		} catch (DataAccessException e) {
			throw new MasterDataServiceException(
					DeviceErrorCode.REGISTRATION_CENTER_DEVICE_FETCH_EXCEPTION.getErrorCode(),
					DeviceErrorCode.REGISTRATION_CENTER_DEVICE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (pageEntity != null && !pageEntity.getContent().isEmpty()) {
			deviceRegistrationCenterDtoList = MapperUtils.mapAll(pageEntity.getContent(),
					DeviceRegistrationCenterDto.class);
			for (DeviceRegistrationCenterDto deviceRegistrationCenterDto : deviceRegistrationCenterDtoList) {
				deviceRegistrationCenterDto.setRegCentId(regCenterId);
			}
		} else {
			throw new RequestException(DeviceErrorCode.DEVICE_REGISTRATION_CENTER_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_REGISTRATION_CENTER_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		pageDto.setPageNo(pageEntity.getNumber());
		pageDto.setPageSize(pageEntity.getSize());
		pageDto.setSort(pageEntity.getSort());
		pageDto.setTotalItems(pageEntity.getTotalElements());
		pageDto.setTotalPages(pageEntity.getTotalPages());
		pageDto.setData(deviceRegistrationCenterDtoList);

		return pageDto;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceService#searchDevice(io.mosip.kernel
	 * .masterdata.dto.request.SearchDto)
	 */
	@Override
	public PageResponseDto<DeviceSearchDto> searchDevice(SearchDtoWithoutLangCode dto) {
		PageResponseDto<DeviceSearchDto> pageDto = new PageResponseDto<>();

		List<DeviceSearchDto> devices = null;
		List<SearchFilter> addList = new ArrayList<>();
		List<SearchFilter> removeList = new ArrayList<>();
		List<SearchFilter> zoneFilter = new ArrayList<>();
		List<Zone> zones = null;
		boolean flag = true;
		boolean isAssigned = true;
		String typeName = null;

		for (SearchFilter filter : dto.getFilters()) {
			String column = filter.getColumnName();

			if (column.equalsIgnoreCase("deviceTypeName")) {
				filter.setColumnName(MasterDataConstant.NAME);
				typeName = filter.getValue();
				if (filterValidator.validate(DeviceTypeDto.class, Arrays.asList(filter))) {

					List<Object[]> dSpecs = deviceRepository.findDeviceSpecByDeviceTypeName(filter.getValue());
					removeList.add(filter);
					addList.addAll(buildDeviceSpecificationSearchFilter(dSpecs));
				}

			}

		}
		if (flag) {

			zones = zoneUtils.getSubZones(dto.getLanguageCode());
			if (zones != null && !zones.isEmpty()) {
				zoneFilter.addAll(buildZoneFilter(zones));
			} else {
				auditUtil.auditRequest(String.format(MasterDataConstant.SEARCH_FAILED, DeviceDto.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.SEARCH_FAILED,
								DeviceErrorCode.DEVICE_NOT_TAGGED_TO_ZONE.getErrorCode(),
								DeviceErrorCode.DEVICE_NOT_TAGGED_TO_ZONE.getErrorMessage()),
						"ADM-510");
				throw new MasterDataServiceException(DeviceErrorCode.DEVICE_NOT_TAGGED_TO_ZONE.getErrorCode(),
						DeviceErrorCode.DEVICE_NOT_TAGGED_TO_ZONE.getErrorMessage());
			}
		}

		dto.getFilters().removeAll(removeList);
		Pagination pagination = dto.getPagination();
		List<SearchSort> sort = dto.getSort();
		pageUtils.validateSortField(DeviceSearchDto.class, Device.class, sort);
		dto.setPagination(new Pagination(0, Integer.MAX_VALUE));
		dto.setSort(Collections.emptyList());
		if (filterValidator.validate(DeviceSearchDto.class, dto.getFilters())) {

			OptionalFilter optionalFilter = new OptionalFilter(addList);
			OptionalFilter zoneOptionalFilter = new OptionalFilter(zoneFilter);
			Page<Device> page = null;

			page = masterdataSearchHelper.searchMasterdataWithoutLangCode(Device.class, dto,
					new OptionalFilter[] { optionalFilter, zoneOptionalFilter });

			if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
				devices = MapperUtils.mapAll(page.getContent(), DeviceSearchDto.class);
				setDeviceMetadata(devices, zones);
				setDeviceTypeNames(devices);
				setMapStatus(devices, dto.getLanguageCode());
				pageDto = pageUtils.sortPage(devices, sort, pagination);

			}

		}
		return pageDto;
	}

	/**
	 * Method to set each device zone meta data.
	 * 
	 * @param list  list of {@link DeviceSearchDto}.
	 * @param zones the list of zones.
	 */
	private void setDeviceMetadata(List<DeviceSearchDto> list, List<Zone> zones) {
		list.forEach(i -> setZoneMetadata(i, zones));
	}

	/**
	 * Method to set DeviceType Name for each Device.
	 * 
	 * @param list the {@link DeviceSearchDto}.
	 */
	private void setDeviceTypeNames(List<DeviceSearchDto> list) {
		List<DeviceSpecification> deviceSpecifications = deviceUtil.getDeviceSpec();
		List<DeviceType> deviceTypes = deviceUtil.getDeviceTypes();
		list.forEach(deviceSearchDto -> {
			deviceSpecifications.forEach(s -> {
				if (s.getId().equals(deviceSearchDto.getDeviceSpecId())) {
					String typeCode = s.getDeviceTypeCode();
					deviceTypes.forEach(mt -> {
						if (mt.getCode().equals(typeCode)) {
							deviceSearchDto.setDeviceTypeName(mt.getName());
						}
					});
				}
			});
		});
	}

	/**
	 * Method to set Map status of each Device.
	 * 
	 * @param list the {@link DeviceSearchDto}.
	 */
	private void setMapStatus(List<DeviceSearchDto> list, String langCode) {
		List<RegistrationCenter> registrationCenterList = deviceUtil.getAllRegistrationCenters(langCode);
		list.forEach(deviceSearchDto -> {
			Optional<RegistrationCenter> result = registrationCenterList.stream()
					.filter(d -> d.getId().equals(deviceSearchDto.getRegCenterId()))
					.findFirst();
			deviceSearchDto.setMapStatus(result.isPresent() ?
					String.format("%s (%s)", deviceSearchDto.getRegCenterId(), result.get().getName()) :
					deviceSearchDto.getRegCenterId());
		});
	}

	/**
	 * Method to set Zone metadata
	 * 
	 * @param devices metadata to be added
	 * @param zones   list of zones
	 * 
	 */
	private void setZoneMetadata(DeviceSearchDto devices, List<Zone> zones) {
		Optional<Zone> zone = zones.stream()
				.filter(i -> i.getCode().equals(devices.getZoneCode()) && i.getLangCode().equals(devices.getLangCode()))
				.findFirst();
		if (zone.isPresent()) {
			devices.setZone(zone.get().getName());
		}
	}

	/**
	 * Search the zone in the based on the received input filter
	 * 
	 * @param filter search input
	 * @return {@link Zone} if successful otherwise throws
	 *         {@link MasterDataServiceException}
	 */
	public Zone getZone(SearchFilter filter) {
		filter.setColumnName(MasterDataConstant.NAME);
		Page<Zone> zones = masterdataSearchHelper.searchMasterdata(Zone.class,
				new SearchDto(Arrays.asList(filter), Collections.emptyList(), new Pagination(), null), null);
		if (zones.hasContent()) {
			return zones.getContent().get(0);
		} else {
			throw new MasterDataServiceException(DeviceErrorCode.ZONE_NOT_EXIST.getErrorCode(),
					String.format(DeviceErrorCode.ZONE_NOT_EXIST.getErrorMessage(), filter.getValue()));
		}
	}

	/**
	 * Creating Search filter from the passed zones
	 * 
	 * @param zones filter to be created with the zones
	 * @return list of {@link SearchFilter}
	 */
	public List<SearchFilter> buildZoneFilter(List<Zone> zones) {
		if (zones != null && !zones.isEmpty()) {
			return zones.stream().filter(Objects::nonNull).map(Zone::getCode).distinct().map(this::buildZoneFilter)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/**
	 * Method to create SearchFilter for the recieved zoneCode
	 * 
	 * @param zoneCode input from the {@link SearchFilter} has to be created
	 * @return {@link SearchFilter}
	 */
	private SearchFilter buildZoneFilter(String zoneCode) {
		SearchFilter filter = new SearchFilter();
		filter.setColumnName(MasterDataConstant.ZONE_CODE);
		filter.setType(FilterTypeEnum.EQUALS.name());
		filter.setValue(zoneCode);
		return filter;
	}

	/**
	 * This method return Device Id list filters.
	 * 
	 * @param deviceIdList the Device Id list.
	 * @return the list of {@link SearchFilter}.
	 */
	private List<SearchFilter> buildRegistrationCenterDeviceTypeSearchFilter(List<String> deviceIdList) {
		if (deviceIdList != null && !deviceIdList.isEmpty())
			return deviceIdList.stream().filter(Objects::nonNull).map(this::buildRegistrationCenterDeviceType)
					.collect(Collectors.toList());
		return Collections.emptyList();
	}

	/**
	 * This method return Device Types list filters.
	 * 
	 * @param deviceTypes the list of Device Type.
	 * @return the list of {@link SearchFilter}.
	 */
	/*
	 * private List<SearchFilter> buildDeviceTypeSearchFilter(List<DeviceType>
	 * deviceTypes) { if (deviceTypes != null && !deviceTypes.isEmpty()) return
	 * deviceTypes.stream().filter(Objects::nonNull).map(this::buildDeviceType)
	 * .collect(Collectors.toList()); return Collections.emptyList(); }
	 */

	/**
	 * This method return Device Specification list filters.
	 * 
	 * @param deviceSpecs the list of Device Specification.
	 * @return the list of {@link SearchFilter}.
	 */
	private List<SearchFilter> buildDeviceSpecificationSearchFilter(List<Object[]> deviceSpecs) {
		SearchFilter filter = null;
		List<SearchFilter> searchFilters = new ArrayList<>();
		for (Object[] dSpecObj : deviceSpecs) {
			filter = new SearchFilter();
			filter.setColumnName("deviceSpecId");
			filter.setType(FilterTypeEnum.EQUALS.name());
			filter.setValue(dSpecObj[0].toString());
			searchFilters.add(filter);

		}

		return searchFilters;
	}

	/**
	 * This method provide search filter for provided device id.
	 * 
	 * @param deviceId the device id.
	 * @return the {@link SearchFilter}.
	 */
	private SearchFilter buildRegistrationCenterDeviceType(String deviceId) {
		SearchFilter filter = new SearchFilter();
		filter.setColumnName("id");
		filter.setType(FilterTypeEnum.EQUALS.name());
		filter.setValue(deviceId);
		return filter;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceService#deviceFilterValues(io.mosip.
	 * kernel.masterdata.dto.request.FilterValueDto)
	 */
	@Override
	public FilterResponseCodeDto deviceFilterValues(FilterValueDto filterValueDto) {
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();
		List<Zone> zones = zoneUtils.getSubZones(filterValueDto.getLanguageCode());
		if (zones == null || zones.isEmpty()) {
			return filterResponseDto;
		}

		List<FilterDto> fil = new ArrayList<>();
		filterValueDto.getFilters().forEach(f -> {
			if (null == f.getType() || f.getType().isBlank() || f.getType().isEmpty()) {
				FilterDto fd = new FilterDto();
				fd.setColumnName(f.getColumnName());
				fd.setText(f.getText());
				fd.setType(FilterColumnEnum.ALL.toString());
				fil.add(fd);
			} else
				fil.add(f);
		});
		filterValueDto.setFilters(fil);
		filterValueDto.setLanguageCode(null);
		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), Device.class))
		{
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				FilterResult<FilterData> filterResult = masterDataFilterHelper
						.filterValuesWithCode(Device.class, filterDto, filterValueDto, "id",
								zoneUtils.getZoneCodes(zones));
						filterResult.getFilterData().forEach(filterValue -> {
							if (filterValue != null) {
								ColumnCodeValue columnValue = new ColumnCodeValue();
								columnValue.setFieldCode(filterValue.getFieldCode());
								columnValue.setFieldID(filterDto.getColumnName());
								columnValue.setFieldValue(filterValue.getFieldValue());
								columnValueList.add(columnValue);
							}
						});
						filterResponseDto.setTotalCount(filterResult.getTotalCount());
			}
			filterResponseDto.setFilters(columnValueList);
		}
		return filterResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceService#decommissionDevice(java.lang
	 * .String)
	 */
	@Override
	@Transactional
	public IdResponseDto decommissionDevice(String deviceId) {
		IdResponseDto idResponseDto = new IdResponseDto();
		int decommissionedDevice = 0;

		// get devices from DB by given id
		List<Device> devices = deviceRepository.findDeviceByIdAndIsDeletedFalseorIsDeletedIsNullNoIsActive(deviceId);

		// device is not in DB
		if (devices == null || devices.isEmpty()) {
			auditUtil
					.auditRequest(
							String.format(MasterDataConstant.FAILURE_DECOMMISSION, DeviceDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorCode(), String.format(
											DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorMessage(), deviceId)),
							"ADM-511");
			throw new RequestException(DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorCode(),
					String.format(DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorMessage(), deviceId));
		}

		List<String> zoneIds;

		// get user zone and child zones list
		List<Zone> userZones = zoneUtils.getSubZones(languageUtils.getDefaultLanguage());
		zoneIds = userZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());

		// check the given device and registration center zones are come under user zone
		if (!zoneIds.contains(devices.get(0).getZoneCode())) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_DECOMMISSION, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC, DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorCode(),
							DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorMessage()),
					"ADM-512");
			throw new RequestException(DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorCode(),
					DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorMessage());
		}
		try {
			// check the device has mapped to any reg-Center
			for (Device device : devices) {
				if (device.getRegCenterId() != null && !device.getRegCenterId().isBlank()) {
					auditUtil.auditRequest(
							String.format(MasterDataConstant.FAILURE_DECOMMISSION, DeviceDto.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									DeviceErrorCode.MAPPED_TO_REGCENTER.getErrorCode(),
									DeviceErrorCode.MAPPED_TO_REGCENTER.getErrorMessage()),
							"ADM-513");
					throw new RequestException(DeviceErrorCode.MAPPED_TO_REGCENTER.getErrorCode(),
							DeviceErrorCode.MAPPED_TO_REGCENTER.getErrorMessage());
				}
			}
			decommissionedDevice = deviceRepository.decommissionDevice(deviceId, MetaDataUtils.getContextUser(),
					MetaDataUtils.getCurrentDateTime());

			// create Device history
			for (Device device : devices) {
				DeviceHistory deviceHistory = new DeviceHistory();
				MapperUtils.map(device, deviceHistory);
				MapperUtils.setBaseFieldValue(device, deviceHistory);
				deviceHistory.setIsActive(false);
				deviceHistory.setIsDeleted(true);
				deviceHistory.setUpdatedBy(MetaDataUtils.getContextUser());
				deviceHistory.setEffectDateTime(LocalDateTime.now(ZoneId.of("UTC")));
				deviceHistory.setDeletedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
				deviceHistoryService.createDeviceHistory(deviceHistory);
			}

		} catch (DataAccessException | DataAccessLayerException exception) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_DECOMMISSION, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorCode(),
							DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorMessage() + exception.getCause()),
					"ADM-514");
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_DELETE_EXCEPTION.getErrorMessage() + exception.getCause());
		}
		if (decommissionedDevice > 0) {
			idResponseDto.setId(deviceId);
		}
		return idResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.RegistrationCenterService#
	 * updateRegistrationCenter1(java.util.List)
	 */
	@Transactional
	@Override
	public DeviceExtnDto updateDevice(DevicePutReqDto devicePutReqDto) {

		Device updDevice = null;
		Device updDeviecEntity = null;
		String deviecZone = devicePutReqDto.getZoneCode();
		DeviceHistory deviceHistory = new DeviceHistory();
		DeviceExtnDto deviceExtnDto = new DeviceExtnDto();

		// call method to check the machineZone will come under Accessed user zone or
		// not
		validateZone(deviecZone,devicePutReqDto.getLangCode());
		try {
			if (devicePutReqDto.getRegCenterId() != null && !devicePutReqDto.getRegCenterId().isEmpty()) {
				validateRegistrationCenter(devicePutReqDto.getRegCenterId());
				regCenterServiceHelper.validateRegistrationCenterZone(devicePutReqDto.getZoneCode(), devicePutReqDto.getRegCenterId());
			}
			// find requested device is there or not in Device Table
			List<Device> renDevice = deviceRepository.findbyDeviceIdAndIsDeletedFalseOrIsDeletedNull(devicePutReqDto.getId());

			//devicePutReqDto = masterdataCreationUtil.updateMasterData(Device.class, devicePutReqDto);

			if (renDevice == null || renDevice.isEmpty()) {
				// create new entry
				Device crtDeviceEntity = new Device();
				crtDeviceEntity = MetaDataUtils.setCreateMetaData(devicePutReqDto, crtDeviceEntity.getClass());
				deviceRepository.create(crtDeviceEntity);

				// updating Device history
				MapperUtils.map(crtDeviceEntity, deviceHistory);
				MapperUtils.setBaseFieldValue(crtDeviceEntity, deviceHistory);
				deviceHistory.setEffectDateTime(crtDeviceEntity.getCreatedDateTime());
				deviceHistory.setCreatedDateTime(crtDeviceEntity.getCreatedDateTime());
				deviceHistoryService.createDeviceHistory(deviceHistory);

				deviceExtnDto = MapperUtils.map(crtDeviceEntity, DeviceExtnDto.class);
			}
			else {
				// updating registration center
				updDeviecEntity = MetaDataUtils.setUpdateMetaData(devicePutReqDto, renDevice.get(0), false);

				// updating Device
				updDevice = deviceRepository.update(updDeviecEntity);

				// updating Device history
				MapperUtils.map(updDevice, deviceHistory);
				MapperUtils.setBaseFieldValue(updDevice, deviceHistory);
				deviceHistory.setEffectDateTime(updDevice.getUpdatedDateTime());
				deviceHistory.setUpdatedDateTime(updDevice.getUpdatedDateTime());
				deviceHistoryService.createDeviceHistory(deviceHistory);
				deviceExtnDto = MapperUtils.map(updDevice, DeviceExtnDto.class);
			}

		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException exception) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_UPDATE,
							DeviceErrorCode.DEVICE_UPDATE_EXCEPTION.getErrorCode(),
							DeviceErrorCode.DEVICE_UPDATE_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(exception)),
					"ADM-516");
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_UPDATE_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_UPDATE_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}

		return deviceExtnDto;

	}

	// method to check the deviceZone will come under Accessed user zone or not
	private void validateZone(String deviceZone,String langCode) {
		List<String> zoneIds;
		// get user zone and child zones list
		if(langCode==null)
			langCode=languageUtils.getDefaultLanguage();
		List<Zone> subZones = zoneUtils.getSubZones(langCode);

		zoneIds = subZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());

		if (!(zoneIds.contains(deviceZone))) {
			// check the given device zones will come under accessed user zones
			throw new RequestException(DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorCode(),
					DeviceErrorCode.INVALID_DEVICE_ZONE.getErrorMessage());
		}
	}

	private void validateRegistrationCenter(String regCenterId) {
		List<RegistrationCenter> centers = regCenterRepository.findByIdAndIsDeletedFalseOrNull(regCenterId);
		if (centers == null || centers.isEmpty()) {
			throw new RequestException(DeviceErrorCode.INVALID_CENTER.getErrorCode(),
					DeviceErrorCode.INVALID_CENTER.getErrorMessage());
		}

	}

	@Override
	public StatusResponseDto updateDeviceStatus(String id, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();
		DeviceHistory deviceHistory = new DeviceHistory();
		List<Device> devices = null;
		List<DeviceSpecification> deviceSpecificationList = null;
		try {
			devices = deviceRepository.findbyDeviceIdAndIsDeletedFalseOrIsDeletedNull(id);	
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorCode(),
							DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-517");
			throw new MasterDataServiceException(DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}
		if (devices == null || devices.isEmpty()) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorCode(),
							DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorMessage()),
					"ADM-518");
			throw new DataNotFoundException(DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorCode(),
					DeviceErrorCode.DEVICE_NOT_EXISTS_EXCEPTION.getErrorMessage());
		}
		if(isActive) {
			deviceSpecificationList = deviceSpecificationRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(devices.get(0).getDeviceSpecId());
			if(deviceSpecificationList==null || deviceSpecificationList.isEmpty()) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceDto.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceErrorCode.DEVICE_SPECIFICATION_INACTIVE.getErrorCode(),
							DeviceErrorCode.DEVICE_SPECIFICATION_INACTIVE.getErrorMessage()),
					"ADM-518");
			throw new DataNotFoundException(DeviceErrorCode.DEVICE_SPECIFICATION_INACTIVE.getErrorCode(),
					DeviceErrorCode.DEVICE_SPECIFICATION_INACTIVE.getErrorMessage());
			}
		}
		masterdataCreationUtil.updateMasterDataStatus(Device.class, id, isActive, "id");
		MetaDataUtils.setUpdateMetaData(devices.get(0), deviceHistory, true);
		deviceHistory.setEffectDateTime(LocalDateTime.now(ZoneId.of("UTC")));
		deviceHistory.setIsActive(isActive);
		deviceHistoryService.createDeviceHistory(deviceHistory);
		response.setStatus("Status updated successfully for Devices");
		return response;
	}

	private String generateId() throws DataAccessLayerException, DataAccessException {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		List<Device> devices = deviceRepository.findDeviceByIdAndIsDeletedFalseorIsDeletedIsNullNoIsActive(uniqueId);
		return devices.isEmpty() ? uniqueId : generateId();
	}

}
