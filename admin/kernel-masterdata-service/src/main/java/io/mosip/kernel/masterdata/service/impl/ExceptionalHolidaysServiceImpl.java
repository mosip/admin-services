package io.mosip.kernel.masterdata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.ExceptionalHolidayErrorCode;
import io.mosip.kernel.masterdata.dto.ExceptionalHolidayDto;
import io.mosip.kernel.masterdata.dto.getresponse.ExceptionalHolidayResponseDto;
import io.mosip.kernel.masterdata.entity.ExceptionalHoliday;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.ExceptionalHolidayRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.service.ExceptionalHolidayService;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;

/**
 * @author Kishan Rathore
 *
 */
@Service
public class ExceptionalHolidaysServiceImpl implements ExceptionalHolidayService {

	@Autowired
	private ExceptionalHolidayRepository repository;

	@Autowired
	private RegistrationCenterRepository regCenterRepo;

	@Value("#{'${mosip.mandatory-languages}'.concat('${mosip.optional-languages}')}")
	private String supportedLang;

	@Cacheable(value = "exceptional-holiday", key = "'exceptionalholiday'.concat('-').concat(#regCenterId).concat('-').concat(#langCode)",
			condition = "#regCenterId != null && #langCode != null")
	@Override
	public ExceptionalHolidayResponseDto getAllExceptionalHolidays(String regCenterId, String langCode) {
		ExceptionalHolidayResponseDto excepHolidayResponseDto = null;
		List<ExceptionalHolidayDto> excepHolidays = null;
		List<ExceptionalHoliday> exeptionalHolidayList = null;
		try {
			if (!supportedLang.contains(langCode)) {
				throw new MasterDataServiceException(ExceptionalHolidayErrorCode.INVALIDE_LANGCODE.getErrorCode(),
						ExceptionalHolidayErrorCode.INVALIDE_LANGCODE.getErrorMessage());
			}
			if (regCenterRepo.findByIdAndLangCode(regCenterId, langCode) == null) {
				throw new MasterDataServiceException(
						ExceptionalHolidayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorCode(),
						ExceptionalHolidayErrorCode.REGISTRATION_CENTER_NOT_FOUND.getErrorMessage());
			}
			exeptionalHolidayList = repository.findAllNonDeletedExceptionalHoliday(regCenterId, langCode);

		} catch (DataAccessException | DataAccessLayerException dataAccessException) {
			throw new MasterDataServiceException(
					ExceptionalHolidayErrorCode.EXCEPTIONAL_HOLIDAY_FETCH_EXCEPTION.getErrorCode(),
					ExceptionalHolidayErrorCode.EXCEPTIONAL_HOLIDAY_FETCH_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.parseException(dataAccessException));
		}
		if (exeptionalHolidayList != null && !exeptionalHolidayList.isEmpty()) {
			excepHolidays = MapperUtils.mapExceptionalHolidays(exeptionalHolidayList);
		} else {
			throw new DataNotFoundException(ExceptionalHolidayErrorCode.EXCEPTIONAL_HOLIDAY_NOTFOUND.getErrorCode(),
					ExceptionalHolidayErrorCode.EXCEPTIONAL_HOLIDAY_NOTFOUND.getErrorMessage());
		}
		excepHolidayResponseDto = new ExceptionalHolidayResponseDto();
		excepHolidayResponseDto.setExceptionalHolidayList(excepHolidays);
		return excepHolidayResponseDto;
	}

}
