package org.javacord.core.event.server.voice;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.voice.VoiceServerUpdateEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link VoiceServerUpdateEvent}.
 */
public class VoiceServerUpdateEventImpl extends ServerEventImpl implements VoiceServerUpdateEvent {

    /**
     * The token of the event.
     */
    private final String token;

    /**
     * The endpoint of the event.
     */
    private final String endpoint;

    /**
     * Creates a new voice server update event.
     *
     * @param server The server of the event.
     * @param token The token of the event.
     * @param endpoint The endpoint of the event.
     */
    public VoiceServerUpdateEventImpl(Server server, String token, String endpoint) {
        super(server);
        this.token = token;
        this.endpoint = endpoint;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

}
