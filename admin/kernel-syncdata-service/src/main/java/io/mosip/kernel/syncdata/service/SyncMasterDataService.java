package io.mosip.kernel.syncdata.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.JsonNode;

import io.mosip.kernel.syncdata.dto.IdSchemaDto;
import io.mosip.kernel.syncdata.dto.UploadPublicKeyRequestDto;
import io.mosip.kernel.syncdata.dto.UploadPublicKeyResponseDto;
import io.mosip.kernel.syncdata.dto.response.*;

/**
 * Masterdata sync handler service
 * 
 * @author Abhishek Kumar
 * @author Urvil Joshi
 * @since 1.0.0
 *
 */
public interface SyncMasterDataService {

	/**
	 * @param regCenterId      - registration center id
	 * @param keyIndex         - registration client TPM EK public key SHA256 
	 * @param lastUpdated      - last updated time stamp
	 * @param currentTimestamp - current time stamp
	 * @return {@link SyncDataResponseDto}
	 * @throws InterruptedException - this method will throw execution exception
	 * @throws ExecutionException   -this method will throw interrupted exception
	 */	
	SyncDataResponseDto syncClientSettings(String regCenterId, String keyIndex,
			LocalDateTime lastUpdated, LocalDateTime currentTimestamp)
			throws InterruptedException, ExecutionException;


	
	/**
	 * Verifies machine name and public key mapping in machine_master table
	 * @param dto {@link UploadPublicKeyRequestDto}
	 * @return {@link UploadPublicKeyResponseDto}
	 */
	UploadPublicKeyResponseDto validateKeyMachineMapping(UploadPublicKeyRequestDto dto);
	
	/**
	 * fetches latest published Identity schema from masterdata-service
	 * @param lastUpdated
	 * @return
	 */
	JsonNode getLatestPublishedIdSchema(LocalDateTime lastUpdated, double schemaVersion, String domain, String type);

	/**
	 * Fetches certifcates from kernel-keymanager service based on the provided appId and refId
	 * @param applicationId
	 * @param referenceId
	 * @return
	 */
	KeyPairGenerateResponseDto getCertificate(String applicationId, Optional<String> referenceId);

	/**
	 * Fetches both signing and encryption public key
	 * @param machineId
	 * @return
	 */
	ClientPublicKeyResponseDto getClientPublicKey(String machineId);

	/**
	 * Fetches all partner CA certs
	 * @param lastUpdated
	 * @param currentTimestamp
	 */
	CACertificates getPartnerCACertificates(LocalDateTime lastUpdated, LocalDateTime currentTimestamp);

	/**
	 * Returns encrypted data rows or url to download data
	 * @param regCenterId      - registration center id
	 * @param keyIndex         - registration client TPM EK public key SHA256
	 * @param lastUpdated      - last updated time stamp
	 * @param currentTimestamp - current time stamp
	 * @return {@link SyncDataResponseDtoV2}
	 * @throws InterruptedException - this method will throw execution exception
	 * @throws ExecutionException   -this method will throw interrupted exception
	 */
	SyncDataResponseDtoV2 syncClientSettingsV2(String regCenterId, String keyIndex,
										   LocalDateTime lastUpdated, LocalDateTime currentTimestamp)
			throws InterruptedException, ExecutionException;
}
