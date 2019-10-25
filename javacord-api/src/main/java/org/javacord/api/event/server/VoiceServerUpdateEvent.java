package org.javacord.api.event.server;

public interface VoiceServerUpdateEvent extends ServerEvent {

    /**
     * Gets the voice token provided in this voice server update.
     *
     * @return The voice token.
     */
    String getToken();

    /**
     * Gets the endpoint provided in this voice server update.
     *
     * @return The endpoint.
     */
    String getEndpoint();

}
