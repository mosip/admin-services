package io.mosip.kernel.syncdata.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.FileNotFoundException;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.FileUtils;
import io.mosip.kernel.core.util.HMACUtils2;
import io.mosip.kernel.cryptomanager.util.CryptomanagerUtils;
import io.mosip.kernel.keymanagerservice.entity.CACertificateStore;
import io.mosip.kernel.keymanagerservice.repository.CACertificateStoreRepository;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.constant.SyncConfigDetailsErrorCode;
import io.mosip.kernel.syncdata.dto.*;
import io.mosip.kernel.syncdata.dto.response.*;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.repository.ModuleDetailRepository;
import io.mosip.kernel.syncdata.service.SyncMasterDataService;
import io.mosip.kernel.syncdata.service.helper.ClientSettingsHelper;
import io.mosip.kernel.syncdata.service.helper.IdentitySchemaHelper;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;

/**
 * Implementation of {@link SyncMasterDataService} for synchronizing client settings and certificates.
 * <p>
 * Handles client settings synchronization, public key validation, identity schema retrieval, certificate management,
 * and client settings file serving. Includes input validation functions to ensure data integrity and throws standardized
 * exceptions ({@link SyncDataServiceException}, {@link RequestException}, {@link SyncInvalidArgumentException}).
 * </p>
 *
 * @author Abhishek Kumar
 * @author Bal Vikash Sharma
 * @author Srinivasan
 * @author Urvil Joshi
 * @since 1.0.0
 */
