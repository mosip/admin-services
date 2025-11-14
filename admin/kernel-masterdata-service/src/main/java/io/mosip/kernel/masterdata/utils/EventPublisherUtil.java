package io.mosip.kernel.masterdata.utils;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.HashedMap;

import io.mosip.kernel.core.util.DateUtils2;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;

public class EventPublisherUtil {
	
	private EventPublisherUtil() {}
	
	
	public static EventModel populateEventModel(Object object,String publisher,String topic,String key) {
	Event event = new Event();
	String timestamp = DateUtils2.formatToISOString(DateUtils2.getUTCCurrentDateTime());
	event.setTimestamp(timestamp);
	//Send UUID as event id
	String eventId = UUID.randomUUID().toString();
	event.setId(eventId);
	Map<String, Object> map = new HashedMap<>();
	map.put(key, object);
	event.setData(map);
	EventModel eventModel = new EventModel();
	eventModel.setEvent(event);
	eventModel.setPublisher(publisher);
	eventModel.setTopic(topic);
	eventModel.setPublishedOn(timestamp);
	return eventModel;
	}

}
