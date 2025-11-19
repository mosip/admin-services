package io.mosip.kernel.syncdata.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils2;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.CACertificates;
import io.mosip.kernel.syncdata.dto.response.ClientPublicKeyResponseDto;
import io.mosip.kernel.syncdata.dto.response.KeyPairGenerateResponseDto;
import io.mosip.kernel.syncdata.dto.response.SyncDataResponseDto;
import io.mosip.kernel.syncdata.service.SyncConfigDetailsService;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
import io.mosip.kernel.syncdata.utils.LocalDateTimeUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * REST Controller for synchronizing master data, configurations, and user details.
 * <p>
 * Handles endpoints for client settings, public keys, certificates, identity schemas, user details, and scripts.
 * All endpoints are secured with role-based access control and include input validation to ensure data integrity.
 * Responses are wrapped in {@link ResponseWrapper} for consistent error handling and metadata inclusion.
 * </p>
 *
 * @author Srinivasan
 * @author Abhishek Kumar
 * @author Bal Vikash Sharma
 * @author Megha Tanga
 * @author Urvil Joshi
 * @since 1.0.0
 */
@RestController
public class SyncDataController {

	private static final String CLIENT_VERSION_KEY = "client_version";
	private static final String KEY_INDEX_KEY = "key_index";
	private static final String NA = "NA";
	private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT;

	/**
	 * Service instance {@link SyncMasterDataService}
	 */
	@Autowired
	private SyncMasterDataService masterDataService;

	/**
	 * Service instance {@link SyncConfigDetailsService}
	 */
	@Autowired
	private SyncConfigDetailsService syncConfigDetailsService;

	@Autowired
	private SyncUserDetailsService syncUserDetailsService;

	@Autowired
	private LocalDateTimeUtil localDateTimeUtil;

	@Autowired
	private SyncJobHelperService syncJobHelperService;

