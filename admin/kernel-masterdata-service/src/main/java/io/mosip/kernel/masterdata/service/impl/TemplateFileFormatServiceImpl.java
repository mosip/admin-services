package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.TemplateFileFormatErrorCode;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatPutDto;
import io.mosip.kernel.masterdata.dto.TemplateFileFormatResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.entity.Template;
import io.mosip.kernel.masterdata.entity.TemplateFileFormat;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.TemplateFileFormatRepository;
import io.mosip.kernel.masterdata.repository.TemplateRepository;
import io.mosip.kernel.masterdata.service.TemplateFileFormatService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

@Service
public class TemplateFileFormatServiceImpl implements TemplateFileFormatService {

	private static final Logger logger = LoggerFactory.getLogger(TemplateFileFormatServiceImpl.class);

	@Autowired
	private TemplateFileFormatRepository templateFileFormatRepository;

	@Autowired
	private TemplateRepository templateRepository;


	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private AuditUtil auditUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.TemplateFileFormatService#
	 * createTemplateFileFormat(io.mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	public CodeAndLanguageCodeID createTemplateFileFormat(TemplateFileFormatDto templateFileFormatRequestDto) {

		TemplateFileFormat templateFileFormat;
		try {
			TemplateFileFormat entity = MetaDataUtils.setCreateMetaData(templateFileFormatRequestDto,
					TemplateFileFormat.class);
			templateFileFormat = templateFileFormatRepository.create(entity);
		} catch (DataAccessException | DataAccessLayerException | IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, TemplateFileFormat.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_INSERT_EXCEPTION.getErrorCode(),
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_INSERT_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_INSERT_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_INSERT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
		CodeAndLanguageCodeID codeLangCodeId = new CodeAndLanguageCodeID();
		MapperUtils.map(templateFileFormat, codeLangCodeId);
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_CREATE, TemplateFileFormat.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						TemplateFileFormat.class.getSimpleName(), codeLangCodeId.getCode()));
		return codeLangCodeId;
	}

