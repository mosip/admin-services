package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.entity.*;
import io.mosip.kernel.masterdata.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserHistoryRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

@Component
public class ZoneUserServiceImpl implements ZoneUserService {

	private static final Logger logger = LoggerFactory.getLogger(ZoneUserServiceImpl.class);
	private static final String USERNAME_FORMAT = "%s (%s)";

	@Autowired
	UserDetailsService userDetailservice;

	@Autowired
	ZoneUtils zoneUtils;

	@Autowired
	ZoneService zoneservice;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private MasterdataSearchHelper masterDataSearchHelper;

	@Autowired
	private LanguageUtils languageUtils;

	@Autowired
	ZoneUserRepository zoneUserRepo;

	@Autowired
	UserDetailsRepository userDetailsRepo;

	@Value("${zone.user.details.url}")
	private String userDetails;

	@Autowired
	ZoneUserHistoryRepository zoneUserHistoryRepo;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private AuditUtil auditUtil;

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public ZoneUserExtnDto createZoneUserMapping(ZoneUserDto zoneUserDto) {
		ZoneUser zu = new ZoneUser();
		try {
			validateZone(zoneUserDto.getZoneCode(),zoneUserDto.getLangCode());
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
			if (!zoneUserRepo.findByUserIdNonDeleted(zoneUserDto.getUserId()).isEmpty()) {
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
			zoneservice.getZone(zoneUserDto.getZoneCode(), languageUtils.getDefaultLanguage());

			zu = zoneUserRepo.save(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			zoneUserHistoryRepo.create(zuh);
		} catch (IllegalArgumentException | SecurityException e) {
			logger.error(ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage(), e);
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
		ZoneUser zu;
		ZoneUserExtnDto dto = new ZoneUserExtnDto();
		try {
			validateZone(zoneUserDto.getZoneCode(), zoneUserDto.getLangCode());
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
				UserDetails ud = userDetailsRepo.findByIdAndIsDeletedFalseorIsDeletedIsNull(zoneUserDto.getUserId());
				if (ud != null) {
					throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorCode(),
							ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorMessage());
				}
			}

			zu = MetaDataUtils.setUpdateMetaData(zoneUserDto, zu, false);
			zu = zoneUserRepo.update(zu);
			ZoneUserHistory zuh = new ZoneUserHistory();
			MapperUtils.map(zu, zuh);
			MapperUtils.setBaseFieldValue(zu, zuh);
			zuh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			zoneUserHistoryRepo.create(zuh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException e) {
			logger.error(ZoneUserErrorCode.USER_MAPPING_EXCEPTION.getErrorMessage(), e);
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
			if (zu == null || zu.isEmpty()) {
				auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
								ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage()));
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_NOT_PRSENT_IN_DB.getErrorMessage());
			}

			UserDetails ud=userDetailsRepo.findByIdAndIsDeletedFalseorIsDeletedIsNullAndIsActive(userId);
			if(ud!=null) {
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorMessage());
			}
			zoneUserRepo.deleteZoneUser(userId,LocalDateTime.now(),MetaDataUtils.getContextUser());
			ZoneUserHistory udh = new ZoneUserHistory();
			MapperUtils.map(zu.get(0), udh);
			MapperUtils.setBaseFieldValue(zu.get(0), udh);
			udh.setIsActive(false);
			udh.setIsDeleted(true);
			udh.setUpdatedBy(MetaDataUtils.getContextUser());
			udh.setDeletedDateTime(LocalDateTime.now(ZoneId.of("UTC")));
			udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			zoneUserHistoryRepo.create(udh);

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
			UserDetails ud=userDetailsRepo.findByIdAndIsDeletedFalseorIsDeletedIsNullAndIsActive(userId);
			if(ud!=null) {
				throw new MasterDataServiceException(ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorCode(),
						ZoneUserErrorCode.USER_MAPPING_EXIST.getErrorMessage());
			}
			masterdataCreationUtil.updateMasterDataStatus(ZoneUser.class, userId, isActive, "userId");

