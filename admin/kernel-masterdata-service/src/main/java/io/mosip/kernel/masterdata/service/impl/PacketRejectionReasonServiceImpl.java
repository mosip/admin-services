package io.mosip.kernel.masterdata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.PacketRejectionReasonErrorCode;
import io.mosip.kernel.masterdata.dto.PostReasonCategoryDto;
import io.mosip.kernel.masterdata.dto.ReasonCategoryDto;
import io.mosip.kernel.masterdata.dto.ReasonListDto;
import io.mosip.kernel.masterdata.dto.getresponse.PacketRejectionReasonResponseDto;
import io.mosip.kernel.masterdata.entity.ReasonCategory;
import io.mosip.kernel.masterdata.entity.ReasonList;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.entity.id.CodeLangCodeAndRsnCatCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.ReasonCategoryRepository;
import io.mosip.kernel.masterdata.repository.ReasonListRepository;
import io.mosip.kernel.masterdata.service.PacketRejectionReasonService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

/**
 * 
 * 
 * This class implements PacketRejectionReasonService has all the logics to
 * store and retrieve data from database {@link ReasonCategoryRepository} and
 * {@link ReasonListRepository}
 * 
 * @author Srinivasan
 *
 */
@Service
public class PacketRejectionReasonServiceImpl implements PacketRejectionReasonService {

	/**
	 * reason repository instance
	 */
	@Autowired
	private ReasonCategoryRepository reasonRepository;

	/**
	 * reason list repository instance
	 */
	@Autowired
	private ReasonListRepository reasonListRepository;

	/**
	 * Method fetches all the reasons from Database irrespective of code or
	 * languagecode {@inheritDoc}
	 */
	@Override
	public PacketRejectionReasonResponseDto getAllReasons() {
		List<ReasonCategory> reasonCategories = null;
		List<ReasonCategoryDto> reasonCategoryDtos = null;
		PacketRejectionReasonResponseDto reasonResponseDto = new PacketRejectionReasonResponseDto();

		try {
			reasonCategories = reasonRepository.findReasonCategoryByIsDeletedFalseOrIsDeletedIsNull();
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_FETCH_EXCEPTION.getErrorCode(),
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (reasonCategories != null && !reasonCategories.isEmpty()) {
			reasonCategoryDtos = MapperUtils.reasonConverter(reasonCategories);

		} else {
			throw new DataNotFoundException(
					PacketRejectionReasonErrorCode.NO_PACKET_REJECTION_REASONS_FOUND.getErrorCode(),
					PacketRejectionReasonErrorCode.NO_PACKET_REJECTION_REASONS_FOUND.getErrorMessage());
		}
		reasonResponseDto.setReasonCategories(reasonCategoryDtos);

		return reasonResponseDto;
	}

	/**
	 * Method fetchs reason based on reasonCategorycode and langCode {@inheritDoc}
	 */
	@Override
	public PacketRejectionReasonResponseDto getReasonsBasedOnLangCodeAndCategoryCode(String categoryCode,
			String langCode) {

		List<ReasonCategory> reasonCategories = null;
		List<ReasonCategoryDto> reasonCategoryDtos = null;
		PacketRejectionReasonResponseDto reasonResponseDto = new PacketRejectionReasonResponseDto();

		try {
			reasonCategories = reasonRepository.findReasonCategoryByCodeAndLangCode(categoryCode, langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_FETCH_EXCEPTION.getErrorCode(),
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (reasonCategories != null && !reasonCategories.isEmpty()) {

			reasonCategoryDtos = MapperUtils.reasonConverter(reasonCategories);

		} else {
			throw new DataNotFoundException(
					PacketRejectionReasonErrorCode.NO_PACKET_REJECTION_REASONS_FOUND.getErrorCode(),
					PacketRejectionReasonErrorCode.NO_PACKET_REJECTION_REASONS_FOUND.getErrorMessage());
		}
		reasonResponseDto.setReasonCategories(reasonCategoryDtos);

		return reasonResponseDto;
	}

	/**
	 * Method creates Reason Category data based on the request sent. {@inheritDoc}
	 */
	@Override
	@Transactional
	public CodeAndLanguageCodeID createReasonCategories(PostReasonCategoryDto reasonRequestDto) {
		ReasonCategory reasonCategories = MetaDataUtils.setCreateMetaData(reasonRequestDto, ReasonCategory.class);

		CodeAndLanguageCodeID codeAndLanguageCodeId = new CodeAndLanguageCodeID();
		ReasonCategory resultantReasonCategory = null;

		try {

			resultantReasonCategory = reasonRepository.create(reasonCategories);

		} catch (DataAccessLayerException | DataAccessException e) {

			throw new MasterDataServiceException(
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_CATEGORY_INSERT_EXCEPTION.getErrorCode(),
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_CATEGORY_INSERT_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		MapperUtils.map(resultantReasonCategory, codeAndLanguageCodeId);

		return codeAndLanguageCodeId;

	}

	/**
	 * Method creates ReasonList with the parameter that is sent in Request.
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public CodeLangCodeAndRsnCatCodeID createReasonList(ReasonListDto reasonRequestDto) {
		ReasonList reasonList = MetaDataUtils.setCreateMetaData(reasonRequestDto, ReasonList.class);

		CodeLangCodeAndRsnCatCodeID codeLangCodeAndRsnCatCodeId = new CodeLangCodeAndRsnCatCodeID();
		ReasonList resultantReasonList;

		try {

			resultantReasonList = reasonListRepository.create(reasonList);

		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_LIST_INSERT_EXCEPTION.getErrorCode(),
					PacketRejectionReasonErrorCode.PACKET_REJECTION_REASONS_LIST_INSERT_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		MapperUtils.map(resultantReasonList, codeLangCodeAndRsnCatCodeId);

		return codeLangCodeAndRsnCatCodeId;

	}

}
