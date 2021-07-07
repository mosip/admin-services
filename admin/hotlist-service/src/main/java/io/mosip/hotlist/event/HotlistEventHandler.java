package io.mosip.hotlist.event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.mosip.hotlist.constant.HotlistErrorConstants;
import io.mosip.hotlist.exception.HotlistRetryException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.retry.WithRetry;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;

/**
 * @author Manoj SP
 *
 */
@Component
public class HotlistEventHandler {

	/** The Constant PUBLISH_EVENT. */
	private static final String PUBLISH_EVENT = "publishEvent";

	/** The mosip logger. */
	private static Logger mosipLogger = HotlistLogger.getLogger(HotlistEventHandler.class);

	/** The topic. */
	@Value("${mosip.hotlist.topic-to-publish}")
	private String topic;

	/** The web sub hub url. */
	@Value("${websub.publish.url}")
	private String webSubHubUrl;

	/** The app id. */
	@Value("${spring.application.name:HOTLIST}")
	private String appId;

	/** The publisher. */
	@Autowired
	private PublisherClient<String, EventModel, HttpHeaders> publisher;

	/**
	 * Publish event.
	 *
	 * @param id              the id
	 * @param idType          the id type
	 * @param status          the status
	 * @param expiryTimestamp the expiry timestamp
	 */
	@Async
	@WithRetry
	public void publishEvent(String id, String idType, String status, LocalDateTime expiryTimestamp) {
		try {
			EventModel payload = new EventModel();
			payload.setPublisher(appId);
			payload.setTopic(topic);
			String publishedOn = DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime());
			payload.setPublishedOn(publishedOn);
			Event event = new Event();
			event.setId(UUID.randomUUID().toString());
			event.setTimestamp(publishedOn);
			Map<String, Object> data = new HashMap<>();
			data.put("id", id);
			data.put("idType", idType);
			data.put("status", status);
			data.put("expiryTimestamp",
					Objects.nonNull(expiryTimestamp) ? DateUtils.formatToISOString(expiryTimestamp) : expiryTimestamp);
			event.setData(data);
			payload.setEvent(event);
			mosipLogger.debug(HotlistSecurityManager.getUser(), "HotlistServiceImpl", PUBLISH_EVENT,
					"PUBLISHING EVENT - " + payload.toString());
			publisher.publishUpdate(topic, payload, MediaType.APPLICATION_JSON_VALUE, null, webSubHubUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mosipLogger.error(HotlistSecurityManager.getUser(), "HotlistServiceImpl", "publishEvent",
					"FAILED TO PUBLISH EVENT WITH ERROR - " + e.getMessage());
			throw new HotlistRetryException(HotlistErrorConstants.UNKNOWN_ERROR, e);
		}
	}
}