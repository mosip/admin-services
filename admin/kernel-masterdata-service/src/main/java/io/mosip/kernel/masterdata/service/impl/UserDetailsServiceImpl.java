package io.mosip.kernel.masterdata.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.signatureutil.exception.ParseResponseException;
import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.constant.UserDetailsErrorCode;
import io.mosip.kernel.masterdata.dto.PageDto;
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UsersDto;
import io.mosip.kernel.masterdata.dto.ZoneUserDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.request.SearchDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.UserDetailsHistory;
import io.mosip.kernel.masterdata.entity.ZoneUser;
import io.mosip.kernel.masterdata.entity.id.IdAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.ValidationException;
import io.mosip.kernel.masterdata.repository.UserDetailsHistoryRepository;
import io.mosip.kernel.masterdata.repository.UserDetailsRepository;
import io.mosip.kernel.masterdata.service.RegistrationCenterService;
import io.mosip.kernel.masterdata.service.UserDetailsHistoryService;
import io.mosip.kernel.masterdata.service.UserDetailsService;
import io.mosip.kernel.masterdata.service.ZoneUserService;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MasterdataCreationUtil;
import io.mosip.kernel.masterdata.utils.MasterdataSearchHelper;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;
import io.mosip.kernel.masterdata.utils.PageUtils;

