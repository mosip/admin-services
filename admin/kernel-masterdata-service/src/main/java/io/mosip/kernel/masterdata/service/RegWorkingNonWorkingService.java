package io.mosip.kernel.masterdata.service;

import io.mosip.kernel.masterdata.dto.WeekDaysResponseDto;
import io.mosip.kernel.masterdata.dto.WorkingDaysResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.WorkingDaysExtnDto;
import io.mosip.kernel.masterdata.dto.request.WorkingDaysPutRequestDto;

public interface RegWorkingNonWorkingService {

	WeekDaysResponseDto getWeekDaysList(String regCenterId, String langCode);

	WorkingDaysResponseDto getWorkingDays(String regCenterId, String dayCode);
	
	WorkingDaysResponseDto getWorkingDays(String langCode);

	WorkingDaysExtnDto updateWorkingDays(WorkingDaysPutRequestDto request) throws NoSuchFieldException, IllegalAccessException;

	StatusResponseDto updateWorkingDaysStatus(String code, boolean isActive);
}