			ZoneUserHistory zoneUserHistory = new ZoneUserHistory();
			MetaDataUtils.setUpdateMetaData(zoneUser, zoneUserHistory, true);
			zoneUserHistory.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			zoneUserHistory.setIsActive(isActive);
			zoneUserHistoryRepo.create(zoneUserHistory);

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
		List<Zone> zones = null;
		List<SearchFilter> zoneFilter = new ArrayList<>();
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
		zones = zoneUtils.getSubZones(searchDto.getLanguageCode());
		if (zones != null && !zones.isEmpty()) {
			zoneFilter.addAll(buildZoneFilter(zones));
		}
		OptionalFilter zoneOptionalFilter = new OptionalFilter(zoneFilter);
		Page<ZoneUser> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(ZoneUser.class, searchDto, new OptionalFilter[] { zoneOptionalFilter });
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
				String username = getUserName(z.getUserId());
				dto.setUserName(username == null || username.isBlank() ? z.getUserId() :
						String.format(USERNAME_FORMAT, z.getUserId(), username));

				if (null != z.getZoneCode()) {
					ZoneNameResponseDto zn = zoneservice.getZone(z.getZoneCode(),searchDto.getLanguageCode());
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

		if(userId == null || userId.trim().isEmpty())
			return null;

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
		try {
			Map m1 = mapper.readValue(response.getBody(), Map.class);
			List<Map<String, String>> list = ((Map<String, List<Map<String, String>>>) m1.get("response"))
					.get("mosipUserDtoList");
			return list.isEmpty() ? null : list.get(0).get("name");

		} catch (Exception e) {
			logger.error("failed to get user name from authmanager", e);
		}
		return null;

	}

	private String getUserDetailsBasedonUserName(String userName) {
		HttpHeaders h = new HttpHeaders();
		h.setContentType(MediaType.APPLICATION_JSON);
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(userDetails + "/admin")
				.queryParam("search", userName);
		HttpEntity<RequestWrapper> httpReq = new HttpEntity<>(null, h);
		ResponseEntity<String> response = restTemplate.exchange(uribuilder.toUriString(), HttpMethod.GET, httpReq,
				String.class);

		List<String> userIds = new ArrayList<>();
		try {
			Map m1 = mapper.readValue(response.getBody(), Map.class);
			List<Map<String, String>> m = ((Map<String, List<Map<String, String>>>) m1.get("response"))
					.get("mosipUserDtoList");

			for (int i = 0; i < m.size(); i++) {
				userIds.add(m.get(i).get("userId"));
			}
		} catch (Exception e) {
			logger.error("failed to get userid from authmanager", e);
		}
		return userIds.isEmpty() ? null : String.join(",", userIds);
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
	private void validateZone(String zoneCode,String langCode) {
		List<String> zoneIds;
		if(langCode==null)
			langCode=languageUtils.getDefaultLanguage();
		// get user zone and child zones list
		List<Zone> subZones = zoneUtils.getSubZones(langCode);

		zoneIds = subZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());

		if (!(zoneIds.contains(zoneCode))) {
			// check the
			// given device zones will come under accessed user zones
			throw new RequestException(ZoneUserErrorCode.INVALID_ZONE.getErrorCode(),
					ZoneUserErrorCode.INVALID_ZONE.getErrorMessage());
		}
	}
	/**
	 * Creating Search filter from the passed zones
	 *
	 * @param zones filter to be created with the zones
	 * @return list of {@link SearchFilter}
	 */
	public List<SearchFilter> buildZoneFilter(List<Zone> zones) {
		if (zones != null && !zones.isEmpty()) {
			return zones.stream().filter(Objects::nonNull).map(Zone::getCode).distinct().map(this::buildZoneFilter)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
	/**
	 * Method to create SearchFilter for the recieved zoneCode
	 *
	 * @param zoneCode input from the {@link SearchFilter} has to be created
	 * @return {@link SearchFilter}
	 */
	private SearchFilter buildZoneFilter(String zoneCode) {
		SearchFilter filter = new SearchFilter();
		filter.setColumnName(MasterDataConstant.ZONE_CODE);
		filter.setType(FilterTypeEnum.EQUALS.name());
		filter.setValue(zoneCode);
		return filter;
	}
}
