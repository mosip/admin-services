package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UISpecRepository uiSpecRepository;

	@Autowired
	private IdentitySchemaRepository identitySchemaRepository;

	/**
	 * 
	 */
	@Override
	public List<UISpecResponseDto> getLatestUISpec(String domain) {
		List<UISpec> uiSpecObjectsFromDb = new ArrayList<UISpec>();
		try {
			uiSpecObjectsFromDb = uiSpecRepository.findLatestPublishedUISpec(domain);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_FETCH_EXCEPTION.getErrorMessage() + " " + ExceptionUtils.parseException(e));
		}
		if (uiSpecObjectsFromDb.isEmpty()) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}

		return prepareResponse(uiSpecObjectsFromDb);
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
	 * 
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
		validateIdentityShema(dto.getIdentitySchemaId());
		isJSONValid(dto.getJsonspec());

		UISpec uiSpecEntity = MetaDataUtils.setCreateMetaData(dto, UISpec.class);

		uiSpecEntity.setIsActive(false);
		uiSpecEntity.setIdentitySchemaId(dto.getIdentitySchemaId());
		uiSpecEntity.setStatus(STATUS_DRAFT);
		uiSpecEntity.setVersion(0);
		uiSpecEntity.setJsonSpec(dto.getJsonspec());
		uiSpecEntity.setId(UUID.randomUUID().toString());
		uiSpecEntity.setIsDeleted(false);
		uiSpecEntity.setEffectiveFrom(LocalDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
		try {
			uiSpecRepository.save(uiSpecEntity);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_INSERT_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_INSERT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
		return prepareResponse(uiSpecEntity);
	}

	/**
	 * 
	 * @param jsonInString
	 */
	private void isJSONValid(String jsonInString) {
		try {
			objectMapper.readTree(jsonInString);
		} catch (IOException e) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorCode(),
					UISpecErrorCode.UI_SPEC_VALUE_PARSE_ERROR.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
	}

	/**
	 * 
	 * @param id
	 */
	private void validateIdentityShema(String id) {
		IdentitySchema schema = identitySchemaRepository.findPublishedIdentitySchema(id);
		if (schema == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
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
		spec.add(new UISpecKeyValuePair(entity.getType(), entity.getJsonSpec()));
		response.setJsonSpec(spec);
		return response;
	}

	/**
	 * 
	 */
	@Override
	public UISpecResponseDto updateUISpec(String id, UISpecDto dto) {
		validateIdentityShema(dto.getIdentitySchemaId());
		isJSONValid(dto.getJsonspec());
		UISpec uiSpecObjectFromDb = getUISpecById(id);
		if (STATUS_PUBLISHED.equalsIgnoreCase(uiSpecObjectFromDb.getStatus())) {
			throw new MasterDataServiceException(UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorCode(),
					UISpecErrorCode.UI_SPEC_ALREADY_PUBLISHED.getErrorMessage() + "Cannot update the same.");
		}
		uiSpecObjectFromDb.setDomain(dto.getDomain());
		uiSpecObjectFromDb.setTitle(dto.getTitle());
		uiSpecObjectFromDb.setDescription(dto.getDescription());
		uiSpecObjectFromDb.setIdentitySchemaId(dto.getIdentitySchemaId());
		uiSpecObjectFromDb.setJsonSpec(dto.getJsonspec());
		uiSpecObjectFromDb.setUpdatedBy(MetaDataUtils.getContextUser());
		uiSpecObjectFromDb.setUpdatedDateTime(MetaDataUtils.getCurrentDateTime());
		uiSpecObjectFromDb.setType(dto.getType());
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
		UISpec latestPublishedUISpec = uiSpecRepository.findLatestPublishedUISpec(uiSpecObjectFromDb.getDomain(),
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

	@Override
	public List<UISpecResponseDto> getUISpec(double version, String domain, String type) {
		List<UISpec> specsFromDb = new ArrayList<UISpec>();
		UISpec uiSpecFromDb = uiSpecRepository.findPublishedUISpec(version, domain, type);
		if (uiSpecFromDb == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		specsFromDb.add(uiSpecFromDb);
		return prepareResponse(specsFromDb);
	}

	@Override
	public List<UISpecResponseDto> getUISpec(String domain, String type) {
		List<UISpec> specsFromDb = new ArrayList<UISpec>();
		UISpec uiSpecFromDb = uiSpecRepository.findLatestPublishedUISpec(domain, type);
		if (uiSpecFromDb == null) {
			throw new DataNotFoundException(UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorCode(),
					UISpecErrorCode.UI_SPEC_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		specsFromDb.add(uiSpecFromDb);
		return prepareResponse(specsFromDb);
	}
}