	@Override
	public CodeAndLanguageCodeID updateTemplateFileFormat(TemplateFileFormatPutDto templateFileFormatRequestDto) {

		TemplateFileFormatPutDto templateFileFormatDto = templateFileFormatRequestDto;

		CodeAndLanguageCodeID templateFileFormatId = new CodeAndLanguageCodeID();

		MapperUtils.mapFieldValues(templateFileFormatDto, templateFileFormatId);

		try {
			TemplateFileFormat templateFileFormat = templateFileFormatRepository
					.findByCodeAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(templateFileFormatRequestDto.getCode(),
							templateFileFormatRequestDto.getLangCode());

			if (templateFileFormat != null) {
				/*
				 * if (!templateFileFormatRequestDto.getIsActive()) { List<Template> templates =
				 * templateRepository
				 * .findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(
				 * templateFileFormatRequestDto.getCode());
				 * 
				 * if (!EmptyCheckUtils.isNullEmpty(templates)) { throw new RequestException(
				 * TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_MAPPING_EXCEPTION
				 * .getErrorCode(),
				 * TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_MAPPING_EXCEPTION
				 * .getErrorMessage()); }
				 * masterdataCreationUtil.updateMasterDataDeactivate(TemplateFileFormat.class,
				 * templateFileFormatRequestDto.getCode()); }
				 */
				templateFileFormatRequestDto = masterdataCreationUtil.updateMasterData(TemplateFileFormat.class,
						templateFileFormatRequestDto);
				MetaDataUtils.setUpdateMetaData(templateFileFormatDto, templateFileFormat, false);
				templateFileFormatRepository.update(templateFileFormat);

			} else {

				throw new RequestException(TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),

						TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage());
			}

		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {

			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, TemplateFileFormat.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_EXCEPTION.getErrorCode(),
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_EXCEPTION.getErrorMessage()
									+ ExceptionUtils.parseException(e)));
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(
				String.format(MasterDataConstant.SUCCESSFUL_UPDATE, TemplateFileFormat.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_UPDATE_DESC,
						TemplateFileFormat.class.getSimpleName(), templateFileFormatId.getCode()));
		return templateFileFormatId;
	}

	@Override
	public CodeResponseDto deleteTemplateFileFormat(String code) {
		List<Template> templates;
		try {
			templates = templateRepository.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(code);
			if (!templates.isEmpty()) {
				throw new MasterDataServiceException(
						TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_DELETE_DEPENDENCY_EXCEPTION.getErrorCode(),
						TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_DELETE_DEPENDENCY_EXCEPTION.getErrorMessage());
			}
			int updatedRows = templateFileFormatRepository.deleteTemplateFileFormat(MetaDataUtils.getContextUser(),
					LocalDateTime.now(ZoneId.of("UTC")), code);
			if (updatedRows < 1) {

				throw new RequestException(TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),

						TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			logger.error("TEMPLATE_FILE_FORMAT_DELETE_EXCEPTION", e);
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_DELETE_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_DELETE_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(e));
		}
		CodeResponseDto responseDto = new CodeResponseDto();
		responseDto.setCode(code);
		return responseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.MachineService#getMachine(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public TemplateFileFormatResponseDto getTemplateFileFormatCodeandLangCode(String templateFileFormatCode,
			String langCode) {
		List<TemplateFileFormat> templateFileFormatList = null;
		List<TemplateFileFormatDto> templateFileFormatDtoList = null;
		TemplateFileFormatResponseDto templateFileFormatResponseDto = new TemplateFileFormatResponseDto();
		try {
			templateFileFormatList = templateFileFormatRepository
					.findAllByCodeAndIsDeletedFalseorIsDeletedIsNull(templateFileFormatCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (templateFileFormatList != null && !templateFileFormatList.isEmpty()) {
			templateFileFormatDtoList = MapperUtils.mapAll(templateFileFormatList, TemplateFileFormatDto.class);
		} else {

			throw new DataNotFoundException(TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage());

		}
		templateFileFormatResponseDto.setTemplateFileFormats(templateFileFormatDtoList);
		return templateFileFormatResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.MachineService#getMachine(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public TemplateFileFormatResponseDto getTemplateFileFormatLangCode(String langCode) {
		List<TemplateFileFormat> templateFileFormatList = null;
		List<TemplateFileFormatDto> templateFileFormatDtoList = null;
		TemplateFileFormatResponseDto templateFileFormatResponseDto = new TemplateFileFormatResponseDto();
		try {
			templateFileFormatList = templateFileFormatRepository.findAllByIsDeletedFalseorIsDeletedIsNull();
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (templateFileFormatList != null && !templateFileFormatList.isEmpty()) {
			templateFileFormatDtoList = MapperUtils.mapAll(templateFileFormatList, TemplateFileFormatDto.class);
		} else {

			throw new DataNotFoundException(TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage());

		}
		templateFileFormatResponseDto.setTemplateFileFormats(templateFileFormatDtoList);
		return templateFileFormatResponseDto;
	}

	@Override
	public StatusResponseDto updateTemplateFileFormat(String code, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();

		List<TemplateFileFormat> templateFileFormats = null;
		try {
			templateFileFormats = templateFileFormatRepository.findtoUpdateTemplateFileFormatByCode(code);
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, TemplateFileFormat.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorCode(),
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-833");
			throw new MasterDataServiceException(
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}

		if (templateFileFormats != null && !templateFileFormats.isEmpty()) {
			if (!isActive) {
				List<Template> templates = templateRepository
						.findAllByFileFormatCodeAndIsDeletedFalseOrIsDeletedIsNull(code);
				if (!EmptyCheckUtils.isNullEmpty(templates)) {
					throw new RequestException(
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_MAPPING_EXCEPTION.getErrorCode(),
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_UPDATE_MAPPING_EXCEPTION
									.getErrorMessage());
				}
			}
			masterdataCreationUtil.updateMasterDataStatus(TemplateFileFormat.class, code, isActive, "code");
		} else {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, TemplateFileFormat.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),
							TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage()),
					"ADM-834");
			throw new DataNotFoundException(TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorCode(),
					TemplateFileFormatErrorCode.TEMPLATE_FILE_FORMAT_NOT_FOUND.getErrorMessage());
		}
		response.setStatus("Status updated successfully for Template File Formats");
		return response;
	}
}
