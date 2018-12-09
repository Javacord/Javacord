package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the voice server update packet.
 */
public class VoiceServerUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceServerUpdateHandler(DiscordApi api) {
        super(api, true, "VOICE_SERVER_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        String token = packet.get("token").asText();
        String endpoint = packet.get("endpoint").asText();
        long serverId = packet.get("guild_id").asLong();

        // We need the session id to connect to an audio websocket
        api.getServerById(serverId)
                .map(ServerImpl.class::cast)
                .flatMap(ServerImpl::getPendingAudioConnection)
                .ifPresent(connection -> {
                    connection.setToken(token);
                    connection.setEndpoint(endpoint);
                    connection.tryConnect();
                });
    }
}
