package io.mosip.hotlist.constant;

/**
 * The Enum AuditEvents - Contains all the events for auditing.
 *
 * @author Manoj SP
 */
public enum AuditEvents {

	BLOCK_REQUEST("HTL-001", "System Event"),

	RETRIEVE_HOTLIST("IDR-002", "System Event"),

	UNBLOCK_HOTLIST("IDR-003", "System Event");

	/** The event id. */
	private final String eventId;

	/** The event type. */
	private final String eventType;

	/**
	 * Instantiates a new audit events.
	 *
	 * @param eventId   the event id
	 * @param eventType the event type
	 */
	private AuditEvents(String eventId, String eventType) {
		this.eventId = eventId;
		this.eventType = eventType;
	}

	/**
	 * Gets the event id.
	 *
	 * @return the event id
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Gets the event type.
	 *
	 * @return the event type
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Gets the event name.
	 *
	 * @return the event name
	 */
	public String getEventName() {
		return this.name();
	}

}
