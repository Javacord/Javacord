package de.btobastian.javacord.utils.handler;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

/**
 * This class handles the ready packet.
 */
public class ResumedHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ResumedHandler(ImplDiscordAPI api) {
        super(api, false, "RESUMED");
    }

    @Override
    public void handle(JSONObject packet) {
        // Dummy implementation
    }
}
