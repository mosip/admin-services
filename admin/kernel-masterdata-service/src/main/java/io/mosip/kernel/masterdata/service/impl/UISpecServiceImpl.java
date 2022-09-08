package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.UISpecErrorCode;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.entity.UISpec;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.repository.UISpecRepository;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecKeyValuePair;
import io.mosip.kernel.masterdata.uispec.dto.UISpecPublishDto;
import io.mosip.kernel.masterdata.uispec.dto.UISpecResponseDto;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

/**
 * 
 * @author Nagarjuna
 *
 */

@Service
@Transactional
public class UISpecServiceImpl implements UISpecService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UISpecServiceImpl.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UISpecRepository uiSpecRepository;

	@Autowired
	private IdentitySchemaRepository identitySchemaRepository;

	/**
	 * Method to bring latest ui spec for given domain
	 */
	@Override
	public List<UISpecResponseDto> getLatestUISpec(String domain) {
		List<UISpec> typesByDomain = uiSpecRepository.findTypesByDomain(domain);
		if (typesByDomain.isEmpty()) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return prepareResponse(typesByDomain);
	}

	/**
	 * 
	 * @param specs
	 * @return
	 */
	private List<UISpecResponseDto> prepareResponse(List<UISpec> specs) {
		List<UISpecResponseDto> response = new ArrayList<>();
		for (UISpec spec : specs) {
			response.add(prepareResponse(spec));
		}
		return response;
	}

	/**
	 * Gets ui spec for given version and domain
	 */
	@Override
	public List<UISpecResponseDto> getUISpec(double version, String domain) {
		List<UISpec> uiSpecsFromDb = uiSpecRepository.findPublishedUISpec(version, domain);
		if (uiSpecsFromDb == null || uiSpecsFromDb.isEmpty()) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return prepareResponse(uiSpecsFromDb);
	}

	/**
	 * Gets all ui specs
	 */
	@Override
	public PageDto<UISpecResponseDto> getAllUISpecs(int pageNumber, int pageSize, String sortBy, String orderBy) {
		List<UISpecResponseDto> response = new ArrayList<>();
		PageDto<UISpecResponseDto> results = new PageDto<>(pageNumber, 0, 0, response);
		Page<UISpec> pagedResult = null;
		try {
			pagedResult = uiSpecRepository.findAllUISpecs(true,
					PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));

		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error occured while getting all ui specs ", e);
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorMessage());
		}
		if (pagedResult != null && pagedResult.getContent() != null) {
			pagedResult.getContent().forEach(entity -> {
				response.add(prepareResponse(entity));
			});
			results.setPageNo(pagedResult.getNumber());
			results.setTotalPages(pagedResult.getTotalPages());
			results.setTotalItems(pagedResult.getTotalElements());
		}
		return results;
	}

	/**
	 * Creates ui spec
	 */
	@CacheEvict(value = "ui-spec", allEntries = true)
	@Override
	public UISpecResponseDto defineUISpec(UISpecDto dto) {		
		IdentitySchema identitySchema = validateIdentityShema(dto.getIdentitySchemaId());
		isJSONValid(dto.getJsonspec() == null ? "[]" : dto.getJsonspec().toString());
		UISpec uiSpecEntity = MetaDataUtils.setCreateMetaData(dto, UISpec.class);

		uiSpecEntity.setIsActive(false);
		uiSpecEntity.setIdentitySchemaId(dto.getIdentitySchemaId());
		uiSpecEntity.setIdSchemaVersion(identitySchema.getIdVersion());
		uiSpecEntity.setStatus(STATUS_DRAFT);
		uiSpecEntity.setVersion(0);
		uiSpecEntity.setJsonSpec(dto.getJsonspec() == null ? "[]" : dto.getJsonspec().toString());
		uiSpecEntity.setId(UUID.randomUUID().toString());
		uiSpecEntity.setIsDeleted(false);
		uiSpecEntity.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
		List<UISpec> uispeclist = uiSpecRepository.findUISpecByDomainTypeandIdentitySchemaId(dto.getDomain(), dto.getType(), dto.getIdentitySchemaId());
		if(!uispeclist.isEmpty() || uispeclist.size() !=0) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_DUPLICATE_ENTRY.getErrorCode(),
					UISpecErrorCode.UI_SPEC_DUPLICATE_ENTRY.getErrorMessage());
		}
		try {
			uiSpecRepository.save(uiSpecEntity);
		} catch (DataAccessLayerException | DataAccessException e) {
			LOGGER.error("Error occured while inserting the ui spec ", e);
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_INSERT_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_INSERT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		} 
		return prepareResponse(uiSpecEntity);
	}

	/**
	 *  Validates string is valid json or not
	 * @param jsonInString
	 */
	private void isJSONValid(String jsonInString) {
		try {
			objectMapper.readTree(jsonInString);
		} catch (IOException e) {
			LOGGER.error("Given jsonSpec is not a valid json object ", e);
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorMessage());
		}
	}

	/**
	 * 
	 * @param id
	 */
	private IdentitySchema validateIdentityShema(String id) {
		IdentitySchema schema = identitySchemaRepository.findPublishedIdentitySchema(id);
		if (schema == null) {
			throw new DataNotFoundException(UISpecErrorCode.IDENTITY_SPEC_NOT_FOUND_ERROR.getErrorCode(),
					UISpecErrorCode.IDENTITY_SPEC_NOT_FOUND_ERROR.getErrorMessage());
		}
		return schema;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	private UISpecResponseDto prepareResponse(UISpec entity) {
		UISpecResponseDto response = new UISpecResponseDto();
		List<UISpecKeyValuePair> spec = new ArrayList<UISpecKeyValuePair>();
		response.setId(entity.getId());
		response.setVersion(entity.getVersion());
		response.setDomain(entity.getDomain());
		response.setTitle(entity.getTitle());
		response.setDescription(entity.getDescription());
		response.setIdentitySchemaId(entity.getIdentitySchemaId());
		response.setStatus(entity.getStatus());
		response.setCreatedBy(entity.getCreatedBy());
		response.setCreatedOn(entity.getCreatedDateTime());
		response.setUpdatedBy(entity.getUpdatedBy());
		response.setUpdatedOn(entity.getUpdatedDateTime());
		response.setEffectiveFrom(entity.getEffectiveFrom());
		response.setIdSchemaVersion(entity.getIdSchemaVersion());
		spec.add(new UISpecKeyValuePair(entity.getType(), getValidJson(entity.getJsonSpec())));
		response.setJsonSpec(spec);
		return response;
	}

	/**
	 * 
	 */
	@CacheEvict(value = "ui-spec", allEntries = true)
	@Override
	public UISpecResponseDto updateUISpec(String id, UISpecDto dto) {
		IdentitySchema identitySchema = validateIdentityShema(dto.getIdentitySchemaId());
		isJSONValid(dto.getJsonspec() == null ? "[]" : dto.getJsonspec().toString());
		UISpec uiSpecObjectFromDb = getUISpecById(id);
		if (STATUS_PUBLISHED.equalsIgnoreCase(uiSpecObjectFromDb.getStatus())) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(),
					UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorMessage());
		}
		uiSpecObjectFromDb.setDomain(dto.getDomain());
		uiSpecObjectFromDb.setTitle(dto.getTitle());
		uiSpecObjectFromDb.setDescription(dto.getDescription());
		uiSpecObjectFromDb.setIdentitySchemaId(dto.getIdentitySchemaId());
		uiSpecObjectFromDb.setIdSchemaVersion(identitySchema.getIdVersion());
		uiSpecObjectFromDb.setJsonSpec(dto.getJsonspec().toString());
		uiSpecObjectFromDb.setUpdatedBy(MetaDataUtils.getContextUser());
		uiSpecObjectFromDb.setUpdatedDateTime(MetaDataUtils.getCurrentDateTime());
		uiSpecObjectFromDb.setType(dto.getType());
		uiSpecRepository.save(uiSpecObjectFromDb);
		return prepareResponse(uiSpecObjectFromDb);
	}

	/**
	 * 
	 */
	@CacheEvict(value = "ui-spec", allEntries = true)
	@Override
	public String publishUISpec(UISpecPublishDto dto) {
		if (dto.getEffectiveFrom().isBefore(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())))) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_EFFECTIVE_FROM_IS_OLDER.getErrorCode(),
					UISpecErrorCode.UI_SPEC_EFFECTIVE_FROM_IS_OLDER.getErrorMessage());
		}
		UISpec uiSpecObjectFromDb = getUISpecById(dto.getId());
		if (STATUS_PUBLISHED.equalsIgnoreCase(uiSpecObjectFromDb.getStatus())) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(),
					UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorMessage());
		}
		UISpec latestPublishedUISpec = uiSpecRepository.findLatestVersion(uiSpecObjectFromDb.getDomain(),
				uiSpecObjectFromDb.getType());
		double currentVersion = latestPublishedUISpec == null ? 0.1 : (latestPublishedUISpec.getVersion() + 0.1);

		uiSpecObjectFromDb.setVersion(currentVersion);
		uiSpecObjectFromDb.setStatus(STATUS_PUBLISHED);
		uiSpecObjectFromDb.setIsActive(true);
		uiSpecObjectFromDb.setUpdatedBy(MetaDataUtils.getContextUser());
		uiSpecObjectFromDb.setUpdatedDateTime(MetaDataUtils.getCurrentDateTime());
		uiSpecRepository.save(uiSpecObjectFromDb);
		return uiSpecObjectFromDb.getId();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private UISpec getUISpecById(String id) {
		UISpec uiSpecObjectFromDb = uiSpecRepository.findUISpecById(id);
		if (uiSpecObjectFromDb == null) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());

		}
		return uiSpecObjectFromDb;
	}

	/**
	 * 
	 */
	@CacheEvict(value = "ui-spec", allEntries = true)
	@Override
	public String deleteUISpec(String id) {
		try {
			int effectedRows = uiSpecRepository.deleteUISpec(id, MetaDataUtils.getCurrentDateTime(),
					MetaDataUtils.getContextUser());
			if (effectedRows < 1) {
				throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
						UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException e) {
			LOGGER.error("Error occured while deleting the ui spec ", e);
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorMessage());

		}
		return id;
	}	

	/**
	 * 
	 */
	@Override
	public List<UISpecResponseDto> getUISpec(double version, String domain, String type) {
		List<UISpec> specsFromDb = new ArrayList<UISpec>();
		List<String> types = validateAndGetTypes(type);
		specsFromDb = uiSpecRepository.findPublishedUISpec(version, domain, types);
		if (specsFromDb.isEmpty()) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return prepareResponse(specsFromDb);
	}

	/**
	 * 
	 */
	@Override
	public List<UISpecResponseDto> getUISpec(String domain, String type) {
		List<String> types = validateAndGetTypes(type);
		List<UISpec> specsFromDb = uiSpecRepository.findLatestPublishedUISpec(domain, types);
		if (specsFromDb.isEmpty()) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}

		return prepareResponse(specsFromDb);
	}

	/**
	 * 
	 * @param typeInString
	 * @return
	 */
	private List<String> validateAndGetTypes(String typeInString) {
		String[] arrayOfTypes = typeInString.split(",");
		List<String> types = new ArrayList<String>();
		for (String type : arrayOfTypes) {
			types.add(type);
		}
		return types;
	}

	/**
	 * 
	 */
	@Override
	public List<UISpecResponseDto> getUISpec(String identitySchemaId, String domain, String type) {
		List<String> types = validateAndGetTypes(type);
		return prepareResponse(
				uiSpecRepository.findLatestPublishedUISpecByIdentitySchema(identitySchemaId, domain, types));
	}

	/**
	 * 
	 */
	@Override
	public List<UISpecResponseDto> getLatestUISpec(String identitySchemaId, String domain) {
		return prepareResponse(uiSpecRepository.findTypesByDomainAndSchema(domain, identitySchemaId));
	}

	/**
	 * Gets latest published ui spec
	 */

	@Cacheable(value = "ui-spec", key = "'uispec'.concat('-').concat(#domain).concat('-').concat(#version).concat('-').concat(#type).concat('-').concat(#identitySchemaVersion)",
			condition = "#domain != null && #type != null")
	@Override
	public List<UISpecResponseDto> getLatestPublishedUISpec(String domain, double version, String type,
			double identitySchemaVersion) {		
		
		//latest ui spec and type is not present
		if (version <= 0 && (type == null || type.isBlank() || type.isEmpty())) {
			return filterByIdentitySchemaVersion(getLatestUISpec(domain), identitySchemaVersion);
		}

		//with ui spec version and domain and type is null
		if (version >= 0 && (type == null || type.isBlank() || type.isEmpty())) {
			return filterByIdentitySchemaVersion(getUISpec(version, domain), identitySchemaVersion);
		}

		//with latest ui spec and type
		if (version <= 0 && !type.isBlank()) {
			return filterByIdentitySchemaVersion(getUISpec(domain, type), identitySchemaVersion);
		}
		return filterByIdentitySchemaVersion(getUISpec(version, domain, type), identitySchemaVersion);
	}

	/**
	 * 
	 * @param response
	 * @param identitySchemaVersion
	 * @return
	 */
	private List<UISpecResponseDto> filterByIdentitySchemaVersion(List<UISpecResponseDto> response,
			double identitySchemaVersion) {
		if (identitySchemaVersion > 0) {
			return response.stream().filter(spec -> spec.getIdSchemaVersion() == identitySchemaVersion)
					.collect(Collectors.toList());
		}
		return response;
	}

	/**
	 *  Validates string is valid json or not and returns the JsonNode
	 * @param jsonInString
	 */
	private JsonNode getValidJson(String jsonInString) {
		try {
			return objectMapper.readTree(jsonInString);
		} catch (IOException e) {
			LOGGER.error("Given jsonSpec is not a valid json object ", e);
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorMessage());
		}
	}
}