package org.javacord.api.event.server.voice;

import org.javacord.api.event.server.ServerEvent;

/**
 * A voice server update event.
 */
public interface VoiceServerUpdateEvent extends ServerEvent {

    /**
     * Gets the token of the event.
     *
     * @return The token of the event.
     */
    String getToken();

    /**
     * Gets the endpoint of the event.
     *
     * @return The endpoint of the event.
     */
    String getEndpoint();

}
