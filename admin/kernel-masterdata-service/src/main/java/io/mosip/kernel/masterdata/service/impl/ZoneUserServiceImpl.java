package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UserDetailsHistoryErrorCode;
import io.mosip.kernel.masterdata.constant.ZoneUserErrorCode;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserHistoryResponseDto;
import io.mosip.kernel.masterdata.dto.ZoneUserPutDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.entity.ZoneUserHistory;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.ZoneUserHistoryRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

@Component
public class ZoneUserServiceImpl implements ZoneUserService {
	

	@Autowired
	UserDetailsService userDetailservice;
	
	@Autowired
	ZoneService zoneservice;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	ZoneUserRepository zoneUserRepo;
	
	@Value("#{'${mosip.mandatory-languages}'.concat('${mosip.optional-languages}')}")
	private String supportedLang;

	@Autowired
	ZoneUserHistoryRepository zoneUserHistoryRepo;

	@Autowired
	private AuditUtil auditUtil;
	
	@Override
	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) {
		ZoneUser zu=new ZoneUser();
		try {
			if(zoneUserRepo.findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(
					zoneUserDto.getUserId(), zoneUserDto.getLangCode(),zoneUserDto.getZoneCode())!=null) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.FAILURE_CREATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
						ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage() );	
			}
			// zu.setIsActive(getisActive(zoneUserDto.getUserId(),zoneUserDto.getLangCode(),zoneUserDto.getZoneCode(),zoneUserDto.getIsActive()
			// ));
			zu.setIsActive(zoneUserDto.getIsActive());
			zu = MetaDataUtils.setCreateMetaData(zoneUserDto, ZoneUser.class);
			zoneservice.getZone(zoneUserDto.getZoneCode(),zoneUserDto.getLangCode());//Throws exception if not found
			zu=zoneUserRepo.create(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(zu.getCreatedDateTime());
			zoneUserHistoryRepo.create(zuh);
		} catch (IllegalArgumentException  | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, ZoneUser.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
							ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, ZoneUser.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				ZoneUser.class.getSimpleName(), zu.getUserId()));
		ZoneUserExtnDto zoneUserDto1=new ZoneUserExtnDto();
		return MapperUtils.map(zu, zoneUserDto1);
	}
	@Override
	public ZoneUserExtnDto updateZoneUserMapping(ZoneUserPutDto zoneUserDto) {
		ZoneUser zu=new ZoneUser();
		ZoneUserExtnDto dto=new ZoneUserExtnDto();
		try {
			 zu = zoneUserRepo.findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(
					zoneUserDto.getUserId(), zoneUserDto.getLangCode(),zoneUserDto.getZoneCode());
			 if(zu ==null) {
				 auditUtil.auditRequest(
							String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
							MasterDataConstant.AUDIT_SYSTEM,
							String.format(MasterDataConstant.FAILURE_DESC,
									ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
									ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage()));
					throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
							ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage() );		
			 }
			 zoneservice.getZone(zoneUserDto.getZoneCode(),zoneUserDto.getLangCode());//Throws exception if not found
				// zu.setIsActive(getisActive(zoneUserDto.getUserId(),zoneUserDto.getLangCode(),zoneUserDto.getZoneCode(),zoneUserDto.getIsActive()
				// ));
			zu = MetaDataUtils.setUpdateMetaData(zoneUserDto, zu, false);
			zu=zoneUserRepo.update(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(zu.getUpdatedDateTime());
			zoneUserHistoryRepo.create(zuh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
							ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));			
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, ZoneUser.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				ZoneUser.class.getSimpleName(), zu.getUserId()));
		
		return MapperUtils.map(zu, dto);
	}

	/**
	 * 
	 * @param userId
	 * @param langCode
	 * @param zoneCode
	 * @param isActive
	 * @return
	 */
	/*
	 * private boolean getisActive(String userId, String langCode, String zoneCode,
	 * boolean isActive) { List<ZoneUser> allZoneUsers =
	 * zoneUserRepo.findByUserIdAndZoneCode(userId, zoneCode); if
	 * (allZoneUsers.stream().filter(a -> a.getIsActive() == false).count() > 0) {
	 * return false; } return isActive; }
	 */
	
	/*
	 * private boolean getisActive(String userId,String langCode,String
	 * zoneCode,boolean isActive) { if(supportedLang.contains(langCode)) { ZoneUser
	 * zoneUser=zoneUserRepo.findZoneUserByUserIdZoneCodeLangCodeIsActive(userId,
	 * secondaryLang,zoneCode); if(zoneUser==null) { return false; } }
	 * if(langCode.equalsIgnoreCase(secondaryLang)) { ZoneUser
	 * zoneUser=zoneUserRepo.findZoneUserByUserIdZoneCodeLangCodeIsActive(userId,
	 * primaryLang,zoneCode); if(zoneUser==null) { throw new
	 * MasterDataServiceException(RequestErrorCode.REQUEST_INVALID_SEC_LANG_ID.
	 * getErrorCode(),
	 * RequestErrorCode.REQUEST_INVALID_SEC_LANG_ID.getErrorMessage()); } } return
	 * isActive; }
	 */
	@Override
	public IdResponseDto deleteZoneUserMapping(String userId,String zoneCode) {
		IdResponseDto idResponse = new IdResponseDto();
		try {
			List<ZoneUser> zu = zoneUserRepo.findByUserIdAndZoneCode(userId,zoneCode);
			zu.forEach(user -> {
				zoneUserRepo.delete(user);
				ZoneUserHistory udh = new ZoneUserHistory();
				MapperUtils.map(user, udh);
				MapperUtils.setBaseFieldValue(user, udh);
				udh.setIsActive(false);
				udh.setIsDeleted(true);
				udh.setUpdatedBy(MetaDataUtils.getContextUser());
				udh.setDeletedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
				udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
				zoneUserHistoryRepo.create(udh);
				
			} );
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException 
		 | SecurityException e) {
			auditUtil.auditRequest(
			String.format(MasterDataConstant.FAILURE_DECOMMISSION, ZoneUser.class.getSimpleName()),
			MasterDataConstant.AUDIT_SYSTEM,
			String.format(MasterDataConstant.FAILURE_DESC,
					ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		idResponse.setId(userId);
		
			return idResponse;
	}
	
	@Override
	public ZoneUserHistoryResponseDto getHistoryByUserIdAndTimestamp(String userId, String effDTimes) {
		List<ZoneUserHistory> userDetails = null;
		ZoneUserHistoryResponseDto userResponseDto = new ZoneUserHistoryResponseDto();

		LocalDateTime localDateTime = null;
		try {
			localDateTime = MapperUtils.parseToLocalDateTime(effDTimes);
		} catch (DateTimeParseException e) {
			throw new RequestException(
					UserDetailsHistoryErrorCode.INVALID_EFFECTIVE_DATE_TIME_FORMATE_EXCEPTION.getErrorCode(),
					UserDetailsHistoryErrorCode.INVALID_EFFECTIVE_DATE_TIME_FORMATE_EXCEPTION.getErrorMessage());
		}
		try {
			userDetails = zoneUserHistoryRepo.getByUserIdAndTimestamp(userId, localDateTime);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					UserDetailsHistoryErrorCode.USER_HISTORY_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsHistoryErrorCode.USER_HISTORY_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (userDetails == null || userDetails.isEmpty()) {
			throw new DataNotFoundException(UserDetailsHistoryErrorCode.USER_HISTORY_NOT_FOUND_EXCEPTION.getErrorCode(),
					UserDetailsHistoryErrorCode.USER_HISTORY_NOT_FOUND_EXCEPTION.getErrorMessage());
		} else {
			userResponseDto.setUserResponseDto(MapperUtils.mapAll(userDetails, ZoneUserExtnDto.class));
		}
		return userResponseDto;
	}
	
	@Override
	public ZoneUser getZoneUser(String userId, String langCode, String zoneCode) {
		return zoneUserRepo.findByIdAndLangCodeAndIsDeletedFalseOrIsDeletedIsNull(userId,langCode,zoneCode);
	}
}
