package io.mosip.kernel.syncdata.controller;

import io.mosip.kernel.partnercertservice.constant.PartnerCertManagerErrorConstants;
import io.mosip.kernel.partnercertservice.dto.CACertificateRequestDto;
import io.mosip.kernel.partnercertservice.exception.PartnerCertManagerException;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Websub callback implementations
 */
@RestController
@RequestMapping("/websub/callback")
public class WebsubCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(WebsubCallbackController.class);
    private static final String CERTIFICATE_DATA_SHARE_URL = "certChainDatashareUrl";
    private static final String PARTNER_DOMAIN = "partnerDomain";

    @Autowired
    private PartnerCertificateManagerService partnerCertificateManagerService;

    @Value("#{'${mosip.syncdata.partner.allowed.domains:FTM,DEVICE}'.split(',')}")
    private List<String> partnerAllowedDomains;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/cacert",consumes = "application/json")
    @PreAuthenticateContentAndVerifyIntent(secret = "${syncdata.websub.callback.secret.ca-cert}",
            callback = "/v1/syncdata/websub/callback/cacert",topic = "${syncdata.websub.topic.ca-cert}")
    public void handleCACertificate(@RequestBody EventModel eventModel) {
        logger.debug("ca_certificate EVENT RECEIVED");
        Map<String, Object> data = eventModel.getEvent().getData();

        if (data.containsKey(PARTNER_DOMAIN) && partnerAllowedDomains.contains((String) data.get(PARTNER_DOMAIN)) &&
                data.containsKey(CERTIFICATE_DATA_SHARE_URL)) {
            CACertificateRequestDto caCertRequestDto = new CACertificateRequestDto();
            caCertRequestDto.setPartnerDomain((String) data.get(PARTNER_DOMAIN));
            String certificateData = restTemplate.getForObject((String) data.get(CERTIFICATE_DATA_SHARE_URL), String.class);
            caCertRequestDto.setCertificateData(certificateData);
            try {
                partnerCertificateManagerService.uploadCACertificate(caCertRequestDto);
                logger.info("CA certs sync completed for {}", caCertRequestDto.getPartnerDomain());
            } catch (PartnerCertManagerException ex) {
                if(PartnerCertManagerErrorConstants.INVALID_PARTNER_DOMAIN.getErrorCode().equals(ex.getErrorCode()) ||
                        PartnerCertManagerErrorConstants.CERTIFICATE_EXIST_ERROR.getErrorCode().equals(ex.getErrorCode())) {
                    logger.error("Failed to insert CA cert {}", ex.getErrorText());
                    return;
                }
                logger.error("Failed to insert CA cert {}", ex);
                throw ex;
            }
        }

    }

}
