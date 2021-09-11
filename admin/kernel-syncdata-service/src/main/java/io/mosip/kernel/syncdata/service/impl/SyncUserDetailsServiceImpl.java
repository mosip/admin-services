package io.mosip.kernel.syncdata.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.exception.*;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.authmanager.exception.AuthNException;
import io.mosip.kernel.core.authmanager.exception.AuthZException;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.constant.RegistrationCenterUserErrorCode;
import io.mosip.kernel.syncdata.constant.UserDetailsErrorCode;
import io.mosip.kernel.syncdata.dto.response.RegistrationCenterUserResponseDto;
import io.mosip.kernel.syncdata.dto.response.UserDetailResponseDto;
import io.mosip.kernel.syncdata.entity.UserDetails;
import io.mosip.kernel.syncdata.repository.UserDetailsRepository;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.utils.MapperUtils;

/**
 * This class will fetch all user details from the LDAP server through
 * auth-service.
 *
 * @author Srinivasan
 * @author Megha Tanga
 * @since 1.0.0
 */
@RefreshScope
@Service
public class SyncUserDetailsServiceImpl implements SyncUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(SyncUserDetailsServiceImpl.class);

	/** The rest template. */
	@Autowired
	RestTemplate restTemplate;

	/** The object mapper. */
	@Autowired
	ObjectMapper objectMapper;

	/** The auth user details base uri. */
	@Value("${mosip.kernel.syncdata.auth-manager-base-uri:https://dev.mosip.io/authmanager/v1.0}")
	private String authUserDetailsBaseUri;

	/** The auth user details uri. */
	@Value("${mosip.kernel.syncdata.auth-user-details:/userdetails}")
	private String authUserDetailsUri;

	/** The auth user details uri. */
	@Value("${mosip.kernel.syncdata.auth-salt-details:/usersaltdetails}")
	private String authUserSaltUri;

	/** The sync data request id. */
	@Value("${mosip.kernel.syncdata.syncdata-request-id:SYNCDATA.REQUEST}")
	private String syncDataRequestId;

	/** The sync data version id. */
	@Value("${mosip.kernel.syncdata.syncdata-version-id:v1.0}")
	private String syncDataVersionId;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	@Autowired
	private MapperUtils mapper;

	@Autowired
	private MachineRepository machineRepository;




	/**
	 * Gets the http request.
	 *
	 * @param userIds the user ids
	 * @return {@link HttpEntity}
	 */
	private HttpEntity<RequestWrapper<?>> getHttpRequest(List<String> userIds) {
		RequestWrapper<UserDetailRequestDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setId(syncDataRequestId);
		requestWrapper.setVersion(syncDataVersionId);
		HttpHeaders syncDataRequestHeaders = new HttpHeaders();
		syncDataRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
		UserDetailRequestDto userDetailsDto = new UserDetailRequestDto();
		userDetailsDto.setUserDetails(userIds);
		requestWrapper.setRequest(userDetailsDto);
		return new HttpEntity<>(requestWrapper, syncDataRequestHeaders);

	}

	/**
	 * Gets the user detail from response.
	 *
	 * @param responseBody the response body
	 * @return {@link UserDetailResponseDto}
	 */
	private UserDetailResponseDto getUserDetailFromResponse(String responseBody) {
		List<ServiceError> validationErrorsList = null;
		validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
		UserDetailResponseDto userDetailResponseDto = null;
		if (!validationErrorsList.isEmpty()) {
			throw new SyncServiceException(validationErrorsList);
		}
		ResponseWrapper<UserDetailResponseDto> responseObject = null;
		try {

			responseObject = objectMapper.readValue(responseBody,
					new TypeReference<ResponseWrapper<UserDetailResponseDto>>() {
					});
			userDetailResponseDto = responseObject.getResponse();
		} catch (IOException | NullPointerException exception) {
			throw new ParseResponseException(UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage() + exception.getMessage(),
					exception);
		}

		return userDetailResponseDto;
	}



	public RegistrationCenterUserResponseDto getUsersBasedOnRegistrationCenterId(String regCenterId) {
		List<UserDetails> users = null;
		List<RegistrationCenterUserDto> registrationCenterUserDtos = new ArrayList<RegistrationCenterUserDto>();
		RegistrationCenterUserResponseDto registrationCenterUserResponseDto = new RegistrationCenterUserResponseDto();
		try {
			users = userDetailsRepository.findByUsersByRegCenterId(regCenterId);
		} catch (DataAccessException | DataAccessLayerException ex) {
			throw new SyncDataServiceException(
					RegistrationCenterUserErrorCode.REGISTRATION_USER_FETCH_EXCEPTION.getErrorCode(),
					RegistrationCenterUserErrorCode.REGISTRATION_USER_FETCH_EXCEPTION.getErrorMessage());
		}

		if (users.isEmpty()) {
			throw new DataNotFoundException(
					RegistrationCenterUserErrorCode.REGISTRATION_USER_DATA_NOT_FOUND_EXCEPTION.getErrorCode(),
					RegistrationCenterUserErrorCode.REGISTRATION_USER_DATA_NOT_FOUND_EXCEPTION.getErrorMessage());
		}

		for(UserDetails userDetails:users) {
			RegistrationCenterUserDto dto=new RegistrationCenterUserDto();
			dto.setIsActive(userDetails.getIsActive());
			dto.setIsDeleted(userDetails.getIsDeleted());
			dto.setLangCode(userDetails.getLangCode());
			dto.setRegCenterId(userDetails.getRegCenterId());
			dto.setUserId(userDetails.getId());
			registrationCenterUserDtos.add(dto);
		}
		registrationCenterUserResponseDto.setRegistrationCenterUsers(registrationCenterUserDtos);
		return registrationCenterUserResponseDto;
	}



	@Override
	public SyncUserDto getAllUserDetailsBasedOnKeyIndex(String keyIndex) {
		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(null, keyIndex);

		RegistrationCenterUserResponseDto registrationCenterResponseDto = getUsersBasedOnRegistrationCenterId(regCenterMachineDto.getRegCenterId());
		List<String> userIds = registrationCenterResponseDto.getRegistrationCenterUsers()
				.stream()
				.map(RegistrationCenterUserDto::getUserId)
				.collect(Collectors.toList());

		UserDetailResponseDto userDetailResponseDto = getUserDetailsFromAuthServer(userIds);
		SyncUserDto syncUserDto = new SyncUserDto();
		if (userDetailResponseDto != null && userDetailResponseDto.getMosipUserDtoList() != null) {
			List<UserDetailMapDto> userDetails = MapperUtils.mapUserDetailsToUserDetailMap(userDetailResponseDto.getMosipUserDtoList(),
					registrationCenterResponseDto.getRegistrationCenterUsers());
			try {
				if(userDetails.size() > 0) {
					TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
					tpmCryptoRequestDto.setValue(CryptoUtil.encodeBase64(mapper.getObjectAsJsonString(userDetails).getBytes()));
					tpmCryptoRequestDto.setPublicKey(regCenterMachineDto.getPublicKey());
					TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
					syncUserDto.setUserDetails(tpmCryptoResponseDto.getValue());
				}
			} catch (Exception e) {
				logger.error("Failed to encrypt user data", e);
			}
		}
		return syncUserDto;
	}

	private UserDetailResponseDto getUserDetailsFromAuthServer(List<String> userIds) {
		StringBuilder userDetailsUri = new StringBuilder();
		userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
		try {
			HttpEntity<RequestWrapper<?>> userDetailReqEntity = getHttpRequest(userIds);
			ResponseEntity<String> response = restTemplate.postForEntity(userDetailsUri.toString() + "/registrationclient",
					userDetailReqEntity, String.class);
			return getUserDetailFromResponse(response.getBody());
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(ex.getResponseBodyAsString());
			if (ex.getRawStatusCode() == 401) {
				throw  (!validationErrorsList.isEmpty()) ? new AuthNException(validationErrorsList) : new BadCredentialsException("Authentication failed from AuthManager");
			}
			if (ex.getRawStatusCode() == 403) {
				throw (!validationErrorsList.isEmpty()) ? new AuthZException(validationErrorsList) : new AccessDeniedException("Access denied from AuthManager");
			}
			throw new SyncDataServiceException(UserDetailsErrorCode.USER_DETAILS_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_FETCH_EXCEPTION.getErrorMessage(), ex);
		}
	}

}
