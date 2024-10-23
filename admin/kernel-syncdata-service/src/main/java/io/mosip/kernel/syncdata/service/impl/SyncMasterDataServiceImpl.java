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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Masterdata sync handler service impl
 * 
 * @author Abhishek Kumar
 * @author Bal Vikash Sharma
 * @author Srinivasan
 * @author Urvil Joshi
 * @since 1.0.0
 */
@Service
public class SyncMasterDataServiceImpl implements SyncMasterDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncMasterDataServiceImpl.class);

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


	@Override
	public SyncDataResponseDto syncClientSettings(String regCenterId, String keyIndex,
			LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		logger.info("syncClientSettings invoked for timespan from {} to {}", lastUpdated, currentTimestamp);
		SyncDataResponseDto response = new SyncDataResponseDto();
		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex);
		String machineId = regCenterMachineDto.getMachineId();
		String registrationCenterId = regCenterMachineDto.getRegCenterId();

		Map<Class, CompletableFuture> futureMap = clientSettingsHelper.getInitiateDataFetch(machineId, registrationCenterId,
				lastUpdated, currentTimestamp, false, lastUpdated!=null, null);

		CompletableFuture[] array = new CompletableFuture[futureMap.size()];
		CompletableFuture<Void> future = CompletableFuture.allOf(futureMap.values().toArray(array));

		try {
			future.join();
		} catch (CompletionException e) {
			logger.error("Failed to fetch data", e);
			if (e.getCause() instanceof SyncDataServiceException) {
				throw (SyncDataServiceException) e.getCause();
			} else {
				throw (RuntimeException) e.getCause();
			}
		}

		response.setDataToSync(clientSettingsHelper.retrieveData(futureMap, regCenterMachineDto, false));
		return response;
	}
	
	@Override
	public UploadPublicKeyResponseDto validateKeyMachineMapping(UploadPublicKeyRequestDto dto) {
		List<Machine> machines = machineRepo.findByMachineName(dto.getMachineName());
		
		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());
		
		if(machines.get(0).getPublicKey() == null || machines.get(0).getPublicKey().length() == 0)
			throw new RequestException(MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
					MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
		
		if(Arrays.equals(cryptomanagerUtils.decodeBase64Data(dto.getPublicKey()),
				cryptomanagerUtils.decodeBase64Data(machines.get(0).getPublicKey()))) {
			return new UploadPublicKeyResponseDto(machines.get(0).getKeyIndex());
		}
		
		throw new RequestException(MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorCode(),
				MasterDataErrorCode.MACHINE_PUBLIC_KEY_NOT_WHITELISTED.getErrorMessage());
	}
	
	@Override
	public JsonNode getLatestPublishedIdSchema(LocalDateTime lastUpdated, double schemaVersion, String domain,
			String type) {
		return identitySchemaHelper.getLatestIdentitySchema(lastUpdated, schemaVersion, domain, type);		
	}

	@Override
	public KeyPairGenerateResponseDto getCertificate(String applicationId, Optional<String> referenceId) {
		return keymanagerHelper.getCertificate(applicationId, referenceId);
	}

	@Override
	public ClientPublicKeyResponseDto getClientPublicKey(String machineId) {
		MachineResponseDto machineResponseDto = getMachineById(machineId);
		List<MachineDto> machines = machineResponseDto.getMachines();

		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		return new ClientPublicKeyResponseDto(machines.get(0).getSignPublicKey(), machines.get(0).getPublicKey());
	}

	@Override
	public CACertificates getPartnerCACertificates(LocalDateTime lastUpdated, LocalDateTime currentTimestamp) {
		CACertificates caCertificates = new CACertificates();
		caCertificates.setCertificateDTOList(new ArrayList<CACertificateDTO>());

		if (lastUpdated == null) {
			lastUpdated = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
		}

		List<CACertificateStore> certs = caCertificateStoreRepository.findAllLatestCreatedUpdateDeleted(lastUpdated, currentTimestamp);
		if(certs == null)
			return caCertificates;

		certs.forEach(cert -> {
				CACertificateDTO caCertDto = new CACertificateDTO();
				caCertDto.setCertId(cert.getCertId());
				caCertDto.setCertIssuer(cert.getCertIssuer());
				caCertDto.setCertSubject(cert.getCertSubject());
				caCertDto.setCertSerialNo(cert.getCertSerialNo());
				caCertDto.setCertNotAfter(cert.getCertNotAfter());
				caCertDto.setCertNotBefore(cert.getCertNotBefore());
				caCertDto.setCertThumbprint(cert.getCertThumbprint());
				caCertDto.setCertData(cert.getCertData());
				caCertDto.setIssuerId(cert.getIssuerId());
				caCertDto.setCreatedtimes(cert.getCreatedtimes());
				caCertDto.setPartnerDomain(cert.getPartnerDomain());
				caCertDto.setCrlUri(cert.getCrlUri());
				caCertDto.setCreatedBy(cert.getCreatedBy());
				caCertDto.setUpdatedBy(cert.getUpdatedBy());
				caCertDto.setUpdatedtimes(cert.getUpdatedtimes());
				caCertDto.setIsDeleted(cert.getIsDeleted());
				caCertDto.setDeletedtimes(cert.getDeletedtimes());
				caCertificates.getCertificateDTOList().add(caCertDto);
		});
		return caCertificates;
	}

	@Override
	public SyncDataResponseDto syncClientSettingsV2(String regCenterId, String keyIndex, LocalDateTime lastUpdated,
													LocalDateTime currentTimestamp, String clientVersion, String fullSyncEntities) {
		logger.info("syncClientSettingsV2 invoked for timespan from {} to {}", lastUpdated, currentTimestamp);
		SyncDataResponseDto response = new SyncDataResponseDto();
		RegistrationCenterMachineDto regCenterMachineDto = serviceHelper.getRegistrationCenterMachine(regCenterId, keyIndex);
		String machineId = regCenterMachineDto.getMachineId();
		String registrationCenterId = regCenterMachineDto.getRegCenterId();

		Map<Class, CompletableFuture> futureMap = clientSettingsHelper.getInitiateDataFetch(machineId, registrationCenterId,
				lastUpdated, currentTimestamp, true, lastUpdated!=null, fullSyncEntities);

		CompletableFuture[] array = new CompletableFuture[futureMap.size()];
		CompletableFuture<Void> future = CompletableFuture.allOf(futureMap.values().toArray(array));

		try {
			future.join();
		} catch (CompletionException e) {
			if (e.getCause() instanceof SyncDataServiceException) {
				throw (SyncDataServiceException) e.getCause();
			} else {
				throw (RuntimeException) e.getCause();
			}
		}

		List<SyncDataBaseDto> list = clientSettingsHelper.retrieveData(futureMap, regCenterMachineDto, true);
		list.addAll(clientSettingsHelper.getConfiguredScriptUrlDetail(regCenterMachineDto));
		response.setDataToSync(list);
		return response;
	}


	@Override
	public ResponseEntity getClientSettingsJsonFile(String entityIdentifier, String keyIndex)
			throws Exception {
		logger.info("getClientSettingsJsonFile({}) started for machine : {}", io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(entityIdentifier.replaceAll("[\n\r]", "_")),  io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(keyIndex.replaceAll("[\n\r]", "_")));
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		Boolean isEncrypted = environment.getProperty(String.format("mosip.sync.entity.encrypted.%s",
				entityIdentifier.toUpperCase().replaceAll("[\n\r]", "_")), Boolean.class, false);

		Path path = getEntityResource(entityIdentifier);
		String content = FileUtils.readFileToString(path.toFile(),	StandardCharsets.UTF_8);

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header("file-signature",
						keymanagerHelper.getFileSignature(HMACUtils2.digestAsPlainText(content.getBytes(StandardCharsets.UTF_8))))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entityIdentifier + "\"")
				.body(isEncrypted ?
						getEncryptedData(content.getBytes(StandardCharsets.UTF_8), machines.get(0).getPublicKey()) :
						content);
	}

	private Path getEntityResource(String entityIdentifier) throws FileNotFoundException {
		Path path = Path.of(clientSettingsDir, entityIdentifier).toAbsolutePath().normalize();
		if(path.toFile().exists())
			return path;

		throw new FileNotFoundException(MasterDataErrorCode.CLIENT_SETTINGS_DATA_FILE_NOT_FOUND.getErrorCode(),
				MasterDataErrorCode.CLIENT_SETTINGS_DATA_FILE_NOT_FOUND.getErrorMessage());
	}

	private String getEncryptedData(byte[] bytes, String publicKey) {
		try {
			TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
			tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(bytes));
			tpmCryptoRequestDto.setPublicKey(publicKey);
			TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
			return tpmCryptoResponseDto.getValue();
		} catch (Exception e) {
			logger.error("Failed to convert json to string", e);
		}
		throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
				SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
	}

	private MachineResponseDto getMachineById(String machineId) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(String.format(machineUrl, machineId));
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.build().toUri(), String.class);

			ResponseWrapper<MachineResponseDto> resp = objectMapper.readValue(responseEntity.getBody(),
					new TypeReference<ResponseWrapper<MachineResponseDto>>() {});

			if(resp.getErrors() != null && !resp.getErrors().isEmpty())
				throw new SyncInvalidArgumentException(resp.getErrors());

			return resp.getResponse();
		} catch (Exception e) {
			throw new SyncDataServiceException(MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage(),
					MasterDataErrorCode.MACHINE_DETAIL_FETCH_EXCEPTION.getErrorMessage() + " : " +
							ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}

}
