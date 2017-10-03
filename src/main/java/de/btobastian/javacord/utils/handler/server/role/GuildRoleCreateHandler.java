package de.btobastian.javacord.utils.handler.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

/**
 * Handles the guild role create packet.
 */
public class GuildRoleCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleCreateHandler(DiscordApi api) {
        super(api, true, "GUILD_ROLE_CREATE");
    }

    @Override
    public void handle(JSONObject packet) {

    }

}