/**
 * @author Sidhant Agarwal
 * @since 1.0.0
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	UserDetailsHistoryRepository history;

	@Autowired
	UserDetailsHistoryService userDetailsHistoryService;

	@Autowired
	MasterdataCreationUtil masterdataCreationUtil;

	@Autowired
	private MasterdataSearchHelper masterDataSearchHelper;
	
	@Autowired
	private PageUtils pageUtils;
	
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
	
	@Autowired
	ZoneUserService zoneUserService;
	
	@Override
	public UserDetailsDto getUser(String id) {
		UserDetails ud = userDetailsRepository.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);
		if(ud!=null) {
		return getDto(ud);
		}
		else {
			throw new DataNotFoundException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
					UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());
		}
	}

	@Override
	public PageDto<UserDetailsExtnDto> getUsers(int pageNumber, int pageSize, String sortBy, String direction) {
		List<UserDetailsExtnDto> userDetails = null;
		PageDto<UserDetailsExtnDto> pageDto = null;
		try {
			Page<UserDetails> pageData = userDetailsRepository
					.findAllByIsDeletedFalseorIsDeletedIsNull(PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(direction), sortBy)));
			if (pageData != null && pageData.getContent() != null && !pageData.getContent().isEmpty()) {
				userDetails = MapperUtils.mapAll(pageData.getContent(), UserDetailsExtnDto.class);				
				pageDto = new PageDto<>(pageData.getNumber(), pageSize, pageData.getSort(), pageData.getTotalElements(), pageData.getTotalPages(), getZonesForUsers(userDetails));
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
		List<ZoneUser> zoneUsers = zoneUserService.getZoneUsers(userDetails.stream().map(UserDetailsExtnDto::getId).collect(Collectors.toList()));
		for (UserDetailsExtnDto userDetail : userDetails) {
			ZoneUser mappedZone = zoneUsers.stream().filter(us->us.getUserId().equals(userDetail.getId())).findFirst().orElse(null);
			if(mappedZone != null) {
				userDetail.setZoneCode(mappedZone.getZoneCode());
				mappedUserDetails.add(userDetail);
			}else {
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
			Optional<UserDetails> ud = userDetailsRepository.findById(id);
			ud.ifPresent(user -> {
				userDetailsRepository.delete(user);
				UserDetailsHistory udh = new UserDetailsHistory();
				MapperUtils.map(user, udh);
				MapperUtils.setBaseFieldValue(user, udh);
				udh.setIsActive(false);
				udh.setIsDeleted(true);
				udh.setCreatedBy(MetaDataUtils.getContextUser());
				udh.setEffDTimes(LocalDateTime.now(ZoneId.of("UTC")));
				userDetailsHistoryService.createUserDetailsHistory(udh);
				MapperUtils.map(user, idResponse);
			} );
		} catch (DataAccessLayerException | DataAccessException | IllegalArgumentException 
		 | SecurityException e) {
			auditUtil.auditRequest(
			String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
			MasterDataConstant.AUDIT_SYSTEM,
			String.format(MasterDataConstant.FAILURE_DESC,
					UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_UNMAP_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.DECOMMISSION_SUCCESS, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.DECOMMISSION_SUCCESS,
				 idResponse.getId()));
		return idResponse;
		
	}

	@Override
	public List<UserDetailsExtnDto> getUsersByRegistrationCenter(String regCenterId, int pageNumber, int pageSize, String sortBy, String orderBy) {
				List<UserDetailsExtnDto> userDetails = null;
				try {
					List<UserDetails> pageData = userDetailsRepository.findByRegIdAndIsDeletedFalseOrIsDeletedIsNull(regCenterId, PageRequest.of(pageNumber, pageSize, Sort.by(Direction.fromString(orderBy), sortBy)));
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
	public IdAndLanguageCodeID createUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		try {
			userDetailsDto = masterdataCreationUtil.createMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			List<RegistrationCenter> regCenters = registrationCenterService
					.getRegistrationCentersByID(userDetailsDto.getRegCenterId()); // Throws exception if not found
			validateZoneUserMapping(regCenters,userDetailsDto.getLangCode(),userDetailsDto.getId());
			userDetailsRepository.create(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
				MapperUtils.map(ud, udh);
				MapperUtils.setBaseFieldValue(ud, udh);
				udh.setIsActive(true);
				udh.setIsDeleted(false);
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
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		IdAndLanguageCodeID idAndLanguageCodeID = new IdAndLanguageCodeID();
		MapperUtils.map(ud, idAndLanguageCodeID);
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				UserDetails.class.getSimpleName(), idAndLanguageCodeID.getId()));
		return idAndLanguageCodeID;
	}

	/**
	 * Validates zone user mapping
	 * if mapping not exists, creates new mapping
	 * if mapping is not-active, throws error
	 * @param regCenters
	 * @param langCode
	 * @param id
	 */
	private void validateZoneUserMapping(List<RegistrationCenter> regCenters, String langCode, String id) {
		RegistrationCenter requiredRegCenter = regCenters.stream()
				.filter(lc -> lc.getLangCode().equalsIgnoreCase(langCode)).collect(Collectors.toList()).get(0);
		if (requiredRegCenter == null) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
							UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
					UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage());
		}
		ZoneUser zoneUser =  zoneUserService.getZoneUser(id, langCode, requiredRegCenter.getZoneCode());
		if(zoneUser == null) {
			createZoneUserMapping(langCode,requiredRegCenter.getZoneCode(),id);
		}
		if(zoneUser != null && !zoneUser.getIsActive()) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorCode(),
							UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_NOT_ACTIVE.getErrorMessage());
		}
	}

	/**
	 * Creates zone user mapping 
	 * @param langCode
	 * @param zoneCode
	 * @param id
	 */
	private void createZoneUserMapping(String langCode, String zoneCode, String id) {
		ZoneUserDto zoneUserDto = new ZoneUserDto();
		zoneUserDto.setIsActive(true);
		zoneUserDto.setLangCode(langCode);
		zoneUserDto.setUserId(id);
		zoneUserDto.setZoneCode(zoneCode);
		try {
			zoneUserService.createZoneUserMapping(zoneUserDto);
		} catch (Exception e) {
			auditUtil.auditRequest(
					String.format(MasterDataConstant.CREATE_ERROR_AUDIT, UserDetails.class.getSimpleName()),
					MasterDataConstant.AUDIT_SYSTEM,
					String.format(MasterDataConstant.FAILURE_DESC,
							UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorCode(),
							UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorCode(),
					UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorMessage() + ExceptionUtils.parseException(e));
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetailsDto updateUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		try {
			userDetailsDto = masterdataCreationUtil.updateMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			List<RegistrationCenter> regCenters = registrationCenterService.getRegistrationCentersByID(userDetailsDto.getRegCenterId()); //Throws exception if not found
			validateZoneUserMapping(regCenters,userDetailsDto.getLangCode(),userDetailsDto.getId());
			userDetailsRepository.update(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(ud, udh);
			MapperUtils.setBaseFieldValue(ud, udh);
			udh.setIsActive(true);
			udh.setIsDeleted(false);
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
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));			
		}

		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_UPDATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				UserDetails.class.getSimpleName(), ud.getId()));
		return userDetailsDto;
	}

	
	private UserDetailsDto getDto(UserDetails ud){
		UserDetailsDto udDto = new UserDetailsDto();
		udDto.setId(ud.getId());
		udDto.setRegCenterId(ud.getRegCenterId());
		udDto.setIsActive(ud.getIsActive());
		udDto.setLangCode(ud.getLangCode());
		return udDto;
	}

	/**
	 * 
	 */
	@Override
	public UsersDto getUsers(String roleName) {
		ResponseEntity<String> response = null;
		try {
			StringBuilder uriBuilder = new StringBuilder();
			String userDetailsUrl = uriBuilder.append(authBaseUrl).append(authServiceName) + "/" + adminRealmId;
			if (roleName != null && !roleName.isBlank() && !roleName.isEmpty()) {
				userDetailsUrl = userDetailsUrl + "?roleName=" + roleName;
			}
			HttpEntity<RequestWrapper<?>> httpRequest = getHttpRequest();
			response = restTemplate.exchange(userDetailsUrl, HttpMethod.GET, httpRequest, String.class);
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
							UserDetailsErrorCode.USER_FETCH_EXCEPTION.getErrorMessage()));
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
							UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage()));
			throw new ParseResponseException(UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage() + exception.getMessage(),
					exception);
		}
		auditUtil.auditRequest(String.format(MasterDataConstant.GET_ALL_SUCCESS, UsersDto.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM,
				String.format(MasterDataConstant.GET_ALL_SUCCESS_DESC, UsersDto.class.getSimpleName()));
		return usersDto;
	}

	@Override
	public PageResponseDto<UserDetailsExtnDto> searchUserDetails(SearchDto searchDto) {
		PageResponseDto<UserDetailsExtnDto> pageDto = new PageResponseDto<>();
		List<UserDetailsExtnDto> userDetails = null;		
		Page<UserDetails> page = masterDataSearchHelper.searchMasterdata(UserDetails.class, searchDto,null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			userDetails = MapperUtils.mapAll(page.getContent(), UserDetailsExtnDto.class);
			pageDto = pageUtils.sortPage(getZonesForUsers(userDetails), searchDto.getSort(), searchDto.getPagination());
		}
		return pageDto;
	}
}

