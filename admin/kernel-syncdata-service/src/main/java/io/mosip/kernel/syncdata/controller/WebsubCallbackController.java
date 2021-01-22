package io.mosip.kernel.syncdata.controller;

import io.mosip.kernel.partnercertservice.dto.CACertificateRequestDto;
import io.mosip.kernel.partnercertservice.service.spi.PartnerCertificateManagerService;
import io.mosip.kernel.syncdata.websub.dto.EventModel;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Websub callback implementations
 */
@RestController
public class WebsubCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(WebsubCallbackController.class);
    private static final String CERTIFICATE_DATA = "certificateData";
    private static final String PARTNER_DOMAIN = "partnerDomain";

    @Autowired
    private PartnerCertificateManagerService partnerCertificateManagerService;

    @PostMapping(value = "${syncdata.websub.callback.url.path.ca-cert}", consumes = "application/json")
    @PreAuthenticateContentAndVerifyIntent(secret = "${syncdata.websub.callback.secret.ca-cert}",
            callback = "/v1/syncdata/callback/partner/ca_certificate", topic = "${syncdata.websub.topic.ca-cert}")
    public void handleCACertificate(@RequestBody EventModel eventModel) {
        logger.info("ca_certificate EVENT RECEIVED");
        Map<String, Object> data = eventModel.getEvent().getData();
        CACertificateRequestDto caCertRequestDto = new CACertificateRequestDto();
        if (data.containsKey(CERTIFICATE_DATA) && data.get(CERTIFICATE_DATA) instanceof String) {
            caCertRequestDto.setCertificateData((String) data.get(CERTIFICATE_DATA));
        }
        if (data.containsKey(PARTNER_DOMAIN) && data.get(PARTNER_DOMAIN) instanceof String) {
            caCertRequestDto.setPartnerDomain((String) data.get(PARTNER_DOMAIN));
        }
        partnerCertificateManagerService.uploadCACertificate(caCertRequestDto);
    }

}
