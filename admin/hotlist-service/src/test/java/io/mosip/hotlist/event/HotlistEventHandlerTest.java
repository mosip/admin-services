package io.mosip.hotlist.event;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.exception.HotlistRetryException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

/**
 * @author Manoj SP
 *
 */
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
public class HotlistEventHandlerTest {

	@InjectMocks
	private HotlistEventHandler handler;

	@Mock
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	@Test
	public void testPublishEvent() {
		handler.publishEvent("id", "idType", "status", DateUtils.getUTCCurrentDateTime());
	}

	@Test
	public void testPublishEventError() {
		try {
			doThrow(new NullPointerException()).when(publisher).publishUpdate(Mockito.any(), Mockito.any(),
					Mockito.any(), Mockito.any(), Mockito.any());
			handler.publishEvent("id", "idType", "status", DateUtils.getUTCCurrentDateTime());
		} catch (HotlistRetryException e) {
			assertTrue(e.getErrorCode().contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorCode()));
			assertTrue(e.getErrorText().contentEquals(HotlistErrorConstants.UNKNOWN_ERROR.getErrorMessage()));
		}
	}
}
