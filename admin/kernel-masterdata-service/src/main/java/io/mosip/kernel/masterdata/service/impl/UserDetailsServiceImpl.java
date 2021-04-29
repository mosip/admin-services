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
import io.mosip.kernel.masterdata.dto.UserDetailsDto;
import io.mosip.kernel.masterdata.dto.UserDetailsGetExtnDto;
import io.mosip.kernel.masterdata.dto.UserDetailsPutDto;
import io.mosip.kernel.masterdata.dto.UsersDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.extn.UserDetailsExtnDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.dto.response.PageResponseDto;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.UserDetails;
import io.mosip.kernel.masterdata.entity.UserDetailsHistory;
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
	
	@Value("${mosip.keycloak.max-no-of-users:100}")
	private String maxUsers;
	
	@Autowired
	ZoneUserService zoneUserService;
	
	@Autowired
	ZoneUserRepository zoneUserRepository;

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
	public UserDetailsGetExtnDto createUser(UserDetailsDto userDetailsDto) {
		UserDetails ud;
		try {
			Optional<UserDetails> result = userDetailsRepository.findById(userDetailsDto.getId());
			if(result.isPresent())
				throw new MasterDataServiceException(UserDetailsErrorCode.USER_ALREADY_EXISTS.getErrorCode(),
						UserDetailsErrorCode.USER_ALREADY_EXISTS.getErrorMessage());

			List<RegistrationCenter> regCenters = registrationCenterService.getRegistrationCentersByID(userDetailsDto.getRegCenterId());
			if(regCenters == null || regCenters.isEmpty()) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage()));
				throw new MasterDataServiceException(UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
						UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage());
			}

			//Zone user mapping should be handled in zoneuser API, here we only validate mapping
			//Commenting this it cannot be directly validated, as user could belong to parent zone
			/*ZoneUser zoneUser =  zoneUserService.getZoneUser(userDetailsDto.getId(), regCenters.get(0).getZoneCode());
			if(zoneUser == null) {
				throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorCode(),
						UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorMessage());
			}*/

			userDetailsDto = masterdataCreationUtil.createMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			ud.setIsActive(true);

			userDetailsRepository.create(ud);

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
							UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage()));
			throw new MasterDataServiceException(UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorCode(),
			UserDetailsErrorCode.USER_CREATION_EXCEPTION.getErrorMessage() + ExceptionUtils.parseException(e));
		}

		UserDetailsGetExtnDto userDetailsGetExtnDto = new UserDetailsGetExtnDto();
		MapperUtils.map(ud, userDetailsGetExtnDto);
		auditUtil.auditRequest(String.format(MasterDataConstant.SUCCESSFUL_CREATE, UserDetails.class.getSimpleName()),
				MasterDataConstant.AUDIT_SYSTEM, String.format(MasterDataConstant.SUCCESSFUL_CREATE_DESC,
				UserDetails.class.getSimpleName(), userDetailsGetExtnDto.getId()));
		return userDetailsGetExtnDto;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetailsPutDto updateUser(UserDetailsPutDto userDetailsDto) {
		UserDetails ud;
		try {
			Optional<UserDetails> result = userDetailsRepository.findById(userDetailsDto.getId());
			if(!result.isPresent())
				throw new MasterDataServiceException(UserDetailsErrorCode.USER_NOT_FOUND.getErrorCode(),
						UserDetailsErrorCode.USER_NOT_FOUND.getErrorMessage());

			List<RegistrationCenter> regCenters = registrationCenterService.getRegistrationCentersByID(userDetailsDto.getRegCenterId()); //Throws exception if not found

			if(regCenters == null || regCenters.isEmpty()) {
				auditUtil.auditRequest(
						String.format(MasterDataConstant.GET_ALL, UserDetails.class.getSimpleName()),
						MasterDataConstant.AUDIT_SYSTEM,
						String.format(MasterDataConstant.FAILURE_DESC,
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
								UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage()));
				throw new MasterDataServiceException(UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorCode(),
						UserDetailsErrorCode.CENTER_LANG_MAPPING_NOT_EXISTS.getErrorMessage());
			}
			//Zone user mapping should be handled in zoneuser API, here we only validate mapping
			//Commenting this it cannot be directly validated, as user could belong to parent zone
			/*ZoneUser zoneUser =  zoneUserService.getZoneUser(userDetailsDto.getId(), regCenters.get(0).getZoneCode());
			if(zoneUser == null) {
				throw new MasterDataServiceException(UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorCode(),
						UserDetailsErrorCode.ZONE_USER_MAPPING_ERROR.getErrorMessage());
			}*/

			userDetailsDto = masterdataCreationUtil.updateMasterData(UserDetails.class, userDetailsDto);
			ud = MetaDataUtils.setCreateMetaData(userDetailsDto, UserDetails.class);
			// ud.setIsActive(userDetailsDto.getIsActive());

			userDetailsRepository.update(ud);
			UserDetailsHistory udh = new UserDetailsHistory();
			MapperUtils.map(ud, udh);
			MapperUtils.setBaseFieldValue(ud, udh);
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



	/*
	private UserDetailsDto getDto(UserDetails ud){
		UserDetailsDto udDto = new UserDetailsDto();
		udDto.setId(ud.getId());
		udDto.setRegCenterId(ud.getRegCenterId());
		udDto.setIsActive(ud.getIsActive());
		udDto.setLangCode(ud.getLangCode());
		return udDto;
	}*/

	/**
	 * 
	 */
	
	@Override
	public StatusResponseDto updateUserStatus(String id, boolean isActive) {
		// TODO Auto-generated method stub
		StatusResponseDto response = new StatusResponseDto();
		UserDetails ud;
		try {
			Optional<UserDetails> result = userDetailsRepository.findById(id);
			if (result != null && !result.isEmpty()) {
				masterdataCreationUtil.updateMasterDataStatus(UserDetails.class, id, isActive, "id");
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
	public UsersDto getUsers(String roleName,int pageStart, int pageFetch,
			String email, String firstName, String lastName, String userName) {
		ResponseEntity<String> response = null;		
		UriComponentsBuilder uriComponentsBuilder = null;
		try {
			uriComponentsBuilder = UriComponentsBuilder.fromUriString(authBaseUrl + authServiceName + "/" + adminRealmId);
			if (StringUtils.isNotBlank(roleName)) {				
				uriComponentsBuilder.queryParam("roleName",roleName);
			}
			if(StringUtils.isNotBlank(email)) {
				uriComponentsBuilder.queryParam("email", email);
			}
			if(StringUtils.isNotBlank(firstName)) {
				uriComponentsBuilder.queryParam("firstName", firstName);
			}
			if(StringUtils.isNotBlank(lastName)) {
				uriComponentsBuilder.queryParam("lastName", lastName);
			}
			if(StringUtils.isNotBlank(userName)) {
				uriComponentsBuilder.queryParam("userName", userName);
			}
			uriComponentsBuilder.queryParam("pageStart", pageStart);
			uriComponentsBuilder.queryParam("pageFetch", pageFetch == 0 ? maxUsers:pageFetch);
			HttpEntity<RequestWrapper<?>> httpRequest = getHttpRequest();
			response = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, httpRequest, String.class);
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
	public PageResponseDto<UserDetailsExtnDto> searchUserDetails(SearchDtoWithoutLangCode searchDto) {
		PageResponseDto<UserDetailsExtnDto> pageDto = new PageResponseDto<>();
		List<UserDetailsExtnDto> userDetails = null;
		Page<UserDetails> page = masterDataSearchHelper.searchMasterdataWithoutLangCode(UserDetails.class, searchDto,
				null);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			userDetails = MapperUtils.mapAll(page.getContent(), UserDetailsExtnDto.class);
			pageDto = PageUtils.pageResponse(page);
			pageDto.setData(getZonesForUsers(userDetails));
		}
		return pageDto;
	}
}