	/**
	 * Synchronizes client settings for a registration center and machine.
	 *
	 * @param keyIndex    the machine key index
	 * @param lastUpdated the last updated timestamp (optional)
	 * @param regCenterId the registration center ID (optional)
	 * @return {@link ResponseWrapper} containing {@link SyncDataResponseDto}
	 * @throws Throwable if data fetching fails
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetclientsettings())")
	@ResponseFilter
	@ApiOperation(value = "API to sync client settings")
	@GetMapping("/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettings(
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex,
			@ApiParam("Last updated timestamp") @RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@ApiParam("Registration center ID") @RequestParam(value = "regcenterId", required = false) String regCenterId)
			throws Throwable {
		MDC.put(KEY_INDEX_KEY, keyIndex);
		try {
			LocalDateTime currentTimeStamp = lastUpdated == null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
					syncJobHelperService.getDeltaSyncCurrentTimestamp();
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);

			SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettings(regCenterId, keyIndex,
					timestamp, currentTimeStamp);

			syncDataResponseDto.setLastSyncTime(DateUtils2.formatToISOString(currentTimeStamp));
			return buildResponse(syncDataResponseDto);
		} finally {
			MDC.remove(KEY_INDEX_KEY);
		}
	}

	/**
	 * Retrieves the public key for an application.
	 *
	 * @param applicationId the application ID
	 * @param timeStamp     the request timestamp (optional)
	 * @param referenceId   the reference ID (optional)
	 * @return {@link ResponseWrapper} containing {@link PublicKeyResponse}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetpublickeyapplicationid())")
	@ResponseFilter
	@ApiOperation(value = "API to get public key")
	@GetMapping(value = "/publickey/{applicationId}")
	public ResponseWrapper<PublicKeyResponse<String>> getPublicKey(
			@ApiParam("Application ID") @PathVariable("applicationId") String applicationId,
			@ApiParam("Request timestamp as metadata") @RequestParam(value = "timeStamp", required = false) String timeStamp,
			@ApiParam("Reference ID as metadata") @RequestParam(value = "referenceId", required = false) String referenceId) {
		String currentTimeStamp = DateUtils2.getUTCCurrentDateTimeString();
		PublicKeyResponse<String> publicKeyResponse = syncConfigDetailsService.getPublicKey(applicationId, timeStamp,
				referenceId);
		publicKeyResponse.setLastSyncTime(currentTimeStamp);

		return buildResponse(publicKeyResponse);
	}

	/**
	 * Verifies the mapping of a public key with a machine.
	 *
	 * @param uploadPublicKeyRequestDto the {@link RequestWrapper} containing {@link UploadPublicKeyRequestDto}
	 * @param clientVersion             the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link UploadPublicKeyResponseDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGettpmpublickeyverify())")
	@ResponseFilter
	@ApiOperation(value = "API to verify public key mapping with machine")
	@PostMapping(value = "/tpm/publickey/verify", produces = "application/json")
	public ResponseWrapper<UploadPublicKeyResponseDto> validateKeyMachineMapping(
			@ApiParam("public key in BASE64 encoded") @RequestBody @Valid RequestWrapper<UploadPublicKeyRequestDto> uploadPublicKeyRequestDto,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			UploadPublicKeyResponseDto responseDto = masterDataService.validateKeyMachineMapping(uploadPublicKeyRequestDto.getRequest());
			return buildResponse(responseDto);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetlatestidschema())")
	@ResponseFilter
	@ApiOperation(value = "API to get latest identity schema")
	@GetMapping(value = "/latestidschema", produces = "application/json")
	public ResponseWrapper<JsonNode> getLatestPublishedIdSchema(
			@ApiParam("Last updated timestamp") @RequestParam(value = "lastupdated", required = false) String lastUpdated,
			@ApiParam("Schema version") @RequestParam(value = "schemaVersion", defaultValue = "0", required = false) double schemaVersion,
			@ApiParam("Domain") @RequestParam(name = "domain", required = false) String domain,
			@ApiParam("Type") @RequestParam(name = "type", required = false) String type,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
			/** If clientVersion, domain and type are null, it is assumed that the request is from regclient version < 1.2.0.*
			 With this assumption, "domain" is set to "registration-client" and "type" to "schema" for backward compatibility
			 */
			if (clientVersion == null && domain == null && type == null) {
				domain = "registration-client";
				type = "schema";
			}
			JsonNode schema = masterDataService.getLatestPublishedIdSchema(timestamp, schemaVersion, domain, type);
			return buildResponse(schema);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Retrieves a certificate for an application.
	 *
	 * @param applicationId the application ID
	 * @param referenceId   the reference ID (optional)
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link KeyPairGenerateResponseDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgetcertificate())")
	@ResponseFilter
	@ApiOperation(value = "API to get certificate")
	@GetMapping(value = "/getCertificate")
	public ResponseWrapper<KeyPairGenerateResponseDto> getCertificate(
			@ApiParam("Application ID") @RequestParam("applicationId") String applicationId,
			@ApiParam("Reference ID as metadata") @RequestParam("referenceId") Optional<String> referenceId,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			KeyPairGenerateResponseDto responseDto = masterDataService.getCertificate(applicationId, referenceId);
			return buildResponse(responseDto);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Retrieves the public key for a client machine.
	 *
	 * @param machineId     the machine ID
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link ClientPublicKeyResponseDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGettpmpublickeymachineid())")
	@ResponseFilter
	@ApiOperation(value = "API to get client public key")
	@GetMapping(value = "/tpm/publickey/{machineId}", produces = "application/json")
	public ResponseWrapper<ClientPublicKeyResponseDto> getClientPublicKey(
			@ApiParam("Machine ID") @PathVariable("machineId") String machineId,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			ClientPublicKeyResponseDto responseDto = masterDataService.getClientPublicKey(machineId);
			return buildResponse(responseDto);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Retrieves global configuration details for a machine.
	 *
	 * @param keyIndex      the machine key index
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link ConfigDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetconfigskeyIndex())")
	@ResponseFilter
	@ApiOperation(value = "API to sync global config details")
	@GetMapping(value = "/configs/{keyIndex}")
	public ResponseWrapper<ConfigDto> getMachineConfigDetails(
			@ApiParam("Machine key index") @PathVariable("keyIndex") String keyIndex,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			String currentTimeStamp = DateUtils2.getUTCCurrentDateTimeString();
			ConfigDto configDto = syncConfigDetailsService.getConfigDetails(keyIndex);
			configDto.setLastSyncTime(currentTimeStamp);
			return buildResponse(configDto);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Retrieves user details for a registration center based on key index.
	 *
	 * @param keyIndex the machine key index
	 * @return {@link ResponseWrapper} containing {@link SyncUserDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetuserdetails())")
	@ResponseFilter
	@ApiOperation(value = "API to get user details by key index")
	@GetMapping("/userdetails")
	public ResponseWrapper<SyncUserDto> getUserDetailsBasedOnKeyIndex(
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex) {
		MDC.put(KEY_INDEX_KEY, keyIndex);
		try {
			String currentTimeStamp = DateUtils2.getUTCCurrentDateTimeString();
			SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndex(keyIndex);
			syncUserDto.setLastSyncTime(currentTimeStamp);
			return buildResponse(syncUserDto);
		} finally {
			MDC.remove(KEY_INDEX_KEY);
		}
	}

	/**
	 * Retrieves CA certificates from the last updated timestamp.
	 *
	 * @param lastUpdated   the last updated timestamp (optional)
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link CACertificates}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgetcacertificates())")
	@ResponseFilter
	@ApiOperation(value = "API to get CA certificates")
	@GetMapping("/getcacertificates")
	public ResponseWrapper<CACertificates> getCACertificates(
			@ApiParam("Last updated timestamp") @RequestParam(value = "lastupdated", required = false) String lastUpdated,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
			CACertificates caCertificates = masterDataService.getPartnerCACertificates(timestamp, currentTimeStamp);
			caCertificates.setLastSyncTime(DateUtils2.formatToISOString(currentTimeStamp));
			return buildResponse(caCertificates);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Synchronizes client settings with additional parameters (V2).
	 *
	 * @param keyIndex         the machine key index
	 * @param lastUpdated      the last updated timestamp (optional)
	 * @param regCenterId      the registration center ID (optional)
	 * @param clientVersion    the client version (optional)
	 * @param fullSyncEntities the entities for full sync (optional)
	 * @return {@link ResponseWrapper} containing {@link SyncDataResponseDto}
	 * @throws Throwable if data fetching fails
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetv2clientsettings())")
	@ResponseFilter
	@ApiOperation(value = "API to sync client settings (V2)")
	@GetMapping("/v2/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettingsV2(
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex,
			@ApiParam("Last updated timestamp") @RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@ApiParam("Registration center ID") @RequestParam(value = "regcenterId", required = false) String regCenterId,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion,
			@ApiParam("Full sync entities") @RequestParam(value = "fullSyncEntities", required = false) String fullSyncEntities)
			throws Throwable {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		MDC.put(KEY_INDEX_KEY, keyIndex == null ? NA : keyIndex);
		try {
			LocalDateTime currentTimeStamp = lastUpdated == null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
					syncJobHelperService.getDeltaSyncCurrentTimestamp();
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
			SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettingsV2(regCenterId, keyIndex,
					timestamp, currentTimeStamp, clientVersion, fullSyncEntities);
			syncDataResponseDto.setLastSyncTime(DateUtils2.formatToISOString(currentTimeStamp));
			return buildResponse(syncDataResponseDto);
		} finally {
			MDC.remove(KEY_INDEX_KEY);
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Downloads an MVEL script for a machine.
	 *
	 * @param scriptName    the script name
	 * @param keyIndex      the machine key index
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseEntity} containing the script content
	 * @throws Exception if script retrieval fails
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetscriptsscriptName())")
	@ApiOperation(value = "API to download mvel scripts")
	@GetMapping(value = "/scripts/{scriptName}")
	public ResponseEntity<?> downloadScript(
			@ApiParam("Script name") @PathVariable("scriptName") String scriptName,
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion)
			throws Exception {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			return syncConfigDetailsService.getScript(scriptName, keyIndex);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Downloads a client settings JSON file for a machine.
	 *
	 * @param entityIdentifier the entity identifier
	 * @param keyIndex         the machine key index
	 * @param clientVersion    the client version (optional)
	 * @return {@link ResponseEntity} containing the file content
	 * @throws Exception if file retrieval fails
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetclientsettingsentityIdentifier())")
	@ApiOperation(value = "API to download client settings JSON files")
	@GetMapping(value = "/clientsettings/{entityIdentifier}")
	public ResponseEntity<?> downloadEntityData(
			@ApiParam("Entity identifier") @PathVariable("entityIdentifier") String entityIdentifier,
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion)
			throws Exception {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			return masterDataService.getClientSettingsJsonFile(entityIdentifier, keyIndex);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Retrieves user details for a registration center based on key index (V2).
	 *
	 * @param keyIndex      the machine key index
	 * @param clientVersion the client version (optional)
	 * @return {@link ResponseWrapper} containing {@link SyncUserDto}
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetuserdetails())")
	@ResponseFilter
	@ApiOperation(value = "API to get user details by key index (V2)")
	@GetMapping("/v2/userdetails")
	public ResponseWrapper<SyncUserDto> getUserDetailsBasedOnKeyIndexV2(
			@ApiParam("Machine key index") @RequestParam(value = "keyindex", required = true) String keyIndex,
			@ApiParam("Client version") @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			String currentTimeStamp = DateUtils2.getUTCCurrentDateTimeString();
			SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
			syncUserDto.setLastSyncTime(currentTimeStamp);
			return buildResponse(syncUserDto);
		} finally {
			MDC.remove(CLIENT_VERSION_KEY);
		}
	}

	/**
	 * Builds a {@link ResponseWrapper} with the given response object.
	 *
	 * @param <T>      the response type
	 * @param response the response object
	 * @return the wrapped response
	 */
	private static <T> ResponseWrapper<T> buildResponse(T response) {
		ResponseWrapper<T> wrapper = new ResponseWrapper<>();
		wrapper.setResponse(response);
		return wrapper;
	}
}
