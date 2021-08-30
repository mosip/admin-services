package io.mosip.kernel.syncdata.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import io.mosip.kernel.core.exception.FileNotFoundException;
import io.mosip.kernel.core.exception.IOException;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

	/*
	@Autowired
	SyncRolesService syncRolesService;*/

	@Autowired
	SyncUserDetailsService syncUserDetailsService;

	@Autowired
	LocalDateTimeUtil localDateTimeUtil;

	/**
	 * This API method would fetch all synced global config details from server
	 * 
	 * @return JSONObject - global config response
	 */
	/*@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@ApiOperation(value = "API to sync global config details")
	@GetMapping(value = "/configs")
	public ResponseWrapper<ConfigDto> getConfigDetails() {
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		ConfigDto syncConfigResponse = syncConfigDetailsService.getConfigDetails();
		syncConfigResponse.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<ConfigDto> response = new ResponseWrapper<>();
		response.setResponse(syncConfigResponse);
		return response;
	}

	*//**
	 * This API method would fetch all synced global config details from server
	 * 
	 * @return JSONObject - global config response
	 *//*
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN')")
	@ResponseFilter
	@ApiIgnore
	@ApiOperation(value = "API to sync global config details")
	@GetMapping(value = "/globalconfigs")
	public ResponseWrapper<JSONObject> getGlobalConfigDetails() {
		ResponseWrapper<JSONObject> response = new ResponseWrapper<>();
		response.setResponse(syncConfigDetailsService.getGlobalConfigDetails());
		return response;
	}

	*//**
	 * * This API method would fetch all synced registration center config details
	 * from server
	 * 
	 * @param regId registration Id
	 * @return JSONObject
	 *//*
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@ApiIgnore
	@ApiOperation(value = "Api to get registration center configuration")
	@GetMapping(value = "/registrationcenterconfig/{registrationcenterid}")
	public ResponseWrapper<JSONObject> getRegistrationCentreConfig(
			@PathVariable(value = "registrationcenterid") String regId) {
		ResponseWrapper<JSONObject> response = new ResponseWrapper<>();
		response.setResponse(syncConfigDetailsService.getRegistrationCenterConfigDetails(regId));
		return response;
	}

	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@ApiIgnore
	@GetMapping("/configuration/{registrationCenterId}")
	public ResponseWrapper<ConfigDto> getConfiguration(
			@PathVariable("registrationCenterId") String registrationCenterId) {
		ResponseWrapper<ConfigDto> response = new ResponseWrapper<>();
		response.setResponse(syncConfigDetailsService.getConfiguration(registrationCenterId));
		return response;
	}*/
	
	/**
	 * 
	 * @param keyIndex     - keyIndex mapped to machine
	 * @param lastUpdated  - last sync updated time stamp
	 * @param regCenterId  - regcenterId mapped to machine
	 * @return {@link SyncDataResponseDto}
	 * @throws InterruptedException - this method will throw interrupted Exception
	 * @throws ExecutionException   - this method will throw exeution exception
	 */
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettings(
			@RequestParam(value = "keyindex", required = true) String keyIndex,
			@RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@RequestParam(value = "regcenterId", required = false) String regCenterId)
			throws Throwable {

		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		
		SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettings(regCenterId, keyIndex,
				timestamp, currentTimeStamp);

		syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));

		ResponseWrapper<SyncDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(syncDataResponseDto);
		return response;
	}
	
	/**
	 * 
	 * @param keyIndex     - keyIndex mapped to machine
	 * @param regCenterId  - reg Center Id
	 * @param lastUpdated  - last sync updated time stamp
	 * @return {@link SyncDataResponseDto}
	 * @throws InterruptedException - this method will throw interrupted Exception
	 * @throws ExecutionException   - this method will throw exeution exception
	 */
	/*@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/clientsettings/{regcenterid}")
	@Deprecated(forRemoval = true, since = "1.1.5")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettingsWithRegCenterId(
			@PathVariable("regcenterid") String regCenterId,
			@RequestParam(value = "lastupdated", required = false) String lastUpdated,
			@RequestParam(value = "keyindex", required = true) String keyIndex)
			throws InterruptedException, ExecutionException {

		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		
		SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettings(regCenterId, keyIndex,
				timestamp, currentTimeStamp);

		syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));

		ResponseWrapper<SyncDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(syncDataResponseDto);
		return response;
	}*/
	

	/**
	 * API will fetch all roles from Auth server
	 * 
	 * @return RolesResponseDto
	 */
	/*@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/roles")
	public ResponseWrapper<RolesResponseDto> getAllRoles() {
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		RolesResponseDto rolesResponseDto = syncRolesService.getAllRoles();
		rolesResponseDto.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<RolesResponseDto> response = new ResponseWrapper<>();
		response.setResponse(rolesResponseDto);
		return response;
	}*/

	/**
	 * API will all the userDetails from LDAP server
	 * 
	 * @param regId - registration center Id
	 * 
	 * @return UserDetailResponseDto - user detail response
	 */
	/*@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/userdetails/{regcenterid}")
	public ResponseWrapper<SyncUserDetailDto> getUserDetails(@PathVariable("regcenterid") String regId) {
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		SyncUserDetailDto syncUserDetailDto = syncUserDetailsService.getAllUserDetail(regId);
		syncUserDetailDto.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<SyncUserDetailDto> response = new ResponseWrapper<>();
		response.setResponse(syncUserDetailDto);
		return response;
	}*/

	/**
	 * API will all the userDetails from LDAP server
	 * 
	 * @param regId - registration center Id
	 * 
	 * @return UserDetailResponseDto - user detail response
	 */
	/*@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/usersalt/{regid}")
	@Deprecated
	public ResponseWrapper<SyncUserSaltDto> getUserSalts(@PathVariable("regid") String regId) {
		String currentTimeStamp = DateUtils.getUTCCurrentDateTimeString();
		SyncUserSaltDto syncUserDetailDto = syncUserDetailsService.getUserSalts(regId);
		syncUserDetailDto.setLastSyncTime(currentTimeStamp);
		ResponseWrapper<SyncUserSaltDto> response = new ResponseWrapper<>();
		response.setResponse(syncUserDetailDto);
		return response;
	}*/

	/**
	 * Request mapping to get Public Key
	 * 
	 * @param applicationId Application id of the application requesting publicKey
	 * @param timeStamp     Timestamp of the request
	 * @param referenceId   Reference id of the application requesting publicKey
	 * @return {@link PublicKeyResponse} instance
	 */
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
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
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@PostMapping(value = "/tpm/publickey/verify", produces = "application/json")
	public ResponseWrapper<UploadPublicKeyResponseDto> validateKeyMachineMapping(
			@ApiParam("public key in BASE64 encoded") @RequestBody @Valid RequestWrapper<UploadPublicKeyRequestDto> uploadPublicKeyRequestDto) {
		ResponseWrapper<UploadPublicKeyResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.validateKeyMachineMapping(uploadPublicKeyRequestDto.getRequest()));
		return response;
	}
	
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','REGISTRATION_PROCESSOR','ID_AUTHENTICATION','RESIDENT','INDIVIDUAL','Default')")
	@ResponseFilter
	@GetMapping(value = "/latestidschema", produces = "application/json")
	public ResponseWrapper<JsonNode> getLatestPublishedIdSchema(
			@RequestParam(value = "lastupdated", required = false) String lastUpdated,
			@RequestParam(value = "schemaVersion", defaultValue = "0", required = false) double schemaVersion,
			@RequestParam(name = "domain", required = false) String domain,
			@RequestParam(name = "type", required = false) String type) {
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		
		ResponseWrapper<JsonNode> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getLatestPublishedIdSchema(timestamp, schemaVersion, domain, type));
		return response;
	}

	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping(value = "/getCertificate")
	public ResponseWrapper<KeyPairGenerateResponseDto> getCertificate(
			@ApiParam("Id of application") @RequestParam("applicationId") String applicationId,
			@ApiParam("Refrence Id as metadata") @RequestParam("referenceId") Optional<String> referenceId) {

		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getCertificate(applicationId, referenceId));
		return response;
	}


	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','REGISTRATION_PROCESSOR')")
	@ResponseFilter
	@GetMapping(value = "/tpm/publickey/{machineId}", produces = "application/json")
	public ResponseWrapper<ClientPublicKeyResponseDto> getClientPublicKey(
			@ApiParam("Machine id") @PathVariable("machineId") String machineId) {
		ResponseWrapper<ClientPublicKeyResponseDto> response = new ResponseWrapper<>();
		response.setResponse(masterDataService.getClientPublicKey(machineId));
		return response;
	}

	/**
	 * This API method would fetch all synced global config details from server
	 *
	 * @return JSONObject - global config response
	 */
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@ApiOperation(value = "API to sync global config details")
	@GetMapping(value = "/configs/{keyIndex}")
	public ResponseWrapper<ConfigDto> getMachineConfigDetails(@PathVariable(value = "keyIndex") String keyIndex) {
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
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
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
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/getcacertificates")
	public ResponseWrapper<CACertificates> getCACertificates(@RequestParam(value = "lastupdated",
			required = false) String lastUpdated) {
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
	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','REGISTRATION_ADMIN','Default')")
	@ResponseFilter
	@GetMapping("/v2/clientsettings")
	public ResponseWrapper<SyncDataResponseDto> syncClientSettingsV2(
			@RequestParam(value = "keyindex", required = true) String keyIndex,
			@RequestParam(value = "lastUpdated", required = false) String lastUpdated,
			@RequestParam(value = "regcenterId", required = false) String regCenterId,
			@RequestParam(value = "version", required = false) String clientVersion)
			throws Throwable {
		MDC.put("client_version", clientVersion == null ? "NA": clientVersion);
		LocalDateTime currentTimeStamp = LocalDateTime.now(ZoneOffset.UTC);
		LocalDateTime timestamp = localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimeStamp, lastUpdated);
		SyncDataResponseDto syncDataResponseDto = masterDataService.syncClientSettingsV2(regCenterId, keyIndex,
				timestamp, currentTimeStamp, clientVersion);
		syncDataResponseDto.setLastSyncTime(DateUtils.formatToISOString(currentTimeStamp));
		ResponseWrapper<SyncDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(syncDataResponseDto);
		return response;
	}

	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','Default')")
	@ApiOperation(value = "API to download mvel scripts")
	@GetMapping(value = "/scripts/{scriptName}")
	public ResponseEntity downloadScript(@PathVariable(value = "scriptName") String scriptName,
														   @RequestParam(value = "keyindex", required = true) String keyIndex) {
		return syncConfigDetailsService.getScript(scriptName, keyIndex);
	}

	@PreAuthorize("hasAnyRole('REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','Default')")
	@ApiOperation(value = "API to download data json files")
	@GetMapping(value = "/clientsettings/{entityIdentifier}")
	public ResponseEntity downloadEntityData(@PathVariable(value = "entityIdentifier") String entityIdentifier,
										 @RequestParam(value = "keyindex", required = true) String keyIndex)
			throws IOException {
		return masterDataService.getClientSettingsJsonFile(entityIdentifier, keyIndex);
	}

}
