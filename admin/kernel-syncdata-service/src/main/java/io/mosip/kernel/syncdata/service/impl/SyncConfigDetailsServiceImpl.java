package io.mosip.kernel.syncdata.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.HMACUtils2;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.constant.SyncConfigDetailsErrorCode;
import io.mosip.kernel.syncdata.dto.ConfigDto;
import io.mosip.kernel.syncdata.dto.PublicKeyResponse;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.SyncConfigDetailsService;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
import io.mosip.kernel.syncdata.utils.SyncMasterDataServiceHelper;
import jakarta.validation.constraints.NotNull;
import net.minidev.json.JSONObject;
import io.mosip.kernel.core.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Base64;

/**
 * Implementation class
 * 
 * @author Bal Vikash Sharma
 *
 */
@RefreshScope
@Service
public class SyncConfigDetailsServiceImpl implements SyncConfigDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncConfigDetailsServiceImpl.class);
	private static final String SLASH = "/";
	private static final String BEGIN_KEY = "-----BEGIN PUBLIC KEY-----";
	private static final String END_KEY = "-----END PUBLIC KEY-----";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * file name referred from the properties file
	 */
	@Value("${mosip.kernel.syncdata.registration-center-config-file}")
	private String regCenterfileName;

	/**
	 * file name referred from the properties file
	 */
	@Value("${mosip.kernel.syncdata.global-config-file}")
	private String globalConfigFileName;

	/**
	 * URL read from properties file
	 */
	@Value("${mosip.kernel.keymanager-service-publickey-url}")
	private String publicKeyUrl;

	@Autowired
	private ClientCryptoManagerService clientCryptoManagerService;

	@Autowired
	private MapperUtils mapper;

	@Autowired
	private MachineRepository machineRepo;

	@Autowired
	private Environment environment;

	@Value("#{'${mosip.registration.sync.scripts:applicanttype.mvel}'.split(',')}")
	private Set<String> scriptNames;

	@Value("${mosip.syncdata.clientsettings.data.dir:./clientsettings-dir}")
	private String clientSettingsDir;

	@Autowired
	private KeymanagerHelper keymanagerHelper;



	/**
	 * This method will consume a REST API based on the filename passed.
	 * 
	 * @param fileName - name of the file
	 * @return String
	 */
	private String getConfigDetailsResponse(@NotNull String fileName) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(environment.getProperty("spring.cloud.config.uri")).
				path(SLASH).path(environment.getProperty("spring.application.name")).
				path(SLASH).path(environment.getProperty("spring.profiles.active")).
				path(SLASH).path(environment.getProperty("spring.cloud.config.label")).
				path(SLASH).path(fileName);
		
		try {
			String str = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
			if (null == str)
				throw new RestClientException("Obtained null from the service");
			return str;
		} catch (RestClientException e) {
			LOGGER.error("Failed to getConfigDetailsResponse", e);
			throw new SyncDataServiceException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
		}
	}

	public JSONObject parsePropertiesString(String s) {
		JSONObject result = new JSONObject();
		try {
			final Properties p = new Properties();
			p.load(new StringReader(s));
			for (Entry<Object, Object> e : p.entrySet()) {
				result.put(String.valueOf(e.getKey()), e.getValue());
			}
		} catch (Exception ex) {
			LOGGER.error("Failed to parse config properties", ex);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.kernel.syncdata.service.SyncConfigDetailsService#getPublicKey(java.
	 * lang.String, java.lang.String, java.util.Optional)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PublicKeyResponse<String> getPublicKey(String applicationId, String timeStamp, String referenceId) {
		ResponseEntity<String> publicKeyResponseEntity = null;

		ResponseWrapper<PublicKeyResponse<String>> publicKeyResponseMapped = null;
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("applicationId", applicationId);
		try {
			// Query parameters
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(publicKeyUrl)
					// Add query parameter
					.queryParam("referenceId", referenceId).queryParam("timeStamp", timeStamp);

			publicKeyResponseEntity = restTemplate.getForEntity(builder.buildAndExpand(uriParams).toUri(),
					String.class);
			List<ServiceError> validationErrorsList = ExceptionUtils.getServiceErrorList(publicKeyResponseEntity.getBody());

			if (!validationErrorsList.isEmpty()) {
				throw new SyncInvalidArgumentException(validationErrorsList);
			}

		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			throw new SyncDataServiceException(
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " "
							+ ExceptionUtils.buildMessage(ex.getMessage(), ex.getCause()));
		}

		try {
			JsonNode root = objectMapper.readTree(publicKeyResponseEntity.getBody());
			String certificatePem = root.path("response").path("certificate").asText();
			String issuedAt = root.path("response").path("issuedAt").asText();
			String expiryAt = root.path("response").path("expiryAt").asText();

			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			ByteArrayInputStream certStream = new ByteArrayInputStream(certificatePem.getBytes(StandardCharsets.UTF_8));
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certStream);
			PublicKey publicKey = certificate.getPublicKey();

			String publicKeyPEM = convertToPEM(publicKey);

			OffsetDateTime issued = OffsetDateTime.parse(issuedAt);
			OffsetDateTime expiry = OffsetDateTime.parse(expiryAt);

			PublicKeyResponse<String> response = new PublicKeyResponse<>();
			response.setPublicKey(publicKeyPEM);
			response.setLastSyncTime(DateUtils.getUTCCurrentDateTimeString());
			response.setIssuedAt(issued.toLocalDateTime());
			response.setExpiryAt(expiry.toLocalDateTime());
			response.setProfile(environment.getActiveProfiles()[0]);

			return response;

		} catch (Exception e) {
			throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorMessage(), e);
		}

	}

	@Override
	public ConfigDto getConfigDetails(String keyIndex) {
		LOGGER.debug("getConfigDetails() started for machine keyIndex >>> {}", keyIndex.replaceAll("[\n\r]", "_"));
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if(machines == null || machines.isEmpty())
			machines = machineRepo.findByMachineName(keyIndex); //This is just for backward compatibility, since LTS

		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		LOGGER.info("getConfigDetails() started for machine : {} with status {}", keyIndex.replaceAll("[\n\r]", "_"),  machines.get(0).getIsActive());
		JSONObject config = new JSONObject();
		JSONObject globalConfig = new JSONObject();
		JSONObject regConfig = parsePropertiesString(getConfigDetailsResponse(regCenterfileName));
		//This is not completely removed only for backward compatibility, all the configs will be part of registrationConfiguration
		config.put("globalConfiguration", getEncryptedData(globalConfig, machines.get(0)));
		config.put("registrationConfiguration", getEncryptedData(regConfig, machines.get(0)));
		ConfigDto configDto = new ConfigDto();
		configDto.setConfigDetail(config);
		LOGGER.info("Get ConfigDetails() {} completed", keyIndex.replaceAll("[\n\r]", "_"));
		return configDto;
	}

	@Override
	public ResponseEntity getScript(String scriptName, String keyIndex) throws Exception {
		LOGGER.info("getScripts({}) started for machine : {}", io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(scriptName.replaceAll("[\n\r]", "_")), io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(keyIndex.replaceAll("[\n\r]", "_")));
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		Boolean isEncrypted = environment.getProperty(String.format("mosip.sync.entity.encrypted.%s",
				scriptName.toUpperCase().replaceAll("[\n\r]", "_")), Boolean.class, false);
		String content = getConfigDetailsResponse(scriptName);

		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.header("file-signature",
						keymanagerHelper.getFileSignature(HMACUtils2.digestAsPlainText(content.getBytes(StandardCharsets.UTF_8))))
				.body(isEncrypted ?	getEncryptedData(content, machines.get(0)) : content);
	}


	private String getEncryptedData(JSONObject config, Machine machine) {
		try {
			String json = mapper.getObjectAsJsonString(config);
			return getEncryptedData(json, machine);
		} catch (Exception e) {
			LOGGER.error("Failed to convert json to string", e);
		}
		throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
				SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
	}

	private String getEncryptedData(String data, Machine machine) {
		try {
			TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
			tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(data.getBytes()));
			tpmCryptoRequestDto.setPublicKey(machine.getPublicKey());
			tpmCryptoRequestDto.setClientType(SyncMasterDataServiceHelper.getClientType(machine));
			TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
			return tpmCryptoResponseDto.getValue();
		} catch (Exception e) {
			LOGGER.error("Failed to convert json to string", e);
		}
		throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
				SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
	}

	private String convertToPEM(PublicKey publicKey) {
		String encoded = CryptoUtil.encodeBase64String(publicKey.getEncoded());
		StringBuilder pemBuilder = new StringBuilder();
		pemBuilder.append(BEGIN_KEY).append("\n");
		for (int i = 0; i < encoded.length(); i += 64) {
			int endIndex = Math.min(i + 64, encoded.length());
			pemBuilder.append(encoded, i, endIndex).append("\n");
		}
		pemBuilder.append(END_KEY);
		return pemBuilder.toString();
	}

}
