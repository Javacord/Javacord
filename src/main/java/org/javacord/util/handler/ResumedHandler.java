package org.javacord.util.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.util.gateway.PacketHandler;

/**
 * This class handles the resumed packet.
 */
public class ResumedHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ResumedHandler(DiscordApi api) {
        super(api, false, "RESUMED");
    }

    @Override
    public void handle(JsonNode packet) {
        // NOP
    }

}