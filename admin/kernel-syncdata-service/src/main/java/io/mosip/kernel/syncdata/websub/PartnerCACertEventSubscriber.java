package io.mosip.kernel.syncdata.websub;

import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.syncdata.websub.dto.EventModel;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class PartnerCACertEventSubscriber implements ApplicationListener<ApplicationReadyEvent> {

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
    protected PublisherClient<String, EventModel, HttpHeaders> publisher;

    /** The subscribe. */
    @Autowired
    protected SubscriptionClient<SubscriptionChangeRequest, UnsubscriptionRequest, SubscriptionChangeResponse> subscribe;

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * Try register topic partner service events.
     */
    private void tryRegisterTopicPartnerCertEvents() {
        try {
            logger.info("Trying to register topic: {}", topicName);
            publisher.registerTopic(topicName, publisherUrl);
            logger.info("Registered topic: {}", topicName);
        } catch (Exception e) {
            logger.error("Error registering topic: {}", topicName, e);
        }
    }

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
            tryRegisterTopicPartnerCertEvents();
            subscribeForPartnerCertEvent();
            return true;
        } catch (Throwable e) {
            logger.error("Error subscribing topic: {}", topicName, e);
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("onApplicationEvent - scheduling subscription with startup delay: {}", initialSubscriptionDelay);
        taskScheduler.schedule(
                this::initSubsriptions,
                new Date(System.currentTimeMillis() + initialSubscriptionDelay)
        );
        if (reSubscriptionDelaySecs > 0) {
            logger.info("Work around for web-sub notification issue after some time.");
            scheduleRetrySubscriptions();
        } else {
            logger.info("Scheduling for re-subscription is Disabled as the re-subsctription delay value is: "
                            + reSubscriptionDelaySecs);
        }
    }

    private void scheduleRetrySubscriptions() {
        logger.info("Scheduling re-subscription every " + reSubscriptionDelaySecs + " seconds");
        taskScheduler.scheduleAtFixedRate(this::retrySubscriptions, Instant.now().plusSeconds(reSubscriptionDelaySecs),
                Duration.ofSeconds(reSubscriptionDelaySecs));
    }

    private void retrySubscriptions() {
        // Call Init Subscriptions for the count until no error in the subscription.
        // This will execute once first for sure if retry count is 0 or more. If the
        // subscription fails it will retry subscriptions up to given retry count.
        for (int i = 0; i <= retryCount; i++) {
            if (initSubsriptions()) {
                return;
            }
        }
    }
}
