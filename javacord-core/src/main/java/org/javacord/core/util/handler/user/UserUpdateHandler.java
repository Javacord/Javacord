package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the user update packet.
 */
public class UserUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public UserUpdateHandler(DiscordApi api) {
        super(api, true, "USER_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        // NOP
    }

}