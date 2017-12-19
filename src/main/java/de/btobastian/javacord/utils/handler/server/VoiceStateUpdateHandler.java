package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.utils.PacketHandler;

/**
 * Handles the voice state update packet.
 */
public class VoiceStateUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceStateUpdateHandler(DiscordApi api) {
        super(api, true, "VOICE_STATE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        // NOP
    }

}
