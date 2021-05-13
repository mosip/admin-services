package io.mosip.kernel.masterdata.utils;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;

public class EventPublisherUtil {
	
	private EventPublisherUtil() {}
	
	
	public static EventModel populateEventModel(Object object,String publisher,String topic,String key) {
	Event event = new Event();
	event.setTimestamp(DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime()));
	Map<String, Object> map = new HashedMap<>();
	map.put(key, object);
	event.setData(map);
	EventModel eventModel = new EventModel();
	eventModel.setEvent(event);
	eventModel.setPublisher(publisher);
	eventModel.setTopic(topic);
	return eventModel;
	}

}
