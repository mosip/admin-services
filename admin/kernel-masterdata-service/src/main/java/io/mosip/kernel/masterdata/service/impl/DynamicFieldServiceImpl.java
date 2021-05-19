package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.SchemaErrorCode;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldSearchResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DynamicFieldExtnDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;

@Service
public class DynamicFieldServiceImpl implements DynamicFieldService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicFieldServiceImpl.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private DynamicFieldRepository dynamicFieldRepository;
	
	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;
	
	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@Autowired
	private FilterTypeValidator filterTypeValidator;

	@Autowired
	private PageUtils pageUtils;


	@Autowired
	AuditUtil auditUtil;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DynamicFieldService#getAllDynamicField()
	 */
	@Cacheable(value = "dynamic-field", key = "'dynamicfield'.concat('-').concat(#pageNumber).concat('-').concat(#pageSize).concat('-').concat(#sortBy).concat('-').concat(#orderBy).concat('-').concat(#langCode)")
	@Override
	public PageDto<DynamicFieldExtnDto> getAllDynamicField(int pageNumber, int pageSize, String sortBy, String orderBy, String langCode,
															   LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		Page<Object[]> pagedResult = null;

		if (lastUpdated == null) {
			lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
		}
		try {
			
			PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy));			
			pagedResult = langCode == null ? dynamicFieldRepository.findAllLatestDynamicFieldNames(lastUpdated, currentTimestamp, pageRequest) :
				dynamicFieldRepository.findAllLatestDynamicFieldNamesByLangCode(langCode,lastUpdated, currentTimestamp, pageRequest);
			
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorCode(),
					SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}

		List<DynamicFieldExtnDto> list = new ArrayList<>();
		PageDto<DynamicFieldExtnDto> pagedFields = new PageDto<>(pageNumber, 0, 0, list);

		if(pagedResult != null && pagedResult.getContent() != null) {
			pagedResult.getContent().forEach( result -> {

				String fieldName = (String) result[0];
				String lang = (String) result[1];

				List<DynamicField> fields = dynamicFieldRepository.findAllDynamicFieldByName(fieldName);

				if(fields != null && !fields.isEmpty()) {
					Map<String, List<DynamicField>> groupedValues = fields
							.stream()
							.collect(Collectors.groupingBy(DynamicField::getLangCode));

					list.add(getDynamicFieldDto(groupedValues.get(lang)));
				}
			});
			
			pagedFields.setPageNo(pagedResult.getNumber());
			pagedFields.setTotalPages(pagedResult.getTotalPages());
			pagedFields.setTotalItems(pagedResult.getTotalElements());
		}		
		return pagedFields;
	}

	@Cacheable(value = "dynamic-field", key = "dynamicfield")
	@Override
	public List<String> getDistinctDynamicFields() {
		List<String> distinctDynamicField = new ArrayList<String>();
		try {
			distinctDynamicField = dynamicFieldRepository.getDistinctDynamicFields();

		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return distinctDynamicField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DynamicFieldService#createDynamicField()
	 */
	@Override
	@Transactional
	public DynamicFieldResponseDto createDynamicField(DynamicFieldDto dto) {
		DynamicField entity = MetaDataUtils.setCreateMetaData(dto, DynamicField.class);
		entity.setId(UUID.randomUUID().toString());

		if(dto.getFieldVal() != null)
			entity.setValueJson(dto.getFieldVal().toString());

		try {
			entity = dynamicFieldRepository.create(entity);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_INSERT_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return getDynamicFieldDto(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.DynamicFieldService#updateDynamicField()
	 */
	@Override
	@Transactional
	public DynamicFieldResponseDto updateDynamicField(String id, DynamicFieldPutDto dto) {
		DynamicField entity = null;
		try {
			int updatedRows = dynamicFieldRepository.updateDynamicField(id, dto.getDescription(), dto.getLangCode(), 
					dto.getDataType(), MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser(),
					dto.getFieldVal() != null ? dto.getFieldVal().toString() : "{}");
			
			if (updatedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}

			entity = dynamicFieldRepository.findDynamicFieldById(id);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_UPDATE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return getDynamicFieldDto(entity);
	}

	@Override
	@Transactional
	public StatusResponseDto deleteDynamicFieldValue(String id) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();
		try {
			int deletedRows = dynamicFieldRepository.deleteDynamicField(id, MetaDataUtils.getCurrentDateTime(),
					MetaDataUtils.getContextUser());

			if (deletedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
			statusResponseDto.setStatus("DynamicField deleted successfully");
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_DELETE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return statusResponseDto;
	}


	@Override
	@Transactional
	public StatusResponseDto deleteDynamicField(String fieldName) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();
		try {
			int deletedRows = dynamicFieldRepository.deleteAllDynamicField(fieldName,
					MetaDataUtils.getCurrentDateTime(),
					MetaDataUtils.getContextUser());

			if (deletedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
			statusResponseDto.setStatus("DynamicField deleted successfully");
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_DELETE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return statusResponseDto;
	}

	@SuppressWarnings("null")
	@Override
	public PageResponseDto<DynamicFieldSearchResponseDto> searchDynamicFields(SearchDto dto) {
		PageResponseDto<DynamicFieldSearchResponseDto> pageDto = new PageResponseDto<>();
		List<DynamicFieldSearchResponseDto> dynamicFieldExtnDtos = new ArrayList<DynamicFieldSearchResponseDto>();
		Page<DynamicField> page = masterdataSearchHelper.searchMasterdata(DynamicField.class, dto, null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			for (DynamicField dynamicDto : page.getContent()) {
				DynamicFieldSearchResponseDto dynamicFieldDto = getDynamicFieldSearchResponseDto(dynamicDto);

				dynamicFieldExtnDtos.add(dynamicFieldDto);
			}
			pageDto = PageUtils.pageResponse(page);
			pageDto.setData(dynamicFieldExtnDtos);
		}
		return pageDto;
	}

	@Override
	@Transactional
	public StatusResponseDto updateDynamicFieldStatus(String fieldName, boolean isActive) {
		StatusResponseDto response = new StatusResponseDto();
		try {
			int updatedRows = dynamicFieldRepository.updateAllDynamicFieldIsActive(fieldName, isActive,
					MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser());

			if (updatedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_UPDATE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		response.setStatus("Status updated successfully for Dynamic Fields");
		return response;
	}

	@Override
	@Transactional
	public StatusResponseDto updateDynamicFieldValueStatus(String id, boolean isActive) {
		StatusResponseDto response = new StatusResponseDto();
		try {
			int updatedRows = dynamicFieldRepository.updateDynamicFieldIsActive(id, isActive,
					MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser());

			if (updatedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_UPDATE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		response.setStatus("Status updated successfully for Dynamic Fields");
		return response;
	}


	private DynamicFieldResponseDto getDynamicFieldDto(DynamicField entity) {
		DynamicFieldResponseDto dto = new DynamicFieldResponseDto();
		dto.setIsActive(entity.getIsActive());
		dto.setDataType(entity.getDataType());
		dto.setDescription(entity.getDescription());
		dto.setId(entity.getId());
		dto.setLangCode(entity.getLangCode());
		dto.setName(entity.getName());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedOn(entity.getCreatedDateTime());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedOn(entity.getUpdatedDateTime());
		try {
			dto.setFieldVal(entity.getValueJson() != null ? objectMapper.readTree(entity.getValueJson()) : null);
		} catch (IOException e) {
			LOGGER.error("Failed to parse field value json object : ", e);
		}
		return dto;
	}

	private DynamicFieldSearchResponseDto getDynamicFieldSearchResponseDto(DynamicField entity) {
		DynamicFieldSearchResponseDto dto = new DynamicFieldSearchResponseDto();
		dto.setIsActive(entity.getIsActive());
		dto.setDataType(entity.getDataType());
		dto.setDescription(entity.getDescription());
		dto.setId(entity.getId());
		dto.setLangCode(entity.getLangCode());
		dto.setName(entity.getName());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedDateTime(entity.getCreatedDateTime());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedDateTime(entity.getUpdatedDateTime());
		try {
			dto.setFieldVal(objectMapper.readTree(entity.getValueJson()));
		} catch (IOException e) {
		}
		return dto;
	}
	private DynamicFieldExtnDto getDynamicFieldDto(List<DynamicField> dynamicFields) {
		dynamicFields = dynamicFields
				.stream()
				.sorted((o1, o2) -> o1.getCreatedDateTime().compareTo(o2.getCreatedDateTime()))
				.collect(Collectors.toList());

		List<JsonNode> jsonArray = new ArrayList<>();
		dynamicFields.forEach(dynamicField -> {
			try {
				if(dynamicField.getValueJson() != null && dynamicField.getIsActive()) {
					jsonArray.add(objectMapper.readTree(dynamicField.getValueJson()));
				}
			} catch (IOException e) { }
		});

		DynamicFieldExtnDto dto = new DynamicFieldExtnDto();
		dto.setIsActive(dynamicFields.stream().anyMatch(field -> field.getIsActive()));
		dto.setDataType(dynamicFields.get(0).getDataType());
		dto.setDescription(dynamicFields.get(0).getDescription());
		dto.setId(dynamicFields.get(0).getId());
		dto.setLangCode(dynamicFields.get(0).getLangCode());
		dto.setName(dynamicFields.get(0).getName());
		dto.setCreatedBy(dynamicFields.get(0).getCreatedBy());
		dto.setCreatedOn(dynamicFields.get(0).getCreatedDateTime());
		dto.setUpdatedBy(dynamicFields.get(0).getUpdatedBy());
		dto.setUpdatedOn(dynamicFields.get(0).getUpdatedDateTime());
		dto.setFieldVal(jsonArray);
		return dto;
	}
}
