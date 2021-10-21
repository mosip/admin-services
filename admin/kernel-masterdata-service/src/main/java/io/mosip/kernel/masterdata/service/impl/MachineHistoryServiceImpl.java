/**
 *
 *
 */

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
import io.mosip.kernel.masterdata.constant.MachineHistoryErrorCode;
import io.mosip.kernel.masterdata.dto.MachineHistoryDto;
import io.mosip.kernel.masterdata.dto.getresponse.MachineHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.MachineHistory;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.MachineHistoryRepository;
import io.mosip.kernel.masterdata.service.MachineHistoryService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;

/**
 * This class have methods to fetch a Machine History Details
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Service
public class MachineHistoryServiceImpl implements MachineHistoryService {

	@Autowired
	MachineHistoryRepository machineHistoryRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.MachineHistoryService#
	 * getMachineHistroyIdLangEffDTime(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public MachineHistoryResponseDto getMachineHistroyIdLangEffDTime(String id, String langCode, String effDtime) {
		LocalDateTime lDateAndTime = null;
		try {
			lDateAndTime = MapperUtils.parseToLocalDateTime(effDtime);
		} catch (DateTimeParseException e) {
			throw new RequestException(
					MachineHistoryErrorCode.INVALIDE_EFFECTIVE_DATE_TIME_FORMATE_EXCEPTION.getErrorCode(),
					MachineHistoryErrorCode.INVALIDE_EFFECTIVE_DATE_TIME_FORMATE_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		List<MachineHistory> macHistoryList = null;
		List<MachineHistoryDto> machineHistoryDtoList = null;
		MachineHistoryResponseDto machineHistoryResponseDto = new MachineHistoryResponseDto();
		try {
			macHistoryList = machineHistoryRepository
					.findByFirstByIdAndLangCodeAndEffectDtimesLessThanEqualAndIsDeletedFalseOrIsDeletedIsNull(id,
							langCode, lDateAndTime);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(MachineHistoryErrorCode.MACHINE_HISTORY_FETCH_EXCEPTION.getErrorCode(),
					MachineHistoryErrorCode.MACHINE_HISTORY_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (macHistoryList != null && !macHistoryList.isEmpty()) {

			machineHistoryDtoList = MapperUtils.mapAll(macHistoryList, MachineHistoryDto.class);
		} else {
			throw new DataNotFoundException(MachineHistoryErrorCode.MACHINE_HISTORY_NOT_FOUND_EXCEPTION.getErrorCode(),
					MachineHistoryErrorCode.MACHINE_HISTORY_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		machineHistoryResponseDto.setMachineHistoryDetails(machineHistoryDtoList);
		return machineHistoryResponseDto;
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
	public IdResponseDto createMachineHistory(MachineHistory entityHistory) {
		MachineHistory createdHistory;
		createdHistory = machineHistoryRepository.create(entityHistory);
		IdResponseDto idResponseDto = new IdResponseDto();
		MapperUtils.map(createdHistory, idResponseDto);
		return idResponseDto;
	}
}