@Service
public class SyncMasterDataServiceImpl implements SyncMasterDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncMasterDataServiceImpl.class);

	@Autowired
	private SyncMasterDataServiceHelper serviceHelper;

	@Autowired
	private MachineRepository machineRepo;

	@Autowired
	private MapperUtils mapper;

	@Autowired
	private IdentitySchemaHelper identitySchemaHelper;

	@Autowired
	private KeymanagerHelper keymanagerHelper;

	@Value("${mosip.kernel.syncdata-service-machine-url}")
	private String machineUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CACertificateStoreRepository caCertificateStoreRepository;

	@Autowired
	private ModuleDetailRepository moduleDetailRepository;

	@Autowired
	private ClientSettingsHelper clientSettingsHelper;

	@Value("${mosip.syncdata.clientsettings.data.dir:./_SNAPSHOTS}")
	private String clientSettingsDir;

	@Autowired
	private Environment environment;

	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	@Autowired
	private CryptomanagerUtils cryptomanagerUtils;

	/**
	 * Validates inputs for {@link #syncClientSettings} and {@link #syncClientSettingsV2}.
	 *
	 * @param regCenterId     the registration center ID
	 * @param keyIndex        the machine key index
	 * @param lastUpdated     the last updated timestamp
	 * @param currentTimestamp the current timestamp
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateSyncClientSettingsInputs(String regCenterId, String keyIndex,
												  LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		if (regCenterId == null || regCenterId.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Registration center ID is null or empty");
		}
		if (keyIndex == null || keyIndex.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Key index is null or empty");
		}
		if (currentTimestamp == null) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Current timestamp is null");
		}
		if (lastUpdated != null && lastUpdated.isAfter(currentTimestamp)) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() +
							" Last updated timestamp is after current timestamp");
		}
	}

	/**
	 * Validates inputs for {@link #validateKeyMachineMapping}.
	 *
	 * @param dto the {@link UploadPublicKeyRequestDto}
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateKeyMachineMappingInputs(UploadPublicKeyRequestDto dto) {
		if (dto == null) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Request DTO is null");
		}
		if (dto.getMachineName() == null || dto.getMachineName().trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Machine name is null or empty");
		}
		if (dto.getPublicKey() == null || dto.getPublicKey().trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Public key is null or empty");
		}
	}

	/**
	 * Validates inputs for {@link #getLatestPublishedIdSchema}.
	 *
	 * @param domain the domain
	 * @param type   the type
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateIdSchemaInputs(String domain, String type) {
		if (domain != null && domain.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Domain is empty");
		}
		if (type != null && type.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Type is empty");
		}
	}

	/**
	 * Validates inputs for {@link #getCertificate}.
	 *
	 * @param applicationId the application ID
	 * @param referenceId   the optional reference ID
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateCertificateInputs(String applicationId, Optional<String> referenceId) {
		if (applicationId == null || applicationId.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Application ID is null or empty");
		}
		if (referenceId.isPresent() && referenceId.get().trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Reference ID is empty");
		}
	}

	/**
	 * Validates inputs for {@link #getClientPublicKey}.
	 *
	 * @param machineId the machine ID
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateClientPublicKeyInputs(String machineId) {
		if (machineId == null || machineId.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Machine ID is null or empty");
		}
	}

	/**
	 * Validates inputs for {@link #getClientSettingsJsonFile}.
	 *
	 * @param entityIdentifier the entity identifier
	 * @param keyIndex         the machine key index
	 * @throws SyncInvalidArgumentException if inputs are invalid
	 */
	private void validateClientSettingsJsonFileInputs(String entityIdentifier, String keyIndex) {
		if (entityIdentifier == null || entityIdentifier.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Entity identifier is null or empty");
		}
		if (keyIndex == null || keyIndex.trim().isEmpty()) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Key index is null or empty");
		}
		// Sanitize entityIdentifier to prevent path traversal
		if (entityIdentifier.contains("..") || entityIdentifier.contains("/") || entityIdentifier.contains("\\")) {
			throw new RequestException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Invalid entity identifier: contains path traversal characters");
		}
	}

	/**
	 * Validates the machine key index and retrieves the machine entity.
	 *
	 * @param keyIndex the machine key index
	 * @return the {@link Machine} entity
	 * @throws RequestException if no machine is found
	 */
	private Machine validateKeyIndexAndGetMachine(String keyIndex) {
		if (keyIndex == null || keyIndex.trim().isEmpty()) {
			throw new RequestException(
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage() + " Key index is null or empty");
		}
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if (machines == null || machines.isEmpty()) {
			machines = machineRepo.findByMachineName(keyIndex); // Backward compatibility
		}
		if (machines == null || machines.isEmpty()) {
			throw new RequestException(
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage() + " No machine found for key index: " + keyIndex.replaceAll("[\n\r]", "_"));
		}
		return machines.get(0);
	}

	/**
	 * Validates the machine name and retrieves the machine entity.
	 *
	 * @param machineName the machine name
	 * @return the {@link Machine} entity
	 * @throws RequestException if no machine is found
	 */
	private Machine validateMachineNameAndGetMachine(String machineName) {
		if (machineName == null || machineName.trim().isEmpty()) {
			throw new RequestException(
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage() + " Key index is null or empty");
		}
		List<Machine> machines = machineRepo.findByMachineName(machineName);
		if (machines == null || machines.isEmpty()) {
			throw new RequestException(
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage() + " No machine found for key index: " + machineName.replaceAll("[\n\r]", "_"));
		}
		Machine machine = machines.get(0);
		if (machine.getPublicKey() == null || machine.getPublicKey().trim().isEmpty()) {
			throw new RequestException(
					MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
					MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
		}

		return machine;
	}
	/**
	 * Synchronizes client settings for a registration center and machine.
	 *
	 * @param regCenterId     the registration center ID
	 * @param keyIndex        the machine key index
	 * @param lastUpdated     the last updated timestamp
	 * @param currentTimestamp the current timestamp
	 * @return the {@link SyncDataResponseDto} containing synchronized data
	 * @throws SyncDataServiceException if data fetching fails
	 */
	@Override
	public SyncDataResponseDto syncClientSettings(String regCenterId, String keyIndex,
												  LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		//validateSyncClientSettingsInputs(regCenterId, keyIndex, lastUpdated, currentTimestamp);
		String safeKeyIndex = keyIndex.replace('\n', '_').replace('\r', '_');
		LOGGER.info("syncClientSettings invoked for regCenterId: {}, keyIndex: {}, timespan {} to {}",
				regCenterId, safeKeyIndex, lastUpdated, currentTimestamp);

		RegistrationCenterMachineDto regCenterMachineDto =
				serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex);

		Map<Class<?>, CompletableFuture<?>> futureMap =
				clientSettingsHelper.getInitiateDataFetch(
						regCenterMachineDto.getMachineId(),
						regCenterMachineDto.getRegCenterId(),
						lastUpdated,
						currentTimestamp,
						false,
						lastUpdated != null,
						null);

		// Wait for all futures once
		CompletableFuture.allOf(futureMap.values().toArray(CompletableFuture[]::new)).join();

		SyncDataResponseDto response = new SyncDataResponseDto();
		response.setDataToSync(
				clientSettingsHelper.retrieveData(futureMap, regCenterMachineDto, false));
		return response;
	}

	/**
	 * Validates the mapping between a machine and its public key.
	 *
	 * @param dto the {@link UploadPublicKeyRequestDto} containing machine name and public key
	 * @return the {@link UploadPublicKeyResponseDto} with the key index
	 * @throws RequestException if the machine or public key is invalid
	 */
	@Override
	public UploadPublicKeyResponseDto validateKeyMachineMapping(UploadPublicKeyRequestDto dto) {
		Machine machine = validateMachineNameAndGetMachine(dto.getMachineName());

		if(Arrays.equals(cryptomanagerUtils.decodeBase64Data(dto.getPublicKey()),
				cryptomanagerUtils.decodeBase64Data(machine.getPublicKey()))) {
			LOGGER.info("Key mapping validated for machine: {}", dto.getMachineName().replaceAll("[\n\r]", "_"));
			return new UploadPublicKeyResponseDto(machine.getKeyIndex());
		}

		throw new RequestException(MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
				MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
	}

	/**
	 * Retrieves the latest published identity schema.
	 *
	 * @param lastUpdated   the last updated timestamp
	 * @param schemaVersion the schema version
	 * @param domain        the domain
	 * @param type          the type
	 * @return the {@link JsonNode} containing the schema
	 */
	@Override
	public JsonNode getLatestPublishedIdSchema(LocalDateTime lastUpdated, double schemaVersion, String domain,
											   String type) {
		LOGGER.debug("Fetching latest identity schema for domain: {}, type: {}, version: {}, lastUpdated: {}",
				domain, type, schemaVersion, lastUpdated);
		JsonNode schema = identitySchemaHelper.getLatestIdentitySchema(lastUpdated, schemaVersion, domain, type);
		LOGGER.debug("Fetched identity schema for domain: {}, type: {}", domain, type);
		return schema;
	}

	/**
	 * Retrieves a certificate for an application.
	 *
	 * @param applicationId the application ID
	 * @param referenceId   the optional reference ID
	 * @return the {@link KeyPairGenerateResponseDto} containing the certificate
	 */
	@Override
	public KeyPairGenerateResponseDto getCertificate(String applicationId, Optional<String> referenceId) {
		LOGGER.debug("Fetching certificate for applicationId: {}, referenceId: {}",
				applicationId, referenceId.orElse("null"));
		KeyPairGenerateResponseDto response = keymanagerHelper.getCertificate(applicationId, referenceId);
		LOGGER.debug("Certificate fetched for applicationId: {}", applicationId);
		return response;
	}

	/**
	 * Retrieves the public key for a client machine.
	 *
	 * @param machineId the machine ID
	 * @return the {@link ClientPublicKeyResponseDto} containing the public key
	 * @throws RequestException if the machine is not found
	 */
	@Override
	public ClientPublicKeyResponseDto getClientPublicKey(String machineId) {
		LOGGER.debug("Fetching client public key for machineId: {}", machineId);
		MachineResponseDto machineResponseDto = getMachineById(machineId);
		List<MachineDto> machines = machineResponseDto.getMachines();

		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		LOGGER.debug("Client public key fetched for machineId: {}", machineId);
		return new ClientPublicKeyResponseDto(machines.get(0).getSignPublicKey(), machines.get(0).getPublicKey());
	}

	/**
	 * Retrieves partner CA certificates within a time range.
	 *
	 * @param lastUpdated     the last updated timestamp
	 * @param currentTimestamp the current timestamp
	 * @return the {@link CACertificates} containing the certificates
	 */
	@Override
	public CACertificates getPartnerCACertificates(LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		LOGGER.debug("Fetching CA certificates from {} to {}", lastUpdated, currentTimestamp);
		final long t0 = System.nanoTime();

		if (lastUpdated == null) {
			lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
		}

		final long t1 = System.nanoTime();
		List<CACertificateStore> certs = caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimestamp);
		final long t2 = System.nanoTime();

		final CACertificates caCertificates = new CACertificates();
		caCertificates.setCertificateDTOList(new ArrayList<>());

		if(certs == null){
			LOGGER.warn("No CA certificates found for the given time range lastUpdated={} currentTimestamp={}",
					lastUpdated, currentTimestamp);
			return caCertificates;
		}
		else {
			for (CACertificateStore cert : certs) {
				CACertificateDTO dto = new CACertificateDTO();
				dto.setCertId(cert.getCertId());
				dto.setCertIssuer(cert.getCertIssuer());
				dto.setCertSubject(cert.getCertSubject());
				dto.setCertSerialNo(cert.getCertSerialNo());
				dto.setCertNotAfter(cert.getCertNotAfter());
				dto.setCertNotBefore(cert.getCertNotBefore());
				dto.setCertThumbprint(cert.getCertThumbprint());
				dto.setCertData(cert.getCertData());
				dto.setIssuerId(cert.getIssuerId());
				dto.setCreatedtimes(cert.getCreatedtimes());
				dto.setPartnerDomain(cert.getPartnerDomain());
				dto.setCrlUri(cert.getCrlUri());
				dto.setCreatedBy(cert.getCreatedBy());
				dto.setUpdatedBy(cert.getUpdatedBy());
				dto.setUpdatedtimes(cert.getUpdatedtimes());
				dto.setIsDeleted(cert.getIsDeleted());
				dto.setDeletedtimes(cert.getDeletedtimes());
				caCertificates.getCertificateDTOList().add(dto);
			}
		}
		final long t3 = System.nanoTime();
		// --- timings ---
		long sanitizeMs = Duration.ofNanos(t1 - t0).toMillis();
		long queryMs    = Duration.ofNanos(t2 - t1).toMillis();
		long mapMs      = Duration.ofNanos(t3 - t2).toMillis();
		long totalMs    = Duration.ofNanos(t3 - t0).toMillis();

		int count = caCertificates.getCertificateDTOList().size();
		long avgMapUs = (count == 0) ? 0 : Duration.ofNanos((t3 - t2) / count).toNanos() / 1_000;

		LOGGER.info("syncdata.cacerts timings | sanitize={}ms | query={}ms | map={}ms (avg={}µs/item) | total={}ms | range=[{}, {}] | count={}",
				sanitizeMs, queryMs, mapMs, avgMapUs, totalMs, lastUpdated, currentTimestamp, count);

		LOGGER.info("Fetched {} CA certificates", count);
		return caCertificates;
	}

	/**
	 * Synchronizes client settings with additional parameters (V2).
	 *
	 * @param regCenterId      the registration center ID
	 * @param keyIndex         the machine key index
	 * @param lastUpdated      the last updated timestamp
	 * @param currentTimestamp  the current timestamp
	 * @param clientVersion     the client version
	 * @param fullSyncEntities the entities for full sync
	 * @return the {@link SyncDataResponseDto} containing synchronized data
	 * @throws SyncDataServiceException if data fetching fails
	 */
	@Override
	public SyncDataResponseDto syncClientSettingsV2(String regCenterId, String keyIndex, LocalDateTime lastUpdated,
													LocalDateTime currentTimestamp, String clientVersion, String fullSyncEntities) {
		LOGGER.info("syncClientSettingsV2 invoked for regCenterId: {}, keyIndex: {}, timespan from {} to {}, clientVersion: {}",
				regCenterId, keyIndex.replaceAll("[\n\r]", "_"), lastUpdated, currentTimestamp, clientVersion);
		SyncDataResponseDto response = new SyncDataResponseDto();
		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex);
		String machineId = regCenterMachineDto.getMachineId();
		String registrationCenterId = regCenterMachineDto.getRegCenterId();
		LOGGER.info("Fetched RegistrationCenterMachineDto - machineId: {}, regCenterId: {}", machineId, registrationCenterId);


		Map<Class<?>, CompletableFuture<?>> futureMap = clientSettingsHelper.getInitiateDataFetch(
				machineId, registrationCenterId,
				lastUpdated, currentTimestamp, true, lastUpdated!=null, fullSyncEntities);
		LOGGER.info("Initiated data fetch tasks: {}", futureMap.keySet());


		CompletableFuture[] array = new CompletableFuture[futureMap.size()];
		LOGGER.info("Size for futureMap: {}", futureMap.size());
		CompletableFuture<Void> future = CompletableFuture.allOf(futureMap.values().toArray(array));

		/*try {
			// Add timeout for debugging
			future.get(60, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			LOGGER.error("Timeout waiting for async tasks. Checking which futures are incomplete:");
			futureMap.forEach((cls, f) ->
					LOGGER.error("Task {} -> done={}, exceptional={}",
							cls.getSimpleName(), f.isDone(), f.isCompletedExceptionally())
			);
			throw new SyncDataServiceException("KER-SYNC-001", "Timeout waiting for async tasks", te);
		} catch (ExecutionException ee) {
			throw new SyncDataServiceException("KER-SYNC-002", "Async task failed", ee.getCause());
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			throw new SyncDataServiceException("KER-SYNC-003", "Async task interrupted", ie);
		}*/

		try {
			future.join();
		} catch (CompletionException e) {
			LOGGER.error("Failed to fetch client settings V2 for regCenterId: {}, keyIndex: {}",
					regCenterId, keyIndex.replaceAll("[\n\r]", "_"), e);
			if (e.getCause() instanceof SyncDataServiceException) {
				throw (SyncDataServiceException) e.getCause();
			} else {
				throw (RuntimeException) e.getCause();
			}
		}

		LOGGER.info("future is finished");

		List<SyncDataBaseDto> list = new ArrayList<>(clientSettingsHelper.retrieveData(futureMap, regCenterMachineDto, true));
		LOGGER.info("Retrieved {} data items for sync (base)", list.size());
		list.addAll(clientSettingsHelper.getConfiguredScriptUrlDetail(regCenterMachineDto));
		LOGGER.info("Retrieved {} script URL items for sync", list.size());
		response.setDataToSync(list);
		LOGGER.info("syncClientSettingsV2 completed for regCenterId: {}, keyIndex: {}",
				regCenterId, keyIndex.replaceAll("[\n\r]", "_"));
		return response;
	}

	/**
	 * Retrieves a client settings JSON file, optionally encrypted.
	 *
	 * @param entityIdentifier the entity identifier
	 * @param keyIndex         the machine key index
	 * @return a {@link ResponseEntity} containing the file content
	 * @throws RequestException if the machine is not found
	 * @throws FileNotFoundException if the file is not found
	 */
	@Override
	public ResponseEntity getClientSettingsJsonFile(String entityIdentifier, String keyIndex)
			throws Exception {
		LOGGER.debug("getClientSettingsJsonFile invoked for entity: {}, keyIndex: {}",
				entityIdentifier.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));
		Machine machine = validateKeyIndexAndGetMachine(keyIndex);

		Boolean isEncrypted = environment.getProperty(String.format("mosip.sync.entity.encrypted.%s",
				entityIdentifier.toUpperCase().replaceAll("[\n\r]", "_")), Boolean.class, false);

		Path path = getEntityResource(entityIdentifier);
		String content = FileUtils.readFileToString(path.toFile(),StandardCharsets.UTF_8);

		LOGGER.debug("Fetched client settings file {} for machine: {}, encrypted: {}",
				entityIdentifier.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"), isEncrypted);

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header("file-signature",
						keymanagerHelper.getFileSignature(HMACUtils2.digestAsPlainText(content.getBytes(StandardCharsets.UTF_8))))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entityIdentifier + "\"")
				.body(isEncrypted ?
						getEncryptedData(content.getBytes(StandardCharsets.UTF_8), machine.getPublicKey()) :
						content);
	}

	/**
	 * Resolves and validates the file path for an entity resource.
	 *
	 * @param entityIdentifier the entity identifier
	 * @return the resolved {@link Path}
	 * @throws FileNotFoundException if the file does not exist
	 */
	private Path getEntityResource(String entityIdentifier) throws FileNotFoundException {
		Path path = Path.of(clientSettingsDir, entityIdentifier).toAbsolutePath().normalize();
		if(path.toFile().exists())
			return path;

		throw new FileNotFoundException(MasterDataErrorCode.CLIENT_SETTINGS_DATA_FILE_NOT_FOUND.getErrorCode(),
				MasterDataErrorCode.CLIENT_SETTINGS_DATA_FILE_NOT_FOUND.getErrorMessage());
	}

	/**
	 * Encrypts data using the provided public key.
	 *
	 * @param bytes     the data to encrypt
	 * @param publicKey the public key
	 * @return the encrypted data
	 * @throws SyncDataServiceException if encryption fails
	 */

	private String getEncryptedData(byte[] bytes, String publicKey) {
		try {
			TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
			tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(bytes));
			tpmCryptoRequestDto.setPublicKey(publicKey);
			TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
			return tpmCryptoResponseDto.getValue();
		} catch (Exception e) {
			LOGGER.error("Failed to encrypt data: {}", e.getMessage());
			throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
		}
	}

	/**
	 * Fetches machine details by ID from an external service.
	 *
	 * @param machineId the machine ID
	 * @return the {@link MachineResponseDto} containing machine details
	 * @throws SyncDataServiceException if the request fails
	 */
	private MachineResponseDto getMachineById(String machineId) {
		try {
			String url = String.format(machineUrl, machineId);
			LOGGER.debug("Fetching machine details from URL: {}", url);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

			ResponseWrapper<MachineResponseDto> resp = objectMapper.readValue(responseEntity.getBody(),
					new TypeReference<ResponseWrapper<MachineResponseDto>>() {});

			if(resp.getErrors() != null && !resp.getErrors().isEmpty()) {
				LOGGER.error("Machine details bot found: {}", machineId);
				throw new SyncInvalidArgumentException(resp.getErrors());
			}

			LOGGER.debug("Machine details fetched for machineId: {}", machineId);
			return resp.getResponse();
		} catch (Exception e) {
			LOGGER.error("Failed to fetch machine details for machineId: {}: {}", machineId, e.getMessage());
			throw new SyncDataServiceException(MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage(),
					MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage() + " : " +
							ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}
}