package io.mosip.kernel.syncdata.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.HMACUtils2;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.entity.Machine;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.repository.MachineRepository;
import io.mosip.kernel.syncdata.service.helper.KeymanagerHelper;
import io.mosip.kernel.syncdata.utils.MapperUtils;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.syncdata.constant.SyncConfigDetailsErrorCode;
import io.mosip.kernel.syncdata.dto.ConfigDto;
import io.mosip.kernel.syncdata.dto.PublicKeyResponse;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.syncdata.exception.SyncInvalidArgumentException;
import io.mosip.kernel.syncdata.service.SyncConfigDetailsService;
import net.minidev.json.JSONObject;

import javax.validation.constraints.NotNull;

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
			publicKeyResponseMapped = objectMapper.readValue(publicKeyResponseEntity.getBody(),
					new TypeReference<ResponseWrapper<PublicKeyResponse<String>>>() {
					});

		} catch (IOException | NullPointerException e) {
			throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorCode(),
					SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorMessage(), e);
		}

		publicKeyResponseMapped.getResponse().setProfile(environment.getActiveProfiles()[0]);
		return publicKeyResponseMapped.getResponse();

	}

	@Override
	public ConfigDto getConfigDetails(String keyIndex) {
		LOGGER.debug("getConfigDetails() started for machine keyIndex >>> {}", keyIndex);
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if(machines == null || machines.isEmpty())
			machines = machineRepo.findByMachineName(keyIndex); //This is just for backward compatibility, since LTS

		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		LOGGER.info("getConfigDetails() started for machine : {} with status {}", keyIndex,  machines.get(0).getIsActive());
		JSONObject config = new JSONObject();
		JSONObject globalConfig = parsePropertiesString(getConfigDetailsResponse(globalConfigFileName));
		JSONObject regConfig = parsePropertiesString(getConfigDetailsResponse(regCenterfileName));
		config.put("globalConfiguration", getEncryptedData(globalConfig, machines.get(0).getPublicKey()));
		config.put("registrationConfiguration", getEncryptedData(regConfig, machines.get(0).getPublicKey()));
		ConfigDto configDto = new ConfigDto();
		configDto.setConfigDetail(config);
		LOGGER.info("Get ConfigDetails() {} completed", keyIndex);
		return configDto;
	}

	@Override
	public ResponseEntity getScript(String scriptName, String keyIndex) throws Exception {
		LOGGER.info("getScripts({}) started for machine : {}", io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(scriptName), io.mosip.kernel.syncdata.utils.ExceptionUtils.neutralizeParam(keyIndex));
		List<Machine> machines = machineRepo.findByMachineKeyIndex(keyIndex);
		if(machines == null || machines.isEmpty())
			throw new RequestException(MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorCode(),
					MasterDataErrorCode.MACHINE_NOT_FOUND.getErrorMessage());

		Boolean isEncrypted = environment.getProperty(String.format("mosip.sync.entity.encrypted.%s",
				scriptName.toUpperCase()), Boolean.class, false);
		String content = getConfigDetailsResponse(scriptName);

		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.header("file-signature",
						keymanagerHelper.getFileSignature(HMACUtils2.digestAsPlainText(content.getBytes(StandardCharsets.UTF_8))))
				.body(isEncrypted ?	getEncryptedData(content, machines.get(0).getPublicKey()) : content);
	}


	private String getEncryptedData(JSONObject config, String publicKey) {
		try {
			String json = mapper.getObjectAsJsonString(config);
			return getEncryptedData(json, publicKey);
		} catch (Exception e) {
			LOGGER.error("Failed to convert json to string", e);
		}
		throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
				SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
	}

	private String getEncryptedData(String data, String publicKey) {
		try {
			TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
			tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(data.getBytes()));
			tpmCryptoRequestDto.setPublicKey(publicKey);
			TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
			return tpmCryptoResponseDto.getValue();
		} catch (Exception e) {
			LOGGER.error("Failed to convert json to string", e);
		}
		throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
				SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
	}


}
