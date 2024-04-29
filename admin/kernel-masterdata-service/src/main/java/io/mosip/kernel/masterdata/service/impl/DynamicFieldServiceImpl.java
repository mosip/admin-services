package io.mosip.kernel.masterdata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.SchemaErrorCode;
import io.mosip.kernel.masterdata.dto.DynamicFieldCodeValueDTO;
import io.mosip.kernel.masterdata.dto.DynamicFieldConsolidateResponseDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldDefDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldNameDto;
import io.mosip.kernel.masterdata.dto.DynamicFieldPutDto;
import io.mosip.kernel.masterdata.dto.FilterData;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.DynamicFieldSearchResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.DynamicFieldExtnDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.ColumnCodeValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicFieldServiceImpl implements DynamicFieldService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicFieldServiceImpl.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

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
	private FilterColumnValidator filterColumnValidator;

	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	AuditUtil auditUtil;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.mosip.kernel.masterdata.service.DynamicFieldService#getAllDynamicField()
	 */

	@Cacheable(value = "dynamic-field", key = "'dynamicfield'.concat('-').concat(#pageNumber).concat('-').concat(#pageSize).concat('-').concat(#sortBy).concat('-').concat(#orderBy).concat('-').concat(#langCode)",
			condition="#langCode != null")
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

	@Cacheable(value = "dynamic-field", key = "'dynamicfield'")
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

	@Cacheable(value = "dynamic-field", key = "'dynamicfield'.concat(#langCode)")
	@Override
	public List<DynamicFieldDefDto> getDistinctDynamicFields(String langCode) {
		List<DynamicFieldDefDto> dynamicFields = new ArrayList<DynamicFieldDefDto>();
		try {
			List<DynamicFieldNameDto> fields = dynamicFieldRepository.getDistinctDynamicFieldsWithDescription();

			Map<String, List<DynamicFieldNameDto>> groupedData = fields
					.stream()
					.collect(Collectors.groupingBy(DynamicFieldNameDto::getName));

			groupedData.keySet().forEach(k -> {
				DynamicFieldDefDto dynamicFieldDefDto = new DynamicFieldDefDto();
				dynamicFieldDefDto.setName(k);

				List<DynamicFieldNameDto> list = groupedData.getOrDefault(k, Collections.EMPTY_LIST);
				Optional<DynamicFieldNameDto> result = list.stream().filter( d -> langCode.equals(d.getLangCode()) ).findFirst();
				if(result.isPresent()) {
					dynamicFieldDefDto.setDescription(result.get().getDescription());
					dynamicFieldDefDto.setIsActive(result.get().getIsActive());
				}
				dynamicFields.add(dynamicFieldDefDto);
			});

		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return dynamicFields;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * io.mosip.kernel.masterdata.service.DynamicFieldService#createDynamicField()
	 */
	@CacheEvict(value = "dynamic-field", allEntries = true)
	@Override
	@Transactional
	public DynamicFieldResponseDto createDynamicField(DynamicFieldDto dto) {
		DynamicField entity = MetaDataUtils.setCreateMetaData(dto, DynamicField.class);

		try {
			List<DynamicField> existingFields = dynamicFieldRepository.findAllByFieldNameAndCode(dto.getName(),
					"%"+dto.getFieldVal().get("code").toString()+ "%");
			if(existingFields != null && !existingFields.isEmpty()) {
				Optional<DynamicField> result = existingFields.stream()
						.filter( f-> f.getLangCode().equalsIgnoreCase(dto.getLangCode()) )
						.findFirst();

				if(result.isPresent())
					throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_ALREADY_EXISTS.getErrorCode(),
							SchemaErrorCode.DYNAMIC_FIELD_ALREADY_EXISTS.getErrorMessage());

				entity.setIsActive(existingFields.get(0).getIsActive());
			}

			entity.setId(UUID.randomUUID().toString());
			entity.setValueJson(getValidatedFieldValue(dto.getFieldVal()));
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
	@CacheEvict(value = "dynamic-field", allEntries = true)
	@Override
	@Transactional
	public DynamicFieldResponseDto updateDynamicField(String id, DynamicFieldPutDto dto) {
		DynamicField entity = null;
		try {

			String valueJson = getValidatedFieldValue(dto.getFieldVal());

			int updatedRows = dynamicFieldRepository.updateDynamicField(id, dto.getDescription(), dto.getLangCode(),
					dto.getDataType(), MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser(),
					valueJson);

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

	@CacheEvict(value = "dynamic-field", allEntries = true)
	@Override
	@Transactional
	public StatusResponseDto deleteDynamicFieldValue(String id) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();
		try {
			DynamicField dynamicField = dynamicFieldRepository.findDynamicFieldById(id);
			if(dynamicField == null)
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			if(dynamicField.getValueJson()==null)
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_VALUE_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_VALUE_NOT_FOUND_EXCEPTION.getErrorMessage());
			JsonNode valueJson =objectMapper.readTree(dynamicField.getValueJson());
			String code = valueJson.get("code").toString();



			int deletedRows = dynamicFieldRepository.deleteDynamicField(dynamicField.getName(), "%"+code+"%",
					MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser());

			if (deletedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
			statusResponseDto.setStatus("DynamicField deleted successfully");
		} catch (DataAccessLayerException | DataAccessException  | IOException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_DELETE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return statusResponseDto;
	}

	@CacheEvict(value = "dynamic-field", allEntries = true)
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

	@CacheEvict(value = "dynamic-field", allEntries = true)
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

	@CacheEvict(value = "dynamic-field", allEntries = true)
	@Override
	@Transactional
	public StatusResponseDto updateDynamicFieldValueStatus(String id, boolean isActive) {
		StatusResponseDto response = new StatusResponseDto();
		try {
			DynamicField dynamicField = dynamicFieldRepository.findDynamicFieldById(id);
			if(dynamicField == null)
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			if(null==dynamicField.getValueJson())
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_VALUE_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_VALUE_NOT_FOUND_EXCEPTION.getErrorMessage());
			JsonNode valueJson = objectMapper.readTree(dynamicField.getValueJson());
			String code = valueJson.get("code").toString();

			int updatedRows = dynamicFieldRepository.updateDynamicFieldIsActive(dynamicField.getName(), "%"+code+"%", isActive,
					MetaDataUtils.getCurrentDateTime(), MetaDataUtils.getContextUser());

			if (updatedRows < 1) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException | IOException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_UPDATE_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		response.setStatus("Status updated successfully for Dynamic Fields");
		return response;
	}

	@Override
	public FilterResponseCodeDto dynamicfieldFilterValues(FilterValueDto filterValueDto) {
		FilterResponseCodeDto filterResponseDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> columnValueList = new ArrayList<>();
		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), DynamicField.class)) {
			for (FilterDto filterDto : filterValueDto.getFilters()) {
				FilterResult<FilterData> filterResult = masterDataFilterHelper.filterValuesWithCode(
						DynamicField.class, filterDto,
						filterValueDto,"id");
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
			dto.setFieldVal(entity.getValueJson() != null ? objectMapper.readTree(entity.getValueJson()) : null);
		} catch (IOException e) {
			LOGGER.error("Failed to parse field {} value json object : {}", entity.getName(),
					entity.getValueJson());
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

	private String getValidatedFieldValue(JsonNode fieldValue) {
		try {
			JSONObject valueJson = new JSONObject(fieldValue.toString());
			Assert.hasText(valueJson.getString("code"), "'code' must not be empty");
			Assert.hasText(valueJson.getString("value"), "'value' must not be empty");
			return valueJson.toString();
		} catch (Throwable t) {
			LOGGER.error("Failed to parse field value json", t);
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_VALUE_JSON_INVALID.getErrorCode(),
					SchemaErrorCode.DYNAMIC_FIELD_VALUE_JSON_INVALID.getErrorMessage());
		}
	}

	@Cacheable(value = "dynamic-field", key = "'dynamicfield'.concat('-').concat(#fieldName).concat('-').concat(#langCode).concat('-').concat(#withValue)")
	@Override
	public DynamicFieldConsolidateResponseDto getDynamicFieldByNameAndLangcode(String fieldName, String langCode,boolean withValue) {
		try {
			List<DynamicField> lst = dynamicFieldRepository.findAllDynamicFieldByNameLangCodeAndisDeleted(fieldName,
					langCode);
			if (null == lst || lst.size() == 0) {
				throw new DataNotFoundException(SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.DYNAMIC_FIELD_NOT_FOUND_EXCEPTION.getErrorMessage());

			}
			DynamicFieldConsolidateResponseDto dto = new DynamicFieldConsolidateResponseDto();
			dto.setDescription(lst.get(0).getDescription());
			dto.setName(lst.get(0).getName());
			List<DynamicFieldCodeValueDTO> dtolist = new ArrayList<DynamicFieldCodeValueDTO>();
			if (withValue == true) {

				List<JSONObject> l = new ArrayList<>();
				for (int i = 0; i < lst.size(); i++) {
					l.add(new JSONObject(lst.get(i).getValueJson()));
					dtolist.add(objectMapper.readValue(lst.get(i).getValueJson(),DynamicFieldCodeValueDTO.class));
				}
				dto.setValues(dtolist);
			}

			return dto;

		} catch (DataAccessLayerException | DataAccessException | JSONException | JsonProcessingException  e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
	}


	@Override
	@Cacheable(value = "dynamic-field", key = "'dynamicfield'.concat('-').concat(#fieldName)")
	public List<DynamicFieldExtnDto> getAllDynamicFieldByName(String fieldName) {
		List<DynamicField> fields = null;
		try {
			fields = dynamicFieldRepository.findAllDynamicFieldByName(fieldName);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorCode(),
					SchemaErrorCode.DYNAMIC_FIELD_FETCH_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
		List<DynamicFieldExtnDto> list = new ArrayList<>();
				if(fields != null && !fields.isEmpty()) {
					Map<String, List<DynamicField>> groupedValues = fields
							.stream()
							.collect(Collectors.groupingBy(DynamicField::getLangCode));
					 list = groupedValues.keySet()
							.stream()
							.map(lang -> getDynamicFieldDto(groupedValues.get(lang)))
							.collect(Collectors.toList());

				}
		return list;
	}

}