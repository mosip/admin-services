package io.mosip.kernel.syncdata.service;

import io.mosip.kernel.core.exception.IOException;
import io.mosip.kernel.syncdata.dto.ConfigDto;
import io.mosip.kernel.syncdata.dto.PublicKeyResponse;
import org.springframework.http.ResponseEntity;

/**
 * Configuration Sync service
 * 
 * @author Srinivasan
 * @author Bal Vikash Sharma
 * @since 1.0.0
 *
 */
public interface SyncConfigDetailsService {

	/**
	 * Function to get public key along with server active profile
	 * 
	 * @param applicationId applicationId
	 * @param timeStamp     timeStamp
	 * @param referenceId   referenceId
	 * @return {@link PublicKeyResponse} instance
	 */
	public PublicKeyResponse<String> getPublicKey(String applicationId, String timeStamp, String referenceId);

	/**
	 * This service will fetch all Configaration details available from server
	 * encrypt it based on machine key
	 *
	 * @return JSONObject - config synced data
	 */
	public ConfigDto getConfigDetails(String keyIndex);


	/**
	 * method to get scripts from config-repo
	 * @return
	 */
	public ResponseEntity getScript(String scriptName, String keyIndex) throws Exception;
}
