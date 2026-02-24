package io.mosip.kernel.syncdata.test.websub;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.syncdata.websub.PartnerCACertEventSubscriber;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.net.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerCACertEventSubscriberTest {

    @Mock
    private SubscriptionClient<SubscriptionChangeRequest,
            UnsubscriptionRequest,
            SubscriptionChangeResponse> subscribe;

    @InjectMocks
    private PartnerCACertEventSubscriber subscriber;

    @BeforeEach
    void setUp() throws Exception {
        setField("topicName", "test-topic");
        setField("callbackUrl", "http://callback-url");
        setField("callbackSecret", "secret");
        setField("hubUrl", "http://hub-url");
        setField("publisherUrl", "http://publisher-url");
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = PartnerCACertEventSubscriber.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(subscriber, value);
    }

    /**
     * ✅ Test Successful Subscription
     */
    @Test
    void subscribeTopics_success() {

        subscriber.subscribeTopics();

        verify(subscribe, times(1)).subscribe(any(SubscriptionChangeRequest.class));
    }

    /**
     * ✅ Test Subscription Request Values Are Set Properly
     */
    @Test
    void subscribeTopics_requestValuesValidation() {

        ArgumentCaptor<SubscriptionChangeRequest> captor =
                ArgumentCaptor.forClass(SubscriptionChangeRequest.class);

        subscriber.subscribeTopics();

        verify(subscribe).subscribe(captor.capture());

        SubscriptionChangeRequest request = captor.getValue();

        assertEquals("test-topic", request.getTopic());
        assertEquals("http://callback-url", request.getCallbackURL());
        assertEquals("secret", request.getSecret());
        assertEquals("http://hub-url", request.getHubURL());
    }

    /**
     * ✅ Test Exception During Subscription
     */
    @Test
    void subscribeTopics_whenExceptionThrown_shouldNotThrow() {

        doThrow(new RuntimeException("Subscription failed"))
                .when(subscribe).subscribe(any());

        assertDoesNotThrow(() -> subscriber.subscribeTopics());

        verify(subscribe, times(1)).subscribe(any());
    }

    /**
     * ✅ Test Multiple Calls (Scheduler Behavior)
     */
    @Test
    void subscribeTopics_multipleCalls_shouldInvokeEachTime() {

        subscriber.subscribeTopics();
        subscriber.subscribeTopics();

        verify(subscribe, times(2)).subscribe(any());
    }

    /**
     * ✅ Test When Required Fields Are Null
     */
    @Test
    void subscribeTopics_withNullSubscriptionFields_shouldStillCallSubscribe() throws Exception {

        setField("topicName", null);
        setField("callbackUrl", null);
        setField("callbackSecret", null);
        setField("hubUrl", null);

        subscriber.subscribeTopics();

        verify(subscribe, times(1)).subscribe(any());
    }
}
