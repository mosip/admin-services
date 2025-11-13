package io.mosip.kernel.syncdata.service.helper;

import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.syncdata.constant.SyncConfigDetailsErrorCode;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ConfigServerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServerClient.class);
    private static final String SLASH = "/";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Environment environment;

    @Cacheable(
            cacheNames = "initial-sync",
            key = "'config:' + #fileName",
            unless = "#result == null || #result.isEmpty()"
    )
    public String fetch(@NotNull String fileName) {
        LOGGER.info("fetch config file: {}", fileName);
        String response = null;
        try {
            var uri = UriComponentsBuilder
                    .fromUriString(environment.getProperty("spring.cloud.config.uri"))
                    .path(SLASH).path(environment.getProperty("spring.application.name"))
                    .path(SLASH).path(environment.getProperty("spring.profiles.active"))
                    .path(SLASH).path(environment.getProperty("spring.cloud.config.label"))
                    .path(SLASH).path(fileName)
                    .toUriString();

            response = restTemplate.getForObject(uri, String.class);
            if (response == null) {
                throw new RestClientException("Obtained null response from the config server");
            }
        }
        catch (RestClientException e) {
            LOGGER.error("Failed to fetch config for file {}: {}", fileName, e.getMessage());
            throw new SyncDataServiceException(
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorCode(),
                    SyncConfigDetailsErrorCode.SYNC_CONFIG_DETAIL_REST_CLIENT_EXCEPTION.getErrorMessage() + " "
                            + ExceptionUtils.buildMessage(e.getMessage(), e.getCause()));
        }
        return response;
    }
}
