package io.mosip.kernel.masterdata.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class AddressConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressConfiguration.class);

    private KieServices kieServices = KieServices.Factory.get();

    @Value("${config.server.file.storage.uri}")
    private String configServerFileStorageURL;

    @Value("#{T(java.util.Arrays).asList('${mosip.location.search.file.name:}')}")
    private List<String> droolsRuleFileName;

    @Bean
    public RestTemplate restTemplate()
            throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean
    public KieSession kieSession() throws IOException {
        LOGGER.info("Session created...");
        KieSession kieSession = getKieContainer().newKieSession();
        kieSession.addEventListener(new RuleRuntimeEventListener() {
            @Override
            public void objectInserted(ObjectInsertedEvent event) {
                LOGGER.info("Object inserted \n "
                        + event.getObject().toString());
            }

            @Override
            public void objectUpdated(ObjectUpdatedEvent event) {
                LOGGER.info("Object was updated \n"
                        + "New Content \n"
                        + event.getObject().toString());
            }

            @Override
            public void objectDeleted(ObjectDeletedEvent event) {
                LOGGER.info("Object retracted \n"
                        + event.getOldObject().toString());
            }
        });
        return kieSession;
    }

    @Bean
    public KieContainer getKieContainer() throws IOException {
        LOGGER.info("Container created...");
        getKieRepository();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }

    private KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for (String fileName : droolsRuleFileName) {
            LOGGER.info("loading Drools configuration file - " + fileName);
            kieFileSystem.write("src/main/resources/" + fileName,
                    kieServices.getResources().newReaderResource(
                            new StringReader(getJson(
                                    configServerFileStorageURL, fileName))));
        }
        return kieFileSystem;
    }

    public static String getJson(String configServerFileStorageURL, String uri) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(configServerFileStorageURL + uri, String.class);
    }
}