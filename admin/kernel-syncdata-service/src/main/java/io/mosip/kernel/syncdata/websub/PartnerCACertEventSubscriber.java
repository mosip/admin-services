package io.mosip.kernel.syncdata.websub;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.http.HttpHeaders;


@Component
public class PartnerCACertEventSubscriber /*implements ApplicationListener<ApplicationReadyEvent>*/ {

    private static final Logger logger = LoggerFactory.getLogger(PartnerCACertEventSubscriber.class);

    @Value("${syncdata.websub.resubscription.retry.count:3}")
    private int retryCount;

    @Value("${syncdata.websub.resubscription.delay.secs:0}")
    private int reSubscriptionDelaySecs; //Default is Zero which will disable the scheduling.

    @Value("${subscriptions-delay-on-startup:6000}")
    private int initialSubscriptionDelay;

    @Value("${syncdata.websub.topic.ca-cert}")
    private String topicName;

    @Value("${syncdata.websub.callback.secret.ca-cert}")
    private String callbackSecret;

    @Value("${syncdata.websub.callback.url.ca-cert}")
    private String callbackUrl;

    @Value("${websub.publish.url}")
    private String publisherUrl;

    @Value("${websub.hub.url}")
    private String hubUrl;

    /** The publisher. */
    @Autowired
    protected PublisherClient<String, EventModel, HttpHeaders> publisherClient;

    /** The subscribe. */
    @Autowired
    protected SubscriptionClient<SubscriptionChangeRequest, UnsubscriptionRequest, SubscriptionChangeResponse> subscribe;

    /**
     * Try register topic partner service events.
     */
   /* private void tryRegisterTopicPartnerCertEvents() {
        try {
            logger.info("Trying to register topic: {}", topicName);
            publisherClient.registerTopic(topicName, publisherUrl);
            logger.info("Registered topic: {}", topicName);
        } catch (Exception e) {
            logger.error("Error registering topic: {}", topicName, e);
        }
    }*/

    /**
     * Subscribe for partner service events.
     */
    private void subscribeForPartnerCertEvent() {
        try {
            SubscriptionChangeRequest subscriptionRequest = new SubscriptionChangeRequest();
            subscriptionRequest.setCallbackURL(callbackUrl);
            subscriptionRequest.setHubURL(hubUrl);
            subscriptionRequest.setSecret(callbackSecret);
            subscriptionRequest.setTopic(topicName);
            logger.debug("Trying to subscribe to topic: {} callback-url: {}", topicName, callbackUrl);
            subscribe.subscribe(subscriptionRequest);
            logger.info("Subscribed to topic: {}", topicName);
        } catch (Exception e) {
            logger.error("Error subscribing topic: {}", topicName, e);
            throw e;
        }
    }

    private boolean initSubsriptions() {
        try {
            subscribeForPartnerCertEvent();
            return true;
        } catch (Throwable e) {
            logger.error("Error subscribing topic: {}", topicName, e);
        }
        return false;
    }


    @Scheduled(fixedDelayString = "${syncdata.websub.resubscription.delay.millis}",
            initialDelayString = "${subscriptions-delay-on-startup}")
    public void subscribeTopics() {
        initSubsriptions();
    }
}
