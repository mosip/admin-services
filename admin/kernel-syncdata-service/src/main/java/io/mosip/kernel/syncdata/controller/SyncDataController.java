package io.mosip.kernel.syncdata.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.service.helper.SyncJobHelperService;
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
 * Sync Handler Controller
 * 
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
	 * Service instance {@link SyncMasterDataService}
	 */
	@Autowired
	private SyncMasterDataService masterDataService;

	/**
	 * Service instance {@link SyncConfigDetailsService}
	 */
	@Autowired
	SyncConfigDetailsService syncConfigDetailsService;

	@Autowired
	SyncUserDetailsService syncUserDetailsService;

	@Autowired
	LocalDateTimeUtil localDateTimeUtil;

	@Autowired
	SyncJobHelperService syncJobHelperService;


	
	/**
	 * 
	 * @param keyIndex     - keyIndex mapped to machine
	 * @param lastUpdated  - last sync updated time stamp
	 * @param regCenterId  - regcenterId mapped to machine
	 * @return {@link SyncDataResponseDto}
	 * @throws InterruptedException - this method will throw interrupted Exception
	 * @throws ExecutionException   - this method will throw exeution exception
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetclientsettings())")
	@ResponseFilter
	@GetMapping("/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettings(
			@RequestParam(value = "keyindex", required = true) String keyIndex,
			@RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@RequestParam(value = "regcenterId", required = false) String regCenterId)
			throws Throwable {

		LocalDateTime currentTimeStamp = lastUpdated==null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
				syncJobHelperService.getDeltaSyncCurrentTimestamp();
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		
		SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettings(regCenterId, keyIndex,
				timestamp, currentTimeStamp);

		syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));

		ResponseWrapper<SyncDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(syncDataResponseDto);
		return response;
	}
	

	/**
	 * Request mapping to get Public Key
	 * 
	 * @param applicationId Application id of the application requesting publicKey
	 * @param timeStamp     Timestamp of the request
	 * @param referenceId   Reference id of the application requesting publicKey
	 * @return {@link PublicKeyResponse} instance
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetpublickeyapplicationid())")
	@ResponseFilter
	@GetMapping(value = "/publickey/{applicationId}")
	public ResponseWrapper<PublicKeyResponse<String>> getPublicKey(
			@ApiParam("Id of application") @PathVariable("applicationId") String applicationId,
			@ApiParam("Timestamp as metadata") @RequestParam(value = "timeStamp", required = false) String timeStamp,
			@ApiParam("Refrence Id as metadata") @RequestParam(value = "referenceId", required = false) String referenceId) {

		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		PublicKeyResponse<String> publicKeyResponse = syncConfigDetailsService.getPublicKey(applicationId, timeStamp,
				referenceId);
		publicKeyResponse.setLastSyncTime(currentTimeStamp);

		ResponseWrapper<PublicKeyResponse<String>> response = new ResponseWrapper<>();
		response.setResponse(publicKeyResponse);
		return response;
	}


	/**
	 * Verifies mapping of input public key with any machine.
	 * if valid returns corresponding keyIndex.
	 * 
	 * @param uploadPublicKeyRequestDto
	 * @return
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGettpmpublickeyverify())")
	@ResponseFilter
	@PostMapping(value = "/tpm/publickey/verify", produces = "application/json")
	public ResponseWrapper<UploadPublicKeyResponseDto> validateKeyMachineMapping(
			@ApiParam("public key in BASE64 encoded") @RequestBody @Valid RequestWrapper<UploadPublicKeyRequestDto> uploadPublicKeyRequestDto,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		ResponseWrapper<UploadPublicKeyResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.validateKeyMachineMapping(uploadPublicKeyRequestDto.getRequest()));
		return response;
	}
	
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetlatestidschema())")
	@ResponseFilter
	@GetMapping(value = "/latestidschema", produces = "application/json")
	public ResponseWrapper<JsonNode> getLatestPublishedIdSchema(
			@RequestParam(value = "lastupdated", required = false) String lastUpdated,
			@RequestParam(value = "schemaVersion", defaultValue = "0", required = false) double schemaVersion,
			@RequestParam(name = "domain", required = false) String domain,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);		
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		/** If clientVersion, domain and type are null, it is assumed that the request is from regclient version < 1.2.0.*
		  With this assumption, "domain" is set to "registration-client" and "type" to "schema" for backward compatibility
		*/
		if (clientVersion == null && domain == null && type == null) {
			domain = "registration-client";
			type = "schema";
		}
		ResponseWrapper<JsonNode> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getLatestPublishedIdSchema(timestamp, schemaVersion, domain, type));
		return response;
	}

	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgetcertificate())")
	@ResponseFilter
	@GetMapping(value = "/getCertificate")
	public ResponseWrapper<KeyPairGenerateResponseDto> getCertificate(
			@ApiParam("Id of application") @RequestParam("applicationId") String applicationId,
			@ApiParam("Refrence Id as metadata") @RequestParam("referenceId") Optional<String> referenceId,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getCertificate(applicationId, referenceId));
		return response;
	}


	@PreAuthorize("hasAnyRole(@authorizedRoles.getGettpmpublickeymachineid())")
	@ResponseFilter
	@GetMapping(value = "/tpm/publickey/{machineId}", produces = "application/json")
	public ResponseWrapper<ClientPublicKeyResponseDto> getClientPublicKey(
			@ApiParam("Machine id") @PathVariable("machineId") String machineId,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		ResponseWrapper<ClientPublicKeyResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getClientPublicKey(machineId));
		return response;
	}

	/**
	 * This API method would fetch all synced global config details from server
	 *
	 * @return JSONObject - global config response
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetconfigskeyIndex())")
	@ResponseFilter
	@ApiOperation(value = "API to sync global config details")
	@GetMapping(value = "/configs/{keyIndex}")
	public ResponseWrapper<ConfigDto> getMachineConfigDetails(@PathVariable(value = "keyIndex") String keyIndex,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		ConfigDto syncConfigResponse = syncConfigDetailsService.getConfigDetails(keyIndex);
		syncConfigResponse.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<ConfigDto> response = new ResponseWrapper<>();
		response.setResponse(syncConfigResponse);
		return response;
	}

	/**
	 * API will get all the userDetails belonging to respective registration center based on keyindex provided
	 * @param keyIndex
	 * @return
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetuserdetails())")
	@ResponseFilter
	@GetMapping("/userdetails")
	public ResponseWrapper<SyncUserDto> getUserDetailsBasedOnKeyIndex(
			@RequestParam(value = "keyindex", required = true) String keyIndex) {
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndex(keyIndex);
		syncUserDto.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<SyncUserDto> response = new ResponseWrapper<>();
		response.setResponse(syncUserDto);
		return response;
	}

	/**
	 * Get all CA certificates from last updated time
	 * @param lastUpdated
	 * @return
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetgetcacertificates())")
	@ResponseFilter
	@GetMapping("/getcacertificates")
	public ResponseWrapper<CACertificates> getCACertificates(@RequestParam(value = "lastupdated",
			required = false) String lastUpdated, @RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		CACertificates caCertificates = masterDataService.getPartnerCACertificates(timestamp, currentTimeStamp);
		ResponseWrapper<CACertificates> response = new ResponseWrapper<>();
		caCertificates.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));
		response.setResponse(caCertificates);
		return response;
	}


	/**
	 *
	 * @param keyIndex     - keyIndex mapped to machine
	 * @param lastUpdated  - last sync updated time stamp
	 * @param regCenterId  - regcenterId mapped to machine
	 * @return {@link SyncDataResponseDto}
	 * @throws InterruptedException - this method will throw interrupted Exception
	 * @throws ExecutionException   - this method will throw exeution exception
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetv2clientsettings())")
	@ResponseFilter
	@GetMapping("/v2/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettingsV2(
			@RequestParam(value = "keyindex", required = true) String keyIndex,
			@RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@RequestParam(value = "regcenterId", required = false) String regCenterId,
			@RequestParam(value = "version", required = false) String clientVersion,
			@RequestParam(value = "fullSyncEntities", required = false) String fullSyncEntities)
			throws Throwable {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		MDC.put("key_index", keyIndex == null ? "NA": keyIndex);
		LocalDateTime currentTimeStamp = lastUpdated==null ? syncJobHelperService.getFullSyncCurrentTimestamp() :
				syncJobHelperService.getDeltaSyncCurrentTimestamp();
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettingsV2(regCenterId, keyIndex,
				timestamp, currentTimeStamp, clientVersion, fullSyncEntities);
		syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));
		ResponseWrapper<SyncDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(syncDataResponseDto);
		return response;
	}

	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetscriptsscriptName())")
	@ApiOperation(value = "API to download mvel scripts")
	@GetMapping(value = "/scripts/{scriptName}")
	public ResponseEntity downloadScript(@PathVariable(value = "scriptName") String scriptName,
														   @RequestParam(value = "keyindex", required = true) String keyIndex,
														   @RequestParam(value = "version", required = false) String clientVersion)
									throws Exception{
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		return syncConfigDetailsService.getScript(scriptName, keyIndex);
	}

	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetclientsettingsentityIdentifier())")
	@ApiOperation(value = "API to download data json files")
	@GetMapping(value = "/clientsettings/{entityIdentifier}")
	public ResponseEntity downloadEntityData(@PathVariable(value = "entityIdentifier") String entityIdentifier,
										 @RequestParam(value = "keyindex", required = true) String keyIndex,
										 @RequestParam(value = "version", required = false) String clientVersion)
			throws Exception {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		return masterDataService.getClientSettingsJsonFile(entityIdentifier, keyIndex);
	}

	/**
	 * API will get all the userDetails belonging to respective registration center based on keyindex provided
	 * @param keyIndex
	 * @return
	 */
	@PreAuthorize("hasAnyRole(@authorizedRoles.getGetuserdetails())")
	@ResponseFilter
	@GetMapping("/v2/userdetails")
	public ResponseWrapper<SyncUserDto> getUserDetailsBasedOnKeyIndexV2(
			@RequestParam(value = "keyindex", required = true) String keyIndex,
			@RequestParam(value = "version", required = false) String clientVersion) {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		SyncUserDto syncUserDto = syncUserDetailsService.getAllUserDetailsBasedOnKeyIndexV2(keyIndex);
		syncUserDto.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<SyncUserDto> response = new ResponseWrapper<>();
		response.setResponse(syncUserDto);
		return response;
	}

}
