package io.mosip.kernel.masterdata.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.mosip.kernel.core.util.EmptyCheckUtils;
import io.mosip.kernel.masterdata.constant.MachineTypeErrorCode;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.MachineTypePutDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.WorkingDaysExtnDto;
import io.mosip.kernel.masterdata.dto.request.WorkingDaysPutRequestDto;
import io.mosip.kernel.masterdata.entity.MachineSpecification;
import io.mosip.kernel.masterdata.entity.MachineType;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.WorkingNonWorkingDayErrorCode;
import io.mosip.kernel.masterdata.dto.DayNameAndSeqListDto;
import io.mosip.kernel.masterdata.dto.WeekDaysResponseDto;
import io.mosip.kernel.masterdata.dto.WorkingDaysResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.WeekDaysDto;
import io.mosip.kernel.masterdata.dto.getresponse.WorkingDaysDto;
import io.mosip.kernel.masterdata.entity.DaysOfWeek;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DaysOfWeekListRepo;
import io.mosip.kernel.masterdata.repository.RegWorkingNonWorkingRepo;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.service.RegWorkingNonWorkingService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegWorkingNonWorkingServiceImpl implements RegWorkingNonWorkingService {

	@Autowired
	@Qualifier("workingDaysRepo")
	private RegWorkingNonWorkingRepo workingDaysRepo;

	@Autowired
	@Qualifier("daysOfWeekRepo")
	private DaysOfWeekListRepo daysOfWeekRepo;

	@Autowired
	private MasterdataCreationUtil masterdataCreationUtil;

	/**
	 * Reference to RegistrationCenterRepository.
	 */
	@Autowired
	private RegistrationCenterRepository registrationCenterRepository;

	@Cacheable(value = "working-day", key = "'weekday'.concat('-').concat(#regCenterId).concat('-').concat(#langCode)",
			condition = "#regCenterId != null && #langCode != null")
	@Override
	public WeekDaysResponseDto getWeekDaysList(String regCenterId, String langCode) {

		List<WeekDaysDto> weekdayList = null;

		WeekDaysResponseDto weekdays = new WeekDaysResponseDto();
		RegistrationCenter registrationCenter = null;

		Objects.requireNonNull(regCenterId);
		Objects.requireNonNull(langCode);

		try {
			weekdayList = workingDaysRepo.findByregistrationCenterIdAndlangCodeForWeekDays(regCenterId, langCode);
			registrationCenter = registrationCenterRepository.findByIdAndLangCode(regCenterId, langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (registrationCenter == null) {
			throw new DataNotFoundException(WorkingNonWorkingDayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
					WorkingNonWorkingDayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
		} else {
			if (weekdayList != null && !weekdayList.isEmpty()) {
				weekdays.setWeekdays(weekdayList);
			}
			// Fetch from global level .
			else {
				List<DaysOfWeek> globalDaysList = daysOfWeekRepo.findBylangCode(langCode);
				if (globalDaysList != null && !globalDaysList.isEmpty()) {
					weekdayList = globalDaysList.stream().map(day -> {
						WeekDaysDto globalWorkingDay = new WeekDaysDto();
						globalWorkingDay.setDayCode(day.getCode());
						globalWorkingDay.setGlobalWorking(day.isGlobalWorking());
						globalWorkingDay.setLanguagecode(day.getLangCode());
						globalWorkingDay.setName(day.getName());
						return globalWorkingDay;
					}).collect(Collectors.toList());

					weekdays.setWeekdays(weekdayList);
				} else {
					throw new DataNotFoundException(
							WorkingNonWorkingDayErrorCode.WEEK_DAY_DATA_FOUND_EXCEPTION.getErrorCode(),
							WorkingNonWorkingDayErrorCode.WEEK_DAY_DATA_FOUND_EXCEPTION.getErrorMessage());
				}

			}
		}

		return weekdays;
	}

	@Cacheable(value = "working-day", key = "'workingday'.concat('-').concat(#regCenterId).concat('-').concat(#langCode)",
			condition = "#regCenterId != null && #langCode != null")
	@Override
	public WorkingDaysResponseDto getWorkingDays(String regCenterId, String langCode) {

		List<WorkingDaysDto> workingDayList = null;
		List<DayNameAndSeqListDto> nameSeqList = null;
		WorkingDaysResponseDto responseDto = new WorkingDaysResponseDto();
		Objects.requireNonNull(regCenterId);
		Objects.requireNonNull(langCode);
		RegistrationCenter registrationCenter = null;
		try {
			nameSeqList = workingDaysRepo.findByregistrationCenterIdAndlanguagecodeForWorkingDays(regCenterId,
					langCode);
			registrationCenter = registrationCenterRepository.findByIdAndLangCode(regCenterId, langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (registrationCenter == null) {
			throw new DataNotFoundException(WorkingNonWorkingDayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
					WorkingNonWorkingDayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
		} else {
			// Fetch from DB.
			if (nameSeqList != null && !nameSeqList.isEmpty()) {

				nameSeqList.sort((d1, d2) -> d1.getDaySeq() - d2.getDaySeq());
				workingDayList = nameSeqList.stream().map(nameSeq -> {
					WorkingDaysDto dto = new WorkingDaysDto();
					dto.setCode(nameSeq.getCode());
					dto.setLanguageCode(langCode);
					dto.setName(nameSeq.getName());
					dto.setOrder(nameSeq.getDaySeq());
					return dto;
				}).collect(Collectors.toList());
				responseDto.setWorkingdays(workingDayList);

			} else {
				List<DaysOfWeek> globalDaysList = daysOfWeekRepo.findByAllGlobalWorkingTrue(langCode);
				if (globalDaysList != null && !globalDaysList.isEmpty()) {
					globalDaysList.sort((d1, d2) -> d1.getDaySeq() - d2.getDaySeq());
					workingDayList = globalDaysList.stream().map(day -> {
						WorkingDaysDto dto = new WorkingDaysDto();
						dto.setLanguageCode(langCode);
						dto.setCode(day.getCode());
						dto.setName(day.getName());
						dto.setOrder(day.getDaySeq());
						return dto;
					}).collect(Collectors.toList());

					responseDto.setWorkingdays(workingDayList);
				} else {
					throw new DataNotFoundException(
							WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorCode(),
							WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorMessage());
				}

			}

		}

		return responseDto;
	}

	@Cacheable(value = "working-day", key = "'workingday'.concat('-').concat(#langCode)", condition = "#langCode != null")
	@Override
	public WorkingDaysResponseDto getWorkingDays(String langCode) {
		// TODO Auto-generated method stub
		List<WorkingDaysDto> workingDayList = null;
		WorkingDaysResponseDto responseDto = new WorkingDaysResponseDto();
		Objects.requireNonNull(langCode);

		List<DaysOfWeek> globalDaysList = null;
		try {
			globalDaysList = daysOfWeekRepo.findByAllGlobalWorkingTrue(langCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		if (globalDaysList != null && !globalDaysList.isEmpty()) {
			globalDaysList.sort((d1, d2) -> d1.getDaySeq() - d2.getDaySeq());
			workingDayList = globalDaysList.stream().map(day -> {
				WorkingDaysDto dto = new WorkingDaysDto();
				dto.setLanguageCode(langCode);
				dto.setName(day.getName());
				dto.setOrder(day.getDaySeq());
				dto.setCode(day.getCode());
				return dto;
			}).collect(Collectors.toList());

			responseDto.setWorkingdays(workingDayList);
		} else {
			throw new DataNotFoundException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorMessage());
		}

		return responseDto;
	}


	@Override
	@Transactional
	public WorkingDaysExtnDto updateWorkingDays(WorkingDaysPutRequestDto workingDaysPutRequestDto) throws NoSuchFieldException, IllegalAccessException {
		WorkingDaysExtnDto workingDayDto=new WorkingDaysExtnDto();
		WorkingDaysResponseDto responseDto = new WorkingDaysResponseDto();
		DaysOfWeek daysOfWeek = null;

		try {
			daysOfWeek = daysOfWeekRepo.findBylangCodeAndCode(workingDaysPutRequestDto.getLangCode(),workingDaysPutRequestDto.getCode());
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		if (daysOfWeek != null) {
			workingDaysPutRequestDto = masterdataCreationUtil.updateMasterData(DaysOfWeek.class, workingDaysPutRequestDto);
			daysOfWeek = MetaDataUtils.setUpdateMetaData(workingDaysPutRequestDto, daysOfWeek, false);
			daysOfWeekRepo.update(daysOfWeek);
			workingDayDto=MapperUtils.map(daysOfWeek,workingDayDto);
		} else {
			throw new DataNotFoundException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorMessage());
		}

		return workingDayDto;
	}

	@Override
	public StatusResponseDto updateWorkingDaysStatus(String code, boolean isActive) {
		StatusResponseDto statusResponseDto = new StatusResponseDto();
		List<DaysOfWeek> globalDaysList = null;
		try {
			globalDaysList = daysOfWeekRepo.findByCode(code);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_TABLE_NOT_ACCESSIBLE.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		if (globalDaysList != null && !globalDaysList.isEmpty()) {
			masterdataCreationUtil.updateMasterDataStatus(DaysOfWeek.class, code, isActive, "code");
		} else {
			throw new DataNotFoundException(
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorCode(),
					WorkingNonWorkingDayErrorCode.WORKING_DAY_DATA_FOUND_EXCEPTION.getErrorMessage());
		}
		statusResponseDto.setStatus("Status updated successfully for workingDays");
		return statusResponseDto;
	}
}
