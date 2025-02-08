package org.javacord.api.event;

import org.javacord.api.listener.GloballyAttachableListener;

/**
 * Event that is called when a new event is received.
 */
public interface GatewayDispatchEvent extends Event {
    /**
     * Get the type of the received event.
     * @return The received event type.
     */
    String getType();
    /**
     * Get the raw json node of the event payload. It is an instance of FasterXML's JsonNode.
     *
     * @return The event payload.
     */
    Object getPayload();
}
