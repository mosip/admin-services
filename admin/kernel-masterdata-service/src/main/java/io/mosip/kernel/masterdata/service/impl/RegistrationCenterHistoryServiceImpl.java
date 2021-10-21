package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.RegistrationCenterErrorCode;
import io.mosip.kernel.masterdata.dto.RegistrationCenterHistoryDto;
import io.mosip.kernel.masterdata.dto.getresponse.RegistrationCenterHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenterHistory;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.RegistrationCenterHistoryRepository;
import io.mosip.kernel.masterdata.service.RegistrationCenterHistoryService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;

/**
 * Service class with function to fetch details of registration center history
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 *
 */
@Service
public class RegistrationCenterHistoryServiceImpl implements RegistrationCenterHistoryService {

	@Autowired
	private RegistrationCenterHistoryRepository registrationCenterHistoryRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.RegistrationCenterHistoryService#
	 * getRegistrationCenterHistory(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public RegistrationCenterHistoryResponseDto getRegistrationCenterHistory(String registrationCenterId,
			String langCode, String effectiveDate) {
		List<RegistrationCenterHistory> registrationCenters = null;
		RegistrationCenterHistoryResponseDto registrationCenterDto = new RegistrationCenterHistoryResponseDto();

		LocalDateTime localDateTime = null;
		try {
			localDateTime = MapperUtils.parseToLocalDateTime(effectiveDate);
		} catch (DateTimeParseException e) {
			throw new RequestException(RegistrationCenterErrorCode.DATE_TIME_PARSE_EXCEPTION.getErrorCode(),
					RegistrationCenterErrorCode.DATE_TIME_PARSE_EXCEPTION.getErrorMessage());
		}
		try {
			registrationCenters = registrationCenterHistoryRepository
					.findByIdAndLangCodeAndEffectivetimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(
							registrationCenterId, langCode, localDateTime);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					RegistrationCenterErrorCode.REGISTRATION_CENTER_FETCH_EXCEPTION.getErrorCode(),
					RegistrationCenterErrorCode.REGISTRATION_CENTER_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (registrationCenters == null || registrationCenters.isEmpty()) {
			throw new DataNotFoundException(RegistrationCenterErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
					RegistrationCenterErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
		} else {
			registrationCenterDto.setRegistrationCentersHistory(
					MapperUtils.mapAll(registrationCenters, RegistrationCenterHistoryDto.class));
		}
		return registrationCenterDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.masterdata.service.MachineHistoryService#createMachineHistory
	 * (io.mosip.kernel.masterdata.entity.MachineHistory)
	 */
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public IdResponseDto createRegistrationCenterHistory(RegistrationCenterHistory entityHistory) {
		RegistrationCenterHistory createdHistory;
		createdHistory = registrationCenterHistoryRepository.create(entityHistory);
		IdResponseDto idResponseDto = new IdResponseDto();
		MapperUtils.map(createdHistory, idResponseDto);
		return idResponseDto;
	}
}
