package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UserDetailsHistoryErrorCode;
import io.mosip.kernel.masterdata.constant.ZoneUserErrorCode;
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
import io.mosip.kernel.masterdata.entity.ZoneUserHistory;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserHistoryRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;

@Component
public class ZoneUserServiceImpl implements ZoneUserService {
	

	@Autowired
	UserDetailsService userDetailservice;
	
	@Autowired
	ZoneService zoneservice;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private MasterdataSearchHelper masterDataSearchHelper;

	@Autowired
	ZoneUserRepository zoneUserRepo;
	
	@Autowired
	UserDetailsRepository userDetailsRepo;
	
	@Value("#{'${mosip.mandatory-languages:}'.concat(',').concat('${mosip.optional-languages:}')}")
	private String supportedLang;

	@Autowired
	ZoneUserHistoryRepository zoneUserHistoryRepo;

	@Autowired
	private AuditUtil auditUtil;
	
	@Override
	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) {
		ZoneUser zu=new ZoneUser();
		try {
			if(zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(
					zoneUserDto.getUserId(), zoneUserDto.getZoneCode())!=null) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.FAILURE_CREATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
						ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage() );	
			}
			if(zoneUserRepo.findByUserId(zoneUserDto.getUserId()) != null) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.FAILURE_CREATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorCode(),
								ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorMessage());
				
			}
			zu.setIsActive(zoneUserDto.getIsActive());
			zu = MetaDataUtils.setCreateMetaData(zoneUserDto, ZoneUser.class);

			//Throws exception if not found
			zoneservice.getZone(zoneUserDto.getZoneCode(), supportedLang.split(",")[0]);

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
		ZoneUser zu= null;
		ZoneUserExtnDto dto=new ZoneUserExtnDto();
		try {
			zu = zoneUserRepo.findByUserId(zoneUserDto.getUserId());
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
			 else {
				 deleteZoneUserMapping(zu.getUserId(), zu.getZoneCode());
			 }

			//Throws exception if not found
			zoneservice.getZone(zoneUserDto.getZoneCode(),  supportedLang.split(",")[0]);
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
	public ZoneUser getZoneUser(String userId, String zoneCode) {
		return zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(userId, zoneCode);
	}
	
	@Override
	public StatusResponseDto updateZoneUserMapping(String userId, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();

		ZoneUser zoneUser = null;
		try {
			zoneUser = zoneUserRepo.findZoneByUserIdNonDeleted(userId);
		} catch (DataAccessException | DataAccessLayerException accessException) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							ZoneUserErrorCode.ZONE_FETCH_EXCEPTION.getErrorCode(),
							ZoneUserErrorCode.ZONE_FETCH_EXCEPTION.getErrorMessage()),
					"ADM-835");
			throw new MasterDataServiceException(ZoneUserErrorCode.ZONE_FETCH_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.ZONE_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(accessException));
		}

		if (zoneUser != null) {
			masterdataCreationUtil.updateMasterDataStatus(ZoneUser.class, userId, isActive, "userId");
		} else {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							ZoneUserErrorCode.ZONE_NOT_FOUND_EXCEPTION.getErrorCode(),
							ZoneUserErrorCode.ZONE_NOT_FOUND_EXCEPTION.getErrorMessage()),
					"ADM-836");
			throw new DataNotFoundException(ZoneUserErrorCode.ZONE_NOT_FOUND_EXCEPTION.getErrorCode(),
					ZoneUserErrorCode.ZONE_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		response.setStatus("Status updated successfully for Zone");
		return response;
	}

	@Override
	public PageResponseDto<ZoneUserSearchDto> searchZoneUserMapping(SearchDtoWithoutLangCode searchDto) {
		PageResponseDto<ZoneUserSearchDto> pageDto = new PageResponseDto<>();
		List<ZoneUserExtnDto> zoneUserSearchDetails = null;
		List<ZoneUserSearchDto> zoneSearch=new ArrayList<>();

		searchDto.getFilters().stream().forEach(fil -> {
			if (fil.getColumnName().equalsIgnoreCase("name")) {
				fil.setValue("*" + fil.getValue() + "*");
				fil.setType("contains");
			}
		});
		Page<ZoneUser> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(ZoneUser.class, searchDto,
				null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			zoneUserSearchDetails = MapperUtils.mapAll(page.getContent(), ZoneUserExtnDto.class);
			pageDto = PageUtils.pageResponse(page);
			zoneUserSearchDetails.forEach(z->{
				ZoneUserSearchDto dto=new ZoneUserSearchDto();
				dto.setCreatedBy(z.getCreatedBy());
				dto.setCreatedDateTime(z.getCreatedDateTime());
				dto.setDeletedDateTime(z.getDeletedDateTime());
				dto.setIsActive(z.getIsActive());
				dto.setIsDeleted(z.getIsDeleted());
				dto.setLangCode(z.getLangCode());
				dto.setZoneCode(z.getZoneCode());
				dto.setUserId(z.getUserId());
				dto.setUpdatedDateTime(z.getUpdatedDateTime());
				dto.setUpdatedBy(z.getUpdatedBy());
				if(null!=z.getUserId())
					dto.setUserName(userDetailsRepo.findById(z.getUserId()).get().getName());
				else
					dto.setUserName(null);
				if(null!=z.getZoneCode())
					dto.setZoneName(zoneservice.getZone(z.getZoneCode(),z.getLangCode()).getZoneName());
				else
					dto.setZoneName(null);
				zoneSearch.add(dto);
			});
			pageDto.setData(zoneSearch);
		}
		return pageDto;
	}

	@Override
	public List<ZoneUser> getZoneUsers(List<String> userIds) {
		return zoneUserRepo.findByUserIds(userIds);
	}
}
