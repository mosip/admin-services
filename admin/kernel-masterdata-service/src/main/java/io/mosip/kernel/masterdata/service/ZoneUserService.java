package io.mosip.kernel.masterdata.service;

import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.ZoneUser;

@Service
public interface ZoneUserService {

	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) ;
	
	public ZoneUserExtnDto updateZoneUserMapping(ZoneUserDto zoneUserDto);
	
	public StatusResponseDto updateZoneUserMapping(String code, boolean isActive);
	
	public IdResponseDto deleteZoneUserMapping( String userId, String zoneCode) ;

	public ZoneUserHistoryResponseDto getHistoryByUserIdAndTimestamp( String userId, String date);
	
	public ZoneUser getZoneUser(String userId, String langCode, String zoneCode);
}
