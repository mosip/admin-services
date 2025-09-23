package io.mosip.kernel.syncdata.service.helper;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final RestTemplate restTemplate;
    private final Environment environment;

    public ConfigServerClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Cacheable(
            cacheNames = "initial-sync",
            key = "'config:' + #fileName",
            sync = true,
            unless = "#result == null || #result.isEmpty()"
    )
    public String fetch(@NotNull String fileName) {
        LOGGER.info("fetch config file: {}", fileName);
        var uri = UriComponentsBuilder
                .fromUriString(environment.getProperty("spring.cloud.config.uri"))
                .path(SLASH).path(environment.getProperty("spring.application.name"))
                .path(SLASH).path(environment.getProperty("spring.profiles.active"))
                .path(SLASH).path(environment.getProperty("spring.cloud.config.label"))
                .path(SLASH).path(fileName)
                .toUriString();

        String response = restTemplate.getForObject(uri, String.class);
        if (response == null) {
            throw new RestClientException("Obtained null response from the config server");
        }
        return response;
    }
}