package io.mosip.kernel.syncdata.test.websub;


import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.syncdata.websub.PartnerCACertEventSubscriber;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PartnerCACertEventSubscriberTest {

    @InjectMocks
    private PartnerCACertEventSubscriber subscriber;

    @Mock
    private SubscriptionClient<SubscriptionChangeRequest, UnsubscriptionRequest, SubscriptionChangeResponse> subscribe;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(subscriber, "retryCount", 3);
        ReflectionTestUtils.setField(subscriber, "reSubscriptionDelaySecs", 10);
        ReflectionTestUtils.setField(subscriber, "initialSubscriptionDelay", 1000);
        ReflectionTestUtils.setField(subscriber, "topicName", "ca-cert-topic");
        ReflectionTestUtils.setField(subscriber, "callbackSecret", "secret123");
        ReflectionTestUtils.setField(subscriber, "callbackUrl", "http://localhost/callback");
        ReflectionTestUtils.setField(subscriber, "publisherUrl", "http://localhost/publisher");
        ReflectionTestUtils.setField(subscriber, "hubUrl", "http://localhost/hub");
    }


    @Test
    public void testSubscribeTopicsSuccess() {
        // when
        subscriber.subscribeTopics();
        // then
        ArgumentCaptor<SubscriptionChangeRequest> captor = ArgumentCaptor.forClass(SubscriptionChangeRequest.class);
        verify(subscribe, times(1)).subscribe(captor.capture());
        SubscriptionChangeRequest req = captor.getValue();
        assertNotNull(req);
        assertEquals("http://localhost/callback", req.getCallbackURL());
        assertEquals("http://localhost/hub", req.getHubURL());
        assertEquals("secret123", req.getSecret());
        assertEquals("ca-cert-topic", req.getTopic());
    }

    @Test
    public void testSubscribeTopicsFailureSubscribeThrowsException() {
        // given
        doThrow(new RuntimeException("boom")).when(subscribe).subscribe(any(SubscriptionChangeRequest.class));
        // when
        subscriber.subscribeTopics();
        // then
        verify(subscribe, times(1)).subscribe(any(SubscriptionChangeRequest.class));
    }

}