package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.VoiceServerUpdateEvent;

public class VoiceServerUpdateEventImpl extends ServerEventImpl implements VoiceServerUpdateEvent {

    private String token;
    private String endpoint;

    /**
     * Constructs a new VoiceStateUpdateEventImpl instance.
     *
     * @param server   The server this voice server update is for.
     * @param token    The token provided in this update.
     * @param endpoint The endpoint provided in this update.
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
