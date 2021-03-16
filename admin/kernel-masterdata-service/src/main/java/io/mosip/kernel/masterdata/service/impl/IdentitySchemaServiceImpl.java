package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.SchemaErrorCode;
import io.mosip.kernel.masterdata.dto.IdSchemaPublishDto;
import io.mosip.kernel.masterdata.dto.IdentitySchemaDto;
import io.mosip.kernel.masterdata.dto.getresponse.IdSchemaResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.service.IdentitySchemaService;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.uispec.dto.UISpecKeyValuePair;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

@Service
public class IdentitySchemaServiceImpl implements IdentitySchemaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySchemaServiceImpl.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	JsonObject pojo = new JsonObject();

	@Autowired
	private IdentitySchemaRepository identitySchemaRepository;

	@Autowired
	private UISpecService uiSpecService;

	private String defaultDomain = "registration-client";

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.IdentitySchemaService#getAllSchema()
	 */
	@Override
	public PageDto<IdSchemaResponseDto> getAllSchema(int pageNumber, int pageSize, String sortBy, String orderBy) {
		List<IdSchemaResponseDto> list = new ArrayList<>();
		PageDto<IdSchemaResponseDto> pagedSchema = new PageDto<>(pageNumber, 0, 0, list);
		Page<IdentitySchema> pagedResult = null;

		try {
			pagedResult = identitySchemaRepository.findAllIdentitySchema(true,
					PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));

		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}

		if (pagedResult != null && pagedResult.getContent() != null) {
			pagedResult.getContent().forEach(entity -> {
				list.add(getIdentitySchemaDto(entity));
			});

			pagedSchema.setPageNo(pagedResult.getNumber());
			pagedSchema.setTotalPages(pagedResult.getTotalPages());
			pagedSchema.setTotalItems(pagedResult.getTotalElements());
		}
		return pagedSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.IdentitySchemaService#getLatestSchema()
	 */
	@Override
	public IdSchemaResponseDto getLatestSchema() {
		IdentitySchema identitySchema = null;
		try {
			identitySchema = identitySchemaRepository.findLatestPublishedIdentitySchema();
		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error while fetching latest schema", e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}

		if (identitySchema == null)
			throw new DataNotFoundException(SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());

		return getIdentitySchemaDto(identitySchema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.IdentitySchemaService#getIdentitySchema()
	 */
	@Override
	public IdSchemaResponseDto getIdentitySchema(double version) {
		IdentitySchema identitySchema = null;
		try {
			identitySchema = identitySchemaRepository.findPublishedIdentitySchema(version);
		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error while fetching schema ver: " + version, e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}

		if (identitySchema == null)
			throw new DataNotFoundException(SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());

		return getIdentitySchemaDto(identitySchema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.IdentitySchemaService#createSchema()
	 */
	@Override
	@Transactional
	public IdSchemaResponseDto createSchema(IdentitySchemaDto dto) {
		isJSONValid(dto.getSchema());
		IdentitySchema entity = MetaDataUtils.setCreateMetaData(dto, IdentitySchema.class);

		entity.setIsActive(false);
		entity.setStatus(STATUS_DRAFT);
		entity.setIdVersion(0);
		entity.setSchemaJson(dto.getSchema());
		entity.setId(UUID.randomUUID().toString());
		entity.setLangCode("eng");
		entity.setIsDeleted(false);

		try {
			entity = identitySchemaRepository.create(entity);

		} catch (DataAccessLayerException | DataAccessException e) {
			LOGGER.error("Error while creating identity schema", e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_INSERT_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return getIdentitySchemaDto(entity);
	}

	/**
	 * 
	 * @param jsonInString
	 */
	private void isJSONValid(String jsonInString) {
		try {
			objectMapper.readTree(jsonInString);
		} catch (IOException e) {
			LOGGER.error("Given jsonSpec is not a valid json object ", e);
			throw new MasterDataServiceException(SchemaErrorCode.VALUE_PARSE_ERROR.getErrorCode(),
					SchemaErrorCode.VALUE_PARSE_ERROR.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.IdentitySchemaService#publishSchema()
	 */
	@Override
	@Transactional
	public String publishSchema(IdSchemaPublishDto dto) {
		if (dto.getEffectiveFrom().isBefore(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())))) {
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_EFFECTIVE_FROM_IS_OLDER.getErrorCode(),
					SchemaErrorCode.SCHEMA_EFFECTIVE_FROM_IS_OLDER.getErrorMessage());
		}

		IdentitySchema identitySchema = identitySchemaRepository.findIdentitySchemaById(dto.getId());
		if (identitySchema == null) {
			throw new RequestException(SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		if (STATUS_PUBLISHED.equalsIgnoreCase(identitySchema.getStatus())) {
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorCode(),
					SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorMessage());
		}
		IdentitySchema publishedSchema = identitySchemaRepository.findLatestPublishedIdentitySchema();
		double currentVersion = publishedSchema == null ? 0.1 : (publishedSchema.getIdVersion() + 0.1);

		identitySchema.setIsActive(true);
		identitySchema.setEffectiveFrom(dto.getEffectiveFrom());
		identitySchema.setStatus(STATUS_PUBLISHED);
		identitySchema.setUpdatedBy(MetaDataUtils.getContextUser());
		identitySchema.setUpdatedDateTime(MetaDataUtils.getCurrentDateTime());
		identitySchema.setIdVersion(currentVersion);
		
		try {

		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error while publishing identity schema : " + dto.getId(), e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorMessage());

		}
		return dto.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.IdentitySchemaService#updateSchema()
	 */
	@Override
	@Transactional
	public IdSchemaResponseDto updateSchema(String id, IdentitySchemaDto dto) {
		IdentitySchema entity = identitySchemaRepository.findIdentitySchemaById(id);
		if (entity == null) {
			throw new RequestException(SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());
		}

		if (STATUS_PUBLISHED.equalsIgnoreCase(entity.getStatus())) {
			throw new RequestException(SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorCode(),
					SchemaErrorCode.SCHEMA_ALREADY_PUBLISHED.getErrorMessage());
		}

		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setSchemaJson(dto.getSchema());
		try {
			identitySchemaRepository.save(entity);
		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error while updating identity schema : " + id, e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}
		return getIdentitySchemaDto(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.IdentitySchemaService#deleteSchema()
	 */
	@Override
	@Transactional
	public String deleteSchema(String id) {
		try {
			int updatedRows = identitySchemaRepository.deleteIdentitySchema(id, MetaDataUtils.getCurrentDateTime(),
					MetaDataUtils.getContextUser());

			if (updatedRows < 1) {
				throw new RequestException(SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
						SchemaErrorCode.SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());
			}

		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error while deleting identity schema : " + id, e);
			throw new MasterDataServiceException(SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorCode(),
					SchemaErrorCode.SCHEMA_UPDATE_EXCEPTION.getErrorMessage());
		}
		return id;
	}

	@Override
	public Map<String, Object> getLatestPublishedSchema(double version, String domain, String type)
			throws JSONException {
		IdSchemaResponseDto response = null;
		List<UISpecResponseDto> uiSpecs = new ArrayList<>();
		if (domain == null || domain.isEmpty() || domain.isBlank()) {
			domain = defaultDomain;
		}
		if (version <= 0 && (type == null || type.isBlank() || type.isEmpty())) {
			response = getLatestSchema();
			if(response != null)
				uiSpecs = uiSpecService.getLatestUISpec(response.getId(),domain);
		}

		if (version <= 0 && !(type == null || type.isBlank() || type.isEmpty())) {
			response = getLatestSchema();
			if(response != null)
				uiSpecs = uiSpecService.getUISpec(response.getId(),domain, type);
		}

		if (version > 0 && (type == null || type.isBlank() || type.isEmpty())) {
			response = getIdentitySchema(version);
			if(response != null)	
				uiSpecs = uiSpecService.getLatestUISpec(response.getId(),domain);
		}

		if (version > 0 && !(type == null || type.isBlank() || type.isEmpty())) {
			response = getIdentitySchema(version);
			if(response != null)
				uiSpecs = uiSpecService.getUISpec(response.getId(),domain, type);
		}

		return populateResponse(response, uiSpecs);
	}

	private Map<String, Object> populateResponse(IdSchemaResponseDto dto, List<UISpecResponseDto> uiSpecs)
			throws JSONException {		
		Map<String, Object> jsonData = new HashMap<>();
		jsonData.put("id", dto.getId());
		jsonData.put("idVersion",dto.getIdVersion());
		jsonData.put("title", dto.getTitle());
		jsonData.put("description", dto.getDescription());
		jsonData.put("schemaJson", dto.getSchemaJson());
		if (!uiSpecs.isEmpty()) {
			for (UISpecResponseDto uiSpec : uiSpecs) {
				for (UISpecKeyValuePair keyValue : uiSpec.getJsonSpec()) {
					jsonData.put(keyValue.getType(), keyValue.getSpec());
				}
			}
		}		
		jsonData.put("status", dto.getStatus());
		jsonData.put("effectiveFrom", dto.getEffectiveFrom());
		jsonData.put("createdBy", dto.getCreatedBy());
		jsonData.put("updatedBy", dto.getUpdatedBy());
		jsonData.put("createdOn", dto.getCreatedOn());
		jsonData.put("updatedOn", dto.getUpdatedOn());
		return jsonData;
	}

	private IdSchemaResponseDto getIdentitySchemaDto(IdentitySchema entity) {
		IdSchemaResponseDto dto = new IdSchemaResponseDto();
		dto.setId(entity.getId());
		dto.setIdVersion(entity.getIdVersion());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setSchemaJson(entity.getSchemaJson());
		dto.setStatus(entity.getStatus());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedOn(entity.getCreatedDateTime());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedOn(entity.getUpdatedDateTime());
		dto.setEffectiveFrom(entity.getEffectiveFrom());
		return dto;
	}
}
