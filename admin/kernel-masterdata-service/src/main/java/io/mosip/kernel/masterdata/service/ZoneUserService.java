package io.mosip.kernel.masterdata.service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;
import io.mosip.kernel.masterdata.validator.StringFormatter;
@Service
public interface ZoneUserService {

	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) ;
	
	public ZoneUserExtnDto updateZoneUserMapping(ZoneUserDto zoneUserDto) ;
	
	public IdResponseDto deleteZoneUserMapping( String userId, String zoneCode) ;

	public ZoneUserHistoryResponseDto getHistoryByUserIdAndTimestamp( String userId, String date);

}
