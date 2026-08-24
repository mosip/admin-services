package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.signatureutil.exception.ParseResponseException;
import io.mosip.kernel.core.util.StringUtils;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UserDetailsErrorCode;
import io.mosip.kernel.masterdata.dto.PageDto;
import io.mosip.kernel.masterdata.dto.SearchDtoWithoutLangCode;
import io.mosip.kernel.masterdata.dto.UserDetailsCenterMapping;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UserDetailsGetExtnDto;
import io.mosip.kernel.masterdata.dto.UserDetailsPutDto;
import io.mosip.kernel.masterdata.dto.UserDetailsPutReqDto;
import io.mosip.kernel.masterdata.dto.UsersDto;
import io.mosip.kernel.masterdata.dto.ZoneUserExtnDto;
import io.mosip.kernel.masterdata.dto.ZoneUserSearchDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserCenterMappingExtnDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.UserDetailsHistory;
import io.mosip.kernel.masterdata.entity.Zone;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.ValidationException;
import io.mosip.kernel.masterdata.repository.UserDetailsHistoryRepository;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.repository.ZoneUserRepository;
import io.mosip.kernel.masterdata.service.RegistrationCenterService;
import io.mosip.kernel.masterdata.service.UserDetailsHistoryService;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	private static final String USERNAME_FORMAT = "%s (%s)";

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	UserDetailsHistoryRepository history;

	@Autowired
	LanguageUtils languageUtils;

	@Autowired
	UserDetailsHistoryService userDetailsHistoryService;
	
	@Value("${zone.user.details.url}")
	private String userDetails;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	RegistrationCenterRepository registrationCenterRepository;

	@Autowired
	private MasterdataSearchHelper masterDataSearchHelper;

	@Autowired
	private PageUtils pageUtils;

	@Autowired
	private ZoneUtils zoneUtils;

	@Autowired
	RegistrationCenterService registrationCenterService;

	@Autowired
	private AuditUtil auditUtil;

	@Autowired
	private ObjectMapper objectMapper;

	/** restemplate instance. */
	@Autowired
	private RestTemplate restTemplate;

	/** Base end point read from property file. */
	@Value("${mosip.kernel.masterdata.auth-manager-base-uri}")
	private String authBaseUrl;

	/** all roles end-point read from properties file. */
	@Value("${mosip.kernel.masterdata.auth-user-details:/userdetails}")
	private String authServiceName;

	/** admin realm id. */
	@Value("${mosip.iam.admin-realm-id:admin}")
	private String adminRealmId;

	@Value("${mosip.keycloak.max-no-of-users:100}")
	private String maxUsers;

	@Autowired
	ZoneUserRepository zoneUserRepository;

	@Autowired
	private ExecutorService enrichmentExecutor;

	@Value("${mosip.kernel.masterdata.enrichment.executor.timeout-seconds:30}")
	private int enrichmentExecutorTimeoutSeconds;



	@Override
	public UserDetailsGetExtnDto getUser(String id) {
		UserDetails ud = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);
		ZoneUser zu = zoneUserRepository.findZoneByUserIdNonDeleted(id);

		if (ud != null) {
			UserDetailsGetExtnDto userDetailsExtnDto = MapperUtils.map(ud, UserDetailsGetExtnDto.class);
			if (zu != null) {
				userDetailsExtnDto.setZoneCode(zu.getZoneCode());
			}
			return userDetailsExtnDto;
		} else {
			throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
		}
	}

	@Override
	public PageDto<UserDetailsExtnDto> getUsers(int pageNumber, int pageSize, String sortBy, String direction) {
		List<UserDetailsExtnDto> userDetails = null;
		PageDto<UserDetailsExtnDto> pageDto = null;
		try {
			Page<UserDetails> pageData = userDetailsRepository.findAllByIsDeletedFalseorIsDeletedIsNull(
					PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(direction), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				userDetails = MapperUtils.mapAll(pageData.getContent(), UserDetailsExtnDto.class);
				pageDto = new PageDto<>(pageData.getNumber(), pageSize, pageData.getSort(), pageData.getTotalElements(),
						pageData.getTotalPages(), getZonesForUsers(userDetails));
			} else {
				throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		return pageDto;
	}

	private List<UserDetailsExtnDto> getZonesForUsers(List<UserDetailsExtnDto> userDetails) {
		List<UserDetailsExtnDto> mappedUserDetails = new ArrayList<UserDetailsExtnDto>();
		List<ZoneUser> zoneUsers = zoneUserRepository.findByUserIds(userDetails.stream().map(UserDetailsExtnDto::getId).collect(Collectors.toList()));
		for (UserDetailsExtnDto userDetail : userDetails) {
			ZoneUser mappedZone = zoneUsers.stream().filter(us -> us.getUserId().equals(userDetail.getId())).findFirst()
					.orElse(null);
			if (mappedZone != null) {
				userDetail.setZoneCode(mappedZone.getZoneCode());
				mappedUserDetails.add(userDetail);
			} else {
				mappedUserDetails.add(userDetail);
			}
		}
		return mappedUserDetails;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IdResponseDto deleteUser(String id) {
		IdResponseDto idResponse = new IdResponseDto();
		try {
			UserDetails userDetails = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);
			if (userDetails == null)
				throw new MasterDataServiceException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
			userDetailsRepository.deleteUserCenterMapping(id,LocalDateTime.now(),MetaDataUtils.getContextUser());
			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(userDetails, udh);
			MapperUtils.setBaseFieldValue(userDetails, udh);
			udh.setIsActive(false);
			udh.setIsDeleted(true);
			udh.setCreatedBy(MetaDataUtils.getContextUser());
			udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			userDetailsHistoryService.createUserDetailsHistory(udh);
			MapperUtils.map(userDetails, idResponse);

		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage()),"ADM-957");
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(
				String.format(MasterDataConstant.DECOMMISSION_SUCCESS, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.DECOMMISSION_SUCCESS, idResponse.getId()),"ADM-958");
		return idResponse;

	}

	@Override
	public List<UserDetailsExtnDto> getUsersByRegistrationCenter(String regCenterId, int pageNumber, int pageSize,
			String sortBy, String orderBy) {
		List<UserDetailsExtnDto> userDetails = null;
		try {
			List<UserDetails> pageData = userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(
					regCenterId, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
			if (pageData != null && !pageData.isEmpty()) {
				userDetails = MapperUtils.mapAll(pageData, UserDetailsExtnDto.class);
			} else {
				throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessException | DataAccessLayerException exception) {
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(exception));
		}
		return userDetails;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetailsCenterMapping createUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		List<RegistrationCenter> regCenters;
		ZoneUser zoneUser = zoneUserRepository.findZoneByUserIdActiveAndNonDeleted(userDetailsDto.getId());
		if(zoneUser == null)
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorMessage());

		try {
			UserDetails result = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(userDetailsDto.getId());
			if (result!=null)
				throw new MasterDataServiceException(UserDetailsErrorCode.USER_ALREADY_EXISTS.getErrorCode(),
						UserDetailsErrorCode.USER_ALREADY_EXISTS.getErrorMessage());

			regCenters = registrationCenterService.getRegistrationCentersByID(userDetailsDto.getRegCenterId());
			if (regCenters == null || regCenters.isEmpty()) {
				auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage()),"ADM-959");
				throw new MasterDataServiceException(UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
						UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage());
			}

			validateMappingData(userDetailsDto.getId(), regCenters.get(0).getZoneCode(), userDetailsDto.getLangCode());

			userDetailsDto = masterdataCreationUtil.createMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			userDetailsRepository.save(ud);

			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(ud, udh);
			MapperUtils.setBaseFieldValue(ud, udh);
			udh.setIsActive(true);
			udh.setCreatedBy(MetaDataUtils.getContextUser());
			udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			userDetailsHistoryService.createUserDetailsHistory(udh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()),"ADM-960");
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		UserDetailsGetExtnDto userDetailsGetExtnDto = new UserDetailsGetExtnDto();
		MapperUtils.map(ud, userDetailsGetExtnDto);
		UserDetailsCenterMapping uc = new UserDetailsCenterMapping();
		uc.buildUserDetailsGetExtDto(userDetailsGetExtnDto);
		if (null != regCenters && regCenters.size() > 0) {

			if (null != userDetailsDto.getLangCode()) {
				final String langCode = userDetailsDto.getLangCode();
				regCenters.forEach(i -> {
					if (i.getLangCode().equalsIgnoreCase(langCode)) {
						uc.setRegCenterName(i.getName());
						uc.setZoneName(zoneUtils.getZoneBasedOnZoneCodeLanguage(i.getZoneCode(), langCode).getName());
						uc.setZoneCode(i.getZoneCode());

					}
				});
			} else {
				uc.setRegCenterName(regCenters.get(0).getName());
				uc.setZoneName(zoneUtils.getZoneBasedOnZoneCodeLanguage(regCenters.get(0).getZoneCode(), regCenters.get(0).getLangCode())
						.getName());
				uc.setZoneCode(regCenters.get(0).getZoneCode());
			}

		} else {
			uc.setRegCenterName(null);
			uc.setZoneCode(null);
			uc.setZoneName(null);

		}
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						UserDetails.class.getSimpleName(), userDetailsGetExtnDto.getId()),"ADM-961");
		return uc;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetailsPutDto updateUser(UserDetailsPutReqDto userDetailsDto) {
		UserDetails ud = null;
		UserDetailsDto udd=new UserDetailsDto();
		UserDetailsPutDto userDetailsPutDto = new UserDetailsPutDto();

		ZoneUser zoneUser = zoneUserRepository.findZoneByUserIdActiveAndNonDeleted(userDetailsDto.getId());
		if(zoneUser == null)
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorMessage());

		try {
			ud = userDetailsRepository
					.findByIdAndIsDeletedFalseorIsDeletedIsNull(userDetailsDto.getId());
			if (ud == null) {
				userDetailsDto = masterdataCreationUtil.createMasterData(UserDetails.class, userDetailsDto);
				ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
				ud.setIsActive(false);
				userDetailsRepository.save(ud);
			}
			List<RegistrationCenter> regCenters = registrationCenterService
					.getRegistrationCentersByID(userDetailsDto.getRegCenterId()); // Throws exception if not found
			if (regCenters == null || regCenters.isEmpty()) {
				auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage()),"ADM-962");
				throw new MasterDataServiceException(UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
						UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage());
			}

			validateMappingData(userDetailsDto.getId(), regCenters.get(0).getZoneCode(), userDetailsDto.getLangCode());

			userDetailsDto = masterdataCreationUtil.updateMasterData(UserDetails.class, userDetailsDto);

			userDetailsPutDto.setId(userDetailsDto.getId());
			userDetailsPutDto.setIsActive(userDetailsDto.getIsActive());
			userDetailsPutDto.setLangCode(userDetailsDto.getLangCode());
			userDetailsPutDto.setName(userDetailsDto.getName());
			userDetailsPutDto.setStatusCode(userDetailsDto.getStatusCode());
			userDetailsPutDto.setRegCenterId(userDetailsDto.getRegCenterId());

			if (null != regCenters && regCenters.size() > 0) {

				if (null != userDetailsDto.getLangCode()) {
					final String langCode = userDetailsDto.getLangCode();
					regCenters.forEach(i -> {
						if (i.getLangCode().equalsIgnoreCase(langCode)) {
							userDetailsPutDto.setRegCenterName(i.getName());
							userDetailsPutDto.setZoneName(zoneUtils.getZoneBasedOnZoneCodeLanguage(i.getZoneCode(), langCode).getName());
							userDetailsPutDto.setZoneCode(i.getZoneCode());

						}
					});
				} else {
					userDetailsPutDto.setRegCenterName(regCenters.get(0).getName());
					userDetailsPutDto.setZoneName(zoneUtils
							.getZoneBasedOnZoneCodeLanguage(regCenters.get(0).getZoneCode(), regCenters.get(0).getLangCode()).getName());
					userDetailsPutDto.setZoneCode(regCenters.get(0).getZoneCode());
				}

			}

			else {
				userDetailsPutDto.setZoneCode(null);
				userDetailsPutDto.setZoneName(null);
				userDetailsPutDto.setRegCenterName(null);
			}
			ud = MetaDataUtils.setUpdateMetaData(userDetailsDto, ud, false);
			userDetailsRepository.update(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(ud, udh);
			MapperUtils.setBaseFieldValue(ud, udh);
			udh.setIsActive(ud.getIsActive());
			udh.setCreatedBy(MetaDataUtils.getContextUser());
			udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
			userDetailsHistoryService.createUserDetailsHistory(udh);
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()),"ADM-978");
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
						UserDetails.class.getSimpleName(), ud.getId()),"ADM-963");

		return userDetailsPutDto;
	}

	/*
	 * private UserDetailsDto getDto(UserDetails ud){ UserDetailsDto udDto = new
	 * UserDetailsDto(); udDto.setId(ud.getId());
	 * udDto.setRegCenterId(ud.getRegCenterId());
	 * udDto.setIsActive(ud.getIsActive()); udDto.setLangCode(ud.getLangCode());
	 * return udDto; }
	 */

	/**
	 * 
	 */
	@Transactional
	@Override
	public StatusResponseDto updateUserStatus(String id, boolean isActive) {
		StatusResponseDto response = new StatusResponseDto();
		ZoneUser zoneUser = zoneUserRepository.findZoneByUserIdNonDeleted(id);
		if(zoneUser == null)
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorMessage());

		if(isActive && !zoneUser.getIsActive())
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorMessage());

		try {
			UserDetails userDetails = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);
			if (userDetails != null) {

				if(isActive) {
					List<RegistrationCenter> regCenters = registrationCenterService
							.getRegistrationCentersByID(userDetails.getRegCenterId());
					validateMappingData(id, regCenters.get(0).getZoneCode(), null);
				}

				masterdataCreationUtil.updateMasterDataStatus(UserDetails.class, id, isActive, "id");

				UserDetailsHistory udh = new UserDetailsHistory();
				MetaDataUtils.setUpdateMetaData(userDetails, udh, true);
				udh.setIsActive(isActive);
				udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
				userDetailsHistoryService.createUserDetailsHistory(udh);

			} else {
				throw new MasterDataServiceException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException e) {
			auditUtil.auditRequest(String.format(MasterDataConstant.FAILURE_UPDATE, ZoneUser.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC, UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
							UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage()),
					"ADM-836");
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
		}
		response.setStatus("Status updated successfully for User");
		return response;
	}

	@Override
	public UsersDto getUsers(String roleName, int pageStart, int pageFetch, String firstName,
			String lastName, String userName) {
		ResponseEntity<String> response = null;
		UriComponentsBuilder uriComponentsBuilder = null;
		try {
			uriComponentsBuilder = UriComponentsBuilder
					.fromUriString(authBaseUrl + authServiceName + "/" + adminRealmId);
			if (StringUtils.isNotBlank(roleName)) {
				uriComponentsBuilder.queryParam("roleName", roleName);
			}
			if (StringUtils.isNotBlank(firstName)) {
				uriComponentsBuilder.queryParam("firstName", firstName);
			}
			if (StringUtils.isNotBlank(lastName)) {
				uriComponentsBuilder.queryParam("lastName", lastName);
			}
			if (StringUtils.isNotBlank(userName)) {
				uriComponentsBuilder.queryParam("userName", userName);
			}
			uriComponentsBuilder.queryParam("pageStart", pageStart);
			uriComponentsBuilder.queryParam("pageFetch", pageFetch == 0 ? maxUsers : pageFetch);
			HttpEntity<RequestWrapper<?>> httpRequest = getHttpRequest();
			response = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, httpRequest,
					String.class);
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			List<ServiceError> validationErrorsList = io.mosip.kernel.core.exception.ExceptionUtils
					.getServiceErrorList(ex.getResponseBodyAsString());
			if (ex.getRawStatusCode() == 401) {
				if (!validationErrorsList.isEmpty()) {
					throw new AuthNException(validationErrorsList);
				} else {
					throw new BadCredentialsException("Authentication failed from AuthManager");
				}
			}
			if (ex.getRawStatusCode() == 403) {
				if (!validationErrorsList.isEmpty()) {
					throw new AuthZException(validationErrorsList);
				} else {
					throw new AccessDeniedException("Access denied from AuthManager");
				}
			}
			auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
							UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()),"ADM-964");
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage());
		}
		return getUsersFromResponse(response);

	}

	/**
	 * 
	 * @return
	 */
	private HttpEntity<RequestWrapper<?>> getHttpRequest() {
		RequestWrapper<?> requestWrapper = new RequestWrapper<>();
		HttpHeaders rolesHttpHeaders = new HttpHeaders();
		rolesHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(requestWrapper, rolesHttpHeaders);
	}

	private UsersDto getUsersFromResponse(ResponseEntity<String> response) {
		String responseBody = response.getBody();
		List<ServiceError> validationErrorsList = null;
		UsersDto usersDto = null;
		validationErrorsList = io.mosip.kernel.core.exception.ExceptionUtils.getServiceErrorList(responseBody);
		if (!validationErrorsList.isEmpty()) {
			throw new ValidationException(validationErrorsList);
		}
		ResponseWrapper<?> responseObject = null;
		try {
			responseObject = objectMapper.readValue(response.getBody(), ResponseWrapper.class);
			usersDto = objectMapper.readValue(objectMapper.writeValueAsString(responseObject.getResponse()),
					UsersDto.class);
		} catch (IOException | NullPointerException exception) {
			auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
							UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage()),"ADM-965");
			throw new ParseResponseException(UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage() + exception.getMessage(),
					exception);
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL_SUCCESS, UsersDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.GET_ALL_SUCCESS_DESC, UsersDto.class.getSimpleName()),"ADM-966");

		if(usersDto.getMosipUserDtoList() != null) {
			usersDto.getMosipUserDtoList().forEach(dto -> {
				String name = dto.getName();
				dto.setName(name == null || name.isBlank() ?
						dto.getUserId() :
						String.format(USERNAME_FORMAT, dto.getUserId(), name));
			});
		}
		return usersDto;
	}

	@Override
	public PageResponseDto<UserDetailsExtnDto> searchUserDetails(SearchDtoWithoutLangCode searchDto) {
		PageResponseDto<UserDetailsExtnDto> pageDto = new PageResponseDto<>();
		List<UserDetailsExtnDto> userDetails = null;

		for (SearchFilter sf : searchDto.getFilters()) {
			if (sf.getColumnName().equalsIgnoreCase("zoneCode")) {
				List<ZoneUser> zu = zoneUserRepository.findZoneByZoneCodeActiveAndNonDeleted(sf.getValue());
				String userIds = new String();
				for (int i = 0; i < zu.size(); i++) {
					userIds = userIds + zu.get(i).getUserId() + ",";
				}
				sf.setColumnName("id");
				sf.setType(FilterTypeEnum.IN.toString());
				sf.setValue(userIds);
			}
		}
		Page<UserDetails> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(UserDetails.class, searchDto,
				null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			userDetails = MapperUtils.mapAll(page.getContent(), UserDetailsExtnDto.class);
			pageDto = PageUtils.pageResponse(page);
			pageDto.setData(getZonesForUsers(userDetails));
		}
		return pageDto;
	}
	
	@Override
	public PageResponseDto<UserCenterMappingExtnDto> searchUserCenterMappingDetails(SearchDtoWithoutLangCode searchDto) {
		PageResponseDto<ZoneUserSearchDto> pageDto = new PageResponseDto<>();
		PageResponseDto<UserCenterMappingExtnDto> userCenterPageDto = new PageResponseDto<>();
		List<UserCenterMappingExtnDto> userCenterMappingExtnDtos = null;
		List<Zone> zones = null;
		List<SearchFilter> zoneFilter = new ArrayList<>();
		List<ZoneUserExtnDto> zoneUserSearchDetails = null;
		List<ZoneUserSearchDto> zoneSearch = new ArrayList<>();
		for (int i = 0; i < searchDto.getFilters().size(); i++) {
			if (searchDto.getFilters().get(i).getColumnName().equalsIgnoreCase("userName")) {
				String userId = getUserDetailsBasedonUserName(searchDto.getFilters().get(i).getValue());
				if (null == userId || userId.isBlank())
					return userCenterPageDto;
				searchDto.getFilters().get(i).setValue(userId);
				searchDto.getFilters().get(i).setType(!userId.contains(",")?FilterTypeEnum.EQUALS.toString():FilterTypeEnum.IN.toString());
				searchDto.getFilters().get(i).setColumnName("userId");

			}
			if (searchDto.getFilters().get(i).getColumnName().equalsIgnoreCase("zoneName")) {
				String zoneCodes = getZoneCode(searchDto.getFilters().get(i).getValue());
				if (null == zoneCodes || zoneCodes.isBlank())
					return userCenterPageDto;
				searchDto.getFilters().get(i).setValue(zoneCodes);
				searchDto.getFilters().get(i).setType(!zoneCodes.contains(",")?FilterTypeEnum.EQUALS.toString():FilterTypeEnum.IN.toString());
				searchDto.getFilters().get(i).setColumnName("zoneCode");
			}
			if (searchDto.getFilters().get(i).getColumnName().equalsIgnoreCase("regCenterName")) {

				String userIds = getUserIdBasedOnRegistrationCenter(searchDto.getFilters().get(i).getValue());
				if (null == userIds || userIds.isEmpty())
					return userCenterPageDto;
				searchDto.getFilters().get(i).setValue(userIds);
				searchDto.getFilters().get(i).setType(!userIds.contains(",")?FilterTypeEnum.EQUALS.toString():FilterTypeEnum.IN.toString());
				searchDto.getFilters().get(i).setColumnName("userId");

			}
		}
		zones = zoneUtils.getSubZones(searchDto.getLanguageCode());
		if (zones != null && !zones.isEmpty()) {
			zoneFilter.addAll(buildZoneFilter(zones));
		}
		OptionalFilter zoneOptionalFilter = new OptionalFilter(zoneFilter);
		Page<ZoneUser> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(ZoneUser.class, searchDto,
				new OptionalFilter[] { zoneOptionalFilter });
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			zoneUserSearchDetails = MapperUtils.mapAll(page.getContent(), ZoneUserExtnDto.class);
			// recover Users Ids for batch user's names search
			final List<String> usersIds = zoneUserSearchDetails.stream().map(ZoneUserExtnDto::getUserId).toList();
			// recover Users ZonesCodes for batch zones search
			final List<String> usersZonesCodes   = zoneUserSearchDetails.stream().map(
					ZoneUserExtnDto::getZoneCode
			).filter(org.springframework.util.StringUtils::hasText).toList();
			// Launch both calls of batch user's names search and  zone's names search in parallel
			CompletableFuture<Map<String, String>> usersNamesByIdsFuture = CompletableFuture.supplyAsync(
					() -> this.getUsersNames(usersIds),
					this.enrichmentExecutor
			);
			CompletableFuture<List<Zone>> recoveredZonesFuture = CompletableFuture.supplyAsync(
					() -> this.zoneUtils.getZonesByCodesAndLanguage(usersZonesCodes, searchDto.getLanguageCode()),
					this.enrichmentExecutor
			);
			// Retrieve results (blocks here until available)
			final Map<String, String> usersNamesByIds = usersNamesByIdsFuture.orTimeout(
					this.enrichmentExecutorTimeoutSeconds,
					TimeUnit.SECONDS
			).exceptionally(ex -> {
				logger.error("Failed to fetch user names in batch", ex);
				return Collections.emptyMap();
			}).join();
			final List<Zone> recoveredZones = recoveredZonesFuture.orTimeout(
					this.enrichmentExecutorTimeoutSeconds,
					TimeUnit.SECONDS
			).exceptionally(ex -> {
				logger.error("Failed to fetch zones in batch", ex);
				return Collections.emptyList();
			}).join();
			// Complete page result construction
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
				String username = usersNamesByIds.getOrDefault(z.getUserId(), null);
				dto.setUserName(username == null || username.isBlank() ? z.getUserId() :
						String.format(USERNAME_FORMAT, z.getUserId(), username));

				if (null != z.getZoneCode()) {
					Zone zn = recoveredZones.stream().filter(
							rzn -> z.getZoneCode().equals(rzn.getCode())
					).findFirst().orElse(null);
					dto.setZoneName(null != zn ? zn.getName() : null);
				} else
					dto.setZoneName(null);
				zoneSearch.add(dto);
			});
			userCenterMappingExtnDtos = dtoMapper(zoneSearch, searchDto.getLanguageCode());
			userCenterPageDto.setFromRecord(pageDto.getFromRecord());
			userCenterPageDto.setToRecord(pageDto.getToRecord());
			userCenterPageDto.setTotalRecord(pageDto.getTotalRecord());
			userCenterPageDto.setData(userCenterMappingExtnDtos);
		}
		return userCenterPageDto;
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
			if(!m1.isEmpty()) {
				List<Map<String, String>> list = ((Map<String, List<Map<String, String>>>) m1.get("response")).get("mosipUserDtoList");
				return list == null || list.isEmpty() ? null : list.get(0).get("name");
			}
		} catch (Exception e) {
			logger.error("Failed to fetch username", e);
		}
		return null;

	}

	private Map<String, String> getUsersNames(List<String> usersIds) {
		if(usersIds == null || usersIds.isEmpty()) {
			return Collections.emptyMap();
		}
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(userDetails + "/admin");
		final List<String> userDetails = new ArrayList<>(usersIds);
		final RequestWrapper<Map<String, List<String>>> requestWrapper = new RequestWrapper<>();
		final Map<String, List<String>> requestEntry = new HashMap<>();
		requestEntry.put("userDetails", userDetails);
		requestWrapper.setRequest(requestEntry);
		final HttpEntity<RequestWrapper<Map<String, List<String>>>> httpEntity = new HttpEntity<>(
				requestWrapper,
				headers
		);
		ResponseEntity<String> response = restTemplate.exchange(
				uribuilder.toUriString(),
				HttpMethod.POST,
				httpEntity,
				String.class
		);
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final Map responseBody = mapper.readValue(response.getBody(), Map.class);
			List<Map<String, String>> usersListResponse = (
					(Map<String, List<Map<String, String>>>) responseBody.get("response")
			).get("mosipUserDtoList");
			if (usersListResponse.isEmpty()) {
				return Collections.emptyMap();
			} else {
				return usersListResponse.stream().collect(Collectors.toMap(
						entry -> entry.get("userId"),
						entry -> entry.get("name")
				));
			}
		} catch (Exception e) {
			logger.error("failed to get users names from authmanager", e);
		}
		return Collections.emptyMap();
	}
	
	private List<UserCenterMappingExtnDto> dtoMapper(List<ZoneUserSearchDto> zoneUserSearchDtos, String languageCode) {
		List<UserCenterMappingExtnDto> userCenterMappingExtnDtos=new ArrayList();
		mapZoneUserDetailsToUserCenter(zoneUserSearchDtos,userCenterMappingExtnDtos);
		for (UserCenterMappingExtnDto userCenterMappingExtnDto : userCenterMappingExtnDtos) {
			userCenterMappingExtnDto.setIsActive(false);
			UserDetails ud=userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(userCenterMappingExtnDto.getUserId());
				if(ud!=null) {
					if(ud.getIsDeleted() == null || !ud.getIsDeleted()) {
						RegistrationCenter regC=registrationCenterRepository.findByIdAndLangCode(ud.getRegCenterId(),
								languageCode ==null ? languageUtils.getDefaultLanguage() : languageCode);
						userCenterMappingExtnDto.setRegCenterName((regC != null) ?
								String.format("%s (%s)", ud.getRegCenterId(), regC.getName()) :
								ud.getRegCenterId());
						userCenterMappingExtnDto.setRegCenterId(ud.getRegCenterId());
					}
					userCenterMappingExtnDto.setCreatedBy(ud.getCreatedBy());
					userCenterMappingExtnDto.setCreatedDateTime(ud.getCreatedDateTime());
					userCenterMappingExtnDto.setUpdatedBy(ud.getUpdatedBy());
					userCenterMappingExtnDto.setUpdatedDateTime(ud.getUpdatedDateTime());
					userCenterMappingExtnDto.setIsDeleted(ud.getIsDeleted());
					userCenterMappingExtnDto.setDeletedDateTime(ud.getDeletedDateTime());
					userCenterMappingExtnDto.setIsActive(ud.getIsActive());
				}

		}

		return userCenterMappingExtnDtos;
	}

	private void mapZoneUserDetailsToUserCenter(List<ZoneUserSearchDto> zoneUserSearchDtos, List<UserCenterMappingExtnDto> userCenterMappingExtnDtos) {
		zoneUserSearchDtos.forEach(zu->{
			UserCenterMappingExtnDto ucm=new UserCenterMappingExtnDto();
			ucm.setZoneCode(zu.getZoneCode());
			ucm.setZoneName(zu.getZoneName());
			ucm.setUserId(zu.getUserId());
			ucm.setUserName(zu.getUserName());
			ucm.setIsActive(zu.getIsActive());
			ucm.setCreatedBy(zu.getCreatedBy());
			ucm.setCreatedDateTime(zu.getCreatedDateTime());
			ucm.setUpdatedDateTime(zu.getUpdatedDateTime());
			ucm.setUpdatedBy(zu.getUpdatedBy());
			ucm.setDeletedDateTime(zu.getDeletedDateTime());
			ucm.setIsDeleted(zu.getIsDeleted());
			userCenterMappingExtnDtos.add(ucm);
		});
	}

	private String getUserDetailsBasedonUserName(String userName) {
		HttpHeaders h = new HttpHeaders();
		h.setContentType(MediaType.APPLICATION_JSON);
		UriComponentsBuilder uribuilder = UriComponentsBuilder.fromUriString(userDetails + "/admin")
				.queryParam("search", userName);
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
			logger.error("Failed to fetch getUserDetailsBasedonUserName", e);
		}

		return null;

	}
	
	private String getZoneCode(String zoneName) {
		List<Zone> zones = zoneUtils.getZoneListBasedonZoneName(zoneName);
		String zoneCodes = new String();
		for (int i = 0; i < zones.size(); i++) {
			zoneCodes = zoneCodes + zones.get(i).getCode() + ",";
		}
		return zoneCodes;
	}
	private void validateZone(String zoneCode,String langCode) {
		List<String> zoneIds;
		// get user zone and child zones list
		if(langCode==null)
			langCode=languageUtils.getDefaultLanguage();
		List<Zone> subZones = zoneUtils.getSubZones(langCode);

		zoneIds = subZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());

		if (!(zoneIds.contains(zoneCode))) {
			// check the given device zones will come under accessed user zones
			throw new RequestException(UserDetailsErrorCode.INVALID_ZONE.getErrorCode(),
					UserDetailsErrorCode.INVALID_ZONE.getErrorMessage());
		}
	}

	private void validateMappingData(String userId, String regCenterZoneCode, String langCode) {
		//check if zone user belongs to loggedin user subzones
		List<Zone> supportedZones = zoneUtils.getSubZones(langCode);
		ZoneUser zoneUser = zoneUserRepository.findZoneByUserIdActiveAndNonDeleted(userId);
		List<String> zoneIds = supportedZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());
		if(zoneUser == null || !(zoneIds.contains(zoneUser.getZoneCode()))) {
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_FOUND.getErrorMessage());
		}

		//check if center zone of the zones of userzone
		supportedZones = zoneUtils.getLeafZones(langCode, zoneUser.getZoneCode());
		zoneIds = supportedZones.parallelStream().map(Zone::getCode).collect(Collectors.toList());
		if(!(zoneIds.contains(regCenterZoneCode))) {
			throw new MasterDataServiceException(UserDetailsErrorCode.INVALID_ZONE_MAPPING.getErrorCode(),
					UserDetailsErrorCode.INVALID_ZONE_MAPPING.getErrorMessage());
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

	private String getUserIdBasedOnRegistrationCenter(String regCenterName) {
		List<String> userIdLst = registrationCenterRepository
				.findUserIdBasedOnRegistrationCenterName(regCenterName.toLowerCase());

		return userIdLst == null ? "" : String.join(",", userIdLst);
	}

}
