package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the presences replace packet.
 */
public class PresencesReplaceHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public PresencesReplaceHandler(DiscordApi api) {
        super(api, true, "PRESENCES_REPLACE");
    }

    @Override
    public void handle(JsonNode packet) {
        // This event is meant for client accounts but also is dispatched for bot account with
        // an empty array. It seems like it's used to replace the presences after an outage.
    }

}