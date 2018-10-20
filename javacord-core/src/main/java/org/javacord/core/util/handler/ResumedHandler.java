package org.javacord.core.util.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.gateway.PacketHandler;

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
