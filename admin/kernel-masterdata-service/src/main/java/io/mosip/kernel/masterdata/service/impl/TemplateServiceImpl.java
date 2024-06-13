package io.mosip.kernel.masterdata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;

import io.mosip.kernel.masterdata.dto.response.FilterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.TemplateErrorCode;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.TemplatePutDto;
import io.mosip.kernel.masterdata.dto.getresponse.PageDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.TemplateResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.TemplateExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.Pagination;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.request.SearchSort;
import io.mosip.kernel.masterdata.dto.response.ColumnValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.Language;
import io.mosip.kernel.masterdata.entity.Template;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.LanguageRepository;
import io.mosip.kernel.masterdata.repository.TemplateRepository;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.EventPublisherUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterDataFilterHelper;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterColumnValidator;
import io.mosip.kernel.masterdata.validator.FilterTypeValidator;
import io.mosip.kernel.websub.api.exception.WebSubClientException;

/**
 * 
 * @author Neha
 * @author Uday Kumar
 * @since 1.0.0
 *
 */
@Service
public class TemplateServiceImpl implements TemplateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);

	@Autowired
	private TemplateRepository templateRepository;

//	private List<Template> templateList;

//	private List<TemplateDto> templateDtoList;

//	private TemplateResponseDto templateResponseDto = new TemplateResponseDto();
	
	
	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private FilterTypeValidator filterTypeValidator;

	@Value("${mosip.kernel.masterdata.template_idauthentication_event:MASTERDATA_IDAUTHENTICATION_TEMPLATES}")
	private String topic;

	@Value("${websub.publish.url}")
	private String hubURL;

	@Value("${mosip.kernel.masterdata.template_idauthentication_event_module_id:10004}")
	private String idAuthModuleId;

	@Autowired
	private FilterColumnValidator filterColumnValidator;

	@Autowired
	private MasterdataSearchHelper masterDataSearchHelper;

	@Autowired
	private MasterDataFilterHelper masterDataFilterHelper;

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private PageUtils pageUtils;

	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private PublisherClient<String, EventModel, HttpHeaders> publisher;
	

	@Scheduled(fixedDelayString = "${masterdata.websub.resubscription.delay.millis}",
			initialDelayString = "${masterdata.subscriptions-delay-on-startup}")
	public void subscribeTopics() {
		try {
			publisher.registerTopic(topic, hubURL);
		} catch (WebSubClientException exception) {
			LOGGER.warn(exception.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateService#getAllTemplate()
	 */
	@Cacheable(value = "templates", key = "'template'")
	@Override
	public TemplateResponseDto getAllTemplate() {
		List<Template> templateList = null;
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		try {
			templateList = templateRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(Template.class);
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		if (templateList != null && !templateList.isEmpty()) {
			templateResponseDto.setTemplates(MapperUtils.mapAll(templateList, TemplateDto.class));
			return templateResponseDto;
		}
		throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
					TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateService#
	 * getAllTemplateByLanguageCode(java.lang.String)
	 */
	@Cacheable(value = "templates", key = "'template'.concat('-').concat(#languageCode)", condition="#languageCode != null")
	@Override
	public TemplateResponseDto getAllTemplateByLanguageCode(String languageCode) {
		List<Template> templateList = null;
		Language language=null;
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		try {
			language=languageRepository.findLanguageByCodeNameAndNativeName(languageCode);
			templateList = templateRepository.findAllByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(language.getCode());
		} catch (DataAccessException | DataAccessLayerException | NullPointerException exception) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		if (templateList != null && !templateList.isEmpty()) {
			templateResponseDto.setTemplates(MapperUtils.mapAll(templateList, TemplateDto.class));
			return templateResponseDto;
		}
		throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
					TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateService#
	 * getAllTemplateByLanguageCodeAndTemplateTypeCode(java.lang.String,
	 * java.lang.String)
	 */
	@Cacheable(value = "templates", key = "'template'.concat('-').concat(#languageCode).concat('-').concat(#templateTypeCode)", condition = "#languageCode != null && #templateTypeCode != null")
	@Override
	public TemplateResponseDto getAllTemplateByLanguageCodeAndTemplateTypeCode(String languageCode,
			String templateTypeCode) {
		List<Template> templateList = null;
		Language language=null;
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		try {
			language=languageRepository.findLanguageByCodeNameAndNativeName(languageCode);
			templateList = templateRepository.findAllByLangCodeAndTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(
					language.getCode(), templateTypeCode);
		} catch (DataAccessException | DataAccessLayerException | NullPointerException exception) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		if (templateList != null && !templateList.isEmpty()) {
			templateResponseDto.setTemplates(MapperUtils.mapAll(templateList, TemplateDto.class));
			return templateResponseDto;
		}
		throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
					TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.TemplateService#createTemplate(io.mosip.
	 * kernel.masterdata.dto.TemplateDto)
	 */
	@CacheEvict(value = "templates", allEntries = true)
	@Override
	public IdAndLanguageCodeID createTemplate(TemplateDto template) {

		Template templateEntity;
		try {
			if (template.getId() == null || template.getId().trim().isBlank()) {
				template.setId(generateId());
			}

			template = masterdataCreationUtil.createMasterData(Template.class, template);
			Template entity = MetaDataUtils.setCreateMetaData(template, Template.class);
			templateEntity = templateRepository.create(entity);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.CREATE_ERROR_AUDIT, Template.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateErrorCode.TEMPLATE_INSERT_EXCEPTION.getErrorCode(),
							TemplateErrorCode.TEMPLATE_INSERT_EXCEPTION.getErrorMessage()),
					"ADM-812");
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_INSERT_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_INSERT_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));

		}

		IdAndLanguageCodeID idAndLanguageCodeID = new IdAndLanguageCodeID();
		MapperUtils.map(templateEntity, idAndLanguageCodeID);
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, Template.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						Template.class.getSimpleName(), idAndLanguageCodeID.getId()),
				"ADM-813");
		return idAndLanguageCodeID;
	}

	private String generateId() throws DataAccessLayerException, DataAccessException {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();

		List<Template> template = templateRepository.findAllByIdAndIsDeletedFalseOrIsDeletedIsNull(uniqueId);
		return template.isEmpty() ? uniqueId : generateId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.TemplateService#updateTemplates(io.mosip.
	 * kernel.masterdata.dto.TemplateDto)
	 */
	@CacheEvict(value = "templates", allEntries = true)
	@Override
	public IdAndLanguageCodeID updateTemplates(TemplatePutDto template) {
		IdAndLanguageCodeID idAndLanguageCodeID = new IdAndLanguageCodeID();
		try {
			Template entity = templateRepository.findTemplateByIDAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(
					template.getId(), template.getLangCode());
			if (!EmptyCheckUtils.isNullEmpty(entity)) {
				template = masterdataCreationUtil.updateMasterData(Template.class, template);
				MetaDataUtils.setUpdateMetaData(template, entity, false);
				templateRepository.update(entity);
				if (template.getModuleId().equalsIgnoreCase(idAuthModuleId)) {
					TemplateDto templateDto = MapperUtils.map(template, TemplateDto.class);
                    EventModel eventModel  =EventPublisherUtil.populateEventModel(templateDto, MasterDataConstant.PUBLISHER_ID, topic , "templates");
					publisher.publishUpdate(topic, eventModel, MediaType.APPLICATION_JSON_UTF8_VALUE,
							null, hubURL);
				}
				idAndLanguageCodeID.setId(entity.getId());
				idAndLanguageCodeID.setLangCode(entity.getLangCode());
			} else {
				auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, Template.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
								TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage()),
						"ADM-814");
				throw new RequestException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
						TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
			}
		} catch (WebSubClientException exception) {
			LOGGER.warn(exception.getMessage());
		}catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, Template.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateErrorCode.TEMPLATE_UPDATE_EXCEPTION.getErrorCode(),
							TemplateErrorCode.TEMPLATE_UPDATE_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(e)),
					"ADM-815");
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_UPDATE_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_UPDATE_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, Template.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						Template.class.getSimpleName(), idAndLanguageCodeID.getId()),
				"ADM-816");
		return idAndLanguageCodeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.TemplateService#deleteTemplates(java.lang.
	 * String)
	 */
	@CacheEvict(value = "templates", allEntries = true)
	@Transactional
	@Override
	public IdResponseDto deleteTemplates(String id) {
		try {
			int updatedRows = templateRepository.deleteTemplate(id, MetaDataUtils.getCurrentDateTime(),
					MetaDataUtils.getContextUser());
			if (updatedRows < 1) {
				throw new RequestException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
						TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_DELETE_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_DELETE_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		IdResponseDto idResponseDto = new IdResponseDto();
		idResponseDto.setId(id);
		return idResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateService#
	 * getAllTemplateByTemplateTypeCode(java.lang.String)
	 */
	@Cacheable(value = "templates", key = "'templateByTemplateCode'.concat('-').concat(#templateTypeCode)", condition = "#templateTypeCode != null")
	@Override
	public TemplateResponseDto getAllTemplateByTemplateTypeCode(String templateTypeCode) {
		List<Template> templates;
		List<TemplateDto> templateDtos;
		try {
			templates = templateRepository
					.findAllByTemplateTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(templateTypeCode);
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		if (templates != null && !templates.isEmpty()) {
			templateDtos = MapperUtils.mapAll(templates, TemplateDto.class);
		} else {
			throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
					TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
		}
		TemplateResponseDto responseDto = new TemplateResponseDto();
		responseDto.setTemplates(templateDtos);
		return responseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateService#getTemplates(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public PageDto<TemplateExtnDto> getTemplates(int pageNumber, int pageSize, String sortBy, String orderBy) {
		List<TemplateExtnDto> templates = null;
		PageDto<TemplateExtnDto> pageDto = null;
		try {
			Page<Template> pageData = templateRepository
					.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				templates = MapperUtils.mapAll(pageData.getContent(), TemplateExtnDto.class);
				pageDto = new PageDto<>(pageData.getNumber(), pageData.getTotalPages(), pageData.getTotalElements(),
						templates);
			} else {
				throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
						TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		return pageDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.TemplateService#searchTemplates(io.mosip.
	 * kernel.masterdata.dto.request.SearchDto)
	 */
	@SuppressWarnings("null")
	@Override
	public PageResponseDto<TemplateExtnDto> searchTemplates(SearchDto searchDto) {
		PageResponseDto<TemplateExtnDto> pageDto = new PageResponseDto<>();
		List<TemplateExtnDto> templates = null;

		if (filterTypeValidator.validate(TemplateExtnDto.class, searchDto.getFilters())) {
			Pagination pagination = searchDto.getPagination();
			List<SearchSort> sort = searchDto.getSort();
			searchDto.setPagination(new Pagination(0, Integer.MAX_VALUE));
			searchDto.setSort(Collections.emptyList());
			pageUtils.validateSortField(Template.class, sort);
			Page<Template> page = masterDataSearchHelper.searchMasterdata(Template.class, searchDto, null);
			if (page.getContent() != null && !page.getContent().isEmpty()) {
				templates = MapperUtils.mapAll(page.getContent(), TemplateExtnDto.class);
				pageDto = pageUtils.sortPage(templates, sort, pagination);
			}
		}
		return pageDto;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.TemplateService#filterTemplates(io.mosip.
	 * kernel.masterdata.dto.request.FilterValueDto)
	 */
	@Override
	public FilterResponseDto filterTemplates(FilterValueDto filterValueDto) {
		FilterResponseDto filterResponseDto = new FilterResponseDto();
		List<ColumnValue> columnValueList = new ArrayList<>();

		if (filterColumnValidator.validate(FilterDto.class, filterValueDto.getFilters(), Template.class)) {
			filterValueDto.getFilters().stream().forEach(filter -> {
				FilterResult<String> filterResult = masterDataFilterHelper.filterValues(Template.class, filter, filterValueDto);
				filterResult.getFilterData().forEach(filteredValue -> {
					if (filteredValue != null) {
						ColumnValue columnValue = new ColumnValue();
						columnValue.setFieldID(filter.getColumnName());
						columnValue.setFieldValue(filteredValue);
						columnValueList.add(columnValue);
					}
				});
				filterResponseDto.setTotalCount(filterResult.getTotalCount());
			});
			filterResponseDto.setFilters(columnValueList);
		}
		return filterResponseDto;
	}

	@CacheEvict(value = "templates", allEntries = true)
	@Override
	public StatusResponseDto updateTemplates(String id, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();

		List<Template> templates = null;
		try {
			templates = templateRepository.findtoUpdateTemplateById(id);
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, Template.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
							TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-817");
			throw new MasterDataServiceException(TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorCode(),
					TemplateErrorCode.TEMPLATE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}

		if (templates != null && !templates.isEmpty()) {
			masterdataCreationUtil.updateMasterDataStatus(Template.class, id, isActive, "id");
		} else {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, Template.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC, TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
							TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage()),
					"ADM-818");
			throw new DataNotFoundException(TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorCode(),
					TemplateErrorCode.TEMPLATE_NOT_FOUND.getErrorMessage());
		}
		response.setStatus("Status updated successfully for Templates");
		return response;
	}

}