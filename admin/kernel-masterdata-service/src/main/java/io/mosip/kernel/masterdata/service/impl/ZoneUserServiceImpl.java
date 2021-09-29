package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.mosip.kernel.core.authmanager.authadapter.model.AuthUserDetails;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.http.RequestWrapper;
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
import io.mosip.kernel.masterdata.dto.getresponse.ZoneNameResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.Zone;
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
import io.mosip.kernel.masterdata.utils.LanguageUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

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

	@Value("${zone.user.details.url}")
	private String userDetails;

	@Autowired
	ZoneUserHistoryRepository zoneUserHistoryRepo;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private AuditUtil auditUtil;

	@Override
	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) {
		ZoneUser zu = new ZoneUser();
		try {
			if (zoneUserRepo.findByIdAndIsDeletedFalseOrIsDeletedIsNull(zoneUserDto.getUserId(),
					zoneUserDto.getZoneCode()) != null) {
				auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_CREATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
								ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorCode(),
						ZoneUserErrorCode.DUPLICATE_REQUEST.getErrorMessage());
			}
			if (zoneUserRepo.findByUserId(zoneUserDto.getUserId()) != null) {
				auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_CREATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorCode(),
								ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_PRSENT_IN_DB.getErrorMessage());

			}
			zu.setIsActive(zoneUserDto.getIsActive());
			zu = MetaDataUtils.setCreateMetaData(zoneUserDto, ZoneUser.class);

			// Throws exception if not found
			zoneservice.getZone(zoneUserDto.getZoneCode(), supportedLang.split(",")[0]);

			zu = zoneUserRepo.create(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(zu.getCreatedDateTime());
			zoneUserHistoryRepo.create(zuh);
		} catch (IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.CREATE_ERROR_AUDIT, ZoneUser.class.getSimpleName()),
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
		ZoneUserExtnDto zoneUserDto1 = new ZoneUserExtnDto();
		return MapperUtils.map(zu, zoneUserDto1);
	}

	@Override
	public ZoneUserExtnDto updateZoneUserMapping(ZoneUserPutDto zoneUserDto) {
		ZoneUser zu = null;
		ZoneUserExtnDto dto = new ZoneUserExtnDto();
		try {
			zu = zoneUserRepo.findByUserId(zoneUserDto.getUserId());
			if (zu == null) {
				auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
								ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage());
			} else {
				deleteZoneUserMapping(zu.getUserId(), zu.getZoneCode());
			}

			// Throws exception if not found
			zoneservice.getZone(zoneUserDto.getZoneCode(), supportedLang.split(",")[0]);
			zu = MetaDataUtils.setUpdateMetaData(zoneUserDto, zu, false);
			zu = zoneUserRepo.update(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(zu.getUpdatedDateTime());
			zoneUserHistoryRepo.create(zuh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
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
	public IdResponseDto deleteZoneUserMapping(String userId, String zoneCode) {
		IdResponseDto idResponse = new IdResponseDto();
		try {
			List<ZoneUser> zu = zoneUserRepo.findByUserIdAndZoneCode(userId, zoneCode);
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

			});
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException e) {
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
		List<ZoneUserSearchDto> zoneSearch = new ArrayList<>();
		for (int i = 0; i < searchDto.getFilters().size(); i++) {
			if (searchDto.getFilters().get(i).getColumnName().equalsIgnoreCase("userName")) {
				String userId = getUserDetailsBasedonUserName(searchDto.getFilters().get(i).getValue());
				if (null == userId)
					return pageDto;
				searchDto.getFilters().get(i).setValue(userId);
				if (!userId.contains(",")) {

					searchDto.getFilters().get(i).setType(FilterTypeEnum.EQUALS.toString());
				} else {

					searchDto.getFilters().get(i).setType(FilterTypeEnum.IN.toString());
				}
				searchDto.getFilters().get(i).setColumnName("userId");

			}
			if (searchDto.getFilters().get(i).getColumnName().equalsIgnoreCase("zoneName")) {
				String zoneCodes = getZoneCode(searchDto.getFilters().get(i).getValue());
				if (null == zoneCodes)
					return pageDto;
				searchDto.getFilters().get(i).setValue(zoneCodes);
				if (!zoneCodes.contains(",")) {
					searchDto.getFilters().get(i).setType(FilterTypeEnum.EQUALS.toString());
				} else {
					searchDto.getFilters().get(i).setType(FilterTypeEnum.IN.toString());
				}
				searchDto.getFilters().get(i).setColumnName("zoneCode");
			}
		}
		Page<ZoneUser> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(ZoneUser.class, searchDto, null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			zoneUserSearchDetails = MapperUtils.mapAll(page.getContent(), ZoneUserExtnDto.class);
			pageDto = PageUtils.pageResponse(page);
			zoneUserSearchDetails.forEach(z -> {
				ZoneUserSearchDto dto = new ZoneUserSearchDto();
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
				if (null != z.getUserId()) {
					dto.setUserName(getUserName(z.getUserId()));

				} else
					dto.setUserName(null);
				if (null != z.getZoneCode()) {
					ZoneNameResponseDto zn = zoneservice.getZone(z.getZoneCode(), LanguageUtils.getLangCode());
					dto.setZoneName(null != zn ? zn.getZoneName() : null);
				} else
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

	private String getUserName(String userId) {

		HttpHeaders h = new HttpHeaders();
		h.setContentType(MediaType.APPLICATION_JSON);
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(userDetails + "/admin");// .queryParam("appid",
																										// "admin");
		List<String> userDetails = new ArrayList<>();
		userDetails.add(userId);
		RequestWrapper<Map<String, List<String>>> r = new RequestWrapper<>();
		Map<String, List<String>> m = new HashMap();
		m.put("userDetails", userDetails);
		r.setRequest(m);
		HttpEntity<RequestWrapper<Map<String, List<String>>>> httpReq = new HttpEntity<RequestWrapper<Map<String, List<String>>>>(
				r, h);
		ResponseEntity<String> response = restTemplate.exchange(uribuilder.toUriString(), HttpMethod.POST, httpReq,
				String.class);
		response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map m1 = mapper.readValue(response.getBody(), Map.class);
			return ((Map<String, List<Map<String, String>>>) m1.get("response")).get("mosipUserDtoList").get(0)
					.get("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String getUserDetailsBasedonUserName(String userName) {
		String[] nameArray = userName.split(" ");
		HttpHeaders h = new HttpHeaders();
		h.setContentType(MediaType.APPLICATION_JSON);
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(userDetails + "/admin")
				.queryParam("firstName", nameArray[0]);
		HttpEntity<RequestWrapper> httpReq = new HttpEntity<>(null, h);
		ResponseEntity<String> response = restTemplate.exchange(uribuilder.toUriString(), HttpMethod.GET, httpReq,
				String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map m1 = mapper.readValue(response.getBody(), Map.class);
			// List<String> userId = new ArrayList<>();
			String userId = new String();
			List<Map<String, String>> m = ((Map<String, List<Map<String, String>>>) m1.get("response"))
					.get("mosipUserDtoList");
			if (m.size() == 1)
				return m.get(0).get("userId");
			for (int i = 0; i < m.size(); i++) {
				userId = userId + m.get(i).get("userId") + ",";
			}
			return userId;
		} catch (Exception e) {
			e.printStackTrace();// TODO
		}

		return null;

	}

	private String getZoneCode(String zoneName) {
		List<Zone> zones = zoneservice.getZoneListBasedonZoneName(zoneName);
		String zoneCodes = new String();
		for (int i = 0; i < zones.size(); i++) {
			zoneCodes = zoneCodes + zones.get(i).getCode() + ",";
		}
		return zoneCodes;
	}

	@Override
	public List<ZoneUser> getZoneUsersBasedOnZoneCode(String zoneCode) {

		return zoneUserRepo.findZoneByZoneCodeActiveAndNonDeleted(zoneCode.toLowerCase());
	}

}
