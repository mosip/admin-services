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
 * Service implementation for fetching and synchronizing user details from an LDAP server via the auth service.
 * <p>
 * This class provides methods to retrieve user details based on a registration center ID or key index,
 * encrypt the data using TPM, and handle interactions with the auth service. It supports both structured
 * and mapped user details, ensuring secure data transmission and proper error handling.
 * </p>
 *
 * @author Srinivasan
 * @author Megha Tanga
 * @since 1.0.0
 */
@RefreshScope
@Service
public class SyncUserDetailsServiceImpl implements SyncUserDetailsService {

	/** Logger instance for logging events and errors. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncUserDetailsServiceImpl.class);

	/** RestTemplate for making HTTP requests to the auth service. */
	@Autowired
	private RestTemplate restTemplate;

	/** ObjectMapper for JSON serialization and deserialization. */
	@Autowired
	private ObjectMapper objectMapper;

	/** Base URI for the auth manager service. */
	@Value("${mosip.kernel.syncdata.auth-manager-base-uri:https://dev.mosip.io/authmanager/v1.0}")
	private String authUserDetailsBaseUri;

	/** URI path for fetching user details. */
	@Value("${mosip.kernel.syncdata.auth-user-details:/userdetails}")
	private String authUserDetailsUri;

	/** URI path for fetching user salt details. */
	@Value("${mosip.kernel.syncdata.auth-salt-details:/usersaltdetails}")
	private String authUserSaltUri;

	/** Request ID for sync data requests. */
	@Value("${mosip.kernel.syncdata.syncdata-request-id:SYNCDATA.REQUEST}")
	private String syncDataRequestId;

	/** Version ID for sync data requests. */
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
	 * Creates an HTTP request entity for fetching user details from the auth service.
	 * <p>
	 * The request includes a list of user IDs wrapped in a {@link RequestWrapper} with
	 * the configured request ID and version, and sets the content type to JSON.
	 * </p>
	 *
	 * @param userIds list of user IDs to include in the request
	 * @return the {@link HttpEntity} containing the request wrapper
	 * @throws IllegalArgumentException if userIds is null or empty
	 */
	private HttpEntity<RequestWrapper<?>> getHttpRequest(List<String> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			LOGGER.error("Invalid request: userIds list is null or empty");
		}

