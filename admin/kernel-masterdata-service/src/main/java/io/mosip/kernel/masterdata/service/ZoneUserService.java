package io.mosip.kernel.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.ZoneUserPutDto;
import io.mosip.kernel.masterdata.dto.ZoneUserSearchDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.ZoneUser;

@Service
public interface ZoneUserService {

	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) ;
	
	public StatusResponseDto updateZoneUserMapping(String userId, boolean isActive);

	public ZoneUserExtnDto updateZoneUserMapping(ZoneUserPutDto zoneUserDto);
	
	public IdResponseDto deleteZoneUserMapping( String userId, String zoneCode) ;

	public ZoneUserHistoryResponseDto getHistoryByUserIdAndTimestamp( String userId, String date);
	
	public ZoneUser getZoneUser(String userId, String zoneCode);
	
	public List<ZoneUser> getZoneUsers(List<String> userIds);

	public PageResponseDto<ZoneUserSearchDto> searchZoneUserMapping(SearchDtoWithoutLangCode request);
	
	public List<ZoneUser> getZoneUsersBasedOnZoneCode(String zoneCode);
}
