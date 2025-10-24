package io.mosip.kernel.syncdata.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import jakarta.validation.Valid;

import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.syncdata.service.SyncConfigDetailsService;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.service.SyncUserDetailsService;
import io.mosip.kernel.syncdata.utils.LocalDateTimeUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataController.class);

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
			LOGGER.debug("syncClientSettings invoked with keyIndex: {}, regCenterId: {}, lastUpdated: {}",
					keyIndex.replaceAll("[\n\r]", "_"), regCenterId, lastUpdated);

			LocalDateTime currentTimeStamp = lastUpdated == null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
					syncJobHelperService.getDeltaSyncCurrentTimestamp();
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);

			SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettings(regCenterId, keyIndex,
					timestamp, currentTimeStamp);

			syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));

			LOGGER.debug("syncClientSettings completed for keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
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
		LOGGER.debug("getPublicKey invoked with applicationId: {}, timeStamp: {}, referenceId: {}",
				applicationId, timeStamp, referenceId);
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		PublicKeyResponse<String> publicKeyResponse = syncConfigDetailsService.getPublicKey(applicationId, timeStamp,
				referenceId);
		publicKeyResponse.setLastSyncTime(currentTimeStamp);

		LOGGER.debug("getPublicKey completed for applicationId: {}", applicationId);
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
		LOGGER.debug("validateKeyMachineMapping invoked with clientVersion: {}", clientVersion);
		MDC.put(CLIENT_VERSION_KEY, clientVersion == null ? NA : clientVersion);
		try {
			UploadPublicKeyResponseDto responseDto = masterDataService.validateKeyMachineMapping(uploadPublicKeyRequestDto.getRequest());
			LOGGER.debug("validateKeyMachineMapping completed for machine: {}",
					uploadPublicKeyRequestDto.getRequest().getMachineName().replaceAll("[\n\r]", "_"));
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
		LOGGER.debug("getLatestPublishedIdSchema invoked with lastUpdated: {}, schemaVersion: {}, domain: {}, type: {}",
				lastUpdated, schemaVersion, domain, type);
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
			LOGGER.debug("getLatestPublishedIdSchema completed for domain: {}, type: {}", domain, type);
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
			LOGGER.debug("getCertificate invoked with applicationId: {}, referenceId: {}",
					applicationId, referenceId.orElse("null"));
			KeyPairGenerateResponseDto responseDto = masterDataService.getCertificate(applicationId, referenceId);
			LOGGER.debug("getCertificate completed for applicationId: {}", applicationId);
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
			LOGGER.debug("getClientPublicKey invoked with machineId: {}", machineId);
			ClientPublicKeyResponseDto responseDto = masterDataService.getClientPublicKey(machineId);
			LOGGER.debug("getClientPublicKey completed for machineId: {}", machineId);
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
			LOGGER.debug("getMachineConfigDetails invoked with keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
			String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
			ConfigDto configDto = syncConfigDetailsService.getConfigDetails(keyIndex);
			configDto.setLastSyncTime(currentTimeStamp);
			LOGGER.info("getMachineConfigDetails completed for keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
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
			LOGGER.debug("getUserDetailsBasedOnKeyIndex invoked with keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
			String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
			SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndex(keyIndex);
			syncUserDto.setLastSyncTime(currentTimeStamp);
			LOGGER.debug("getUserDetailsBasedOnKeyIndex completed for keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
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
			LOGGER.debug("getCACertificates invoked with lastUpdated: {}", lastUpdated);
			LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
			CACertificates caCertificates = masterDataService.getPartnerCACertificates(timestamp, currentTimeStamp);
			caCertificates.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));

			LOGGER.info("getCACertificates completed with {} certificates", caCertificates.getCertificateDTOList().size());
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
		LOGGER.info("Request received to sync client settings V2");
		LOGGER.info("Parameters: keyIndex={}, regCenterId={}, lastUpdated={}, clientVersion={}, fullSyncEntities={}",
				keyIndex.replaceAll("[\n\r]", "_"), regCenterId, lastUpdated, clientVersion, fullSyncEntities);
		try {
			LocalDateTime currentTimeStamp = lastUpdated == null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
					syncJobHelperService.getDeltaSyncCurrentTimestamp();
			LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
			LOGGER.info("Calculated timestamps - current: {}, parsed lastUpdated: {}", currentTimeStamp, timestamp);
			SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettingsV2(regCenterId, keyIndex,
					timestamp, currentTimeStamp, clientVersion, fullSyncEntities);
			syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));
			LOGGER.info("Sync completed successfully for keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
			return buildResponse(syncDataResponseDto);
		} catch (Exception e) {
			LOGGER.error("Error during syncClientSettingsV2 for keyIndex: {}: {}", keyIndex, e.getMessage(), e);
			throw e;
		} finally {
			MDC.remove(KEY_INDEX_KEY);
			MDC.remove(CLIENT_VERSION_KEY);
			LOGGER.info("MDC keys cleared after request processing for syncClientSettingsV2");
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
			LOGGER.debug("downloadScript invoked with scriptName: {}, keyIndex: {}",
					scriptName.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));
			ResponseEntity<?> response = syncConfigDetailsService.getScript(scriptName, keyIndex);
			LOGGER.debug("downloadScript completed for scriptName: {}, keyIndex: {}",
					scriptName.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));
			return response;
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
			LOGGER.debug("downloadEntityData invoked with entityIdentifier: {}, keyIndex: {}",
					entityIdentifier.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));
			ResponseEntity<?> response = masterDataService.getClientSettingsJsonFile(entityIdentifier, keyIndex);
			LOGGER.debug("downloadEntityData completed for entityIdentifier: {}, keyIndex: {}",
					entityIdentifier.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));
			return response;
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
			LOGGER.debug("getUserDetailsBasedOnKeyIndexV2 invoked with keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
			String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
			SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
			syncUserDto.setLastSyncTime(currentTimeStamp);
			LOGGER.debug("getUserDetailsBasedOnKeyIndexV2 completed for keyIndex: {}", keyIndex.replaceAll("[\n\r]", "_"));
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
