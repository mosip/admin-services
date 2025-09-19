package io.mosip.kernel.syncdata.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoRequestDto;
import io.mosip.kernel.clientcrypto.dto.TpmCryptoResponseDto;
import io.mosip.kernel.clientcrypto.service.spi.ClientCryptoManagerService;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.HMACUtils2;
import io.mosip.kernel.syncdata.constant.MasterDataErrorCode;
import io.mosip.kernel.syncdata.constant.SyncConfigDetailsErrorCode;
import io.mosip.kernel.syncdata.constant.SyncDataConstant;
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
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * Implementation class for {@link SyncConfigDetailsService}.
 * <p>
 * Handles retrieval and encryption of configuration details, public keys, and scripts for client machines.
 * Uses separate validation functions to ensure input integrity and throws standardized exceptions
 * ({@link SyncDataServiceException}, {@link RequestException}, {@link SyncInvalidArgumentException}).
 * </p>
 *
 * @author Bal Vikash Sharma
 * @since 1.0.0
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

    @Autowired(required = false)
    private CacheMetricsRegistrar cacheMetricsRegistrar;

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void bindCacheMetrics() {
        if (cacheMetricsRegistrar != null) {
            Cache cache = cacheManager.getCache(SyncDataConstant.CACHE_NAME_SYNC_DATA);
            if (cache != null) {
                cacheMetricsRegistrar.bindCacheToRegistry(cache);
                LOGGER.info("Bound cache '{}' to Micrometer metrics",
                        SyncDataConstant.CACHE_NAME_SYNC_DATA);
            } else {
                LOGGER.warn("Could not bind cache metrics: cache '{}' not found",
                        SyncDataConstant.CACHE_NAME_SYNC_DATA);
            }
        } else {
            LOGGER.warn("CacheMetricsRegistrar not present; cache metrics may remain zero");
        }
    }

    /**
     * Validates inputs for the {@link #getPublicKey} method.
     *
     * @param applicationId the application ID
     * @param timeStamp     the timestamp
     * @throws SyncInvalidArgumentException if inputs are null or empty
     */
    private void validatePublicKeyInputs(String applicationId, String timeStamp) {
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new RequestException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Application ID is null or empty");
        }
        if (timeStamp == null || timeStamp.trim().isEmpty()) {
            throw new RequestException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Timestamp is null or empty");
        }
    }

    /**
     * Validates the key index for the {@link #getConfigDetails} and {@link #getScript} methods.
     *
     * @param keyIndex the machine key index or name
     * @throws RequestException if the key index is null, empty, or no matching machine is found
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
     * Validates the script name for the {@link #getScript} method.
     *
     * @param scriptName the script name
     * @throws SyncInvalidArgumentException if the script name is null, empty, or not in the allowed list
     */
    private void validateScriptName(String scriptName) {
        if (scriptName == null || scriptName.trim().isEmpty()) {
            throw new RequestException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Script name is null or empty");
        }
       /* if (!scriptNames.contains(scriptName)) {
            throw new RequestException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_INPUT_PARAMETER_EXCEPTION.getErrorMessage() + " Invalid script name: " + scriptName.replaceAll("[\n\r]", "_"));
        }*/
    }

    /**
     * Fetches configuration details from the Spring Cloud Config server.
     *
     * @param fileName the name of the configuration file
     * @return the configuration content as a string
     * @throws SyncDataServiceException if the request fails or the response is null
     */
    @Cacheable(value = SyncDataConstant.CACHE_NAME_SYNC_DATA, key = "#fileName")
    public String getConfigDetailsResponse(@NotNull String fileName) {
        LOGGER.info("getConfigDetailsResponse fileName :{}", fileName);
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new SyncDataServiceException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " File name is null or empty");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(environment.getProperty("spring.cloud.config.uri")).
                path(SLASH).path(environment.getProperty("spring.application.name")).
                path(SLASH).path(environment.getProperty("spring.profiles.active")).
                path(SLASH).path(environment.getProperty("spring.cloud.config.label")).
                path(SLASH).path(fileName);

        try {
            LOGGER.debug("Fetching config from URL: {}", uriBuilder.toUriString());
            String str = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            if (response == null) {
                throw new RestClientException("Obtained null response from the config server");
            }
            return response;
        } catch (RestClientException e) {
            LOGGER.error("Failed to fetch config for file {}: {}", fileName, e.getMessage());
            throw new SyncDataServiceException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " "
                            + ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
        }
    }

    /**
     * Parses a properties string into a JSON object.
     *
     * @param properties the properties string
     * @return the parsed JSON object
     */
    public JSONObject parsePropertiesString(String properties) {
        if (properties == null || properties.trim().isEmpty()) {
            LOGGER.warn("Properties string is null or empty");
            return new JSONObject();
        }
        JSONObject result = new JSONObject();
        try {
            final Properties p = new Properties();
            p.load(new StringReader(properties));
            for (Entry<Object, Object> e : p.entrySet()) {
                result.put(String.valueOf(e.getKey()), e.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse properties string: {}", e.getMessage());
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
    @SuppressWarnings({"rawtypes", "unchecked"})
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
            LOGGER.error("Failed to fetch public key: {}", ex.getMessage());
            throw new SyncDataServiceException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " "
                            + ExceptionUtils.buildMessage(ex.getMessage(), ex.getCause()));
        }

        try {
            publicKeyResponseMapped = objectMapper.readValue(publicKeyResponseEntity.getBody(),
                    new TypeReference<ResponseWrapper<PublicKeyResponse<String>>>() {
                    });

            publicKeyResponseMapped.getResponse().setProfile(environment.getActiveProfiles()[0]);
            LOGGER.debug("Public key fetched successfully for applicationId: {}", applicationId);
            return publicKeyResponseMapped.getResponse();
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Failed to parse public key response: {}", e.getMessage());
            throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_IO_EXCEPTION.getErrorMessage(), e);
        }
    }

    /**
     * Retrieves and encrypts configuration details for a machine.
     *
     * @param keyIndex the machine key index or name
     * @return the {@link ConfigDto} containing encrypted global and registration configurations
     * @throws RequestException         if no machine is found
     * @throws SyncDataServiceException if fetching or encryption fails
     */
    @Override
    public ConfigDto getConfigDetails(String keyIndex) {
        Machine machine = validateKeyIndexAndGetMachine(keyIndex);
        LOGGER.debug("Fetching config details for machine keyIndex: {}, machine: {}",
                keyIndex.replaceAll("[\n\r]", "_"), machine.getName());

        JSONObject config = new JSONObject();
        JSONObject globalConfig = new JSONObject();
        JSONObject regConfig = parsePropertiesString(getConfigDetailsResponse(regCenterfileName));
        //This is not completely removed only for backward compatibility, all the configs will be part of registrationConfiguration
        config.put("globalConfiguration", getEncryptedData(globalConfig, machine));
        config.put("registrationConfiguration", getEncryptedData(regConfig, machine));

        ConfigDto configDto = new ConfigDto();
        configDto.setConfigDetail(config);
        LOGGER.debug("Get ConfigDetails() {} completed", keyIndex.replaceAll("[\n\r]", "_"));
        return configDto;
    }

    @CacheEvict(value = {SyncDataConstant.CACHE_NAME_SYNC_DATA, SyncDataConstant.CACHE_NAME_INITIAL_SYNC}, allEntries = true)
    public void clearConfigCache() {
        LOGGER.info("Cleared all entries in caches: syncDataConfigCache, initial-sync");
    }

    /**
     * Retrieves a script, optionally encrypted, for a machine.
     *
     * @param scriptName the name of the script
     * @param keyIndex   the machine key index or name
     * @return a {@link ResponseEntity} containing the script content
     * @throws RequestException             if no machine is found
     * @throws SyncInvalidArgumentException if the script name is invalid
     * @throws SyncDataServiceException     if fetching or encryption fails
     */
    @Override
    public ResponseEntity getScript(String scriptName, String keyIndex) throws Exception {
        validateScriptName(scriptName);
        Machine machine = validateKeyIndexAndGetMachine(keyIndex);
        LOGGER.debug("Fetching script {} for machine keyIndex: {}",
                scriptName.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"));

        String content = getConfigDetailsResponse(scriptName);
        Boolean isEncrypted = environment.getProperty(String.format("mosip.sync.entity.encrypted.%s",
                scriptName.toUpperCase().replaceAll("[\n\r]", "_")), Boolean.class, false);

        String responseBody = isEncrypted ? getEncryptedData(content, machine) : content;
        String fileSignature = keymanagerHelper.getFileSignature(
                HMACUtils2.digestAsPlainText(content.getBytes(StandardCharsets.UTF_8)));

        LOGGER.debug("Script {} fetched for machine: {}, encrypted: {}",
                scriptName.replaceAll("[\n\r]", "_"), keyIndex.replaceAll("[\n\r]", "_"), isEncrypted);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header("file-signature", fileSignature)
                .body(responseBody);
    }

    /**
     * Encrypts a JSON object using the machine's public key.
     *
     * @param config  the JSON configuration
     * @param machine the machine entity
     * @return the encrypted data
     * @throws SyncDataServiceException if JSON serialization or encryption fails
     */
    private String getEncryptedData(JSONObject config, Machine machine) {
        try {
            String json = mapper.getObjectAsJsonString(config);
            return getEncryptedData(json, machine);
        } catch (Exception e) {
            LOGGER.error("Failed to serialize JSON for encryption: {}", e.getMessage());
            throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
        }
    }

    /**
     * Encrypts a string using the machine's public key.
     *
     * @param data    the data to encrypt
     * @param machine the machine entity
     * @return the encrypted data
     * @throws SyncDataServiceException if encryption fails
     */
    private String getEncryptedData(String data, Machine machine) {
        try {
            TpmCryptoRequestDto tpmCryptoRequestDto = new TpmCryptoRequestDto();
            tpmCryptoRequestDto.setValue(CryptoUtil.encodeToURLSafeBase64(data.getBytes()));
            tpmCryptoRequestDto.setPublicKey(machine.getPublicKey());
            tpmCryptoRequestDto.setClientType(SyncMasterDataServiceHelper.getClientType(machine));
            TpmCryptoResponseDto tpmCryptoResponseDto = clientCryptoManagerService.csEncrypt(tpmCryptoRequestDto);
            return tpmCryptoResponseDto.getValue();
        } catch (Exception e) {
            LOGGER.error("Failed to encrypt data: {}", e.getMessage());
            throw new SyncDataServiceException(SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_SERIALIZATION_ERROR.getErrorMessage());
        }
    }
}