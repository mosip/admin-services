package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.UISpecErrorCode;
import io.mosip.kernel.masterdata.dto.SchemaDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.entity.IdentitySchema;
import io.mosip.kernel.masterdata.entity.UISpec;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.IdentitySchemaRepository;
import io.mosip.kernel.masterdata.repository.UISpecRepository;
import io.mosip.kernel.masterdata.service.UISpecService;
import io.mosip.kernel.masterdata.uispec.dto.UISpecDto;
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

	private static final String DOCUMENT_TYPE = "documentType";
	private static final String BIOMETRICS_TYPE = "biometricsType";
	private static final String NONE = "none";

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UISpecRepository uiSpecRepository;

	@Autowired
	private IdentitySchemaRepository identitySchemaRepository;

	/**
	 * 
	 */
	@Override
	public UISpecResponseDto getLatestUISpec(String domain) {
		UISpec uiSpecObjectFromDb = null;
		try {
			uiSpecObjectFromDb = uiSpecRepository.findLatestPublishedUISpec(domain);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}
		if (uiSpecObjectFromDb == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return prepareResponse(uiSpecObjectFromDb);
	}

	/**
	 * 
	 */
	@Override
	public UISpecResponseDto getUISpec(double version, String domain) {
		UISpec uiSpecFromDb = uiSpecRepository.findPublishedUISpec(version, domain);
		if (uiSpecFromDb == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		return prepareResponse(uiSpecFromDb);
	}

	/**
	 * 
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
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
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
	 * 
	 */
	@Override
	public UISpecResponseDto defineUISpec(UISpecDto dto) {
		validateIdentityShema(dto.getIdentityschemaid());
		validateDuplicateFields(dto.getJsonspec());
		validateDocumentFields(dto.getJsonspec());
		validateBiometricFields(dto.getJsonspec());

		UISpec uiSpecEntity = MetaDataUtils.setCreateMetaData(dto, UISpec.class);

		uiSpecEntity.setIsActive(false);		
		uiSpecEntity.setStatus(STATUS_DRAFT);
		uiSpecEntity.setVersion(0);
		uiSpecEntity.setJsonSpec(getIdAttributeJsonString(dto.getJsonspec()));
		uiSpecEntity.setId(UUID.randomUUID().toString());
		uiSpecEntity.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
		try {
			uiSpecRepository.save(uiSpecEntity);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_INSERT_EXCEPTION.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return prepareResponse(uiSpecEntity);
	}

	private void validateIdentityShema(String id) {
		IdentitySchema schema = identitySchemaRepository.findPublishedIdentitySchema(id);
		if (schema == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_SCHEMA_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_SCHEMA_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	private UISpecResponseDto prepareResponse(UISpec entity) {
		UISpecResponseDto response = new UISpecResponseDto();

		response.setId(entity.getId());
		response.setVersion(entity.getVersion());
		response.setDomain(entity.getDomain());
		response.setTitle(entity.getTitle());
		response.setDescription(entity.getDescription());
		response.setIdentitySchemaId(entity.getIdentitySchemaId());
		response.setJsonSpec(convertJSONStringToSchemaDTO(entity.getJsonSpec()));
		response.setStatus(entity.getStatus());
		response.setCreatedBy(entity.getCreatedBy());
		response.setCreatedOn(entity.getCreatedDateTime());
		response.setUpdatedBy(entity.getUpdatedBy());
		response.setUpdatedOn(entity.getUpdatedDateTime());
		response.setEffectiveFrom(entity.getEffectiveFrom());
		return response;
	}

	/**
	 * 
	 * @param jsonSpec
	 * @return
	 */
	private List<SchemaDto> convertJSONStringToSchemaDTO(String jsonSpec) {
		List<SchemaDto> listOfSchemaDto = new ArrayList<>();
		try {
			listOfSchemaDto = objectMapper.readValue(jsonSpec == null ? "[]" : jsonSpec,
					new TypeReference<List<SchemaDto>>() {
					});
		} catch (IOException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
		return listOfSchemaDto;
	}

	/**
	 * 
	 * @param jsonspec
	 * @return
	 */
	private String getIdAttributeJsonString(List<SchemaDto> jsonspec) {
		try {
			return objectMapper.writeValueAsString(jsonspec);
		} catch (IOException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					ExceptionUtils.parseException(e));
		}
	}

	/**
	 * 
	 * @param jsonspec
	 */
	private void validateBiometricFields(List<SchemaDto> jsonspec) {
		validateSubType(jsonspec, BIOMETRICS_TYPE);
		List<SchemaDto> fields = jsonspec.stream().filter(obj -> BIOMETRICS_TYPE.equalsIgnoreCase(obj.getType()))
				.collect(Collectors.toList());
		if (fields != null) {
			Map<String, List<SchemaDto>> fieldsGroupedBySubType = fields.stream()
					.collect(Collectors.groupingBy(SchemaDto::getSubType)).entrySet().stream()
					.filter(e -> e.getValue().size() > 1)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			if (fieldsGroupedBySubType != null) {
				for (Entry<String, List<SchemaDto>> entry : fieldsGroupedBySubType.entrySet()) {
					List<SchemaDto> listOfSpecs = entry.getValue();
					if (entry.getKey() != null || NONE.equalsIgnoreCase(entry.getKey())) {
						throw new MasterDataServiceException(
								UISpecErrorCode.UI_SPEC_SUB_TYPE_REQUIRED_EXCEPTION.getErrorCode(),
								String.format(UISpecErrorCode.UI_SPEC_SUB_TYPE_REQUIRED_EXCEPTION.getErrorMessage(),
										listOfSpecs.get(0).getId()));
					}
					List<String> tempBioAttributes = new ArrayList<String>();
					for (SchemaDto spec : listOfSpecs) {
						if (spec.getBioAttributes() == null) {
							throw new MasterDataServiceException(
									UISpecErrorCode.UI_SPEC_BIO_ATTRIBUTES_REQUIRED_EXCEPTION.getErrorCode(),
									String.format(
											UISpecErrorCode.UI_SPEC_BIO_ATTRIBUTES_REQUIRED_EXCEPTION.getErrorMessage(),
											spec.getId()));
						}
						tempBioAttributes.addAll(spec.getBioAttributes());
					}
					List<String> distinctBioAttributes = tempBioAttributes.stream().distinct()
							.collect(Collectors.toList());
					if (tempBioAttributes.size() > distinctBioAttributes.size()) {
						throw new MasterDataServiceException(
								UISpecErrorCode.UI_SPEC_BIO_ATTRIBUTES_DUPLICATED_EXCEPTION.getErrorCode(),
								String.format(
										UISpecErrorCode.UI_SPEC_BIO_ATTRIBUTES_DUPLICATED_EXCEPTION.getErrorMessage(),
										listOfSpecs.get(0).getId()));
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param jsonspec
	 */
	private void validateDocumentFields(List<SchemaDto> jsonspec) {
		validateSubType(jsonspec, DOCUMENT_TYPE);
	}

	/**
	 * 
	 * @param jsonspec
	 * @param documentType
	 */
	private void validateSubType(List<SchemaDto> jsonspec, String documentType) {
		List<SchemaDto> fields = jsonspec.stream().filter(obj -> documentType.equalsIgnoreCase(obj.getType()))
				.collect(Collectors.toList());

		for (SchemaDto dto : fields) {
			if ("none".equalsIgnoreCase(dto.getSubType()))
				throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_SUB_TYPE_REQUIRED_EXCEPTION.getErrorCode(),
						String.format(UISpecErrorCode.UI_SPEC_SUB_TYPE_REQUIRED_EXCEPTION.getErrorMessage(),
								dto.getId()));
		}
	}

	/**
	 * 
	 * @param jsonspec
	 */
	private void validateDuplicateFields(List<SchemaDto> jsonspec) {
		List<String> duplicates = jsonspec.stream().collect(Collectors.groupingBy(SchemaDto::caseIgnoredId)).entrySet()
				.stream().filter(e -> e.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
		if (duplicates != null && duplicates.size() > 0) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_DUPLICATE_FIELD_EXCEPTION.getErrorCode(),
					String.format(UISpecErrorCode.UI_SPEC_DUPLICATE_FIELD_EXCEPTION.getErrorMessage(), duplicates));
		}
	}

	/**
	 * 
	 */
	@Override
	public UISpecResponseDto updateUISpec(String id, UISpecDto dto) {
		validateIdentityShema(dto.getIdentityschemaid());
		validateDuplicateFields(dto.getJsonspec());
		validateDocumentFields(dto.getJsonspec());
		validateBiometricFields(dto.getJsonspec());

		UISpec uiSpecObjectFromDb = getUISpecById(id);
		uiSpecObjectFromDb.setDomain(dto.getDomain());
		uiSpecObjectFromDb.setTitle(dto.getTitle());
		uiSpecObjectFromDb.setDescription(dto.getDescription());
		uiSpecObjectFromDb.setIdentitySchemaId(dto.getIdentityschemaid());
		uiSpecObjectFromDb.setJsonSpec(getIdAttributeJsonString(dto.getJsonspec()));
		uiSpecObjectFromDb.setUpdatedBy(MetaDataUtils.getContextUser());
		uiSpecObjectFromDb.setUpdatedDateTime(MetaDataUtils.getCurrentDateTime());
		uiSpecRepository.save(uiSpecObjectFromDb);
		return prepareResponse(uiSpecObjectFromDb);
	}

	/**
	 * 
	 */
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
		UISpec latestPublishedUISpec = uiSpecRepository.findLatestPublishedUISpec(uiSpecObjectFromDb.getDomain());
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
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorMessage());

		}
		return uiSpecObjectFromDb;
	}

	/**
	 * 
	 */
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
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_UPDATE_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));

		}
		return id;
	}
}