		RequestWrapper<UserDetailRequestDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setId(syncDataRequestId);
		requestWrapper.setVersion(syncDataVersionId);
		UserDetailRequestDto userDetailsDto = new UserDetailRequestDto();
		userDetailsDto.setUserDetails(userIds);
		requestWrapper.setRequest(userDetailsDto);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(requestWrapper, headers);
	}

	/**
	 * Parses the response body from the auth service into a {@link UserDetailResponseDto}.
	 * <p>
	 * This method validates the response for service errors and deserializes the JSON response.
	 * If parsing fails or the response contains errors, appropriate exceptions are thrown.
	 * </p>
	 *
	 * @param responseBody the JSON response body from the auth service
	 * @return the parsed {@link UserDetailResponseDto}
	 * @throws SyncServiceException     if the response contains service errors
	 * @throws ParseResponseException   if the response cannot be parsed
	 * @throws IllegalArgumentException if responseBody is null or empty
	 */
	private UserDetailResponseDto getUserDetailFromResponse(String responseBody) {
		if (responseBody == null || responseBody.isEmpty()) {
			LOGGER.error("Invalid request: response body is null or empty");
		}

		List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(responseBody);
		if (!validationErrorsList.isEmpty()) {
			throw new SyncServiceException(validationErrorsList);
		}
		try {
			ResponseWrapper<UserDetailResponseDto> responseObject = objectMapper.readValue(
					responseBody, new TypeReference<ResponseWrapper<UserDetailResponseDto>>() {});
			UserDetailResponseDto userDetailResponseDto = responseObject.getResponse();
			if (userDetailResponseDto == null) {
				LOGGER.error("Invalid response: UserDetailResponseDto is null");
				throw new ParseResponseException(
						UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
						UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage() + ": Response is null");
			}
			return userDetailResponseDto;
		} catch (IOException | NullPointerException e) {
			LOGGER.error("Failed to parse user details response", e);
			throw new ParseResponseException(UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_PARSE_ERROR.getErrorMessage() + e.getMessage(),
					e);
		}
	}

	/**
	 * Retrieves user details for a given registration center ID from the local repository.
	 * <p>
	 * This method queries the repository for users associated with the specified registration center
	 * and maps them to {@link RegistrationCenterUserDto} objects. If no users are found, a
	 * {@link DataNotFoundException} is thrown. Data access errors are handled appropriately.
	 * </p>
	 *
	 * @param regCenterId the registration center ID
	 * @return a {@link RegistrationCenterUserResponseDto} containing the user details
	 * @throws SyncDataServiceException if a data access error occurs
	 * @throws DataNotFoundException   if no users are found for the registration center
	 * @throws IllegalArgumentException if regCenterId is null or empty
	 */
	public RegistrationCenterUserResponseDto getUsersBasedOnRegistrationCenterId(String regCenterId) {
		if (regCenterId == null || regCenterId.isEmpty()) {
			LOGGER.error("Invalid request: regCenterId is null or empty");
		}
		List<UserDetails> users = null;
		try {
			users = userDetailsRepository.findByUsersByRegCenterId(regCenterId);
		} catch (DataAccessException | DataAccessLayerException ex) {
			LOGGER.error("Failed to fetch users for registration center: {}", regCenterId, ex);
			throw new SyncDataServiceException(
					RegistrationCenterUserErrorCode.REGISTRATION_USER_FETCH_EXCEPTION.getErrorCode(),
					RegistrationCenterUserErrorCode.REGISTRATION_USER_FETCH_EXCEPTION.getErrorMessage());
		}

		if (users.isEmpty()) {
			LOGGER.warn("No users found for registration center: {}", regCenterId);
			throw new DataNotFoundException(
					RegistrationCenterUserErrorCode.REGISTRATION_USER_DATA_NOT_FOUND_EXCEPTION.getErrorCode(),
					RegistrationCenterUserErrorCode.REGISTRATION_USER_DATA_NOT_FOUND_EXCEPTION.getErrorMessage());
		}

		List<RegistrationCenterUserDto> registrationCenterUserDtos = new ArrayList<>();
		RegistrationCenterUserResponseDto registrationCenterUserResponseDto = new RegistrationCenterUserResponseDto();
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
		LOGGER.debug("Retrieved {} users for registration center: {}", registrationCenterUserDtos.size(), regCenterId);
		return registrationCenterUserResponseDto;
	}

	/**
	 * Fetches and encrypts user details based on a key index for synchronization (version 1).
	 * <p>
	 * This method retrieves the registration center associated with the key index, fetches user IDs
	 * for that center, retrieves detailed user information from the auth service, maps the data,
	 * and encrypts it using TPM. The encrypted data is returned in a {@link SyncUserDto}.
	 * </p>
	 *
	 * @param keyIndex the key index associated with a machine
	 * @return a {@link SyncUserDto} containing encrypted user details
	 * @throws SyncDataServiceException if data fetching or encryption fails
	 * @throws IllegalArgumentException if keyIndex is null or empty
	 */
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	@Override
	public SyncUserDto getAllUserDetailsBasedOnKeyIndex(String keyIndex) {
		if (keyIndex == null || keyIndex.isEmpty()) {
			LOGGER.error("Invalid request: keyIndex is null or empty");
		}

		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(null, keyIndex);
		if (regCenterMachineDto == null) {
			LOGGER.error("No registration center machine found for keyIndex: {}", keyIndex);
		}

		List<RegistrationCenterUserDto> rcUsers = fetchUsersPaged(regCenterMachineDto.getRegCenterId());
		if (rcUsers.isEmpty()) {
			LOGGER.warn("No valid user IDs found for registration center: {}", regCenterMachineDto.getRegCenterId());
			return new SyncUserDto();
		}

		// Batch call to Auth + single compress→encrypt (exactly as discussed)
		final int BATCH = 200;
		List<String> userIds = rcUsers.stream().map(RegistrationCenterUserDto::getUserId).collect(Collectors.toList());
		List<List<String>> parts = new ArrayList<>();
		for (int i = 0; i < userIds.size(); i += BATCH) parts.add(userIds.subList(i, Math.min(i + BATCH, userIds.size())));

		List<UserDetailMapDto> merged = new ArrayList<>(userIds.size());
		for (List<String> ids : parts) {
			UserDetailResponseDto part = getUserDetailsFromAuthServer(ids);
			if (part != null && part.getMosipUserDtoList() != null) {
				merged.addAll(MapperUtils.mapUserDetailsToUserDetailMap(part.getMosipUserDtoList(), rcUsers));
			}
		}

		SyncUserDto syncUserDto = new SyncUserDto();
		try {
			if (!merged.isEmpty()) {
				byte[] json = mapper.getObjectAsJsonString(merged).getBytes(java.nio.charset.StandardCharsets.UTF_8);
				// Optional but recommended: gzip before encrypt (toggle via property if needed)
				// byte[] gz = gzip(json);
				TpmCryptoRequestDto req = new TpmCryptoRequestDto();
				req.setValue(CryptoUtil.encodeToURLSafeBase64(json)); // or gz if you enable compression
				req.setPublicKey(regCenterMachineDto.getPublicKey());
				TpmCryptoResponseDto enc = clientCryptoManagerService.csEncrypt(req);
				syncUserDto.setUserDetails(enc.getValue());
				LOGGER.debug("Encrypted {} user details for keyIndex: {}", merged.size(), keyIndex);
			}
		} catch (Exception e) {
			LOGGER.error("Failed to encrypt user data for keyIndex: {}", keyIndex, e);
		}
		return syncUserDto;
	}

	/**
	 * Fetches user details from the auth service for the specified user IDs.
	 * <p>
	 * This method sends a POST request to the auth service with the user IDs and parses the response.
	 * It handles HTTP errors (401, 403, etc.) and maps them to appropriate exceptions.
	 * </p>
	 *
	 * @param userIds list of user IDs to fetch details for
	 * @return the {@link UserDetailResponseDto} containing user details
	 * @throws AuthNException           if authentication fails (401)
	 * @throws AuthZException           if authorization fails (403)
	 * @throws SyncDataServiceException if the request fails for other reasons
	 * @throws IllegalArgumentException if userIds is null or empty
	 */
	public UserDetailResponseDto getUserDetailsFromAuthServer(List<String> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			LOGGER.error("Invalid request: userIds list is null or empty");
		}

		StringBuilder userDetailsUri = new StringBuilder();
		userDetailsUri.append(authUserDetailsBaseUri).append(authUserDetailsUri);
		try {
			HttpEntity<RequestWrapper<?>> userDetailReqEntity = getHttpRequest(userIds);
			ResponseEntity<String> response = restTemplate.postForEntity(userDetailsUri.toString() + "/registrationclient",
					userDetailReqEntity, String.class);
			return getUserDetailFromResponse(response.getBody());
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(ex.getResponseBodyAsString());
			if (ex.getStatusCode().value() == 401) {
				LOGGER.error("Authentication failed for user details request: {}", userIds, ex);
				throw  (!validationErrorsList.isEmpty()) ? new AuthNException(validationErrorsList) : new BadCredentialsException("Authentication failed from AuthManager");
			}
			if (ex.getStatusCode().value() == 403) {
				LOGGER.error("Authentication failed for user details request: {}", userIds, ex);
				throw (!validationErrorsList.isEmpty()) ? new AuthZException(validationErrorsList) : new AccessDeniedException("Access denied from AuthManager");
			}
			LOGGER.error("Failed to fetch user details from auth server: {}", userIds, ex);
			throw new SyncDataServiceException(UserDetailsErrorCode.USER_DETAILS_FETCH_EXCEPTION.getErrorCode(),
					UserDetailsErrorCode.USER_DETAILS_FETCH_EXCEPTION.getErrorMessage(), ex);
		}
	}

	/**
	 * Fetches and encrypts user details based on a key index for synchronization (version 2).
	 * <p>
	 * This method retrieves the registration center associated with the key index, fetches user details
	 * for that center, and encrypts them using TPM. The encrypted data is returned in a {@link SyncUserDto}.
	 * Unlike version 1, this method does not fetch additional details from the auth service.
	 * </p>
	 *
	 * @param keyIndex the key index associated with a machine
	 * @return a {@link SyncUserDto} containing encrypted user details
	 * @throws SyncDataServiceException if data fetching or encryption fails
	 * @throws IllegalArgumentException if keyIndex is null or empty
	 */
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	@Override
	public SyncUserDto getAllUserDetailsBasedOnKeyIndexV2(String keyIndex) {
		if (keyIndex == null || keyIndex.isEmpty()) {
			LOGGER.error("Invalid request: keyIndex is null or empty");
		}

		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(null, keyIndex);
		if (regCenterMachineDto == null) {
			LOGGER.error("No registration center machine found for keyIndex: {}", keyIndex);
		}

		List<RegistrationCenterUserDto> users = fetchUsersPaged(regCenterMachineDto.getRegCenterId());
		SyncUserDto syncUserDto = new SyncUserDto();
		if (users != null && !users.isEmpty()) {
			LOGGER.info("{} Users synced for this reg center {}", users.size(), regCenterMachineDto.getRegCenterId());
			try {
				byte[] json = mapper.getObjectAsJsonString(users).getBytes(java.nio.charset.StandardCharsets.UTF_8);
				// Optional: gzip before encrypt (toggleable)
				// byte[] gz = gzip(json);
				TpmCryptoRequestDto req = new TpmCryptoRequestDto();
				req.setValue(CryptoUtil.encodeToURLSafeBase64(json)); // or gz
				req.setPublicKey(regCenterMachineDto.getPublicKey());
				req.setClientType(regCenterMachineDto.getClientType());
				TpmCryptoResponseDto enc = clientCryptoManagerService.csEncrypt(req);
				syncUserDto.setUserDetails(enc.getValue());
				LOGGER.info("Encrypted {} user details for keyIndex: {} (V2)", users.size(), keyIndex);
			} catch (Exception e) {
				LOGGER.error("Failed to encrypt user data for keyIndex: {} (V2)", keyIndex, e);
			}
		}
		return syncUserDto;
	}

	private List<RegistrationCenterUserDto> fetchUsersPaged(String regCenterId) {
		final int PAGE_SIZE = 1000; // tune 500–2000
		List<RegistrationCenterUserDto> out = new ArrayList<>(2048);
		int page = 0;
		org.springframework.data.domain.Page<UserDetails> p;
		do {
			p = userDetailsRepository.findPageByRegCenterId(
					regCenterId, org.springframework.data.domain.PageRequest.of(page++, PAGE_SIZE));
			for (UserDetails u : p.getContent()) {
				RegistrationCenterUserDto dto = new RegistrationCenterUserDto();
				dto.setIsActive(u.getIsActive());
				dto.setIsDeleted(u.getIsDeleted());
				dto.setLangCode(u.getLangCode());
				dto.setRegCenterId(u.getRegCenterId());
				dto.setUserId(u.getId());
				out.add(dto);
			}
		} while (p.hasNext());
		return out;
	}

}