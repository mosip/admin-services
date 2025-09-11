package io.mosip.kernel.syncdata.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.partnercertservice.dto.CACertificateRequestDto;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.syncdata.constant.SyncAuthErrorCode;
import io.mosip.kernel.syncdata.exception.SyncDataServiceException;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * REST controller for handling WebSub callbacks related to CA certificate updates.
 * <p>
 * This controller processes WebSub callback events for CA certificates, validating the partner domain
 * and retrieving certificate data from a data-share URL. It integrates with the
 * {@link PartnerCertificateManagerService} to upload CA certificates and is optimized for performance
 * by caching constants, minimizing JSON parsing overhead, and using efficient error handling.
 * The controller supports only allowed partner domains and handles errors gracefully with minimal
 * logging overhead.
 * </p>
 *
 * @author MOSIP Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/websub/callback")
public class WebsubCallbackController {

    /**
     * Logger instance for logging errors and debugging information.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsubCallbackController.class);

    /**
     * Key for the certificate data share URL in the event data.
     */
    private static final String CERTIFICATE_DATA_SHARE_URL = "certChainDatashareUrl";

    /**
     * Key for the partner domain in the event data.
     */
    private static final String PARTNER_DOMAIN = "partnerDomain";

    /**
     * Type reference for parsing ResponseWrapper with JsonNode.
     */
    private static final TypeReference<ResponseWrapper<JsonNode>> RESPONSE_WRAPPER_TYPE =
            new TypeReference<ResponseWrapper<JsonNode>>() {};

    /**
     * Service for managing CA certificate operations.
     */
    @Autowired
    private PartnerCertificateManagerService partnerCertificateManagerService;

    /**
     * List of allowed partner domains, configured via properties.
     */
    @Value("#{'${mosip.syncdata.partner.allowed.domains:FTM,DEVICE}'.split(',')}")
    private List<String> partnerAllowedDomains;

    /**
     * RestTemplate for making HTTP requests, configured with self-token authentication.
     */
    @Autowired
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate restTemplate;

    /**
     * ObjectMapper for JSON serialization and deserialization.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Handles WebSub callback events for CA certificate updates.
     * <p>
     * This method processes incoming WebSub events containing CA certificate data. It validates the
     * partner domain against the allowed domains and retrieves the certificate data from the provided
     * data-share URL. If the data is valid and error-free, it uploads the certificate using the
     * {@link PartnerCertificateManagerService}. The method is optimized for performance with cached
     * constants, efficient JSON parsing, and minimal logging overhead. It logs errors for debugging
     * and handles exceptions gracefully.
     * </p>
     *
     * @param eventModel the WebSub event containing certificate data and metadata
     * @throws IllegalArgumentException if the event data is invalid or missing required fields
     */
    @PostMapping(value = "/cacert", consumes = "application/json")
    @PreAuthenticateContentAndVerifyIntent(secret = "${syncdata.websub.callback.secret.ca-cert}",
            callback = "/v1/syncdata/websub/callback/cacert", topic = "${syncdata.websub.topic.ca-cert}")
    public void handleCACertificate(@RequestBody EventModel eventModel) {
        LOGGER.debug("Received CA certificate event");
        try {
            Map<String, Object> data = eventModel.getEvent().getData();
            if (data == null) {
                throw new SyncDataServiceException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                        SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage() + ": Event data is null");
            }

            String partnerDomain = (String) data.get(PARTNER_DOMAIN);
            String dataShareUrl = (String) data.get(CERTIFICATE_DATA_SHARE_URL);

            if (partnerDomain == null || dataShareUrl == null) {
                throw new SyncDataServiceException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                        SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage() +
                                ": Missing partnerDomain or certChainDatashareUrl");
            }

            if (!partnerAllowedDomains.contains(partnerDomain)) {
                LOGGER.error("Invalid partner domain: {}", partnerDomain);
                throw new SyncDataServiceException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                        SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage() + ": Invalid partner domain: " + partnerDomain);
            }

            String certificateData = restTemplate.getForObject(dataShareUrl, String.class);
            if (certificateData == null) {
                LOGGER.error("Failed to retrieve certificate data from URL: {}", dataShareUrl);
                throw new SyncDataServiceException(SyncAuthErrorCode.ERROR_GETTING_TOKEN.getErrorCode(),
                        SyncAuthErrorCode.ERROR_GETTING_TOKEN.getErrorMessage() +
                                ": Null certificate data from URL: " + dataShareUrl);
            }

            ServiceError serviceError = parseDataShareResponse(certificateData);
            if (serviceError == null) {
                CACertificateRequestDto caCertRequestDto = new CACertificateRequestDto();
                caCertRequestDto.setPartnerDomain(partnerDomain);
                caCertRequestDto.setCertificateData(certificateData);
                partnerCertificateManagerService.uploadCACertificate(caCertRequestDto);
                LOGGER.info("CA certificate sync completed for domain: {}", partnerDomain);
            } else {
                LOGGER.error("Failed to get certificate data from data-share service: {}", serviceError.getErrorCode());
                throw new SyncDataServiceException(SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorCode(),
                        SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorMessage() +
                                ": Data-share service error: " + serviceError.getErrorCode());
            }
        } catch (RestClientException e) {
            LOGGER.error("Failed to retrieve certificate data from URL: {}", e.getMessage());
            throw new SyncDataServiceException(SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorCode(),
                    SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorMessage() + ": REST call failed", e);
        } catch (SyncDataServiceException e) {
            LOGGER.error("Failed to process CA certificate event: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error processing CA certificate event: {}", e.getMessage());
            throw new SyncDataServiceException(SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorCode(),
                    SyncAuthErrorCode.ERROR_HANDLE_CA_CERTIFICATE.getErrorMessage() + ": Unexpected error", e);
        }
    }

    /**
     * Parses the data-share response to extract certificate data or errors.
     * <p>
     * This method parses the JSON response from the data-share service to check for errors.
     * If errors are present, it returns the first {@link ServiceError}; otherwise, it returns
     * null to indicate success. The method is optimized for performance by using efficient
     * JSON parsing and minimal logging.
     * </p>
     *
     * @param response the JSON response from the data-share service
     * @return the first {@link ServiceError} if present, or null if the response is valid
     */
    private ServiceError parseDataShareResponse(String response) {
        try {
            ResponseWrapper<JsonNode> resp = objectMapper.readValue(response,
                    new TypeReference<ResponseWrapper<JsonNode>>() {});
            if(resp.getErrors() != null && !resp.getErrors().isEmpty())
                return resp.getErrors().get(0);
        } catch (Exception e) {
            LOGGER.error("Failed to parse data-share response", e);
        }
        LOGGER.info("Received certificate data {} but failed to parse", response);
        return null;
    }
}