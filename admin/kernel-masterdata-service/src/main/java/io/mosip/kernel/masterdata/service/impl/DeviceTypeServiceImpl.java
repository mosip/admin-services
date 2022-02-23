package io.mosip.kernel.masterdata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.DeviceTypeErrorCode;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.DeviceTypeDto;
import io.mosip.kernel.masterdata.dto.DeviceTypePutDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DeviceTypeExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.dto.response.ColumnCodeValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.DeviceSpecification;
import io.mosip.kernel.masterdata.entity.DeviceType;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.DeviceSpecificationRepository;
import io.mosip.kernel.masterdata.repository.DeviceTypeRepository;
import io.mosip.kernel.masterdata.service.DeviceTypeService;
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
 * Service class have methods to save a DeviceType Details to the Database table
 * 
 * @author Megha Tanga
 * @author Ayush Saxena
 * @since 1.0.0
 *
 */
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

	@Autowired
	AuditUtil auditUtil;

	/**
	 * Reference to DeviceTypeRepository.
	 */
	@Autowired
	DeviceTypeRepository deviceTypeRepository;

	@Autowired
	DeviceSpecificationRepository deviceSpecificationRepository;

	@Autowired
	private FilterTypeValidator filterValidator;

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	private FilterColumnValidator filterColumnValidator;

	@Autowired
	private PageUtils pageUtils;

	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceTypeService#createDeviceTypes(io.
	 * mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	public CodeAndLanguageCodeID createDeviceType(DeviceTypeDto deviceType) {
		DeviceType renDeviceType = null;
		try {
			DeviceType entity = MetaDataUtils.setCreateMetaData(deviceType, DeviceType.class);
			renDeviceType = deviceTypeRepository.create(entity);
		} catch (DataAccessLayerException | DataAccessException
				| IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_CREATE, DeviceType.class.getCanonicalName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorCode(),
							DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorMessage()),
					"ADM-637");
			throw new MasterDataServiceException(DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorCode(),
					DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		CodeAndLanguageCodeID codeLangCodeId = new CodeAndLanguageCodeID();
		MapperUtils.map(renDeviceType, codeLangCodeId);
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, DeviceType.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						DeviceType.class.getSimpleName(), codeLangCodeId.getCode()),"ADM-937");
		return codeLangCodeId;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceTypeService#createDeviceTypes(io.
	 * mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	public CodeAndLanguageCodeID updateDeviceType(DeviceTypePutDto deviceTypePutDto) {
		CodeAndLanguageCodeID codeAndLanguageCodeID=new CodeAndLanguageCodeID();

		//DeviceType deviceType=null;
		try {
			List<DeviceType> deviceTypes = deviceTypeRepository
					.findtoUpdateDeviceTypeByCode(deviceTypePutDto.getCode());
			if (!EmptyCheckUtils.isNullEmpty(deviceTypes)) {
				/*
				 * if(!deviceTypeDto.getIsActive()) { List<DeviceSpecification>
				 * deviceSpecification = deviceSpecificationRepository
				 * .findByLangCodeAndDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(
				 * deviceTypeDto.getLangCode(), deviceTypeDto.getCode());
				 * 
				 * if (!EmptyCheckUtils.isNullEmpty(deviceSpecification)) { throw new
				 * RequestException(
				 * DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_MAPPING_EXCEPTION.getErrorCode(),
				 * DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_MAPPING_EXCEPTION.getErrorMessage());
				 * } masterdataCreationUtil.updateMasterDataDeactivate(DeviceType.class,
				 * deviceTypeDto.getCode()); }
				 */
				deviceTypePutDto = masterdataCreationUtil.updateMasterData(DeviceType.class, deviceTypePutDto);
				DeviceType deviceType = MetaDataUtils.setUpdateMetaData(deviceTypePutDto, deviceTypes.get(0), false);
				deviceTypeRepository.update(deviceType);
				MapperUtils.map(deviceType, codeAndLanguageCodeID);
			} else {
				throw new RequestException(DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorCode(),
						DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
			
		} catch (DataAccessLayerException | DataAccessException
				| IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_CREATE, DeviceType.class.getCanonicalName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorCode(),
							DeviceTypeErrorCode.DEVICE_TYPE_INSERT_EXCEPTION.getErrorMessage()),
					"ADM-637");
			throw new MasterDataServiceException(DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_EXCEPTION.getErrorCode(),
					DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, DeviceType.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						DeviceType.class.getSimpleName(), codeAndLanguageCodeID.getCode()),"ADM-938");
		return codeAndLanguageCodeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceTypeService#getAllDeviceTypes(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public PageDto<DeviceTypeExtnDto> getAllDeviceTypes(int pageNumber, int pageSize, String sortBy, String orderBy) {
		List<DeviceTypeExtnDto> deviceTypes = null;
		PageDto<DeviceTypeExtnDto> deviceTypesPages = null;
		try {
			Page<DeviceType> pageData = deviceTypeRepository
					.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				deviceTypes = MapperUtils.mapAll(pageData.getContent(), DeviceTypeExtnDto.class);
				deviceTypesPages = new PageDto<>(pageData.getNumber(), pageData.getTotalPages(),
						pageData.getTotalElements(), deviceTypes);
			} else {
				throw new DataNotFoundException(DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorCode(),
						DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		return deviceTypesPages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceTypeService#deviceTypeSearch(io.
	 * mosip.kernel.masterdata.dto.request.SearchDto)
	 */
	@SuppressWarnings("null")
	@Override
	public PageResponseDto<DeviceTypeExtnDto> deviceTypeSearch(SearchDtoWithoutLangCode searchRequestDto) {
		PageResponseDto<DeviceTypeExtnDto> pageDto = new PageResponseDto<>();

		List<DeviceTypeExtnDto> deviceTypeList = null;

		if (filterValidator.validate(DeviceTypeExtnDto.class, searchRequestDto.getFilters())) {
			Pagination pagination = searchRequestDto.getPagination();
			List<SearchSort> sort = searchRequestDto.getSort();
			pageUtils.validateSortField(DeviceTypeExtnDto.class, DeviceType.class, sort);
			searchRequestDto.setPagination(new Pagination(0, Integer.MAX_VALUE));
			searchRequestDto.setSort(Collections.emptyList());
			Page<DeviceType> page = masterdataSearchHelper.searchMasterdataWithoutLangCode(DeviceType.class,
					searchRequestDto, null);

			if (page.getContent() != null && !page.getContent().isEmpty()) {
				deviceTypeList = MapperUtils.mapAll(page.getContent(), DeviceTypeExtnDto.class);
				pageDto = pageUtils.sortPage(deviceTypeList, sort, pagination);
			}

		}
		return pageDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DeviceTypeService#deviceTypeFilterValues(
	 * io.mosip.kernel.masterdata.dto.request.FilterValueDto)
	 */
	@Override
	public FilterResponseCodeDto deviceTypeFilterValues(FilterValueDto filterValueDto) {
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();
		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), DeviceType.class)) {
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				masterDataFilterHelper
						.filterValuesWithCodeWithoutLangCode(DeviceType.class, filterDto, filterValueDto, "code")
						.forEach(filterValue -> {
							if (filterValue != null) {
								ColumnCodeValue columnValue = new ColumnCodeValue();
								columnValue.setFieldCode(filterValue.getFieldCode());
								columnValue.setFieldID(filterDto.getColumnName());
								columnValue.setFieldValue(filterValue.getFieldValue());
								columnValueList.add(columnValue);
							}
						});
			}
			filterResponseDto.setFilters(columnValueList);

		}
		return filterResponseDto;
	}

	@Override
	public StatusResponseDto updateDeviceType(String code, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();

		List<DeviceType> deviceTypes = null;
		try {
			deviceTypes = deviceTypeRepository.findtoUpdateDeviceTypeByCode(code);
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceType.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorCode(),
							DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-638");
			throw new MasterDataServiceException(DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorCode(),
					DeviceTypeErrorCode.DEVICE_TYPE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}

		if (deviceTypes != null && !deviceTypes.isEmpty()) {
			if (!isActive) {
				List<DeviceSpecification> deviceSpecification = deviceSpecificationRepository
						.findByDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(code);
				if (!EmptyCheckUtils.isNullEmpty(deviceSpecification)) {
					throw new RequestException(DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_MAPPING_EXCEPTION.getErrorCode(),
							DeviceTypeErrorCode.DEVICE_TYPE_UPDATE_MAPPING_EXCEPTION.getErrorMessage());
				}
			}
			masterdataCreationUtil.updateMasterDataStatus(DeviceType.class, code, isActive, "code");
		} else {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, DeviceType.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorCode(),
							DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorMessage()),
					"ADM-639");
			throw new DataNotFoundException(DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceTypeErrorCode.DEVICE_TYPE_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		response.setStatus("Status updated successfully for Device Types");
		return response;
	}

}
